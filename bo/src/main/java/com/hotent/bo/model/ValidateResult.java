package com.hotent.bo.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 数据校验错误结果对象
 * @author co
 *
 */
public class ValidateResult {
	

	@ApiModelProperty(value="错误列名")
	protected String columnName; 

	@ApiModelProperty(value="错误信息")
	protected String errorMsg;

	public ValidateResult(String columnName, String errorMsg) {
		super();
		this.columnName = columnName;
		this.errorMsg = errorMsg;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	} 
	
	
}
