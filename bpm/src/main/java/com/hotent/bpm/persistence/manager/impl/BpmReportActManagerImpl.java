package com.hotent.bpm.persistence.manager.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.hotent.base.manager.CommonManager;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.constants.DataSourceConsts;
import com.hotent.base.constants.SQLConst;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.service.BpmOpinionService;
import com.hotent.bpm.persistence.dao.BpmReportActDao;
import com.hotent.bpm.persistence.dao.BpmReportDao;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmReportActManager;
import com.hotent.bpm.persistence.manager.BpmReportListManager;
import com.hotent.bpm.persistence.model.BpmReportAct;
import com.hotent.bpm.persistence.model.BpmReportActVo;
import com.hotent.bpm.persistence.model.BpmReportList;
import com.hotent.bpm.persistence.model.vo.FlowOrgCountVo;
import com.hotent.bpm.persistence.model.vo.FlowUserCountVo;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmReportActManager")
public class BpmReportActManagerImpl extends BaseManagerImpl<BpmReportActDao, BpmReportAct> implements BpmReportActManager{
	

	@Resource
	BpmDefinitionManager pmDefinitionManager;
	@Resource
	BpmReportDao bpmReportDao;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmOpinionService bpmOpinionService;
	@Resource
	IUserService ius;
	@Resource
	UCFeignService ucService;
	@Resource
	BpmReportListManager bpmReportListManager;
	@Resource
	BpmReportActManager bpmReportActManager;
	@Resource
	DatabaseContext databaseContext;
	@Resource
	protected SqlSessionTemplate sqlSessionTemplate;
	@Resource
	CommonManager commonManager;
	private static final String NAME_SPACE = "com.hotent.sql.common"; // mybatis命名空间


	@Override
    @Transactional
	public CommonResult<String> saveAct(BpmReportActVo bpmReportAct) {
		String resultMsg=null;
		String actId=bpmReportAct.getId();
		String reportId=bpmReportAct.getReportId();
		try {
			if(StringUtil.isEmpty(actId)){
				bpmReportAct.setId(UniqueIdUtil.getSuid());
				if(StringUtil.isEmpty(reportId)) {
					if(StringUtil.isEmpty(bpmReportAct.getName())){
						return new CommonResult<>(false, "标题不能为空！");
					}
					BpmReportList reportList = new BpmReportList();
					String id = UniqueIdUtil.getSuid();
					reportList.setId(id);
					reportList.setName(bpmReportAct.getName());
					IUser user = ContextUtil.getCurrentUser();
					String userId = user.getUserId();
					String userName = user.getUsername();
					reportList.setCreateBy(userId);
					reportList.setCreateName(userName);
					ObjectNode org=ucService.getMainGroup(userId);
					if(BeanUtils.isNotEmpty(org)){
						String orgId=org.get("id").asText();
						String orgName=org.get("name").asText();
						reportList.setCreateOrgId(orgId);
						reportList.setOrgName(orgName);
					}
					bpmReportListManager.create(reportList);
					bpmReportAct.setReportId(id);
				}
				bpmReportActManager.create(bpmReportAct);
				resultMsg="添加报表成功";
			}else{
				bpmReportActManager.update(bpmReportAct);
				resultMsg="更新报表成功";
			}
			if(StringUtil.isNotEmpty(reportId)){
				BpmReportList reportList = bpmReportListManager.get(reportId);
				if(BeanUtils.isNotEmpty(reportList) && StringUtil.isNotEmpty(bpmReportAct.getName()) &&
						!bpmReportAct.getName().equals(reportList.getName())){
					reportList.setName(bpmReportAct.getName());
					bpmReportListManager.update(reportList);
				}
			}
			return new CommonResult<>(true, resultMsg, bpmReportAct.getReportId());
		} catch (Exception e) {
			resultMsg="对报表操作失败";
			return new CommonResult<>(false, resultMsg);
		}
	}

	@Override
	public CommonResult<JsonNode> getEchartsData(String reportId) throws Exception{
		ArrayNode array = JsonUtil.getMapper().createArrayNode();
		BpmReportList reportList = bpmReportListManager.get(reportId);
		if(BeanUtils.isNotEmpty(reportList)){
			List<BpmReportAct> acts = baseMapper.queryList(reportId);
			for (BpmReportAct act : acts) {
				ObjectNode node = analysisData(act);
				array.add(node);
			}
		}else{
			return new CommonResult<>(false, "获取失败！id为【"+reportId+"】的统计不存在！");
		}
		return new CommonResult<JsonNode>(true, "获取成功！", array);
	}

	@Override
	public CommonResult<JsonNode> getSingleEchartsData(String id) throws Exception{
		ObjectNode node = JsonUtil.getMapper().createObjectNode();
		BpmReportAct act = super.get(id);
		if (BeanUtils.isNotEmpty(act)){
			node = analysisData(act);
		}else{
			return new CommonResult<>(false, "获取失败！id为【"+id+"】的统计不存在！");
		}
		return new CommonResult<JsonNode>(true, "获取成功！", node);
	}
	
	private ObjectNode analysisData(BpmReportAct act) throws IOException{
		ObjectNode node = JsonUtil.getMapper().createObjectNode();
		node.put("name", act.getReportName());
		ObjectNode porp =  (ObjectNode) JsonUtil.toJsonNode(act.getPorp());
		//流程范围类型：“1”流程；“2”流程分类
		String flowType = BeanUtils.isNotEmpty(porp.get("flowType"))?porp.get("flowType").asText():"1";
		//部门/人员范围类型：“1”部门；“2”人员
		String orgType = BeanUtils.isNotEmpty(porp.get("orgType"))?porp.get("orgType").asText():"1";
		//统计维度 org:组织维度，flow:流程或流程分类维度
		String dimension = BeanUtils.isNotEmpty(porp.get("dimension"))?porp.get("dimension").asText():"org";
		//流程或流程分类ID
		String ids = "";
		//流程或流程分类名称
		String names = "";
		if(BeanUtils.isNotEmpty(porp.get("id"))){
			ids = porp.get("id").asText();
			names = porp.get("name").asText();
		}
		//HaspMap为无序
		Map<String,String> flowMap = new LinkedHashMap<String, String>();
		if(StringUtil.isNotEmpty(ids)){
			String[] idArray = ids.split(",");
			String[] idNameArray = names.split(",");
			for (int i = 0; i < idArray.length; i++) {
				flowMap.put(idArray[i], idNameArray[i]);
			}
		}
		//部门ID
		String orgIds = "";
		//部门名称
		String orgNames = "";
		if(BeanUtils.isNotEmpty(porp.get("orgIds"))){
			orgIds = porp.get("orgIds").asText();
			orgNames = porp.get("orgNames").asText();
		}
		//HashMap为无序
		Map<String,String> orgMap = new LinkedHashMap<String, String>();
		if(StringUtil.isNotEmpty(orgIds)){
			String[] orgIdArray = orgIds.split(",");
			String[] orgNameArray = orgNames.split(",");
			for (int i = 0; i < orgIdArray.length; i++) {
				orgMap.put(orgIdArray[i], orgNameArray[i]);
			}
		}
		//统计类型 year：年度，quarter：季度，monthly：月度，custom：自定义时间段
		String calcuCycle = porp.get("calcuCycle").asText();
		//开始时间
		String calcuStart = porp.get("calcuStart").asText();
		String startTime = getDateTime(calcuCycle, calcuStart, true);
		//结束时间
		String calcuEnd = porp.get("calcuEnd").asText();
		String endTime = getDateTime(calcuCycle, calcuEnd, false);
		//统计任务类型
		ObjectNode flowStatus = (ObjectNode) porp.get("flowStatus");
		boolean isRunning = flowStatus.get("running").asBoolean();
		boolean isBack = flowStatus.get("back").asBoolean();
		boolean isManualend = flowStatus.get("manualend").asBoolean();
		boolean isEnd = flowStatus.get("end").asBoolean();
		boolean isInstances = BeanUtils.isEmpty(flowStatus.get("instances"))?false:flowStatus.get("instances").asBoolean();
		boolean isHourLong = BeanUtils.isEmpty(flowStatus.get("hourLong"))?false:flowStatus.get("hourLong").asBoolean();
		boolean isIncomplete = BeanUtils.isEmpty(flowStatus.get("incomplete"))?false:flowStatus.get("incomplete").asBoolean();
		boolean isAvgLong = BeanUtils.isEmpty(flowStatus.get("avgLong"))?false:flowStatus.get("avgLong").asBoolean();
		boolean isOvertime = BeanUtils.isEmpty(flowStatus.get("overtime"))?false:flowStatus.get("overtime").asBoolean();
		boolean isClosingRate = BeanUtils.isEmpty(flowStatus.get("closingRate"))?false:flowStatus.get("closingRate").asBoolean();
		
		//统计类型 start_throughput：启动流程吞吐量，handle_throughput：办件吞吐量，handle_efficiency：办件效率
		String params = act.getParams();
		ObjectNode rightContentNode = (ObjectNode) JsonUtil.toJsonNode(act.getRightContent());
		boolean histogram = rightContentNode.get("histogram").asBoolean();
		boolean line = rightContentNode.get("line").asBoolean();
		boolean pie = rightContentNode.get("pie").asBoolean();
		boolean dataViews = rightContentNode.get("dataViews").asBoolean();
		String default1 = act.getIsDefault();
		ObjectNode analysisNode = getAnalysisNode(flowType, dimension, ids, names, orgIds, orgNames, startTime, endTime, isRunning, isBack, isManualend, isEnd,isInstances,isHourLong,isIncomplete,isAvgLong,isOvertime,isClosingRate, flowMap, orgMap, default1,params,orgType);
		String preDesc = "org".equals(dimension)?"部门/人员":"流程/流程分类";
		String calcuCycleDesc = "year".equals(calcuCycle)?"年度":"quarter".equals(calcuCycle)?"季度":"monthly".equals(calcuCycle)?"月度":"自定义时间段";
		String calcuDesc = "（"+calcuStart+"至"+calcuEnd+"）";
		if("quarter".equals(calcuCycle)) {
			calcuDesc="（"+calcuStart+"季度"+"至"+calcuEnd+"季度"+"）";
		}

		String ptypeDesc = "启动吞吐量";
		if("handle_throughput".equals(params)){//办件吞吐量
			ptypeDesc = "办件吞吐量";
		}else if("handle_efficiency".equals(params)){//办件效率
			ptypeDesc = "办件效率";
		}else if ("work_status".equals(params)){
			ptypeDesc = "工作状态";
		}else if ("task_num".equals(params)){
			ptypeDesc = "任务量";
		}else if ("inst_start_num".equals(params)){
			ptypeDesc = "流程启动";
		}else if ("flow_status".equals(params)){
			ptypeDesc = "流程状态";
		}
		analysisNode.put("name", act.getReportName());
		analysisNode.put("histogram", histogram);
		analysisNode.put("line", line);
		analysisNode.put("pie", pie);
		analysisNode.put("dataViews", dataViews);
		analysisNode.put("id", act.getId());
		analysisNode.put("subtext", "按"+preDesc+"维度"+calcuCycleDesc+calcuDesc+ptypeDesc+"统计图表");
		return analysisNode;
	}
	
	private ObjectNode getAnalysisNode(String flowType,String dimension,String ids,String names, String orgIds, String orgNames, String startTime, String endTime, boolean isRunning, boolean isBack, boolean isManualend, boolean isEnd,
			boolean isInstances,boolean isHourLong,boolean isIncomplete,boolean isAvgLong,boolean isOvertime,boolean isClosingRate,Map<String,String> flowMap,Map<String,String> orgMap,String default1,String params,String orgType) throws IOException{
		ObjectNode startNode = JsonUtil.getMapper().createObjectNode();
		String unionSql = getUnionSql(flowType, dimension, ids, orgIds, startTime, endTime, isRunning, isBack, isManualend, isEnd, isInstances, isHourLong, isIncomplete, isAvgLong, isOvertime, isClosingRate, params, orgType);
		List<String> legend = new ArrayList<String>();
		if("start_throughput".equals(params)){//启动流程吞吐量
			if(isRunning) {
				legend.add("审批中");
			}
			if(isBack) {
				legend.add("驳回/驳回发起人/撤回");
			}
			if(isManualend) {
				legend.add("废弃/终止");		
			}
			if(isEnd) {
				legend.add("审批完成");
			}
			if(isInstances) {
				legend.add("工单数量");
			}
			if(isHourLong) {
				legend.add("运行时长（小时）");
			}
			if(isIncomplete) {
				legend.add("未完成工单");
			}
			if(isAvgLong) {
				legend.add("平均时长（小时）");
			}
			if(isOvertime) {
				legend.add("逾期工单");
			}
			if(isClosingRate) {
				legend.add("闭单率（%）");
			}
//			legend = Arrays.asList("审批中","驳回/驳回发起人/撤回","废弃/终止","审批完成");		
		}else if("handle_throughput".equals(params)){//办件吞吐量
			legend = Arrays.asList("审批次数","沟通次数","征询次数","转办次数","传阅次数");
		}else if("handle_efficiency".equals(params)){//办件效率
			legend = Arrays.asList("办件平均耗时（小时）","审批平均耗时（小时）","沟通平均耗时（小时）","转办平均耗时（小时）");
		}else if("work_status".equals(params) || "flow_status".equals(params)){
			legend = Arrays.asList("草稿","运行中","完结","已退回","失效");
		}else if("task_num".equals(params)){
			legend = Arrays.asList("任务量");
		}else if("inst_start_num".equals(params)){
			legend = Arrays.asList("启动数量");
		}
		//输出拼接的sql语句
		System.out.println(unionSql);
		List<Map<String, Object>> selectList = querySql(unionSql);
		if ("flow_status".equals(params)){
			startNode.set("legend",JsonUtil.toJsonNode(legend));
			ArrayNode series = getPieSeries(selectList,legend,params);
			startNode.set("series",series);
			return startNode;
		}
		List<String> xAxis = Arrays.asList("org".equals(dimension)?orgNames.split(","):names.split(","));
		ArrayNode series = getSeries(legend, selectList, "org".equals(dimension)?orgMap:flowMap, default1,params);
		startNode.set("legend", JsonUtil.toJsonNode(legend));
		startNode.set("xAxis", JsonUtil.toJsonNode(xAxis));
		startNode.set("series", series);
		return startNode;
	}

	private ArrayNode getPieSeries(List<Map<String, Object>> selectList, List<String> legends, String params) {
		ArrayNode allSeries = JsonUtil.getMapper().createArrayNode();
		ObjectNode aSeries = JsonUtil.getMapper().createObjectNode();
		aSeries.put("type","pie");
		aSeries.put("name","流程状态");
		aSeries.put("radius","55%");
		aSeries.put("selectedMode","single");
		ArrayNode data = JsonUtil.getMapper().createArrayNode();
		for (String legend:legends){
			String status = getLegendCode(legend,params);
			ObjectNode node = JsonUtil.getMapper().createObjectNode();
			node.put("name",legend);
			node.put("value",Integer.valueOf(selectList.get(0).get(status).toString()));
			data.add(node);
		}
		aSeries.set("data",data);
		allSeries.add(aSeries);
		return allSeries;
	}

	private ArrayNode getSeries(List<String> legend,List<Map<String, Object>> selectList,Map<String,String> map,String default1,String params) throws IOException{
		ArrayNode array = JsonUtil.getMapper().createArrayNode();
		String defType = "1".equals(default1)?"bar":"line";
		for (String le : legend) {
			ObjectNode node = JsonUtil.getMapper().createObjectNode();
			node.put("name",le);
			node.put("type",defType);
			List<Object> data = new ArrayList<>();
			if ("work_status".equals(params) || "task_num".equals(params) || "inst_start_num".equals(params)){
				data = getOtherSeriesData(selectList,le,params,map);
			}else{
				data = getSeriesData(selectList, map, le,params);
			}
			node.set("data",JsonUtil.toJsonNode(data));
			array.add(node);
		}
		return array;
	}

	private List<Object> getOtherSeriesData(List<Map<String,Object>> selectList,String legend,String param,Map<String,String> map){
		String status = getLegendCode(legend,param);
		List<Object> data = new ArrayList<>();
		for (String key : map.keySet()) {
			int length = data.size();
			for (Map<String,Object> select:selectList){
				if (key.equals(select.get("id").toString())){
					data.add(Integer.valueOf(select.get(status).toString()));
				}
			}
			if (data.size()==length){
				data.add(0);
			}
		}

		return data;
	}
	
	private List<Object> getSeriesData(List<Map<String, Object>> selectList,Map<String,String> map,String legend,String params) throws IOException{
		List<Object> data = new ArrayList<Object>();
		String status = getLegendCode(legend,params);
		for (String key : map.keySet()) {
			double isValue = 0;
			long durMs = 0;
			for (Object obj : selectList) {
				if(BeanUtils.isNotEmpty(obj)){
					ObjectNode oNode = (ObjectNode) JsonUtil.toJsonNode(obj);
					if(BeanUtils.isNotEmpty(oNode.get("id")) && key.equals(oNode.get("id").asText())
							&& status.equals(oNode.get("status").asText())){
						double count = BeanUtils.isNotEmpty(oNode.get("count"))?oNode.get("count").asDouble():0;
						isValue += count;
					}//处理oracle返回格式大小问题
					else if(BeanUtils.isNotEmpty(oNode.get("ID")) && key.equals(oNode.get("ID").asText())
							&& status.equals(oNode.get("STATUS").asText())){
						double count = BeanUtils.isNotEmpty(oNode.get("COUNT"))?oNode.get("COUNT").asDouble():0;
						isValue += count;
					}
					if(BeanUtils.isNotEmpty(oNode.get("durMs")) && key.equals(oNode.get("id").asText())
							&& status.equals(oNode.get("status").asText())){
						durMs += oNode.get("durMs").asLong();
					}//处理oracle返回格式大小问题
					else if(BeanUtils.isNotEmpty(oNode.get("DURMS")) && key.equals(oNode.get("ID").asText())
							&& status.equals(oNode.get("STATUS").asText())){
						durMs += oNode.get("DURMS").asLong();
					}

				}
			}
			if("handle_efficiency".equals(params)){//办件效率统计时，毫秒转小时
				if(isValue >0 && durMs>0){
					BigDecimal b1 = new BigDecimal(Double.toString(durMs/isValue));  
					BigDecimal b2 = new BigDecimal(Double.toString(1000*60*60));  
					double num = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
			        data.add(num);
				}else{
					data.add(0);
				}
			}else{
				data.add(isValue);
			}
		}
		return data;
	}
	
	private String getLegendCode(String desc,String params){
		String status = "";
		if("start_throughput".equals(params)){//启动流程吞吐量
			switch (desc) {
				case "审批中":
					status = "running";
					break;
				case "审批完成":
					status = "end";
				break;
				case "驳回/驳回发起人/撤回":
					status = "backRevoke";
				break;
				case "废弃/终止":
					status = "manualend";	
				break;
				case "工单数量":
					status = "instances";	
				break;
				case "运行时长（小时）":
					status = "hourLong";	
				break;
				case "未完成工单":
					status = "incomplete";	
				break;
				case "平均时长（小时）":
					status = "avgLong";	
				break;
				case "逾期工单":
					status = "overtime";	
				break;
				case "闭单率（%）":
					status = "closingRate";	
				break;
			}
		}else if("handle_throughput".equals(params)){//办件吞吐量
			switch (desc) {
				case "审批次数":
					status = "agree";
					break;
				case "沟通次数":
					status = "start_commu";
				break;
				case "征询次数":
					status = "inqu";
				break;
				case "转办次数":
					status = "deliverto";	
				break;
				case "传阅次数":
					status = "copyto";	
				break;
			}
		}else if("handle_efficiency".equals(params)){//办件效率
			switch (desc) {
				case "办件平均耗时（小时）":
					status = "average";
					break;
				case "审批平均耗时（小时）":
					status = "approval";
				break;
				case "沟通平均耗时（小时）":
					status = "feedback";
				break;
				case "转办平均耗时（小时）":
					status = "deliverto";	
				break;
			}
		}else if ("work_status".equals(params) || "flow_status".equals(params)){
			switch (desc){
				case "草稿":
					status="draft";
					break;
				case "运行中":
					status = "running";
					break;
				case "已退回":
					status = "back";
					break;
				case "失效":
					status = "revoke";
					break;
				case "完结":
					status = "end";
					break;
			}
		}else if ("task_num".equals(params) || "inst_start_num".equals(params)){
			status = "count";
		}
		return status;
	}
	
	
	private String getDateTime(String calcuCycle,String timeStr,boolean isStart){
		String newTimeStr = timeStr;
		switch (calcuCycle) {
		case "year":
			newTimeStr = timeStr + "-01-01 00:00:00";
			if(!isStart){
				newTimeStr = timeStr+"-12-31 23:59:59";
			}
			break;
		case "quarter":
			String[] timeArray = timeStr.split("-");
			String mothDate = getQuarterMothDate(timeArray[1], isStart);
			newTimeStr = timeArray[0]+ "-"+mothDate+" 00:00:00";
			if(isStart){
				newTimeStr = timeArray[0]+ "-" +mothDate+" 23:59:59";
			}
			break;
		case "monthly":
			String[] monthlyArray = timeStr.split("-");
			String monthDayStr = getMonthlyMothDate(monthlyArray[0],monthlyArray[1],isStart);
			
			String laster = isStart?" 00:00:00":" 23:59:59";
			newTimeStr = monthlyArray[0]+ "-" +monthDayStr+laster;
			break;
		}
		return newTimeStr;
	}
	
	private String getQuarterMothDate(String quarter,boolean isStart){
		int quart = Integer.valueOf(quarter);
		int moth = quart*3;
		if(isStart){
			moth -= 2; 
		}
		String mothStr = String.valueOf(moth);
		if(moth<10){
			mothStr =  "0"+moth;
		}
		if(!isStart){
			switch (quart) {
			case 1:
			case 4:
				mothStr = mothStr +"-31";
				break;
			case 2:
			case 3:
				mothStr = mothStr +"-30";
				break;
			default:
				break;
			}
		}else{
			mothStr = mothStr +"-01";
		}
		return mothStr;
	}
	
	private String getMonthlyMothDate(String yearly,String monthly,boolean isStart) {
		int year=Integer.valueOf(yearly);
		int month=Integer.valueOf(monthly);
		if(month<10){
			monthly =  "0"+monthly;
		}
		if(!isStart){
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				monthly = monthly +"-31";
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				monthly = monthly +"-30";
				break;
			case 2:
				if((year%4==0 && year%100!=0) || year%400==0 ){//判断是闰年还是平年
					monthly = monthly +"-29";
				}else {
					monthly = monthly +"-28";
				}
				break;
			default:
				break;
			}
		}else{
			monthly = monthly +"-01";
		}
		return monthly;
		
	}
	
	private String getMothLastDay(int year,int month){
		LocalDate today = LocalDate.of(year, month, 1);
		LocalDate lastDay =today.with(TemporalAdjusters.lastDayOfMonth());
		return String.valueOf(lastDay.getMonthValue());
	}
	
	private String getUnionSql(String flowType,String dimension,String ids,String orgIds,String startTime,String endTime,
			boolean isRunning,boolean isBack,boolean isManualend,boolean isEnd,boolean isInstances,boolean isHourLong,boolean isIncomplete,boolean isAvgLong,boolean isOvertime,boolean isClosingRate,String params,String orgType){
		StringBuilder sql = new StringBuilder();
		String whereIds = "org".equals(dimension)?ids:orgIds;
		String whereSql = " a.IS_DELE_=0 ";
		if("start_throughput".equals(params)){//启动流程吞吐量
			whereSql = getWhereSql(flowType, dimension, whereIds, startTime, endTime, orgType);
		}else if("handle_throughput".equals(params)){//办件吞吐量
			whereSql = getWhereApprovalSql(flowType, dimension, whereIds, startTime, endTime, orgType);
		}else if("handle_efficiency".equals(params)){//办件效率
			whereSql = getWhereApprovalSql(flowType, dimension, whereIds, startTime, endTime, orgType);
		}else if ("work_status".equals(params)){
			whereSql = getWhereWorkStatusSql(flowType,dimension,ids,orgIds,startTime,endTime,orgType);
			return whereSql.toString();
		}else if ("task_num".equals(params)){
			whereSql = getWhereTaskNumSql(flowType,dimension,ids,orgIds,startTime,endTime,orgType);
			return whereSql.toString();
		}else if ("inst_start_num".equals(params)){
			whereSql = getWhereInstNumSql(flowType,dimension,ids,orgIds,startTime,endTime,orgType);
			return whereSql.toString();
		}else if("flow_status".equals(params)){
			whereSql = getWhereFlowStatusSql(flowType,dimension,ids,orgIds,startTime,endTime,orgType);
			return whereSql.toString();
		}
		if(StringUtil.isNotEmpty(orgIds) && "org".equals(dimension)){
			String[] orgIdArray = orgIds.split(",");
			boolean isFirst = false;
			for (String id : orgIdArray) {
				if(!isFirst){
					isFirst = true;
				}else{
					sql.append(" UNION ");
				}
				
				String baseSql = getBaseTypeSql(id, flowType, dimension, isRunning, isBack, isManualend, isEnd, isInstances, isHourLong, isIncomplete, isAvgLong, isOvertime, isClosingRate, whereSql, params, orgType);
				sql.append(baseSql);
			}
		}else if(StringUtil.isNotEmpty(ids) && "flow".equals(dimension)){
			String[] idArray = ids.split(",");
			boolean isFirst = false;
			for (String id : idArray) {
				if(!isFirst){
					isFirst = true;
				}else{
					sql.append(" UNION ");
				}
				String baseSql = getBaseTypeSql(id, flowType, dimension, isRunning, isBack, isManualend, isEnd, isInstances, isHourLong, isIncomplete, isAvgLong, isOvertime, isClosingRate, whereSql, params, orgType);
				sql.append(baseSql);
			}
		}else{
			String baseSql = getBaseTypeSql("", flowType, dimension, isRunning, isBack, isManualend, isEnd, isInstances, isHourLong, isIncomplete, isAvgLong, isOvertime, isClosingRate, whereSql, params, orgType);
			sql.append(baseSql);
		}
		return sql.toString();
	}

	private String getWhereFlowStatusSql(String flowType, String dimension, String ids, String orgIds, String startTime, String endTime, String orgType) {
		String orgWhereId = "2".equals(orgType)?"a.create_by_":"a.create_org_id_";
		String defWhereId = "1".equals(flowType)?"a.proc_def_key_":"a.type_id_";
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("sum(CASE a.STATUS_ WHEN 'running' THEN 1 ELSE 0 END) AS 'running',");
		sql.append("sum(CASE a.STATUS_ WHEN 'draft' THEN 1 ELSE 0 END) AS 'draft',");
		sql.append("sum(CASE a.STATUS_ WHEN 'end' THEN 1 ELSE 0 END) AS 'end',");
		sql.append("sum(CASE a.STATUS_ WHEN 'backToStart' THEN 1 ELSE 0 END)+sum(CASE a.STATUS_ WHEN 'back' THEN 1 ELSE 0 END) AS 'back',");
		sql.append("sum(CASE a.STATUS_ WHEN 'revoke' THEN 1 ELSE 0 END)+sum(CASE a.STATUS_ WHEN 'revokeToStart' THEN 1 ELSE 0 END) AS 'revoke' ");
		sql.append("from bpm_pro_inst a ");
		sql.append("where a.IS_DELE_ = 0 ");
		String dbType = getDbType();
		if(SQLConst.DB_ORACLE.equals(dbType)){
			sql.append(" AND a.CREATE_TIME_ between to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') and to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')");
		}else{
			sql.append(" AND a.CREATE_TIME_>='"+startTime+"' AND a.CREATE_TIME_<='"+endTime+"' ");
		}
		if(StringUtil.isNotEmpty(ids)){
			String[] idArray = ids.split(",");
			String idsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(idArray)));
			sql.append(" AND "+defWhereId+" IN ("+idsSql+")");
		}
		if (StringUtil.isNotEmpty(orgIds)){
			String[] orgIdArray = orgIds.split(",");
			String orgIdsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(orgIdArray)));
			sql.append(" AND "+orgWhereId+" IN ("+ orgIdsSql+")");
		}
		return sql.toString();
	}

	private String getWhereInstNumSql(String flowType, String dimension, String ids, String orgIds, String startTime, String endTime, String orgType) {
		StringBuilder whereSql = new StringBuilder();
		String orgId = "2".equals(orgType)?"user.FULLNAME_":"org.NAME_";
		String defId = "1".equals(flowType)?"def.NAME_":"type.NAME_";
		String orgWhereId = "2".equals(orgType)?"user.ID_":"org.ID_";
		String defWhereId = "1".equals(flowType)?"def.DEF_KEY_":"type.ID_";
		String xProp = "org".equals(dimension)?orgId:defId;
		String xPropId = "org".equals(dimension)?orgWhereId:defWhereId;
		whereSql.append(getBaseInstNumSql(xProp,xPropId));
		whereSql.append("where inst.IS_DELE_ = 0 ");
		String dbType =  getDbType();
		if(SQLConst.DB_ORACLE.equals(dbType)){
			whereSql.append(" AND inst.CREATE_TIME_ between to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') and to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')");
		}else{
			whereSql.append(" AND inst.CREATE_TIME_>='"+startTime+"' AND inst.CREATE_TIME_<='"+endTime+"' ");
		}
		if(StringUtil.isNotEmpty(ids)){
			String[] idArray = ids.split(",");
			String idsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(idArray)));
			whereSql.append(" AND "+defWhereId+" IN ("+idsSql+")");
		}
		if (StringUtil.isNotEmpty(orgIds)){
			String[] orgIdArray = orgIds.split(",");
			String orgIdsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(orgIdArray)));
			whereSql.append(" AND "+orgWhereId+" IN ("+ orgIdsSql+")");
		}
		whereSql.append("group by "+xProp);
		return whereSql.toString();
	}

	private String getBaseInstNumSql(String xProp, String xPropId) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(xPropId+" as id,");
		sql.append(xProp+" as name,");
		sql.append("COUNT( 1 ) as count ");
		sql.append("FROM bpm_pro_inst inst ");
		sql.append("left join uc_user user on user.id_ = inst.CREATE_BY_ ");
		sql.append("left join portal_sys_type type on type.id_ = inst.TYPE_ID_ ");
		sql.append("left join bpm_definition def on def.DEF_KEY_ = inst.PROC_DEF_KEY_ ");
		sql.append("left join uc_org org on org.ID_ = inst.CREATE_ORG_ID_ ");
		return sql.toString();
	}

	private String getWhereTaskNumSql(String flowType,String dimension,String ids,String orgIds,String startTime,String endTime,String orgType){
		StringBuilder whereSql = new StringBuilder();
		String orgId = "2".equals(orgType)?"user.FULLNAME_":"org.NAME_";
		String defId = "1".equals(flowType)?"def.NAME_":"type.NAME_";
		String orgWhereId = "2".equals(orgType)?"user.ID_":"org.ID_";
		String defWhereId = "1".equals(flowType)?"def.DEF_KEY_":"type.ID_";
		String xProp = "org".equals(dimension)?orgId:defId;
		String xPropId = "org".equals(dimension)?orgWhereId:defWhereId;
		whereSql.append(getBaseTaskNumSql(xProp,xPropId));
		whereSql.append("where task.IS_DELE_ = 0 ");
		String dbType =  getDbType();
		if(SQLConst.DB_ORACLE.equals(dbType)){
			whereSql.append(" AND task.CREATE_TIME_ between to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') and to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')");
		}else{
			whereSql.append(" AND task.CREATE_TIME_>='"+startTime+"' AND task.CREATE_TIME_<='"+endTime+"' ");
		}
		if(StringUtil.isNotEmpty(ids)){
			String[] idArray = ids.split(",");
			String idsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(idArray)));
			whereSql.append(" AND "+defWhereId+" IN ("+idsSql+")");
		}
		if (StringUtil.isNotEmpty(orgIds)){
			String[] orgIdArray = orgIds.split(",");
			String orgIdsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(orgIdArray)));
			whereSql.append(" AND "+orgWhereId+" IN ("+ orgIdsSql+")");
		}
		whereSql.append("group by "+xProp);
		return whereSql.toString();
	}

	private String getBaseTaskNumSql(String xProp,String xPropId){
		StringBuilder sql = new StringBuilder("select ");
		sql.append(xPropId+" as id,");
		sql.append(xProp+" as name,");
		sql.append("COUNT( 1 ) as count ");
		sql.append("FROM bpm_task task ");
		sql.append("left join uc_user user on user.id_ = task.OWNER_ID_ ");
		sql.append("left join portal_sys_type type on type.id_ = task.TYPE_ID_ ");
		sql.append("left join bpm_definition def on def.DEF_KEY_ = task.PROC_DEF_KEY_ ");
		sql.append("left join bpm_pro_inst inst on inst.ID_ = task.PROC_INST_ID_ ");
		sql.append("left join uc_org org on org.ID_ = inst.CREATE_ORG_ID_ ");
		return sql.toString();
	}

	private String getWhereWorkStatusSql(String flowType,String dimension,String ids,String orgIds,String startTime,String endTime,String orgType){
		StringBuilder whereSql = new StringBuilder();
		String orgId = "2".equals(orgType)?"user.FULLNAME_":"org.NAME_";
		String defId = "1".equals(flowType)?"def.NAME_":"type.NAME_";
		String orgWhereId = "2".equals(orgType)?"user.ID_":"org.ID_";
		String defWhereId = "1".equals(flowType)?"def.DEF_KEY_":"type.ID_";
		String xProp = "org".equals(dimension)?orgId:defId;
		String xPropId = "org".equals(dimension)?orgWhereId:defWhereId;
		whereSql.append(getBaseWorkStatusSql(xProp,xPropId));
		whereSql.append("where a.IS_DELE_ = 0 ");
		String dbType =  getDbType();
		if(SQLConst.DB_ORACLE.equals(dbType)){
			whereSql.append(" AND a.STATUS_ IS NOT NULL AND a.CREATE_TIME_ between to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') and to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')");
		}else{
			whereSql.append(" AND a.STATUS_ IS NOT NULL AND a.CREATE_TIME_>='"+startTime+"' AND a.CREATE_TIME_<='"+endTime+"' ");
		}
		if(StringUtil.isNotEmpty(ids)){
			String[] idArray = ids.split(",");
			String idsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(idArray)));
			whereSql.append(" AND "+defWhereId+" IN ("+idsSql+")");
		}
		if (StringUtil.isNotEmpty(orgIds)){
			String[] orgIdArray = orgIds.split(",");
			String orgIdsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(orgIdArray)));
			whereSql.append(" AND "+orgWhereId+" IN ("+ orgIdsSql+")");
		}
		whereSql.append("group by "+xProp);
		return whereSql.toString();
	}

	private String getBaseWorkStatusSql(String xProp,String xPropId){
		StringBuilder sql = new StringBuilder("select ");
		sql.append(xPropId+" as id,");
		sql.append(xProp+" as name,");
		sql.append("sum(CASE a.STATUS_ WHEN 'running' THEN 1 ELSE 0 END) AS 'running',");
		sql.append("sum(CASE a.STATUS_ WHEN 'draft' THEN 1 ELSE 0 END) AS 'draft',");
		sql.append("sum(CASE a.STATUS_ WHEN 'end' THEN 1 ELSE 0 END) AS 'end',");
		sql.append("sum(CASE a.STATUS_ WHEN 'backToStart' THEN 1 ELSE 0 END)+sum(CASE a.STATUS_ WHEN 'back' THEN 1 ELSE 0 END) AS 'back',");
		sql.append("sum(CASE a.STATUS_ WHEN 'revoke' THEN 1 ELSE 0 END)+sum(CASE a.STATUS_ WHEN 'revokeToStart' THEN 1 ELSE 0 END) AS 'revoke' ");
		sql.append("from bpm_pro_inst a ");
		sql.append("left join portal_sys_type type on type.id_ = a.type_id_ ");
		sql.append("left join bpm_definition def on def.def_key_ = a.proc_def_key_ ");
		sql.append("left join uc_user user on user.id_ = a.create_by_ ");
		sql.append("left join uc_org org on org.id_ = a.create_org_id_ ");
		return sql.toString();
	}
	
	private String getBaseTypeSql(String id,String flowType,String dimension,boolean isRunning,boolean isBack,boolean isManualend,boolean isEnd
			,boolean isInstances,boolean isHourLong,boolean isIncomplete,boolean isAvgLong,boolean isOvertime,boolean isClosingRate,String whereSql,String params,String orgType){
		if("start_throughput".equals(params)){//启动流程吞吐量
			return getBaseSql(id, flowType, dimension, isRunning, isBack, isManualend, isEnd, isInstances, isHourLong, isIncomplete, isAvgLong, isOvertime, isClosingRate, whereSql,orgType);
		}else if("handle_throughput".equals(params)){//办件吞吐量
			return getBaseApprovalSql(id, flowType, dimension, whereSql,orgType);
		}else if("handle_efficiency".equals(params)){//办件效率
			return getBaseEfficiencySql(id, flowType, dimension, whereSql, orgType);
		}
		return null;
	}

	private String getEfficiencySelectSql(String flowType,String orgType,String dimension,String table,String status){
		String idCode = "2".equals(orgType)?"AUDITOR_":"ORG_ID_";
		String selectSql = "select count(1) as count,sum(opinion.DUR_MS_) as durMs,opinion."+idCode+" as id,'"+status+"' as status FROM "+table+" opinion LEFT JOIN bpm_pro_inst inst on opinion.PROC_INST_ID_=inst.ID_ where opinion."+idCode+" is not null ";
		if("flow".equals(dimension)){
			if("1".equals(flowType)){
				selectSql = "select count(1) as count,sum(opinion.DUR_MS_) as durMs,inst.PROC_DEF_KEY_ as id,'"+status+"' as status FROM "+table+" opinion LEFT JOIN bpm_pro_inst inst on opinion.PROC_INST_ID_=inst.ID_ where 1=1 ";
			}else{
				selectSql = "select count(1) as count,sum(opinion.DUR_MS_) as durMs,inst.TYPE_ID_ as id,'"+status+"' as status FROM "+table+" opinion LEFT JOIN bpm_pro_inst inst on opinion.PROC_INST_ID_=inst.ID_ where 1=1 ";
			}
		}
		return selectSql;
	}
	
	private String getOrgSql(String flowType,String orgType,String orgId,String dimension){
		String idCode = "2".equals(orgType)?"AUDITOR_":"ORG_ID_";
		String orgSql = StringUtil.isNotEmpty(orgId)?" AND opinion."+idCode+" ='"+orgId+"' ":" ";
		if("flow".equals(dimension)){
			if("1".equals(flowType)){
				orgSql = StringUtil.isNotEmpty(orgId)?" AND inst.PROC_DEF_KEY_ ='"+orgId+"' ":" ";
			}else{
				orgSql = StringUtil.isNotEmpty(orgId)?" AND inst.TYPE_ID_ ='"+orgId+"' ":" ";
			}
		}
		return orgSql;
	}
	
	
	private String getBaseEfficiencySql(String orgId,String flowType,String dimension,String whereSql,String orgType){
		StringBuilder baseSql = new StringBuilder();
		String orgSql = getOrgSql(flowType, orgType, orgId, dimension);
		String havingSql = " having count(1) >0 ";
		String dbType =  getDbType();
		if(SQLConst.DB_ORACLE.equals(dbType)){
			String idCode = "2".equals(orgType)?"AUDITOR_":"ORG_ID_";
			String groupBySql = " GROUP BY opinion."+idCode;
			if("flow".equals(dimension)){
				if("1".equals(flowType)){
					groupBySql = " GROUP BY inst.PROC_DEF_KEY_ ";
				}else{
					groupBySql = " GROUP BY inst.TYPE_ID_ ";
				}
			}
			havingSql += groupBySql;
		}
		//办件平均耗时
		StringBuilder allSql = new StringBuilder();
		allSql.append(getEfficiencySelectSql(flowType, orgType, dimension, "bpm_check_opinion","average"));
		allSql.append(" and opinion.STATUS_ not in('start','skip','end','awaiting_check','awaiting_feedback','START_COMMU') AND opinion.DUR_MS_ is not null ");
		allSql.append(orgSql);
		allSql.append(whereSql);
		allSql.append(havingSql);
		allSql.append(" UNION ");
		allSql.append(getEfficiencySelectSql(flowType, orgType, dimension, "bpm_check_opinion_hi","average"));
		allSql.append(" and opinion.STATUS_ not in('start','skip','end','awaiting_check','awaiting_feedback','START_COMMU') AND opinion.DUR_MS_ is not null ");
		allSql.append(orgSql);
		allSql.append(whereSql);
		allSql.append(havingSql);
		baseSql.append(" select sum(a.count) as count,sum(a.durMs) as durMs,a.id as id,'average' as status from ( ");
		baseSql.append(allSql.toString());
		baseSql.append(" ) a ");
		if(SQLConst.DB_ORACLE.equals(dbType)){
			baseSql.append(" GROUP BY a.id ");
		}
		baseSql.append(" UNION ");
		//审批平均耗时
		StringBuilder approvalSql = new StringBuilder();
		approvalSql.append(getEfficiencySelectSql(flowType, orgType, dimension, "bpm_check_opinion","approval"));
		approvalSql.append(" and opinion.STATUS_ not in('start','skip','end','awaiting_check','awaiting_feedback','START_COMMU','feedback','deliverto','delivertoAgree','delivertoOppose') AND opinion.DUR_MS_ is not null ");
		approvalSql.append(orgSql);
		approvalSql.append(whereSql);
		approvalSql.append(havingSql);
		approvalSql.append(" UNION ");
		approvalSql.append(getEfficiencySelectSql(flowType, orgType, dimension, "bpm_check_opinion_hi","approval"));
		approvalSql.append(" and opinion.STATUS_ not in('start','skip','end','awaiting_check','awaiting_feedback','START_COMMU','feedback','deliverto','delivertoAgree','delivertoOppose') AND opinion.DUR_MS_ is not null ");
		approvalSql.append(orgSql);
		approvalSql.append(whereSql);
		approvalSql.append(havingSql);
		baseSql.append(" select sum(a.count) as count,sum(a.durMs) as durMs,a.id as id,'approval' as status from ( ");
		baseSql.append(approvalSql.toString());
		baseSql.append(" ) a ");
		if(SQLConst.DB_ORACLE.equals(dbType)){
			baseSql.append(" GROUP BY a.id ");
		}
		//沟通效率
		baseSql.append(" UNION ");
		StringBuilder otherSql = new StringBuilder();
		otherSql.append(getEfficiencySelectSql(flowType, orgType, dimension, "bpm_check_opinion","feedback"));
		otherSql.append(" and opinion.STATUS_ = 'feedback' AND opinion.DUR_MS_ is not null ");
		otherSql.append(orgSql);
		otherSql.append(whereSql);
		otherSql.append(havingSql);
		otherSql.append(" UNION ");
		otherSql.append(getEfficiencySelectSql(flowType, orgType, dimension, "bpm_check_opinion_hi","feedback"));
		otherSql.append(" and opinion.STATUS_ = 'feedback' AND opinion.DUR_MS_ is not null ");
		otherSql.append(orgSql);
		otherSql.append(whereSql);
		otherSql.append(havingSql);
		baseSql.append(" select sum(a.count) as count,sum(a.durMs) as durMs,a.id as id,'feedback' as status from ( ");
		baseSql.append(otherSql.toString());
		baseSql.append(" ) a ");
		if(SQLConst.DB_ORACLE.equals(dbType)){
			baseSql.append(" GROUP BY a.id ");
		}
			
		//转办效率
		baseSql.append(" UNION ");
		StringBuilder deliversql = new StringBuilder();
		deliversql.append(getEfficiencySelectSql(flowType, orgType, dimension, "bpm_check_opinion","deliverto"));
		deliversql.append(" and opinion.STATUS_ in('delivertoAgree','delivertoOppose') AND opinion.DUR_MS_ is not null ");
		deliversql.append(orgSql);
		deliversql.append(whereSql);
		deliversql.append(havingSql);
		deliversql.append(" UNION ");
		deliversql.append(getEfficiencySelectSql(flowType, orgType, dimension, "bpm_check_opinion_hi","deliverto"));
		deliversql.append(" and opinion.STATUS_ in('delivertoAgree','delivertoOppose') AND opinion.DUR_MS_ is not null ");
		deliversql.append(orgSql);
		deliversql.append(whereSql);
		deliversql.append(havingSql);
		baseSql.append(" select sum(a.count) as count,sum(a.durMs) as durMs,a.id as id,'deliverto' as status from ( ");
		baseSql.append(deliversql.toString());
		baseSql.append(" ) a ");
		if(SQLConst.DB_ORACLE.equals(dbType)){
			baseSql.append(" GROUP BY a.id ");
		}	
		
		return baseSql.toString();
	}
	
	
	private String getApprovalSelectSql(String flowType,String orgType,String dimension,String table,String status){
		String idCode = "2".equals(orgType)?"AUDITOR_":"ORG_ID_";
		String selectSql = "select count(1) as count,opinion."+idCode+" as id,'"+status+"' as status FROM "+table+" opinion LEFT JOIN bpm_pro_inst inst on opinion.PROC_INST_ID_=inst.ID_ where opinion."+idCode+" is not null ";
		if("flow".equals(dimension)){
			if("1".equals(flowType)){
				selectSql = "select count(1) as count,inst.PROC_DEF_KEY_ as id,'"+status+"' as status FROM "+table+" opinion LEFT JOIN bpm_pro_inst inst on opinion.PROC_INST_ID_=inst.ID_ where 1=1 ";
			}else{
				selectSql = "select count(1) as count,inst.TYPE_ID_ as id,'"+status+"' as status FROM "+table+" opinion LEFT JOIN bpm_pro_inst inst on opinion.PROC_INST_ID_=inst.ID_ where 1=1 ";
			}
		}
		return selectSql;
	}
	private String getBaseApprovalSql(String orgId,String flowType,String dimension,String whereSql,String orgType){
		StringBuilder baseSql = new StringBuilder();
		String orgSql = getOrgSql(flowType, orgType, orgId, dimension);
		String havingSql = " having count(1) >0 ";
		String dbType =  getDbType();
		if(SQLConst.DB_ORACLE.equals(dbType)){
			String idCode = "2".equals(orgType)?"AUDITOR_":"ORG_ID_";
			String groupBySql = " GROUP BY opinion."+idCode;
			if("flow".equals(dimension)){
				if("1".equals(flowType)){
					groupBySql = " GROUP BY inst.PROC_DEF_KEY_  ";
				}else{
					groupBySql = " GROUP BY inst.TYPE_ID_ ";
				}
			}
			havingSql += groupBySql;
		}
		//审批
		StringBuilder agreeSql = new StringBuilder();
		agreeSql.append(getApprovalSelectSql(flowType, orgType, dimension, "bpm_check_opinion","agree"));
		agreeSql.append(" and opinion.STATUS_ in('transAgree','oppose','transOppose','back','backToStart','revoker','revoker_to_start','agree') ");
		agreeSql.append(orgSql);
		agreeSql.append(whereSql);
		agreeSql.append(havingSql);
		agreeSql.append(" UNION ");
		agreeSql.append(getApprovalSelectSql(flowType, orgType, dimension, "bpm_check_opinion_hi","agree"));
		agreeSql.append(" and opinion.STATUS_ in('transAgree','oppose','transOppose','back','backToStart','revoker','revoker_to_start','agree') ");
		agreeSql.append(orgSql);
		agreeSql.append(whereSql);
		agreeSql.append(havingSql);
		baseSql.append(" select SUM(a.count) as count,a.id as id,'agree' as status from ( ");
		baseSql.append(agreeSql.toString());
		baseSql.append(" ) a ");
		if(SQLConst.DB_ORACLE.equals(dbType)){
			baseSql.append(" GROUP BY a.id ");
		}
		//沟通、转办、征询、抄送
		String[] status = new String[]{"start_commu","deliverto","inqu","copyto"};
		for (String statu : status) {
			baseSql.append(" UNION ");
			StringBuilder otherSql = new StringBuilder();
			otherSql.append(getApprovalSelectSql(flowType, orgType, dimension, "bpm_check_opinion_hi",statu));
			otherSql.append(" and opinion.STATUS_ ='"+statu+"' ");
			otherSql.append(orgSql);
			otherSql.append(whereSql);
			otherSql.append(havingSql);
			otherSql.append(" UNION ");
			otherSql.append(getApprovalSelectSql(flowType, orgType, dimension, "bpm_check_opinion",statu));
			otherSql.append(" and opinion.STATUS_ ='"+statu+"' ");
			otherSql.append(orgSql);
			otherSql.append(whereSql);
			otherSql.append(havingSql);
			baseSql.append(" select SUM(a.count) as count,a.id as id,'"+statu+"' as status from ( ");
			baseSql.append(otherSql.toString());
			baseSql.append(" ) a ");
			if(SQLConst.DB_ORACLE.equals(dbType)){
				baseSql.append(" GROUP BY a.id ");
			}
		}
		return baseSql.toString();
	}
	
	private String getSelectSql(String orgType,String flowType,String dimension,String status){
		String idCode = "2".equals(orgType)?"CREATE_BY_":"CREATE_ORG_ID_";
		String selectSql = "select count(1) as count,a."+idCode+" as id,'"+status+"' as status ";
		if("flow".equals(dimension)){
			if("1".equals(flowType)){
				selectSql = "select count(1) as count,a.PROC_DEF_KEY_ as id,'"+status+"' as status ";
			}else{
				selectSql = "select count(1) as count,a.TYPE_ID_ as id,'"+status+"' as status ";
			}
		}
		return selectSql;
	}
	
	private String getHourLongSql_mysql(String orgType,String flowType,String dimension,String status,boolean isRate){
		String idCode = "2".equals(orgType)?"CREATE_BY_":"CREATE_ORG_ID_";
		String rateSql = isRate?"/count(a.ID_),1)) ":")";
		String roundSql = isRate?"ROUND(":"";
		String selectSql = "select ("+roundSql+"SUM(ROUND(TIMESTAMPDIFF(MINUTE,a.CREATE_TIME_,if(a.END_TIME_ IS not NULL,a.END_TIME_,now()))/60,1))"+rateSql+" as count,a."+idCode+" as id,'"+status+"' as status ";
		if("flow".equals(dimension)){
			if("1".equals(flowType)){
				selectSql = "select ("+roundSql+"SUM(ROUND(TIMESTAMPDIFF(MINUTE,a.CREATE_TIME_,if(a.END_TIME_ IS not NULL,a.END_TIME_,now()))/60,1))"+rateSql+" as count,a.PROC_DEF_KEY_ as id,'"+status+"' as status ";
			}else{
				selectSql = "select ("+roundSql+"SUM(ROUND(TIMESTAMPDIFF(MINUTE,a.CREATE_TIME_,if(a.END_TIME_ IS not NULL,a.END_TIME_,now()))/60,1))"+rateSql+" as count,a.TYPE_ID_ as id,'"+status+"' as status ";
			}
		}
		return selectSql;
	}
	
	private String getClosingRate_mysql(String orgType,String flowType,String dimension,String status,String countSql){
		String idCode = "2".equals(orgType)?"CREATE_BY_":"CREATE_ORG_ID_";
		String selectSql = "select ROUND(("+countSql+")/count(1)*100) as count,a."+idCode+" as id,'"+status+"' as status ";
		if("flow".equals(dimension)){
			if("1".equals(flowType)){
				selectSql = "select ROUND(("+countSql+")/count(1)*100) as count,a.PROC_DEF_KEY_ as id,'"+status+"' as status ";
			}else{
				selectSql = "select ROUND(("+countSql+")/count(1)*100) as count,a.TYPE_ID_ as id,'"+status+"' as status ";
			}
		}
		return selectSql;
	}
	
	private String getClosingRate_oracle(String orgType,String flowType,String dimension,String status,String countSql){
		String idCode = "2".equals(orgType)?"CREATE_BY_":"CREATE_ORG_ID_";
		String selectSql = "select ROUND((select ("+countSql+") from dual)/count(1)*100) as count,a."+idCode+" as id,'"+status+"' as status ";
		if("flow".equals(dimension)){
			if("1".equals(flowType)){
				selectSql = "select ROUND((select ("+countSql+") from dual)/count(1)*100) as count,a.PROC_DEF_KEY_ as id,'"+status+"' as status ";
			}else{
				selectSql = "select ROUND((select ("+countSql+") from dual)/count(1)*100) as count,a.TYPE_ID_ as id,'"+status+"' as status ";
			}
		}
		return selectSql;
	}
	
	private String getHourLongSql_oracle(String orgType,String flowType,String dimension,String status,boolean isRate){
		String idCode = "2".equals(orgType)?"CREATE_BY_":"CREATE_ORG_ID_";
		String rateSql = isRate?"/count(a.ID_)) ":")";
		String selectSql = "select round((SUM(TO_NUMBER((DECODE(END_TIME_,NULL,sysdate,END_TIME_)) -(CREATE_TIME_+0))*24))"+rateSql+" as count,a."+idCode+" as id,'"+status+"' as status ";
		if("flow".equals(dimension)){
			if("1".equals(flowType)){
				selectSql = "select round((SUM(TO_NUMBER((DECODE(END_TIME_,NULL,sysdate,END_TIME_)) -(CREATE_TIME_+0))*24))"+rateSql+" as count,a.PROC_DEF_KEY_ as id,'"+status+"' as status ";
			}else{
				selectSql = "select round((SUM(TO_NUMBER((DECODE(END_TIME_,NULL,sysdate,END_TIME_)) -(CREATE_TIME_+0))*24))"+rateSql+" as count,a.TYPE_ID_ as id,'"+status+"' as status ";
			}
		}
		return selectSql;
	}
	
	private String getBaseSql(String orgId,String flowType,String dimension,boolean isRunning,boolean isBack,boolean isManualend,boolean isEnd,boolean isInstances,boolean isHourLong,boolean isIncomplete,boolean isAvgLong,boolean isOvertime,boolean isClosingRate,String whereSql,String orgType){
		StringBuilder baseSql = new StringBuilder();
		String idCode = "2".equals(orgType)?"CREATE_BY_":"CREATE_ORG_ID_";
		boolean isAll = !isRunning && !isBack && !isManualend && !isEnd && !isInstances && !isInstances && !isIncomplete;
		String orgSql = StringUtil.isNotEmpty(orgId)?" AND a."+idCode+" ='"+orgId+"' ":" ";
		String havingSql = " having count(1) >0 ";
		String groupBySql = " GROUP BY a."+idCode;
		if("flow".equals(dimension)){
			if("1".equals(flowType)){
				orgSql = StringUtil.isNotEmpty(orgId)?" AND a.PROC_DEF_KEY_ ='"+orgId+"' ":" ";
				groupBySql = " GROUP BY a.PROC_DEF_KEY_  ";
			}else{
				orgSql = StringUtil.isNotEmpty(orgId)?" AND a.TYPE_ID_ ='"+orgId+"' ":" ";
				groupBySql = " GROUP BY a.TYPE_ID_ ";
			}
		}
		String dbType =  getDbType();
		if(SQLConst.DB_ORACLE.equals(dbType)){
			havingSql += groupBySql;
		}
		boolean isFirst = false;
		if(isAll || isRunning){
			baseSql.append(getSelectSql(orgType, flowType, dimension, "running"));
			baseSql.append(" from bpm_pro_inst a WHERE a.STATUS_ in('running','back','backToStart','revoke','revokeToStart') ");
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
			isFirst = true;
		}
		if(isAll || isBack){
			if(isFirst){
				baseSql.append(" UNION ");
			}else{
				isFirst = true;
			}
			baseSql.append(getSelectSql(orgType, flowType, dimension, "backRevoke"));
			baseSql.append(" from bpm_pro_inst a WHERE a.STATUS_ in('back','backToStart','revoke','revokeToStart') ");
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
		}
		if(isAll || isManualend){
			if(isFirst){
				baseSql.append(" UNION ");
			}else{
				isFirst = true;
			}
			baseSql.append(getSelectSql(orgType, flowType, dimension, "manualend"));
			baseSql.append(" from bpm_pro_inst a WHERE a.STATUS_ in('manualend') ");
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
		}
		if(isAll || isEnd){
			if(isFirst){
				baseSql.append(" UNION ");
			}else{
				isFirst = true;
			}
			baseSql.append(getSelectSql(orgType, flowType, dimension, "end"));
			baseSql.append(" from bpm_pro_inst a WHERE a.STATUS_ ='end' ");
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
		}
		if(isAll || isInstances){
			if(isFirst){
				baseSql.append(" UNION ");
			}else{
				isFirst = true;
			}
			baseSql.append(getSelectSql(orgType, flowType, dimension, "instances"));
			baseSql.append(" from bpm_pro_inst a WHERE 1=1 ");
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
		}
		if(isAll || isHourLong){
			if(isFirst){
				baseSql.append(" UNION ");
			}else{
				isFirst = true;
			}
			if(SQLConst.DB_ORACLE.equals(dbType)){
				baseSql.append(getHourLongSql_oracle(orgType, flowType, dimension, "hourLong",false));
			}else{
				baseSql.append(getHourLongSql_mysql(orgType, flowType, dimension, "hourLong",false));
			}
			baseSql.append(" from bpm_pro_inst a WHERE 1=1 ");
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
		}
		if(isAll || isIncomplete){
			if(isFirst){
				baseSql.append(" UNION ");
			}else{
				isFirst = true;
			}
			baseSql.append(getSelectSql(orgType, flowType, dimension, "incomplete"));
			baseSql.append(" from bpm_pro_inst a WHERE a.STATUS_ not in ('end','manualend') ");
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
		}
		if(isAll || isAvgLong){
			if(isFirst){
				baseSql.append(" UNION ");
			}else{
				isFirst = true;
			}
			//oracle数据库暂未完善
			if(SQLConst.DB_ORACLE.equals(dbType)){
				baseSql.append(getHourLongSql_oracle(orgType, flowType, dimension, "avgLong",true));
			}else{
				baseSql.append(getHourLongSql_mysql(orgType, flowType, dimension, "avgLong",true));
			}
			baseSql.append(" from bpm_pro_inst a WHERE 1=1 ");
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
		}
		if(isAll || isOvertime){
			if(isFirst){
				baseSql.append(" UNION ");
			}else{
				isFirst = true;
			}
			baseSql.append(getSelectSql(orgType, flowType, dimension, "overtime"));
			//oracle数据库暂未处理
			if(SQLConst.DB_ORACLE.equals(dbType)){
				baseSql.append(" from bpm_pro_inst a WHERE ROUND(((TO_DATE(to_char((CASE a.END_TIME_ WHEN a.END_TIME_ THEN a.END_TIME_ ELSE (select sysdate from dual) END), 'YYYY-MM-DD HH24-MI-SS'), 'YYYY-MM-DD HH24-MI-SS') - TO_DATE(to_char(a.CREATE_TIME_ , 'YYYY-MM-DD HH24-MI-SS'), 'YYYY-MM-DD HH24-MI-SS')) * 24*60  ),1)>24 ");
			}else{
				baseSql.append(" from bpm_pro_inst a WHERE TIMESTAMPDIFF(HOUR,a.CREATE_TIME_,IF(a.END_TIME_ IS NOT NULL,a.END_TIME_,now()))>24 ");
			}
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
		}
		if(isAll || isClosingRate){
			if(isFirst){
				baseSql.append(" UNION ");
			}else{
				isFirst = true;
			}
			StringBuilder countSql = new StringBuilder();
			countSql.append("select count(1) from bpm_pro_inst a where a.STATUS_ in ('end','manualend') ");
			countSql.append(orgSql);
			countSql.append(whereSql);
			countSql.append(havingSql);
			//暂未对oracle数据库做处理
			if(SQLConst.DB_ORACLE.equals(dbType)){
				baseSql.append(getClosingRate_oracle(orgType, flowType, dimension, "closingRate",countSql.toString()));
			}else{
				baseSql.append(getClosingRate_mysql(orgType, flowType, dimension, "closingRate",countSql.toString()));
			}
			baseSql.append(" from bpm_pro_inst a WHERE 1=1 ");
			baseSql.append(orgSql);
			baseSql.append(whereSql);
			baseSql.append(havingSql);
		}
		return baseSql.toString();
	}
	
	private String getDbType() {
		return databaseContext.getDbTypeByAlias(DataSourceConsts.LOCAL_DATASOURCE);
	}
	
	private String getWhereSql(String flowType,String dimension,String ids,String startTime,String endTime,String orgType){
		StringBuilder whereSql = new StringBuilder();
		String idCode = "2".equals(orgType)?"CREATE_BY_":"CREATE_ORG_ID_";
		String dbType =  getDbType();
		if(SQLConst.DB_ORACLE.equals(dbType)){
			whereSql.append(" AND a.STATUS_ IS NOT NULL AND a.CREATE_TIME_ between to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') and to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')");
		}else{
			whereSql.append(" AND a.STATUS_ IS NOT NULL AND a.CREATE_TIME_>='"+startTime+"' AND a.CREATE_TIME_<='"+endTime+"' ");
		}
		if(StringUtil.isNotEmpty(ids)){
			String[] idArray = ids.split(",");
			String idsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(idArray)));
			if("org".equals(dimension)){
				if("1".equals(flowType)){
					whereSql.append(" AND a.PROC_DEF_KEY_ IN ("+ idsSql+") ");
				}else if("2".equals(flowType)){
					whereSql.append(" AND a.TYPE_ID_ IN ("+ idsSql+") ");
				}
			}else {
				whereSql.append(" AND a."+idCode+" IN ("+ idsSql+") ");
			}
		}
		return whereSql.toString();
	}
	
	private String getWhereApprovalSql(String flowType,String dimension,String ids,String startTime,String endTime,String orgType){
		StringBuilder whereSql = new StringBuilder();
		String idCode = "2".equals(orgType)?"CREATE_BY_":"CREATE_ORG_ID_";
		String dbType =  getDbType();
		if(SQLConst.DB_ORACLE.equals(dbType)){
			whereSql.append(" AND opinion.STATUS_ IS NOT NULL AND opinion.COMPLETE_TIME_ between to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') and to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')");
		}else{
			whereSql.append(" AND opinion.STATUS_ IS NOT NULL AND opinion.COMPLETE_TIME_>='"+startTime+"' AND opinion.COMPLETE_TIME_<='"+endTime+"' ");
		}
		if(StringUtil.isNotEmpty(ids)){
			String[] idArray = ids.split(",");
			String idsSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(idArray)));
			if("org".equals(dimension)){
				if("1".equals(flowType)){
					whereSql.append(" AND inst.PROC_DEF_KEY_ IN ("+ idsSql+") ");
				}else if("2".equals(flowType)){
					whereSql.append(" AND inst.TYPE_ID_ IN ("+ idsSql+") ");
				}
			}else {
				if(!"2".equals(orgType)) {
					idCode = "ORG_ID_";
				}else {
					idCode = "AUDITOR_";
				}
				whereSql.append(" AND opinion."+idCode+" IN ("+ idsSql+") ");
			}
		}
		return whereSql.toString();
	}
	
	private List<Map<String, Object>> querySql(String sql){
		return commonManager.query(sql);
	}

	@Override
	public PageList<FlowOrgCountVo> flowOrgCountList(QueryFilter queryFilter) {
		List<Map<String, Object>> selectList = this.getFlowOrgSelectList(queryFilter);
		return parseToOrgPageList(selectList);
	}
	
	public List<Map<String, Object>> getFlowOrgSelectList(QueryFilter queryFilter){
		String dbType =  getDbType();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.PROC_DEF_KEY_ as procDefKey,a.PROC_DEF_NAME_ as procDefName,COUNT(1) AS instances,");
		if(SQLConst.DB_ORACLE.equals(dbType)){
			sql.append("ROUND(SUM((TO_DATE (TO_CHAR ((CASE a.END_TIME_ WHEN a.END_TIME_ THEN a.END_TIME_ ELSE (SELECT SYSDATE FROM dual) END ), 'YYYY-MM-DD HH24-MI-SS' ),'YYYY-MM-DD HH24-MI-SS') - TO_DATE (TO_CHAR (a.CREATE_TIME_,'YYYY-MM-DD HH24-MI-SS'),'YYYY-MM-DD HH24-MI-SS')) * 24),1) AS hourLong,");
		}else{
			sql.append("SUM(ROUND(TIMESTAMPDIFF(MINUTE,a.CREATE_TIME_,IF (a.END_TIME_ IS NOT NULL,a.END_TIME_,now()))/60,1)) AS hourLong,");
		}
		sql.append("(SELECT COUNT(1) FROM bpm_pro_inst b WHERE b.STATUS_ NOT IN('end','manualend') and b.PROC_DEF_KEY_=a.PROC_DEF_KEY_"+getFlowOrgWhereSql(queryFilter, "b")+") as incomplete");
		sql.append(" FROM bpm_pro_inst a WHERE 1=1 ");
		sql.append(getFlowOrgWhereSql(queryFilter, "a"));
		sql.append(" GROUP BY a.PROC_DEF_KEY_,a.PROC_DEF_NAME_");
		return querySql(sql.toString());
	}
	
	private String getFlowOrgWhereSql(QueryFilter queryFilter,String pre){
		StringBuilder whereSql = new StringBuilder();
		List<QueryField> querys = queryFilter.getQuerys();
		if(BeanUtils.isNotEmpty(queryFilter) && BeanUtils.isNotEmpty(querys)){
			for (QueryField queryField : querys) {
				if("createTime".equals(queryField.getProperty()) && BeanUtils.isNotEmpty(queryField.getValue())){
					List<String> times = (List<String>) queryField.getValue();
					whereSql.append(" AND "+pre+".CREATE_TIME_ >='"+times.get(0)+"' ");
					whereSql.append(" AND "+pre+".CREATE_TIME_ <='"+times.get(1)+"' ");
				}else if("createOrgId".equals(queryField.getProperty()) && BeanUtils.isNotEmpty(queryField.getValue())){
					Map<String,String> map = new HashMap<String, String>();
					String[] orgIds = queryField.getValue().toString().split(",");
					for (String orgId : orgIds) {
						map.put(orgId, orgId);
					}
					Set<String> allOrgIds = new HashSet<String>();
					//获取子部门
					Map<String, Set<String>> mapSet = ucService.getChildrenIds(map);
					for (String key : mapSet.keySet()) { 
						allOrgIds.addAll(mapSet.get(key));
					}
					String orgSql = StringUtil.convertListToSingleQuotesString(allOrgIds);
					whereSql.append(" AND "+pre+".CREATE_ORG_ID_ in("+orgSql+")");
				}
			}
			return whereSql.toString();
		}
		return "";
	}
	
	/**
	 * 转orgPageList
	 * @param selectList
	 * @return
	 */
	private PageList<FlowOrgCountVo> parseToOrgPageList(List<Map<String, Object>> selectList){
		PageList<FlowOrgCountVo> pageList = new PageList<>();
		pageList.setPage(1);
		pageList.setPageSize(1);
		pageList.setTotal(0);
		pageList.setRows(new ArrayList<FlowOrgCountVo>());
		try {
			if(BeanUtils.isNotEmpty(selectList)){
				List<FlowOrgCountVo> rows = new ArrayList<FlowOrgCountVo>();
				if(selectList.get(0).containsKey("PROCDEFKEY")){
					for (Map<String, Object> map : selectList) {
						FlowOrgCountVo vo = new FlowOrgCountVo();
						vo.setProcDefKey(map.get("PROCDEFKEY").toString());
						vo.setProcDefName(map.get("PROCDEFNAME").toString());
						vo.setHourLong(BeanUtils.isNotEmpty(map.get("HOURLONG"))?Float.valueOf(map.get("HOURLONG").toString()):0);
						vo.setIncomplete(BeanUtils.isNotEmpty(map.get("INCOMPLETE"))?Integer.valueOf(map.get("INCOMPLETE").toString()):0);
						vo.setInstances(BeanUtils.isNotEmpty(map.get("INSTANCES"))?Integer.valueOf(map.get("INSTANCES").toString()):0);
						rows.add(vo);
					}
				}else{
					JavaType javaType = JsonUtil.getMapper().getTypeFactory().constructParametricType(ArrayList.class, FlowOrgCountVo.class); 
					rows = JsonUtil.getMapper().readValue(JsonUtil.toJson(selectList), javaType);
				}
				if(BeanUtils.isNotEmpty(rows)){
					pageList.setRows(rows);
					pageList.setPageSize(rows.size());
					pageList.setTotal(rows.size());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageList;
	}

	@Override
	public PageList<FlowUserCountVo> flowUserCountList(QueryFilter queryFilter) {
		List<Map<String, Object>> selectList = this.getFlowUserSelectList(queryFilter);
		return parseToUserPageList(selectList);
	}
	
	public List<Map<String, Object>> getFlowUserSelectList(QueryFilter queryFilter){
		String dbType =  getDbType();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.PROC_DEF_KEY_ as procDefKey, a.PROC_DEF_NAME_ as procDefName, a.CREATE_BY_ as userId, a.CREATOR_ as userName,");
		if(SQLConst.DB_ORACLE.equals(dbType)){
			sql.append("ROUND(( SELECT SUM((TO_DATE ( TO_CHAR ( ( CASE END_TIME_ WHEN END_TIME_ THEN END_TIME_ ELSE (SELECT SYSDATE FROM dual) END ), 'YYYY-MM-DD HH24-MI-SS' ), 'YYYY-MM-DD HH24-MI-SS' ) - TO_DATE ( TO_CHAR ( CREATE_TIME_,'YYYY-MM-DD HH24-MI-SS'),'YYYY-MM-DD HH24-MI-SS')) * 24) FROM BPM_PRO_INST WHERE PROC_DEF_KEY_=a.PROC_DEF_KEY_)/ COUNT(a.ID_),1) AS avgLong,");
			sql.append("(SELECT count(1) FROM (SELECT PROC_DEF_KEY_,CREATE_BY_,((TO_DATE (TO_CHAR ((CASE END_TIME_ WHEN END_TIME_ THEN END_TIME_ ELSE (SELECT SYSDATE FROM dual) END ), 'YYYY-MM-DD HH24-MI-SS'),'YYYY-MM-DD HH24-MI-SS') - TO_DATE (TO_CHAR (CREATE_TIME_,'YYYY-MM-DD HH24-MI-SS'),'YYYY-MM-DD HH24-MI-SS')) * 24 ) as otime FROM bpm_pro_inst) c where c.otime>24 AND c.PROC_DEF_KEY_=a.PROC_DEF_KEY_ AND c.CREATE_BY_=a.CREATE_BY_) as overtime,");
		}else{
			sql.append("ROUND((ROUND(SUM(TIMESTAMPDIFF(MINUTE,a.CREATE_TIME_,IF (a.END_TIME_ IS NOT NULL,a.END_TIME_,now()))) / 60,1))/count(a.ID_),1) as avgLong,");
			sql.append("(SELECT count(1) FROM (SELECT PROC_DEF_KEY_,CREATE_BY_,TIMESTAMPDIFF(HOUR,CREATE_TIME_,IF(END_TIME_ IS NOT NULL,END_TIME_,now())) as otime FROM bpm_pro_inst) c where c.otime>24 AND c.PROC_DEF_KEY_=a.PROC_DEF_KEY_ AND c.CREATE_BY_=a.CREATE_BY_) as overtime,");
		}
		sql.append("ROUND((select count(1) from bpm_pro_inst b where b.STATUS_ in ('end','manualend') AND b.PROC_DEF_KEY_=a.PROC_DEF_KEY_ AND b.CREATE_BY_=a.CREATE_BY_ "+getFlowUserWhereSql(queryFilter, "b")+")/count(1)*100) as closingRate ");
		sql.append(" FROM bpm_pro_inst a WHERE 1=1 ");
		sql.append(getFlowUserWhereSql(queryFilter, "a"));
		sql.append(" GROUP BY a.PROC_DEF_KEY_,a.PROC_DEF_NAME_,a.CREATE_BY_,a.CREATOR_ ");
		return querySql(sql.toString());
	}
	
	private String getFlowUserWhereSql(QueryFilter queryFilter,String pre){
		StringBuilder whereSql = new StringBuilder();
		List<QueryField> querys = queryFilter.getQuerys();
		if(BeanUtils.isNotEmpty(queryFilter) && BeanUtils.isNotEmpty(querys)){
			for (QueryField queryField : querys) {
				if("createTime".equals(queryField.getProperty()) && BeanUtils.isNotEmpty(queryField.getValue())){
					List<String> times = (List<String>) queryField.getValue();
					whereSql.append(" AND "+pre+".CREATE_TIME_ >='"+times.get(0)+"' ");
					whereSql.append(" AND "+pre+".CREATE_TIME_ <='"+times.get(1)+"' ");
				}else if("procDefKey".equals(queryField.getProperty()) && BeanUtils.isNotEmpty(queryField.getValue())){
					String[] defKeys = queryField.getValue().toString().split(",");
					String defKeySql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(defKeys)));
					whereSql.append(" AND "+pre+".PROC_DEF_KEY_ in("+defKeySql+")");
				}else if("createBy".equals(queryField.getProperty()) && BeanUtils.isNotEmpty(queryField.getValue())){
					String[] userIds = queryField.getValue().toString().split(",");
					String userSql = StringUtil.convertListToSingleQuotesString(new HashSet<String>(Arrays.asList(userIds)));
					whereSql.append(" AND "+pre+".CREATE_BY_ in("+userSql+")");
				}
			}
			return whereSql.toString();
		}
		return "";
	}
	
	/**
	 * 转userPageList
	 * @param selectList
	 * @return
	 */
	private PageList<FlowUserCountVo> parseToUserPageList(List<Map<String, Object>> selectList){
		PageList<FlowUserCountVo> pageList = new PageList<>();
		pageList.setPage(1);
		pageList.setPageSize(1);
		pageList.setTotal(0);
		pageList.setRows(new ArrayList<FlowUserCountVo>());
		try {
			if(BeanUtils.isNotEmpty(selectList)){
				List<FlowUserCountVo> rows = new ArrayList<FlowUserCountVo>();
				if(selectList.get(0).containsKey("PROCDEFKEY")){
					for (Map<String, Object> map : selectList) {
						FlowUserCountVo vo = new FlowUserCountVo();
						vo.setProcDefKey(map.get("PROCDEFKEY").toString());
						vo.setProcDefName(map.get("PROCDEFNAME").toString());
						vo.setUserId(map.get("USERID").toString());
						vo.setUserName(map.get("USERNAME").toString());
						vo.setAvgLong(BeanUtils.isNotEmpty(map.get("AVGLONG"))?Float.valueOf(map.get("AVGLONG").toString()):0);
						vo.setOvertime(BeanUtils.isNotEmpty(map.get("OVERTIME"))?Integer.valueOf(map.get("OVERTIME").toString()):0);
						vo.setClosingRate(BeanUtils.isNotEmpty(map.get("CLOSINGRATE"))?Integer.valueOf(map.get("CLOSINGRATE").toString()):0);
						rows.add(vo);
					}
				}else{
					JavaType javaType = JsonUtil.getMapper().getTypeFactory().constructParametricType(ArrayList.class, FlowUserCountVo.class); 
					rows = JsonUtil.getMapper().readValue(JsonUtil.toJson(selectList), javaType);
				}
				
				if(BeanUtils.isNotEmpty(rows)){
					pageList.setRows(rows);
					pageList.setPageSize(rows.size());
					pageList.setTotal(rows.size());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageList;
	}

	@Override
    @Transactional
	public void removeByReportId(String id) {
		baseMapper.removeByReportId(id);
	}
	
}
