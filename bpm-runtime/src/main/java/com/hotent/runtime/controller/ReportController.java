package com.hotent.runtime.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import poi.util.ExcelUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
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

/**
 * 流程统计分析相关接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author tangxin
 * @email tangx@jee-soft.cn
 * @date 2019年3月25日
 */

@RestController
@RequestMapping("/runtime/report/v1/")
@Api(tags="流程统计分析")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class ReportController extends BaseController<BpmReportListManager,BpmReportList>{
	
	@Resource
	BpmReportListManager bpmReportListManager;
	@Resource
	BpmReportActManager bpmReportActManager;
	@Resource
	IUserService ius;
	@Resource
	UCFeignService ucService;
	
	
	
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程统计分析列表(分页条件查询)数据", httpMethod = "POST", notes = "获取流程统计分析列表(分页条件查询)数据")
	public PageList<BpmReportList> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter,@ApiParam(name="isPublic", value="只获取已发布的", required = true)@RequestParam Optional<Boolean> isPublic) throws Exception {
		boolean is = isPublic.orElse(false);
		if(is){
			queryFilter.addFilter("type", "1", QueryOP.EQUAL);
		}
		return bpmReportListManager.query(queryFilter);
	}
	
	@RequestMapping(value="getList", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程统计分析明细页面", httpMethod = "GET", notes = "流程统计分析明细页面")
	public @ResponseBody BpmReportList getList(@ApiParam(name="id", value="流程分析id", required = true)@RequestParam String id) throws Exception {
		if(StringUtil.isEmpty(id)){
			return new BpmReportList();
		}
		BpmReportList bpmReportList=bpmReportListManager.get(id);
		return bpmReportList;
	}
	
	@RequestMapping(value="getAct", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "报表明细页面", httpMethod = "GET", notes = "报表明细页面")
	public @ResponseBody BpmReportAct getAct(@ApiParam(name="id", value="报表id", required = true)@RequestParam String id) throws Exception {
		if(StringUtil.isEmpty(id)){
			return new BpmReportAct();
		}
		BpmReportAct bpmReportAct=bpmReportActManager.get(id);
		return bpmReportAct;
	}
	
	@RequestMapping(value="saveList", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存流程统计分析信息", httpMethod = "POST", notes = "保存流程统计分析信息")
	public CommonResult<String> saveList(@ApiParam(name="bpmReportList", value="计划名称")@RequestBody BpmReportList bpmReportList) throws Exception {
		String resultMsg=null;
		String id=bpmReportList.getId();
		try {
			if(StringUtil.isEmpty(id)){
				IUser user = ContextUtil.getCurrentUser();
				bpmReportList.setCreateBy(user.getUserId());
				bpmReportList.setCreateName(user.getFullname());
				ObjectNode org=ucService.getMainGroup(user.getUserId());
				if(BeanUtils.isNotEmpty(org)){
					String orgId=org.get("id").asText();
					String orgName=org.get("name").asText();
					bpmReportList.setCreateOrgId(orgId);
					bpmReportList.setOrgName(orgName);
				}
				bpmReportList.setId(UniqueIdUtil.getSuid());
				bpmReportListManager.create(bpmReportList);
				resultMsg="添加流程统计分析成功";
			}else{
				bpmReportListManager.update(bpmReportList);
				resultMsg="更新流程统计分析成功";
			}
			return new CommonResult<>(true, resultMsg,bpmReportList.getId());
		} catch (Exception e) {
			resultMsg="对流程统计分析操作失败";
			return new CommonResult<>(false, resultMsg);
		}
	}
	
	@RequestMapping(value="saveAct", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存报表信息", httpMethod = "POST", notes = "保存报表信息")
	public CommonResult<String> saveAct(@ApiParam(name="bpmReportAct", value="计划名称")@RequestBody BpmReportActVo bpmReportAct) throws Exception {
		return bpmReportActManager.saveAct(bpmReportAct);
	}
	
	@RequestMapping(value="removeList", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除流程统计分析记录", httpMethod = "DELETE", notes = "批量删除流程统计分析记录")
	public CommonResult<String> removeList(@ApiParam(name="ids", value="联系人ids", required = true)@RequestParam String ids) throws Exception {
		try {
			String[] aryIds=StringUtil.getStringAryByStr(ids);
			bpmReportListManager.removeByIds(aryIds);
			return new CommonResult<>(true, "删除流程统计分析成功", null);
		} catch (Exception e) {
			return new CommonResult<>(false, "删除流程统计分析失败", null);
		}
	}
	
	@RequestMapping(value="removeAct", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除报表记录", httpMethod = "DELETE", notes = "批量删除报表记录")
	public CommonResult<String> removeAct(@ApiParam(name="ids", value="联系人ids", required = true)@RequestParam String ids) throws Exception {
		try {
			String[] aryIds=StringUtil.getStringAryByStr(ids);
			bpmReportActManager.removeByIds(aryIds);
			return new CommonResult<>(true, "删除报表成功", null);
		} catch (Exception e) {
			return new CommonResult<>(false, "删除报表失败", null);
		}
	}
	
	
	@RequestMapping(value="getEchartsData", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取统计图表数据", httpMethod = "GET", notes = "报获取统计图表数据")
	public CommonResult<JsonNode> getEchartsData(@ApiParam(name="reportId", value="统计图表id", required = true)@RequestParam String reportId) throws Exception {
		return bpmReportActManager.getEchartsData(reportId);
	}

	@RequestMapping(value="getSingleEchartsData", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取统计图表数据", httpMethod = "GET", notes = "报获取统计图表数据")
	public CommonResult<JsonNode> getSingleEchartsData(@ApiParam(name="id", value="统计图表id", required = true)@RequestParam String id) throws Exception {
		return bpmReportActManager.getSingleEchartsData(id);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="publish", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "发布图表设计", httpMethod = "POST", notes = "发布图表设计")
	public CommonResult<String> publish(@ApiParam(name="id",value="图表ID")@RequestParam String id)
			throws Exception {
		try {
			BpmReportList bpmReportList = bpmReportListManager.get(id);
			if(BeanUtils.isNotEmpty(bpmReportList)){
				bpmReportList.setType("1");
				bpmReportListManager.update(bpmReportList);
			}else{
				return new CommonResult(true, "发布失败：ID为【"+id+"】的统计分析不存在！");
			}
			return new CommonResult(true, "发布成功");
		} catch (Exception e) {
			return new CommonResult(false, "发布失败："+e.getCause().toString());
		}
	}
	
	@RequestMapping(value="listAll", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有流程统计分析列表数据", httpMethod = "GET", notes = "获取所有流程统计分析列表数据")
	public PageList<BpmReportList> listAll() throws Exception {
		QueryFilter queryFilter = QueryFilter.<BpmReportList>build().withPage(new PageBean(1,Integer.MAX_VALUE));
		queryFilter.addFilter("type", "1", QueryOP.EQUAL);
		return bpmReportListManager.query(queryFilter);
	}
	
	@RequestMapping(value="flowOrgCountList", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程按部门统计发起数据", httpMethod = "POST", notes = "流程按部门统计发起数据")
	public PageList<FlowOrgCountVo> flowOrgCountList(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		return bpmReportActManager.flowOrgCountList(queryFilter);
	}
	
	@RequestMapping(value="flowOrgCountListExport", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程按部门统计发起数据", httpMethod = "POST", notes = "流程按部门统计发起数据")
	public void flowOrgCountListExport(HttpServletResponse response,@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		try {
			List<Map<String, Object>> list = bpmReportActManager.getFlowOrgSelectList(queryFilter);
			String tempName = "流程按部门统计发起数据";
			Map<String, String> exportMaps = new LinkedHashMap<String, String>();
			exportMaps.put("procDefKey", "工单key");
			exportMaps.put("procDefName", "工单类型");
			exportMaps.put("instances", "工单数量");
			exportMaps.put("hourLong", "运行时长（小时）");
			exportMaps.put("incomplete", "未完成工单数");
			HSSFWorkbook book = ExcelUtil.exportExcel(tempName, 24, exportMaps, list);
			ExcelUtil.downloadExcel(book, tempName, response);
		} catch (Exception e) {
			throw new RuntimeException("导出失败："+e.getMessage());
		}
	}
	
	@RequestMapping(value="flowUserCountList", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程按人员统计发起数据", httpMethod = "POST", notes = "流程按人员统计发起数据")
	public PageList<FlowUserCountVo> flowUserCountList(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		return bpmReportActManager.flowUserCountList(queryFilter);
	}
	
	@RequestMapping(value="flowUserCountListExport", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程按人员统计发起数据", httpMethod = "POST", notes = "流程按人员统计发起数据")
	public void flowUserCountListExport(HttpServletResponse response,@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		try {
			List<Map<String, Object>> list = bpmReportActManager.getFlowUserSelectList(queryFilter);
			String tempName = "流程按人员统计发起数据";
			Map<String, String> exportMaps = new LinkedHashMap<String, String>();
			exportMaps.put("procDefKey", "工单key");
			exportMaps.put("procDefName", "工单类型");
			exportMaps.put("userId", "处理人ID");
			exportMaps.put("userName", "处理人");
			exportMaps.put("avgLong", "平均时长（小时）");
			exportMaps.put("overtime", "逾期工单数");
			exportMaps.put("closingRate", "闭单率（%）");
			HSSFWorkbook book = ExcelUtil.exportExcel(tempName, 24, exportMaps, list);
			ExcelUtil.downloadExcel(book, tempName, response);
		} catch (Exception e) {
			throw new RuntimeException("导出失败："+e.getMessage());
		}
	}
	
}
