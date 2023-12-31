package com.hotent.bpmModel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.bpm.persistence.manager.BpmDefUserManager;
import com.hotent.bpm.persistence.model.BpmDefUser;
import com.hotent.bpmModel.params.SaveRightsVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 栏目授权
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月15日
 */
@RestController
@RequestMapping("/flow/bpmDefUser/v1/")
@Api(tags="栏目授权")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmDefUserController extends BaseController<BpmDefUserManager, BpmDefUser> {
	@RequestMapping(value="getRights", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取首页栏目权限", httpMethod = "GET", notes = "获取首页栏目权限")
	public  ArrayNode getRights(
			@ApiParam(name="id",value="id")@RequestParam  String id,
			@ApiParam(name="objType",value="objType")@RequestParam  String objType) throws Exception {
		return baseService.getRights(id, objType);
	}

	@RequestMapping(value="saveRights", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存首页栏目权限", httpMethod = "POST", notes = "保存节点json 配置")
	public CommonResult<String> saveRights(@ApiParam(name="vo",value="节点保存对象")@RequestBody SaveRightsVo rightsVo) throws Exception {
		try {
			baseService.saveRights(rightsVo.getId(), rightsVo.getObjType(), rightsVo.getOwnerNameJson());
			return new CommonResult<String>("保存首页栏目权限成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"保存首页栏目权限失败"+e.getMessage());
		}
	}

	@RequestMapping(value="getAuthorizeIdsByUserMap", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "通过objType获取当前用户权限", httpMethod = "GET", notes = "通过objType获取当前用户权限")
	public List<String> getAuthorizeIdsByUserMap(@ApiParam(name="objType",value="objType")@RequestParam String objType) throws Exception {
		return baseService.getAuthorizeIdsByUserMap(objType);
	}


	@RequestMapping(value="hasRights", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "判断用户对某个模块数据是否有权限", httpMethod = "GET", notes = "判断用户对某个模块数据是否有权限")
	public boolean hasRights(@ApiParam(name="authorizeId",value="authorizeId")@RequestParam String authorizeId) throws Exception {
		return baseService.hasRights(authorizeId);
	}
}
