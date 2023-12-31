package com.hotent.runtime.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.runtime.model.BpmTaskTransRecord;

/**
 * 任务流转记录接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface BpmTaskTransRecordManager extends BaseManager<BpmTaskTransRecord>{
	
	/**
	 * 根据任务id获取流转任务记录
	 * @param taskId
	 * @return
	 */
	BpmTaskTransRecord getByTaskId(String taskId);
	
	/**
	 * 获取用户的流转记录列表
	 * @param queryFilter
	 * @return
	 */
	PageList<BpmTaskTransRecord> getMyTransRecord(String userId,QueryFilter queryFilter);
	
	/**
	 * 获取流转记录列表
	 * @param queryFilter
	 * @return
	 */
	List<BpmTaskTransRecord> getTransRecordList(QueryFilter queryFilter);
}
