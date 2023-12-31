package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * 未找到资源
 * @author heyifan
 * @date 2017年6月30日
 */
public class NotFoundException extends BaseException{
	private static final long serialVersionUID = 1L;
	
	public NotFoundException(){
		super(ResponseErrorEnums.NOT_FOUND);
	}
	
	public NotFoundException(String detailMessage){
		super(ResponseErrorEnums.NOT_FOUND, detailMessage);
	}
}
