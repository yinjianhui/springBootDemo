package com.huisir.springboot.demo.redis.lock;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.huisir.springboot.demo.redis.config.RedisUtil;


/**
 * 
 **********************************************************
 * @作者: http://blueskykong.com/2018/01/06/redislock/   huisir
 * @日期: 2018年9月10日
 * @描述:
 **********************************************************
基于redis的分布式锁实现
SETNX
使用redis的SETNX实现分布式锁，多个进程执行以下Redis命令：

SETNX lock.id <current Unix time + lock timeout + 1>
SETNX是将 key 的值设为 value，当且仅当 key 不存在。若给定的 key 已经存在，则 SETNX 不做任何动作。

返回1，说明该进程获得锁，SETNX将键 lock.id 的值设置为锁的超时时间，当前时间 +加上锁的有效时间。
返回0，说明其他进程已经获得了锁，进程不能进入临界区。进程可以在一个循环中不断地尝试 SETNX 操作，以获得锁。
存在死锁的问题
SETNX实现分布式锁，可能会存在死锁的情况。与单机模式下的锁相比，分布式环境下不仅需要保证进程可见，还需要考虑进程与锁之间的网络问题。某个线程获取了锁之后，断开了与Redis 的连接，锁没有及时释放，竞争该锁的其他线程都会hung，产生死锁的情况。

在使用 SETNX 获得锁时，我们将键 lock.id 的值设置为锁的有效时间，线程获得锁后，其他线程还会不断的检测锁是否已超时，如果超时，等待的线程也将有机会获得锁。然而，锁超时，我们不能简单地使用 DEL 命令删除键 lock.id 以释放锁。

考虑以下情况:

A已经首先获得了锁 lock.id，然后线A断线。B,C都在等待竞争该锁；
B,C读取lock.id的值，比较当前时间和键 lock.id 的值来判断是否超时，发现超时；
B执行 DEL lock.id命令，并执行 SETNX lock.id 命令，并返回1，B获得锁；
C由于各刚刚检测到锁已超时，执行 DEL lock.id命令，将B刚刚设置的键 lock.id 删除，执行 SETNX lock.id命令，并返回1，即C获得锁。
上面的步骤很明显出现了问题，导致B,C同时获取了锁。在检测到锁超时后，线程不能直接简单地执行 DEL 删除键的操作以获得锁。

对于上面的步骤进行改进，问题是出在删除键的操作上面，那么获取锁之后应该怎么改进呢？
首先看一下redis的GETSET这个操作，GETSET key value，将给定 key 的值设为 value ，并返回 key 的旧值(old value)。利用这个操作指令，我们改进一下上述的步骤。

A已经首先获得了锁 lock.id，然后线A断线。B,C都在等待竞争该锁；
B,C读取lock.id的值，比较当前时间和键 lock.id 的值来判断是否超时，发现超时；
B检测到锁已超时，即当前的时间大于键 lock.id 的值，B会执行
GETSET lock.id <current Unix timestamp + lock timeout + 1>设置时间戳，通过比较键 lock.id 的旧值是否小于当前时间，判断进程是否已获得锁；
B发现GETSET返回的值小于当前时间，则执行 DEL lock.id命令，并执行 SETNX lock.id 命令，并返回1，B获得锁；
C执行GETSET得到的时间大于当前时间，则继续等待。
在线程释放锁，即执行 DEL lock.id 操作前，需要先判断锁是否已超时。如果锁已超时，那么锁可能已由其他线程获得，这时直接执行 DEL lock.id 操作会导致把其他线程已获得的锁释放掉。 


 */

@Service
public class RedisdistributionLock {
	
	public static Logger logger = LoggerFactory.getLogger(RedisdistributionLock.class);
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate; 
	
	@Autowired
	private RedisUtil redisUtil;
	
	private static String LOCK_PREFIX = "prefix";
	String key = LOCK_PREFIX + "key";

	public static int sleepTime = 10000;
	
	public static int timeout = 8000;//redis 掉线后重新连接的超时时间,判断获取锁的线程是正常，如果不正常，锁不会自动释放，就会死锁，因此要判断持有锁的线程是否挂掉。
	
	ReentrantLock threadLock = new ReentrantLock();

	//限时看是否获得锁
	public boolean lock(long acquireTimeout, TimeUnit timeUnit) throws InterruptedException {
	    acquireTimeout = timeUnit.toMillis(acquireTimeout);
	    long acquireTime = acquireTimeout + System.currentTimeMillis();
	    //使用J.U.C的ReentrantLock
	    threadLock.tryLock(acquireTimeout, timeUnit);
	    try {
	    	//循环尝试
	        while (true) {
	        	//调用tryLock
	            boolean hasLock = tryLock();
	            if (hasLock) {
	                //获取锁成功
	                return true;
	            } else if (acquireTime < System.currentTimeMillis()) {
	                break;
	            }
	            Thread.sleep(sleepTime);
	        }
	    } finally {
	        if (threadLock.isHeldByCurrentThread()) {
	            threadLock.unlock();
	        }
	    }
	    return false;
	}
	public boolean tryLock() {
	    long currentTime = System.currentTimeMillis();
	    final String expires = String.valueOf(timeout + currentTime);
	    //设置互斥量
	    if(redisTemplate.execute(connection ->{
	    	return connection.setNX(key.getBytes(), expires.getBytes());
	    })){
	    	//获取锁，设置超时时间
	    	redisUtil.expire(key, Long.valueOf(expires));
	        return true;
	    } else {
	    	//key已经存在
	        String currentLockTime = (String) redisUtil.get(key);
	        //检查锁是否超时
	        //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
	        if (Objects.nonNull(currentLockTime) && Long.parseLong(currentLockTime) < System.currentTimeMillis()) {
	            //获取旧的锁时间并设置互斥量
	        	//获取上一个锁到期时间，并设置现在的锁到期时间，
                //只有一个线程才能获取上一个线上的设置时间，因为getSet是同步的
	            String oldLockTime = (String) redisTemplate.opsForValue().getAndSet(key, expires);
	            		
	            //旧值与当前时间比较
	            if (Objects.nonNull(oldLockTime) && Objects.equals(oldLockTime, currentLockTime)) {
	            	//如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
	            	//获取锁，设置超时时间
	            	redisUtil.expire(key, Long.valueOf(expires));
	                return true;
	            }
	        }
	        return false;
	    }
	}
	
	public boolean unlock() {
	    //只有锁的持有线程才能解锁
//		if (lockHolder == Thread.currentThread()) {
	    if (threadLock.isHeldByCurrentThread()) {
	        //判断锁是否超时，没有超时才将互斥量删除
	        if (timeout > System.currentTimeMillis()) {
	            redisUtil.del(key);
	            logger.info("删除互斥量[{}]", key);
	        }
	        //lockHolder = null;
	        logger.info("释放[{}]锁成功", key);
	        return true;
	    } else {
	        throw new IllegalMonitorStateException("没有获取到锁的线程无法执行解锁操作");
	    }
	}
	
	
	
	public static void main(String[] args) {
		
		RedisdistributionLock lock = new RedisdistributionLock();
        try {
            if (lock.lock(8, TimeUnit.SECONDS)) { // 启用锁
                //执行业务逻辑
            } else {
                logger.info("The time wait for lock more than [{}] ms ");
            }
        } catch (Throwable t) {
            // 分布式锁异常
            logger.warn(t.getMessage(), t);
        } finally {
            if (lock != null) {
                try {
                    lock.unlock();// 则解锁
                } catch (Exception e) {
                }
            }
            if (lock != null) {
                try {
                    //pool.returnResource(jedis);// 还到连接池里
                } catch (Exception e) {
                }
            }
        }
	}
}

