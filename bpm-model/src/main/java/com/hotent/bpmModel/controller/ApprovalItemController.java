package com.hotent.bpmModel.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.constants.CategoryConstants;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpmModel.manager.BpmApprovalItemManager;
import com.hotent.bpmModel.model.BpmApprovalItem;
import com.hotent.uc.api.impl.util.ContextUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 常用语管理 控制器类
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/flow/approvalItem/v1/")
@Api(tags="常用语管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class ApprovalItemController extends BaseController<BpmApprovalItemManager, BpmApprovalItem>{
	@Resource
	PortalFeignService PortalFeignService;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	
	/**
	 * 常用语管理列表(分页条件查询)数据
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "常用语列表(分页条件查询)数据", httpMethod = "POST", notes = "流程代理设置列表(分页条件查询)数据")
	public PageList<BpmApprovalItem> listJson(
			@ApiParam(name="isPersonal",value="是否个人常用语",defaultValue="false")@RequestParam boolean isPersonal,
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<BpmApprovalItem> queryFilter) throws Exception{
		PageList<BpmApprovalItem> bpmApprovalItemts = null;
		if(!isPersonal){
			queryFilter.addFilter("TYPE_", 4, QueryOP.NOT_EQUAL,FieldRelation.AND,"a");
			bpmApprovalItemts =  baseService.query(queryFilter);
		}else {
			String currUserId = ContextUtil.getCurrentUserId();
			queryFilter.addFilter("TYPE_", 4, QueryOP.EQUAL,FieldRelation.AND,"a");
			queryFilter.addFilter("USER_ID_", currUserId, QueryOP.EQUAL,FieldRelation.AND,"a");
			bpmApprovalItemts =  baseService.query(queryFilter);
		}
		QueryFilter<?> queryFilter2 = QueryFilter.build();
		queryFilter.withPage(new PageBean(1, Integer.MAX_VALUE));
		queryFilter2.addFilter("type_group_key_", CategoryConstants.CAT_FLOW.key(), QueryOP.EQUAL);
		ObjectNode result = PortalFeignService.getAllSysType(queryFilter2);
		List<ObjectNode> sysTypeList = (List<ObjectNode>) JsonUtil.toBean(JsonUtil.toJson(result.get("rows")),new TypeReference<List<ObjectNode>>(){});
		List<DefaultBpmDefinition> bpmDefinitionlList = bpmDefinitionManager.getAll();
		//根据类型，把常用于的作用对象初始化
		for (BpmApprovalItem approvalItem : bpmApprovalItemts.getRows()) {
			if (approvalItem.getType() == BpmApprovalItem.TYPE_FLOW ) {
				for (DefaultBpmDefinition bpmDefinition : bpmDefinitionlList) {
					if (approvalItem.getDefKey().equals(bpmDefinition.getDefKey())) {
						approvalItem.setDefKey(bpmDefinition.getName());						
					}
				}
			}else if(approvalItem.getType() == BpmApprovalItem.TYPE_FLOWTYPE) {
				for (ObjectNode ISysType : sysTypeList) {
					if (approvalItem.getTypeId().equals(ISysType.get("id").asText())) {
						approvalItem.setTypeId(ISysType.get("name").asText());
					}
				}
			}else {
				approvalItem.setDefKey("所有流程");
			}
		}
		return  bpmApprovalItemts;
	}
	
	/**
	 * 编辑常用语管理信息页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 * @exception 
	 */
	@RequestMapping(value="approvalItemGet", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "编辑常用语管理信息页面", httpMethod = "GET", notes = "编辑常用语管理信息页面")
	public Object approvalItemGet(
			@ApiParam(name="id",value="常用语id")@RequestParam String id ) throws Exception{
		BpmApprovalItem bpmApprovalItem=null;
		if(StringUtil.isNotEmpty(id)){
			bpmApprovalItem=baseService.get(id);
		}
		return  bpmApprovalItem;
	}

	
	/**
	 * 保存常用语管理信息
	 * @param request
	 * @param response
	 * @param bpmApprovalItem
	 * @throws Exception 
	 * void
	 * @exception 
	 */
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存常用语管理信息", httpMethod = "POST", notes = "保存常用语管理信息")
	public CommonResult<String> save(
			@ApiParam(name="bpmApprovalItem",value="常用语对象", required = true) @RequestBody BpmApprovalItem bpmApprovalItem) throws Exception{
		String resultMsg=null;
		try {
			String dealType = StringUtil.isNotEmpty(bpmApprovalItem.getId())?"编辑":"添加";
			baseService.addTaskApproval(bpmApprovalItem);
			resultMsg= dealType+"常用语成功";
			return new CommonResult<String>(true,resultMsg,"");
		} catch (Exception e) {
			resultMsg="对常用语操作失败";
			return new CommonResult<String>(false,e.getMessage(),"");
		}
	}

	/**
	 * 批量删除常用语管理记录
	 * @param request
	 * @param response
	 * @throws Exception 
	 * void
	 * @exception 
	 */
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除常用语管理记录", httpMethod = "DELETE", notes = "批量删除常用语管理记录")
	public CommonResult<String> remove(
			@ApiParam(name="ids",value="常用语id字符串", required = true) @RequestParam String ids)throws Exception {
		try {
			String[] aryIds = ids.split(",");
			baseService.removeByIds(aryIds);
			return new CommonResult<String>(true,"删除常用语成功","");
		} catch (Exception e) {
			return new CommonResult<String>(false,"删除常用语失败","");
		}
	}
	
	/**
	 * 获取常用语
	 * @param defKey
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getApprovalByDefKeyAndTypeId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取常用语", httpMethod = "GET", notes = "获取常用语")
	public List<String> getApprovalByDefKeyAndTypeId(
			@ApiParam(name="defKey",value="流程定义key", required = true) @RequestParam String defKey,
			@ApiParam(name="typeId",value="流程分类id", required = true) @RequestParam Optional<String> typeId,
			@ApiParam(name="userId",value="当前用户id") @RequestParam Optional<String> userId)throws Exception {
			return baseService.getApprovalByDefKeyAndTypeId(defKey,typeId.orElse(null),userId.orElse(null));
	}
}
