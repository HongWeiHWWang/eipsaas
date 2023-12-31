package com.hotent.bpm.api.model.process.def;

import java.io.Serializable;

/**
 * 
 * 描述：流程变量定义模型
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-14-下午4:13:47
 * 版权：广州宏天软件有限公司版权所有
 */
public interface BpmVariableDef extends Serializable{
	
	public static final String VAR_TYPE_STRING="string";		/* 用流程变量实例的变量值字段存储 */
	public static final String VAR_TYPE_INT="int";						/* 用流程变量实例的变量值字段存储 */
	public static final String VAR_TYPE_FLOAT="float";			/* 用流程变量实例的变量值字段存储 */
	public static final String VAR_TYPE_DOUBLE="double";		/* 用流程变量实例的变量值字段存储 */
	public static final String VAR_TYPE_LONG="long";		/* 用流程变量实例的变量值字段存储 */
	public static final String VAR_TYPE_DATE="date";				/* 用流程变量实例的变量值字段存储 */
	public static final String VAR_TYPE_JSON="json";  				/* 用流程变量大数据值的大文本字段存储 */
	public static final String VAR_TYPE_XML="xml";					/* 用流程变量大数据值的大文本字段存储 */
	public static final String VAR_TYPE_BYTES="bytes";			/* 用流程变量大数据值的二进制字段存储 */

	/**
	 * 变量标识名称 
	 * @return  String
	 */
	String getName();
	
	/**
	 * 设置变量名称 
	 * @param name 
	 * void
	 */
	void setName(String name);
	
	/**
	 * 取得节点ID。
	 * @return  String
	 */
	String getNodeId();
	
	/**
	 * 设置节点ID
	 * @param nodeId 
	 * void
	 */
	void setNodeId(String nodeId);
	
	
	/**
	 * 变量Key
	 * @return  String
	 */
	String getVarKey();
	
	/**
	 * 设置变量key
	 * @param key 
	 * void
	 */
	void setVarKey(String key);

	/**
	 * 变量类型
	 * string,int,float,double,date,json,xml,db
	 * @return  String
	 */
	String getDataType();
	
	/**
	 * 设置数据类型。
	 * @param dataType 
	 * void
	 */
	void setDataType(String dataType);
	
	/**
	 * 获取缺省值
	 * @return  Object
	 */
	Object getDefaultVal();
	
	/**
	 * 设置默认值。
	 * @param defaultVal 
	 * void
	 */
	void setDefaultVal(Object defaultVal);
	
	/**
	 * 是否必需
	 * @return  boolean
	 */
	boolean isRequired();
	
	/**
	 * 设置是否必填。
	 * @param isRequired 
	 * void
	 */
	void setRequired(boolean isRequired);
	
	/**
	 * 是否必需
	 * @return  boolean
	 */
	boolean getIsRequired();
	
	/**
	 * 设置是否必填。
	 * @param isRequired 
	 * void
	 */
	void setIsRequired(boolean isRequired);
	
	/**
	 * 描述
	 * @return  String
	 */
	String getDescription();
	
	/**
	 * 设置描述。
	 * @param description 
	 * void
	 */
	void setDescription(String description);
	
	
}
