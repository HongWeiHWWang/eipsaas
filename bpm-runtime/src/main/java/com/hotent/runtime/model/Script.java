package com.hotent.runtime.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象功能:sys_script entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:helh
 * 创建时间:2014-05-08 14:50:24
 */
@TableName("bpm_script")
@ApiModel(value = "MessageType",description = "常用脚本") 
public class Script extends AutoFillModel<Script> implements Cloneable{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键")
	protected String id;

	@XmlAttribute(name = "name")
	@TableField("NAME_")
	@ApiModelProperty(name="name",notes="名称")
	protected String name;

	@XmlAttribute(name = "script")
	@TableField("SCRIPT_")
	@ApiModelProperty(name="script",notes="脚本")
	protected String script;

	@XmlAttribute(name = "category")
	@TableField("CATEGORY_")
	@ApiModelProperty(name="category",notes="脚本分类")
	protected String category;

	@XmlAttribute(name = "memo")
	@TableField("MEMO_")
	@ApiModelProperty(name="memo",notes="备注")
	protected String memo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("script", this.script) 
		.append("category", this.category) 
		.append("memo", this.memo) 
		.toString();
	}
}
