package com.huisir.springboot.demo.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 **********************************************************
 * @作者: huisir
 * @日期: 2018年9月3日
 * @描述:	AOP技术，编写一个全局的异常处理切面类，用它来统一处理所有的异常行为

	利用 @ControllerAdvice + @ExceptionHandler 组合处理Controller层RuntimeException异常
 **********************************************************
 */
@ControllerAdvice   // 控制器增强
@ResponseBody
public class ExceptionAspect {

    private static final Logger log = Logger.getLogger(ExceptionAspect.class);

    /**
     * 400 - Bad Request
     */
    //要返回的http状态码
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //表示处理这类异常 HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response handleHttpMessageNotReadableException(
    		//将这个异常传进来
            HttpMessageNotReadableException e) {
        log.error("could_not_read_json...", e);
        return new Response().failure("could_not_read_json");
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Response handleValidationException(MethodArgumentNotValidException e) {
        log.error("parameter_validation_exception...", e);
        return new Response().failure("parameter_validation_exception");
    }

    /**
     * 405 - Method Not Allowed。HttpRequestMethodNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.error("request_method_not_supported...", e);
        return new Response().failure("request_method_not_supported");
    }
    /**
     * 415 - Unsupported Media Type。HttpMediaTypeNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    public Response handleHttpMediaTypeNotSupportedException(Exception e) {
        log.error("content_type_not_supported...", e);
        return new Response().failure("content_type_not_supported");
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response handleTokenException(Exception e) {
        log.error("Token is invaild...", e);
        return new Response().failure("Token is invaild");
    }


}
