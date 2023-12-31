package com.hotent.bpm.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmReadRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface BpmReadRecordDao extends BaseMapper<BpmReadRecord> {
	 /**
	  * 根据实例ID查询意见列表。
	  * @param instIdList
	  * @return 
	  * List&lt;BpmReadRecord>
	  */
	 List<BpmReadRecord> getByInstIds(@Param("list") List<String> instIdList);
	
	 
	 /**
	  * 根据父实例ID查询子实例ID列表。
	  * @param supInstId
	  * @return  List&lt;BpmReadRecord>
	  */
	 List<String> getBySupInstId(@Param("procInstId") String supInstId);
	 
	 /**
	  * 查询父实例ID。
	  * @param instId
	  * @return BpmReadRecord
	  */
	 String getSupInstByInstId(@Param("procInstId") String instId);
	 
	 
	 /**
	  * 根据流程实例列表删除意见数据。
	  * @param instList 
	  * void
	  */
	 void delByInstList(@Param("list") List<String> instList);

	 /**
	  * 根据流程实例，节点ID 获取该节点审批意见
	  * @param instId
	  * @param nodeId
	  * @return
	  */
	List<BpmReadRecord> getByInstNodeId(@Param("instId") String instId, @Param("nodeId") String nodeId);

	/**
	  * 根据任务id和阅读人id 查找记录
	  * @param taskId
	  * @param reader
	  * @return
	  */
	List<BpmReadRecord> getByTaskIdandrecord(@Param("taskId") String taskId, @Param("reader") String ...reader);

	 /**
	  * 根据流程实例id和阅读人id 查找记录
	  * @param taskId
	  * @param reader
	  * @return
	  */
	List<BpmReadRecord> getByinstidandrecord(@Param("instId") String instId, @Param("reader") String reader);
	
	/**
	 * 获取任务被拥有者阅读的次数
	 * @param taskId
	 * @return
	 */
	Integer getReadByOwnerCountWithTaskId(@Param("taskId")String taskId);
	
	/**
	 * 这些任务是否有被阅读过的
	 * @param taskIds
	 * @return
	 */
	List<BpmReadRecord> getByTaskIds(@Param("taskIds")List<String> taskIds);
}
