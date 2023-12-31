package com.hotent.bo.exception;


/**
 * 超出索引范围异常
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public class OutOfIndexException extends BoBaseException{
	private static final long serialVersionUID = 1L;

	public OutOfIndexException(){
		super();
	}
	
	public OutOfIndexException(String message){
		super(message);
	}
}
