package com.hotent.sys.persistence.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 对象功能: 对象功能:portal_sys_datasorce_def
 * 开发公司:广州宏天软件有限公司
 * 开发人员:liyg
 * 创建时间:2018-07-04 09:04:28
 */
@TableName("portal_sys_datasource_def")
@ApiModel(description="数据源定义")
public class SysDataSourceDef extends BaseModel<SysDataSourceDef> {
	
	private static final long serialVersionUID = 1L;

	@TableId("id_")
	@ApiModelProperty(name="id", notes="主键")
	protected String id;
	
	@TableField("name_")	
	@ApiModelProperty("名称")
	protected String name; /* 数据源名称 */
	
	@TableField("class_path_")	
	@ApiModelProperty("名称")
	protected String classPath; /* 数据源全路径 */
	
	@TableField("setting_json_")	
	@ApiModelProperty("名称")
	protected String settingJson; /* 属性配置 */
	
	@TableField("init_method_")	
	@ApiModelProperty("名称")
	protected String initMethod; /* 初始化方法，有些可以不填写 */
	
	@TableField("close_method_")	
	@ApiModelProperty("名称")
	protected String closeMethod; /* 关闭数据源的时候应该调用的方法，可不填 */
	
	@TableField("is_system_")	
	@ApiModelProperty("名称")
	protected Boolean isSystem; /* 是系统默认的 */

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
	 * 返回 数据源名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	/**
	 * 返回 数据源全路径
	 * 
	 * @return
	 */
	public String getClassPath() {
		return this.classPath;
	}

	public void setSettingJson(String settingJson) {
		this.settingJson = settingJson;
	}

	/**
	 * 返回 属性配置
	 * 
	 * @return
	 */
	public String getSettingJson() {
		return this.settingJson;
	}

	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}

	/**
	 * 返回 初始化方法，有些可以不填写
	 * 
	 * @return
	 */
	public String getInitMethod() {
		return this.initMethod;
	}

	
	/**
	 * isSystem
	 * @return  the isSystem
	 * @since   1.0.0
	 */
	
	public Boolean getIsSystem() {
		return isSystem;
	}

	/**
	 * @param isSystem the isSystem to set
	 */
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
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
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("name", this.name).append("classPath", this.classPath).append("settingJson", this.settingJson).append("initMethod", this.initMethod).append("closeMethod", this.closeMethod).append("inSystem", this.isSystem).toString();
	}
}