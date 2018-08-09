package com.huisir.springboot.demo.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
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
@Order(1)
public class TestCommandLineRunnerService implements CommandLineRunner{
	String get(){
		return "123";
	}
	
	/**
	 * CommandLineRunner，容器初始化后会调用该方法。其中order注解表示执行的顺讯，相较于其他同样继承了该接口的方法。值越低，越先执行。接受main方法启动的参数 
	 */
	@Override
	public void run(String... args) throws Exception {
		System.out.println(args);
	}

}
