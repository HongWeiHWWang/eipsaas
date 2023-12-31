package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * 坏请求
 * @author heyifan
 * @date 2017年6月30日
 */
public class BadRequestException extends BaseException{
	private static final long serialVersionUID = 1L;
	
	public BadRequestException(){
		super(ResponseErrorEnums.ILLEGAL_ARGUMENT);
	}
	
	public BadRequestException(String detailMessage){
		super(ResponseErrorEnums.ILLEGAL_ARGUMENT, detailMessage);
	}
}
