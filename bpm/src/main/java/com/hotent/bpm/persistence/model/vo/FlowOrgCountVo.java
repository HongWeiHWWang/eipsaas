package com.hotent.bpm.persistence.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 信息盒
 * 
 * @author zxh
 * 
 */
@ApiModel(value="流程按部门统计发起对象")
public class FlowOrgCountVo {
	
	@ApiModelProperty(name="procDefName",notes="流程定义名称")
	public String procDefKey;
	
	@ApiModelProperty(name="procDefName",notes="流程定义名称")
	public String procDefName;
	
	@ApiModelProperty(name="instances",notes="工单数量")
	public Integer instances;
	
	@ApiModelProperty(name="hourLong",notes="运行时长（小时）")
	public float hourLong;
	
	@ApiModelProperty(name="incomplete",notes="未完成工单")
	public Integer incomplete;

	public String getProcDefName() {
		return procDefName;
	}

	public Integer getInstances() {
		return instances;
	}

	public void setInstances(Integer instances) {
		this.instances = instances;
	}

	public float getHourLong() {
		return hourLong;
	}

	public void setHourLong(float hourLong) {
		this.hourLong = hourLong;
	}

	public Integer getIncomplete() {
		return incomplete;
	}

	public void setIncomplete(Integer incomplete) {
		this.incomplete = incomplete;
	}

	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}

	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

}
