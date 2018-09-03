package com.huisir.springboot.demo.exception;

import org.apache.log4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huisir.springboot.demo.interceptor.PermissionConstants;
import com.huisir.springboot.demo.interceptor.RequiredPermission;
import com.huisir.springboot.demo.service.TestApplicationRunnerService;


/**
 * 
 **********************************************************
 * @作者: huisir
 * @日期: 2018年9月3日
 * @描述: //该cont测试统一异常处理切面是否起作用
 **********************************************************
 */
@RestController
@RequestMapping("/users")
//防止被拦截器拦截，就访问不到这个controller了
@RequiredPermission(value = PermissionConstants.ADMIN_PRODUCT_DETAIL)
public class UserController {

    private TestApplicationRunnerService service;

    private static final Logger log = Logger.getLogger(UserController.class);


    @RequestMapping(value = "/user")
    public User addUser() {  // 将接收到的HTTP消息转化为Java对象
    	//service.addUser(user);
    	System.out.println("fangfa 执行");
    	
        throw new HttpMessageNotReadableException("dd");
       // return null;
    }
}