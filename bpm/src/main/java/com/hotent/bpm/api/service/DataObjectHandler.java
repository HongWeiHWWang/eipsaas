package com.hotent.bpm.api.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;

/**
 * bo对象处理。
 * 用于流程修改bo实例的数据。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-8-12-下午1:55:13
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */	
public interface DataObjectHandler {
	
	/**
	 * 流程启动时显示表单数据进行修改。
	 * <pre>
	 * 执行bo的前置脚本
	 * </pre>
	 * @param defId
	 * @param dataObject 
	 * void
	 * @throws IOException 
	 * @throws Exception 
	 */
	void handShowData(String defId, List<ObjectNode> boDatas) throws  Exception;
	
	/**
	 * 在任务审批时修改表单显示数据。
	 * <pre>
	 * 执行bo的前置脚本
	 * </pre>
	 * @param instance
	 * @param nodeId
	 * @param dataObject 
	 * void
	 * @throws Exception 
	 * @throws ParseException 
	 */
	void handShowData(BpmProcessInstance instance,String nodeId,List<ObjectNode> boDatas) throws Exception ;
	
	/**
	 * 用于流程启动时修改数据。
	 * <pre>
	 * 执行bo的后置脚本
	 * </pre>
	 * @param instance
	 * @param dataObject 
	 * void
	 * @throws IOException 
	 * @throws Exception 
	 */
	void handSaveData(BpmProcessInstance instance, List<ObjectNode> boDatas) throws  Exception;
	
	/**
	 * 用于流程审批时修改数据。
	 * <pre>
	 * 执行bo的后置脚本
	 * </pre> 
	 * @param instance
	 * @param nodeId
	 * @param dataObject 
	 * void
	 * @throws Exception 
	 */
	void handSaveData(BpmProcessInstance instance,String nodeId,List<ObjectNode> boDatas) throws Exception;
	
	
}
