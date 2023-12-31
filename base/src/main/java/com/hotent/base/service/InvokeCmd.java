package com.hotent.base.service;

/**
 * 服务调用参数
 * @author heyifan
 * @version 创建时间: 2014-8-18
 */
public interface InvokeCmd {
	/**
	 * 获取服务调用地址
	 * @return
	 */
	String getAddress();
	/**
	 * 设置服务调用地址
	 * @param address
	 */
	void setAddress(String address);
	/**
	 * 获取服务用户名
	 * @return
	 */
	String getUsername();
	/**
	 * 设置服务用户名
	 * @param username
	 */
	void setUsername(String username);
	/**
	 * 获取服务密码
	 * @return
	 */
	String getPassword();
	/**
	 * 设置服务密码
	 * @param password
	 */
	void setPassword(String password);
	/**
	 * 获取要调用的方法名
	 * @return
	 */
	String getOperatorName();
	/**
	 * 设置要调用的方法名
	 * @param operatorName
	 */
	void setOperatorName(String operatorName);
	/**
	 * 获取要调用方法的名称空间
	 * @return
	 */
	String getOperatorNamespace();
	/**
	 * 设置要调用的方法的名称空间
	 * @param operatorNamespace
	 */
	void setOperatorNamespace(String operatorNamespace);
	/**
	 * 获取json参数
	 * @return
	 */
	String getJsonParam();
	/**
	 * 设置json参数
	 * @param jsonParam
	 */
	void setJsonParam(String jsonParam);
	/**
	 * 获取xml参数
	 * @return
	 */
	String getXmlParam();
	/**
	 * 设置xml参数
	 * @param xmlParam
	 */
	void setXmlParam(String xmlParam);
	/**
	 * 获取构建xml时是否添加xmlns
	 * @return
	 */
	Boolean getNeedPrefix();
	/**
	 * 设置构建xml时是否添加xmlns
	 * @param soapAction
	 */
	void setNeedPrefix(Boolean needPrefix);
	/**
	 * 获取服务类型
	 * @return
	 */
	String getType();
	/**
	 * 设置服务类型
	 * @param type
	 */
	void setType(String type);
}
