package com.hotent.bpm.persistence.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.hotent.base.util.BeanUtils;
import com.hotent.uc.api.model.IUser;

/**
 * 流程任务处理人
 * @author heyifan
 */
@ApiModel(description="流程任务处理人", value="流程任务处理人")
public class BpmIdentityResult {
	@ApiModelProperty(name="id",notes="处理人ID",example="10000000000001",required=true)
	protected String id;
	@ApiModelProperty(name="account",notes="处理人账号",example="admin",required=true)
	protected String account;
	@ApiModelProperty(name="name",notes="处理人名称",example="管理员",required=true)
	protected String name;
	
	public BpmIdentityResult(IUser user){
		if(BeanUtils.isEmpty(user)){
			throw new RuntimeException("传入的用户为空");
		}
		this.id = user.getUserId();
		this.account = user.getAccount();
		this.name = user.getFullname();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
