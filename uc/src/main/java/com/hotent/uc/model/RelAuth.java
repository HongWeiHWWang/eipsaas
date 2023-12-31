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
 * 描述：分级组织管理 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-20 14:30:28
 * 版权：广州宏天软件有限公司
 * </pre>
 */

@TableName("UC_REL_AUTH")
@ApiModel(description="分级组织管理")
public class RelAuth extends UcBaseModel<RelAuth> {

	private static final long serialVersionUID = 6155180992784105371L;
	
	/**
	* ID_
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="分级组织id")
	protected String id; 
	
	/**
	* 汇报线管理员id
	*/
	@TableField("USER_ID_")
	@ApiModelProperty(name="userId",notes="汇报线管理员id")
	protected String userId; 
	
	/**
	* 汇报线节点id
	*/
	@TableField("REL_ID_")
	@ApiModelProperty(name="relId",notes="汇报线节点id")
	protected String relId; 
	
	/**
	* 汇报线分类id
	*/
	@TableField("TYPE_ID_")
	@ApiModelProperty(name="typeId",notes="汇报线分类id")
	protected String typeId; 
	
	/**
	 * 汇报线管理员姓名
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="account",notes="汇报线管理员账号")
	protected String account;
	
	/**
	 * 汇报线管理员姓名
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="fullname",notes="汇报线管理员姓名")
	protected String fullname;
	
	/**
	 * 汇报线节点名称
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="relName",notes="汇报线节点名称")
	protected String relName;
	
	/**
	 * 汇报线分类名称
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="typeName",notes="汇报线分类名称")
	protected String typeName;
	
	/**
	 * 节点路径
	 */
	@TableField(exist=false)
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
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

}
