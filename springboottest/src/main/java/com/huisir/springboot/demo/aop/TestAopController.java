package com.huisir.springboot.demo.aop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestAopController {
	
	
	@MyAnnotation
	@RequestMapping("/first")
	public String testController(){
		System.out.println("testController....");
		return "ok";
	}
	
}
