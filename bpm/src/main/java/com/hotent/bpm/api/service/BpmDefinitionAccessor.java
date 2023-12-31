package com.hotent.bpm.api.service;

import java.util.List;

import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;




/**
 * 
 * <pre> 
 * 描述：流程定义描述访问器
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-11-下午8:43:14
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmDefinitionAccessor {
	/**
	 * 通过流程定义ID返回所有的节点定义
	 * @param processDefinitionId
	 * @return 
	 * List&lt;BpmNodeDef>
	 * @throws Exception 
	 */
	 List<BpmNodeDef> getNodeDefs(String processDefinitionId) throws Exception;
	 
	 
	 /**
	  * 根据流程节点类型获取节点定义列表。
	  * @param processDefinitionId
	  * @param nodeType
	  * @return 
	  * List&lt;BpmNodeDef>
	 * @throws Exception 
	  */
	 List<BpmNodeDef> getNodesByType(String processDefinitionId,NodeType nodeType) throws Exception;
	 
	 
	 /**
	  * 根据流程节点类型获取节点定义列表，包括内部子流程节点列表。
	  * @param processDefinitionId
	  * @param nodeType
	  * @return 
	  * List&lt;BpmNodeDef>
	 * @throws Exception 
	  */
	 List<BpmNodeDef> getAllNodeDef(String processDefinitionId) throws Exception;
	 
	/**
	 * 通过流程定义及节点定义ID获取流程节点定义
	 * @param processDefinitionId
	 * @param nodeId
	 * @return  BpmNodeDef
	 * @throws Exception 
	 */
	 BpmNodeDef getBpmNodeDef(String processDefinitionId,String nodeId) throws Exception;
	/**
	 * 通过流程定义ID获取流程定义的实体描述
	 * @param processDefId
	 * @return  BpmProcessDef
	 * @throws Exception 
	 */
	 BpmProcessDef<BpmProcessDefExt> getBpmProcessDef(String processDefId) throws Exception;
	
	/**
	 * 
	 * 根据流程定义ID删除缓存。
	 * @param processDefId 
	 * void
	 */
	 void clean(String processDefId);
	 
	 /**
	  * 根据节点获取开始节点。
	  * @param defId
	  * @return 
	  * BpmNodeDef
	 * @throws Exception 
	  */
	 BpmNodeDef getStartEvent(String processDefId) throws Exception;
	 
	 /**
	  * 获取流程的结束节点。
	  * @param processDefId
	  * @return 
	  * List&lt;BpmNodeDef>
	 * @throws Exception 
	  */
	 List<BpmNodeDef> getEndEvents(String processDefId) throws Exception;
	 
	 /**
	  * 获取第一个节点。 
	  * 第一个节点的定义是指，开始事件后的第一个节点。
	  * @param processDefId
	  * @return 
	  * List&lt;BpmNodeDef>
	 * @throws Exception 
	  */
	 List<BpmNodeDef> getStartNodes(String processDefId) throws Exception;
	 
	 
	 
	 
	 /**
	  * 
	  * 判定某个节点是否为第一个节点。
	  * @param defId
	  * @param nodeId
	  * @return  boolean
	 * @throws Exception 
	  */
	 boolean isStartNode(String defId,String nodeId) throws Exception;
	 
	 /**
	  * 判定某个节点是否某种类型。
	  * 
	  * 判断节点是否为指定的节点类型。
	  * 
	  * @param defId
	  * @param nodeId
	  * @param nodeDefType
	  * @return 
	  * boolean
	 * @throws Exception 
	  */
	 boolean validNodeDefType(String defId,String nodeId,NodeType nodeDefType) throws Exception;
	 
	 /**
	  * 是否包含子流程数据。
	  * @param defId
	  * @return 
	  * boolean
	 * @throws Exception 
	  */
	 boolean isContainCallActivity(String defId) throws Exception;


	/**
	 * 仅仅获取开始、普通、会签的节点定义
	 * @param defId
	 * @return
	 * @throws Exception 
	 */
	List<BpmNodeDef> getSignUserNode(String defId) throws Exception;
	 
	
}
