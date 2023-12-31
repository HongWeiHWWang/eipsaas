package com.hotent.form.model;


import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.typehandle.ShortTypeHandle;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 自定义对话框对象
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月13日
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description="自定义对话框对象")
@TableName("form_custom_dialog")
public class CustomDialog extends AutoFillModel<CustomDialog> {
	private static final long serialVersionUID = 1L;
	/**数据源*/
	public static String DS_TYPE_DATASOURCE = "dataSource";
	/**restful接口*/
	public static String DS_TYPE_RESTFUL = "restful";
	
	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id; /* 主键 */
	
	@ApiModelProperty("名字")
	@TableField("name_")
	protected String name; /* 名字 */
	
	@ApiModelProperty("别名")
	@TableField("alias_")
	protected String alias; /* 别名 */
	
	@ApiModelProperty(notes="显示样式(0:列表 1:树形)", allowableValues="0,1")
	@TableField("style_")
	protected Short style; /* 显示样式：0-列表，1-树形 */
	
	@ApiModelProperty("对象名称，如果是表就是表名，视图则视图名")
	@TableField("obj_name_")
	protected String objName; /* 对象名称，如果是表就是表名，视图则视图名 */
	
	@ApiModelProperty("是否分页 ")
	@TableField(value = "need_page_", typeHandler = ShortTypeHandle.class)
	protected Boolean needPage; /* 是否分页 */
	
	@ApiModelProperty("分页大小")
	@TableField("page_size_")
	protected Integer pageSize; /* 分页大小 */
	
	@ApiModelProperty("显示字段")
	@TableField("displayfield_")
	protected String displayfield; /* 显示字段 */
	
	@ApiModelProperty("条件字段的json")
	@TableField("conditionfield_")
	protected String conditionfield; /* 条件字段的json */
	
	@ApiModelProperty("返回字段json")
	@TableField("resultfield_")
	protected String resultfield; /* 返回字段json */
	
	@ApiModelProperty("排序字段")
	@TableField("sortfield_")
	protected String sortfield; /* 排序字段 */
	
	@ApiModelProperty("数据源的类型(dataSource：数据源，restful：restful接口)")
	@TableField("ds_type_")
	protected String dsType; /* 数据源的别名 */
	
	@ApiModelProperty("数据源的别名")
	@TableField("dsalias_")
	protected String dsalias; /* 数据源的别名 */
	
	@ApiModelProperty(name="isTable", notes="是否数据库表(0:视图 1:数据库表)",allowableValues="0,1")
	@TableField("is_table_")
	protected Short isTable; /* 是否数据库表0:视图,1:数据库表 */
	
	@ApiModelProperty("diy的sql语句")
	@TableField("diy_sql_")
	protected String diySql; /* diy的sql语句 */
	
	@ApiModelProperty(name="sqlBuildType", notes="sql生成的方式(0:页面设置 1:diy)",allowableValues="0,1")
	@TableField("sql_build_type_")
	protected Short sqlBuildType; /* sql生成的方式：0-页面设置；1-diy */
	
	@ApiModelProperty("弹出框的宽度")
	@TableField("width_")
	protected Integer width; /* 弹出框的宽度 */
	
	@ApiModelProperty("弹出框的高度")
	@TableField("height_")
	protected Integer height; /* 弹出框的高度 */
	
	@ApiModelProperty(name="selectNum", notes="选择的人数(-1:多选 1:单选)",allowableValues="-1,1")
	@TableField("select_num_")
	protected Integer selectNum; /* 选择的人数 -1:多选 */
	
	@ApiModelProperty("是否系统内部的")
	@TableField(value = "system_", typeHandler = ShortTypeHandle.class)
	protected Boolean system; /* 是否系统内部的 */
	
	@ApiModelProperty(name="parentCheck", notes="树多选时父级联动 (1:草稿  0:false)", allowableValues="1,0")
	@TableField("parent_check_")
	protected Short parentCheck;//树多选时父级联动 1-true,0-false
	
	@ApiModelProperty(name="childrenCheck",  notes="树多选时子级联动 (1:草稿  0:false)", allowableValues="1,0")
	@TableField("children_check_")
	protected Short childrenCheck;//树多选时子级联动 1-true,0-false
	
	@ApiModelProperty("接口地址")
	@TableField("url_")
	protected String url; /*接口地址*/
	
	@ApiModelProperty(name="header", notes="接口请求的头部")
	@TableField("header_")
	protected String header;
	
	@ApiModelProperty("请求地址类型：POST、GET（当数据加载方式为RESTFUL时）")
	@TableField("request_type_")
	protected String requestType;
	
	@ApiModelProperty("数据参数模板")
	@TableField("data_param_")
	protected String  dataParam;
	
	@ApiModelProperty("页号（key）")
	@TableField("page_key_")
	protected String  pageKey;
	
	@ApiModelProperty("分页大小(key)")
	@TableField("pagesize_key_")
	protected String  pageSizeKey;
	
	@ApiModelProperty("总条数（key）")
	@TableField("total_key_")
	protected String  totalKey;
	
	@ApiModelProperty("列表(key)")
	@TableField("list_key_")
	protected String  listKey;
	
	
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

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 名字
	 * 
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
	 * 
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}

	public void setStyle(Short style) {
		this.style = style;
	}

	/**
	 * 返回 显示样式：0-列表，1-树形
	 * 
	 * @return
	 */
	public Short getStyle() {
		return this.style;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	/**
	 * 返回 对象名称，如果是表就是表名，视图则视图名
	 * 
	 * @return
	 */
	public String getObjName() {
		return this.objName;
	}

	/**
	 * needPage
	 * @return  the needPage
	 * @since   1.0.0
	 */
	
	public Boolean getNeedPage() {
		return needPage;
	}

	/**
	 * @param needPage the needPage to set
	 */
	public void setNeedPage(Boolean needPage) {
		this.needPage = needPage;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回 分页大小
	 * 
	 * @return
	 */
	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setDisplayfield(String displayfield) {
		this.displayfield = displayfield;
	}

	/**
	 * 返回 显示字段
	 * 
	 * @return
	 */
	public String getDisplayfield() {
		return this.displayfield;
	}

	public void setConditionfield(String conditionfield) {
		this.conditionfield = conditionfield;
	}

	/**
	 * 返回 条件字段的json
	 * 
	 * @return
	 */
	public String getConditionfield() {
		return this.conditionfield;
	}

	public void setResultfield(String resultfield) {
		this.resultfield = resultfield;
	}

	/**
	 * 返回 返回字段json
	 * 
	 * @return
	 */
	public String getResultfield() {
		return this.resultfield;
	}

	public void setSortfield(String sortfield) {
		this.sortfield = sortfield;
	}

	/**
	 * 返回 排序字段
	 * 
	 * @return
	 */
	public String getSortfield() {
		return this.sortfield;
	}

	public void setDsalias(String dsalias) {
		this.dsalias = dsalias;
	}

	/**
	 * 返回 数据源的别名
	 * 
	 * @return
	 */
	public String getDsalias() {
		return this.dsalias;
	}

	public void setIsTable(Short isTable) {
		this.isTable = isTable;
	}

	/**
	 * 返回 是否数据库表0:视图,1:数据库表
	 * 
	 * @return
	 */
	public Short getIsTable() {
		return this.isTable;
	}

	public void setDiySql(String diySql) {
		this.diySql = diySql;
	}

	/**
	 * 返回 diy的sql语句
	 * 
	 * @return
	 */
	public String getDiySql() {
		return this.diySql;
	}

	public void setSqlBuildType(Short sqlBuildType) {
		this.sqlBuildType = sqlBuildType;
	}

	/**
	 * 返回 sql生成的方式：0-页面设置；1-diy
	 * 
	 * @return
	 */
	public Short getSqlBuildType() {
		return this.sqlBuildType;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * 返回 弹出框的宽度
	 * 
	 * @return
	 */
	public Integer getWidth() {
		return this.width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * 返回 弹出框的高度
	 * 
	 * @return
	 */
	public Integer getHeight() {
		return this.height;
	}

	public void setSelectNum(Integer selectNum) {
		this.selectNum = selectNum;
	}

	/**
	 * 返回 选择的人数 -1:多选
	 * 
	 * @return
	 */
	public Integer getSelectNum() {
		return this.selectNum;
	}

	/**
	 * system
	 * @return  the system
	 * @since   1.0.0
	 */
	
	public Boolean getSystem() {
		return system;
	}

	/**
	 * @param system the system to set
	 */
	public void setSystem(Boolean system) {
		this.system = system;
	}
	
	public Short getParentCheck() {
		return parentCheck;
	}

	public void setParentCheck(Short parentCheck) {
		this.parentCheck = parentCheck;
	}

	public Short getChildrenCheck() {
		return childrenCheck;
	}

	public void setChildrenCheck(Short childrenCheck) {
		this.childrenCheck = childrenCheck;
	}

	public String getDsType() {
		return dsType;
	}

	public void setDsType(String dsType) {
		this.dsType = dsType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getDataParam() {
		return dataParam;
	}

	public void setDataParam(String dataParam) {
		this.dataParam = dataParam;
	}

	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	public String getPageSizeKey() {
		return pageSizeKey;
	}

	public void setPageSizeKey(String pageSizeKey) {
		this.pageSizeKey = pageSizeKey;
	}

	public String getTotalKey() {
		return totalKey;
	}

	public void setTotalKey(String totalKey) {
		this.totalKey = totalKey;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("name", this.name).append("alias", this.alias).append("style", this.style).append("objName", this.objName).append("needPage", this.needPage).append("pageSize", this.pageSize).append("displayfield", this.displayfield).append("conditionfield", this.conditionfield).append("resultfield", this.resultfield).append("sortfield", this.sortfield).append("dsalias", this.dsalias).append("isTable", this.isTable).append("diySql", this.diySql).append("sqlBuildType", this.sqlBuildType).append("width", this.width).append("height", this.height).append("selectNum", this.selectNum).append("dsType", this.dsType).append("url", this.url).append("requestType", this.requestType).append("dataParam", this.dataParam).append("pageKey", this.pageKey).append("pageSizeKey", this.pageSizeKey).append("totalKey", this.totalKey).append("listKey", this.listKey).toString();
	}
}