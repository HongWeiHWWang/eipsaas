package com.hotent.portal.controller;


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
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.model.SysLayoutSetting;
import com.hotent.portal.persistence.manager.SysLayoutSettingManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 布局设置 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author mouhb
 * @email mouhb@jee-soft.cn
 * @date 2017-08-07 16:18:52
 */
@RestController
@RequestMapping("/portal/sysLayoutSetting/sysLayoutSetting/v1/")
@Api(tags="门户布局设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysLayoutSettingController extends BaseController<SysLayoutSettingManager, SysLayoutSetting>{
	@Resource
	SysLayoutSettingManager sysLayoutSettingManager;
	

	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "布局设置列表(分页条件查询)数据", httpMethod = "POST", notes = "布局设置列表(分页条件查询)数据")
	public PageList<SysLayoutSetting> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysLayoutSetting> queryFilter) throws Exception {
		return sysLayoutSettingManager.query(queryFilter);
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "布局设置明细页面", httpMethod = "GET", notes = "布局设置明细页面")
	public @ResponseBody SysLayoutSetting getJson(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		if(StringUtil.isEmpty(id)){
			return new SysLayoutSetting();
		}
		SysLayoutSetting sysLayoutSetting=sysLayoutSettingManager.get(id);
		return sysLayoutSetting;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存布局设置信息", httpMethod = "POST", notes = "保存布局设置信息")
	public CommonResult<String> save(@ApiParam(name="sysLayoutSetting", value="布局设置")@RequestBody SysLayoutSetting sysLayoutSetting) throws Exception {
		String resultMsg=null;
		try {
			SysLayoutSetting setting = sysLayoutSettingManager.getByLayoutId(sysLayoutSetting.getLayoutId());
			if(setting == null){
				sysLayoutSetting.setId(UniqueIdUtil.getSuid());
				sysLayoutSettingManager.create(sysLayoutSetting);
				resultMsg="添加布局设置成功";
			}else{
				BeanUtils.copyNotNullProperties(setting, sysLayoutSetting);
				sysLayoutSettingManager.update(setting);
				resultMsg="更新布局设置成功";
			}
			return new CommonResult<>(true, resultMsg);
		} catch (Exception e) {
			resultMsg="对布局设置操作失败";
			return new CommonResult<>(false, resultMsg,e.getMessage());
		}
	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除布局设置记录", httpMethod = "DELETE", notes = "批量删除布局设置记录")
	public CommonResult<String> remove(@ApiParam(name="ids", value="主键", required = true)@RequestParam String ids) throws Exception {
		try {
			String[] aryIds=StringUtil.getStringAryByStr(ids);
			sysLayoutSettingManager.removeByIds(aryIds);
			return new CommonResult<>(true, "删除布局设置成功");
		} catch (Exception e) {
			return new CommonResult<>(false, "删除布局设置失败");
		}
	}
	
	@RequestMapping(value="getByLayout", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取布局详情", httpMethod = "GET", notes = "获取布局详情")
	public @ResponseBody SysLayoutSetting getByLayout(@ApiParam(name="layoutId", value="布局id", required = true)@RequestParam String layoutId) throws Exception {
		if(StringUtil.isEmpty(layoutId)){
			return new SysLayoutSetting();
		}
		SysLayoutSetting sysLayoutSetting=sysLayoutSettingManager.getByLayoutId(layoutId);
		return sysLayoutSetting;
	}
}
