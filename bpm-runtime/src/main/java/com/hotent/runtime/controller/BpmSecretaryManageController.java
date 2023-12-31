package com.hotent.runtime.controller;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeDoneManager;
import com.hotent.bpm.persistence.model.BpmTaskNoticeDone;
import com.hotent.uc.api.model.IUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.manager.BpmSecretaryManageManager;
import com.hotent.bpm.persistence.model.BpmSecretaryManage;
import com.hotent.uc.api.impl.util.ContextUtil;

/**
 * 
 * <pre> 
 * 描述：秘书管理表 控制器类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2019-09-16 10:07:13
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/runtime/bpmSecretaryManage/v1")
@Api(tags="流程秘书管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmSecretaryManageController extends BaseController<BpmSecretaryManageManager,BpmSecretaryManage>{
	@Resource
	BpmSecretaryManageManager bpmSecretaryManageManager;
	
	/**
	 * 秘书管理表列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="秘书管理表数据列表", httpMethod = "POST", notes = "获取秘书管理表列表")
	public PageList<BpmSecretaryManage> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter,
			                                 @ApiParam(name="personal",value="是否个人")@RequestParam Optional<Boolean> personal) throws Exception{
		if (personal.orElse(false)) {
			queryFilter.addFilter("leaderId", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
		}
		return bpmSecretaryManageManager.query(queryFilter);
	}
	
	/**
	 * 秘书管理表明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="秘书管理表数据详情",httpMethod = "GET",notes = "秘书管理表数据详情")
	public BpmSecretaryManage get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return bpmSecretaryManageManager.get(id);
	}
	
    /**
	 * 新增秘书管理表
	 * @param bpmSecretaryManage
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新秘书管理表数据", httpMethod = "POST", notes = "新增,更新秘书管理表数据")
	public CommonResult<String> save(@ApiParam(name="bpmSecretaryManage",value="秘书管理表业务对象", required = true)@RequestBody BpmSecretaryManage bpmSecretaryManage) throws Exception{
		String msg = "添加秘书成功";
		if(StringUtil.isEmpty(bpmSecretaryManage.getId())){
			if (StringUtil.isEmpty(bpmSecretaryManage.getLeaderId())) {
				bpmSecretaryManage.setLeaderId(ContextUtil.getCurrentUserId());
				bpmSecretaryManage.setLeaderName(ContextUtil.getCurrentUser().getFullname());
			}
			bpmSecretaryManageManager.create(bpmSecretaryManage);
		}else{
			bpmSecretaryManageManager.update(bpmSecretaryManage);
			 msg = "更新秘书成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除秘书管理表记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除秘书管理表记录", httpMethod = "DELETE", notes = "删除秘书管理表记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		bpmSecretaryManageManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除秘书管理表记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除秘书管理表记录", httpMethod = "DELETE", notes = "批量删除秘书管理表记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		bpmSecretaryManageManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}


    @RequestMapping(value = "/getSecretaryByUserId", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "根据当前登录用户ID获取该用户的领导", httpMethod = "GET", notes = "根据当前登录用户ID获取该用户的领导")
    public List<BpmSecretaryManage> getSecretaryByUserId(){
        IUser user = ContextUtil.getCurrentUser();
        List<BpmSecretaryManage> list = bpmSecretaryManageManager.getSecretaryByUserId(user.getUserId());
        return list;
    }

}
