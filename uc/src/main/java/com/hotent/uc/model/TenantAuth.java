package com.hotent.uc.model;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;


 /**
 * UC_TENANT_AUTH
 * <pre> 
 * 描述：租户管理员 实体对象
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:55:39
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("UC_TENANT_AUTH")
@ApiModel(value = "TenantAuth",description = "租户管理员") 
public class TenantAuth extends AutoFillModel<TenantAuth>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@XmlAttribute(name = "typeId")
	@TableField("TYPE_ID_")
	@ApiModelProperty(value="租户类型id")
	protected String typeId; 
	
	@XmlAttribute(name = "tenantId")
	@TableField("TENANT_ID_")
	@ApiModelProperty(value="租户id")
	protected String tenantId; 
	
	@XmlAttribute(name = "userId")
	@TableField("USER_ID_")
	@ApiModelProperty(value="用户id")
	protected String userId; 
	
	@XmlAttribute(name = "userName")
	@TableField("USER_NAME_")
	@ApiModelProperty(value="用户名称")
	protected String userName; 
	
	@XmlAttribute(name = "account")
	@TableField(exist=false)
	@ApiModelProperty(value="用户账号")
	protected String account; 
	
	@XmlAttribute(name = "email")
	@TableField(exist=false)
	@ApiModelProperty(value="用户邮箱")
	protected String email; 
	
	@XmlAttribute(name = "createTime")
	@TableField("CREATE_TIME_")
	@ApiModelProperty(value="创建时间")
	protected LocalDateTime createTime; 
	
	@XmlAttribute(name = "createBy")
	@TableField("CREATE_BY_")
	@ApiModelProperty(value="创建人")
	protected String createBy;
	
	@XmlAttribute(name = "createOrgId")
	@TableField("CREATE_ORG_ID_")
	@ApiModelProperty(value="创建人所属部门id")
	protected String createOrgId;
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	/**
	 * 返回 租户类型id
	 * @return
	 */
	public String getTypeId() {
		return this.typeId;
	}
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	/**
	 * 返回 租户id
	 * @return
	 */
	public String getTenantId() {
		return this.tenantId;
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
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 返回 用户名称
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}
	
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("typeId", this.typeId) 
		.append("tenantId", this.tenantId) 
		.append("userId", this.userId) 
		.append("userName", this.userName) 
		.append("account", this.account) 
		.append("email", this.email) 
		.append("createTime", this.createTime) 
		.append("createBy", this.createBy) 
		.append("createOrgId", this.createOrgId) 
		.toString();
	}
}