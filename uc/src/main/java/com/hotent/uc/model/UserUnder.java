package com.hotent.uc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


/**
* 
* <pre> 
* 描述：下属管理 实体对象
* 构建组：x5-bpmx-platform
* 作者:liyg
* 邮箱:liyg@jee-soft.cn
* 日期:2017-07-25 09:24:29
* 版权：广州宏天软件有限公司
* </pre>
*/
@TableName("UC_USER_UNDER")
@ApiModel(description="下属管理 ")
public class UserUnder extends UcBaseModel<UserUnder>  {

	private static final long serialVersionUID = -2703708208120760021L;
	/**
	* ID_
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="下属管理id")
	protected String id; 
	
	/**
	* 用户id
	*/
	@TableField("USER_ID_")
	@ApiModelProperty(name="userId",notes="用户id")
	protected String userId; 
	
	/**
	* 下属用户id
	*/
	@TableField("UNDER_USER_ID_")
	@ApiModelProperty(name="underUserId",notes="下属用户id")
	protected String underUserId; 
	
	/**
	* 下属用户名
	*/
	@TableField("UNDER_USER_NAME_")
	@ApiModelProperty(name="underUserName",notes="下属用户名")
	protected String underUserName; 
	
	@TableField("ORG_ID_")
	@ApiModelProperty(name="orgId",notes="组织id")
	protected String orgId;
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 ID_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 返回 用户id
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	public void setUnderUserId(String underUserId) {
		this.underUserId = underUserId;
	}
	
	/**
	 * 返回 下属用户id
	 * @return
	 */
	public String getUnderUserId() {
		return this.underUserId;
	}
	
	public void setUnderUserName(String underUserName) {
		this.underUserName = underUserName;
	}
	
	/**
	 * 返回 下属用户名
	 * @return
	 */
	public String getUnderUserName() {
		return this.underUserName;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("userId", this.userId) 
		.append("underUserId", this.underUserId) 
		.append("underUserName", this.underUserName) 
		.append("orgId",this.orgId)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

}
