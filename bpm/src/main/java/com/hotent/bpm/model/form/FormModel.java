package com.hotent.bpm.model.form;

/**
 * 在线表单的接口
 * @author heyifan
 * @version 创建时间: 2014-11-27
 */
public interface FormModel extends Form{
	/**
	 * 获取表单ID
	 * @return
	 */
	String getFormId();
	/**
	 * 设置表单ID
	 * @param formId
	 */
	void setFormId(String formId);
	/**
	 * 获取表单KEY
	 * @return
	 */
	String getFormKey();
	/**
	 * 设置表单KEY
	 * @param formKey
	 */
	void setFormKey(String formKey);
	/**
	 * 获取表单的HTML
	 * @return
	 */
	String getFormHtml();
	/**
	 * 设置表单的HTML
	 * @param formHtml
	 */
	void setFormHtml(String formHtml);
}
