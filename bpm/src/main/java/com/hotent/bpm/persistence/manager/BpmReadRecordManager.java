package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmReadRecord;
import com.hotent.bpm.persistence.model.DefaultBpmTask;

import java.util.List;

public interface BpmReadRecordManager extends BaseManager<BpmReadRecord> {	
	/**
	 * 根据流程实例ID获取流程意见。
	 * @param instId
	 * @return 
	 * List&lt;BpmReadRecord>
	 */
	List<BpmReadRecord> getByInstId(String instId);
	
	/**
	 * 根据流程实例Id获取表单的意见数据,用于在表单展示意见。
	 * @param instId
	 * @return List&lt;BpmReadRecord>
	 */
	List<BpmReadRecord> getFormOpinionByInstId(String instId);
	
	/**
	 * 根据流程实例取得关联的流程实例ID列表。
	 * @param instId	流程实例ID
	 * @return  List&lt;String>
	 */
	List<String> getListByInstId(String instId);
	
	 /**
	  * 向上查询得到顶级的流程实例。
	  * @param instId 流程实例ID
	  * @return  String
	  */
	 String getTopInstId(String instId);
	
	 /**
	 * 根据流程实例，节点 获取 流程意见
	 * @param instId
	 * @param nodeId
	 * @return
	 */
	 List<BpmReadRecord> getByInstNodeId(String instId, String nodeId);

	 /**
	  * 根据任务id和阅读人id 查找记录
	  * @param taskId
	  * @param reader
	  * @return
	  */
	List<BpmReadRecord> getByTaskIdandrecord(String taskId, String ...reader);
	
	/**
	 * 判断任务是否被所属人阅读
	 * @param taskId
	 * @return
	 */
	Boolean isTaskReadByOwner(String taskId);

	 /**
	  * 根据流程实例id和阅读人id 查找记录
	  * @param taskId
	  * @param reader
	  * @return
	  */
	List<BpmReadRecord> getByinstidandrecord(String instId, String reader);
	
	
	/**
	 * 是否有记录 如果有，则这些任务已经被查看过
	 * 需要过滤掉转办等原任务
	 * @param list
	 * @return
	 */
	List<BpmReadRecord> getByTaskIds(List<DefaultBpmTask> list);
	
}
