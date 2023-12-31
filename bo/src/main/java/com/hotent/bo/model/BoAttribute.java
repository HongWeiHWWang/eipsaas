package com.hotent.bo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.bo.constant.BoConstants;
import com.hotent.table.model.Column;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * bo属性
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
@TableName("form_bo_attr")
@ApiModel(description="bo属性")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoAttribute extends BaseModel<BoAttribute> implements Column{
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "id")
	@TableId("id_")
	@ApiModelProperty("主键")
	protected String id = "";

	@TableField("ent_id_")
	@ApiModelProperty("所属实体ID")
	protected String entId; 

	@XmlAttribute(name = "name")
	@TableField("name_")
	@ApiModelProperty("名称")
	protected String name;

	@XmlAttribute(name = "description")
	@TableField("desc_")
	@ApiModelProperty("描述名称")
	protected String desc;

	@XmlAttribute(name = "dataType")
	@TableField("data_type_")
	@ApiModelProperty("数据类型：string=字符串；number=数值；date=日期")
	protected String dataType;

	@XmlAttribute(name = "defaultValue")
	@TableField("default_value_")
	@ApiModelProperty("默认值")
	protected String defaultValue="";

	@XmlAttribute(name = "format")
	@TableField("format_")
	@ApiModelProperty("显示格式")
	protected String format="";

	@XmlAttribute(name = "isRequired")
	@TableField("is_required_")
	@ApiModelProperty("是否必填")
	protected int isRequired = BoConstants.REQUIRED_NO;

	@XmlAttribute(name = "attrLength")
	@TableField("attr_length_")
	@ApiModelProperty("属性长度")
	protected int attrLength = 0;

	@XmlAttribute(name = "decimalLen")
	@TableField("decimal_len_")
	@ApiModelProperty("浮点长度")
	protected int decimalLen = 0;
	
	@XmlAttribute(name = "sn")
	@TableField("sn_")
	@ApiModelProperty("排序号")
	protected int sn = 0;

	@XmlAttribute(name = "fieldName")
	@TableField("field_name_")
	@ApiModelProperty("数据库字段名称，外部表专用")
	protected String fieldName="";

	@XmlAttribute(name = "status")
	@TableField("status_")
	@ApiModelProperty("状态 show：显示 hide：隐藏")
	protected String status;

	@XmlAttribute(name = "index")
	@TableField("index_")
	@ApiModelProperty("索引")
	protected int index;

	/**
	 * 列实际类型
	 */
	@TableField(exist=false)
	protected String fcolumnType;
	@TableField(exist=false)
	private boolean isPk = false;
	@TableField(exist=false)
	private boolean isNull = false;
	@TableField(exist=false)
	private String tableName = "";
	@XmlTransient
	@TableField(exist=false)
	private BoEnt boEnt = null;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BoAttribute(){}

	public BoAttribute(String name, String desc, String dataType) {
		this.name = name;
		this.desc = desc;
		this.dataType = dataType;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public String getEntId() {
		return entId;
	}

	public void setEntId(String entId) {
		this.entId = entId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 属性名称
	 * 
	 * @return
	 */
	@Override
	public String getFieldName() {
		if(this.boEnt!=null && this.boEnt.isExternal()){
			if(StringUtil.isEmpty(this.fieldName)){
				return this.name;
			}
			return this.fieldName;
		}
		if(this.name.equalsIgnoreCase(BoEnt.FK_NAME) || this.name.equalsIgnoreCase(BoEnt.PK_NAME) ){
			return this.name;
		}
		return BoEnt.FIELD_PREFIX + this.name;
	}

	@Override
	public void setFieldName(String tmp) {
		this.fieldName = tmp;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 返回 属性描述
	 * 
	 * @return
	 */
	public String getDesc() {
		return this.desc;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * 返回 数据类型。varchar=字符串；number=数值；date=日期（长日期，通过显示格式来限制）；
	 * 
	 * @return
	 */
	public String getDataType() {
		return this.dataType;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * 返回 基本默认值
	 * 
	 * @return
	 */
	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * 返回 基本类型显示格式
	 * 
	 * @return
	 */
	public String getFormat() {
		if(StringUtil.isEmpty(format) && "date".equals(this.dataType)){
			return StringPool.DATE_FORMAT_DATE;
		}
		return this.format;
	}

	@Override
	public void setIsRequired(int isRequired) {
		this.isRequired = isRequired;
	}

	/**
	 * 返回 是否必填
	 * 
	 * @return
	 */
	@Override
	public int getIsRequired() {
		return this.isRequired;
	}

	/**
	 * 返回属性长度
	 * 
	 * @return
	 */
	public int getAttrLength() {
		return attrLength;
	}

	/**
	 * 返回 浮点长度
	 * 
	 * @return
	 */
	@Override
	public int getDecimalLen() {
		return decimalLen;
	}

	@Override
	public void setDecimalLen(int decimalLen) {
		this.decimalLen = decimalLen;
	}

	@Override
	public String getComment() {
		return this.desc;
	}

	@Override
	public boolean getIsPk() {
		return this.isPk;
	}

	@Override
	public boolean getIsNull() {
		return this.isNull;
	}

	@Override
	public String getColumnType() {
		return this.dataType;
	}

	@Override
	public int getCharLen() {
		return attrLength;
	}

	@Override
	public int getIntLen() {
		return attrLength;
	}

	@Override
	public void setColumnType(String columnType) {
		/*this.columnType = columnType;*/
		columnType = this.dataType;
	}

	@Override
	public void setComment(String comment) {
		this.desc = comment;

	}

	@Override
	public void setIsNull(boolean isNull) {
		this.isNull = isNull;

	}

	@Override
	public void setIsPk(boolean isPk) {
		this.isPk = isPk;

	}

	public void setAttrLength(int attrLength) {
		this.attrLength = attrLength;
	}

	@Override
	public void setCharLen(int charLen) {
	}

	@Override
	public void setIntLen(int intLen) {
	}

	@Override
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String getTableName() {
		return this.tableName;
	}

	@JsonIgnore
	public BoEnt getBoEnt() {
		return boEnt;
	}

	public void setBoEnt(BoEnt boEnt) {
		this.boEnt = boEnt;
	}

	@Override
	public String toString() {
		return "BaseAttribute [name=" + name + ", desc=" + desc + "]";
	}

	@Override
	public String getFcolumnType() {
		return this.fcolumnType;
	}

	@Override
	public void setFcolumnType(String fcolumnType) {
		this.fcolumnType = fcolumnType;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}
}