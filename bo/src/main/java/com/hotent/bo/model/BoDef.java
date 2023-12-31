package com.hotent.bo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.typehandle.ShortTypeHandle;
import com.hotent.bo.constant.BoConstants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * bo定义
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
@ApiModel(description="bo定义")
@TableName("form_bo_def")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoDef extends AutoFillModel<BoDef> {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("主键")
	@TableId("id_")
	@XmlElement(name = "id")
	protected String id="";
	
	@XmlAttribute(name = "alias")
	@ApiModelProperty(value="别名", required=true)
	@TableField("alias_")
	protected String alias="";
	
	@XmlElement(name = "description")
	@ApiModelProperty("描述")
	@TableField("description_")
	protected String description="";
	
	@XmlAttribute(name = "supportDb")
	@ApiModelProperty(value="是否支持数据库(0:不支持  1:支持)", allowableValues="0,1")
	@TableField(value="support_db_",typeHandler = ShortTypeHandle.class)
	protected Short supportDb = 0;
	
	@XmlAttribute(name = "categoryId")
	@ApiModelProperty(value="categoryId", notes="所属分类ID", required=true)
	@TableField("category_id_")
	protected String categoryId = "";
	
	@ApiModelProperty("所属分类名称")
	@TableField("category_name_")
	protected String categoryName = "";
	
	@XmlAttribute(name = "status")
	@ApiModelProperty(value="状态(normal:正常 forbidden:禁用)", allowableValues="normal,forbidden")
	@TableField("status_")
	protected String status = "normal";
	
	@XmlAttribute(name = "deployed")
	@ApiModelProperty(value="是否发布(0:未发布 1:已发布)", allowableValues="0,1")
	@TableField(value= "deployed_" ,typeHandler = ShortTypeHandle.class)
	protected Short deployed = 0;

    @ApiModelProperty("关联锁")
    @TableField("rev_")
    @Version
    protected Integer rev =1;
    
    @XmlElement(name = "ent")
	@ApiModelProperty("业务实体")
    @TableField(exist=false)
	protected BoEnt boEnt = new BoEnt();

    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

	public void setId(String id) {
		this.id=id;
	}

	public String getId() {
		return this.id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonIgnore
	public BoEnt getBoEnt() {
		return boEnt;
	}

	public void setBoEnt(BoEnt boEnt) {
		this.boEnt = boEnt;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public boolean isSupportDb() {
		return BoConstants.BOOLEAN_YES_SHORT.equals(this.supportDb);
	}

	public void setSupportDb(boolean supportDb) {
		this.supportDb = supportDb ? BoConstants.BOOLEAN_YES_SHORT : BoConstants.BOOLEAN_NO_SHORT;
	}

	public boolean isDeployed() {
		return BoConstants.BOOLEAN_YES_SHORT.equals(this.deployed);
	}

	public void setDeployed(boolean deployed) {
		this.deployed = deployed ? BoConstants.BOOLEAN_YES_SHORT : BoConstants.BOOLEAN_NO_SHORT;
	}
	
	public String toString() {
		return "BaseBoDef [alias=" + alias + ", description=" + description + ",categoryId=" + categoryId + "]";
	}
}