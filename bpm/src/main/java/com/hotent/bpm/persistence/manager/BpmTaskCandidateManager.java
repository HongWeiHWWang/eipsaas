package com.hotent.bpm.persistence.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskCandidate;


/**
 * 任务候选人数据库服务类。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-11-19-下午6:01:35
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmTaskCandidateManager  extends BaseManager<DefaultBpmTaskCandidate>{
	

	/**
	 * 添加任务候选人。
	 * <pre>
	 * 	1.如果候选人为user类型，那么直接添加候选人。
	 *  2.如果为用户组合，那么将用户组合分割开，循环添加候选人。
	 *  3.如果候选人为组织。
	 *  	1.用户抽取类型为不抽取。
	 *  		则将组织作为执行人插入候选人表。
	 *  	2.如果抽取类型为延迟抽取。
	 *  		那么将组织中的人员计算出来，添加到候选人表中。
	 * </pre>
	 * @param BpmTask	关联任务ID
	 * @param list 			候选人列表。
	 * void
	 */
	void addCandidate(BpmTask bpmTask, List<BpmIdentity> list);
	
	/**
	 * 添加任务候选人。
	 * @param taskId
	 * @param list 
	 * void
	 */
	void addCandidate(String taskId, List<BpmIdentity> list);
	
	/**
	 * 根据任务ID删除候选人。
	 * @param taskId
	 */
	void removeByTaskId(String taskId);
	
	List<DefaultBpmTaskCandidate> queryByTaskId(String taskId);
	
	/**
	 * 通过任务ID、执行人ID、类型ID取得候选人记录
	 * @param taskId
	 * @param executorId
	 * @param type 值为'user',或DefaultGroup中的dimKey值
	 * @return  DefaultBpmTaskCandidate
	 */
	DefaultBpmTaskCandidate getByTaskIdExeIdType(String taskId,String executorId,String type);
	
	
	/**
	  * 根据实例ID删除流程。
	  * @param instList 
	  * void
	  */
	 void delByInstList(List<String> instList);
	 
	 
	 /**
	  * 根据流程实例列表获取候选人列表。
	  * @param instList
	  * @return 
	  * List&lt;DefaultBpmTaskCandidate>
	  */
	 List<DefaultBpmTaskCandidate> getByInstList(List<String> instList);

	/**
	 * 更改任务获选人
	 * @param params
	 */
	void updateExecutor(Map<String,Object> params);
}

