package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmAgentCondition;


public interface BpmAgentConditionDao extends BaseMapper<BpmAgentCondition> {
	
	/**
	 * 根据设定ID获取设定的条件。
	 * @param settingId
	 * @return List&lt;BpmAgentCondition>
	 */
	List<BpmAgentCondition> getBySettingId(@Param("settingId") String settingId);
	
	/**
	 * 根据settingId删除数据。
	 * @param settingId 
	 * void
	 */
	void removeBySettingId(@Param("settingId") String settingId);
}
