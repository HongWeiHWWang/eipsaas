package com.hotent.bpm.persistence.manager;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.persistence.model.BpmReportAct;
import com.hotent.bpm.persistence.model.BpmReportActVo;
import com.hotent.bpm.persistence.model.vo.FlowOrgCountVo;
import com.hotent.bpm.persistence.model.vo.FlowUserCountVo;

public interface BpmReportActManager extends BaseManager<BpmReportAct>{
	
	/**
	 * 保存统计设置
	 * @param bpmReportAct
	 * @return
	 */
	CommonResult<String> saveAct(BpmReportActVo bpmReportAct);
	
	/**
	 * 根据统计ID获取统计图表数据
	 * @param reportId
	 * @return
	 */
	CommonResult<JsonNode> getEchartsData(String reportId) throws Exception;

	/**
	 * 根据单个图表id获取数据
	 * @param id
	 * @return
	 */
	CommonResult<JsonNode> getSingleEchartsData(String id) throws Exception;

	/**
	 * 流程按部门统计发起数据
	 * @param queryFilter
	 * @return
	 */
	PageList<FlowOrgCountVo> flowOrgCountList(QueryFilter queryFilter);
	
	/**
	 * 流程按部门统计发起数据
	 * @param queryFilter
	 * @return
	 */
	List<Map<String, Object>> getFlowOrgSelectList(QueryFilter queryFilter);
	
	/**
	 * 流程按人员统计发起数据
	 * @param queryFilter
	 * @return
	 */
	PageList<FlowUserCountVo> flowUserCountList(QueryFilter queryFilter);
	
	/**
	 * 流程按人员统计发起数据
	 * @param queryFilter
	 * @return
	 */
	List<Map<String, Object>> getFlowUserSelectList(QueryFilter queryFilter);

	void removeByReportId(String id);
}
