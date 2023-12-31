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
 * 对象功能:系统分类组值表 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-05-08 14:12:26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("portal_sys_type_group")
@ApiModel(description="系统分类分组")
public class SysCategory extends AutoFillModel<SysCategory>{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 树型数据 type=1
	 */
	public final static Integer DATA_TYPE_TREE=1;
	/**
	 * 平铺数据 type=0
	 */
	public final static Integer DATA_TYPE_FLAT=0;
	/**
	 * 是否叶子(N否,Y是)
	 */
	public final static char IS_LEAF_N='N';
	public final static char IS_LEAF_Y='Y';
	
	/**
	 * 自编码生成方式(0	手工录入,1自动生成)
	 */
	public final static String NODE_CODE_TYPE_AUTO_N="0";
	public final static String NODE_CODE_TYPE_AUTO_Y="1";
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id;
	
	@TableField("group_key_")
	@ApiModelProperty("分类组业务编码")
	protected String groupKey;
	
	@TableField("name_")
	@ApiModelProperty("分类名")
	protected String name;
	
	@TableField("flag_")
	@ApiModelProperty("标识")
	protected Integer flag; 
	
	@TableField("sn_")
	@ApiModelProperty("序号")
	protected Integer sn; 
	
	@TableField("type_")
	@ApiModelProperty("类别。0=平铺结构；1=树型结构")
	protected Short type;
	
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 主键ID
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setGroupKey(String groupKey) 
	{
		this.groupKey = groupKey;
	}
	/**
	 * 返回 分类组业务主键
	 * @return
	 */
	public String getGroupKey() 
	{
		return this.groupKey;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	/**
	 * 返回 分类名
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	public void setFlag(Integer flag) 
	{
		this.flag = flag;
	}
	/**
	 * 返回 标识
	 * @return
	 */
	public Integer getFlag() 
	{
		return this.flag;
	}
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
	public void setType(Short type)
	{
		this.type = type;
	}
	/**
	 * 返回 类别。0=平铺结构；1=树型结构
	 * @return
	 */
	public Short getType()
	{
		return this.type;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("groupKey", this.groupKey) 
		.append("name", this.name) 
		.append("flag", this.flag) 
		.append("sn", this.sn) 
		.append("type", this.type)
		.toString();
	}
}