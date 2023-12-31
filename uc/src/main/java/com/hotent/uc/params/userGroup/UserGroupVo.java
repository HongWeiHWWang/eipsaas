package com.hotent.uc.params.userGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="群组视图对象")
public class UserGroupVo {
	
	@ApiModelProperty(name="name",notes="群组名称",required=true)
	private String name;
	
	@ApiModelProperty(name="code",notes="群组代码",required=true)
	private String code;
	
	@ApiModelProperty(name="description",notes="群组描述")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		return "{"
				+ "\""+"name"+"\""+":"+"\""+this.name+"\","
				+"\""+"code"+"\""+":"+"\""+this.code+"\","
				+"\""+"description"+"\""+":"+"\""+this.description+"\""
				+ "}";
	}

}
