package com.hotent.bpm.api.service;

import java.util.Map;

import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.uc.api.model.IUser;

/**
 * 代理服务接口。
 * @author ray
 *
 */
public interface BpmAgentService {
	
	
	/**
	 * 获取代理人。
	 * @param userId		任务的执行人ID
	 * @param delegateTask		流程定义ID
	 * @param dataMode 
	 * @param vars			流程变量
	 * @return User			返回代理用户。
	 */
	IUser getAgent(String userId,BpmDelegateTask delegateTask,Map<String, Object> vars);
	
	
	
	/**
	 * 取回任务。
	 * <pre>
	 * 	1.BPM_TASK 执行人员修改，任务状态的修改。
	 * 	2.ACT_RU_TASK 执行人修改。
	 *  3.BPM_TASK_TURN状态的修改。
	 * </pre>
	 * @param bpmnTaskId 
	 * @param userId
	 * void
	 */
	void retrieveTask(String taskId,String informType,String cause);

}
