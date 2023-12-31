package com.hotent.bo.exception;

import com.hotent.base.exception.SystemException;

/**
 * 业务对象异常基类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public class BoBaseException extends SystemException{
	private static final long serialVersionUID = 1L;

	public BoBaseException(){
		super();
	}
	
	public BoBaseException(String message){
		super(message);
	}
}
