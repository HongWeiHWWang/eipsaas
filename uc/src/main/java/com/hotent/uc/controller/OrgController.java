package com.hotent.uc.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.dao.OrgUserDao;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgAuthManager;
import com.hotent.uc.manager.OrgJobManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.OrgParamsManager;
import com.hotent.uc.manager.OrgPostManager;
import com.hotent.uc.manager.OrgRoleManager;
import com.hotent.uc.manager.OrgUserManager;
import com.hotent.uc.manager.UserImportManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserUnderManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.model.OrgParams;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgRole;
import com.hotent.uc.model.OrgTree;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserUnder;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.group.GroupIdentity;
import com.hotent.uc.params.org.OaAsyncObject;
import com.hotent.uc.params.org.OrgAuthVo;
import com.hotent.uc.params.org.OrgPostVo;
import com.hotent.uc.params.org.OrgUserVo;
import com.hotent.uc.params.org.OrgVo;
import com.hotent.uc.params.orgRole.OrgRoleVo;
import com.hotent.uc.params.post.PostDueVo;
import com.hotent.uc.params.user.UserUnderVo;
import com.hotent.uc.params.user.UserVo;
import com.hotent.uc.util.AuthFilterUtil;
import com.hotent.uc.util.ContextUtil;
import com.hotent.uc.util.OrgUtil;
import com.hotent.uc.util.UpdateMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 组织模块接口
 * 
 * @author liangqf
 *
 */
@RestController
@RequestMapping("/api/org/v1/")
@Api(tags = "组织管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_UC})
public class OrgController extends BaseController<OrgManager, Org> {

	@Autowired
	OrgManager orgService;
	@Autowired
	DemensionManager demService;
	@Autowired
	OrgPostManager postService;
	@Autowired
	OrgUserManager orgUserService;
	@Autowired
	UserUnderManager userUnderService;
	@Autowired
	UserImportManager userImportService;
	@Resource
	OrgRoleManager orgRoleService;
	@Autowired
	OrgAuthManager orgAuthService;
	@Autowired
	OrgJobManager orgJobManager;
	@Autowired
	OrgUserManager orgUserManager;
	@Autowired
	UserManager userService;
	@Resource
	OrgParamsManager orgParamsService;
	@Resource
    OrgUserDao orgUserDao;

    /**
     * 根据组织id和用户id删除组织下的用户，并岗位id为空
     * @param orgId,userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "orgUser/deleteOrgById", method = RequestMethod.DELETE, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据组织id和用户id删除组织下的用户，并岗位id为空", httpMethod = "DELETE", notes = "根据组织id和用户id删除组织下的用户，并岗位id为空")
    public CommonResult<String> deleteOrgById(
            @ApiParam(name = "orgId", value = "组织id", required = true) @RequestParam
                    String orgId,
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) throws Exception {
        return orgUserService.deleteOrgById(orgId,userId);
    }

	@RequestMapping(value = "orgs/getOrgPage", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织列表（带分页信息）", httpMethod = "POST", notes = "获取组织列表")
	public PageList<Org> getOrgPage(
			@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter<Org> queryFilter) throws Exception {
		PageList<Org> list = orgService.query(queryFilter);
		return list;
	}

	/**
	 * 添加组织
	 * 
	 * @param orgVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "org/addOrg", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "添加组织", httpMethod = "POST", notes = "添加组织")
	public CommonResult<String> addOrg(
			@ApiParam(name = "orgVo", value = "组织视图", required = true) @RequestBody OrgVo orgVo) throws Exception {
		CommonResult<String> rtn = null;
		try {
			rtn = orgService.addOrg(orgVo);
		} catch (Exception e) {
			rtn = new CommonResult<String>(false,e.getMessage(),"保存失败");
		}
		return rtn;
	}

	/**
	 * 根据组织编码删除组织
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "org/deleteOrg", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据组织编码删除组织", httpMethod = "POST", notes = "根据组织编码删除组织（多个用,号隔开），连同其子组织、组织参数、岗位、组织人员关系及对应下属一起删除")
	public CommonResult<String> deleteOrg(
			@ApiParam(name = "codes", value = "组织编码", required = true) @RequestBody String codes) throws Exception {
		CommonResult<String> rtn = null;
		try {
			rtn = orgService.deleteOrg(codes);
		} catch (Exception e) {
			e.printStackTrace();
			rtn = new CommonResult<String>(false,e.getMessage(),"保存失败");
		}
		return rtn;
	}

	/**
	 * 修改组织
	 * 
	 * @param orgVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "org/updateOrg", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "修改组织", httpMethod = "POST", notes = "修改组织")
	@UpdateMethod(type=OrgVo.class)
	public CommonResult<String> updateOrg(
			@ApiParam(name = "orgVo", value = "组织视图", required = true) @RequestBody OrgVo orgVo) throws Exception {
		return orgService.updateOrg(orgVo);
	}

	/**
	 * 根据组织编码获取组织
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "org/getOrg", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据组织编码获取组织", httpMethod = "GET", notes = "根据组织编码获取组织")
	public Org getOrg(@ApiParam(name = "code", value = "组织编码", required = true) @RequestParam String code)
			throws Exception {
		Org org = orgService.getOrg(code);
		Map<String, Object> map = new HashMap<>();
		map.put("orgCode", code);
		map.put("group", "true");
		List<Map<String, Object>> list = orgUserDao.getUserNumByOrgCode(map);
		org.setNowNum(list.size());
		return org;
	}
	
	/**
	 * 根据组织编码获取组织限编
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "org/getOrgLimitByCodes", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据组织编码获取组织限编", httpMethod = "GET", notes = "根据组织编码获取组织限编")
	public PageList<Org> getOrgLimitByCodes(@ApiParam(name = "codes", value = "组织编码", required = true) @RequestParam String codes)
			throws Exception {
		QueryFilter queryFilter = QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
		queryFilter.addFilter("code", codes, QueryOP.IN,FieldRelation.AND,"group_1");
		PageList<Org> query = orgService.query(queryFilter);
		if (BeanUtils.isNotEmpty(query) && BeanUtils.isNotEmpty(query.getRows())) {
			for (Org org : query.getRows()) {
				Map<String, Object> map = new HashMap<>();
				map.put("orgCode", org.getCode());
				map.put("group", "true");
				List<Map<String, Object>> list = orgUserDao.getUserNumByOrgCode(map);
				org.setNowNum(list.size());
			}
		}
		return query;
	}
	
	/**
	 * 根据组织id获取组织
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "org/get", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据组织id获取组织", httpMethod = "GET", notes = "根据组织id获取组织")
	public Org get(@ApiParam(name = "id", value = "组织id", required = true) @RequestParam String id)
			throws Exception {
		Org org = orgService.get(id);
		if(BeanUtils.isEmpty(org)){
			org = orgService.getByCode(id);
		}
		return org;
	}
	
	/**
	 * 获取当前用户主组织
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "org/getOrgMaster", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据当前用户主组织", httpMethod = "GET", notes = "根据当前用户主组织")
	public Org getOrgMaster()
			throws Exception {
		return orgService.getOrgMaster(ContextUtil.getCurrentUser().getAccount());
		
	}

	/**
	 * 保存组织参数
	 * 
	 * @param orgCode
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgParam/saveOrgParams", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "保存组织参数", httpMethod = "POST", notes = "保存组织参数，参数params格式[{\"alias\":\"a1\",\"value\":\"v1\"},{\"alias\":\"a2\",\"value\":\"v2\"}]")
	public CommonResult<String> saveOrgParams(
			@ApiParam(name = "orgCode", value = "组织编码", required = true) @RequestParam String orgCode,
			@ApiParam(name = "params", value = "参数值", required = true) @RequestBody List<ObjectNode> params)
			throws Exception {
		CommonResult<String> rtn = null;
		try {
			rtn = orgService.saveOrgParams(orgCode, params);
		} catch (Exception e) {
			rtn = new CommonResult<String>(false,"保存失败,请将参数填写完整",e.getMessage());
		}
		return rtn;
	}

	/**
	 * 获取组织参数
	 * 
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgParam/getOrgParams", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织参数", httpMethod = "GET", notes = "根据组织编码获取组织参数")
	public List<OrgParams> getOrgParams(
			@ApiParam(name = "orgCode", value = "组织编码", required = true) @RequestParam String orgCode)
			throws Exception {
		return orgService.getOrgParams(orgCode);
	}

	/**
	 * 获取指定组织参数
	 * 
	 * @param orgCode
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgParam/getParamByAlias", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定组织参数", httpMethod = "GET", notes = "根据组织编码和参数别名获取组织参数")
	public CommonResult<OrgParams> getParamByAlias(
			@ApiParam(name = "orgCode", value = "组织代码", required = true) @RequestParam String orgCode,
			@ApiParam(name = "alias", value = "参数代码", required = true) @RequestParam String alias) throws Exception {
		return orgService.getParamByAlias(orgCode, alias);
	}

	/**
	 * 加入用户
	 * 
	 * @param orgUserVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgUser/addOrgUser", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "加入用户", httpMethod = "POST", notes = "向组织中加入系统已有的用户")
	public CommonResult<String> addOrgUser(
			@ApiParam(name = "orgUserVo", value = "组织用户", required = true) @RequestBody OrgUserVo orgUserVo)
			throws Exception {
		return orgService.addOrgUser(orgUserVo);
	}

    /**
     * 新增用户是给用户设置组织
     *
     * @param account,orgCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "orgUser/addOrgUserByCode", method = RequestMethod.POST, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "新增用户是给用户设置组织", httpMethod = "POST", notes = "新增用户是给用户设置组织")
    public CommonResult<String> addOrgUserByCode(
            @ApiParam(name = "account", value = "组织用户", required = true) @RequestParam String account,
            @ApiParam(name = "orgCode", value = "组织编码", required = true) @RequestParam String orgCode)
            throws Exception {
        String[] orgCodes = orgCode.split(",");

        for (String code : orgCodes) {
            OrgUserVo orgUserVo = new OrgUserVo();
            orgUserVo.setAccount(account);
            orgUserVo.setOrgCode(code);
            orgService.addOrgUser(orgUserVo);
        }

        return new CommonResult<String>(true, "设置成功", "");
    }

	/**
	 * 用户取消加入组织
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgUser/delOrgUser", method = RequestMethod.DELETE, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "用户取消加入组织", httpMethod = "DELETE", notes = "用户取消加入组织，ids为用户组织关系id，多个用英文逗号隔开")
	public CommonResult<String> delOrgUser(
			@ApiParam(name = "ids", value = "用户组织关系id字符串", required = true) @RequestParam String ids) throws Exception {
		return orgService.delOrgUser(ids);
	}

	/**
	 * 判断用户在某维度下是否有主组织
	 * 
	 * @param account
	 * @param demCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgUser/getUserIsMaster", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "判断用户是否有主组织", httpMethod = "GET", notes = "判断用户是否有主组织")
	public CommonResult<Boolean> getUserIsMaster(
			@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account,
			@ApiParam(name = "demCode", value = "维度编码", required = true) @RequestParam String demCode)
			throws Exception {
		return orgService.getUserIsMaster(account, demCode);
	}

	/**
	 * 设置人员（取消）主岗位
	 * 
	 * @param account
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPost/setMaster", method = RequestMethod.PUT, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "设置人员（取消）主岗位", httpMethod = "PUT", notes = "设置人员（取消）主岗位")
	public CommonResult<String> setMaster(
			@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account,
			@ApiParam(name = "postCode", value = "岗位编码", required = true) @RequestParam String postCode)
			throws Exception {
		return orgService.setMaster(account, postCode);
	}

	/**
	 * 获取组织树
	 * 
	 * @param demCode
	 * @param pOrgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgs/getTreeDataByDem", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织树", httpMethod = "GET", notes = "获取组织树")
	public List<OrgTree> getTreeDataByDem(
			@ApiParam(name = "demCode", value = "维度编码", required = true) @RequestParam String demCode,
			@ApiParam(name = "pOrgCode", value = "父组织编码",required=false) @RequestParam(required=false) String pOrgCode) throws Exception {
		return orgService.getTreeDataByDem(demCode, pOrgCode);
	}

	/**
	 * 设置（取消）（主）负责人
	 * 
	 * @param account
	 * @param orgCode
	 * @param isCharge
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgUser/setOrgCharge", method = RequestMethod.PUT, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "设置（取消）（主）负责人", httpMethod = "PUT", notes = "isCharge为true表示设置主负责人，为false时，若此时为负责人，则降为非负责人")
	public CommonResult<String> setOrgCharge(
			@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account,
			@ApiParam(name = "orgCode", value = "组织编码", required = true) @RequestParam String orgCode,
			@ApiParam(name = "isCharge", value = "是否设置主负责人", required = true) @RequestParam Boolean isCharge)
			throws Exception {
		return orgService.setOrgCharge(account, orgCode, isCharge);
	}

	/**
	 * 组织人员添加下属
	 * 
	 * @param userUnder
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userUnder/addUserUnders", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "组织人员添加下属", httpMethod = "POST", notes = "组织人员添加下属")
	public CommonResult<String> addUserUnders(
			@ApiParam(name = "userUnder", value = "下属用户", required = true) @RequestBody UserUnderVo userUnder)
			throws Exception {
		return orgService.addUserUnders(userUnder);
	}

	/**
	 * 组织人员删除下属
	 * 
	 * @param account
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userUnder/delUserUnders", method = RequestMethod.DELETE, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "组织人员删除下属", httpMethod = "DELETE", notes = "组织人员删除下属")
	public CommonResult<String> delUserUnders(
			@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account,
			@ApiParam(name = "orgCode", value = "组织编码", required = true) @RequestParam String orgCode)
			throws Exception {
		return orgService.delUserUnders(account, orgCode);
	}

	/**
	 * 获取用户在某组织下的下属
	 * 
	 * @param account
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userUnder/getUserUnders", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户在某组织下的下属", httpMethod = "GET", notes = "获取用户在某组织下的下属")
	public List<UserVo> getUserUnders(
			@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account,
			@ApiParam(name = "orgCode", value = "组织编码", required = true) @RequestParam String orgCode)
			throws Exception {
		return orgService.getUserUnders(account, orgCode);
	}
	
	/**
	 * 获取用户在某组织下的下属(含分页)
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userUnder/getUserUndersPage", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户在某组织下的下属(含分页)", httpMethod = "POST", notes = "获取用户在某组织下的下属(含分页)",hidden=true)
	public PageList<UserUnder> getUserUndersPage(
			@ApiParam(name = "filter", value = "查询对象", required = true) @RequestBody QueryFilter filter)
			throws Exception {
		Page<UserUnder> list = (Page<UserUnder>)userUnderService.getUserUnder(filter);
		return new PageList<UserUnder>(list);
	}
	
	@RequestMapping(value = "userUnder/delUnderUserByIds", method = RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据id删除下属", httpMethod = "DELETE", notes = "根据id删除下属，多个用户逗号隔开",hidden=true)
	public CommonResult<String> delUnderUserByIds(@ApiParam(name="ids",value="记录id字符串",required=true) @RequestParam String ids) throws Exception{
		String[] idArr = ids.split(",");
		userUnderService.removeByIds(idArr);
		return new CommonResult<String>(true,"删除成功！","");
	}

	/**
	 * 添加分级管理
	 * 
	 * @param orgAuthVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgAuth/saveOrgAuth", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "添加分级管理", httpMethod = "POST", notes = "添加分级管理")
	public CommonResult<String> saveOrgAuth(
			@ApiParam(name = "orgAuthVo", value = "分级组织管理", required = true) @RequestBody OrgAuthVo orgAuthVo)
			throws Exception {
		CommonResult<String> rtn = null;
		try {
			rtn = orgService.saveOrgAuth(orgAuthVo);
		} catch (Exception e) {
			rtn = new CommonResult<String>(false,e.getMessage(),"保存失败");
		}
		return rtn;
	}

	/**
	 * 删除分级管理
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgAuth/deleteOrgAuth", method = RequestMethod.DELETE, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "删除分级管理", httpMethod = "DELETE", notes = "删除分级管理")
	public CommonResult<String> deleteOrgAuth(
			@ApiParam(name = "id", value = "分级管理id", required = true) @RequestParam String id) throws Exception {
		return orgService.deleteOrgAuth(id);
	}

	/**
	 * 获取分级管理列表
	 * 
	 * @param account
	 * @param orgCode
	 * @param demCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgAuths/getOrgAuthList", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取分级管理列表", httpMethod = "GET", notes = "获取分级管理列表")
	public PageList<OrgAuth> getOrgAuthList(
			@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account,
			@ApiParam(name = "orgCode", value = "组织编码", required = false) @RequestParam(required=false) String orgCode,
			@ApiParam(name = "demCode", value = "维度编码", required = false) @RequestParam(required=false) String demCode)
			throws Exception {
		Page<OrgAuth> list = orgService.getOrgAuthList(account, orgCode, demCode);
		return new PageList<OrgAuth>(list);
	}

	/**
	 * 获取分级管理
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgAuth/getOrgAuth", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取分级管理", httpMethod = "GET", notes = "根据id获取分级管理")
	public OrgAuth getOrgAuth(@ApiParam(name = "id", value = "分级管理id", required = true) @RequestParam String id)
			throws Exception {
		return orgService.getOrgAuth(id);
	}

	/**
	 * 获取子组织
	 * 
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgs/getByParentId", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取子组织", httpMethod = "GET", notes = "根据父组织id获取子组织")
	public List<Org> getByParentId(
			@ApiParam(name = "parentId", value = "父组织id", required = true) @RequestParam String parentId)
			throws Exception {
		if(BeanUtils.isEmpty(orgService.get(parentId))){
			throw new RuntimeException("根据组织id【"+parentId+"】没有找到对应的组织");
		}
		return orgService.getByParentId(parentId);
	}
	/**
	 * 获取子组织(只获取底下一层子组织)
	 *
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgs/getOrgsByparentId", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取子组织(只获取底下一层子组织)", httpMethod = "GET", notes = "根据父组织id获取子组织")
	public List<Org> getOrgsByparentId(@ApiParam(name = "parentId", value = "父组织id", required = true) @RequestParam String parentId)
					throws Exception {
		return orgService.getOrgsByparentId(parentId);
	}

	/**
	 * 获取用户所属（主）组织
	 * 
	 * @param account
	 * @param demCode
	 * @param isMain
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgs/getUserOrgs", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所属（主）组织", httpMethod = "GET", notes = "获取用户所属（主）组织")
	public List<Org> getUserOrgs(
			@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account,
			@ApiParam(name = "demCode", value = "维度编码", required = false) @RequestParam(required=false) String demCode,
			@ApiParam(name = "isMain", value = "是否主组织", required = false) @RequestParam(required=false) Boolean isMain)
			throws Exception {
		return orgService.getUserOrgs(account, demCode, isMain);
	}
	
	@RequestMapping(value = "orgs/getOrgsByAccount", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所属组织", httpMethod = "POST", notes = "获取用户所属组织")
	public List<Org> getOrgsByAccount(@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account) throws Exception{
		return orgService.getOrgsByAccount(account);
	}
	
	@RequestMapping(value = "orgs/getOrgListByUserId", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所属组织", httpMethod = "GET", notes = "获取用户所属组织")
	public List<Org> getOrgListByUserId(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) throws Exception{
		return orgService.getOrgListByUserId(userId);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "orgs/getUserOrgPage", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所属组织", httpMethod = "POST", notes = "获取用户所属组织")
	public PageList<HashMap<String,Object>> getUserOrgPage(@ApiParam(name = "filter", value = "查询对象", required = true) @RequestBody QueryFilter filter) throws Exception{
		IPage<HashMap<String,Object>> p = orgUserService.getUserOrgPage(filter);
		return  new PageList<HashMap<String,Object>>(p);
	}

	/**
	 * 获取组织下的人员
	 * 
	 * @param orgCodes
	 * @param isMain
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgUsers/getUsersByOrgCodes", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 获取组织下的人员", httpMethod = "GET", notes = " 获取组织下的人员，orgCodes组织编码多个用英文逗号隔开")
	public List<UserVo> getUsersByOrgCodes(
			@ApiParam(name = "orgCodes", value = "组织编码", required = true) @RequestParam String orgCodes,
			@ApiParam(name = "isMain", value = "是否主岗位", required = false) @RequestParam(required=false) Boolean isMain)
			throws Exception {
		return orgService.getUsersByOrgCodes(orgCodes, isMain);
	}
	
	/**
	 * 根据组织ID获取组织的负责人组织关系
	 * 
	 * @param orgCodes
	 * @param isMain
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgUsers/getChargesByOrgId", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据组织ID获取组织的负责人组织关系", httpMethod = "GET", notes = "根据组织ID获取组织的负责人组织关系")
	public List<User> getChargesByOrgId(
			@ApiParam(name = "orgId", value = "组织id", required = true) @RequestParam String orgId,
			@ApiParam(name = "isMain", value = "是否主组织", required = false) @RequestParam(required=false) Boolean isMain,
			@ApiParam(name = "demCode", value = "维度编码（不传则为默认维度）", required = false) @RequestParam(required=false) Boolean demCode)
			throws Exception {
		List<User> users = new ArrayList<User>();
		Org org = orgService.get(orgId);
		if(BeanUtils.isEmpty(org)){
			org = orgService.getByCode(orgId);
			if(BeanUtils.isNotEmpty(org)){
				orgId = org.getId();
			}
		}
		List<OrgPost> mainPost = postService.getRelCharge(orgId, true);
		if(BeanUtils.isNotEmpty(mainPost)){
			users = userService.getListByPostId(mainPost.get(0).getId());
		}else{
			List<OrgUser> orgUsers = orgUserManager.getChargesByOrgId(orgId, isMain);
			if(BeanUtils.isNotEmpty(orgUsers)){
				Set<String> userIds = new HashSet<String>();
				for (OrgUser orgUser : orgUsers) {
					if(!userIds.contains(orgUser.getUserId())){
						User user = userService.get(orgUser.getUserId());
						if(BeanUtils.isNotEmpty(user)){
							userIds.add(orgUser.getUserId());
							users.add(user);
						}
					}
				}
			}
		}
		return users;
	}
	

	/**
	 * 获取组织下的岗位
	 * 
	 * @param orgCodes
	 * @param isMain
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPosts/getPostsByOrgCodes", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 获取组织下的岗位", httpMethod = "GET", notes = " 获取组织下的岗位，orgCodes组织编码多个用英文逗号隔开")
	public List<OrgPost> getPostsByOrgCodes(
			@ApiParam(name = "orgCodes", value = "组织编码", required = true) @RequestParam String orgCodes,
			@ApiParam(name = "isMain", value = "是否主岗位", required = false) @RequestParam(required=false) Boolean isMain)
			throws Exception {
		return orgService.getPostsByOrgCodes(orgCodes, isMain);
	}
	
	/**
	 * 获取岗位列表（分页）
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPosts/getOrgPostPage", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取岗位列表（分页）", httpMethod = "POST", notes = "获取岗位列表（分页）")
	public PageList<OrgPost> getOrgPostPage(@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception {
		//queryFilter.setClazz(OrgPost.class);
		//添加权限语句
		String postAuthSql = AuthFilterUtil.getPostAuthSql();
		if(StringUtil.isNotEmpty(postAuthSql)){
			queryFilter.addParams("authSql", postAuthSql);
		}
		return postService.getOrgPost(queryFilter);
	}

	/**
	 * 根据级别获取组织
	 * 
	 * @param level
	 * @param demCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgs/getByLevel", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 根据级别获取组织", httpMethod = "GET", notes = " 根据级别获取组织")
	public List<Org> getByLevel(@ApiParam(name = "level", value = "组织级别", required = true) @RequestParam String level,
			@ApiParam(name = "demCode", value = "维度编码",required=false) @RequestParam(required=false) String demCode)
			throws Exception {
		return orgService.getByLevel(level, demCode);
	}

	/**
	 * 用户加入到岗位
	 * 
	 * @param accounts
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userPost/saveUserPost", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 用户加入到岗位", httpMethod = "POST", notes = " 用户加入到岗位，accounts用户帐号，多个用英文逗号隔开")
	public CommonResult<String> saveUserPost(
			@ApiParam(name = "accounts", value = "用户帐号", required = true) @RequestParam String accounts,
			@ApiParam(name = "postCode", value = "岗位编码", required = true) @RequestParam String postCode)
			throws Exception {
		return orgService.saveUserPost(accounts, postCode);
	}
	
	/**
	 * 用户加入到岗位
	 * 
	 * @param accounts
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userPost/saveUserPosts", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 用户加入到岗位", httpMethod = "POST", notes = " 用户加入到岗位，多个用英文逗号隔开")
	public CommonResult<String> saveUserPosts(
			@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account,
			@ApiParam(name = "postCodes", value = "岗位编码", required = true) @RequestParam String postCodes)
			throws Exception {
		return orgService.saveUserPosts(account, postCodes);
	}

	/**
	 * 用户退出岗位
	 * 
	 * @param accounts
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userPost/delUserPost", method = RequestMethod.DELETE, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 用户退出岗位", httpMethod = "DELETE", notes = " 用户退出岗位，accounts用户帐号，多个用英文逗号隔开")
	public CommonResult<String> delUserPost(
			@ApiParam(name = "accounts", value = "用户帐号", required = true) @RequestParam String accounts,
			@ApiParam(name = "postCode", value = "岗位编码", required = true) @RequestParam String postCode)
			throws Exception {
		return orgService.delUserPost(accounts, postCode);
	}

	/**
	 * 组织添加岗位
	 * 
	 * @param orgPostVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPost/saveOrgPost", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 组织添加岗位", httpMethod = "POST", notes = " 组织添加岗位")
	public CommonResult<String> saveOrgPost(
			@ApiParam(name = "orgPostVo", value = "组织岗位", required = true) @RequestBody OrgPostVo orgPostVo)
			throws Exception {
		CommonResult<String> rtn = null;
		try {
			rtn = orgService.saveOrgPost(orgPostVo);
		} catch (Exception e) {
			e.printStackTrace();
			rtn = new CommonResult<String>(false,e.getMessage(),"保存失败");
		}
		return rtn;
	}
	
	/**
	 * 更新岗位
	 * @param orgPostVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPost/updateOrgPost", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "更新岗位", httpMethod = "POST", notes = "更新岗位")
	public CommonResult<String> updateOrgPost(
			@ApiParam(name = "orgPostVo", value = "组织岗位", required = true) @RequestBody OrgPostVo orgPostVo)
			throws Exception {
		CommonResult<String> rtn = null;
		try {
			rtn = orgService.updateOrgPost(orgPostVo);
		} catch (Exception e) {
			e.printStackTrace();
			rtn = new CommonResult<String>(false,e.getMessage(),"保存失败");
		}
		return rtn;
	}
	
	/**
	 * 根据岗位编码获取岗位信息
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPost/getOrgPost", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 根据岗位id或编码获取岗位信息", httpMethod = "GET", notes = " 根据岗位编码获取岗位信息")
	public CommonResult<OrgPost> getOrgPost(@ApiParam(name="postCode",value="岗位代码",required=true) @RequestParam String postCode) throws Exception{
		OrgPost p = postService.getByCode(postCode);
		if(BeanUtils.isEmpty(p)){
			p = postService.get(postCode);
		}
		return new CommonResult<OrgPost>(true, "获取岗位成功", p);
	}
	
	/**
	 * 根据用户账号获取所属岗位列表
	 * @param account
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPost/getOrgPostByUserAccount", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = " 根据用户账号获取所属岗位", httpMethod = "GET", notes = " 根据用户账号获取所属岗位")
	public List<OrgPost> getOrgPostByUserAccount(@ApiParam(name="account",value="用户账号",required=true) @RequestParam String account) throws Exception {
		List<OrgPost> list = postService.getListByAccount(account, null);
		return list;
	}

	/**
	 * 删除组织岗位，连同岗位下的人员信息一起删除
	 * 
	 * @param postCodes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPost/deleteOrgPost", method = RequestMethod.DELETE, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 删除组织岗位", httpMethod = "DELETE", notes = " 删除组织岗位，连同岗位下的人员信息一起删除，postCodes岗位编码，多个用英文逗号隔开")
	public CommonResult<String> deleteOrgPost(
			@ApiParam(name = "postCodes", value = "岗位编码", required = true) @RequestParam String postCodes)
			throws Exception {
		return orgService.deleteOrgPost(postCodes);
	}

	/**
	 * 设置组织（取消）责任岗位
	 * 
	 * @param postCode
	 * @param isMain
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPost/setPostMaster", method = RequestMethod.PUT, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 设置组织（取消）责任岗位", httpMethod = "PUT", notes = " 设置组织（取消）责任岗位")
	public CommonResult<String> setPostMaster(
			@ApiParam(name = "postCode", value = "岗位编码", required = true) @RequestParam String postCode,
			@ApiParam(name = "isMain", value = "是否责任岗位", required = true) @RequestParam Boolean isMain)
			throws Exception {
		return orgService.setPostMaster(postCode, isMain);
	}

	/**
	 * 设置人员岗位有效期
	 * 
	 * @param postDueVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userPost/setUserPostDueTime", method = RequestMethod.PUT, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 设置人员岗位有效期", httpMethod = "PUT", notes = " 设置人员岗位有效期")
	public CommonResult<String> setUserPostDueTime(
			@ApiParam(name = "postDueVo", value = "人员岗位有效期", required = true) @RequestBody PostDueVo postDueVo)
			throws Exception {
		return orgService.setUserPostDueTime(postDueVo);
	}

	/**
	 * 校验所有用户岗位是否有效
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userPost/validOrgUser", method = RequestMethod.PUT, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 校验所有用户岗位是否有效", httpMethod = "PUT", notes = " 校验所有用户岗位是否有效")
	public CommonResult<String> validOrgUser() throws Exception {
		return orgService.validOrgUser();
	}
	
	/**
	 * 组织添加角色
	 * @param orgRoleVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgRole/addOrgRole", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 组织添加角色", httpMethod = "POST", notes = " 组织添加角色")
	public CommonResult<String> addOrgRole(@ApiParam(name="orgRoleVo",value="组织角色",required=true) @RequestBody OrgRoleVo orgRoleVo) throws Exception{
		orgService.addOrgRole(orgRoleVo);
		return new CommonResult<>(true,"添加成功","");
	}
	
	/**
	 * 删除组织指定角色
	 * @param orgCode
	 * @param roleCodes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgRole/delOrgRoleByCode", method = RequestMethod.DELETE, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 删除组织指定角色", httpMethod = "DELETE", notes = " 删除组织指定角色，roleCodes角色编码多个用英文逗号隔开")
	public CommonResult<String> delOrgRoleByCode(
			@ApiParam(name = "orgCode", value = "组织编码", required = true) @RequestParam String orgCode,
			@ApiParam(name = "roleCodes", value = "角色编码", required = true) @RequestParam String roleCodes)
			throws Exception {
		orgService.delOrgRoleByCode(orgCode, roleCodes);
		return new CommonResult<String>(true, "删除成功！", "");
	}
	
	/**
	 * 删除组织所拥有的角色
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgRole/delAllOrgRole", method = RequestMethod.DELETE, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 删除组织所拥有的角色", httpMethod = "DELETE", notes = " 删除组织所拥有的角色")
	public CommonResult<String> delAllOrgRole(
			@ApiParam(name = "orgCode", value = "组织编码", required = true) @RequestParam String orgCode)
			throws Exception {
		orgService.delAllOrgRole(orgCode);
		return new CommonResult<String>(true, "删除成功！", "");
	}
	
	/**
	 * 获取组织所拥有的角色
	 * 1.首先查找组织自身的角色，有则返回
	 * 2.若组织自身没有角色，则从下往上找父组织的角色（可继承的），有则返回最近的
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgRoles/getOrgRoleByCode", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 获取组织所拥有的角色", httpMethod = "GET", notes = " 获取组织所拥有的角色，若自身有角色则返回自身的角色，若自身没有角色，则返回最近的父组织所拥有的可继承的角色")
	public List<Role> getOrgRoleByCode(
			@ApiParam(name = "orgCode", value = "组织编码", required = true) @RequestParam String orgCode)
			throws Exception {
		return orgService.getOrgRoleByCode(orgCode);
	}
	
	/**
	 * 物理删除所有逻辑删除了的组织数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="org/deleteOrgPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的组织数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的组织数据")
	public CommonResult<Integer> deleteOrgPhysical() throws Exception{
		Integer num = orgService.removePhysical();
		return OrgUtil.getRemovePhysiMsg(num);
	}
	
	/**
	 * 物理删除所有逻辑删除了的岗位数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="org/deletePostPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的岗位数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的岗位数据")
	public CommonResult<Integer> deletePostPhysical() throws Exception{
		Integer num = orgService.removePostPhysical();
		return OrgUtil.getRemovePhysiMsg(num);
	}
	
	/**
	 * 物理删除所有逻辑删除了的用户组织关系数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="org/deleteOrgUserPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的用户组织关系数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的用户组织关系数据")
	public CommonResult<Integer> deleteOrgUserPhysical() throws Exception{
		Integer num = orgService.removeOrgUserPhysical();
		return OrgUtil.getRemovePhysiMsg(num);
	}
	
	/**
	 * 根据维度id  获取对应维度下的组织
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgs/getByParentAndDem", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据维度id  获取对应维度下的组织", httpMethod = "POST", notes = "根据维度id获取对应维度下的组织，参数{\"demId\":\"1\",\"parentId\":\"1\"}")
	public List<OrgTree> getByParentAndDem(
			@ApiParam(name = "params", value = "参数值") @RequestBody Map<String,String> params) throws Exception {
		QueryFilter filter = QueryFilter.build();
		String demId = "";
		if(BeanUtils.isNotEmpty(params.get("demId"))){
			demId = params.get("demId").toString();
			filter.addParams("demId", demId);
		}
		String parentId = "0";
		if(BeanUtils.isNotEmpty(params.get("parentId"))){
			filter.addParams("parentId", " ('" +params.get("parentId").toString()+"') ");
		}else{
			parentId = AuthFilterUtil.getOrgAuthParentId(demId);
			filter.addParams("parentId", parentId);
		}
		String authSql = AuthFilterUtil.getOrgAuthSql(demId);
		if(StringUtil.isNotEmpty(authSql)){
			filter.addParams("authSql", authSql);
		}
		List<Org> list = orgService.getByParentAndDem(filter);
		//处理有父子关系的权限组织根目录
		if(StringUtil.isNotEmpty(authSql)){
			OrgUtil.dealRepeatAuthRoot(list);
		}
		
		List<OrgTree> groupTreeList = new ArrayList<OrgTree>();
		
		for (Org group : list) {
			OrgTree groupTree = new OrgTree(group);
			if(BeanUtils.isNotEmpty(params.get("isShowPost"))&& StringUtil.isNotEmpty(params.get("isShowPost").toString())&&params.get("isShowPost").toString().equals("true")){
				if(!groupTree.isIsParent()){
					List<OrgPost> postList = postService.getListByOrgId(groupTree.getId());
					if(postList.size() > 0){
						groupTree.setIsParent(1);
					}
				}
			}
			groupTreeList.add(groupTree);
		}
		if(BeanUtils.isNotEmpty(params.get("demId")) && StringUtil.isNotEmpty(params.get("demId").toString()) && BeanUtils.isEmpty(params.get("parentId"))){
			OrgTree root = new OrgTree();
			Demension d = demService.get(params.get("demId").toString());
			if(BeanUtils.isNotEmpty(d)){
				root.setId("0");
				if(BeanUtils.isNotEmpty(groupTreeList)){
					root.setIsParent(1);
				}else{
					root.setIsParent(0);
				}
				root.setDemId(d.getId());
				root.setName(d.getDemName());
				root.setParentId("0");
			}
			groupTreeList.add(root);
		}else if(BeanUtils.isNotEmpty(params.get("isOrgAuth"))&& StringUtil.isNotEmpty(params.get("isOrgAuth").toString())&&params.get("isOrgAuth").toString().equals("true")){
			OrgTree root = new OrgTree();
			Org org = orgService.get(params.get("parentId").toString());
			if(BeanUtils.isNotEmpty(org)){
				root = new OrgTree(org);
				if(org.isIsParent()){
					root.setAuthRoot(true);
				}
			}
			groupTreeList.add(root);
		}
		
		//以组织id为parentId获取岗位数据，显示在树下
		if(BeanUtils.isNotEmpty(params.get("isShowPost"))&& StringUtil.isNotEmpty(params.get("isShowPost").toString())&&params.get("isShowPost").toString().equals("true")){
			if(BeanUtils.isNotEmpty(params.get("parentId"))){
				List<OrgPost> postList = postService.getListByOrgId(params.get("parentId").toString());
				for (OrgPost orgPost : postList) {
					OrgTree postTree = new OrgTree(orgPost);
					postTree.setDemId(params.get("demId").toString());
					//设置岗位类别，默认false
					postTree.setPost(true);
					groupTreeList.add(postTree);
				}
			}
		}
		return groupTreeList;
	}

	@GetMapping(value = "orgs/children/{orgId}", produces = {"application/json; charset=utf-8" })
	@ApiOperation(value = "获取orgId的下级组织", httpMethod = "GET", notes = "获取orgId的下级组织")
	public List<Org> getByParentAndDem(@ApiParam(name = "orgId", value = "组织id") @PathVariable String orgId) throws Exception {
		QueryFilter filter = QueryFilter.build();
		filter.addParams("parentId", " ('" +orgId+"') ");
		List<Org> list = orgService.getByParentAndDem(filter);
		return list;
	}

	/**
	 * 根据维度id  获取对应维度下的组织 专为组织管理树服务
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgs/getByParentAndDemToTree", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据维度id  获取对应维度下的组织", httpMethod = "POST", notes = "根据维度id获取对应维度下的组织，参数{\"demId\":\"1\",\"parentId\":\"1\"}")
	public List<OrgTree> getByParentAndDemToTree(
			@ApiParam(name = "params", value = "参数值") @RequestBody Map<String,String> params) throws Exception {
		QueryFilter filter = QueryFilter.build();
		String demId = "";
		if(BeanUtils.isNotEmpty(params.get("demId"))){
			demId = params.get("demId").toString();
			filter.addParams("demId", demId);
		}
		//@TODO @userqiao 注释代码，能够让其查出所有的子集 但防止其影响其余未知地方 新开个接口专为组织管理树服务
		/*String parentId = "0";
		if(BeanUtils.isNotEmpty(params.get("parentId"))){
			filter.addParams("parentId", " ('" +params.get("parentId").toString()+"') ");
		}else{
			parentId = AuthFilterUtil.getOrgAuthParentId(demId);
			filter.addParams("parentId", parentId);
		}*/
		String authSql = AuthFilterUtil.getOrgAuthSql(demId);
		
		if(StringUtil.isNotEmpty(authSql)){
			filter.addParams("authSql", authSql);
		}
		List<Org> list = orgService.getByParentAndDem(filter);
		//处理有父子关系的权限组织根目录
		if(StringUtil.isNotEmpty(authSql)){
			OrgUtil.dealRepeatAuthRoot(list);
		}

		List<OrgTree> groupTreeList = new ArrayList<OrgTree>();

		for (Org group : list) {
			OrgTree groupTree = new OrgTree(group);
			if(BeanUtils.isNotEmpty(params.get("isShowPost"))&& StringUtil.isNotEmpty(params.get("isShowPost").toString())&&params.get("isShowPost").toString().equals("true")){
				if(!groupTree.isIsParent()){
					List<OrgPost> postList = postService.getListByOrgId(groupTree.getId());
					if(postList.size() > 0){
						groupTree.setIsParent(1);
					}
				}
			}
			groupTreeList.add(groupTree);
		}
		if(BeanUtils.isNotEmpty(params.get("demId")) && StringUtil.isNotEmpty(params.get("demId").toString()) && BeanUtils.isEmpty(params.get("parentId"))){
			OrgTree root = new OrgTree();
			Demension d = demService.get(params.get("demId").toString());
			if(BeanUtils.isNotEmpty(d)){
				root.setId("0");
				if(BeanUtils.isNotEmpty(groupTreeList)){
					root.setIsParent(1);
				}else{
					root.setIsParent(0);
				}
				root.setDemId(d.getId());
				root.setName(d.getDemName());
				root.setParentId("0");
			}
			groupTreeList.add(root);
		}else if(BeanUtils.isNotEmpty(params.get("isOrgAuth"))&& StringUtil.isNotEmpty(params.get("isOrgAuth").toString())&&params.get("isOrgAuth").toString().equals("true")){
			OrgTree root = new OrgTree();
			Org org = orgService.get(params.get("parentId").toString());
			if(BeanUtils.isNotEmpty(org)){
				root = new OrgTree(org);
				if(org.isIsParent()){
					root.setAuthRoot(true);
				}
			}
			groupTreeList.add(root);
		}

		//以组织id为parentId获取岗位数据，显示在树下
		if(BeanUtils.isNotEmpty(params.get("isShowPost"))&& StringUtil.isNotEmpty(params.get("isShowPost").toString())&&params.get("isShowPost").toString().equals("true")){
			if(BeanUtils.isNotEmpty(params.get("parentId"))){
				List<OrgPost> postList = postService.getListByOrgId(params.get("parentId").toString());
				for (OrgPost orgPost : postList) {
					OrgTree postTree = new OrgTree(orgPost);
					postTree.setDemId(params.get("demId").toString());
					//设置岗位类别，默认false
					postTree.setPost(true);
					groupTreeList.add(postTree);
				}
			}
		}
		return groupTreeList;
	}


	/**
	 * 获取组织人员列表
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "orgUsers/getOrgUserPage", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织人员（带分页信息）", httpMethod = "POST", notes = "获取组织人员列表")
	public PageList<HashMap<String,Object>> getOrgUserPage(
			@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception {
		QueryFilter groupFilter = QueryFilter.build();
		List<HashMap<String,Object>> userByGroupList = orgUserService.getUserByGroupList(groupFilter);
		Page<HashMap<String,Object>> p = (Page<HashMap<String,Object>>) orgUserService.getUserByGroup(queryFilter);
		if(p.getTotal() > 0 && userByGroupList.size() > 0){
			for (Map user : p.getRecords()) {
				for (Map obj : userByGroupList){
					if(user.get("userId").equals(obj.get("userId"))){
						String isMaster = obj.get("isMaster").toString();
						String[] split = isMaster.split(",");
						int count = 0;
						Pattern pt = Pattern.compile("1");
						Matcher m = pt.matcher(isMaster);
						while (m.find()) {
							count++;
						}
						if(split.length > 1 && count > 1 || isMaster.indexOf("1") > -1 && !"1".equals(user.get("isMaster").toString())){
							user.put("otherPost",3);
						}else if(split.length == 1){
							user.put("otherPost",1);
						}else{
							user.put("otherPost",2);
						}
					}
				}
			}
		}
		return  new PageList<HashMap<String,Object>>(p);
	}
	
	/**
	 * 通过id，设置人员（取消）主岗位
	 * 
	 * @param account
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPost/setMasterById", method = RequestMethod.PUT, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "设置人员（取消）主岗位", httpMethod = "PUT", notes = "通过id，设置人员（取消）主岗位")
	public CommonResult<String> setMasterById(
			@ApiParam(name = "id", value = "组织人员关系id", required = true) @RequestParam String ... id )throws Exception {
		try {
			orgUserService.setMaster(id);
			return  new CommonResult<String>(true, "操作成功！", "");
		} catch (Exception e) {
			e.printStackTrace();
			return  new CommonResult<String>(false, "操作失败！", "");
		}
	}

	/**
	 * 通过id，设置人员主岗位
	 *
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgPost/setMasterByIds", method = RequestMethod.PUT, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "设置人员主岗位", httpMethod = "PUT", notes = "通过id，设置人员主岗位")
	public CommonResult<String> setMasterByIds(
			@ApiParam(name = "ids", value = "组织人员关系ids", required = true) @RequestParam String ... ids )throws Exception {
		try {
			orgUserService.setMasterByIds(ids);
			return  new CommonResult<String>(true, "操作成功！", "");
		} catch (Exception e) {
			e.printStackTrace();
			return  new CommonResult<String>(false, "操作失败！", "");
		}
	}
	
	/**
	 * 用户批量添加下属
	 * 
	 * @param orgId
	 * @param account
	 *            上级用户帐号
	 * @param underAccounts
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgUsers/setUnderUsers", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "用户批量添加下属", httpMethod = "POST", notes = "用户批量添加下属", hidden = true)
	public CommonResult<String> setUnderUsers(
			@ApiParam(name = "orgId", value = "组织id", required = true) @RequestParam String orgId,
			@ApiParam(name = "account", value = "上级用户帐号", required = true) @RequestParam String account,
			@ApiParam(name = "underAccounts", value = "下级用户帐号", required = true) @RequestParam String underAccounts) throws Exception {
		return orgService.setUnderUsers(orgId, account, underAccounts);
	}
	
	/**
	 * 组织批量加入用户
	 * 
	 * @param orgCode
	 * @param accounts
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgUsers/addUsersForOrg", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "组织批量加入用户", httpMethod = "POST", notes = "组织批量加入用户", hidden = true)
	public CommonResult<String> addUsersForOrg(
			@ApiParam(name = "orgCode", value = "组织代码", required = true) @RequestParam String orgCode,
			@ApiParam(name = "accounts", value = "用户帐号", required = true) @RequestParam String accounts) throws Exception {
		CommonResult<String> rtn = null;
		try {
			rtn = orgService.addUsersForOrg(orgCode, accounts);
		} catch (Exception e) {
			rtn = new CommonResult<String>(false,e.getMessage(),"保存失败");
		}
		return rtn;
	}
	
	/**
	 * 根据时间获取组织数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgs/getOrgByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取组织数据（数据同步）", httpMethod = "POST", notes = "根据时间获取组织数据（数据同步）")
	public List<Org> getOrgByTime(@ApiParam(name="exportObject",value="获取数据参数类",required=true) @RequestBody OrgExportObject exportObject) throws Exception{
		return orgService.getOrgByTime(exportObject);
	}
	
	/**
	 * 根据时间获取组织参数数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgParams/getOrgParamByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取组织参数数据（数据同步）", httpMethod = "POST", notes = "根据时间获取组织参数数据（数据同步）")
	public List<OrgParams> getOrgParamByTime(@ApiParam(name="exportObject",value="获取数据参数类",required=true) @RequestBody OrgExportObject exportObject) throws Exception{
		return orgService.getOrgParamByTime(exportObject);
	}
	
	/**
	 * 根据时间获取组织岗位数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgPosts/getOrgPostByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取组织岗位数据（数据同步）", httpMethod = "POST", notes = "根据时间获取组织岗位数据（数据同步）")
	public List<OrgPost> getOrgPostByTime(@ApiParam(name="exportObject",value="获取数据参数类",required=true) @RequestBody OrgExportObject exportObject) throws Exception{
		return orgService.getOrgPostByTime(exportObject);
	}
	
	/**
	 * 根据时间获取组织角色数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgRoles/getOrgRoleByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取组织角色数据（数据同步）", httpMethod = "POST", notes = "根据时间获取组织角色数据（数据同步）")
	public List<OrgRole> getOrgRoleByTime(@ApiParam(name="exportObject",value="获取数据参数类",required=true) @RequestBody OrgExportObject exportObject) throws Exception{
		return orgService.getOrgRoleByTime(exportObject);
	}
	
	/**
	 * 根据时间获取用户组织关系数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgUsers/getOrgUserByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取用户组织关系数据（数据同步）", httpMethod = "POST", notes = "根据时间获取用户组织关系数据（数据同步）")
	public List<OrgUser> getOrgUserByTime(@ApiParam(name="exportObject",value="获取数据参数类",required=true) @RequestBody OrgExportObject exportObject) throws Exception{
		return orgService.getOrgUserByTime(exportObject);
	}
	
	/**
	 * 根据时间获取组织中下属数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userUnders/getUserUnderByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取组织中下属数据（数据同步）", httpMethod = "POST", notes = "根据时间获取组织中下属数据（数据同步）")
	public List<UserUnder> getUserUnderByTime(@ApiParam(name="exportObject",value="获取数据参数类",required=true) @RequestBody OrgExportObject exportObject) throws Exception{
		return orgService.getUserUnderByTime(exportObject);
	}
	
	@RequestMapping(value="org/isCodeExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询组织编码是否已存在", httpMethod = "GET", notes = "查询组织编码是否已存在")
	public CommonResult<Boolean> isCodeExist(@ApiParam(name="code",value="组织编码")
												@RequestParam(required=true) String code) throws Exception{
		return orgService.isCodeExist(code);
	}
	
	@RequestMapping(value="orgPost/isPostCodeExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询岗位编码是否已存在", httpMethod = "GET", notes = "查询岗位编码是否已存在")
	public CommonResult<Boolean> isPostCodeExist(@ApiParam(name="code",value="岗位编码")
												@RequestParam(required=true) String code) throws Exception{
		return orgService.isPostCodeExist(code);
	}
	
	
	/**
	 * 同步OA组织岗位数据
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgs/oaAsync",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "同步OA组织岗位数据", httpMethod = "POST", notes = "同步OA组织岗位数据")
	public CommonResult<String> oaAsync(@ApiParam(name="oaAsyncObject",value="同步参数",required=true) @RequestBody OaAsyncObject oaAsyncObject,HttpServletRequest request) throws Exception{
		String ip = OrgUtil.getIpAddress(request);
		return userImportService.syncSoap(oaAsyncObject,ip);
	}
	
	@RequestMapping(value = "orgRoles/getOrgRoleList", method = RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织角色列表（带分页信息）", httpMethod = "POST", notes = "获取组织角色列表（带分页信息）")
	public PageList<OrgRole> getOrgRoleList(@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception {
		//queryFilter.setClazz(OrgRole.class);
		PageList<OrgRole> list = orgRoleService.query(queryFilter);
		return list;
	}
	
	@RequestMapping(value = "orgRoles/saveOrgRole", method = RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "保存组织角色信息", httpMethod = "POST", notes = "保存组织角色信息")
	public CommonResult<String> saveOrgRole(@ApiParam(name = "vo", value = "通用查询对象") @RequestBody OrgRoleVo vo) throws Exception {
		return orgService.addOrgRole(vo);
	}
	
	@RequestMapping(value = "orgRoles/removeOrgRole", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "保存组织角色信息", httpMethod = "GET", notes = "保存组织角色信息")
	public CommonResult<String> removeOrgRole(@ApiParam(name="code",value="岗位编码")
	@RequestParam(required=true) String id) throws Exception {
		return orgService.delOrgRoleById(id);
	}
	
	
	/**
	 * 更新组织所在树的位置
	 * @param request
	 * @param response
	 * @throws Exception void
	 * @exception
	 */
	@RequestMapping(value="orgs/updateOrgPos",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新组织所在树的位置", httpMethod = "POST", notes = "更新组织所在树的位置（树结构拖动保存）")
	public CommonResult<String> updateOrgPos(@ApiParam(name="orgId",value="移动节点id", required = true) @RequestParam String orgId,@ApiParam(name="parentId",value="移至（目标）节点id", required = true) @RequestParam String parentId) throws Exception {
		
		return orgService.updateOrgPos(orgId, parentId);
	}
	
	
	@RequestMapping(value="orgs/exportUsers",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "导出用户组织数据", httpMethod = "GET", notes = "导出用户组织数据")
	public void exportUsers(@ApiParam(name="orgCode",value="组织编码",required=true)   @RequestParam String orgCode,@ApiParam(name="isChildre",value="是否包含子组织。默认为true",required=true)   @RequestParam Boolean isChildre) throws Exception{
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		isChildre = BeanUtils.isEmpty(isChildre)?true:isChildre;
		String path = orgService.exportData(orgCode, isChildre);
		if(StringUtil.isNotEmpty(path)){
			String excelPath = path+".xls";
			File file = new File(excelPath);
			if(!file.exists()){
				throw new RuntimeException("生成Excel文件失败！");
			}
			String excelName = file.getName();
			// 导出
			HttpUtil.downLoadFile(response, excelPath, excelName);
			// 删除导出的文件
			FileUtil.deleteFile(file.getParentFile().getPath());
		}else{
			throw new RuntimeException("导出组织用户信息失败！");
		}
	}
	
	
	
	
	@RequestMapping(value = "org/getMainGroup", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户主组织", httpMethod = "GET", notes = "获取用户主组织")
	public Org getMainGroup(
			@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
			@ApiParam(name = "demId", value = "维度id", required = true) @RequestParam  Optional<String> demId)throws Exception {
		return orgService.getMainGroup(userId, demId.orElse(""));
	}
	
	@RequestMapping(value = "orgJobs/getJobsByUserId", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所有职务", httpMethod = "GET", notes = "获取用户所有职务")
	public List<OrgJob> getJobsByUserId(
		@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId)throws Exception {
		return orgJobManager.getListByUserId(userId);
	}
	
	@RequestMapping(value = "org/isSupOrgByCurrMain", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "判断当前用户主部门是否有上级 ", httpMethod = "GET", notes = "判断当前用户主部门是否有上级 ")
	public Boolean isSupOrgByCurrMain(
		@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
		@ApiParam(name = "demId", value = "维度id", required = true) @RequestParam String demId,
		@ApiParam(name = "level", value = "级别", required = true) @RequestParam Integer level)throws Exception {
		List<OrgUser> orgUsers = orgUserService.getOrgUserMaster(userId,demId);
        if(BeanUtils.isNotEmpty(orgUsers)){
        	OrgUser orgUser = orgUsers.get(0);
        	String levelGroupId = orgUser.getOrgId();
            Org org = orgService.get(levelGroupId);
            while(level>0) {
                org = orgService.get(org.getParentId());
                if(BeanUtils.isNotEmpty(org)){
                	levelGroupId = org.getId();
                	--level;
                }else{
                	return false;
                }
            }
            Org currentOrg = orgService.getMainGroup(userId, demId);
            if(BeanUtils.isNotEmpty(currentOrg)){
            	String groupId = currentOrg.getId();
                if(levelGroupId.equals(groupId)){
                    return false;
                }
                if(StringUtil.isNotZeroEmpty(currentOrg.getParentId())){
                    return true;
                }
            }
        }
		return false;
	}
	
	@RequestMapping(value = "org/getSupOrgByCurrMain", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户的主岗位组织关系", httpMethod = "GET", notes = "获取用户的主岗位组织关系")
	public OrgUser getSupOrgByCurrMain(
		@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
		@ApiParam(name = "demId", value = "维度id", required = true) @RequestParam String demId)throws Exception {
		List<OrgUser> orgUsers = orgUserService.getOrgUserMaster(userId,demId);
        if(BeanUtils.isNotEmpty(orgUsers)) {
        	return orgUsers.get(0);
        }
		return null;
	}
	
	@RequestMapping(value = "org/getMainPostOrOrgByUserId", method = RequestMethod.GET, produces = {"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户的主岗位、主组织(优先获取默认维度的，没有时获取其他维度的)", httpMethod = "GET", notes = "获取用户的主岗位、主组织(优先获取默认维度的，没有时获取其他维度的)")
	public OrgUser getMainPostOrOrgByUserId(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) throws Exception{
		return orgUserService.getMainPostOrOrgByUserId(userId);
	}
	
	/**
	 * 获取指定上级级别的组织的负责人
	 * @param userId
	 * @param level
	 * @param isMainCharge
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orgusers/getCustomLevelCharge", method = RequestMethod.GET)
	public List<User> getCustomLevelCharge(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="level",required=true)  String level,@RequestParam(value="isMainCharge",required=true)  boolean isMainCharge) throws Exception {
		List<Org> userOrg = orgService.getUserOrg(userId, "", true);
		List<User> chargesByOrg = new ArrayList<User>();
		if( BeanUtils.isEmpty(userOrg) ) {
			return chargesByOrg;
		}
		Org org = userOrg.get(0);
		while(!"0".equals(org.getParentId())) {
			String orgId = org.getParentId();
			org = orgService.get(orgId);
			if(level.equals(org.getGrade())) {
				chargesByOrg = userService.getChargesByOrg(org.getCode(), isMainCharge);
				break;
			}
		}
		return chargesByOrg;
	}
	
	/**
	 * 获取指定上级级别的组织 中指定岗位的用户
	 * @param userId
	 * @param level
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orgusers/getCustomLevelPost", method = RequestMethod.GET)
	public  Set<GroupIdentity> getCustomLevelPost(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="level",required=true)  String level,@RequestParam(value="postCode",required=true)  String postCode) throws Exception {
		List<Org> userOrg = orgService.getUserOrg(userId, "", true);
		 Set<GroupIdentity> chargesByOrg = new HashSet<GroupIdentity>();
		if( BeanUtils.isEmpty(userOrg) ) {
			return chargesByOrg;
		}
		Org org = userOrg.get(0);
		while(!"0".equals(org.getParentId())) {
			String orgId = org.getParentId();
			org = orgService.get(orgId);
			if(level.equals(org.getGrade())) {
				chargesByOrg = userService.getByPostCodeAndOrgCode(postCode, org.getCode());
				break;
			}
		}
		return chargesByOrg;
	}
	
	/**
	 * 获取指定上级级别的组织 中指定职务的用户
	 * @param userId
	 * @param level
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orgusers/getCustomLevelJob", method = RequestMethod.GET)
	public  Set<GroupIdentity> getCustomLevelJob(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="level",required=true)  String level,@RequestParam(value="jobCode",required=true)  String jobCode) throws Exception {
		List<Org> userOrg = orgService.getUserOrg(userId, "", true);
		 Set<GroupIdentity> chargesByOrg = new HashSet<GroupIdentity>();
		if( BeanUtils.isEmpty(userOrg) ) {
			return chargesByOrg;
		}
		Org org = userOrg.get(0);
		while(!"0".equals(org.getParentId())) {
			String orgId = org.getParentId();
			org = orgService.get(orgId);
			if(level.equals(org.getGrade())) {
				chargesByOrg =  userService.getByJobCodeAndOrgCode(jobCode,org.getCode());
				break;
			}
		}
		return chargesByOrg;
	}
	
	@RequestMapping(value = "/orgusers/getStartOrgParam", method = RequestMethod.GET)
	public String getStartOrgParam(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="param",required=true)  String param) {
		List<Org> userOrg = orgService.getUserOrg(userId, "", true);
		String paramValue = "";
		if( BeanUtils.isEmpty(userOrg) ) {
			return paramValue;
		}
		Org org = userOrg.get(0);
		OrgParams byOrgIdAndAlias = orgParamsService.getByOrgIdAndAlias(org.getId(), param);
		return byOrgIdAndAlias.getValue();
	}

    @RequestMapping(value = "orgpost/getFullname", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据岗位id查询岗位所在的人员，一般只有一位", httpMethod = "GET", notes = "根据岗位id查询岗位所在的人员，一般只有一位")
    public List<Map<String, Object>> getFullname(@ApiParam(name = "postId", value = "岗位id", required = true) @RequestParam String postId)throws Exception {
        return postService.getFullname(postId);
    }

    @RequestMapping(value = "orgpost/getPostByJobId", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据职务id查询岗位以及这些岗位所属的组织全路径", httpMethod = "GET", notes = "根据职位id查询岗位以及这些岗位所属的组织全路径")
    public List<Map<String, Object>> getPostByJobId(@ApiParam(name = "jobId", value = "职务id", required = true) @RequestParam String jobId)throws Exception {
        return postService.getPostByJobId(jobId);
    }

    @RequestMapping(value = "orgpost/getUserByUserId", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据用户ID查询用户组织岗位角色信息", httpMethod = "GET", notes = "根据用户ID查询用户组织岗位角色信息")
    public List<Map<String, Object>> getUserByUserId(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId)throws Exception {
        return postService.getUserByUserId(userId);
    }
    
	@RequestMapping(value = "org/getPathNames", method = RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户id组获取组织全路径", httpMethod = "POST", notes = "根据用户id获得主岗位的岗位组")
	public List<Map<String,String>> getPathNames(@ApiParam(name = "userIds", value = "用户id组", required = false) @RequestParam List<String> userIds)throws Exception {
		return orgService.getPathNames(userIds);
	}
	
	@RequestMapping(value = "org/getChildrenIds", method = RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取子组织ID（包含自己）", httpMethod = "POST", notes = "获取子组织ID（包含自己）")
	public Map<String, Set<String>> getChildrenIds(@ApiParam(name = "ids", value = "组织id", required = true) @RequestParam Map<String,String> ids)throws Exception {
		return orgService.getChildrenIds(ids);
	}
	
	@RequestMapping(value = "org/getOrgListByDemId", method = RequestMethod.GET, produces = {"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织列表", httpMethod = "GET", notes = "获取组织列表")
	public List<Org> getOrgListByDemId(@ApiParam(name = "demId", value = "维度id", required = true) @RequestParam String demId) throws Exception{
		return orgService.getOrgListByDemId(demId);
	}

    @RequestMapping(value = "orgusers/getUserOrgNowNumByOrgId", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据组织id查询组织下面的人员数量", httpMethod = "GET", notes = "根据组织id查询组织下面的人员数量")
    public Integer getUserOrgNowNumByOrgId(@ApiParam(name = "orgId", value = "组织id", required = true) @RequestParam String orgId)throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orgId", orgId);
        map.put("group", "true");
        List<Map<String, Object>> list = orgUserDao.getUserNumByOrgId(map);
	    return list.size();
    }

    @RequestMapping(value = "orgusers/getUserOrgNowNumByOrgIds", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据组织id集合查询组织下面的人员数量", httpMethod = "GET", notes = "根据组织id查询组织下面的人员数量")
    public List<Map<String, Object>> getUserOrgNowNumByOrgIds(@ApiParam(name = "orgIds", value = "组织id集合", required = true) @RequestParam String orgIds)throws Exception {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String[] value = orgIds.split(",");
        for(String orgId :value){
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> numMap = new HashMap<>();
            map.put("orgId", orgId);
            map.put("group", "true");
            List<Map<String, Object>> list = orgUserDao.getUserNumByOrgId(map);
            numMap.put("orgId", orgId);
            numMap.put("num", list.size());
            mapList.add(numMap);
        }
        return mapList;
    }
    /**
	 * 从第三方获取组织数据添加到本系统
	 *
	 * @param orgVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "org/addOrgFromExterUni", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "从第三方获取组织数据添加到本系统", httpMethod = "POST", notes = "从第三方获取组织数据添加到本系统")
	public CommonResult<String> addOrgFromExterUni(
			@ApiParam(name = "org", value = "组织", required = true) @RequestBody Org orgVo) throws Exception {
		CommonResult<String> rtn = null;
		rtn = orgService.addOrgFromExterUni(orgVo);
		return rtn;
	}

	@RequestMapping(value = "org/getFillOrg", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取当前用户填制单位", httpMethod = "GET", notes = "获取当前用户填制单位")
	public CommonResult<Org> getFillOrg(
			@ApiParam(name = "demId", value = "维度Id", required = true) @RequestParam Optional<String> demId,
			@ApiParam(name = "grade", value = "组织级别", required = true) @RequestParam Optional<String> grade) throws Exception {
		return orgService.getFillOrg(demId.orElse(""),grade.orElse("2"));
	}
}
