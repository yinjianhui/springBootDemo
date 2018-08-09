package com.huisir.springboot.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class ApplicationDemo implements CommandLineRunner{
	

	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationDemo.class, args);
	}

    
	
	/**
	 * 继承CommandLineRunner,复写该方法。当容器启动后会调用该方法。 
	 */
	@Override
	public void run(String... args) throws Exception {
		System.out.println("dddd");
		for (String string : args) {
			System.out.println(string);
		}
	}


}
