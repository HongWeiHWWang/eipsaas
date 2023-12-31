package com.hotent.runtime.manager;

import java.io.IOException;
import java.util.List;

import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.runtime.model.BpmTaskTrans;
import com.hotent.runtime.model.TaskTrans;
import com.hotent.runtime.params.RevokeSignLineParamObject;
import com.hotent.runtime.params.RevokeTransParamObject;
import com.hotent.runtime.params.TaskTransParamObject;
import com.hotent.uc.api.model.IUser;

/**
 * 流转任务接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface TaskTransService {

    /**
     * 征询回复。
     * <pre>
     * 	1.删除本任务。
     *  2.发送通知给发送人。
     * 	2.根据父任务ID修改票数，同意和反对的票数。
     * 	3.判断流程是否完成。
     * 		如果完成执行是否返回或或提交。
     * 		如果是再派生的。
     * 			1.如果为返回修改这个父任务状态为trans。
     * 			2.如果提交则根据提交的结果，对父任务进行投票。
     *  4.如果未完成那么判断流程是否是并行，如果是串行，那么取得下一个执行人，并产生任务。
     *
     * </pre>
     * @param taskId		任务ID
     * @param actionName	审批的意见同意或反对
     * @param opinion
     * @param isIntervene   是否管理员干预
     * void
     * @throws IOException
     * @throws Exception
     */
    void taskToInquReply(String taskId,String actionName,String notifyType, String opinion,boolean isIntervene,String files,String zFiles) throws IOException, Exception;


    /**
	 * 结束流转任务。
	 * <pre>
	 * 	1.删除本任务。
	 *  2.发送通知给发送人。
	 * 	2.根据父任务ID修改票数，同意和反对的票数。
	 * 	3.判断流程是否完成。
	 * 		如果完成执行是否返回或或提交。
	 * 		如果是再派生的。
	 * 			1.如果为返回修改这个父任务状态为trans。
	 * 			2.如果提交则根据提交的结果，对父任务进行投票。
	 *  4.如果未完成那么判断流程是否是并行，如果是串行，那么取得下一个执行人，并产生任务。
	 *  
	 * </pre>
	 * @param taskId		任务ID
	 * @param actionName	审批的意见同意或反对
	 * @param opinion 		
	 * void
	 * @throws IOException 
	 * @throws Exception 
	 */
	void completeTask(String taskId,String actionName,String notifyType, String opinion,String addSignAction) throws IOException, Exception;
	
	
	/**
	 * 获取我流转出去的流转任务。
	 * 根据规则判断是否完成。
	 * @param taskId 
	 * void
	 */
	@SuppressWarnings("rawtypes")
	PageList getMyTransTask(String userId,QueryFilter queryFilter);
	
	
	/**
	 * 撤销我的流转出去任务。
	 * <pre>
	 * 	1.根据当前任务查找下面的所有子任务。
	 * 	2.删除这些子任务并给子任务人员发送给通知。
	 * 	3.将当前任务修改状态。
	 * 		普通任务修改为normal。
	 * 		流转任务修改为transformed.
	 * </pre>
	 * @param taskId
	 * @param opinion 
	 * void
	 */
	void withDraw(String taskId,String notifyType,String opinion) throws Exception;

	/**
	 * 添加流转任务。
	 * <pre>
	 * 	1.选择多个执行人，那么会产生多个任务。
	 * 		在BPM_TASK表中添加多条任务数据，任务类型为trans，这些任务的exec_id_为空，表示为派生的任务。
	 * 		这些任务父ID为parentTaskId。
	 * 	2.更新父任务类型为startTrans，这类任务在查询是不可见，并且检查不能执行。
	 * 	3.在bpm_task_trans表中添加一条记录，添加会签规则。
	 * 	4.根据通知类型发送通知。
	 * </pre>
	 * @param opinion
	 * @param notifyType 
	 * void
	 * @throws IOException 
	 */
	void addTransTask(TaskTrans taskTrans, List<IUser> listUsers, String notifyType, String opinion,String formData,String files,Boolean addSignUser) throws Exception;
	
	/**
	 * 获取流转信息
	 * @param taskId
	 * @return
	 */
	TaskTrans getTransTaskByTaskId(String taskId);


	CommonResult<String> addSign(TaskTransParamObject taskTransParamObject) throws Exception;


    /**
     * 征询设置
     * @param taskTrans
     * @param listUsers
     * @param notifyType
     * @param opinion
     * @param files
     * @throws Exception
     */
    void addTaskToInqu(TaskTrans taskTrans, List<IUser> listUsers, String notifyType, String opinion, String files) throws Exception;

    /**
     * 添加并行审批任务
     * @param taskTrans
     * @param listUsers
     * @param notifyType
     * @param opinion
     * @throws Exception
     */
    void addApproveLineTask(TaskTrans taskTrans, List<IUser> listUsers, String notifyType, String opinion) throws Exception;


	void addSignSequenceTask(BpmTaskTrans taskTrans, List<IUser> userList,
			String notifyType, String opinion) throws Exception;
    
   /**
    * 并行审批任务撤回
    * @param bpmTask
    * @param user
    * @param messageType
    * @throws Exception
    */
    void addRevokeTask(DefaultBpmTask bpmTask, IUser user,String messageType) throws Exception;
    
    /**
	 * 撤销我的流转出去任务。
	 * <pre>
	 * 	1.根据当前任务查找下面的所有子任务。
	 * 	2.删除这些子任务并给子任务人员发送给通知。
	 * 	3.将当前任务修改状态。
	 * 		普通任务修改为normal。
	 * 		流转任务修改为transformed.
	 * </pre>
	 * @param taskId
	 * @param opinion 
	 * void
	 */
	void withDraw(String taskId,String notifyType,String opinion,String msgTemplate) throws Exception;


	void revokeSignSequence(RevokeTransParamObject revokeTransParamObject) throws IOException, Exception;

	/**
	 * 发起并行审批签署
	 * @param taskTrans
	 * @param userList
	 * @param notifyType
	 * @param opinion
	 * @throws Exception
	 */
	void addSignLineTask(BpmTaskTrans taskTrans, List<IUser> userList,
			String notifyType, String opinion) throws Exception;


	void revokeSignLine(RevokeSignLineParamObject revokeParamObject) throws Exception;
	
	void addCheckOpinion(DefaultBpmTask bpmTask, OpinionStatus opinionStatus, String transUser,
			String opinion, boolean isCompleted) throws Exception;
}
