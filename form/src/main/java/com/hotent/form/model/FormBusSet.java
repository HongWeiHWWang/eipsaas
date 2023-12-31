package com.hotent.form.model;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 表单业务设置实体对象
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月13日
 */
@ApiModel("表单业务设置实体对象")
@TableName("form_bus_set")
public class FormBusSet extends BaseModel<FormBusSet>{
	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
	@ApiModelProperty(name="id",notes="主键")
	@TableId("id_")
	protected String id; 
	
	
	@ApiModelProperty(name="formKey",notes="key")
	@TableField("form_key_")
	protected String formKey; 
	
	/**
	* JavaScript前置脚本
	*/
	@ApiModelProperty(name="jsPreScript",notes="JavaScript前置脚本")
	@TableField("js_pre_script")
	protected String jsPreScript; 
	
	/**
	* JavaScript后置脚本
	*/
	@ApiModelProperty(name="jsAfterScript",notes="JavaScript后置脚本")
	@TableField("JS_AFTER_SCRIPT")
	protected String jsAfterScript; 
	
	/**
	* 保存前置脚本
	*/
	@ApiModelProperty(name="preScript",notes="保存前置脚本")
	@TableField("pre_script")
	protected String preScript; 
	
	/**
	* 保存后置脚本
	*/
	
	@ApiModelProperty(name="afterScript",notes="保存后置脚本")
	@TableField("after_script")
	protected String afterScript; 
	
	/**
	* 是否树形列表
	*/
	@ApiModelProperty(name="isTreeList",notes="是否树形列表")
	@TableField("ISTREELIST")
	protected Short isTreeList; 
	
	/**
	* {idKey:"idKEY名称",pIdKey:"",name:"显示名称",title,rootPid}
	*/
	@ApiModelProperty(name="treeConf",notes="{idKey:\"idKEY名称\",pIdKey:\"\",name:\"显示名称\",title,rootPid}")
	@TableField("treeconf")
	protected String treeConf; 
	
	
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
	
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	
	/**
	 * 返回 formKey
	 * @return
	 */
	public String getFormKey() {
		return this.formKey;
	}
	
	public void setJsPreScript(String jsPreScript) {
		this.jsPreScript = jsPreScript;
	}
	
	/**
	 * 返回 JavaScript前置脚本
	 * @return
	 */
	public String getJsPreScript() {
		return this.jsPreScript;
	}
	
	public void setJsAfterScript(String jsAfterScript) {
		this.jsAfterScript = jsAfterScript;
	}
	
	/**
	 * 返回 JavaScript后置脚本
	 * @return
	 */
	public String getJsAfterScript() {
		return this.jsAfterScript;
	}
	
	public void setPreScript(String preScript) {
		this.preScript = preScript;
	}
	
	/**
	 * 返回 保存前置脚本
	 * @return
	 */
	public String getPreScript() {
		return this.preScript;
	}
	
	public void setAfterScript(String afterScript) {
		this.afterScript = afterScript;
	}
	
	/**
	 * 返回 保存后置脚本
	 * @return
	 */
	public String getAfterScript() {
		return this.afterScript;
	}
	
	public void setIsTreeList(Short isTreeList) {
		this.isTreeList = isTreeList;
	}
	
	/**
	 * 返回 是否树形列表
	 * @return
	 */
	public Short getIsTreeList() {
		return this.isTreeList;
	}
	
	public void setTreeConf(String treeConf) {
		this.treeConf = treeConf;
	}
	
	/**
	 * 返回 {idKey:"idKEY名称",pIdKey:"",name:"显示名称",title,rootPid}
	 * @return
	 */
	public String getTreeConf() {
		return this.treeConf;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("formKey", this.formKey) 
		.append("jsPreScript", this.jsPreScript) 
		.append("jsAfterScript", this.jsAfterScript) 
		.append("preScript", this.preScript) 
		.append("afterScript", this.afterScript) 
		.append("isTreeList", this.isTreeList) 
		.append("treeConf", this.treeConf) 
		.toString();
	}
}