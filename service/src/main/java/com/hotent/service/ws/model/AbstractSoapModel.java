package com.hotent.service.ws.model;

/**
 * soap基类
 * <pre>
 * 所有soap对象都有name和namespace属性
 * </pre>
 * @author heyifan
 * @version 创建时间: 2014-8-18
 */
public abstract class AbstractSoapModel {
	private String name;

	private String namespace;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
