package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

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
import com.hotent.uc.model.OrgParams;
import com.hotent.uc.model.Params;
import com.hotent.uc.model.UserParams;
import com.hotent.uc.params.params.ParamVo;

/**
 * 参数模块接口
 * @author zhangxw
 *
 */
@RestController
@RequestMapping("/api/params/v1/")
@Api(tags="ParamsController")
public class ParamsController extends BaseController {
	
	/**
	 * 查询所有参数
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="params/getParamsPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取参数列表（带分页信息）", httpMethod = "POST", notes = "获取参数列表")
	public PageList<Params> getParamsPage(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter filter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取用户参数列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userParams/getUserParams",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户的参数列表", httpMethod = "GET", notes = "获取用户的参数列表")
	public List<Params> getUserParams() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取组织参数列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgParams/getOrgParams",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织的参数列表", httpMethod = "GET", notes = "获取组织的参数列表")
	public List<Params> getOrgParams() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 添加参数
	 * @param param
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="param/addParams",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "添加参数", httpMethod = "POST", notes = "添加参数")
	public CommonResult<String> addParams(@ApiParam(name="param",value="参数参数对象", required = true) @RequestBody ParamVo param) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据参数编码删除参数
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="param/deleteParams",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据参数编码删除参数", httpMethod = "POST", notes = "根据角编码识删除参数")
	public CommonResult<String> deleteParams(@ApiParam(name="codes",value="参数编码（多个用,号隔开）", required = true) @RequestBody String codes) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据参数ids删除参数
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="param/deleteParamsByIds",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据参数id删除参数", httpMethod = "DELETE", notes = "根据id删除参数")
	public CommonResult<String> deleteParamsByIds(@ApiParam(name="ids",value="参数id（多个用,号隔开）", required = true) @RequestParam String ids) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	
	/**
	 * 更新参数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="param/updateParams",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新参数，参数类型不予更新", httpMethod = "PUT", notes = "更新参数，参数类型不予更新")
	public CommonResult<String> updateParams(@ApiParam(name="param",value="参数参数对象", required = true) @RequestBody  ParamVo param) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 获取参数信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="param/getParams",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据参数编码获取参数信息", httpMethod = "GET", notes = "获取参数信息")
	public Params getParams(@ApiParam(name="code",value="参数编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 通过用户账号获取用户指定参数
	 * @param account
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userParam/getUserParamsByCode",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户指定参数", httpMethod = "GET", notes = "获取用户指定参数")
	public UserParams getUserParamsByCode(@ApiParam(name="account",value="用户账户", required = true) @RequestParam String account,@ApiParam(name="code",value="参数编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 通过用户ID获取用户指定参数
	 * @param userId
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userParam/getUserParamsById",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户指定参数", httpMethod = "GET", notes = "获取用户指定参数")
	public UserParams getUserParamsById(@ApiParam(name="userId",value="用户ID", required = true) @RequestParam String userId, @ApiParam(name="code",value="参数编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 通过组织代码获取组织指定参数
	 * @param orgCode
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgParam/getOrgParamsByCode",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织指定参数", httpMethod = "GET", notes = "获取组织指定参数")
	public OrgParams getOrgParamsByCode(@ApiParam(name="orgCode",value="组织编码", required = true) @RequestParam String orgCode,@ApiParam(name="code",value="参数编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 通过组织ID获取组织指定参数
	 * @param orgCode
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgParam/getOrgParamsById",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取组织指定参数", httpMethod = "GET", notes = "获取组织指定参数")
	public OrgParams getOrgParamsById(@ApiParam(name="orgId",value="组织编码", required = true) @RequestParam String orgId,@ApiParam(name="code",value="参数编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 物理删除所有逻辑删除了的参数数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="Param/deleteParamPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的参数", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的参数")
	public com.hotent.base.model.CommonResult<Integer> deleteParamPhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 物理删除所有逻辑删除了的用户参数数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="userParam/deleteUserParamPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的用户参数数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的用户参数数据")
	public com.hotent.base.model.CommonResult<Integer> deleteUserParamPhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 物理删除所有逻辑删除了的组织参数数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="orgParam/deleteOrgParamPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的组织参数数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的组织参数数据")
	public com.hotent.base.model.CommonResult<Integer> deleteOrgParamPhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据时间获取用户组织参数数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="params/getParamsByTime",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取用户组织参数数据（数据同步）", httpMethod = "GET", notes = "根据时间获取用户组织参数数据（数据同步）")
	public List<Params> getParamsByTime(@ApiParam(name="btime",value="开始时间（格式：2018-01-01 12:00:00或2018-01-01）") @RequestParam(required=false) String btime,@ApiParam(name="etime",value="结束时间（格式：2018-02-01 12:00:00或2018-02-01）") @RequestParam(required=false) String etime) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="params/isCodeExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询用户组织参数编码是否已存在", httpMethod = "GET", notes = "查询用户组织参数编码是否已存在")
	public CommonResult<Boolean> isCodeExist(@ApiParam(name="code",value="用户组织参数编码")
												@RequestParam(required=true) String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
}
