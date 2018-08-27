package com.huisir.springboot.demo.interceptor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class AdminUserService {
	
	boolean hashRole(){
		return true;
	}
	
	boolean hashRight(){
		return false;
	}
	
	Set<String> getPermissionSet(){
		Set<String> set = new HashSet<String>();
		return set;
	}
}
