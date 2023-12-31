package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.DefaultBpmTaskCandidate;


public interface BpmTaskCandidateDao  extends BaseMapper<DefaultBpmTaskCandidate> {
	public void removeByTaskId(@Param("taskId") String taskId);
	public List<DefaultBpmTaskCandidate> queryByTaskId(@Param("taskId") String taskId);
	
	/**
	 * 通过任务ID、执行人ID、类型ID取得候选人记录
	 * @param taskId
	 * @param executorId
	 * @param type 值为'user',或DefaultGroup中的dimKey值
	 * @return 
	 * DefaultBpmTaskCandidate
	 */
	DefaultBpmTaskCandidate getByTaskIdExeIdType(@Param("taskId") String taskId,@Param("executorId") String executorId,@Param("type") String type);
	
	
	/**
	 * 根据流程实例列表删除候选人数据。 
	 * @param instList 
	 * void
	 */
	void delByInstList(@Param("list") List<String> instList);
	
	
	/**
	 * 根据流程实例列表获取任务候选人。
	 * @param instList
	 * @return 
	 * List&lt;DefaultBpmTaskCandidate>
	 */
	List<DefaultBpmTaskCandidate> getByInstList(@Param("list") List<String> instList);

	/**
	 * 更改任务获选人
	 * @param params
	 */
	void updateExecutor(Map<String,Object> params);
}


