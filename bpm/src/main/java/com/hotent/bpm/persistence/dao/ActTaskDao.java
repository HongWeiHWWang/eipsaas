package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.ActTask;


public interface ActTaskDao extends BaseMapper<ActTask> {
	
	/**
	 * 根据流程实例ID列表删除相关的任务。
	 * @param list 
	 * void
	 */
	void delByInstList(@Param("list") List<String> list) ;
	
	/**
	 * 根据流程实例ID列表删除相关候选人。
	 * @param list 
	 * void
	 */
	void delCandidateByInstList(@Param("list") List<String> list);
	
	
	/**
	 * 删除指定的流程变量。
	 * @param list 
	 * void
	 */
	void delSpecVarsByInstList(@Param("list") List<String> list);
	
	/**
	 * 根据流程实例ID获取流程任务列表。
	 * @param instId
	 * @return  List&lt;ActTask>
	 */
	List<ActTask> getByInstId(@Param("instId") String instId);
	/**
	 * 删除task 排除id==parentId
	 * @param list
	 */
	void delTaskByInstList(@Param("list") List<String> list);
}
