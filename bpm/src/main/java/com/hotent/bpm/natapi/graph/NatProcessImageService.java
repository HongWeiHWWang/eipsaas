package com.hotent.bpm.natapi.graph;

import java.io.InputStream;
import java.util.Map;

/**
 *  流程图服务接口
 * @author csx
 *
 */
public interface NatProcessImageService {
	/**
	 * 通过流程定义ID生成流程图
	 * @param bpmnDefId
	 * @return
	 */
	public InputStream getProcessImageByBpmnXml(String bpmnXml);
	/**
	 * 
	 * @param bpmnInstId
	 * @return
	 */
	public InputStream getProcessImageByBpmnXml(String bpmnXml,Map<String,String>colorMap);
	
	
}
