package com.hotent.bpm.api.service;

import java.util.Map;

/**
 * 流程网关条件操作接口。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-5-22-上午9:23:01
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmDefConditionService {
	
	/**
	 * 设置网关节点条件。
	 * <pre>
	 * 	根据流程定义和网关节点设置网关条件。
	 *  需要修改定义xml和设计的xml。
	 *  不同的设计器需要分开处理。
	 * </pre>
	 * @param defId		流程定义ID
	 * @param nodeId	节点ID
	 * @param map 		条件map，键后续节点id，值 条件。
	 * void
	 * @throws Exception 
	 */
	void saveCondition(String defId,String nodeId,Map<String,String> map) throws Exception;
	
	
	/**
	 * 获取分支网关节点条件。
	 * @param defId		流程定义ID
	 * @param nodeId	节点ID
	 * @return 
	 * Map&lt;String,String>
	 */
	Map<String,String> getDecisionConditions(String defId,String nodeId);
	
	
	
}
