package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * 服务器拒绝异常
 * @author heyifan
 * @date 2017年6月30日
 */
public class ServerRejectException extends BaseException{
	private static final long serialVersionUID = 1L;
	
	public ServerRejectException(){
		super(ResponseErrorEnums.NO_PERMISSION);
	}
	
	public ServerRejectException(String detailMessage){
		super(ResponseErrorEnums.NO_PERMISSION, detailMessage);
	}
}
