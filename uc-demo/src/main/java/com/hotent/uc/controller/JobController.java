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
import com.hotent.uc.mock.MockUCDataUtil;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.params.job.JobVo;
import com.hotent.uc.params.user.UserVo;

/**
 * 职务模块接口
 * @author zhangxw
 *
 */
@RestController
@RequestMapping("/api/job/v1/")
@Api(tags="JobController")
public class JobController extends BaseController{
	
	
	/**
	 * 查询职务
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="jobs/getJobPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取职务列表（带分页信息）", httpMethod = "POST", notes = "获取职务列表")
	public PageList<OrgJob> getJobPage(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter filter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取职务列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="jobs/getJobList",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取职务列表", httpMethod = "POST", notes = "获取职务列表")
	public List<OrgJob> getJobList(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter filter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 添加职务
	 * @param job
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="job/addJob",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "添加职务", httpMethod = "POST", notes = "添加职务")
	public CommonResult<String> addJob(@ApiParam(name="job",value="职务参数对象", required = true) @RequestBody JobVo job) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据职务帐号删除职务
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="job/deleteJob",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据职务编码删除职务", httpMethod = "POST", notes = "根据角编码识删除职务")
	public CommonResult<String> deleteJob(@ApiParam(name="codes",value="职务编码（多个用,号隔开）", required = true) @RequestBody String codes) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据职务id删除职务
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="job/deleteJobByIds",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据职务id删除职务", httpMethod = "DELETE", notes = "根据职务id删除职务")
	public CommonResult<String> deleteJobByIds(@ApiParam(name="ids",value="职务id（多个用,号隔开）", required = true) @RequestParam String ids) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 更新职务
	 * @param job
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="job/updateJob",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新职务", httpMethod = "PUT", notes = "更新职务")
	public CommonResult<String> updateJob(@ApiParam(name="job",value="职务参数对象", required = true) @RequestBody  JobVo job) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 获取职务信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="job/getJob",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据职务编码或id获取职务信息", httpMethod = "GET", notes = "获取职务信息")
	public OrgJob getJob(@ApiParam(name="code",value="职务编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取职务信息（返回CommonResult<OrgJob>）
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="job/getOrgJob",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据职务编码或id获取职务信息", httpMethod = "GET", notes = "获取职务信息")
	public CommonResult<OrgJob> getOrgJob(@ApiParam(name="code",value="职务编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取职务（多个）下的所有人员
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="jobUser/getUsersByJob",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取职务（多个）下的所有人员", httpMethod = "GET", notes = "获取职务下的所有人员")
	public List<UserVo> getUsersByJob(@ApiParam(name="codes",value="职务编码（多个用“,”号隔开）", required = true) @RequestParam String codes) throws Exception{
		//return orgJobService.getUsersByJob(codes);
		return MockUCDataUtil.getUsersByJob(codes);
	}
	
	/**
	 * 物理删除所有逻辑删除了的职务数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="job/deleteJobPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的职务数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的职务数据")
	public com.hotent.base.model.CommonResult<Integer> deleteJobPhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据时间获取职务数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="jobs/getJobByTime",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取职务数据（数据同步）", httpMethod = "GET", notes = "根据时间获取职务数据（数据同步）")
	public List<OrgJob> getJobByTime(@ApiParam(name="btime",value="开始时间（格式：2018-01-01 12:00:00或2018-01-01）") @RequestParam(required=false) String btime,@ApiParam(name="etime",value="结束时间（格式：2018-02-01 12:00:00或2018-02-01）") @RequestParam(required=false) String etime) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	@RequestMapping(value="job/isCodeExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询职务编码是否已存在", httpMethod = "GET", notes = "查询职务编码是否已存在")
	public CommonResult<Boolean> isCodeExist(@ApiParam(name="code",value="职务编码")
												@RequestParam(required=true) String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
}
