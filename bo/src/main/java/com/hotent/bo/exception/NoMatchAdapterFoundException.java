package com.hotent.bo.exception;


/**
 * 未找到匹配的适配器异常
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public class NoMatchAdapterFoundException extends BoBaseException{
	private static final long serialVersionUID = 1L;

	public NoMatchAdapterFoundException(){
		super();
	}
	
	public NoMatchAdapterFoundException(String message){
		super(message);
	}
}
