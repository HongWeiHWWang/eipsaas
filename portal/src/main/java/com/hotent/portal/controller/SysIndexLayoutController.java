package com.hotent.portal.controller;


import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.model.SysIndexLayout;
import com.hotent.portal.persistence.manager.SysIndexLayoutManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



/**
 * 首页布局 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月14日
 */
@RestController
@RequestMapping("/portal/sysIndexLayout/sysIndexLayout/v1/")
@Api(tags="门户布局")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysIndexLayoutController extends BaseController<SysIndexLayoutManager, SysIndexLayout>{
	@Resource
	SysIndexLayoutManager sysIndexLayoutManager;
	
	
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "系统首页布局列表(分页条件查询)数据", httpMethod = "POST", notes = "系统首页布局列表(分页条件查询)数据")
	public PageList<SysIndexLayout> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysIndexLayout> queryFilter) throws Exception {
		return sysIndexLayoutManager.query(queryFilter);
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "布局明细页面", httpMethod = "GET", notes = "布局明细页面")
	public @ResponseBody SysIndexLayout getJson(@ApiParam(name="id", value="主键", required = true)@RequestParam Long id) throws Exception {
		if(id==null||id==0){
			return null;
		}
		SysIndexLayout sysIndexLayout=sysIndexLayoutManager.get(id);
		return sysIndexLayout;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存布局信息", httpMethod = "POST", notes = "保存布局信息")
	public CommonResult<String> save(@ApiParam(name="sysIndexLayout", value="系统主页布局实体")@RequestBody SysIndexLayout sysIndexLayout) throws Exception {
		String resultMsg=null;
		Long id=sysIndexLayout.getId();
		try {
			if(id==null||id==0){
				sysIndexLayout.setId(UniqueIdUtil.getUId());
				sysIndexLayoutManager.create(sysIndexLayout);
				resultMsg="添加布局成功";
			}else{
				sysIndexLayoutManager.update(sysIndexLayout);
				resultMsg="更新布局成功";
			}
			return new CommonResult<>(true, resultMsg, null);
		} catch (Exception e) {
			resultMsg="对布局操作失败";
			return new CommonResult<>(false, resultMsg, null);
		}
	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除布局记录", httpMethod = "DELETE", notes = "批量删除布局记录")
	public CommonResult<String> remove(@ApiParam(name="ids", value="主键", required = true)@RequestParam String ids) throws Exception {
		try {
			Long[] aryIds = StringUtil.getLongAryByStr(ids);
			sysIndexLayoutManager.removeByIds(Arrays.asList(aryIds));
			return new CommonResult<>(true, "删除布局成功", null);
		} catch (Exception e) {
			return new CommonResult<>(false, "删除布局失败", null);
		}
	}
}
