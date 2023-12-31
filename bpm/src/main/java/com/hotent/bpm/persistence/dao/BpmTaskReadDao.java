package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmTaskRead;


public interface BpmTaskReadDao extends BaseMapper<BpmTaskRead> {
	
	/**
	  * 根据流程实例列表删除任务。
	  * @param instList 
	  * void
	  */
	 void delByInstList(@Param("list") List<String> instList);
}
