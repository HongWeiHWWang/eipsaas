package com.hotent.base.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 排序对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月4日
 */
@ApiModel(description="排序对象")
public enum Direction {
	@ApiModelProperty(name="ASC",notes="升序")
	ASC,
	@ApiModelProperty(name="DESC",notes="降序")
	DESC;

	public static Direction fromString(String value) {
		try {
			return Direction.valueOf(value.toUpperCase());
		} catch (Exception e) {
			return ASC;
		}
	}
}
