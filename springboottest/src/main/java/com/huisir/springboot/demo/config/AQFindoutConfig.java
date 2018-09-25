package com.huisir.springboot.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * 
 * @author huisir
 *
 */
@Configuration
@Service
public class AQFindoutConfig {
	
    @Value("#{'${monitor.cdq.aqfindout.quotaSpecialFindOutList}'.split(',')}")


	//@Value("${quotaSpecialFindOutList}.split(',')}")
	public List<String> quotaList;
	
}
