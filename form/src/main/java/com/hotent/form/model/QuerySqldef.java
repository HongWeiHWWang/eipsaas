package com.hotent.form.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 自定义SQL查询
 * <pre>
 * 描述：sys_query_sqldef 实体对象
 * 日期:2016-06-13 17:28:47
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@ApiModel("自定义SQL查询")
@TableName("form_query_sqldef")
public class QuerySqldef extends BaseModel<QuerySqldef> {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id;

	@ApiModelProperty("别名")
	@TableField("alias_")
	protected String alias;

	@ApiModelProperty("名称")
	@TableField("name_")
	protected String name;

	@ApiModelProperty("数据源名称")
	@TableField("ds_name_")
	protected String dsName;

	@ApiModelProperty("sql")
	@TableField("sql_")
	protected String sql;

	@ApiModelProperty("分类ID")
	@TableField("category_id_")
	protected String categoryId;

	@ApiModelProperty("支持TAB")
	@TableField("support_tab_")
	protected Short supportTab;

	@ApiModelProperty("按钮定义")
	@TableField("button_def_")
	protected String buttonDef;

	// 以下字段跟数据库无关
	@TableField(exist=false)
	private List<QueryMetafield> metafields = new ArrayList<QueryMetafield>();
	@TableField(exist=false)
	private List<QueryView> views = new ArrayList<QueryView>();

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 返回 ALIAS_
	 * 
	 * @return
	 */
	@XmlAttribute(name = "alias")
	public String getAlias() {
		return this.alias;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 NAME_
	 * 
	 * @return
	 */
	@XmlAttribute(name = "name")
	public String getName() {
		return this.name;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	/**
	 * 返回 DS_NAME_
	 * 
	 * @return
	 */
	@XmlAttribute(name = "dsName")
	public String getDsName() {
		return this.dsName;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * 返回 SQL_
	 * 
	 * @return
	 */
	@XmlElement(name = "sql")
	public String getSql() {
		return this.sql;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * 返回 CATEGORY_ID_
	 * 
	 * @return
	 */
	@XmlAttribute(name = "categoryId")
	public String getCategoryId() {
		return this.categoryId;
	}

	public void setSupportTab(Short supportTab) {
		this.supportTab = supportTab;
	}

	/**
	 * 返回 SUPPORT_TAB_
	 * 
	 * @return
	 */
	@XmlAttribute(name = "supportTab")
	public Short getSupportTab() {
		return this.supportTab;
	}

	public void setButtonDef(String buttonDef) {
		this.buttonDef = buttonDef;
	}

	/**
	 * 返回 BUTTON_DEF_
	 * 
	 * @return
	 */
	@XmlElement(name = "buttonDef")
	public String getButtonDef() {
		return this.buttonDef;
	}
	
	public List<QueryMetafield> getMetafields() {
		return metafields;
	}

	public void setMetafields(List<QueryMetafield> metafields) {
		this.metafields = metafields;
	}

	public List<QueryView> getViews() {
		return views;
	}

	public void setViews(List<QueryView> views) {
		this.views = views;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("alias", this.alias).append("name", this.name).append("dsName", this.dsName).append("sql", this.sql).append("categoryId", this.categoryId).append("supportTab", this.supportTab).append("buttonDef", this.buttonDef).toString();
	}
}