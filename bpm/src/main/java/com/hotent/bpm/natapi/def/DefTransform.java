package com.hotent.bpm.natapi.def;

import java.util.Map;


/**
 * 流程定义转换接口。这个结构负责将不同类型的设计xml文件，
 * 转换成标砖的BPMN流程定义文件。
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-2-8-上午11:42:03
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface DefTransform {
	
	public static final String bpmnNamespace="http://www.omg.org/spec/BPMN/20100524/MODEL";
	
	public static final String xsiNamespace="http://www.w3.org/2001/XMLSchema-instance";
	
	//
	
	/**
	 * 将设计器产生的文件转换成标准的BPMN XML。
	 * @param id			流程key
	 * @param name			流程名称
	 * @param designFile	设计的文件文本
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	String convert(String id,String name, String designFile) throws Exception;
	
	/**
	 * 设置设计xml的条件。
	 * @param nodeId	节点ID
	 * @param map		节点，条件映射
	 * @param xml		设计的xml
	 * @return 
	 * String
	 */
	String converConditionXml(String nodeId,Map<String,String> map,String xml);
}
