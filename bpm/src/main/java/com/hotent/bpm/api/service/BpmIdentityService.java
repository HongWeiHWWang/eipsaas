package com.hotent.bpm.api.service;

import java.util.List;

import com.hotent.base.groovy.IScript;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * 描述：流程组织架构服务
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-8-下午3:51:04
 * 版权：广州宏天软件有限公司版权所有
 */
public interface BpmIdentityService  extends IScript {	
	/**
	 * 根据流程实例ID 和 节点ID查询配置的BpmIdentity集合。
	 * @param procInstId	流程实例ID
	 * @param nodeId		流程节点ID
	 * @return  List&lt;BpmIdentity>
	 * @throws Exception 
	 */
	List<BpmIdentity> searchByNode(String procInstId,String nodeId) throws Exception;
	
	/**
	 * 
	 * 根据流程实例ID 和 节点ID查询配置的用户集合
	 * @param procInstId
	 * @param nodeId
	 * @return  List&lt;User>
	 * @throws Exception 
	 */
	List<IUser> queryUsersByNode(String procInstId,String nodeId) throws Exception;
	
	/**
	 * 根据任务ID查询用户集合。
	 * 如果该任务已有执行人，则返回执行人。如果没有，则返回候选人集合。
	 * @param taskId
	 * @return  List&lt;BpmIdentity>
	 */
	List<BpmIdentity> queryByTask(String taskId);
	
	/**
	 * 根据Bpm任务ID查询用户集合。
	 * 如果该任务已有执行人，则返回执行人。如果没有，则返回候选人集合。
	 * @param bpmnTaskId
	 * @return  List&lt;BpmIdentity>
	 */
	List<BpmIdentity> queryByBpmTask(String bpmnTaskId);
	
	/**
	 * 根据Bpm任务查询用户集合。
	 * 如果该任务已有执行人，则返回执行人。如果没有，则返回候选人集合。
	 * @param BpmTask {@linkplain BpmTask 流程任务 }
	 * @return  List&lt;BpmIdentity>
	 */
	List<BpmIdentity> queryByBpmTask(BpmTask bpmTask);
	/**
	 * 根据Bpm任务查询用户集合(包含用户的信息)。
	 * 如果该任务已有执行人，则返回执行人。如果没有，则返回候选人集合。
	 * @param BpmTask {@linkplain BpmTask 流程任务 } 
	 * @return  List&lt;BpmIdentity>
	 */
	List<BpmIdentity> queryListByBpmTask(BpmTask bpmTask);
	
	/**
	 * 
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	List<BpmIdentity> searchStartByNode(String defId,String nodeId);

	List<BpmIdentity> searchByNodeIdOnStartEvent(String defId, String nodeId) throws Exception;
}
