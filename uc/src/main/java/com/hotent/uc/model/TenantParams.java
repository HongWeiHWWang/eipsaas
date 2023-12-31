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
import com.hotent.base.entity.BaseModel;


 /**
 * UC_TENANT_PARAMS
 * <pre> 
 * 描述：租户扩展参数值 实体对象
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 14:54:36
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("UC_TENANT_PARAMS")
@ApiModel(value = "TenantParams",description = "租户扩展参数值实体") 
public class TenantParams extends AutoFillModel<TenantParams>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="主键")
	protected String id;
	
	@XmlAttribute(name = "tenantId")
	@TableField("TENANT_ID_")
	@ApiModelProperty(value="租户id")
	protected String tenantId; 
	
	@XmlAttribute(name = "code")
	@TableField("CODE_")
	@ApiModelProperty(value="参数编码")
	protected String code; 
	
	@XmlAttribute(name = "value")
	@TableField("VALUE_")
	@ApiModelProperty(value="参数值")
	protected String value; 
	
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
	
	
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 返回 参数编码
	 * @return
	 */
	public String getCode() {
		return this.code;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 返回 参数值
	 * @return
	 */
	public String getValue() {
		return this.value;
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
		.append("code", this.code) 
		.append("value", this.value) 
		.append("createTime", this.createTime) 
		.append("createBy", this.createBy) 
		.append("updateBy", this.updateBy) 
		.append("updateTime", this.updateTime) 
		.append("createOrgId", this.createOrgId) 
		.toString();
	}
}