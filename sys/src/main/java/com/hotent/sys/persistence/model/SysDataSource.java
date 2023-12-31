package com.hotent.sys.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象功能:sys_data_source entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:jason
 * 创建时间:2014-06-24 11:41:52
 */
@TableName("portal_sys_datasource")
@ApiModel(description="数据源配置")
public class SysDataSource extends AutoFillModel<SysDataSource>{

	private static final long serialVersionUID = 1L;
	@TableId("id_")
	@ApiModelProperty(name="id", notes="主键")
	protected String id;
	
	@TableField("name_")	
	@ApiModelProperty("名称")
	protected String name; 
	
	@TableField("alias_")	
	@ApiModelProperty("别名")
	protected String alias;
	
	@TableField("class_path_")	
	@ApiModelProperty("驱动类路径")
	protected String classPath;/*类名*/
	
	@TableField("db_type_")	
	@ApiModelProperty("数据源类型")
	protected String dbType;
	
	@TableField("setting_json_")	
	@ApiModelProperty("Json存储配置")
	protected String settingJson;
	
	@TableField("init_on_start_")	
	@ApiModelProperty("在启动时，启动连接池，并添加到spring容器中管理。")
	protected Boolean initOnStart;
	
	@TableField("enabled_")	
	@ApiModelProperty("是否生效")
	protected Boolean enabled;
	
	@TableField("init_method_")	
	@ApiModelProperty("初始化方法，有些可以不填写")
	protected String initMethod;
	
	@TableField("close_method_")	
	@ApiModelProperty("关闭数据源的时候应该调用的方法，可不填 ")
	protected String closeMethod;

	/**
	 * id
	 * @return  the id
	 * @since   1.0.0
	 */
	
	public String getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}



	/**
	 * name
	 * @return  the name
	 * @since   1.0.0
	 */
	
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * alias
	 * @return  the alias
	 * @since   1.0.0
	 */
	
	public String getAlias() {
		return alias;
	}



	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}



	/**
	 * dbType
	 * @return  the dbType
	 * @since   1.0.0
	 */
	
	public String getDbType() {
		return dbType;
	}



	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}



	/**
	 * settingJson
	 * @return  the settingJson
	 * @since   1.0.0
	 */
	
	public String getSettingJson() {
		return settingJson;
	}



	/**
	 * @param settingJson the settingJson to set
	 */
	public void setSettingJson(String settingJson) {
		this.settingJson = settingJson;
	}



	/**
	 * initOnStart
	 * @return  the initOnStart
	 * @since   1.0.0
	 */
	
	public Boolean getInitOnStart() {
		return initOnStart;
	}



	/**
	 * @param initOnStart the initOnStart to set
	 */
	public void setInitOnStart(Boolean initOnStart) {
		this.initOnStart = initOnStart;
	}



	/**
	 * enabled
	 * @return  the enabled
	 * @since   1.0.0
	 */
	
	public Boolean getEnabled() {
		return enabled;
	}



	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}



	/**
	 * initMethod
	 * @return  the initMethod
	 * @since   1.0.0
	 */
	
	public String getInitMethod() {
		return initMethod;
	}



	/**
	 * @param initMethod the initMethod to set
	 */
	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}



	/**
	 * closeMethod
	 * @return  the closeMethod
	 * @since   1.0.0
	 */
	
	public String getCloseMethod() {
		return closeMethod;
	}



	/**
	 * @param closeMethod the closeMethod to set
	 */
	public void setCloseMethod(String closeMethod) {
		this.closeMethod = closeMethod;
	}


	/**
	 * classPath
	 * @return  the classPath
	 * @since   1.0.0
	 */
	
	public String getClassPath() {
		return classPath;
	}



	/**
	 * @param classPath the classPath to set
	 */
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}



	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("alias", this.alias) 
		.append("dsId", this.dbType) 
		.append("settingJson", this.settingJson) 
		.append("initOnStart", this.initOnStart) 
		.append("enabled", this.enabled)
		.append("initMethod", this.initMethod) 
		.append("closeMethod", this.closeMethod)
		.append("classPath", this.classPath) 
		.toString();
	}
}