package com.hotent.Calendar.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象功能:CAL_DATE_TYPE entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-02-13 13:40:06
 */
@TableName("portal_cal_date_type")
@ApiModel(description="工作日类型")
public class CalendarDateType extends BaseModel<CalendarDateType> {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String  id;                  
	
	@TableField("name_")
	@ApiModelProperty("名称")
	protected String  name;
	
	@TableField("key_")
	@ApiModelProperty("分类键")
	protected String  key;  
	
	@TableField("year_number_")
	@ApiModelProperty("年份")
	protected Long    yearNumber;
	
	@TableField("date_type_")
	@ApiModelProperty("日期类型，W 工作日；PH 公休日；LH 法定假日；CH 公司特定假日")
	protected String  dateType; 
	
	@TableField("scope_type_")
	@ApiModelProperty("日期范围类型，1 周日期范围；2 年日期范围；3 指定日期")
	protected String  scopeType; 
	
	@TableField("week_begin_")
	@ApiModelProperty("周日期起始")
	protected Long    weekBegin;
	
	@TableField("week_end_")
	@ApiModelProperty("周日期结束")
	protected Long    weekEnd;	
	
	@TableField("year_begin_")
	@ApiModelProperty("年日期起始")
	protected LocalDateTime  yearBegin; 
	
	@TableField("year_end_")
	@ApiModelProperty("年日期结束")
	protected LocalDateTime  yearEnd;      
	
	/**
	 * 工作日(日期类型)
	 */
	public static String WORKING_DAY="W";    
	/**
	 * 公休日
	 */
	public static String PUBLIC_HOLIDAY="PH";
	/**
	 * 法定假日
	 */
	public static String LEGAL_HOLIDAY="LH";
	/**
	 * 公司特定假日
	 */
	public static String COMPANY_HOLIDAY="CH";  
	
	/**
	 * 周日期范围
	 */
	public static int WEEK_SCOPE=2; 
	/**
	 * 年日期范围
	 */
	public static int YEAR_SCOPE=1; 
	/**
	 * 指定日期
	 */
	public static int SPEC_SCOPE=0; 
	
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 ID_
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
	/**
	 * 返回 NAME_
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	public void setKey(String key) 
	{
		this.key = key;
	}
	/**
	 * 返回 KEY_
	 * @return
	 */
	public String getKey() 
	{
		return this.key;
	}
	public void setYearNumber(Long yearNumber) 
	{
		this.yearNumber = yearNumber;
	}
	/**
	 * 返回 YEAR_NUMBER_
	 * @return
	 */
	public Long getYearNumber() 
	{
		return this.yearNumber;
	}
	public void setDateType(String dateType) 
	{
		this.dateType = dateType;
	}
	/**
	 * 返回 W 工作日；PH 公休日；LH 法定假日；CH 公司特定假日
	 * @return
	 */
	public String getDateType() 
	{
		return this.dateType;
	}
	public void setScopeType(String scopeType) 
	{
		this.scopeType = scopeType;
	}
	/**
	 * 返回 1 周日期范围；2 年日期范围；3 指定日期
	 * @return
	 */
	public String getScopeType() 
	{
		return this.scopeType;
	}
	public void setWeekBegin(Long weekBegin) 
	{
		this.weekBegin = weekBegin;
	}
	/**
	 * 返回 WEEK_BEGIN_
	 * @return
	 */
	public Long getWeekBegin() 
	{
		return this.weekBegin;
	}
	public void setWeekEnd(Long weekEnd) 
	{
		this.weekEnd = weekEnd;
	}
	/**
	 * 返回 WEEK_END_
	 * @return
	 */
	public Long getWeekEnd() 
	{
		return this.weekEnd;
	}
	public void setYearBegin(LocalDateTime yearBegin) 
	{
		this.yearBegin = yearBegin;
	}
	/**
	 * 返回 YEAR_BEGIN_
	 * @return
	 */
	public LocalDateTime getYearBegin() 
	{
		return this.yearBegin;
	}
	public void setYearEnd(LocalDateTime yearEnd) 
	{
		this.yearEnd = yearEnd;
	}
	/**
	 * 返回 YEAR_END_
	 * @return
	 */
	public LocalDateTime getYearEnd() 
	{
		return this.yearEnd;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("key", this.key) 
		.append("yearNumber", this.yearNumber) 
		.append("dateType", this.dateType) 
		.append("scopeType", this.scopeType) 
		.append("weekBegin", this.weekBegin) 
		.append("weekEnd", this.weekEnd) 
		.append("yearBegin", this.yearBegin) 
		.append("yearEnd", this.yearEnd) 
		.toString();
	}
}