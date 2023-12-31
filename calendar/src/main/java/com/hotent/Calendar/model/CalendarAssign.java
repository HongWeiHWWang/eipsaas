package com.hotent.Calendar.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象功能:日历分配 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-01-07 16:06:46
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("portal_cal_assign")
@ApiModel(description="班次")
public class CalendarAssign extends BaseModel<CalendarAssign>{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String     id;        
	
	@TableField("canlendar_Id_")
	@ApiModelProperty("日历ID")
	protected String     calendarId;    
	
	@TableField("assign_Type_")
	@ApiModelProperty("分配者类型")
	protected String     assignType;  
	
	@TableField("assign_Id_")
	@ApiModelProperty("分配者ID")
	protected String     assignId;       
	
	// 日历名称
	@TableField(exist=false)
	protected String calendarName;
	
	// 分配人名称
	@TableField(exist=false)
	protected String assignUserName;
	/**
	 * 用户
	 */
	public static int TYPE_USER=1;
	
	/**
	 * 组织
	 */
	public static int TYPE_ORGANIZATION=2;
	
	public String getCalendarName() {
		return calendarName;
	}
	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
	public String getAssignUserName() {
		return assignUserName;
	}
	public void setAssignUserName(String assignUserName) {
		this.assignUserName = assignUserName;
	}
	
	
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setCanlendarId(String calendarId) 
	{
		this.calendarId = calendarId;
	}
	/**
	 * 返回 日历ID
	 * @return
	 */
	public String getCanlendarId() 
	{
		return this.calendarId;
	}
	public void setAssignType(String assignType) 
	{
		this.assignType = assignType;
	}
	/**
	 * 返回 分配者类型

	 * @return
	 */
	public String getAssignType() 
	{
		return this.assignType;
	}
	
	public void setAssignId(String assignId) 
	{
		this.assignId = assignId;
	}
	/**
	 * 返回 分配者ID
	 * @return
	 */
	public String getAssignId() 
	{
		return this.assignId;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("calendarId", this.calendarId) 
		.append("assignType", this.assignType) 
		.append("assignId", this.assignId)
		.append("assignUserName", this.assignUserName)
		.toString();
	}
}