package com.hotent.uc.exception;

import org.springframework.http.HttpStatus;

/**
 * 应用系统异常(500)
 * @author heyifan
 * @date 2017年6月30日
 */
public class SystemException extends BaseException{
	private static final long serialVersionUID = 1L;
	
	public SystemException(){
		super(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
	
	public SystemException(String message){
		super(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
	}
	
	public SystemException(String message, String shortMessage){
		super(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), message, shortMessage);
	}
	
	public SystemException(HttpStatus status, Integer code, String message, String shortMessage){
		super(status, code, message, shortMessage);
	}
}
