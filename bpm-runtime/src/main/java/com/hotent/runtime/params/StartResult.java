package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.hotent.base.model.CommonResult;

/**
 * 启动流程的结果
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="启动流程的结果",parent=CommonResult.class)
public class StartResult extends CommonResult{
	@ApiModelProperty(name="message",notes="流程实例ID",example="10000000000001",required=true)
	private String instId;
	
	public StartResult(String instId){
		this.instId = instId;
	}
	
	public StartResult(String message, String instId){
		super(message);
		this.instId = instId;
	}
	
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
}
