package com.hotent.uc.exception;

import org.springframework.http.HttpStatus;

/**
 * 必填
 * @author liangqf 2017-09-05
 *
 */
public class RequiredException extends BaseException {
	
	private static final long serialVersionUID = 1L;
	
	public RequiredException(String message){
		super(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
	}
	
	public RequiredException(String message, String shortMessage){
		super(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), message,shortMessage);
	}

}
