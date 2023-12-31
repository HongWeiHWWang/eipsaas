package com.hotent.bpm.persistence.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmReportAct;

/**
 * 
 * @author tangxin
 *
 */
public interface BpmReportActDao extends BaseMapper<BpmReportAct>{
	
	/**
	 * 根据报表id查询报表列表
	 * @param reportId
	 * @return
	 */
	List<BpmReportAct> queryList(String reportId);
	
	/**
	 * 根据报表ID删除
	 * @param reportId
	 */
	void removeByReportId(String reportId);
}
