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
 * 对象功能:系统日历 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-02-18 13:59:33
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("portal_calendar")
@ApiModel(description="系统日历")
public class Calendar extends BaseModel<Calendar> implements Cloneable{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String  id; 
	
	@TableField("name_")
	@ApiModelProperty("日历名称")
	protected String  name;
	
	@TableField("memo_")
	@ApiModelProperty("描述")
	protected String  memo;
	
	@TableField("is_default_")
	@ApiModelProperty("1=默认日历")
	protected char  isDefault; 
	
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
	/**
	 * 返回 日历名称
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
	public void setIsDefault(char isDefault) 
	{
		this.isDefault = isDefault;
	}
	/**
	 * 返回 1=默认日历

	 * @return
	 */
	public char getIsDefault() 
	{
		return this.isDefault;
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
		.append("isDefault", this.isDefault) 
		.toString();
	}
}