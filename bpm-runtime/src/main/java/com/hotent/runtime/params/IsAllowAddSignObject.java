package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 是否允许任务转办、加签参数对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="是否允许任务转办、加签参数对象")
public class IsAllowAddSignObject {
	
	@ApiModelProperty(name="taskId",notes="任务id",required=true)
	private String taskId;
	
	@ApiModelProperty(name="userId",notes="用户id（用户id和账号只需要任意填一个）")
	private String userId;
	
	@ApiModelProperty(name="account",notes="用户账号（用户id和账号只需要任意填一个）")
	private String account;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
