package com.hotent.table.exception;

import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;

/**
 * 数据源异常
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月26日
 */
public class DataSourceException extends BaseException {
	private static final long serialVersionUID = 3148019938789322656L;

	public DataSourceException(){
		super(ResponseErrorEnums.DATASOURCE_ERROR);
	}

	public DataSourceException(String detailMessage){
		super(ResponseErrorEnums.DATASOURCE_ERROR, detailMessage);
	}
}
