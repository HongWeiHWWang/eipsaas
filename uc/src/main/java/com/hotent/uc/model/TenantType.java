package com.hotent.uc.model;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;


 /**
 * UC_TENANT_TYPE
 * <pre> 
 * 描述：租户类型 实体对象
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:52:37
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("UC_TENANT_TYPE")
@ApiModel(value = "TenantType",description = "租户类型") 
public class TenantType extends AutoFillModel<TenantType>{

	private static final long serialVersionUID = 1L;
	
	public final static String STATUS_ENABLE = "enable";
	public final static String STATUS_DISABLED = "disabled";

	public final static String DEFAULT = "1";
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@XmlAttribute(name = "name")
	@TableField("NAME_")
	@ApiModelProperty(value="类型名称")
	protected String name; 
	
	@XmlAttribute(name = "code")
	@TableField("CODE_")
	@ApiModelProperty(value="类型编码")
	protected String code; 
	
	@XmlAttribute(name = "status")
	@TableField("STATUS_")
	@ApiModelProperty(value="状态：启用（enable）、禁用（disabled）")
	protected String status; 
	
	@XmlAttribute(name = "isDefault")
	@TableField("IS_DEFAULT_")
	@ApiModelProperty(value="是否默认（1:是，0:否）")
	protected String isDefault="0"; 
	
	@XmlAttribute(name = "desc")
	@TableField("DESC_")
	@ApiModelProperty(value="类型说明")
	protected String desc; 
	
	@ApiModelProperty(value = "创建人ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_by_", fill=FieldFill.INSERT)
	private String createBy;

	@ApiModelProperty(value = "创建人组织ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_org_id_", fill=FieldFill.INSERT)
	private String createOrgId;
	
	@ApiModelProperty(value = "创建时间", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_time_", fill=FieldFill.INSERT)
	private LocalDateTime createTime;
	
	@ApiModelProperty(value = "更新人ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="update_by_", fill=FieldFill.UPDATE)
	private String updateBy;

	@ApiModelProperty(value = "更新时间", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="update_time_", fill=FieldFill.UPDATE)
	private LocalDateTime updateTime;

	
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 类型名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 返回 类型编码
	 * @return
	 */
	public String getCode() {
		return this.code;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 返回 状态：启用（enable）、禁用（disabled）
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}
	
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	/**
	 * 返回 是否默认（1:是，0:否）
	 * @return
	 */
	public String getIsDefault() {
		return this.isDefault;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 返回 类型说明
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
		.append("name", this.name) 
		.append("code", this.code) 
		.append("status", this.status) 
		.append("isDefault", this.isDefault) 
		.append("desc", this.desc) 
		.append("createTime", this.createTime) 
		.append("createBy", this.createBy) 
		.append("updateBy", this.updateBy) 
		.append("updateTime", this.updateTime) 
		.append("createOrgId", this.createOrgId) 
		.toString();
	}
}