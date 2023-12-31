package com.hotent.bpm.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hotent.bpm.persistence.model.BpmReport;

/**
 * 报表查询接口
 * @author tangxin
 *
 */
public interface BpmReportDao {
	
	/**
	 * 查询发起流程
	 * @param defId
	 * @return
	 */
	List<BpmReport> queryFlow(@Param("defId")String defId);
	
	
}
