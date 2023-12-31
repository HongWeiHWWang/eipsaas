package com.hotent.bpm.api.service;

import com.hotent.base.groovy.IScript;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;

/**
 * <pre> 
 * 描述：处理任务的操作动作服务类
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-4-下午4:12:48
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmTaskActionService  extends IScript {
	/**
	 * 完成任务
	 * @param taskId
	 * @return
	 * @throws Exception 
	 */
	 boolean finishTask(TaskFinishCmd taskCmd) throws Exception;
	 
	 
	 /**
	  * 创建任务接口。
	  * 给bpmx-core项目来实现此接口。
	  * @param task 
	  * void
	 * @throws Exception 
	  */
	 void create(BpmDelegateTask task) throws Exception;
	
	 /**
	  * 删除任务ID。 
	  * @param taskId 
	  * void
	  */
	 void remove(String taskId);
	 
	 
	 
	 /**
	  * 将任务委托给几候选个人进行处理。
	  * <pre>
	  * 	1.将任务执行人修改为指定的人员。
	  * 		bpm_task,act_ru_task 的assignee_
	  * 	2.通知相关人员。
	  * 	3.在bpm_task_turn中添加记录，任务所属人可以通过这个表查询数据。
	  * </pre>
	  * @param taskId	任务ID。
	  * @param toUser	转交的人员。 
	  * @param notifyType 通知类型。
	  * @param opinion 意见。
	  * void
	  */
	 void delegate(String taskId, String toUser, String notifyType, String opinion,String files) throws Exception;
	 
	 /**
	  * 根据任务ID终止流程。
	  * <pre>
	  * 1.根据任务ID查询到BPM_TASK记录。
	  * 2.发送通知消息，通知相关人员。	
	  * 3.删除bpm_task_candidate对应记录。
	  * 4.删除BPM_TASK记录。
	  * 5.删除act_ru_identitylink记录。
	  * 6.删除act_ru_task记录。
	  * 7.删除act_ru_execution的记录.
	  * 8.标记bpm_pro_inst，bpm_pro_inst_hi为人工终止。
	  * 9.归档bpm_pro_inst。
	  * </pre>
	  * @param taskId 
	  * @param informType
	  * @param cause
	  * void
	  */
	 void endProcessByTaskId(String taskId,String informType,String cause,String files) throws Exception;
	 
	 
	
}
