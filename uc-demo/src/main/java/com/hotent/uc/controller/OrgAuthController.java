package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.exception.BaseException;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.org.OrgAuthVo;

/**
 * 组织分级管理接口
 * @author liangqf
 *
 */
@RestController
@RequestMapping("/api/orgAuth/v1/")
@Api(tags="OrgAuthController")
public class OrgAuthController extends BaseController  {
	
	/**
	 * 获取组织分级列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgAuths/getOrgAuthPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织分级列表（带分页信息）", httpMethod = "POST", notes = "获取组织分级列表")
	public PageList<OrgAuth> getOrgAuthPage(@ApiParam(name="filter",value="查询对象")
	 @RequestBody QueryFilter filter,@ApiParam(name="orgCode",value="组织编码",required=true) @RequestParam String orgCode,@ApiParam(name="account",value="用户账号") @RequestParam(required=false) String account) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 添加组织分级
	 * @param orgAuthVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgAuth/addOrgAuth",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "添加组织分级", httpMethod = "POST", notes = "添加组织分级")
	public CommonResult<String> addOrgAuth(@ApiParam(name="orgAuthVo",value="组织分级对象",required=true) @RequestBody OrgAuthVo orgAuthVo) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 修改组织分级
	 * @param orgAuthVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgAuth/updateOrgAuth",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "修改组织分级", httpMethod = "PUT", notes = "修改组织分级")
	public CommonResult<String> updateOrgAuth(@ApiParam(name="orgAuthVo",value="组织分级对象",required=true) @RequestBody OrgAuthVo orgAuthVo) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 删除组织分级
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgAuth/delOrgAuth",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "删除组织分级", httpMethod = "DELETE", notes = "删除组织分级")
	public CommonResult<String> delOrgAuth(@ApiParam(name="account",value="用户账号",required=true) @RequestParam String account,@ApiParam(name="orgCode",value="组织编码",required=true) @RequestParam String orgCode) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取组织分级
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgAuth/getOrgAuth",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织分级", httpMethod = "GET", notes = "获取组织分级")
	public OrgAuth getOrgAuth(@ApiParam(name="account",value="用户账号",required=true) @RequestParam String account,@ApiParam(name="orgCode",value="组织编码",required=true) @RequestParam String orgCode) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 物理删除所有逻辑删除了的分级组织数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgAuth/deleteOrgAuthPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的分级组织数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的分级组织数据")
	public CommonResult<Integer> deleteOrgAuthPhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 根据时间获取分级组织数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgAuths/getOrgAuthByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取分级组织数据（数据同步）", httpMethod = "POST", notes = "根据时间获取分级组织数据（数据同步）")
	public List<OrgAuth> getOrgAuthByTime(@ApiParam(name="exportObject",value="获取数据参数类",required=true) @RequestBody OrgExportObject exportObject) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="orgAuths/getAllOrgAuth",method=RequestMethod.GET, produces = {"application/json; charset=utf-8" })
	@ApiOperation(value = "获取分级组织", httpMethod = "POST", notes = "获取分级组织")
	public List<OrgAuth> getAllOrgAuth(QueryFilter queryFilter) {
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	@RequestMapping(value="orgAuths/getCurrentUserAuthOrgLayout",method=RequestMethod.GET, produces = {"application/json; charset=utf-8" })
	@ApiOperation(value = "获取当前用户的组织布局管理权限", httpMethod = "GET", notes = "获取当前用户的组织布局管理权限")
	public List<OrgAuth> getCurrentUserAuthOrgLayout(@ApiParam(name="userId",value="用户id") @RequestParam Optional<String> userId) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
}
