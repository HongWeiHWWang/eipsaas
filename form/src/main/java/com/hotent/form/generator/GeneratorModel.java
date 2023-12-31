package com.hotent.form.generator;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用于代码生成的模型
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月30日
 */
@ApiModel("代码生成的模型")
public class GeneratorModel implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 代码生成类型：表单
	 */
	public static final String TYPE_FORM = "form";
	/**
	 * 代码生成类型：物理表
	 */
	public static final String TYPE_TABLE = "table";
	@ApiModelProperty(value="代码生成类型", example = "form", allowableValues="form,table")
	private String type = GeneratorModel.TYPE_FORM;
	@ApiModelProperty(value="代码所在微服务简称", example = "form")
	private String system;
	@ApiModelProperty("代码生成物理表所在数据源别名（类型为表单时可以为空）")
	private String dataSourceAlias;
	@ApiModelProperty("表单别名")
	private String formkey;
	@ApiModelProperty("物理表名")
	private String[] tableName;
	@ApiModelProperty("生成的代码所在名称空间")
	private String basePackage;
	@ApiModelProperty("代码生成的作者")
	private String authorName;
	@ApiModelProperty("模块名称")
	private String moduleName;
	@ApiModelProperty("代码生成的作者邮箱")
	private String authorEmail;
	@ApiModelProperty("公司名称")
	private String companyName;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getDataSourceAlias() {
		return dataSourceAlias;
	}
	public void setDataSourceAlias(String dataSourceAlias) {
		this.dataSourceAlias = dataSourceAlias;
	}
	public String getFormkey() {
		return formkey;
	}
	public void setFormkey(String formkey) {
		this.formkey = formkey;
	}
	public String[] getTableName() {
		return tableName;
	}
	public void setTableName(String[] tableName) {
		this.tableName = tableName;
	}
	public String getBasePackage() {
		return basePackage;
	}
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	public static String getTypeForm() {
		return TYPE_FORM;
	}
	public static String getTypeTable() {
		return TYPE_TABLE;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
