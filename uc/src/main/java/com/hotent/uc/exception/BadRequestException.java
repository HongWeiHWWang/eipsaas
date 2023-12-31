package com.hotent.uc.exception;

import org.springframework.http.HttpStatus;

/**
 * 坏请求(400)
 * @author heyifan
 * @date 2017年6月30日
 */
public class BadRequestException extends BaseException{
	private static final long serialVersionUID = 1L;
	
	public BadRequestException(){
		super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
	}
	
	public BadRequestException(String message){
		super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), message);
	}
	
	public BadRequestException(String message, String shortMessage){
		super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), message, shortMessage);
	}
	
	public BadRequestException(HttpStatus status, Integer code, String message, String shortMessage){
		super(status, code, message, shortMessage);
	}
}
