package com.hotent.sys.persistence.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象功能:流水号生成 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-07-16 16:10:29
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description="流水号定义")
@TableName("portal_sys_identity")
public class SysIdentity extends AutoFillModel<SysIdentity>{

	private static final long serialVersionUID = 1L;
	
	@TableId("id_")
	@ApiModelProperty(name="id", notes="主键")
	protected String id;

	@TableField("name_")
	@ApiModelProperty(name="name", notes="名称")
	protected String name;
	
	@TableField("alias_")
	@ApiModelProperty(name="alias", notes="别名")
	protected String alias; 
	
	@TableField("regulation_")
	@ApiModelProperty(name="regulation", notes="规则")
	protected String regulation; 
	
	@TableField("gen_type_")
	@ApiModelProperty(name="genType", notes="生成类型")
	protected Short genType; 
	
	@TableField("no_length_")
	@ApiModelProperty(name="noLength", notes="流水号长度")
	protected Integer noLength; 
	
	@TableField("cur_date_")
	@ApiModelProperty(name="curDate", notes="当前日期")
	protected String curDate;
	
	@TableField("init_value_")
	@ApiModelProperty(name="initValue", notes="初始值")
	protected Integer initValue;
	
	@TableField("cur_value_")
	@ApiModelProperty(name="curValue", notes="当前值")
	protected Integer curValue=0; 
	
	@TableField("step_")
	@ApiModelProperty(name="step", notes="步长")
	protected Short step; 
	
	/**
	 * 新的流水号。
	 */
	@TableField(exist=false)
	protected Integer newCurValue=0; 
	
	/**
	 * 预览流水号。
	 */
	@TableField(exist=false)
	protected String curIdenValue="";
	
	
	
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 id_
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
	 * 返回 name_
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	public void setAlias(String alias) 
	{
		this.alias = alias;
	}
	/**
	 * 返回 alias_
	 * @return
	 */
	public String getAlias() 
	{
		return this.alias;
	}
	public void setRegulation(String regulation) 
	{
		this.regulation = regulation;
	}
	/**
	 * 返回 regulation_
	 * @return
	 */
	public String getRegulation() 
	{
		return this.regulation;
	}
	public void setGenType(Short genType) 
	{
		this.genType = genType;
	}
	/**
	 * 返回 gen_type_
	 * @return
	 */
	public Short getGenType() 
	{
		return this.genType;
	}
	public void setNoLength(Integer noLength) 
	{
		this.noLength = noLength;
	}
	/**
	 * 返回 no_length_
	 * @return
	 */
	public Integer getNoLength() 
	{
		return this.noLength;
	}
	public void setCurDate(String curDate) 
	{
		this.curDate = curDate;
	}
	/**
	 * 返回 cur_date_
	 * @return
	 */
	public String getCurDate() 
	{
		return this.curDate;
	}
	public void setInitValue(Integer initValue) 
	{
		this.initValue = initValue;
	}
	/**
	 * 返回 init_value_
	 * @return
	 */
	public Integer getInitValue() 
	{
		return this.initValue;
	}
	public void setCurValue(Integer curValue) 
	{
		this.curValue = curValue;
	}
	/**
	 * 返回 cur_value_
	 * @return
	 */
	public Integer getCurValue() 
	{
		if(curValue==null) return 0;
		return this.curValue;
	}
	public void setStep(Short step) 
	{
		this.step = step;
	}
	/**
	 * 返回 step_
	 * @return
	 */
	public Short getStep() 
	{
		return this.step;
	}
	
	

	public Integer getNewCurValue() {
		return newCurValue;
	}
	public void setNewCurValue(Integer newCurValue) {
		this.newCurValue = newCurValue;
	}
	public String getCurIdenValue() {
		return curIdenValue;
	}
	public void setCurIdenValue(String curIdenValue) {
		this.curIdenValue = curIdenValue;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("alias", this.alias) 
		.append("regulation", this.regulation) 
		.append("genType", this.genType) 
		.append("noLength", this.noLength) 
		.append("curDate", this.curDate) 
		.append("initValue", this.initValue) 
		.append("curValue", this.curValue) 
		.append("step", this.step) 
		.toString();
	}
}