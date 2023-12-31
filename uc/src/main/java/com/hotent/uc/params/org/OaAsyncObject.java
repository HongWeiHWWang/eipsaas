package com.hotent.uc.params.org;

import io.swagger.annotations.ApiModelProperty;


public class OaAsyncObject {

	@ApiModelProperty(name="type",notes="同步类型：all（全量），add（增量）")
	private String type;
	
	@ApiModelProperty(name="time",notes="同步时间，当type为‘add’时生效，如果不传，则取系统上一次同步记录的时间")
	private String time;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
