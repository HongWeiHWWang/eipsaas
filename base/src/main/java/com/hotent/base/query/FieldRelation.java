package com.hotent.base.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询条件之间的组合关系
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月4日
 */
@ApiModel(description="查询字段间的组合关系")
public enum FieldRelation {
	@ApiModelProperty(name="AND",notes="并且")
	AND("AND"),
	@ApiModelProperty(name="OR",notes="或者")
	OR("OR");
	
	private String val;
	FieldRelation(String _val) {
		val = _val;
	}
	public String value(){
		return val;
	}
}
