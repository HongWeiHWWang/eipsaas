package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.exception.BaseException;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.model.RelAuth;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserRel;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.org.RelAuthVo;
import com.hotent.uc.util.OrgUtil;

/**
 * 汇报线分级管理接口
 * @author liangqf
 *
 */
@RestController
@RequestMapping("/api/relAuth/v1/")
@Api(tags="relAuthController")
public class RelAuthController extends BaseController  {
	
	
	/**
	 * 获取汇报线分级列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="relAuths/getRelAuthPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取汇报线分级列表（带分页信息）", httpMethod = "POST", notes = "获取汇报线分级列表")
	public PageList<RelAuth> getRelAuthPage(@ApiParam(name="filter",value="查询对象")
	 @RequestBody QueryFilter filter,@ApiParam(name="relCode",value="汇报线编码",required=true) @RequestParam String relCode,@ApiParam(name="account",value="用户账号") @RequestParam(required=false) String account) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 添加汇报线分级
	 * @param relAuthVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="relAuth/addRelAuth",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "添加汇报线分级", httpMethod = "POST", notes = "添加汇报线分级")
	public CommonResult<String> addRelAuth(@ApiParam(name="relAuthVo",value="汇报线分级对象",required=true) @RequestBody RelAuthVo relAuthVo) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 分配管理员
	 * @param Role
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="relAuths/addRelAuths",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "分配管理员（多个）", httpMethod = "POST", notes = "分配管理员（多个）")
	public CommonResult<String> addRelAuths(@ApiParam(name="code",value="汇报线节点编码", required = true) @RequestParam String code,
			@ApiParam(name="accounts",value="用户账号，多个用“,”号隔开", required = true) @RequestParam String accounts) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 修改汇报线分级
	 * @param relAuthVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="relAuth/updateRelAuth",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "修改汇报线分级", httpMethod = "PUT", notes = "修改汇报线分级")
	public CommonResult<String> updateRelAuth(@ApiParam(name="relAuthVo",value="汇报线分级对象",required=true) @RequestBody RelAuthVo relAuthVo) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 删除汇报线分级
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="relAuth/delRelAuth",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "删除汇报线分级", httpMethod = "DELETE", notes = "删除汇报线分级")
	public CommonResult<String> delRelAuth(@ApiParam(name="relCode",value="汇报线编码",required=true) @RequestParam String relCode,@ApiParam(name="accounts",value="用户账号（多个用“,”号隔开）",required=true) @RequestBody String accounts) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取汇报线分级
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="relAuth/getRelAuth",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取汇报线分级", httpMethod = "GET", notes = "获取汇报线分级")
	public RelAuth getRelAuth(@ApiParam(name="account",value="用户账号",required=true) @RequestParam String account,@ApiParam(name="relCode",value="汇报线编码",required=true) @RequestParam String relCode) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 物理删除所有逻辑删除了的分级汇报线数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="relAuth/deleteRelAuthPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的分级汇报线数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的分级汇报线数据")
	public CommonResult<Integer> deleteRelAuthPhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 根据时间获取分级汇报线数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="relAuths/getRelAuthByTime",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取分级汇报线数据（数据同步）", httpMethod = "POST", notes = "根据时间获取分级汇报线数据（数据同步）")
	public List<RelAuth> getRelAuthByTime(@ApiParam(name="exportObject",value="获取数据参数类",required=true) @RequestBody OrgExportObject exportObject) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
}
