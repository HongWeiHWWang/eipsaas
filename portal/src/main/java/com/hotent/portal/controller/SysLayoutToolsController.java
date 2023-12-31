package com.hotent.portal.controller;



import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.model.SysLayoutTools;
import com.hotent.portal.persistence.manager.SysLayoutToolsManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



/**
 * 布局工具设置 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author mouhb
 * @email mouhb@jee-soft.cn
 * @date 2017-08-06 20:25:54
 */
@RestController
@RequestMapping("/portal/sysLayoutTools/sysLayoutTools/v1/")
@Api(tags="布局工具设置")
public class SysLayoutToolsController extends BaseController<SysLayoutToolsManager, SysLayoutTools> {
	
	@Resource
	SysLayoutToolsManager sysLayoutToolsManager;
	

	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "布局工具设置列表(分页条件查询)数据", httpMethod = "POST", notes = "布局工具设置列表(分页条件查询)数据")
	public PageList<SysLayoutTools> executeJob(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysLayoutTools> queryFilter) throws Exception {
		return sysLayoutToolsManager.query(queryFilter);
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "布局工具设置明细页面", httpMethod = "GET", notes = "布局工具设置明细页面")
	public @ResponseBody SysLayoutTools getJson(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		if(StringUtil.isEmpty(id)){
			return new SysLayoutTools();
		}
		SysLayoutTools sysLayoutTools=sysLayoutToolsManager.get(id);
		return sysLayoutTools;
	}

	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存布局工具设置信息", httpMethod = "POST", notes = "保存布局工具设置信息")
	public CommonResult<String> save(@ApiParam(name="sysLayoutTools", value="布局工具设置")@RequestBody SysLayoutTools sysLayoutTools) throws Exception {
		String resultMsg=null;
		try {
			SysLayoutTools layoutTools = sysLayoutToolsManager.getByLayoutID(sysLayoutTools.getLayoutId(), sysLayoutTools.getToolsType());
			if(layoutTools == null){
				sysLayoutTools.setId(UniqueIdUtil.getSuid());
				sysLayoutToolsManager.create(sysLayoutTools);
				resultMsg="添加布局工具设置成功";
			}else{
				layoutTools.setToolsIds(sysLayoutTools.getToolsIds());
				sysLayoutToolsManager.update(layoutTools);
				resultMsg="更新布局工具设置成功";
			}
			return new CommonResult<>(true, resultMsg);
		} catch (Exception e) {
			resultMsg="对布局工具设置操作失败";
			return new CommonResult<>(false, resultMsg);
		}
	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除布局工具设置记录", httpMethod = "DELETE", notes = "批量删除布局工具设置记录")
	public CommonResult<String> remove(@ApiParam(name="ids", value="主键", required = true)@RequestParam String ids) throws Exception {
		try{
			String[] aryIds=StringUtil.getStringAryByStr(ids);
			sysLayoutToolsManager.removeByIds(aryIds);
			return new CommonResult<>(true, "删除布局工具设置成功");
		} catch (Exception e) {
			return new CommonResult<>(false, "删除布局工具设置失败");
		}
	}
	
//	@RequestMapping(value="toolsJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
//	@ApiOperation(value = "获取系统主页工具集合", httpMethod = "POST", notes = "获取系统主页工具集合")
//	public PageList<SysIndexTools> toolsJson(@ApiParam(name="layoutId", value="布局id", required = true)@RequestParam String layoutId,
//						   					 @ApiParam(name="toolsType", value="工具类型", required = true)@RequestParam String toolsType
//	) throws Exception {
//		PageList<SysIndexTools> sysIndexToolsList=new PageList<SysIndexTools>(sysLayoutToolsManager.queryTools(layoutId,toolsType));
//		return sysIndexToolsList;
//	}
	
	@RequestMapping(value="moveTool", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "移动布局工具", httpMethod = "POST", notes = "移动布局工具")
	public CommonResult<String> moveTool(@ApiParam(name="layoutId", value="布局id", required = true)@RequestParam String layoutId,
						 @ApiParam(name="toolsType", value="工具类型", required = true)@RequestParam String toolsType,
						 @ApiParam(name="toolId", value="工具id", required = true)@RequestParam String toolId,
						 @ApiParam(name="isMove", value="是否移动", required = true)@RequestParam boolean isMove
	) throws Exception {
		try{
			SysLayoutTools sysLayoutTools = sysLayoutToolsManager.getByLayoutID(layoutId, toolsType);
			String[] toolsArray = sysLayoutTools.getToolsIds().split(",");
			for (int i=0; i < toolsArray.length; i++){
				if(toolsArray[i].equals(toolId)){
					if(isMove){
						// 向前移动
						String pre = toolsArray[i-1];
						toolsArray[i-1] = toolsArray[i];
						toolsArray[i] = pre;
					}else{
						// 向后移动
						String later = toolsArray[i+1];
						toolsArray[i+1] = toolsArray[i];
						toolsArray[i] = later;
					}
					sysLayoutTools.setToolsIds(StringUtils.join(toolsArray, ","));
					sysLayoutToolsManager.update(sysLayoutTools);
					break;
				}
			}
			return new CommonResult<>(true, "移动成功");
		}catch(Exception e){
			return new CommonResult<>(false, "移动失败", e.getMessage());
		}
	}
	
	@RequestMapping(value="deleteTool", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除工具", httpMethod = "DELETE", notes = "删除工具")
	public CommonResult<String> deleteTool(@ApiParam(name="layoutId", value="布局id", required = true)@RequestParam String layoutId,
						   		   		   @ApiParam(name="toolsType", value="工具类型", required = true)@RequestParam String toolsType,
						   		   		   @ApiParam(name="toolId", value="工具id", required = true)@RequestParam String toolId
	) throws Exception {
		try{
			SysLayoutTools sysLayoutTools = sysLayoutToolsManager.getByLayoutID(layoutId, toolsType);
			String[] toolsArray = sysLayoutTools.getToolsIds().split(",");
			toolsArray = (String[]) ArrayUtils.removeElement(toolsArray, toolId);
			sysLayoutTools.setToolsIds(StringUtils.join(toolsArray, ","));
			sysLayoutToolsManager.update(sysLayoutTools);
			return new CommonResult<>(true, "删除成功"); 
		}catch(Exception e){
			return new CommonResult<>(false, "删除失败", e.getMessage());
		}
	}
}
