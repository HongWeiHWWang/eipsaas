package com.hotent.bpm.persistence.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 信息盒
 * 
 * @author zxh
 * 
 */
@ApiModel(value="流程按人员统计发起对象")
public class FlowUserCountVo {
	@ApiModelProperty(name="procDefName",notes="流程定义名称")
	public String procDefKey;
	
	@ApiModelProperty(name="procDefName",notes="流程定义名称")
	public String procDefName;
	
	@ApiModelProperty(name="userId",notes="发起人Id")
	public String userId;
	
	@ApiModelProperty(name="userName",notes="发起人姓名")
	public String userName;
	
	@ApiModelProperty(name="avgLong",notes="平均运行时长（小时）")
	public float avgLong;
	
	@ApiModelProperty(name="overtime",notes="逾期工单数")
	public Integer overtime;
	
	@ApiModelProperty(name="closingRate",notes="闭单率（%）")
	public Integer closingRate;

	public String getProcDefName() {
		return procDefName;
	}

	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public float getAvgLong() {
		return avgLong;
	}

	public void setAvgLong(float avgLong) {
		this.avgLong = avgLong;
	}

	public Integer getOvertime() {
		return overtime;
	}

	public void setOvertime(Integer overtime) {
		this.overtime = overtime;
	}

	public Integer getClosingRate() {
		return closingRate;
	}

	public void setClosingRate(Integer closingRate) {
		this.closingRate = closingRate;
	}

	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

	
}
