package com.hotent.Calendar.model;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象功能:班次 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-02-18 10:45:58
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("portal_cal_shift")
@ApiModel(description="班次")
public class CalendarShift extends BaseModel<CalendarShift> implements Cloneable{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String  id; 
	
	@TableField("name_")
	@ApiModelProperty("班次名")
	protected String  name;  
	
	@TableField("memo_")
	@ApiModelProperty("描述")
	protected String  memo;      
	
	@TableField("minutes_")
	@ApiModelProperty("工时,精确到分钟")
	protected Long    minutes;    
	
	@TableField("is_default_")
	@ApiModelProperty("是否默认班次,1是,0否")
	protected String  isDefault="0"; /**/

	protected List<CalendarShiftPeroid> calendarShiftPeroidList=new ArrayList<CalendarShiftPeroid>(); /*班次时间列表*/
	
	public List<CalendarShiftPeroid> getCalendarShiftPeroidList() {
		return calendarShiftPeroidList;
	}
	public void setCalendarShiftPeroidList(
			List<CalendarShiftPeroid> calendarShiftPeroidList) {
		this.calendarShiftPeroidList = calendarShiftPeroidList;
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
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	/**
	 * 返回 班次名
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	public void setMemo(String memo) 
	{
		this.memo = memo;
	}
	/**
	 * 返回 描述
	 * @return
	 */
	public String getMemo() 
	{
		return this.memo;
	}
	public void setMinutes(Long minutes) 
	{
		this.minutes = minutes;
	}
	/**
	 * 返回 工时,精确到分钟
	 * @return
	 */
	public Long getMinutes() 
	{
		return this.minutes;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("memo", this.memo) 
		.append("minutes", this.minutes) 
		.toString();
	}
}