package com.huisir.springboot.demo.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * 
 **********************************************************
 * @作者: huisir
 * @日期: 2018年8月9日
 * @版权: 2018 www.huisir.com Inc. All rights reserved.
 * @描述:
 **********************************************************
 */
@Service
public class TestApplicationRunnerService implements ApplicationRunner{
	
	/**
	 * 继承ApplicationRunner,复写run()方法后,spring容器启动后会执行该方法。接受main方法启动的参数 
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		for (String ele : args.getSourceArgs()) {
			
			System.out.println("args:" + ele );
		}
		
	}
	
	
}
