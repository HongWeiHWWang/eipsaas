package com.hotent.form.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程表单HTML设计历史记录 实体对象
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月6日
 */
@ApiModel("表单历史记录")
@TableName("form_definition_hi")
public class FormHistory extends AutoFillModel<FormHistory> {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id;
	
	@ApiModelProperty(name="formId", notes="对应表单ID")
	@TableField("form_id_")
	protected String formId;
	
	@ApiModelProperty(name="name", notes="表单名称")
	@TableField("name_")
	protected String name;
	
	@ApiModelProperty(name="desc", notes="表单描述")
	@TableField("desc_")
	protected String desc;
	
	@ApiModelProperty(name="formHtml", notes="表单设计（HTML代码）")
	@TableField("form_html_")
	protected String formHtml;

	public FormHistory(){
	}
	
	public FormHistory(Form bpmFom){
		this.formId = bpmFom.getId(); /*对应表单ID*/
		this.name = bpmFom.getName(); /*表单名称*/
		this.desc = bpmFom.getDesc(); /*表单描述*/
		this.formHtml = bpmFom.getFormHtml(); /*表单设计（HTML代码）*/
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the formId
	 */
	public String getFormId() {
		return formId;
	}
	/**
	 * @param formId the formId to set
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}
	/**
	 * @return the name
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
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the formHtml
	 */
	public String getFormHtml() {
		return formHtml;
	}
	/**
	 * @param formHtml the formHtml to set
	 */
	public void setFormHtml(String formHtml) {
		this.formHtml = formHtml;
	}
	
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", this.id)
				.append("formId", this.formId)
				.append("name", this.name)
				.append("desc", this.desc)
				.append("formHtml", this.formHtml)
				.toString();
	}
}
