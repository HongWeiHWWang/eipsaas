package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * 授信异常，禁止访问
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年8月30日
 */
public class CertificateException extends BaseException{
	private static final long serialVersionUID = 1L;

	public CertificateException(){
		super(ResponseErrorEnums.CERT_ERROR);
	}
	
	public CertificateException(String detailMessage){
		super(ResponseErrorEnums.CERT_ERROR, detailMessage);
	}
}
