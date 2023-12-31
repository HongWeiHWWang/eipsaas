package com.hotent.bpm.natapi.inst;

import java.util.Map;

/**
 * 历史变量服务。
 * <pre> 
 * 构建组：x5-bpmx-native-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-28-上午8:51:43
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface NatHistoryVariableService {
	
	/**
	 * 根据流程实例ID和名字获取变量。
	 * @param bpmnInstId
	 * @param name
	 * @return 
	 * Object
	 */
	Object getByBpmnInstIdVarName(String bpmnInstId,String name);
	
	
	/**
	 * 根据流程实例Id获取流程变量。
	 * @param bpmnInstId
	 * @return   Map&lt;String,Object>
	 */
	Map<String, Object> getByBpmnInstId(String bpmnInstId);
}
