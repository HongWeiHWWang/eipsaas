package com.hotent.form.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 自定义查询实体对象
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月13日
 */
@ApiModel(description="自定义查询实体对象")
@TableName("form_custom_query")
public class CustomQuery extends AutoFillModel<CustomQuery> {
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULTTYPE_INPUT_PARAM = "1";
	public static final String DEFAULTTYPE_FIX_VALUE = "2";
	public static final String DEFAULTTYPE_SCRIPT = "3";
	
	/** 页面设置 */
	public static final Short SQLBUILDTYPE_PAGESETTING = 0;
	/** DIY SQL */
	public static final Short SQLBUILDTYPE_DIY = 1;
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id; /* 主键 */
	
	@ApiModelProperty(name="name", notes="名字")
	@TableField("name_")
	protected String name; /* 名字 */
	
	@ApiModelProperty(name="alias", notes="别名")
	@TableField("alias_")
	protected String alias; /* 别名 */
	
	@ApiModelProperty(name="objName", notes="对象名称，如果是表就是表名，视图则视图名")
	@TableField("obj_name_")
	protected String objName; /* 对象名称，如果是表就是表名，视图则视图名 */
	
	@ApiModelProperty(name="needPage", notes="是否分页")
	@TableField("need_page_")
	protected Integer needPage=0; /* 是否分页 */
	
	@ApiModelProperty(name="pageSize", notes="分页大小")
	@TableField("page_size_")
	protected Integer pageSize; /* 分页大小 */
	
	@ApiModelProperty(name="conditionfield", notes="条件字段的json")
	@TableField("conditionfield_")
	protected String conditionfield; /* 条件字段的json */
	
	@ApiModelProperty(name="resultfield", notes="返回字段json")
	@TableField("resultfield_")
	protected String resultfield; /* 返回字段json */
	
	@ApiModelProperty(name="sortfield", notes="排序字段")
	@TableField("sortfield_")
	protected String sortfield; /* 排序字段 */
	
	@ApiModelProperty(name="dsalias", notes="数据源的别名")
	@TableField("dsalias_")
	protected String dsalias; /* 数据源的别名 */
	
	@ApiModelProperty(name="isTable", notes="是否数据库表(0:视图 1:数据库表)",allowableValues="0,1")
	@TableField("is_table_")
	protected Short isTable; /* 是否数据库表0:视图,1:数据库表 */
	
	@ApiModelProperty("sql语句生成方式：0-页面配置；1-diy自己写")
	@TableField("sql_build_type_")
	protected Short sqlBuildType;/* sql语句生成方式：0-页面配置；1-diy自己写 */
	
	@ApiModelProperty("sql语句生成方式：0-页面配置；1-diy自己写")
	@TableField("diy_sql_")
	protected String diySql;/* 自定义sql */
	
	@ApiModelProperty(name="dsType", notes="数据源的类型(dataSource：数据源，restful：restful接口)")
	@TableField("ds_type_")
	protected String dsType; /* 数据源的别名 */
	
	@ApiModelProperty(name="url", notes="接口地址")
	@TableField("url_")
	protected String url; /*接口地址*/
	
	@ApiModelProperty(name="header", notes="接口请求的头部")
	@TableField("header_")
	protected String header;
	
	@ApiModelProperty(name="requestType", notes="请求地址类型：POST、GET（当数据加载方式为RESTFUL时）")
	@TableField("request_type_")
	protected String requestType;
	
	@ApiModelProperty(name="dataParam", notes="数据参数模板")
	@TableField("data_param_")
	protected String  dataParam;
	
	@ApiModelProperty(name="pageKey", notes="页号（key）")
	@TableField("page_key_")
	protected String  pageKey;
	
	@ApiModelProperty(name="pageSizeKey", notes="分页大小(key)")
	@TableField("pagesize_key_")
	protected String  pageSizeKey;
	
	@ApiModelProperty(name="totalKey", notes="总条数（key）")
	@TableField("total_key_")
	protected String  totalKey;
	
	@ApiModelProperty(name="listKey", notes="列表(key)")
	@TableField("list_key_")
	protected String  listKey;

	/**
	 * id
	 * 
	 * @return the id
	 * @since 1.0.0
	 */

	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * name
	 * 
	 * @return the name
	 * @since 1.0.0
	 */

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * alias
	 * 
	 * @return the alias
	 * @since 1.0.0
	 */

	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * objName
	 * 
	 * @return the objName
	 * @since 1.0.0
	 */

	public String getObjName() {
		return objName;
	}

	/**
	 * @param objName
	 *            the objName to set
	 */
	public void setObjName(String objName) {
		this.objName = objName;
	}

	/**
	 * needPage
	 * 
	 * @return the needPage
	 * @since 1.0.0
	 */

	public Integer getNeedPage() {
		return needPage;
	}

	/**
	 * @param needPage
	 *            the needPage to set
	 */
	public void setNeedPage(Integer needPage) {
		this.needPage = needPage;
	}

	/**
	 * pageSize
	 * 
	 * @return the pageSize
	 * @since 1.0.0
	 */

	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * conditionfield
	 * 
	 * @return the conditionfield
	 * @since 1.0.0
	 */

	public String getConditionfield() {
		return conditionfield;
	}

	/**
	 * @param conditionfield
	 *            the conditionfield to set
	 */
	public void setConditionfield(String conditionfield) {
		this.conditionfield = conditionfield;
	}

	/**
	 * resultfield
	 * 
	 * @return the resultfield
	 * @since 1.0.0
	 */

	public String getResultfield() {
		return resultfield;
	}

	/**
	 * @param resultfield
	 *            the resultfield to set
	 */
	public void setResultfield(String resultfield) {
		this.resultfield = resultfield;
	}

	/**
	 * sortfield
	 * 
	 * @return the sortfield
	 * @since 1.0.0
	 */

	public String getSortfield() {
		return sortfield;
	}

	/**
	 * @param sortfield
	 *            the sortfield to set
	 */
	public void setSortfield(String sortfield) {
		this.sortfield = sortfield;
	}

	/**
	 * dsalias
	 * 
	 * @return the dsalias
	 * @since 1.0.0
	 */

	public String getDsalias() {
		return dsalias;
	}

	/**
	 * @param dsalias
	 *            the dsalias to set
	 */
	public void setDsalias(String dsalias) {
		this.dsalias = dsalias;
	}

	/**
	 * isTable
	 * 
	 * @return the isTable
	 * @since 1.0.0
	 */

	public Short getIsTable() {
		return isTable;
	}

	/**
	 * @param isTable
	 *            the isTable to set
	 */
	public void setIsTable(Short isTable) {
		this.isTable = isTable;
	}

	/**
	 * sqlBuildType
	 * 
	 * @return the sqlBuildType
	 * @since 1.0.0
	 */

	public Short getSqlBuildType() {
		return sqlBuildType;
	}

	/**
	 * @param sqlBuildType
	 *            the sqlBuildType to set
	 */
	public void setSqlBuildType(Short sqlBuildType) {
		this.sqlBuildType = sqlBuildType;
	}

	/**
	 * diySql
	 * 
	 * @return the diySql
	 * @since 1.0.0
	 */

	public String getDiySql() {
		return diySql;
	}

	/**
	 * @param diySql
	 *            the diySql to set
	 */
	public void setDiySql(String diySql) {
		this.diySql = diySql;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BpmCustomQuery [id=" + id + ", name=" + name + ", alias=" + alias + ", objName=" + objName + ", needPage=" + needPage + ", pageSize=" + pageSize + ", conditionfield=" + conditionfield + ", resultfield=" + resultfield + ", sortfield=" + sortfield + ", dsalias=" + dsalias + ", isTable=" + isTable + ", sqlBuildType=" + sqlBuildType + ", diySql=" + diySql + ", dsType=" + dsType + ", url=" + url + ", requestType=" + requestType + ", dataParam=" + dataParam + ", pageKey=" + pageKey + ", pageSizeKey=" + pageSizeKey + ", totalKey=" + totalKey + ", listKey=" + listKey + "]";
	}

}