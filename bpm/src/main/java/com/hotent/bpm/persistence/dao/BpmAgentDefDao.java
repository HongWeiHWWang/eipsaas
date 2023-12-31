package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmAgentDef;


public interface BpmAgentDefDao extends BaseMapper<BpmAgentDef> {
	
	/**
	 * 根据代理设定删除。
	 */
	void removeBySettingId(@Param("settingId") String settingId);
	/**
	 * 根据代理设定id获取代理流程
	 * @param id
	 * @return
	 */
	List<BpmAgentDef> getBySettingId(@Param("id") String id);
}
