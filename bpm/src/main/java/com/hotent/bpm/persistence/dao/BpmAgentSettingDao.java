package com.hotent.bpm.persistence.dao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmAgentSetting;


public interface BpmAgentSettingDao extends BaseMapper<BpmAgentSetting> {
	
	/**
	 * 根据授权人和流程定义ID获取流程代理设定。
	 * @param authId
	 * @param flowKey
	 * @return BpmAgentSetting
	 */
	List<BpmAgentSetting> getSettingByFlowAndAuthidAndDate(@Param("authid")String authid,@Param("flowkey")String flowKey,@Param("date")LocalDateTime date);
	
	/**
	 * 检查全局代理是否存在冲突。
	 * @param authId
	 * @param beginDate
	 * @param endDate
	 * @return  Integer
	 */
	Integer getByAuthAndDate(Map<String,Object> params);
	
	
	
	
	/**
	 * 检查流程定义指定的流程定义是否存在冲突。
	 * @param authId
	 * @param beginDate
	 * @param endDate
	 * @param flowKey
	 * @return Integer
	 */
	Integer getByAuthDateFlowKey(Map<String,Object> params);
	
	
	
	/**
	 * 检查条件流程冲突。
	 * @param authId
	 * @param beginDate
	 * @param endDate
	 * @param flowKey
	 * @return Integer
	 */
	Integer getForCondition(Map<String,Object> params);
	

	
}
