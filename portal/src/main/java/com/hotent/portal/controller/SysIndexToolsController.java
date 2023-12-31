package com.hotent.portal.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.sys.persistence.manager.SysAuthUserManager;
import com.hotent.sys.persistence.model.SysAuthUser;
import com.hotent.portal.model.SysIndexTools;
import com.hotent.portal.persistence.manager.SysIndexToolsManager;


/**
 * 首页工具 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月14日
 */
@RestController
@RequestMapping("/portal/sysIndexTools/sysIndexTools/v1/")
@Api(tags="门户工具")
public class SysIndexToolsController extends BaseController<SysIndexToolsManager, SysIndexTools> {
	@Resource
	SysIndexToolsManager sysIndexToolsManager;
	@Resource
	SysAuthUserManager bpmDefUserManager;
	

	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "首页工具列表(分页条件查询)数据", httpMethod = "POST", notes = "首页工具列表(分页条件查询)数据")
	public PageList<SysIndexTools> executeJob(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysIndexTools> queryFilter) throws Exception {
		return sysIndexToolsManager.query(queryFilter);
	}

	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "首页工具明细页面", httpMethod = "GET", notes = "首页工具明细页面")
	public @ResponseBody SysIndexTools getJson(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		if(StringUtil.isEmpty(id)){
			return new SysIndexTools();
		}
		SysIndexTools sysIndexTools=sysIndexToolsManager.get(id);
		return sysIndexTools;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存首页工具信息", httpMethod = "POST", notes = "保存首页工具信息")
	public CommonResult<String> save(@ApiParam(name="sysIndexTools", value="首页工具")@RequestBody SysIndexTools sysIndexTools) throws Exception {
		String resultMsg=null;
		String id=sysIndexTools.getId();
		try {
			if(StringUtil.isEmpty(id)){
				sysIndexTools.setId(UniqueIdUtil.getSuid());
				sysIndexToolsManager.create(sysIndexTools);
				resultMsg="添加首页工具成功";
			}else{
				sysIndexToolsManager.update(sysIndexTools);
				resultMsg="更新首页工具成功";
			}
			return new CommonResult<>(true, resultMsg);
		} catch (Exception e) {
			resultMsg="对首页工具操作失败";
			return new CommonResult<>(false, resultMsg+e.getMessage());
		}
	}
	
	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除首页工具记录", httpMethod = "DELETE", notes = "批量删除首页工具记录")
	public CommonResult<String> remove(@ApiParam(name="ids", value="主键", required = true)@RequestParam String ids) throws Exception {
		try {
			String[] aryIds = StringUtil.getStringAryByStr(ids);
			sysIndexToolsManager.removeByIds(aryIds);
			return new CommonResult<>(true, "删除首页工具成功");
		} catch (Exception e) {
			return new CommonResult<>(false, "删除首页工具失败");
		}
	}

	@RequestMapping(value="getRights", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取授权信息", httpMethod = "GET", notes = "获取授权信息")
	public @ResponseBody JsonNode getColumnRights(@ApiParam(name="id" , value="主键", required = true)@RequestParam String id) throws Exception {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		return bpmDefUserManager.getRights(id,SysAuthUser.BPMDEFUSER_OBJ_TYPE.INDEX_TOOLS);
	}

//	@RequestMapping(value="saveRights", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
//	@ApiOperation(value = "保存授权信息", httpMethod = "POST", notes = "保存授权信息")
//	public CommonResult<String> saveRights(@ApiParam(name="id", value="主键", required = true)@RequestParam String id,
//						   		   @ApiParam(name="rightsData", value="权限数据", required = true)@RequestParam String rightsData
//	) throws Exception {
//		try { 
//			String objType = SysIndexTools.INDEX_TOOLS;
//			bpmDefUserManager.saveRights(id, objType, rightsData);
//			return new CommonResult<>(true, "权限保存成功！");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new CommonResult<>(false, "权限保存失败");
//		}
//	}
}
