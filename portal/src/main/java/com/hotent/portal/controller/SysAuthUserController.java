package com.hotent.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.params.SaveRightsVo;
import com.hotent.sys.persistence.manager.SysAuthUserManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.impl.util.PermissionCalc;
import com.hotent.uc.api.model.IUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年7月9日
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/sys/authUser/v1/")
@Api(tags="通用权限设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysAuthUserController extends BaseController {
	@Resource
	SysAuthUserManager sysAuthUserManager;
	@Resource
	PermissionCalc permssionCalc;

	@RequestMapping(value="getRights", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取权限", httpMethod = "GET", notes = "获取权限")
	public  ArrayNode getRights(
			@ApiParam(name="id",value="id")@RequestParam  String id,
			@ApiParam(name="objType",value="objType")@RequestParam  String objType) throws Exception {
		try {
			return sysAuthUserManager.getRights(id, objType);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="getRightsAndDefaultRightType", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取权限和系统默认的权限类型", httpMethod = "GET", notes = "获取权限和系统默认的权限类型")
	public  Map<String, Object> getRightsAndDefaultRightType(
			@ApiParam(name="id",value="id")@RequestParam  String id,
			@ApiParam(name="objType",value="objType")@RequestParam  String objType) throws Exception {
		try {
			Map<String, Object> result = new HashMap<>();
			result.put("right", sysAuthUserManager.getRights(id, objType));
			result.put("type", AppUtil.getBean("defaultObjectRightType"));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value="saveRights", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存权限", httpMethod = "POST", notes = "保存节点json 配置")
	public CommonResult<String> saveRights(@ApiParam(name="vo",value="节点保存对象")@RequestBody SaveRightsVo rightsVo) throws Exception {
		try {
			sysAuthUserManager.saveRights(rightsVo.getId(), rightsVo.getObjType(), rightsVo.getOwnerNameJson());
			IUser currentUser = ContextUtil.getCurrentUser();
			sysAuthUserManager.delUserMenuCache(currentUser.getUserId());
			return new CommonResult<String>("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"保存权限失败"+e.getMessage());
		}
	}

	@RequestMapping(value="getAuthorizeIdsByUserMap", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "通过objType获取当前用户权限", httpMethod = "GET", notes = "通过objType获取当前用户权限")
	public List<String> getAuthorizeIdsByUserMap(@ApiParam(name="objType",value="objType")@RequestParam String objType) throws Exception {
		return sysAuthUserManager.getAuthorizeIdsByUserMap(objType);
	}


	@RequestMapping(value="hasRights", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "判断用户对某个模块数据是否有权限", httpMethod = "GET", notes = "判断用户对某个模块数据是否有权限")
	public boolean hasRights(@ApiParam(name="authorizeId",value="authorizeId")@RequestParam String authorizeId) throws Exception {
		return sysAuthUserManager.hasRights(authorizeId);
	}
	
	@RequestMapping(value="calcPermssion", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据权限数据判断当前人是否有权限", httpMethod = "GET", notes = "根据权限数据判断当前人是否有权限")
	public boolean calcPermssion(@ApiParam(name="permssionJson",value="权限定义json数据")@RequestParam String permssionJson) throws Exception {
		Map<String, Set<String>>  currentMap = permssionCalc.getCurrentProfiles();
		return permssionCalc.hasRight(permssionJson, currentMap);
	}
	
	@RequestMapping(value="calcAllPermssion", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据权限数据批量判断当前人是否有权限", httpMethod = "GET", notes = "根据权限数据批量判断当前人是否有权限")
	public ObjectNode calcAllPermssion(@ApiParam(name="permssionJson",value="权限定义json数据")@RequestParam String permssionJson) throws Exception {
		ObjectNode rightJson = JsonUtil.getMapper().createObjectNode();
		Map<String, Set<String>>  currentMap = permssionCalc.getCurrentProfiles();
		ArrayNode array = (ArrayNode) JsonUtil.toJsonNode(permssionJson);
		for (JsonNode jsonNode : array) {
			ObjectNode node = (ObjectNode) jsonNode;
			String permssionStr = "";
			if(node.get("right").isArray()){
				permssionStr = JsonUtil.toJson(node.get("right"));
			}else{
				permssionStr = node.get("right").asText();
			}
			boolean is = false;
			if(StringUtil.isNotEmpty(permssionStr)){
				ArrayNode ay = (ArrayNode) JsonUtil.toJsonNode(permssionStr);
				for (JsonNode jsonNode2 : ay) {
					 boolean isRight = permssionCalc.hasRight(JsonUtil.toJson(jsonNode2), currentMap);
					 if(isRight){
						 is = true;
						 break;
					 }
				}
			}
            rightJson.put(node.get("fieldName").asText(), is);
		}
		return rightJson;
	}
}
