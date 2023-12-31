package com.hotent.activemq.model;

import java.io.Serializable;

/**
 * JMS消息
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
public class JmsSysTypeChangeMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3363346153708470650L;
	/**
	 *  
	 */
	protected String typeGroupKey;
	protected String typeId;
	protected String typeName;
	protected String oldTypeName;
	protected String entityIds;
	/**
	 * 操作类型。1:修改，2：删除,3:更新分类
	 */
	protected Integer opType;
	
	public String getTypeGroupKey() {
		return typeGroupKey;
	}
	public void setTypeGroupKey(String typeGroupKey) {
		this.typeGroupKey = typeGroupKey;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public JmsSysTypeChangeMessage(String typeGroupKey, String typeId, String typeName,String oldTypeName,Integer opType) {
		super();
		this.typeGroupKey = typeGroupKey;
		this.typeId = typeId;
		this.typeName = typeName;
		this.oldTypeName = oldTypeName;
		this.opType = opType;
	}
	public Integer getOpType() {
		return opType;
	}
	public void setOpType(Integer opType) {
		this.opType = opType;
	}
	public String getOldTypeName() {
		return oldTypeName;
	}
	public void setOldTypeName(String oldTypeName) {
		this.oldTypeName = oldTypeName;
	}
	public String getEntityIds() {
		return entityIds;
	}
	public void setEntityIds(String entityIds) {
		this.entityIds = entityIds;
	}
	public JmsSysTypeChangeMessage(String typeGroupKey, String typeId, String typeName,
			Integer opType, String entityIds) {
		super();
		this.typeGroupKey = typeGroupKey;
		this.typeId = typeId;
		this.typeName = typeName;
		this.entityIds = entityIds;
		this.opType = opType;
	}
	
}
