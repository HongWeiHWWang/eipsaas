package com.hotent.bpm.persistence.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmTaskCommu;


public interface BpmTaskCommuDao extends BaseMapper<BpmTaskCommu> {
	
	/**
	 * 根据任务ID获取任务通知。
	 * @param taskId
	 * @return
	 */
	BpmTaskCommu getByTaskId(@Param("taskId") String taskId);

    /**
     * 根据流程实例ID获取任务通知。
     * @param instId
     * @return
     */
    BpmTaskCommu getByInstId(@Param("instId")String instId);
}
