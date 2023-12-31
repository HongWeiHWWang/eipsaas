package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * 应用系统异常
 * @author heyifan
 * @date 2017年6月30日
 */
public class SystemException extends BaseException{
	private static final long serialVersionUID = 1L;
	
	public SystemException(){
		super(ResponseErrorEnums.SYSTEM_ERROR);
	}
	
	public SystemException(String detailMessage){
		super(ResponseErrorEnums.SYSTEM_ERROR, detailMessage);
	}
}
