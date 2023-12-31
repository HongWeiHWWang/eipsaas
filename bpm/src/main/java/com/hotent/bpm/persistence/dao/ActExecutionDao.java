package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.ActExecution;


public interface ActExecutionDao extends BaseMapper<ActExecution> {
	
	/**
	 * 根据parentId获取流程实例列表。
	 * @param parentId
	 * @return 
	 * List&lt;String>
	 */
	List<String> getByParentsId(@Param("parentId") String parentId);
	
	/**
	 * 根据superID获取流程实例列表。
	 * @param superID
	 * @return 
	 * List&lt;String>
	 */
	List<String> getBySupperId(@Param("superID") String superID);
	
	/**
	 * 根据流程实例列表删除关联任务。
	 * @param instList 
	 * void
	 */
	void delTaskByByInstList(@Param("list") List<String> instList);
	
	/**
	 * 根据实例列表删除关联任务。
	 * @param instList 
	 * void
	 */
	void delCandidateByInstList(@Param("list") List<String> instList);

	/**
	 * 根据实例列表删除事件订阅。
	 * @param instList 
	 * void
	 */
	void delEventSubByInstList(@Param("list") List<String> instList);

	/**
	 * 根据流程实例列表删除变量。
	 * @param instList 
	 * void
	 */
	void delVarsByInstList(@Param("list") List<String> instList);
	
	/**
	 * 根据流程实例列表删除历史变量。
	 * @param instList 
	 * void
	 */
	void delHiVarByInstList(@Param("list") List<String> instList);
	
	/**
	 * 根据流程实例列表删除历史任务。
	 * @param instList 
	 * void
	 */
	void delHiTaskByInstList(@Param("list") List<String> instList);
	
	
	/**
	 * 根据流程实例列表删除历史实例。
	 * @param instList 
	 * void
	 */
	void delHiProcinstByInstList(@Param("list") List<String> instList);


	/**
	 * 删除历史任务候选人。
	 * @param instList 
	 * void
	 */
	void delHiCandidateByInstList(@Param("list") List<String> instList);


	/**
	 * 根据实例删除历史实例 。
	 * @param instList 
	 * void
	 */
	void delHiActInstByInstList(@Param("list") List<String> instList);
	
	/**
	 * 根据流程实例删除Excution。
	 * @param instList 
	 * void
	 */
	void delExectionByInstList(@Param("list") List<String> instList);
	
	/**
	 * 撤回发起人时 删除act 残留的数据
	 * @param includeBpmnIdList
	 */
	void delActiveByInstList(@Param("list") List<String> includeBpmnIdList);
	
	/**
	 * 撤回发起人时 删除任务相关的流程变量
	 * @param includeBpmnIdList
	 */
	void delActiveVarsByInstList(@Param("list") List<String> includeBpmnIdList);


}
