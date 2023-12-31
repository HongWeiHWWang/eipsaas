package com.hotent.uc.params.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户或组织参数
 * @author zhangxw
 *
 */
@ApiModel
public class ParamObject {

	@ApiModelProperty(name="alias",notes="参数别名",required=true)
	private String alias;
	
	@ApiModelProperty(name="value",notes="参数值")
	private String value;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "{"
				+ "\""+"alias"+"\""+":"+"\""+this.alias+"\","
				+"\""+"value"+"\""+":"+"\""+this.value+"\""
				+ "}";
	}
	
}
