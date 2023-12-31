package com.hotent.portal.controller;

import java.util.HashMap;
import java.util.List;

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
import com.hotent.base.util.StringUtil;
import com.hotent.sys.persistence.manager.SysRoleAuthManager;
import com.hotent.sys.persistence.model.SysRoleAuth;
import com.hotent.sys.persistence.param.SysRoleAuthParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 
 * <pre> 
 * 描述：角色授权 控制器类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-07-3 09:29:59
 * 版权：广州宏天软件有限公司
 * </pre>
 */ 
@RestController
@RequestMapping(value="/sys/sysRoleAuth/v1")
@Api(tags="角色授权")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysRoleAuthController extends BaseController<SysRoleAuthManager, SysRoleAuth>{
	@Resource
	SysRoleAuthManager sysRoleAuthManager;
	
	@RequestMapping(value="save", method=RequestMethod.POST)
	@ApiOperation(value = "保存角色授权信息", httpMethod = "POST", notes = "保存角色授权信息")
	public CommonResult<String> save(@ApiParam(name="sysRoleAuthParam", value="角色授权", required = true)@RequestBody SysRoleAuthParam sysRoleAuthParam) throws Exception {
		String resultMsg=null;
		sysRoleAuthManager.create(sysRoleAuthParam);
		resultMsg="角色授权成功";
		return new CommonResult<String>(true, resultMsg);
	}
	
	@RequestMapping(value="saveRoleMethods", method=RequestMethod.POST)
	@ApiOperation(value = "保存角色接口授权信息", httpMethod = "POST", notes = "保存角色接口授权信息")
	public CommonResult<String> saveRoleMethods(@ApiParam(name="sysRoleAuthParam", value="角色授权", required = true)@RequestBody SysRoleAuthParam sysRoleAuthParam) throws Exception {
		String resultMsg=null;
		sysRoleAuthManager.saveRoleMethods(sysRoleAuthParam);
		resultMsg="角色授权成功";
		return new CommonResult<String>(true, resultMsg);
	}
	
	@RequestMapping(value="getByRoleAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取角色授权信息", httpMethod = "GET", notes = "获取角色授权信息")
	public @ResponseBody CommonResult<List<SysRoleAuth>> getByRoleAlias(@ApiParam(name="roleAlias", value="角色别名", required = true)@RequestParam String roleAlias) throws Exception {
		List<SysRoleAuth> sysRoleAuthByRoleAlias = sysRoleAuthManager.getSysRoleAuthByRoleAlias(roleAlias);
		CommonResult<List<SysRoleAuth>> commonResult = new CommonResult<List<SysRoleAuth>>();
		commonResult.setValue(sysRoleAuthByRoleAlias);
		return commonResult;
	}
	
	@RequestMapping(value="removeRoleMethods", method=RequestMethod.DELETE)
	@ApiOperation(value = "批量删除角色接口授权记录", httpMethod = "DELETE", notes = "批量删除角色授权记录a,b,c")
	public CommonResult<String> removeRoleMethods(@ApiParam(name="roleAlias", value="角色别名", required = true)@RequestParam String roleAlias,@ApiParam(name="methodAlias", value="接口别名", required = true)@RequestParam String methodAlias) throws Exception {
		String[] methodAliasArr=StringUtil.getStringAryByStr(methodAlias);
		sysRoleAuthManager.removeRoleMethods(roleAlias,methodAliasArr);
		return new CommonResult<String>(true, "删除角色接口授权成功");
	}

	@RequestMapping(value="removeByRoleAlias", method=RequestMethod.DELETE)
	@ApiOperation(value = "批量删除角色授权记录", httpMethod = "DELETE", notes = "批量删除角色授权记录a,b,c")
	public CommonResult<String> removeByRoleAlias(@ApiParam(name="roleAlias", value="角色别名", required = true)@RequestParam String roleAlias) throws Exception {
		String[] aryroleAlias=StringUtil.getStringAryByStr(roleAlias);
		sysRoleAuthManager.removeByArrRoleAlias(aryroleAlias);
		return new CommonResult<String>(true, "删除角色授权成功");
	}
	
	@RequestMapping(value="getMethodRoleAuth", method=RequestMethod.GET)
	@ApiOperation(value = "获取角色跟请求地址的关系", httpMethod = "GET", notes = "获取角色跟请求地址的关系")
	public List<HashMap<String,String>> getMethodRoleAuth() throws Exception {
		return sysRoleAuthManager.getSysRoleAuthAll();
	}

	@RequestMapping(value="saveCopy", method=RequestMethod.POST)
	@ApiOperation(value = "保存角色权限复制信息", httpMethod = "POST", notes = "保存角色权限复制信息")
	public CommonResult<String> saveCopy(@ApiParam(name="oldCode", value="原角色别名", required = true)@RequestParam String oldCode,
										  @ApiParam(name="newCodes", value="新角色别名，多个逗号隔开", required = true)@RequestParam String newCodes) throws Exception {
		String resultMsg=null;
		String[] str = newCodes.split(",");

		sysRoleAuthManager.createCopy(oldCode,str);
		resultMsg="角色权限复制成功";
		return new CommonResult<String>(true, resultMsg);
	}
}
