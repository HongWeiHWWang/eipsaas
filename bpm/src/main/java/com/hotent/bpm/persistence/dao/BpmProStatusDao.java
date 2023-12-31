package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.DefaultBpmProStatus;


public interface BpmProStatusDao extends BaseMapper<DefaultBpmProStatus> {
	
	
	
	public List<DefaultBpmProStatus> queryHistorys(@Param("procInstId") String procInstId);
	
	/**
	 * 根据流程实例ID归档。
	 * @param procInstId 
	 * void
	 */
	public void archiveHistory(@Param("procInstId") String procInstId);
	
	
	/**
	 * 返回流程状态数据。
	 * @param instId
	 * @param nodeId
	 * @return  int
	 */
	DefaultBpmProStatus getByInstNodeId(@Param("procInstId") String instId,@Param("nodeId") String nodeId);
	
	
	/**
	 * 根据流程实例列表删除数据。
	 * @param instList 
	 * void
	 */
	void delByInstList(List<String> instList);
	
	
	/**
	 * 更新待审批的节点为指定状态。
	 * @param list
	 * @param status 
	 * void
	 */
	void updStatusByInstList(@Param("list") List<String> list,@Param("status") String status);
}
