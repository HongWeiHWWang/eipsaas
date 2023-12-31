package com.hotent.uc.params.org;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author liangqf
 * <pre>组织岗位视图对象</pre>
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgPostVo {
	
	@ApiModelProperty(name="orgCode",notes="组织代码",required=true)
	private String orgCode;
	
	@ApiModelProperty(name="jobCode",notes="职务代码",required=true)
	private String jobCode;
	
	@ApiModelProperty(name="name",notes="岗位名称",required=true)
	private String name;
	
	@ApiModelProperty(name="code",notes="岗位代码",required=true)
	private String code;
	
	@ApiModelProperty(name="isCharge",notes="是否主岗位 0：不是（默认）；1：是",example="0")
	private int isCharge;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

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

	public int getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(int isCharge) {
		this.isCharge = isCharge;
	}
	
	public String toString() {
		return "{"
				+ "\""+"name"+"\""+":"+"\""+this.name+"\","
				+"\""+"code"+"\""+":"+"\""+this.code+"\","
				+"\""+"orgCode"+"\""+":"+"\""+this.orgCode+"\","
				+"\""+"jobCode"+"\""+":"+"\""+this.jobCode+"\","
				+"\""+"isCharge"+"\""+":"+"\""+this.isCharge+"\""
				+ "}";
	}

}
