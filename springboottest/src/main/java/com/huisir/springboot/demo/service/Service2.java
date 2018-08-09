package com.huisir.springboot.demo.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class Service2 implements ApplicationRunner{

	@Override
	public void run(ApplicationArguments args) throws Exception {
		for (String ele : args.getSourceArgs()) {
			
			System.out.println("args:" + ele );
		}
		
	}
	
	
}
