package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmExeStackExecutor;


public interface BpmExeStackExecutorDao extends BaseMapper<BpmExeStackExecutor> {
	
	/**
	 * 根据任务Id获取堆栈执行人。
	 * @param taskId	任务ID
	 * @return 
	 * BpmExeStackExecutor
	 */
	public BpmExeStackExecutor getByTaskId(@Param("taskId") String taskId);
	
	
	/**
	 * 根据堆栈ID获取堆栈执行人。
	 * @param stackId	堆栈ID
	 * @return 
	 * List&lt;BpmExeStackExecutor>
	 */
	public  List<BpmExeStackExecutor> getByStackId(@Param("stackId") String stackId);

	/***
	 * 根据堆栈ID删除执行人
	 * @param stackId
	 */
	public void deleteByStackId(@Param("stackId") String stackId);

	/***
	 * 根据堆栈path 删除所有执行人
	 * @param stackPath
	 */
	public void deleteByStackIds(@Param("stackIds") String [] stackIds);
	
	/**
	 * 更新之前的堆栈执行人
	 * @param taskId
	 * @param assigneeId
	 * @param newTaskId
	 */
	public void updateByTaskId(@Param("taskId") String taskId,@Param("assigneeId")  String assigneeId,@Param("newTaskId")  String newTaskId);


}
