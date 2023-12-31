package com.hotent.uc.model;

import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * 
 * <pre> 
 * 描述：分级组织管理 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-20 14:30:28
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class RelAuth extends UcBaseModel {

	private static final long serialVersionUID = 6155180992784105371L;
	
	/**
	* ID_
	*/
	@ApiModelProperty(name="id",notes="分级组织id")
	protected String id; 
	
	/**
	* 汇报线管理员id
	*/
	@ApiModelProperty(name="userId",notes="汇报线管理员id")
	protected String userId; 
	
	/**
	* 汇报线节点id
	*/
	@ApiModelProperty(name="relId",notes="汇报线节点id")
	protected String relId; 
	
	/**
	* 汇报线分类id
	*/
	@ApiModelProperty(name="typeId",notes="汇报线分类id")
	protected String typeId; 
	
	/**
	 * 汇报线管理员姓名
	 */
	@ApiModelProperty(name="account",notes="汇报线管理员账号")
	protected String account;
	
	/**
	 * 汇报线管理员姓名
	 */
	@ApiModelProperty(name="fullname",notes="汇报线管理员姓名")
	protected String fullname;
	
	/**
	 * 汇报线节点名称
	 */
	@ApiModelProperty(name="relName",notes="汇报线节点名称")
	protected String relName;
	
	/**
	 * 汇报线分类名称
	 */
	@ApiModelProperty(name="typeName",notes="汇报线分类名称")
	protected String typeName;
	
	/**
	 * 节点路径
	 */
	@ApiModelProperty(name="relPath",notes="节点路径")
	protected String relPath;
	
	
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
	
	

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/**
	 * 返回 分级组织管理员id
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}

	

	public String getRelId() {
		return relId;
	}

	public void setRelId(String relId) {
		this.relId = relId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getRelName() {
		return relName;
	}

	public void setRelName(String relName) {
		this.relName = relName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getRelPath() {
		return relPath;
	}

	public void setRelPath(String relPath) {
		this.relPath = relPath;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("userId", this.userId) 
		.append("relId", this.relId) 
		.append("typeId", this.typeId) 
		.append("account",this.account)
		.append("fullname",this.fullname)
		.append("relName",this.relName)
		.append("typeName",this.typeName)
		.append("relPath",this.relPath)
		.append("updateTime",this.updateTime)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

}
