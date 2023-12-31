package com.hotent.form.model;

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
 * 自定义视图
 * <pre>
 * 描述：sys_query_view 实体对象
 * 日期:2016-06-13 17:26:55
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@ApiModel("自定义视图")
@TableName("form_query_view")
public class QueryView extends BaseModel<QueryView> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id;

	/**
	 * sql别名
	 */
	@ApiModelProperty("sql别名")
	@TableField("sql_alias_")
	protected String sqlAlias;

	/**
	 * 视图名称
	 */
	@ApiModelProperty("视图名称")
	@TableField("name_")
	protected String name;

	/**
	 * 视图别名
	 */
	@ApiModelProperty("视图别名")
	@TableField("alias_")
	protected String alias;

	/**
	 * 显示字段JSON数据
	 */
	@ApiModelProperty("显示字段")
	@TableField("shows_")
	protected String shows;

	/**
	 * 查询条件定义
	 */
	@ApiModelProperty("查询条件")
	@TableField("conditions_")
	protected String conditions;

	/**
	 * 过滤器类型
	 */
	@ApiModelProperty("过滤器类型")
	@TableField("filter_type_")
	protected Short filterType;

	/**
	 * 过滤器
	 */
	@ApiModelProperty("过滤器")
	@TableField("filter_")
	protected String filter;

	/**
	 * 按纽定义
	 */
	@ApiModelProperty("按钮定义")
	@TableField("buttons_")
	protected String buttons;

	/**
	 * 是否初始化查询
	 */
	@ApiModelProperty("是否初始化查询")
	@TableField("init_query_")
	protected Short initQuery;

	/**
	 * 模版定义
	 */
	@ApiModelProperty("模版定义")
	@TableField("template_")
	protected String template;

	/**
	 * 是否支持分组
	 */
	@ApiModelProperty("是否支持分组")
	@TableField("support_group_")
	protected Short supportGroup;

	/**
	 * 分组设定
	 */
	@ApiModelProperty("分组设定")
	@TableField("group_setting_")
	protected String groupSetting;

	/**
	 * 分页大小
	 */
	@ApiModelProperty("分页大小")
	@TableField("page_size_")
	protected Short pageSize;

	/**
	 * 显示行号
	 */
	@ApiModelProperty("显示行号")
	@TableField("show_rows_num_")
	protected Short showRowsNum;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	@TableField("sn_")
	protected Short sn;

	/**
	 * 是否分页
	 */
	@ApiModelProperty("是否分页")
	@TableField("need_page_")
	protected Short needPage;

	/**
	 * 摸版别名
	 */
	@ApiModelProperty("摸版别名")
	@TableField("TEMPLATE_ALIAS_")
	protected String templateAlias;
	
	// 以下字段跟数据库无关
	@TableField(exist=false)
	private Short rebuildTemp;
	
	/**
	 * 是否显示合并查询
	 */
	@TableField(exist=false)
	private String isIndistinct;

	/**
	 * 合并查询字段名称
	 */
	@TableField(exist=false)
	private String conditionAllName;
	
	/**
	 * 合并查询字段别名
	 */
	@TableField(exist=false)
	private String conditionAllDesc;

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 主键
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setSqlAlias(String sqlAlias) {
		this.sqlAlias = sqlAlias;
	}

	/**
	 * 返回 sql别名
	 * 
	 * @return
	 */
	@XmlAttribute(name = "sqlAlias")
	public String getSqlAlias() {
		return this.sqlAlias;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 视图名称
	 * 
	 * @return
	 */
	@XmlAttribute(name = "name")
	public String getName() {
		return this.name;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 返回 视图别名
	 * 
	 * @return
	 */
	@XmlAttribute(name = "alias")
	public String getAlias() {
		return this.alias;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	/**
	 * 返回 查询条件定义
	 * 
	 * @return
	 */
	@XmlElement(name = "conditions")
	public String getConditions() {
		return this.conditions;
	}

	@XmlElement(name = "shows")
	public String getShows() {
		return shows;
	}

	public void setShows(String shows) {
		this.shows = shows;
	}

	public void setFilterType(Short filterType) {
		this.filterType = filterType;
	}

	/**
	 * 返回 过滤器类型
	 * 
	 * @return
	 */
	@XmlAttribute(name = "filterType")
	public Short getFilterType() {
		return this.filterType;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * 返回 过滤器
	 * 
	 * @return
	 */
	@XmlElement(name = "filter")
	public String getFilter() {
		return this.filter;
	}

	public void setButtons(String buttons) {
		this.buttons = buttons;
	}

	/**
	 * 返回 按纽定义
	 * 
	 * @return
	 */
	@XmlElement(name = "buttons")
	public String getButtons() {
		return this.buttons;
	}

	public void setInitQuery(Short initQuery) {
		this.initQuery = initQuery;
	}

	/**
	 * 返回 是否初始化查询
	 * 
	 * @return
	 */
	@XmlAttribute(name = "initQuery")
	public Short getInitQuery() {
		return this.initQuery;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * 返回 模版定义
	 * 
	 * @return
	 */
	@XmlElement(name = "template")
	public String getTemplate() {
		return this.template;
	}

	public void setSupportGroup(Short supportGroup) {
		this.supportGroup = supportGroup;
	}

	/**
	 * 返回 是否支持分组
	 * 
	 * @return
	 */
	@XmlAttribute(name = "supportGroup")
	public Short getSupportGroup() {
		return this.supportGroup;
	}

	public void setGroupSetting(String groupSetting) {
		this.groupSetting = groupSetting;
	}

	/**
	 * 返回 分组设定
	 * 
	 * @return
	 */
	@XmlElement(name = "groupSetting")
	public String getGroupSetting() {
		return this.groupSetting;
	}

	public void setPageSize(Short pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回 分页大小
	 * 
	 * @return
	 */
	@XmlAttribute(name = "pageSize")
	public Short getPageSize() {
		return this.pageSize;
	}

	public void setShowRowsNum(Short showRowsNum) {
		this.showRowsNum = showRowsNum;
	}

	/**
	 * 返回 显示行号
	 * 
	 * @return
	 */
	@XmlAttribute(name = "showRowsNum")
	public Short getShowRowsNum() {
		return this.showRowsNum;
	}

	public void setSn(Short sn) {
		this.sn = sn;
	}

	/**
	 * 返回 排序
	 * 
	 * @return
	 */
	@XmlAttribute(name = "sn")
	public Short getSn() {
		return this.sn;
	}

	public void setNeedPage(Short needPage) {
		this.needPage = needPage;
	}

	/**
	 * 返回 是否分页
	 * 
	 * @return
	 */
	@XmlAttribute(name = "needPage")
	public Short getNeedPage() {
		return this.needPage;
	}

	public void setTemplateAlias(String templateAlias) {
		this.templateAlias = templateAlias;
	}

	/**
	 * 返回 摸版别名
	 * 
	 * @return
	 */
	@XmlAttribute(name = "templateAlias")
	public String getTemplateAlias() {
		return this.templateAlias;
	}
	
	
	public Short getRebuildTemp() {
		return rebuildTemp;
	}

	public void setRebuildTemp(Short rebuildTemp) {
		this.rebuildTemp = rebuildTemp;
	}

	public String getIsIndistinct() {
		return isIndistinct;
	}

	public void setIsIndistinct(String isIndistinct) {
		this.isIndistinct = isIndistinct;
	}

	public String getConditionAllName() {
		return conditionAllName;
	}

	public void setConditionAllName(String conditionAllName) {
		this.conditionAllName = conditionAllName;
	}

	public String getConditionAllDesc() {
		return conditionAllDesc;
	}

	public void setConditionAllDesc(String conditionAllDesc) {
		this.conditionAllDesc = conditionAllDesc;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("sqlAlias", this.sqlAlias).append("name", this.name).append("alias", this.alias).append("conditions", this.conditions).append("filterType", this.filterType).append("filter", this.filter).append("buttons", this.buttons).append("initQuery", this.initQuery).append("template", this.template).append("supportGroup", this.supportGroup).append("groupSetting", this.groupSetting).append("pageSize", this.pageSize).append("showRowsNum", this.showRowsNum).append("sn", this.sn).append("needPage", this.needPage).append("templateAlias", this.templateAlias).toString();
	}
}