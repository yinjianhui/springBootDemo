package com.huisir.springboot.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.huisir.springboot.demo.config.AQFindoutConfig;

/**
 * 
 **********************************************************
 * @作者: huisir
 * @日期: 2018年8月9日
 * @版权: 2018 www.huisir.com Inc. All rights reserved.
 * @描述:	
 **********************************************************
 */

@SpringBootApplication
public class ApplicationDemo implements CommandLineRunner{
	
	@Autowired
    AQFindoutConfig aQFindoutConfig;
    
	public static void main(String[] args) {
		SpringApplication.run(ApplicationDemo.class, args);
	}

	
	/**
	 * 继承CommandLineRunner,复写该方法。当容器启动后会调用该方法。 
	 */
	@Override
	public void run(String... args) throws Exception {
		
//		System.out.println("dddd");
//		for (String string : args) {
//			System.out.println(string);
//		}
	    
	    aQFindoutConfig.quotaList.forEach(e -> System.out.println(e));
	}


}
