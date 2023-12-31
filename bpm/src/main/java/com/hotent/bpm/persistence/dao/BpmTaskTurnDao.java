package com.hotent.bpm.persistence.dao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.bpm.persistence.model.DefaultBpmTaskTurn;


public interface BpmTaskTurnDao extends BaseMapper<DefaultBpmTaskTurn> {
	
	/**
	 * 根据任务ID和状态获取转办的数据。
	 * @param taskId	任务ID
	 * @param status
	 * @return DefaultBpmTaskTurn
	 */
	DefaultBpmTaskTurn getByTaskId(@Param("taskId") String taskId);
	
	/**
	 * 根据taskId 和被授权人ID 获取该转办任务
	 * @param taskId
	 * @param assigneeId
	 * @return
	 */
	List<DefaultBpmTaskTurn> getByTaskIdAndAssigneeId(@Param("taskId") String taskId,@Param("assigneeId") String assigneeId);
	
	/**
	 * 完成任务时执行。
	 * @param taskId
	 * @param user 
	 * void
	 */
	void updComplete(@Param("taskId") String taskId,@Param("execUserId") String execUserId,@Param("execUserName") String execUserName,
			@Param("finishTime") LocalDateTime localDateTime);
	
	/**
	 * 通过任务ID更新转办任务标题
	 * @param taskId
	 * @param subject
	 */
	void updSubjectByTaskId(@Param("taskId") String taskId, @Param("subject") String subject);
	
	/**
	* 根据流程实例列表删除任务。
	* @param instList 
	* void
	*/
	void delByInstList(@Param("list") List<String> instList);
	
	/**通过用户查询自己转发出去的流程**/
	IPage<DefaultBpmTaskTurn> getMyDelegate(IPage<DefaultBpmTaskTurn> page,@Param(Constants.WRAPPER) Wrapper<DefaultBpmTaskTurn> wrapper,@Param("defKey") String defKey);

	/**
	 * 获取我转办的在各分类下的数量
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> getMyDelegateCount(@Param("userId") String userId);
	/**
	 * 获取我转办的数量
	 * @param userId
	 * @return
	 */
	Long getMyDelegateCountByUserId(@Param("userId") String userId);

    void delByTaskId(@Param("taskId") String taskId);
	
}
