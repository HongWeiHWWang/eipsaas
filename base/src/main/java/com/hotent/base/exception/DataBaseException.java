package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * 数据源异常
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月4日
 */
public class DataBaseException extends BaseException {
	private static final long serialVersionUID = 3148019938789322656L;

	public DataBaseException(){
		super(ResponseErrorEnums.DATABASE_ERROR);
	}
	
	public DataBaseException(String detailMessage){
		super(ResponseErrorEnums.DATABASE_ERROR, detailMessage);
	}
}
