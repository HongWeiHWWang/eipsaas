package com.hotent.base.service.impl;

import com.hotent.base.service.DbTypeService;

/**
 * 数据库类型服务的默认实现
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月19日
 */
public class DbTypeServiceImpl implements DbTypeService{
	private String dbType;
	
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	public DbTypeServiceImpl(){}
	
	public DbTypeServiceImpl(String type){
		this.dbType = type;
	}

	@Override
	public String getDbType() {
		return dbType;
	}
}
