package com.hotent.uc.exception;

import org.springframework.http.HttpStatus;

/**
 * 服务器拒绝异常(403)
 * @author heyifan
 * @date 2017年6月30日
 */
public class ServerRejectException extends BaseException{
	private static final long serialVersionUID = 1L;
	
	public ServerRejectException(){
		super(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value());
	}
	
	public ServerRejectException(String message){
		super(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), message);
	}
	
	public ServerRejectException(String message, String shortMessage){
		super(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), message, shortMessage);
	}
	
	public ServerRejectException(HttpStatus status, Integer code, String message, String shortMessage){
		super(status, code, message, shortMessage);
	}
}
