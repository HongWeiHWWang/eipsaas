package com.hotent.uc.params.post;


import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author liangqf
 * <pre>人员岗位有效期</pre>
 *
 */
public class PostDueVo {
	
	@ApiModelProperty(name="code",notes="岗位代码",required=true)
	private String code;
	
	@ApiModelProperty(name="account",notes="用户帐号",required=true)
	private String account;
	
	@ApiModelProperty(name="startDate",notes="开始日期，日期格式yyyy-MM-dd hh:mm:ss或yyyy-MM-dd，开始/结束日期至少需要填写一个")
	protected String startDate;
	
	@ApiModelProperty(name="endDate",notes="结束日期，日期格式yyyy-MM-dd hh:mm:ss或yyyy-MM-dd，开始/结束日期至少需要填写一个")
	protected String endDate;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String toString() {
		return "{"
				+ "\""+"account"+"\""+":"+"\""+this.account+"\","
				+"\""+"code"+"\""+":"+"\""+this.code+"\","
				+"\""+"startDate"+"\""+":"+"\""+this.startDate+"\","
				+"\""+"endDate"+"\""+":"+"\""+this.endDate+"\""
				+ "}";
	}

}
