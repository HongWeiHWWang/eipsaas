package com.hotent.bpmModel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpmModel.manager.BpmDeputyManager;
import com.hotent.bpmModel.model.BpmDeputy;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：代理设置 控制器类
 * 构建组：x7
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2019-02-18 09:46
 * </pre>
 */
@RestController
@RequestMapping("/bpmModel/bpmDeputy/v1")
@Api(tags="流程代理设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmDeputyController extends BaseController<BpmDeputyManager, BpmDeputy> {
	/**
	 * 代理设置列表(分页条件查询)数据
	 * @param queryFilter
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="代理设置数据列表", httpMethod = "POST", notes = "获取代理设置列表")
	public PageList<BpmDeputy> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<BpmDeputy> queryFilter,
			@ApiParam(name="personal",value="是否个人")@RequestParam Optional<Boolean> personal) throws Exception{
		if (personal.orElse(false)) {
			queryFilter.addFilter("userId", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
		}
		return baseService.query(queryFilter);
	}
	
	/**
	 * 代理设置明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="代理设置数据详情",httpMethod = "GET",notes = "代理设置数据详情")
	public BpmDeputy get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return baseService.get(id);
	}
	
    /**
	 * 新增代理设置
	 * @param bpmDeputy
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新代理设置数据", httpMethod = "POST", notes = "新增,更新代理设置数据")
	public CommonResult<String> save(@ApiParam(name="bpmDeputy",value="代理设置业务对象", required = true)@RequestBody BpmDeputy bpmDeputy) throws Exception{
		String msg = "添加代理设置成功";
		QueryFilter<BpmDeputy> queryFilter = QueryFilter.<BpmDeputy>build();
		queryFilter.addFilter("USER_ID_", bpmDeputy.getUserId(), QueryOP.EQUAL);
		queryFilter.addFilter("AGENT_ID_", bpmDeputy.getAgentId(), QueryOP.EQUAL);
		PageList<BpmDeputy> list = baseService.query(queryFilter);
		if(list.getRows().size()>0){
			return new CommonResult<String>(false, "当前用户已设置该被代理人,请勿重复保存！");
		}else if(StringUtil.isEmpty(bpmDeputy.getId())&&list.getRows().size()==0){
			baseService.create(bpmDeputy);
		}else{
			baseService.update(bpmDeputy);
			 msg = "更新代理设置成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除代理设置记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除代理设置记录", httpMethod = "DELETE", notes = "删除代理设置记录")
	public CommonResult<String> remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		baseService.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除代理设置记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除代理设置记录", httpMethod = "DELETE", notes = "批量删除代理设置记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		baseService.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	/**
	 * 根据代理人获取代理记录
	 * @param agentId
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getByAgentId")
	@ApiOperation(value="根据代理人获取代理记录",httpMethod = "GET",notes = "根据代理人获取代理记录")
	public List<BpmDeputy> getByAgentId(@ApiParam(name="agentId",value="代理人id", required = true)@RequestParam String agentId) throws Exception{
		return baseService.getByAgentId(agentId);
	}
	
	/**
	 * 根据代理人获取代理记录
	 * @param userId
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getByUserId")
	@ApiOperation(value="获取用户代理设置",httpMethod = "GET",notes = "获取用户代理设置")
	public BpmDeputy getByUserId(@ApiParam(name="userId",value="用户id", required = false)@RequestParam Optional<String> userId) throws Exception{
		BpmDeputy deputy = null;
		String uId = userId.orElse("");
		if(StringUtil.isEmpty(uId)){
			deputy = baseService.getByUserId(ContextUtil.getCurrentUserId());
		}else{
			deputy = baseService.getByUserId(userId.orElse(ContextUtil.getCurrentUserId()));
		}
		if(BeanUtils.isEmpty(deputy) && StringUtil.isEmpty(uId)){
			IUser cuser = ContextUtil.getCurrentUser();
			if(BeanUtils.isNotEmpty(cuser)){
				deputy = new BpmDeputy();
				deputy.setUserId(cuser.getUserId());
				deputy.setUserName(cuser.getFullname());
				deputy.setIsMail(1);
				deputy.setIsUsable(1);
			}
		}
		return deputy;
	}
}
