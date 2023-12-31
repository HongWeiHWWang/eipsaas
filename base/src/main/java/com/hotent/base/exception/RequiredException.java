package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * 必填异常
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年6月29日
 */
public class RequiredException extends BaseException {
	
	private static final long serialVersionUID = 1L;
	
	public RequiredException(){
		super(ResponseErrorEnums.REQUIRED_ERROR);
	}
	
	public RequiredException(String detailMessage){
		super(ResponseErrorEnums.REQUIRED_ERROR, detailMessage);
	}
}
