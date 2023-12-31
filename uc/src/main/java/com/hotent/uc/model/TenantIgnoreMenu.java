package com.hotent.uc.model;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;


 /**
 * 租户禁用菜单
 * <pre> 
 * 描述：租户禁用菜单 实体对象
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-20 17:00:53
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("UC_TENANT_IGNORE_MENU")
@ApiModel(value = "TenantIgnoreMenu",description = "租户禁用菜单") 
public class TenantIgnoreMenu extends AutoFillModel<TenantIgnoreMenu>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@XmlAttribute(name = "tenantId")
	@TableField("TENANT_ID_")
	@ApiModelProperty(value="租户id")
	protected String tenantId; 
	
	@XmlAttribute(name = "menuCode")
	@TableField("MENU_CODE_")
	@ApiModelProperty(value="禁用菜单别名")
	protected String menuCode; 
	
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
	
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	
	/**
	 * 返回 禁用菜单别名
	 * @return
	 */
	public String getMenuCode() {
		return this.menuCode;
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
		.append("tenantId", this.tenantId) 
		.append("menuCode", this.menuCode) 
		.append("createTime", this.createTime) 
		.append("createBy", this.createBy) 
		.append("createOrgId", this.createOrgId) 
		.toString();
	}
}