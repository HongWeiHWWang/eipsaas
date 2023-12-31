package com.hotent.portal.model;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 布局工具设置 实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(description="布局工具设置 实体对象")
@TableName("portal_sys_layout_tools")
public class SysLayoutTools extends BaseModel<SysLayoutTools>{

	private static final long serialVersionUID = 1L;
	
	public static final String TOOLS = "快捷工具";
	public static final String COLUMN = "统计栏目";

	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID_")
	protected String id; 

	@ApiModelProperty(name="layoutId", notes="布局ID")
	@TableField("LAYOUT_ID_")
	protected String layoutId; 

	@ApiModelProperty(name="toolsIds", notes="工具ID列表")
	@TableField("TOOLS_IDS")
	protected String toolsIds; 

	@ApiModelProperty(name="toolsType", notes="工具类型")
	@TableField("TOOLS_TYPE")
	protected String toolsType; 
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
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
	 * 设置工具ID列表
	 * @param toolsIds 工具ID列表
	 */
	public void setToolsIds(String toolsIds) {
		this.toolsIds = toolsIds;
	}
	
	/**
	 * 返回 工具ID列表
	 * @return
	 */
	public String getToolsIds() {
		return this.toolsIds;
	}
	
	/**
	 * 设置工具类型
	 * @param toolsType 工具类型
	 */
	public void setToolsType(String toolsType) {
		this.toolsType = toolsType;
	}
	
	/**
	 * 返回 工具类型
	 * @return
	 */
	public String getToolsType() {
		return this.toolsType;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("layoutId", this.layoutId) 
		.append("toolsIds", this.toolsIds) 
		.append("toolsType", this.toolsType) 
		.toString();
	}
}