package com.hotent.runtime.manager;

import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.runtime.model.BpmTaskSignSequence;

/**
 * 
 * <pre> 
 * 描述：顺序签署人员 处理接口
 * 构建组：x7
 * 作者:jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-10-09 10:40:32
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BpmTaskSignSequenceManager extends BaseManager<BpmTaskSignSequence>{
	/**
	 * 将a状态改为b状态
	 * @param procInstId
	 * @param rootTaskId
	 * @param statusa
	 * @param statusb
	 */
	void updateStatus(String procInstId, String rootTaskId, String statusa, String statusb);
	
	/**
	 * 根据taskId 获取记录
	 * @param id
	 * @return
	 */
	BpmTaskSignSequence getByTaskId(String id);
	
	/**
	 * 同意提交后 后台使用
	 * @param taskId
	 * @return {id:"用户id",name:"用户名称",taskId:"新任务id"}
	 */
	Map<String, String> getNextExecutor(String taskId);
	
	/**
	 * 预览获取下一步处理人
	 * @param taskId
	 * @return {id:"用户id",name:"用户名称",taskId:"新任务id"}
	 */
	Map<String, String> demoNextExecutor(String taskId);
	
	/**
	 * 获取顺序签署中的待审批的记录
	 * @param instId
	 * @param rootTaskId
	 * @return
	 */
	BpmTaskSignSequence getInApprovalByInstNodeId(String instId, String rootTaskId, String nodeId);
	
	/**
	 * 根据path 删除数据
	 * @param taskId
	 */
	void removeByPath(String taskId);
	
	/**
	 * A 撤回删除所有
	 * @param instanceId
	 * @param rootTaskId
	 */
	void removeByInstNodeId(String instanceId, String rootTaskId,String nodeId);

}
