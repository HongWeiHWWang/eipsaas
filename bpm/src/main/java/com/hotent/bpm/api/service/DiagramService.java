package com.hotent.bpm.api.service;

import java.io.InputStream;
import java.util.Map;

import com.hotent.bpm.api.model.process.def.BpmDefLayout;

/**
 * 流程图API.
 * <pre> 
 * 描述：流程图。
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-16-下午4:17:36
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface DiagramService {

	/**
	 * 根据流程定义ID获取流程图。
	 * @param bpmnDefId
	 * @return  InputStream
	 */
	InputStream getDiagramByBpmnDefId(String bpmnDefId);
	
	
	/**
	 * 根据流程定义ID产生流程图。
	 * @param bpmnDefId		流程定义ID
	 * @param colourMap		节点颜色map。
	 * @return  InputStream
	 */
	InputStream getDiagramByDefId(String bpmnDefId, Map<String,String> colourMap);
	
	
	/**
	 * 根据流程定义ID获取流程节点的布局情况。
	 * @param defId
	 * @return 
	 * BpmDefLayout
	 */
	public BpmDefLayout getLayoutByDefId(String defId);
	
}
