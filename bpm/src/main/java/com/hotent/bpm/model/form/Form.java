package com.hotent.bpm.model.form;

/**
 * 表单接口。
 * @author heyifan
 * @version 创建时间: 2014-11-27
 */
public interface Form {
	
	String getId();
	
	
	/**
	 * local
	 */
	public final static String LOCAL = "local_";
	
	/**
	 * 获取表单所绑定的节点
	 * @return
	 */
	String getNodeId();
	/**
	 * 设置表单所绑定的节点
	 * @param nodeId
	 */	
	void setNodeId(String nodeId);
	/**
	 * 获取父流程KEY
	 * @return
	 */
	String getParentFlowKey();
	/**
	 * 设置父流程KEY
	 * @param parentFlowKey
	 */
	void setParentFlowKey(String parentFlowKey);
	/**
	 * 获取表单名称
	 * @return
	 */
	String getName();
	/**
	 * 设置表单名称
	 * @param name
	 */
	void setName(String name);
	/**
	 * 获取表单分类
	 * @return
	 */
	FormCategory getType();
	/**
	 * 设置表单分类
	 * @param type
	 */
	void setType(FormCategory type);
	/**
	 * 获取表单值
	 * <pre>
	 * 不同类型的表单该字段的值不一样：
	 * 1、INNER（在线表单）：存放表单key
	 * 2、FRAME（以iframe方式嵌入的表单）：存放表单的url
	 * </pre>
	 * @return
	 */
	String getFormValue();
	/**
	 * 设置表单值
	 * @param formValue
	 */
	void setFormValue(String formValue);
	
	
	/**
	 * 设置表单类型 pc,mobile。
	 * @param formType
	 */
	void setFormType(String formType );
	
	/**
	 * 获取表单类型。
	 * @return
	 */
	String getFormType();
	
	/**
	 * 表单配置是否为空。
	 * @return
	 */
	boolean isFormEmpty();
	
	
	String getFormExtraConf();
	
	void setFormExtraConf(String formExtraConf);
	

	String getHelpFile();
	
	void setHelpFile(String file);
}
