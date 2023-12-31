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
 * UC_TENANT_MAIL_SERVER
 * <pre> 
 * 描述：租户邮件服务器 实体对象
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 11:01:29
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("UC_TENANT_MAIL_SERVER")
@ApiModel(value = "TenantMailServer",description = "租户邮件服务器") 
public class TenantMailServer extends AutoFillModel<TenantMailServer>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@XmlAttribute(name = "tenantId")
	@TableField("TENANT_ID_")
	@ApiModelProperty(value="租户id")
	protected String tenantId; 
	
	@XmlAttribute(name = "mailType")
	@TableField("MAIL_TYPE_")
	@ApiModelProperty(value="邮件类型（pop3,imap,exchange）")
	protected String mailType; 
	
	@XmlAttribute(name = "mailHost")
	@TableField("MAIL_HOST_")
	@ApiModelProperty(value="协议服务器")
	protected String mailHost; 
	
	@XmlAttribute(name = "mailPass")
	@TableField("MAIL_PASS_")
	@ApiModelProperty(value="密码")
	protected String mailPass; 
	
	@XmlAttribute(name = "nickName")
	@TableField("NICK_NAME_")
	@ApiModelProperty(value="昵称")
	protected String nickName; 
	
	@XmlAttribute(name = "userName")
	@TableField("USER_NAME_")
	@ApiModelProperty(value="邮箱地址")
	protected String userName; 
	
	@XmlAttribute(name = "mailPort")
	@TableField("MAIL_PORT_")
	@ApiModelProperty(value="端口号")
	protected Integer mailPort; 
	
	@XmlAttribute(name = "useSsl")
	@TableField("USE_SSL_")
	@ApiModelProperty(value="是否使用SSL认证。0：否；1：是")
	protected Short useSsl=0; 
	
	@XmlAttribute(name = "desc")
	@TableField("DESC_")
	@ApiModelProperty(value="说明")
	protected String desc; 
	
	@XmlAttribute(name = "createTime")
	@TableField("CREATE_TIME_")
	@ApiModelProperty(value="创建时间")
	protected LocalDateTime createTime; 
	
	@XmlAttribute(name = "createBy")
	@TableField("CREATE_BY_")
	@ApiModelProperty(value="创建人")
	protected String createBy;
	
	@XmlAttribute(name = "updateTime")
	@TableField("UPDATE_TIME_")
	@ApiModelProperty(value="更新时间")
	protected LocalDateTime updateTime; 
	
	@XmlAttribute(name = "updateBy")
	@TableField("UPDATE_BY_")
	@ApiModelProperty(value="更新人")
	protected String updateBy;
	
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
	
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	
	/**
	 * 返回 邮件类型
	 * @return
	 */
	public String getMailType() {
		return this.mailType;
	}
	
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	
	/**
	 * 返回 协议服务器
	 * @return
	 */
	public String getMailHost() {
		return this.mailHost;
	}
	
	public void setMailPass(String mailPass) {
		this.mailPass = mailPass;
	}
	
	/**
	 * 返回 密码
	 * @return
	 */
	public String getMailPass() {
		return this.mailPass;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	/**
	 * 返回 昵称
	 * @return
	 */
	public String getNickName() {
		return this.nickName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 返回 邮箱地址
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}
	
	public void setMailPort(Integer mailPort) {
		this.mailPort = mailPort;
	}
	
	/**
	 * 返回 端口号
	 * @return
	 */
	public Integer getMailPort() {
		return this.mailPort;
	}
	
	public void setUseSsl(Short useSsl) {
		this.useSsl = useSsl;
	}
	
	/**
	 * 返回 是否使用SSL认证。0：否；1：是
	 * @return
	 */
	public Short getUseSsl() {
		return this.useSsl;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 返回 说明
	 * @return
	 */
	public String getDesc() {
		return this.desc;
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

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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
		.append("tenantId", this.tenantId) 
		.append("mailType", this.mailType) 
		.append("mailHost", this.mailHost) 
		.append("mailPass", this.mailPass) 
		.append("nickName", this.nickName) 
		.append("userName", this.userName) 
		.append("mailPort", this.mailPort) 
		.append("useSsl", this.useSsl) 
		.append("desc", this.desc) 
		.append("createTime", this.createTime) 
		.append("createBy", this.createBy) 
		.append("updateBy", this.updateBy) 
		.append("updateTime", this.updateTime) 
		.append("createOrgId", this.createOrgId) 
		.toString();
	}
}