package com.hotent.runtime.model;

import io.swagger.annotations.ApiModel;

/**
 * 流程变量实例
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */

@ApiModel(value="流程变量实例")
public interface BpmVariableInstance {
	/**
	 * 变量实例ID
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getId();
	/**
	 * 流程节点Id
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getNodeId();	
	/**
	 * 流程实例ID
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getProcInstId();	
	/**
	 * 流程定义Id
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getProcDefId();
	/**
	 * 流程变量Key
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getVarKey();
	/**
	 * 流程变量值
	 * @return 
	 * Object
	 * @exception 
	 * @since  1.0.0
	 */
	public Object getValue();
}
