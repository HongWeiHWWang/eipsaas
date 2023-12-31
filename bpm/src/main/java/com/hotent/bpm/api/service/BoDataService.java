package com.hotent.bpm.api.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;

/**
 * bo 获取数据服务接口。
 * @author ray
 *
 */
public interface BoDataService {
	
	/**
	 * 根据bokey获取数据。
	 * @param boKeyList
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	List<ObjectNode> getDataByBoKeys(List<String> boKeyList) throws ClientProtocolException, IOException;
	
	/**
	 * 通过流程定义获取初始化的bo数据。
	 * <pre>
	 * 1.根据流程定义ID获取流程定义定义的BO列表。
	 * 2.根据bocode 获取 bodata数据。
	 * </pre>
	 * @param defId	流程定义ID
	 * @return
	 * @throws Exception 
	 */
	List<ObjectNode> getDataByDefId(String defId) throws Exception;
	
	/**
	 * 根据流程实例获取bo数据。
	 * <pre>
	 *  1.获取最外层的流程实例。
	 * 	2.获取流程定义的bo 列表。
	 *  3.获取bo数据的存储模式。
	 *  4.根据流程实例ID获取业务数据关联关系。
	 *  5.根据业务数据关联关系的业务主键获取bo的数据。
	 * </pre>
 	 * @return                                    
	 * List&lt;DataObject>
	 * @throws Exception 
	 */                                                                                                                      
	List<ObjectNode> getDataByInst(BpmProcessInstance instance) throws Exception;
	
	//获取表单中的意见数据
	ObjectNode getFormOpinionJson(String proInstId) throws IOException, Exception;

	List<ObjectNode> getDataByBizKey(String businessKey) throws Exception;
}
