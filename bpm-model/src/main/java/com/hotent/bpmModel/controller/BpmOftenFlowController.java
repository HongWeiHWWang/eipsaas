package com.hotent.bpmModel.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpmModel.manager.BpmOftenFlowManager;
import com.hotent.bpmModel.model.BpmOftenFlow;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：通用流程 控制器类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-03-04 15:23:03
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping("/bpmModel/BpmOftenFlow/v1")
@Api(tags="常用流程设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmOftenFlowController extends BaseController<BpmOftenFlowManager, BpmOftenFlow>{
	/**
	 * 通用流程列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="通用流程数据列表", httpMethod = "POST", notes = "获取通用流程列表")
	public PageList<BpmOftenFlow> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<BpmOftenFlow> queryFilter) throws Exception{
		PageList<BpmOftenFlow> query = baseService.query(queryFilter);
		return query;
	}
	
	/**
	 * 通用流程明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="通用流程数据详情",httpMethod = "GET",notes = "通用流程数据详情")
	public BpmOftenFlow get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return baseService.get(id);
	}
	
    /**
	 * 新增通用流程
	 * @param bpmCommonDef
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新通用流程数据", httpMethod = "POST", notes = "新增,更新通用流程数据")
	public CommonResult<String> save(@ApiParam(name="defkeys",value="通用流程defkey，多个defkey用逗号分隔", required = true)@RequestBody String defkeys) throws Exception{
		baseService.saveOrUpdateCommonFlow(defkeys);
		return new CommonResult<String>("添加通用流程成功");
	}
	
	/**
	 * 删除通用流程记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除通用流程记录", httpMethod = "DELETE", notes = "删除通用流程记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		baseService.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除通用流程记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除通用流程记录", httpMethod = "DELETE", notes = "批量删除通用流程记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		String msg = "批量删除成功";
		baseService.removeByIds(ids);
		List<String> list = Arrays.asList(ids);
		if(list.size() == 1) {
			msg = "删除成功";
		}
		return new CommonResult<String>(true, msg);
	}
	
	/**
	 * 通用流程明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getMyOftenFlowKey")
	@ApiOperation(value="获取我的常用流程key",httpMethod = "GET",notes = "获取我的常用流程key")
	public Set<String> getMyOftenFlowKey() throws Exception{
		QueryFilter<BpmOftenFlow> queryFilter = QueryFilter.<BpmOftenFlow>build();
		PageBean page = new PageBean(1,20);
		queryFilter.setPageBean(page);
		queryFilter.addFilter("USER_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
		PageList<BpmOftenFlow> query = baseService.query(queryFilter);
		if (query.getRows().size()==0) {
			//设置USER_ID_ 为 -1
			queryFilter.getQuerys().get(0).setValue("-1");;
			query = baseService.query(queryFilter);
		}
		Set<String> defKeySet = new HashSet<>();
		if (BeanUtils.isEmpty(query) || query.getRows().size()==0) {
			return defKeySet;
		}
		for (BpmOftenFlow flow : query.getRows()) {
			defKeySet.add(flow.getDefKey());
		}
		return defKeySet;
	}
	
	@PostMapping(value="saveMyOftenFlow")
	@ApiOperation(value = "保存我的常用流程", httpMethod = "POST", notes = "保存我的常用流程")
	public CommonResult<String> saveMyOftenFlow(@ApiParam(name="list",value="通用流程业务对象", required = true)@RequestBody ArrayNode list) throws Exception{
		IUser user = ContextUtil.getCurrentUser();
		baseService.saveMyFlow(user.getUserId(), user.getFullname(), list);
		return new CommonResult<String>("添加通用流程成功");
	}
}
