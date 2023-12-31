package com.hotent.bo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotent.base.constants.SQLConst;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bo.constant.BoConstants;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * BO实体
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
@ApiModel("BO实体")
@TableName("form_bo_ent")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoEnt extends BaseModel<BoEnt> implements Table {
	/**
	 * 主键字段名称
	 */
	public static String PK_NAME = "ID_";
	/**
	 * 外键字段名称
	 */
	public static String FK_NAME = "REF_ID_";
	/**
	 * 表前缀。
	 */
	public static String TABLE_PREFIX = SQLConst.CUSTOMER_TABLE_PREFIX;
	/**
	 * 列前缀。
	 */
	public static String FIELD_PREFIX = "F_";
	
	/**
	 * 未激活
	 */
	public static String STATUS_INACTIVE = "inactive";
	/**
	 * 激活
	 */
	public static String STATUS_ACTIVED = "actived";
	/**
	 * 禁用
	 */
	public static String STATUS_FORBIDDEN = "forbidden";

	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "id")
	@TableId("id_")
	@ApiModelProperty("主键")
	private String id = "";

	@XmlAttribute(name = "name")
	@TableField("name_")
	@ApiModelProperty("名称")
	private String name = "";

	@XmlAttribute(name = "description")
	@TableField("desc_")
	@ApiModelProperty("描述")
	private String desc = "";
	
	@XmlAttribute(name = "packageId")
	@TableField("package_id_")
	@ApiModelProperty("分类ID")
	protected String packageId;
	
	@TableField("ds_name_")
	@ApiModelProperty("数据源名称")
	protected String dsName;

	@XmlAttribute(name = "tableName")
	@TableField("table_name_")
	@ApiModelProperty("物理表名")
	protected String tableName;
	
	@XmlAttribute(name = "isExternal")
	@TableField("is_external_")
	@ApiModelProperty("是否外部表")
	protected Short isExternal=0;
	
	@TableField("status_")
	@ApiModelProperty("状态：enabled：正常，forbidden：隐藏")
	protected String status ="enabled";
	
	@TableField("is_create_table_")
	@ApiModelProperty("是否生成表")
	protected Short isCreateTable=0;

	@XmlAttribute(name = "pk")
	@TableField("pk_")
	@ApiModelProperty("主键字段")
	protected String pk;

	@XmlAttribute(name = "fk")
	@TableField("fk_")
	@ApiModelProperty("外键字段")
	protected String fk;
	
	@XmlAttribute(name = "pkType")
	@TableField("pk_type_")
	@ApiModelProperty("主键值类型")
	protected String pkType;
	
	@XmlAttribute(name = "type")
	@TableField(exist=false)
	protected String type = BoConstants.RELATION_MAIN;
	
	@TableField(exist=false)
	protected String tableNameNoPrefix;

	@XmlElement(name = "attributeList")
	@TableField(exist=false)
	private List<BoAttribute> boAttrList = new ArrayList<BoAttribute>();

	@XmlElementWrapper(name = "ents")
	@XmlElement(name = "ent", type = BoEnt.class)
	@TableField(exist=false)
	private List<BoEnt> childEntList = new ArrayList<BoEnt>();
	
	@TableField(exist=false)
	private String show;//实体类型
	
	@TableField(exist=false)
	private String relation;// 关系
	
	@XmlTransient
	@TableField(exist=false)
	private List<Column> columnList = new ArrayList<Column>();

	@XmlTransient
	@TableField(exist=false)
	private Map<String, BoEnt> childMapList = new HashMap<String, BoEnt>();

	@XmlTransient
	@JsonIgnore
	@TableField(exist=false)
	private Map<String, BoAttribute> attributeMap = new HashMap<String, BoAttribute>();
	
	@XmlTransient
	@TableField(exist=false)
	private Map<String, BoAttribute> attrFieldMap = new HashMap<String, BoAttribute>();
	
	/**
	 * 是否可以编辑实体
	 */
	@TableField(exist=false)
	private boolean canEditTable;
	
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String description) {
		this.desc = description;
	}
	
	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	
	/**
	 * 获取当前实体对对于父实体的关系。
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置实体关系。
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("attributeList")
	public List<BoAttribute> getBoAttrList() {
		return boAttrList;
	}

	public void setBoAttrList(List<BoAttribute> boAttrList) {
		this.boAttrList = boAttrList;
		for (BoAttribute attribute : boAttrList) {
			// 设置BoEnt
			attribute.setBoEnt(this);
			this.attributeMap.put(attribute.getName().toLowerCase(), attribute);
			this.attrFieldMap.put(attribute.getFieldName().toLowerCase(), attribute);
			// 添加列
			this.columnList.add(attribute);
			
		}
	}
	
	/**
	 * 获取初始数据。
	 * @return
	 */
	@JsonIgnore
	public Map<String,Object> getInitData(){
		Map<String,Object> map=new HashMap<String, Object>();
		for (BoAttribute attr : boAttrList) {
			String val=StringUtil.isEmpty(attr.getDefaultValue())?"":attr.getDefaultValue();
			map.put(attr.getName(), val);
		}
		return map;
	}

	@JsonProperty("childEnts")
	@JsonIgnore
	public List<BoEnt> getChildEntList() {
		return childEntList;
	}

	public void setChildEntList(List<BoEnt> childEntList) {
		this.childEntList = childEntList;
		for (BoEnt ent : childEntList) {
			childMapList.put(ent.getName().toLowerCase(), ent);
		}
	}

	/**
	 * 添加实体类。
	 * 
	 * @param ent
	 */
	public void addEnt(BoEnt ent) {
		this.childEntList.add(ent);
		childMapList.put(ent.getName().toLowerCase(), ent);
	}

	@JsonIgnore
	public Map<String, BoEnt> getChildMap() {
		return childMapList;
	}

	/**
	 * 根据键获取BoAttribute。
	 * 
	 * @param key
	 * @return
	 */
	public BoAttribute getAttribute(String key) {
		return this.attributeMap.get(key.toLowerCase());
	}
	
	/**
	 * 根据字段名获取属性
	 * @param fieldName
	 * @return
	 */
	public BoAttribute getAttrByField(String fieldName){
		//主键
		if(fieldName.equalsIgnoreCase(this.getPkKey())){
			BoAttribute attribute = new BoAttribute(this.getPkKey(), "", "");
			attribute.setBoEnt(this);
			attribute.setFieldName(fieldName);
			return attribute;
		}
		//外键
		if(fieldName.equalsIgnoreCase(this.getFk())){
			BoAttribute attribute=new BoAttribute(this.getFk(), "", "");
			attribute.setBoEnt(this);
			attribute.setFieldName(fieldName);
			return attribute;
		}
		return this.attrFieldMap.get(fieldName.toLowerCase());
	}

	/**
	 * 添加属性。
	 * 
	 * @param attribute
	 */
	public void addAttr(BoAttribute attribute) {
		attribute.setBoEnt(this);
		this.boAttrList.add(attribute);
		this.attributeMap.put(attribute.name.toLowerCase(), attribute);
		this.attrFieldMap.put(attribute.getFieldName().toLowerCase(), attribute);
	}

	public void addAttrFirst(BoAttribute attribute) {
		attribute.setBoEnt(this);
		this.boAttrList.add(0, attribute);
		this.attributeMap.put(attribute.getName().toLowerCase(), attribute);
		this.attrFieldMap.put(attribute.getFieldName().toLowerCase(), attribute);
	}

	@Override
	public String getComment() {
		return this.desc;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@JsonIgnore
	public List getColumnList() {
		return this.boAttrList;
	}

	@Override
	@JsonIgnore
	public List<Column> getPrimayKey() {
		List<Column> list = new ArrayList<Column>();
		for (Column col : columnList) {
			if (col.getIsPk()) {
				list.add(col);
			}
		}
		return list;
	}

	@Override
	public void setComment(String comment) {
		this.desc = comment;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setColumnList(List columns) {
		this.boAttrList = columns;
	}

	@Override
	public void addColumn(Column column) {
		this.boAttrList.add((BoAttribute) column);
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	/**
	 * 返回 数据源名称
	 * 
	 * @return
	 */
	public String getDsName() {
		return this.dsName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 返回 表名
	 * 
	 * @return
	 */
	public String getTableName() {
		if(isExternal()){
			return this.tableName;
		}
		if(StringUtil.isEmpty(this.name)) {
			return this.name;
		}
		MultiTenantHandler multiTenantHandler = AppUtil.getBean(MultiTenantHandler.class);
		String tenantCode = multiTenantHandler.getTenantCode();
		if(StringUtil.isNotEmpty(tenantCode)) {
			return String.format("%s%s_%s", TABLE_PREFIX, tenantCode, this.name);
		}
		return TABLE_PREFIX+this.name;
	}
	
	public void setTableNameNoPrefix(String tableNameNoPrefix) {
		this.tableNameNoPrefix = tableNameNoPrefix;
	}
	
	public String getTableNameNoPrefix() {
		return this.tableNameNoPrefix;
	}

	public void setIsExternal(Short isExternal) {
		this.isExternal = isExternal;
	}

	/**
	 * 返回 是否外部表
	 * 
	 * @return
	 */
	public Short getIsExternal() {
		return this.isExternal;
	}

	/**
	 * 是否为外部表。
	 * 
	 * @return
	 */
	public boolean isExternal() {
		if (this.isExternal == null)
			return false;
		return this.isExternal == 1;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	/**
	 * 返回 pk_
	 * 
	 * @return
	 */
	public String getPk() {
		return this.pk;
	}

	public void setFk(String fk) {
		this.fk = fk;
	}

	public String getPkKey() {
		if (isExternal()) {
			return this.pk.toLowerCase();
		}
		return BoEnt.PK_NAME.toLowerCase();
	}

	/**
	 * 返回 fk_
	 * 
	 * @return
	 */
	public String getFk() {
		if (isExternal()) {
			if(StringUtil.isEmpty(fk)){
				return "";
			}
			return this.fk.toLowerCase();
		}
		return BoEnt.FK_NAME.toLowerCase();
	}

	public void setPkType(String pkType) {
		this.pkType = pkType;
	}

	/**
	 * 返回 主键类型，主要用于外部表的时候
	 * <pre>
	 * number
	 * varchar
	 * </pre>
	 * @return
	 */
	public String getPkType() {
		return this.pkType;
	}
	
	/**
	 * 主键类型是否为数字。
	 * @return
	 */
	public boolean isPkNumber(){
		//自定义表主键为字符串类型。
		if(this.isExternal()){
			return Column.COLUMN_TYPE_NUMBER.equalsIgnoreCase(this.pkType);
		}
		return false;
	}

	@JsonIgnore
	public Map<String, BoAttribute> getAttrFieldMap() {
		return attrFieldMap;
	}

	public void setAttrFieldMap(Map<String, BoAttribute> attrFieldMap) {
		this.attrFieldMap = attrFieldMap;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isCreatedTable() {
		return BoConstants.BOOLEAN_YES_SHORT.equals(this.isCreateTable);
	}

	public void setIsCreatedTable(boolean isCreateTable) {
		this.isCreateTable = isCreateTable ? BoConstants.BOOLEAN_YES_SHORT:BoConstants.BOOLEAN_NO_SHORT;
	}

	public boolean isCanEditTable() {
		return canEditTable;
	}
	public void setCanEditTable(boolean canEditTable) {
		this.canEditTable = canEditTable;
	}
	@Override
	public String toString() {
		return "BoEnt [name=" + name + ", desc=t" + desc + ", packageId=" + packageId + ", type=" + type + "]";
	}
}