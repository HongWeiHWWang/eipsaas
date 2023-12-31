package com.hotent.bpm.api.service;

import java.util.List;

import com.hotent.base.groovy.IScript;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * 描述：流程任务服务接口（专门负责查询方法）
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-8-上午10:51:13
 * 版权：广州宏天软件有限公司版权所有
 */
public interface BpmTaskService  extends IScript {
	 /**
	  * 根据任务ID获取任务。
	  * @param taskId
	  * @return  BpmTask
	  */
	 BpmTask getByTaskId(String taskId);	 	 	
	 
	 /**
	  * 根据任务ID获取子任务列表。
	  * @param taskId
	  * @return
	  */
	 List<BpmTask>  getChildsByTaskId(String taskId);
	 
	/**
	 * 按用户ID获取所有的待办任务列表
	 * @param userId
	 * @return  List&lt;BpmTask>
	 */
	 List<BpmTask> getTasksByUserId(String userId);
	 
	 /**
	 * 根据任务ID获取所有的相关人员，包括任务执行人和候选人。
	 * @param userId
	 * @return  List&lt;BpmTask>
	 */
	 List<IUser> getUsersByTaskId(String taskId);
	 
	 
	 /**
	  * 根据流程实例ID查询任务。
	  * @param instId
	  * @return  List&lt;BpmTask>
	  */
	 List<BpmTask> getTasksInstId(String instId);	 
	 
	 /**
	  * 根据流程实例和用户ID查询任务列表。
	  * @param instId
	  * @param userId
	  * @return  List&lt;BpmTask>
	  */
	 List<BpmTask> getTaskByInstUser(String instId,String userId);	 
	 
	/**
	 * 按用户ID获取所有的待办任务列表
	 * @param userId
	 * @param page
	 * @return  List&lt;BpmTask>
	 */
	 List<BpmTask> getTasksByUserId(String userId,PageBean page);
	 
	/**
	 * 按用户ID及条件过滤获取所有的待办任务列表
	 * @param userId
	 * @param filter
	 * @return
	 */
	 List<BpmTask> getTasksByUserId(String userId,QueryFilter filter);
	
	 /**
	 * 查找所有的任务列表并进行分页过滤查询
	 * @param filter
	 * @return
	 */
	 List<BpmTask> getAllTasks(QueryFilter filter);
	
	
	
	/**
	 * 是否可以访问任务实例。
	 * <pre>
	 * 	任务是否能被某些人进行访问。
	 *  1.任务所有人。
	 *  2.任务候选人。
	 *  	1.取出候选人列表。
	 *  	2.遍历候选人列表。
	 *  		1.如果候选人类型为用户，直接比对当前人和候选人的ID。
	 *  		2.如果为组类型。
	 *  			1.首先个人所属组的列表。
	 *  			2.判断候选组是否在这个人的组中。
	 * </pre>
	 * @param taskId 
	 * void
	 */
	boolean canAccessTask(String taskId,String userId);
	
	
	/**
	 * 锁定任务
	 * @param taskId  BpmTask的主键
	 * void
	 */
	void lockTask(String taskId,String userId);
	/**
	 * 解锁任务
	 * @param taskId BpmTask的主键  
	 * void
	 */
	void unlockTask(String taskId);
	
	/**
	 * 如果任务执行人和当前人不是同一个人则，不能处理任务。
	 * <br/>
	 * 当任务执行人为空或者和当前人相同时返回true。
	 * <pre>
	 *  这个主要防止，一个任务由多个候选人处理一个任务的情况。
	 *  比如：
	 *  一个任务有A,B两个候选人:
	 *  1. 如果A在列表中打开此任务并锁定，这个时候B就不用在处理了。
	 *  B点击任务是判断任务执行人是否是自己，不是自己就不能处理了。
	 *  
	 *  2.A和B都打开任务，A和B ，A先锁定任务，这个时候B如果在处理任务，就需要
	 *  提醒B用户，告诉B，任务不能由他处理。
	 * 
	 * </pre>
	 * @param taskId
	 * @return 0:任务已经处理,1:可以处理,2,被其他人锁定
	 */
	int canLockTask(String taskId);
	
	/**
	 * 设置任务的执行人
	 * @param taskId
	 * @param assigneeId 
	 * void
	 */
	void assigTask(String taskId,String assigneeId);
	/**
	 * 增加候选用户 
	 * @param taskId 
	 * @param userIds 
	 * void
	 */
	void addCandidateUsers(String taskId,String[]userIds);
	/**
	 * 增加候选的用户与用户组
	 * @param taskId
	 * @param candidates 为IdentityType的子类，如用户或用户组
	 * void
	 */
	void addCandidates(String taskId,List<BpmIdentity> candidates);
	
	
	/**
	 * 设置任务执行人。
	 * <pre>
	 * 	根据用户ID个数判断。
	 *  1.如果只有一个人员，那么这个人就是任务的执行人。
	 *  2.如果有多个那么这些人员为任务的候选人。
	 *  3.如果任务只有一个那么需要通知原有的执行人。
	 * </pre>
	 * @param taskId	任务ID
	 * @param userIds	用户ID
	 */
	void setTaskExecutors(String taskId,String[] userIds,String notifyType,String opinion) throws Exception;
	
	/**
	 * 设置任务是否跳过
	 * @param bpmTask 
	 * void
	 * @throws Exception 
	 */
	void setTaskSkip(BpmTask bpmTask) throws Exception;
	
	/**
	 * 获取任务的候选人
	 * @param taskId
	 * @return 
	 * List&lt;BpmIdentity>
	 */
	List<BpmIdentity> getTaskCandidates(String taskId);
	
	
	/**
	 * 保存草稿。
	 * @param cmd
	 */
	void saveDraft(TaskFinishCmd cmd);
	
}
