package com.hotent.bpm.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.persistence.model.CopyTo;

public interface CopyToManager extends BaseManager<CopyTo>{
	
	
	/**
	 * 根据流程实例删除抄送数据。
	 * @param instList 
	 * void
	 */
	void delByInstList(List<String> instList);
	
	/**
	 * 获取用户接收的抄送。
	 * @param userId
	 * @param queryFilter
	 * @return PageList&lt;CopyTo>
	 */
	List<CopyTo> getReceiverCopyTo(String userId,QueryFilter queryFilter);
	
	/**
	 * 获取由我发起的抄送。
	 * @param userId
	 * @param filter
	 * @return PageList&lt;CopyTo>
	 */
	List<CopyTo> getMyCopyTo(String userId,QueryFilter filter);
	
	
	
	/**
	 * 
	 * 抄送转发给多人
	 * @param instanceId 流程实例id
	 * @param userIds 转发接收人id集合
	 * @param messageType 消息通知类型
	 * @param opinion 转发原因
	 * @param copyToType 类型原因
	 * @throws Exception 
	 */
	void transToMore(String instanceId,List<String> userIds,String messageType,String opinion,String copyToType,String taskId,String files,String selectNodeId) throws Exception;
	
}
