package com.hotent.im.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.model.BaseModel;


 /**
 * 
 * <pre> 
 * 描述：聊天会话 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:44:37
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public class ImMessageSession extends BaseModel<String>{
	
	/**
	* ID
	*/
	protected String id; 
	
	/**
	* 会话编号
	*/
	protected String code; 
	
	/**
	* 会话标题
	*/
	protected String title; 
	
	/**
	* 会话图标
	*/
	protected String icon; 
	
	/**
	* 会话类型
	*/
	protected String scene; 
	
	/**
	* 最后一条文本
	*/
	protected String lastText; 
	
	/**
	* 创建时间
	*/
	protected java.util.Date createTime; 
	
	protected java.util.Date lastTextTime; 
	
	/**
	* 所属人
	*/
	protected String owner; 

	
	
	protected String userId;
	
	protected String userAlias;
	
	protected String userAccount;
	
	protected String userSessionTitle;
	
	protected String ownerName;
	
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	/**
	 * 介绍
	 * @return
	 */
	protected String description;
	
	
	public java.util.Date getLastTextTime() {
		return lastTextTime;
	}

	public void setLastTextTime(java.util.Date lastTextTime) {
		this.lastTextTime = lastTextTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserSessionTitle() {
		return userSessionTitle;
	}

	public void setUserSessionTitle(String userSessionTitle) {
		this.userSessionTitle = userSessionTitle;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 ID
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 返回 会话编号
	 * @return
	 */
	public String getCode() {
		return this.code;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 返回 会话标题
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * 返回 会话图标
	 * @return
	 */
	public String getIcon() {
		return this.icon;
	}
	
	public void setScene(String scene) {
		this.scene = scene;
	}
	
	/**
	 * 返回 会话类型
	 * @return
	 */
	public String getScene() {
		return this.scene;
	}
	
	public void setLastText(String lastText) {
		this.lastText = lastText;
	}
	
	/**
	 * 返回 最后一条文本
	 * @return
	 */
	public String getLastText() {
		return this.lastText;
	}
	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * 返回 所属人
	 * @return
	 */
	public String getOwner() {
		return this.owner;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("code", this.code) 
		.append("title", this.title) 
		.append("icon", this.icon) 
		.append("scene", this.scene) 
		.append("lastText", this.lastText) 
		.append("createTime", this.createTime) 
		.append("owner", this.owner) 
		.toString();
	}
}