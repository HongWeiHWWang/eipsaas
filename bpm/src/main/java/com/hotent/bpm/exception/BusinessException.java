package com.hotent.bpm.exception;

/**
 * 用于业务代码和流程交互是抛出业务异常
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月8日
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -3211477670808757149L;

	public BusinessException(String msg){
		
		super(msg);
	}
	
	public BusinessException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}
}
