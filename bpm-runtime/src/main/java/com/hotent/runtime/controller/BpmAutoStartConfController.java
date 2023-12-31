package com.hotent.runtime.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.runtime.manager.BpmAutoStartConfManager;
import com.hotent.runtime.model.BpmAutoStartConf;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 流程自动发起配置表
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月15日
 */
@RestController	
@RequestMapping("/bpm/bpmAutoStartConf/v1")
@Api(tags="流程自动发起配置表")
public class BpmAutoStartConfController extends BaseController<BpmAutoStartConfManager, BpmAutoStartConf>{
	/**
	 * 流程自动发起配置表列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="流程自动发起配置表数据列表", httpMethod = "POST", notes = "获取流程自动发起配置表列表")
	public PageList<BpmAutoStartConf> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<BpmAutoStartConf> queryFilter) throws Exception{
		return baseService.query(queryFilter);
	}
	
	/**
	 * 流程自动发起配置表明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="流程自动发起配置表数据详情",httpMethod = "GET",notes = "流程自动发起配置表数据详情")
	public BpmAutoStartConf get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return baseService.get(id);
	}
	
    /**
	 * 新增流程自动发起配置表
	 * @param boAutoStartConf
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新流程自动发起配置表数据", httpMethod = "POST", notes = "新增,更新流程自动发起配置表数据")
	public CommonResult<String> save(@ApiParam(name="boAutoStartConf",value="流程自动发起配置表业务对象", required = true)@RequestBody BpmAutoStartConf boAutoStartConf) throws Exception{
		String msg = "添加流程自动发起配置表成功";
		if(StringUtil.isEmpty(boAutoStartConf.getId())){
			baseService.create(boAutoStartConf);
		}else{
			baseService.update(boAutoStartConf);
			 msg = "更新流程自动发起配置表成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 批量删除流程自动发起配置表记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除流程自动发起配置表记录", httpMethod = "DELETE", notes = "批量删除流程自动发起配置表记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		baseService.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	/**
	 * 流程自动发起配置表明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getByDefId")
	@ApiOperation(value="流程自动发起配置表数据详情",httpMethod = "GET",notes = "流程自动发起配置表数据详情")
	public BpmAutoStartConf getByDefId(@ApiParam(name="defId",value="流程定义id", required = true)@RequestParam String defId) throws Exception{
		BpmDefinitionManager bpmDefinitionManager = AppUtil.getBean(BpmDefinitionManager.class);
		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		if (BeanUtils.isEmpty(defaultBpmDefinition)) {
			throw new RuntimeException(String.format("根据所传流程定义ID:%s未找到流定义", defId));
		}
		BpmAutoStartConf byDefKey = baseService.getByDefKey(defaultBpmDefinition.getDefKey());
		if (BeanUtils.isEmpty(byDefKey)) {
			byDefKey = new BpmAutoStartConf();
			byDefKey.setDefKey(defaultBpmDefinition.getDefKey());
		}
		return byDefKey;
	}
	
	@RequestMapping(value = "/defAutoStart", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "批量启动流程", httpMethod = "GET", notes = "批量启动流程")
	public ObjectNode defAutoStart()throws Exception {
		return baseService.defAutoStart();
	}
}
