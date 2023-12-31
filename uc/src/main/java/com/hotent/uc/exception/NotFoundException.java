package com.hotent.uc.exception;

import org.springframework.http.HttpStatus;

/**
 * 未找到资源(404)
 * @author heyifan
 * @date 2017年6月30日
 */
public class NotFoundException extends BaseException{
	private static final long serialVersionUID = 1L;
	
	public NotFoundException(){
		super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value());
	}
	
	public NotFoundException(String message){
		super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), message);
	}
	
	public NotFoundException(String message, String shortMessage){
		super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), message, shortMessage);
	}
	
	public NotFoundException(HttpStatus status, Integer code, String message, String shortMessage){
		super(status, code, message, shortMessage);
	}
}
