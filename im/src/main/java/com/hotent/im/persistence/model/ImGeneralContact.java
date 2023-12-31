package com.hotent.im.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.model.BaseModel;


 /**
 * 
 * <pre> 
 * 描述：im_general_contact 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2018-03-23 10:00:00
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class ImGeneralContact extends BaseModel<String>{
	
	/**
	* 常用联系人
	*/
	protected String id; 
	
	/**
	* owner_
	*/
	protected String owner; 
	
	/**
	* account_
	*/
	protected String account; 
	
	/**
	* create_time_
	*/
	protected java.util.Date createTime; 
	
	protected String contactPhoto;
	protected String contactName;
	
	
	
	public String getContactPhoto() {
		return contactPhoto;
	}

	public void setContactPhoto(String contactPhoto) {
		this.contactPhoto = contactPhoto;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 常用联系人
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * 返回 owner_
	 * @return
	 */
	public String getOwner() {
		return this.owner;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	/**
	 * 返回 account_
	 * @return
	 */
	public String getAccount() {
		return this.account;
	}
	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("owner", this.owner) 
		.append("account", this.account) 
		.append("createTime", this.createTime) 
		.toString();
	}
}