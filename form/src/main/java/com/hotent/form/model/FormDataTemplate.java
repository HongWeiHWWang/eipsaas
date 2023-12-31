package com.hotent.form.model;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 业务数据模板
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月13日
 */
@ApiModel("业务数据模板")
@TableName("form_data_template")
public class FormDataTemplate extends AutoFillModel<FormDataTemplate> {
	private static final long serialVersionUID = 1L;

	/** 参数标识(当前路径)*/
	public static final  String PARAMS_KEY_CTX=  "__ctx";

	public static final String PARAMS_KEY_ALIAS = "alias";

	/** 新增*/
	public static final  String MANAGE_TYPE_ADD="add";
	/** 编辑 */
	public static final  String MANAGE_TYPE_EDIT="edit";
	/** 删除*/
	public static final  String MANAGE_TYPE_DEL="del";
	/** 明细 */
	public static final  String MANAGE_TYPE_DETAIL="detail";
	/** 导出 */
	public static final  String MANAGE_TYPE_EXPORT="export";

	/** 启动流程 */
	public static final String MANAGE_TYPE_START_FLOW = "startFlow";

	/** boAlias */
	public static final String PARAMS_KEY_BOALIAS = "boAlias";

	/** boAlias */
	public static final String PARAMS_KEY_FORM_KEY = "formKey";

	/** 流程定义id */
	public static final String PARAMS_KEY_DEF_ID = "defId";

	/** filterKey */
	public static final String PARAMS_KEY_FILTER_KEY = "filterKey";


	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id;

	/**
	 * 业务对象定义id
	 */
	@ApiModelProperty("业务对象定义id")
	@TableField("bo_def_id_")
	protected String boDefId;

	/**
	 * 业务对象定义别名
	 */
	@ApiModelProperty("业务对象定义别名")
	@TableField("bo_def_alias_")
	protected String boDefAlias;

	/**
	 * 自定义表单key
	 */
	@ApiModelProperty("自定义表单key")
	@TableField("form_key_")
	protected String formKey;

	/**
	 * 名称
	 */
	@ApiModelProperty("名称")
	@TableField("name_")
	protected String name;

	/**
	 * 别名
	 */
	@ApiModelProperty("别名")
	@TableField("alias_")
	protected String alias;

	/**
	 * 样式
	 */
	@ApiModelProperty("样式")
	@TableField("style_")
	protected Short style;

	/**
	 * 是否需要分页
	 */
	@ApiModelProperty("是否需要分页")
	@TableField("need_page_")
	protected Short needPage;

	/**
	 * 分页大小
	 */
	@ApiModelProperty("分页大小")
	@TableField("page_size_")
	protected Short pageSize;

	/**
	 * 数据模板别名
	 */
	@ApiModelProperty("数据模板别名")
	@TableField("template_alias_")
	protected String templateAlias;

	/**
	 * 数据模板代码
	 */
	@ApiModelProperty("数据模板代码")
	@TableField("template_html_")
	protected String templateHtml;

	/**
	 * 显示字段
	 */
	@ApiModelProperty("显示字段")
	@TableField("display_field_")
	protected String displayField;

	/**
	 * 排序字段
	 */
	@ApiModelProperty("排序字段")
	@TableField("sort_field_")
	protected String sortField;

	/**
	 * 条件字段
	 */
	@ApiModelProperty("条件字段")
	@TableField("condition_field_")
	protected String conditionField;

	/**
	 * 管理字段
	 */
	@ApiModelProperty("管理字段")
	@TableField("manage_field_")
	protected String manageField;

	/**
	 * 过滤条件
	 */
	@ApiModelProperty("过滤条件")
	@TableField("filter_field_")
	protected String filterField;

	/**
	 * 变量字段
	 */
	@ApiModelProperty("变量字段")
	@TableField("var_field_")
	protected String varField;

	/**
	 * 过滤类型（1.建立条件,2.脚本条件）
	 */
	@ApiModelProperty("过滤类型（1.建立条件,2.脚本条件）")
	@TableField("filter_type_")
	protected Short filterType;

	/**
	 * 数据来源
	 */
	@ApiModelProperty("数据来源")
	@TableField("source_")
	protected Short source;

	/**
	 * 流程定义ID
	 */
	@ApiModelProperty("流程定义ID")
	@TableField("def_id_")
	protected String defId;

	/**
	 * 绑定的流程名称
	 */
	@ApiModelProperty("绑定的流程名称")
	@TableField("subject_")
	protected String subject;

	/**
	 * 是否查询
	 */
	@ApiModelProperty("是否查询")
	@TableField("is_query_")
	protected Short isQuery;

	/**
	 * 是否过滤
	 */
	@ApiModelProperty("是否过滤")
	@TableField("is_filter_")
	protected Short isFilter;

	/**
	 * 导出字段
	 */
	@ApiModelProperty("导出字段")
	@TableField("export_field_")
	protected String exportField;

	/**
	 * 打印字段
	 */
	@ApiModelProperty("打印字段")
	@TableField("print_field_")
	protected String printField;

	/**
	 *	表单字段
	 */
	@ApiModelProperty("表单字段")
	@TableField("form_field_")
	protected String formField;

	@ApiModelProperty("分类ID")
	@TableField("type_id_")
	protected String typeId;

	@ApiModelProperty("分类名称")
	@TableField("type_name_")
	protected String typeName;

	/**
	 * 手机表单KEY
	 */
	@ApiModelProperty("手机表单KEY")
	@TableField("mobile_form_alias_")
	protected String mobileFormAlias;
	
	/**
	 * 手机表单名称
	 */
	@ApiModelProperty("手机表单名称")
	@TableField("mobile_form_name_")
	protected String mobileFormName;

	/**
	 * 过滤树条件
	 */
	@ApiModelProperty("过滤树条件")
	@TableField("tree_field_")
	protected String treeField;

	/**
	 * 过滤字段
	 */
	@ApiModelProperty("过滤字段")
	@TableField("filtering_field_")
	protected String filteringField;
	

	/**
	 * 是否显示合并查询
	 */
	@ApiModelProperty("是否显示合并查询")
	@TableField(exist=false)
	protected String isIndistinct;

	/**
	 * 合并查询字段名称
	 */
	@ApiModelProperty("合并查询字段名称")
	@TableField(exist=false)
	protected String conditionAllName;

	/**
	 * 合并查询字段别名
	 */
	@ApiModelProperty("合并查询字段别名")
	@TableField(exist=false)
	protected String conditionAllDesc;

	public String getTreeField() {
		return treeField;
	}

	public void setTreeField(String treeField) {
		this.treeField = treeField;
	}

	public String getMobileFormName() {
		return mobileFormName;
	}

	public void setMobileFormName(String mobileFormName) {
		this.mobileFormName = mobileFormName;
	}

	public String getMobileFormAlias() {
		return mobileFormAlias;
	}

	public void setMobileFormAlias(String mobileFormAlias) {
		this.mobileFormAlias = mobileFormAlias;
	}

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

	public void setBoDefId(String boDefId) {
		this.boDefId = boDefId;
	}

	/**
	 * 返回 自定义表ID
	 * @return
	 */
	public String getBoDefId() {
		return this.boDefId;
	}

	public String getBoDefAlias() {
		return boDefAlias;
	}

	public void setBoDefAlias(String boDefAlias) {
		this.boDefAlias = boDefAlias;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	/**
	 * 返回 自定义表单key
	 * @return
	 */
	public String getFormKey() {
		return this.formKey;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 返回 别名
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}

	public void setStyle(Short style) {
		this.style = style;
	}

	/**
	 * 返回 样式
	 * @return
	 */
	public Short getStyle() {
		return this.style;
	}

	public void setNeedPage(Short needPage) {
		this.needPage = needPage;
	}

	/**
	 * 返回 是否需要分页
	 * @return
	 */
	public Short getNeedPage() {
		return this.needPage;
	}

	public void setPageSize(Short pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回 分页大小
	 * @return
	 */
	public Short getPageSize() {
		return this.pageSize;
	}

	public void setTemplateAlias(String templateAlias) {
		this.templateAlias = templateAlias;
	}

	/**
	 * 返回 数据模板别名
	 * @return
	 */
	public String getTemplateAlias() {
		return this.templateAlias;
	}

	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}

	/**
	 * 返回 数据模板代码
	 * @return
	 */
	public String getTemplateHtml() {
		return this.templateHtml;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	/**
	 * 返回 显示字段
	 * @return
	 */
	public String getDisplayField() {
		return this.displayField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	/**
	 * 返回 排序字段
	 * @return
	 */
	public String getSortField() {
		return this.sortField;
	}

	public void setConditionField(String conditionField) {
		this.conditionField = conditionField;
	}

	/**
	 * 返回 条件字段
	 * @return
	 */
	public String getConditionField() {
		return this.conditionField;
	}

	public void setManageField(String manageField) {
		this.manageField = manageField;
	}

	/**
	 * 返回 管理字段
	 * @return
	 */
	public String getManageField() {
		return this.manageField;
	}

	public void setFilterField(String filterField) {
		this.filterField = filterField;
	}

	/**
	 * 返回 过滤条件
	 * @return
	 */
	public String getFilterField() {
		return this.filterField;
	}

	public void setVarField(String varField) {
		this.varField = varField;
	}

	/**
	 * 返回 变量字段
	 * @return
	 */
	public String getVarField() {
		return this.varField;
	}

	public void setFilterType(Short filterType) {
		this.filterType = filterType;
	}

	/**
	 * 返回 过滤类型（1.建立条件,2.脚本条件）
	 * @return
	 */
	public Short getFilterType() {
		return this.filterType;
	}

	public void setSource(Short source) {
		this.source = source;
	}

	/**
	 * 返回 数据来源
	 * @return
	 */
	public Short getSource() {
		return this.source;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	/**
	 * 返回 流程定义ID
	 * @return
	 */
	public String getDefId() {
		return this.defId;
	}

	public void setIsQuery(Short isQuery) {
		this.isQuery = isQuery;
	}

	/**
	 * 返回 是否查询
	 * @return
	 */
	public Short getIsQuery() {
		return this.isQuery;
	}

	public void setIsFilter(Short isFilter) {
		this.isFilter = isFilter;
	}

	/**
	 * 返回 是否过滤
	 * @return
	 */
	public Short getIsFilter() {
		return this.isFilter;
	}

	public void setExportField(String exportField) {
		this.exportField = exportField;
	}

	/**
	 * 返回 导出字段
	 * @return
	 */
	public String getExportField() {
		return this.exportField;
	}

	public void setPrintField(String printField) {
		this.printField = printField;
	}

	/**
	 * 返回 打印字段
	 * @return
	 */
	public String getPrintField() {
		return this.printField;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getFormField() {
		return formField;
	}

	public void setFormField(String formField) {
		this.formField = formField;
	}
	
	public String getFilteringField() {
		return filteringField;
	}

	public String getFilteringFieldMap() {
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotEmpty(filteringField)) {
			try {
				ArrayNode arrayNode = (ArrayNode) JsonUtil.toJsonNode(filteringField);
				for (JsonNode jsonNode : arrayNode) {
					map.put(jsonNode.get("name").asText(), jsonNode.get("formatterData"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return JsonUtil.toJsonString(map);
	}
	
	public void setFilteringField(String filteringField) {
		this.filteringField = filteringField;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", this.id)
				.append("boDefId", this.boDefId)
				.append("formKey", this.formKey)
				.append("name", this.name)
				.append("alias", this.alias)
				.append("style", this.style)
				.append("needPage", this.needPage)
				.append("pageSize", this.pageSize)
				.append("templateAlias", this.templateAlias)
				.append("templateHtml", this.templateHtml)
				.append("displayField", this.displayField)
				.append("sortField", this.sortField)
				.append("conditionField", this.conditionField)
				.append("manageField", this.manageField)
				.append("filterField", this.filterField)
				.append("varField", this.varField)
				.append("filterType", this.filterType)
				.append("source", this.source)
				.append("defId", this.defId)
				.append("subject", this.subject)
				.append("isQuery", this.isQuery)
				.append("isFilter", this.isFilter)
				.append("exportField", this.exportField)
				.append("printField", this.printField)
				.append("isIndistinct", this.isIndistinct)
				.append("conditionAllName", this.conditionAllName)
				.append("conditionAllDesc", this.conditionAllDesc)
				.toString();
	}

}
