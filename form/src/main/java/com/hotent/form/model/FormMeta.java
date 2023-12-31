package com.hotent.form.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.util.JsonUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 表单元数据
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月6日
 */
@ApiModel("表单元数据对象")
@TableName("form_meta")
public class FormMeta extends AutoFillModel<FormMeta>{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id;
	
	@ApiModelProperty(name="key", notes="表单元数据key")
	@TableField("key_")
	protected String key;
	
	@ApiModelProperty(name="name", notes="表单元数据名称")
	@TableField("name_")
	protected String name;
	
	@ApiModelProperty(name="desc", notes="描述")
	@TableField("desc_")
	protected String desc; 
	
	@ApiModelProperty(name="type", notes="分类名称")
	@TableField("type_")
	protected String type;
	
	@ApiModelProperty(name="typeId", notes="分类ID")
	@TableField("type_id_")
	protected String typeId;
	
	@ApiModelProperty(name="expand", notes="扩展字段")
	@TableField("expand_")
	protected String expand;

	@ApiModelProperty(name="opinionConf", notes="意见配置",example="[{\"name\": \"jzyj\",\"desc\": \"局长意见\"},{\"name\": \"cwyj\",\"desc\": \"财务意见\" }]")
	@TableField("opinion_conf_")
	protected String opinionConf;
	
	@ApiModelProperty(name="ganged", notes="联动设置")
	@TableField("ganged_")
	protected String ganged;
	
	@ApiModelProperty(name="macroAlias", notes="宏模板")
	@TableField("macroAlias_")
	protected String macroAlias;
	
	@ApiModelProperty(name="mainAlias", notes="表单模板")
	@TableField("mainAlias_")
	protected String mainAlias;
	
	@ApiModelProperty(name="subEntity", notes="子实体模板")
	@TableField("subEntity_")
	protected String subEntity;

    @ApiModelProperty(name="rev", notes="关联锁")
    @TableField("rev_")
    @Version
    protected Integer rev = 1; /*关联锁*/
    
    @ApiModelProperty(name="formType", notes="表单类型")
	@TableField(exist=false)
	protected String formType;
    
    @ApiModelProperty(name="bpmFormFieldList", notes="字段列表")
    @TableField(exist=false)
    protected List<FormField> bpmFormFieldList = new ArrayList<FormField>(); /*用户任务表单字段信息表列表*/
    
    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    public String getMacroAlias() {
		return macroAlias;
	}

	public void setMacroAlias(String macroAlias) {
		this.macroAlias = macroAlias;
	}

	public String getMainAlias() {
		return mainAlias;
	}

	public void setMainAlias(String mainAlias) {
		this.mainAlias = mainAlias;
	}

	public String getSubEntity() {
		return subEntity;
	}

	public void setSubEntity(String subEntity) {
		this.subEntity = subEntity;
	}
	
	public String getGanged() {
		return ganged;
	}

	public void setGanged(String ganged) {
		this.ganged = ganged;
	}

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeId() {
		return typeId;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public List<FormField> getBpmFormFieldList() {
		return bpmFormFieldList;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setBpmFormFieldList(List<FormField> bpmFormFieldList) {
		this.bpmFormFieldList = bpmFormFieldList;
	}

	public String getId() {
		return id;
	}

	public String getOpinionConf() {
		return opinionConf;
	}

	public void setOpinionConf(String opinionConf) {
		this.opinionConf = opinionConf;
	}

	public void setId(String id) 
	{
		this.id = id;
	}
	
	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	@XmlTransient
	public JsonNode getFieldList() throws IOException {
		if(this.expand!=null){
			JsonNode json=JsonUtil.toJsonNode(this.expand);
			return json.get("fields");
		}
		return null;
	}
	
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", this.id)
				.append("name", this.name)
				.append("desc", this.desc)
				.append("type", this.type)
				.append("typeId", this.typeId)
				.append("expand", this.expand)
				.append("opinionConf", this.opinionConf)
				.append("ganged", this.ganged)
				.append("macroAlias", this.macroAlias)
				.append("mainAlias", this.mainAlias)
				.append("subEntity", this.subEntity)
				.append("rev", this.rev)
				.toString();
	}
}