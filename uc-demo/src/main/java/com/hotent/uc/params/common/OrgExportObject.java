package com.hotent.uc.params.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 副本数据同步获取参数类
 * @author zhangxw
 *
 */
@ApiModel
public class OrgExportObject {

	@ApiModelProperty(name="btime",notes="开始时间")
	private String btime;
	@ApiModelProperty(name="etime",notes="结束时间")
	private String etime;
	
	@ApiModelProperty(name="demCodes",notes="维度编码（多个用“,”号隔开）")
	private String demCodes;
	@ApiModelProperty(name="orgCodes",notes="组织编码（多个用“,”号隔开）")
	private String orgCodes;
	public String getBtime() {
		return btime;
	}
	public void setBtime(String btime) {
		this.btime = btime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public String getDemCodes() {
		return demCodes;
	}
	public void setDemCodes(String demCodes) {
		this.demCodes = demCodes;
	}
	public String getOrgCodes() {
		return orgCodes;
	}
	public void setOrgCodes(String orgCodes) {
		this.orgCodes = orgCodes;
	}
}
