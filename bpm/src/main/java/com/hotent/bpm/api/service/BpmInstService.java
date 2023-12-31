package com.hotent.bpm.api.service;

import java.util.List;
import java.util.Map;

import com.hotent.base.groovy.IScript;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.cmd.ProcessInstCmd;
import com.hotent.bpm.api.model.process.inst.BpmInstanceTrack;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;

/**
 * 
 * <pre> 
 * 描述：流程实例服务接口定义
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-12-15-下午9:32:07
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmInstService  extends IScript {
	/**
	 * 启动流程实例。
	 * <pre>
	 * {@linkplain ProcessInstCmd} 参数说明
	 * <b>bpmnDefId</b>：流程定义ID，为act_re_procdef主键字段。
	 * <b>procDefId</b>:bpm_definition主键字段。
     * <b>flowKey</b>: act_re_procdef的KEY字段，bpm_definition的def_key_字段。
	 * 
	 * 这3个字段必填其中一个。
     * 
     * <b>instId</b>:流程实例ID，如果此实例ID不为空，那么表示从草稿启动流程。
	 * <b>destination</b>: 启动时跳转的目标节点，如果指定那么流程将会跳转到指定的任务节点。
	 * <b>businessKey</b>:业务主键，在发起流程时使用，这种情况适合单主键URL表单，流程外部调用的模式。
	 * <b>subject</b>:直接指定任务主题。
	 * <b>dataMode</b>:数据存储模式:
	 * bo:bo业务数据。
	 * 	如果为bo数据模式，那么需要调用setBusData设置bo数据。
	 * pair:键值对模式(业务表，主键值)
	 * 	如果为这种模式：
	 * 	需要在cmd变量中添加： form_identity_ ，主键值，数据类型默认是String,可以通过cmd 设置PkDataType。
	 * pk:业务主键
	 * </pre>
	 * @param processInstCmd
	 * @return
	 * @throws Exception 
	 */
	BpmProcessInstance startProcessInst(ProcessInstCmd processInstCmd) throws Exception;
	
	/**
	 * 保存流程实例为草稿。
	 * <pre>
	 *1.保存bo表单数据。
	 *2.保存BpmProcessInstance数据。
	 *	
	 *ProcessInstCmd
	 *bpmnDefId：流程定义ID，为act_re_procdef主键字段。
	 *procDefId:bpm_definition主键字段。
	 *flowKey: act_re_procdef的KEY字段，bpm_definition的def_key_字段。
	 *
	 *这3个字段必填其中一个。
	 *dataMode：数据类型为BO。
	 *setBusData:设置BO JSON数据。
	 * </pre>
	 * @param processInstCmd
	 * @return 
	 * BpmProcessInstance
	 * @throws Exception 
	 */
	Map saveDraft(ProcessInstCmd processInstCmd) throws Exception;
	
	/**
	 * 根据草稿启动流程。
	 * @param processInstance
	 * @return  BpmProcessInstance
	 * @throws Exception 
	 */
	BpmProcessInstance startDraftProcessInstance(BpmProcessInstance processInstance) throws Exception;
	
	/**
	 * 启动草稿的流程实例
	 * @param processInstance
	 * @param variables 变量
	 * @return  BpmProcessInstance
	 * @throws Exception 
	 */
	BpmProcessInstance startDraftProcessInstance(BpmProcessInstance processInstance,Map<String,Object> variables) throws Exception;
	
	/**
	 * 取得某用户发起的流程实例列表
	 * @param userId
	 * @return
	 */
	List<BpmProcessInstance> getProcessInstancesByUserId(String userId);
	/**
	 * 取得某用户发起的流程实例列表
	 * @param userId
	 * @param page
	 * @return
	 */
	PageList<BpmProcessInstance> getProcessInstancesByUserId(String userId,PageBean pageBean);
	/**
	 * 取得某用户发起的流程实例列表
	 * @param userId
	 * @param queryFilter
	 * @return List&lt;BpmProcessInstance>
	 */
	List<BpmProcessInstance> getProcessInstancesByUserId(String userId,QueryFilter queryFilter);
	
	/**
	 * 取得我参与的流程实例列表
	 * @param userId
	 * @return
	 */
	List<BpmProcessInstance> getAttendProcessInstancesByUserId(String userId);
	/**
	 * 取得某人参与的流程实例列表
	 * @param userId
	 * @param page
	 * @return
	 */
	PageList<BpmProcessInstance> getAttendProcessInstancesByUserId(String userId,PageBean pageBean);
	/**
	 * 取得某人参与的流程实例列表
	 * @param userId
	 * @param queryFilter
	 * @return
	 */
	List<BpmProcessInstance> getAttendProcessInstancesByUserId(String userId,QueryFilter queryFilter);
	

	/**
	 * 获取某个用户的草稿流程实例列表
	 * @param userId
	 * @param queryFilter
	 * @return List&lt;BpmProcessInstance>
	 */
	List<BpmProcessInstance> getDraftsByUserId(String userId,QueryFilter queryFilter);
	
	/**
	 * 取得所有的流程实例
	 * @param queryFilter
	 * @return
	 */
	List<BpmProcessInstance> getAll(QueryFilter queryFilter);
	/**
	 * 删除流程实例
	 * @param processInstId
	 * @return
	 */
	boolean removeProcessInstance(String processInstId);
	/**
	 * 挂起流程实例
	 * @param processInstId
	 * @return
	 */
	boolean suspendProcessInstance(String processInstId);
	/**
	 * 恢复挂起的流程实例
	 * @param processInstId
	 * @return
	 */
	boolean recoverProcessInstance(String processInstId);
	
	/**
	 * 判断流程实例是否挂起。
	 * <pre>
	 * 1.判断流程实例禁用状态。
	 * 2.判读流程定义是否禁用。
	 * </pre>
	 * @param processInstId
	 * @return  boolean
	 */
	boolean isSuspendByInstId(String processInstId);
	/**
	 * 结束流程实例
	 * @param processInstId
	 * @return
	 */
	boolean endProcessInstance(String processInstId);
	/**
	 * 通过Id获得流程实例明细
	 * @param processInstId
	 * @return
	 */
	BpmProcessInstance getProcessInstance(String processInstId);
	/**
	 * 通过BPMN的流程实例Id获取流程实例
	 * @param bpmnInstId
	 * @return  BpmProcessInstance
	 */
	BpmProcessInstance getProcessInstanceByBpmnInstId(String bpmnInstId);
	
	/**
	 * 按任务中的执行人Id 获取其待办的列表
	 * @param userId
	 * @return
	 */
	List<BpmProcessInstance> getByTaskUserId(String userId);
	/**
	 * 按任务中的执行人Id 获取其待办的列表，并且分页显示
	 * @param userId
	 * @param page
	 * @return
	 */
	PageList<BpmProcessInstance> getByTaskUserId(String userId,PageBean pageBean);
	
	/**
	 * 按任务中的执行人Id 获取其待办的列表，并且组合其他查询条件分页显示
	 * @param userId
	 * @param queryFilter
	 * @return
	 */
	List<BpmProcessInstance> getByTaskUserId(String userId,QueryFilter queryFilter);
	
	
	/**
	 * 根据流程defKey删除测试的流程实例。
	 * @param defKey	流程定义KEY。 
	 * void
	 */
	void removeTestInstByDefKey(String defKey);
	
	/**
	 * 流程发起人撤销流程实例。
	 * <pre>
	 * 	1.根据流程实例ID查找所有的子实例。
	 * 	2.查找相关的任务数据和Execution数据。
	 *  3.保留主Execution。
	 * 	4.创建新任务指向主流程实例。
	 * 	
	 * </pre>
	 * @param instanceId 
	 * void
	 * @throws Exception 
	 */
	void revokeInstance(String instanceId,String informType,String cause) throws Exception;
	
	/**
	 * 获取流程实例的已审批节点
	 * @param processInstId
	 * @return
	 * @throws Exception 
	 */
	List<UserTaskNodeDef> getApprovalNodes(String processInstId) throws Exception;
	
	/**
	 * 通过流程实例ID获取流程运行轨迹
	 * @param processInstId
	 * @return
	 */
	List<BpmInstanceTrack> getTracksByInstId(String processInstId);

}
