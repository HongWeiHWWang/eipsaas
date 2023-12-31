package com.hotent.portal.model;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 布局设置 实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(description="布局设置 实体对象")
@TableName("portal_sys_layout_setting")
public class SysLayoutSetting extends BaseModel<SysLayoutSetting>{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID_")
	protected String id; 

	@ApiModelProperty(name="layoutId", notes="布局ID")
	@TableField("LAYOUT_ID_")
	protected String layoutId; 
	
	@ApiModelProperty(name="logo", notes="标志")
	@TableField("LOGO_")
	protected String logo; 
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 设置布局ID
	 * @param layoutId 布局ID
	 */
	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}
	
	/**
	 * 返回 布局ID
	 * @return
	 */
	public String getLayoutId() {
		return this.layoutId;
	}
	
	/**
	 * 设置LOGO
	 * @param logo 标志
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	/**
	 * 返回 LOGO
	 * @return
	 */
	public String getLogo() {
		return this.logo;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("layoutId", this.layoutId) 
		.append("logo", this.logo) 
		.toString();
	}
}