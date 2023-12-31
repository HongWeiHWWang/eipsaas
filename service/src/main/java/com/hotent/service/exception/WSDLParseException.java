package com.hotent.service.exception;

import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;

/**
 * WSDL文档解析异常
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public class WSDLParseException extends BaseException {
	private static final long serialVersionUID = 1L;

	public WSDLParseException(){
		super(ResponseErrorEnums.WEBSERVICE_PARSE_ERROR);
	}
	
	public WSDLParseException(String message){
		super(ResponseErrorEnums.WEBSERVICE_PARSE_ERROR, message);
	}
}