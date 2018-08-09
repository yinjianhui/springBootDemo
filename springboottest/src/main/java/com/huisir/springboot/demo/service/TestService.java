package com.huisir.springboot.demo.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(1)
public class TestService implements CommandLineRunner{
	String get(){
		return "123";
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("!!!!!!!!!!!!!!!");
		
	}

}
