package com.hotent.form.controller;

import javax.annotation.Resource;

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
import com.hotent.form.model.ViewManage;
import com.hotent.form.persistence.manager.ViewManageManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：视图管理 控制器类
 * 构建组：x7
 * 作者:pangq
 * 邮箱:pangq@jee-soft.cn
 * 日期:2020-04-30 17:01:50
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/form/viewManage/v1")
@Api(tags="viewManageController")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class ViewManageController extends BaseController<ViewManageManager,ViewManage>{
	@Resource
	ViewManageManager viewManageManager;
	
	/**
	 * 视图管理列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="视图管理数据列表", httpMethod = "POST", notes = "获取视图管理列表")
	public PageList<ViewManage> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return viewManageManager.query(queryFilter);
	}
	
	/**
	 * 视图管理明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="视图管理数据详情",httpMethod = "GET",notes = "视图管理数据详情")
	public ViewManage get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return viewManageManager.get(id);
	}
	
    /**
	 * 新增视图管理
	 * @param viewManage
	 * @param saveType 保存类型：0仅保存，1保存并创建视图
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="/save/{saveType}")
	@ApiOperation(value = "新增,更新视图管理数据", httpMethod = "POST", notes = "新增,更新视图管理数据")
	public CommonResult<String> save(@ApiParam(name="viewManage",value="视图管理业务对象", required = true)@RequestBody ViewManage viewManage,
			@PathVariable Integer saveType) throws Exception{
		String msg = "操作成功";
		viewManageManager.savePub(viewManage,saveType);
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 生成视图
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@GetMapping(value="/createPhysicalView/{id}")
	@ApiOperation(value = "新增,更新视图管理数据", httpMethod = "POST", notes = "新增,更新视图管理数据")
	public CommonResult<String> createPhysicalView(@PathVariable String id) throws Exception{
		viewManageManager.createPhysicalView(id);
		String msg = "操作成功";
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 批量删除视图管理记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除视图管理记录", httpMethod = "DELETE", notes = "批量删除视图管理记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		viewManageManager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
}
