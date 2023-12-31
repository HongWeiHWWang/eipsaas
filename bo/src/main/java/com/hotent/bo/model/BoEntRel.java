package com.hotent.bo.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

 /**
  * BO实体的关系
  * 
  * @company 广州宏天软件股份有限公司
  * @author heyifan
  * @email heyf@jee-soft.cn
  * @date 2018年4月12日
  */
@ApiModel("BO实体的关系")
@TableName("form_bo_ent_relation")
public class BoEntRel extends BaseModel<BoEntRel>{
	private static final long serialVersionUID = 2325631939274387702L;

	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty("BO定义ID")
	@TableField("bo_defid_")
	protected String boDefid; 
	
	@ApiModelProperty("上级ID")
	@TableField("parent_id_")
	protected String parentId; 
	
	@ApiModelProperty("关联的bo实体ID")
	@TableField("ref_ent_id_")
	protected String refEntId; 
	
	@ApiModelProperty("关系类型：main,onetoone,onetomany,manytomany")
	@TableField("type_")
	protected String type;
	
	/**
	 * 关联的bo实体类
	 */
	@TableField(exist=false)
	protected BoEnt refEnt;
	
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
	
	public void setBoDefid(String boDefid) {
		this.boDefid = boDefid;
	}
	
	/**
	 * 返回 BO定义ID
	 * @return
	 */
	public String getBoDefid() {
		return this.boDefid;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 返回 上级ID
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
	}
	
	public void setRefEntId(String refEntId) {
		this.refEntId = refEntId;
	}
	
	/**
	 * 返回 引用的BODEFID
	 * @return
	 */
	public String getRefEntId() {
		return this.refEntId;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 返回 类型(main,onetoone,onetomany,manytomany)
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * 关联实体。
	 * @return
	 */
	public BoEnt getRefEnt() {
		return refEnt;
	}

	public void setRefEnt(BoEnt refEnt) {
		this.refEnt = refEnt;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("boDefid", this.boDefid) 
		.append("parentId", this.parentId) 
		.append("refEntId", this.refEntId) 
		.append("type", this.type) 
		.toString();
	}
}