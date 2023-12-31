package com.hotent.bpm.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.persistence.model.BpmCustomSignData;

/**
 * 
 * <pre> 
 * 描述：bpm_custom_signdata 处理接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-03 17:18:18
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BpmCustomSignDataManager extends BaseManager<BpmCustomSignData>{

	/**
	 * 根据任务添加数据
	 * @param task
	 * @param preTaskId 发起二次签署的任务id
	 */
	void addCustomSignData(BpmTask task, String preTaskId);
	
	/**
	 * 根据人数添加多条
	 * @param task
	 * @param preTaskId 发起二次签署的任务id
	 * @param idList
	 */
	void addCustomSignDatas(BpmTask task, String preTaskId, List<BpmIdentity> idList);
	
	List<BpmCustomSignData> getByInstIdAndStatus(String instId, String status);
	
	List<BpmCustomSignData> getByInstIdAndStatus(String instId, List<String> status);
	
	/**
	 * 将oldStatus 状态 更改为B newStatus
	 * @param id
	 * @param oldStatus
	 * @param newStatus
	 * @param newCreateTaskId 
	 */
	void updateStatusByTaskId(String taskId, List<String> oldStatus, String newStatus, String newCreateTaskId);
	
	/**
	 * 将oldStatus 状态 更改为B newStatus
	 * @param id
	 * @param oldStatus
	 * @param newStatus
	 * @param newCreateTaskId 
	 */
	void updateStatusByTaskId(String taskId, String oldStatus, String newStatus, String newCreateTaskId);
	
	/**
	 * 将 oldStatusList 状态的数据更改为newStatus
	 * @param instId
	 * @param asList
	 * @param statusRetracted
	 */
	void updateStatusByInstId(String instId, List<String> oldStatusList, String newStatus);
	
	/**
	 * 通过任务ID获取顺签的直接后代(即parentId为任务ID所对应数据id的这条记录)
	 * @param taskId
	 * @return
	 */
	BpmCustomSignData getSequentialSonByTaskId(String taskId);
	
	/**
	 * 获取串并签前置任务对应的处于审批中和撤回审批中的数据
	 * @param instId
	 * @param taskId
	 * @return
	 */
	List<BpmCustomSignData> getSignDataByBeforeSignTaskId(String instId, String taskId);
	
	/**
	 * 根据实例ID 清空数据
	 * @param instanceId
	 */
	void removeByInstId(String instanceId);
	
	/**
	 * 获取并签下级未审批的数据
	 * @param taskId
	 * @return
	 */
	List<BpmCustomSignData> getParallelSonByTaskId(String taskId);
	
	/**
	 * 获取并签下级 所有的数据
	 * @param taskId
	 * @return
	 */
	List<BpmCustomSignData> getParallelAllSonByTaskId(String taskId);
	
	/**
	 * 获取串并签前置任务对应的所有数据
	 * @param instId
	 * @param taskId
	 * @return
	 */
	List<BpmCustomSignData> getAllSignDataByBeforeSignTaskId(String instanceId, String taskId);

	
	/**
	 * 获取兄弟指定状态的数据
	 * @param targetTaskId
	 * @return
	 */
	List<BpmCustomSignData> getBrotherByTaskId(String taskId, List<String> status);
}
