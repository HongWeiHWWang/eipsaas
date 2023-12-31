package com.hotent.form.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

 /**
 * 字段授权信息
 * <pre> 
 * 描述：form_field_auth 实体对象
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-10-27 14:37:10
 * 版权：广州宏天软件有限公司
 * </pre>
 */
 @ApiModel("字段授权信息")
 @TableName("form_field_auth")
public class FieldAuth extends AutoFillModel<FieldAuth>{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("主键")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty("数据源别名")
	@TableField("ds_alias_")
	protected String dsAlias; 
	
	@ApiModelProperty("表名")
	@TableField("table_name_")
	protected String tableName; 
	
	@ApiModelProperty("描述")
	@TableField("desc_")
	protected String desc; 
	
	@ApiModelProperty("实体名")
	@TableField("ent_name_")
	protected String entName; 
	
	@ApiModelProperty("类名")
	@TableField("class_name_")
	protected String className; 
	
	@ApiModelProperty("类路径")
	@TableField("class_path_")
	protected String classPath; 
	
	@ApiModelProperty("字段列表（权限设置）")
	@TableField("field_list_")
	protected String fieldList; 
	
	
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
	
	public String getDsAlias() {
		return dsAlias;
	}

	public void setDsAlias(String dsAlias) {
		this.dsAlias = dsAlias;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * 返回 表名
	 * @return
	 */
	public String getTableName() {
		return this.tableName;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 返回 描述
	 * @return
	 */
	public String getDesc() {
		return this.desc;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * 返回 类名
	 * @return
	 */
	public String getClassName() {
		return this.className;
	}
	
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	
	/**
	 * 返回 类路径
	 * @return
	 */
	public String getClassPath() {
		return this.classPath;
	}
	
	public void setFieldList(String fieldList) {
		this.fieldList = fieldList;
	}
	
	/**
	 * 返回 字段列表（权限设置）
	 * @return
	 */
	public String getFieldList() {
		return this.fieldList;
	}
	
	
	public String getEntName() {
		return entName;
	}

	public void setEntName(String entName) {
		this.entName = entName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("tableName", this.tableName) 
		.append("desc", this.desc) 
		.append("entName", this.entName) 
		.append("className", this.className) 
		.append("classPath", this.classPath) 
		.append("fieldList", this.fieldList) 
		.toString();
	}
}