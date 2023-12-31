package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.cache.ICache;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.exception.BaseException;
import com.hotent.uc.mock.MockUCDataUtil;
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

/**
 * 组织模块接口
 * 
 * @author liangqf
 *
 */
@RestController
@RequestMapping("/api/org/v1/")
@Api(tags = "OrgController")
public class OrgController extends BaseController {

	@Autowired
	ICache<UserDetails> iCache;

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
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }

	@RequestMapping(value = "orgs/getOrgPage", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织列表（带分页信息）", httpMethod = "POST", notes = "获取组织列表")
	public PageList<Org> getOrgPage(
			@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
	}

	/**
	 * 修改组织
	 * 
	 * @param orgVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "org/updateOrg", method = RequestMethod.PUT, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "修改组织", httpMethod = "PUT", notes = "修改组织")
	public CommonResult<String> updateOrg(
			@ApiParam(name = "orgVo", value = "组织视图", required = true) @RequestBody OrgVo orgVo) throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据当前用户主组织
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "org/getOrgMaster", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据当前用户主组织", httpMethod = "GET", notes = "根据当前用户主组织")
	public Org getOrgMaster()
			throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
    	throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "userUnder/delUnderUserByIds", method = RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据id删除下属", httpMethod = "DELETE", notes = "根据id删除下属，多个用户逗号隔开",hidden=true)
	public CommonResult<String> delUnderUserByIds(@ApiParam(name="ids",value="记录id字符串",required=true) @RequestParam String ids) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "orgs/getOrgsByAccount", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所属组织", httpMethod = "POST", notes = "获取用户所属组织")
	public List<Org> getOrgsByAccount(@ApiParam(name = "account", value = "用户帐号", required = true) @RequestParam String account) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "orgs/getOrgListByUserId", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所属组织", httpMethod = "GET", notes = "获取用户所属组织")
	public List<Org> getOrgListByUserId(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) throws Exception{
		//return orgService.getOrgListByUserId(userId);
		return MockUCDataUtil.getOrgListByUserId(userId);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "orgs/getUserOrgPage", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所属组织", httpMethod = "POST", notes = "获取用户所属组织")
	public PageList<HashMap<String,Object>> getUserOrgPage(@ApiParam(name = "filter", value = "查询对象", required = true) @RequestBody QueryFilter filter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		return MockUCDataUtil.getChargesByOrdId(orgId,isMain,demCode);
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
//		List<OrgPost> list = postService.getListByAccount(account, null);
		List<OrgPost> list = MockUCDataUtil.getOrgPostByUserAccount(account);
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
			@ApiParam(name = "id", value = "组织人员关系id", required = true) @RequestParam String id )throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="org/isCodeExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询组织编码是否已存在", httpMethod = "GET", notes = "查询组织编码是否已存在")
	public CommonResult<Boolean> isCodeExist(@ApiParam(name="code",value="组织编码")
												@RequestParam(required=true) String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="orgPost/isPostCodeExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询岗位编码是否已存在", httpMethod = "GET", notes = "查询岗位编码是否已存在")
	public CommonResult<Boolean> isPostCodeExist(@ApiParam(name="code",value="岗位编码")
												@RequestParam(required=true) String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "orgRoles/getOrgRoleList", method = RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织角色列表（带分页信息）", httpMethod = "POST", notes = "获取组织角色列表（带分页信息）")
	public PageList<OrgRole> getOrgRoleList(@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter) throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "orgRoles/saveOrgRole", method = RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "保存组织角色信息", httpMethod = "POST", notes = "保存组织角色信息")
	public CommonResult<String> saveOrgRole(@ApiParam(name = "vo", value = "通用查询对象") @RequestBody OrgRoleVo vo) throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "orgRoles/removeOrgRole", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "保存组织角色信息", httpMethod = "GET", notes = "保存组织角色信息")
	public CommonResult<String> removeOrgRole(@ApiParam(name="code",value="岗位编码")
	@RequestParam(required=true) String id) throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	@RequestMapping(value="orgs/exportUsers",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "导出用户组织数据", httpMethod = "GET", notes = "导出用户组织数据")
	public void exportUsers(@ApiParam(name="orgCode",value="组织编码",required=true)   @RequestParam String orgCode,@ApiParam(name="isChildre",value="是否包含子组织。默认为true",required=true)   @RequestParam Boolean isChildre) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	
	
	@RequestMapping(value = "org/getMainGroup", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户主组织", httpMethod = "GET", notes = "获取用户主组织")
	public Org getMainGroup(
			@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
			@ApiParam(name = "demId", value = "维度id", required = true) @RequestParam  Optional<String> demId)throws Exception {
//		return orgService.getMainGroup(userId, demId.orElse(""));
		return MockUCDataUtil.getMainGroup(userId,demId.orElse(""));
	}
	
	@RequestMapping(value = "orgJobs/getJobsByUserId", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户所有职务", httpMethod = "GET", notes = "获取用户所有职务")
	public List<OrgJob> getJobsByUserId(
		@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId)throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "org/isSupOrgByCurrMain", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "判断当前用户主部门是否有上级 ", httpMethod = "GET", notes = "判断当前用户主部门是否有上级 ")
	public Boolean isSupOrgByCurrMain(
		@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
		@ApiParam(name = "demId", value = "维度id", required = true) @RequestParam String demId,
		@ApiParam(name = "level", value = "级别", required = true) @RequestParam Integer level)throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "org/getSupOrgByCurrMain", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户的主岗位组织关系", httpMethod = "GET", notes = "获取用户的主岗位组织关系")
	public OrgUser getSupOrgByCurrMain(
		@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
		@ApiParam(name = "demId", value = "维度id", required = true) @RequestParam String demId)throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
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
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "/orgusers/getStartOrgParam", method = RequestMethod.GET)
	public String getStartOrgParam(@RequestParam(value="userId",required=true) String userId,@RequestParam(value="param",required=true)  String param) {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}

    @RequestMapping(value = "orgpost/getFullname", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据岗位id查询岗位所在的人员，一般只有一位", httpMethod = "GET", notes = "根据岗位id查询岗位所在的人员，一般只有一位")
    public List<Map<String, Object>> getFullname(@ApiParam(name = "postId", value = "岗位id", required = true) @RequestParam String postId)throws Exception {
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }

    @RequestMapping(value = "orgpost/getPostByJobId", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据职务id查询岗位以及这些岗位所属的组织全路径", httpMethod = "GET", notes = "根据职位id查询岗位以及这些岗位所属的组织全路径")
    public List<Map<String, Object>> getPostByJobId(@ApiParam(name = "jobId", value = "职务id", required = true) @RequestParam String jobId)throws Exception {
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }

    @RequestMapping(value = "orgpost/getUserByUserId", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据用户ID查询用户组织岗位角色信息", httpMethod = "GET", notes = "根据用户ID查询用户组织岗位角色信息")
    public List<Map<String, Object>> getUserByUserId(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId)throws Exception {
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }
    
	@RequestMapping(value = "org/getPathNames", method = RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据用户id组获取组织全路径", httpMethod = "POST", notes = "根据用户id组获取组织全路径")
	public List<Map<String,String>> getPathNames(@ApiParam(name = "userIds", value = "用户id组", required = false) @RequestParam List<String> userIds)throws Exception {
		//return orgService.getPathNames(userIds);
		return MockUCDataUtil.getPathNames(userIds);
	}
	
	@RequestMapping(value = "org/getChildrenIds", method = RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取子组织ID（包含自己）", httpMethod = "POST", notes = "获取子组织ID（包含自己）")
	public Map<String, Set<String>> getChildrenIds(@ApiParam(name = "ids", value = "组织id", required = true) @RequestParam Map<String,String> ids)throws Exception {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value = "org/getOrgListByDemId", method = RequestMethod.GET, produces = {"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织列表", httpMethod = "GET", notes = "获取组织列表")
	public List<Org> getOrgListByDemId(@ApiParam(name = "postId", value = "维度id", required = true) @RequestParam String demId) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}

    @RequestMapping(value = "orgusers/getUserOrgNowNumByOrgId", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据组织id查询组织下面的人员数量", httpMethod = "GET", notes = "根据组织id查询组织下面的人员数量")
    public Integer getUserOrgNowNumByOrgId(@ApiParam(name = "orgId", value = "组织id", required = true) @RequestParam String orgId)throws Exception {
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }

    @RequestMapping(value = "orgusers/getUserOrgNowNumByOrgIds", method = RequestMethod.GET, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "根据组织id集合查询组织下面的人员数量", httpMethod = "GET", notes = "根据组织id查询组织下面的人员数量")
    public List<Map<String, Object>> getUserOrgNowNumByOrgIds(@ApiParam(name = "orgIds", value = "组织id集合", required = true) @RequestParam String orgIds)throws Exception {
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }
	
}
