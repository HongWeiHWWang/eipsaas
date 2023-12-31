package com.hotent.bpm.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


 /**
 * 
 * <pre> 
 * 描述：bpm_inst_form 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2017-07-04 15:19:05
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("bpm_inst_form")
public class BpmInstForm extends BaseModel<BpmInstForm>{
	private static final long serialVersionUID = 1L;

	/**
	* id_
	*/
	@TableId("id_")
	protected String id; 
	
	/**
	* 流程定义id
	*/
	@TableField("def_id_")
	protected String defId; 
	
	/**
	* 实例id
	*/
	@TableField("inst_id_")
	protected String instId; 
	
	/**
	* 表单 inner 记录id  frame 记录formValue
	*/
	@TableField("form_value_")
	protected String formValue; 
	
	/**
	* 节点id  instId == nodeId 一样时， 存储的是流程的实例表单
	*/
	@TableField("node_id_")
	protected String nodeId; 
	
	/**
	* 表单类型 pc mobile
	*/
	@TableField("form_type_")
	protected String formType; 
	
	/**
	* url表单 frame  在线表单 inner
	*/
	@TableField("form_category_")
	protected String formCategory; 
	
	@TableField(exist=false)
	protected String formExtraConf;
	
	@TableField(exist=false)
	protected String helpFile;
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 id_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setDefId(String defId) {
		this.defId = defId;
	}
	
	/**
	 * 返回 流程定义id
	 * @return
	 */
	public String getDefId() {
		return this.defId;
	}
	
	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	/**
	 * 返回 实例id
	 * @return
	 */
	public String getInstId() {
		return this.instId;
	}
	
	public void setFormValue(String formValue) {
		this.formValue = formValue;
	}
	
	/**
	 * 返回 表单 inner 记录id  frame 记录formValue
	 * @return
	 */
	public String getFormValue() {
		return this.formValue;
	}
	
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	/**
	 * 返回 节点id
	 * @return
	 */
	public String getNodeId() {
		return this.nodeId;
	}
	
	public void setFormType(String formType) {
		this.formType = formType;
	}
	
	/**
	 * 返回 表单类型 pc mobile
	 * @return
	 */
	public String getFormType() {
		return this.formType;
	}
	
	public void setFormCategory(String formCategory) {
		this.formCategory = formCategory;
	}
	
	/**
	 * 返回 url表单 frame  在线表单 inner
	 * @return
	 */
	public String getFormCategory() {
		return this.formCategory;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("defId", this.defId) 
		.append("instId", this.instId) 
		.append("formValue", this.formValue) 
		.append("nodeId", this.nodeId) 
		.append("formType", this.formType) 
		.append("formCategory", this.formCategory) 
		.toString();
	}

	public String getFormExtraConf() {
		return formExtraConf;
	}

	public void setFormExtraConf(String formExtraConf) {
		this.formExtraConf = formExtraConf;
	}

	public String getHelpFile() {
		return helpFile;
	}

	public void setHelpFile(String helpFile) {
		this.helpFile = helpFile;
	}
	
}