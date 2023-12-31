package com.hotent.sys.persistence.model;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.model.Tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据字典 entity对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author miao
 * @email miao@jee-soft.cn
 * @date 2014-06-20 13:55:53
 */
@TableName("portal_sys_dic")
@ApiModel(description="数据字典 entity对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDict extends BaseModel<DataDict> implements Tree{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty(name="typeId", notes="类型ID")
	@TableField("type_id_")
	protected String typeId; 
	
	@ApiModelProperty(name="key", notes="字典值代码,在同一个字典中值不能重复")
	@TableField("key_")
	protected String key; 
	
	@ApiModelProperty(name="name", notes="字典值名称")
	@TableField("name_")
	protected String name; 
	
	@ApiModelProperty(name="parentId", notes="父ID")
	@TableField("parent_id_")
	protected String parentId; 
	
	@ApiModelProperty(name="sn", notes="序号")
	@TableField("sn_")
	protected Integer sn;
	
	@TableField(exist=false)
	protected String open;//为true时树父节点展开
	
	@ApiModelProperty(name="isParent", notes="是否父节点")
	@TableField(exist=false)
	protected String isParent;//是否有子节点数据
	
	@TableField(exist=false)
	protected List<DataDict> children = new ArrayList<DataDict>();

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
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
	
	/**
	 * 设置序号
	 * @param sn 序号
	 */
	public void setSn(Integer sn) 
	{
		this.sn = sn;
	}
	
	/**
	 * 返回 序号
	 * @return
	 */
	public Integer getSn() 
	{
		return this.sn;
	}
	
	/**
	 * 设置类型id
	 * @param typeId 类型id
	 */
	public void setTypeId(String typeId) 
	{
		this.typeId = typeId;
	}
	
	/**
	 * 返回 类型ID
	 * @return
	 */
	public String getTypeId() 
	{
		return this.typeId;
	}
	
	/**
	 * 设置字典值代码,在同一个字典中值不能重复
	 * @param key 字典值代码
	 */
	public void setKey(String key) 
	{
		this.key = key;
	}
	
	/**
	 * 返回 字典值代码,在同一个字典中值不能重复
	 * @return
	 */
	public String getKey() 
	{
		return this.key;
	}
	
	/**
	 * 设置 字典值名称
	 * @param name 字典值名称
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
	
	/**
	 * 返回 字典值名称
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	/**
	 * 设置 父ID
	 * @param parentId 父ID
	 */
	public void setParentId(String parentId) 
	{
		this.parentId = parentId;
	}
	
	/**
	 * 返回 父ID
	 * @return
	 */
	public String getParentId() 
	{
		return this.parentId;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("typeId", this.typeId) 
		.append("key", this.key) 
		.append("name", this.name) 
		.append("parentId", this.parentId)
		.append("sn", this.sn)
		.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getChildren() {
		return children;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setChildren(List children){
		this.children = children;
	}
	
	public String getText() {
		return this.name;
	}
	
}