package com.huisir.springboot.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 
 **********************************************************
 * @作者: huisir
 * @日期: 2018年8月27日
 * @描述: 切面
 **********************************************************
 */
@Component
@Aspect
public class Aop {

	@Pointcut("execution(* com.huisir.springboot.demo.aop.TestAopController.test*(..)) && @annotation(com.huisir.springboot.demo.aop.MyAnnotation)" )
	public void addAdvice(){};
	
	@Before("addAdvice()")
	public void test(JoinPoint pp) throws Throwable{
		System.out.println("hello before");
		
	}
	
	@After("addAdvice()")
	public void test2(JoinPoint pj) throws Throwable{
		System.out.println("hello after");
		
	}
	
	@Around("addAdvice()")
	public Object test3(ProceedingJoinPoint pjp){
		
		System.out.println("【注解：Around . 环绕前】方法环绕start.....");
		try {
			Object o =  pjp.proceed();//如果不执行这句，会不执行切面的Before方法及controller的业务方法
			System.out.println("【注解：Around. 环绕后】方法环绕proceed，结果是 :" + o);
			return o;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
		
}
