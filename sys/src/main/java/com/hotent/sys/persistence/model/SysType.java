package com.hotent.sys.persistence.model;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.model.Tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 总分类表。用于显示平级或树层次结构的分类，可以允许任何层次结构。 entity对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zyp
 * @email zyp@jee-soft.cn
 * @date 2018年6月21日
 */
@ApiModel(description="总分类表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("portal_sys_type")
public class SysType extends AutoFillModel<SysType> implements Tree{
	
	private static final long serialVersionUID = 6670633211129568564L;

	@ApiModelProperty(name="id", notes="分类ID")
	@TableId("id_")
	protected String id;
	
	@ApiModelProperty(name="typeGroupKey", notes="所属分类组业务主键")
	@TableField("type_group_key_")
	protected String typeGroupKey; 
	
	@ApiModelProperty(name="name", notes="分类名称")
	@TableField("name_")
	protected String name;
	
	@ApiModelProperty(name="typeKey", notes="节点的分类Key")
	@TableField("type_key_")
	protected String typeKey; 
	
	@ApiModelProperty(name="struType", notes="类别(0:平铺结构 1:树型结构)", allowableValues="0,1")
	@TableField("stru_type_")
	protected Short struType;
	
	@ApiModelProperty(name="parentId", notes="父节点")
	@TableField("parent_id_")
	protected String parentId; 
	
	@ApiModelProperty(name="depth", notes="层次")
	@TableField("depth_")
	protected Integer depth;
	
	@ApiModelProperty(name="path", notes="路径")
	@TableField("path_")
	protected String path; 
	
	@ApiModelProperty(name="isLeaf", notes="是否叶子节点(Y:是 N:否)", allowableValues="Y,N")
	@TableField("is_leaf_")
	protected char isLeaf = 'N';
	
	@ApiModelProperty(name="ownerId", notes="所属人ID")
	@TableField("owner_id_")
	protected String ownerId;
	
	@ApiModelProperty(name="sn", notes="序号")
	@TableField("sn_")
	protected Integer sn; 
	
	@ApiModelProperty(name="icon", notes="图标地址")
	@TableField("icon_")
	protected String icon; /*更新人ID*/
	
	@TableField(exist=false)
	protected List<SysType> children = new ArrayList<SysType>();
	
	@TableField(exist=false)
	protected String open;//为true时树父节点展开
	
	@ApiModelProperty(name="isParent", notes="是否父节点")
	@TableField(exist=false)
	protected String isParent;//是否有子节点数据

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

	/**
	 * 设置主键 
	 * @param id 主键 
	 */
	public void setId(String id) 
	{
		this.id = id;
	}
	
	/**
	 * 返回 分类ID
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	
	/**
	 * 设置所属分类组业务主键
	 * @param typeGroupKey 所属分类组业务主键
	 */
	public void setTypeGroupKey(String typeGroupKey) 
	{
		this.typeGroupKey = typeGroupKey;
	}
	
	/**
	 * 返回 所属分类组业务主键
	 * @return
	 */
	public String getTypeGroupKey() 
	{
		return this.typeGroupKey;
	}
	
	/**
	 * 设置分类名称
	 * @param name 分类名称
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
	
	/**
	 * 返回 分类名称
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	
	/**
	 * 设置节点的分类Key
	 * @param typeKey 节点的分类Key
	 */
	public void setTypeKey(String typeKey) 
	{
		this.typeKey = typeKey;
	}
	
	/**
	 * 返回 节点的分类Key
	 * @return
	 */
	public String getTypeKey() 
	{
		return this.typeKey;
	}
	
	/**
	 * 设置0=平铺结构；1=树型结构
	 * @param struType 0=平铺结构；1=树型结构
	 */
	public void setStruType(Short struType) 
	{
		this.struType = struType;
	}
	
	/**
	 * 返回 0=平铺结构；1=树型结构
	 * @return
	 */
	public Short getStruType() 
	{
		return this.struType;
	}
	
	/**
	 * 设置父节点
	 * @param parentId 父节点
	 */
	public void setParentId(String parentId) 
	{
		this.parentId = parentId;
	}
	
	/**
	 * 返回 父节点
	 * @return
	 */
	public String getParentId() 
	{
		return this.parentId;
	}
	
	/**
	 * 设置层次
	 * @param depth 层次
	 */
	public void setDepth(Integer depth) 
	{
		this.depth = depth;
	}
	
	/**
	 * 返回 层次
	 * @return
	 */
	public Integer getDepth() 
	{
		return this.depth;
	}
	
	/**
	 * 设置路径
	 * @param path 路径
	 */
	public void setPath(String path) 
	{
		this.path = path;
	}
	
	/**
	 * 返回 路径
	 * @return
	 */
	public String getPath() 
	{
		return this.path;
	}
	
	/**
	 * 设置是否叶子节点
	 * @param isLeaf 是否叶子节点
	 */
	public void setIsLeaf(char isLeaf) 
	{
		this.isLeaf = isLeaf;
	}
	
	/**
	 * 返回 是否叶子节点。Y=是；N=否
	 * @return
	 */
	public char getIsLeaf() 
	{
		return this.isLeaf;
	}
	
	/**
	 * 设置所属人ID
	 * @param ownerId 所属人ID
	 */
	public void setOwnerId(String ownerId) 
	{
		this.ownerId = ownerId;
	}
	
	/**
	 * 返回 所属人ID
	 * @return
	 */
	public String getOwnerId() 
	{
		return this.ownerId;
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

	public List getChildren() {
		return children;
	}
	
	public void setChildren(List children){
		this.children = children;
	}
	
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("typeGroupKey", this.typeGroupKey) 
		.append("name", this.name) 
		.append("typeKey", this.typeKey) 
		.append("struType", this.struType) 
		.append("parentId", this.parentId) 
		.append("depth", this.depth) 
		.append("path", this.path) 
		.append("isLeaf", this.isLeaf) 
		.append("ownerId", this.ownerId) 
		.append("sn", this.sn) 
		.toString();
	}
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return this.name;
	}
}