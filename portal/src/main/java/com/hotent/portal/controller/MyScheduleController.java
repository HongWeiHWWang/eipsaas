package com.hotent.portal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hotent.portal.persistence.manager.MyScheduleManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.portal.model.MySchedule;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.StringUtil;

/**
 * 
 * <pre> 
 * 描述：行程管理 控制器类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2018-09-10 09:51:04
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/portal/mySchedule/v1")
@Api(tags="行程管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class MyScheduleController extends BaseController<MyScheduleManager, MySchedule>{
	@Resource
	MyScheduleManager myScheduleManager;
	
	/**
	 * 行程管理列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="行程管理数据列表", httpMethod = "POST", notes = "获取行程管理列表")
	public PageList<MySchedule> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<MySchedule> queryFilter) throws Exception{
		queryFilter.addFilter("USER_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
		PageList<MySchedule> query = myScheduleManager.query(queryFilter);
		
		return query;
	}
	
	/**
	 * 行程管理明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="行程管理数据详情",httpMethod = "GET",notes = "行程管理数据详情")
	public MySchedule get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return myScheduleManager.get(id);
	}
	
    /**
	 * 新增行程管理
	 * @param mySchedule
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新行程管理数据", httpMethod = "POST", notes = "新增,更新行程管理数据")
	public CommonResult<String> save(@ApiParam(name="mySchedule",value="行程管理业务对象", required = true)@RequestBody MySchedule mySchedule) throws Exception{
		String msg = "添加行程管理成功";
		if(StringUtil.isEmpty(mySchedule.getId())){
			mySchedule.setUserId(ContextUtil.getCurrentUserId());
			myScheduleManager.create(mySchedule);
		}else{
			myScheduleManager.update(mySchedule);
			 msg = "更新行程管理成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除行程管理记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除行程管理记录", httpMethod = "DELETE", notes = "删除行程管理记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		myScheduleManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除行程管理记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除行程管理记录", httpMethod = "DELETE", notes = "批量删除行程管理记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		myScheduleManager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
}
