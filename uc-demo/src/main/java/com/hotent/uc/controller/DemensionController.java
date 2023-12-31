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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.exception.BaseException;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.params.demension.DemensionVo;
import com.hotent.uc.params.user.UserVo;

/**
 * 维度模块接口
 * @author zhangxw
 *
 */
@RestController
@RequestMapping("/api/demension/v1/")
@Api(tags="DemensionController")
public class DemensionController extends BaseController {
	
	/**
	 * 查询维度
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dems/getDemPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取维度列表（带分页信息）", httpMethod = "POST", notes = "获取维度列表")
	public PageList<Demension> getDemPage(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter queryFilter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
     * 获取维度列表
     * @param filter
     * @return
     * @throws Exception
     */
    @RequestMapping(value="dems/getDemList",method=RequestMethod.POST, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "获取维度 列表", httpMethod = "POST", notes = "获取维度列表")
    public List<Demension> getDemList(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter filter) throws Exception{
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }

    /**
     * 获取维度列表
     * @param filter
     * @return
     * @throws Exception
     */
    @RequestMapping(value="dems/getDemListAll",method=RequestMethod.POST, produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value = "获取维度 列表", httpMethod = "POST", notes = "获取维度列表")
    public PageList<Demension> getDemListAll(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter filter) throws Exception{
    	throw new BaseException("uc-demo模块不需要实现该接口");
    }
	
	/**
	 * 获取所有维度列表
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dems/getAll",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有维度列表", httpMethod = "GET", notes = "获取所有维度列表")
	public List<Demension> getAll() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 添加维度
	 * @param dem
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="dem/addDem",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "添加维度", httpMethod = "POST", notes = "添加维度")
	public CommonResult<String> addDem(@ApiParam(name="dem",value="维度参数对象", required = true) @RequestBody DemensionVo dem) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据维度帐号删除维度
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/deleteDem",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据维度编码删除维度", httpMethod = "DELETE", notes = "根据角编码识删除维度")
	public CommonResult<String> deleteDem(@ApiParam(name="codes",value="维度编码（多个用,号隔开）", required = true) @RequestBody(required=false) String codes) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据维度ids删除维度
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/deleteDemByIds",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据维度编码删除维度", httpMethod = "DELETE", notes = "根据角编码识删除维度")
	public CommonResult<String> deleteDemByIds(@ApiParam(name="ids",value="维度id（多个用,号隔开）", required = true) @RequestParam(required=false) String ids) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 更新维度
	 * @param dem
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/updateDem",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新维度", httpMethod = "PUT", notes = "更新维度")
	public CommonResult<String> updateDem(@ApiParam(name="dem",value="维度参数对象", required = true) @RequestBody  DemensionVo dem) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 获取维度信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/getDem",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据维度编码获取维度信息", httpMethod = "GET", notes = "获取维度信息")
	public Demension getDem(@ApiParam(name="code",value="维度编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取指定维度下的所有人员
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="demUser/getUsersByDem",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定维度下的所有人员", httpMethod = "GET", notes = "获取指定维度下的所有人员")
	public List<UserVo> getUsersByDem(@ApiParam(name="code",value="维度编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取指定维度下的所有组织
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="demUser/getOrgsByDem",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取指定维度下的所有组织", httpMethod = "GET", notes = "获取维度下的所有组织")
	public List<Org> getOrgsByDem(@ApiParam(name="code",value="维度编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 设置默认维度
	 * @param dem
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/setDefaultDem",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "设置默认维度", httpMethod = "PUT", notes = "设置默认维度")
	public CommonResult<String> setDefaultDem(@ApiParam(name="code",value="维度编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 取消默认维度
	 * @param dem
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/cancelDefaultDem",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "取消默认维度", httpMethod = "PUT", notes = "取消默认维度")
	public CommonResult<String> cancelDefaultDem(@ApiParam(name="code",value="维度编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取默认维度信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/getDefaultDem",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取默认维度信息", httpMethod = "GET", notes = "获取默认维度信息")
	public Demension getDefaultDem() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 物理删除所有逻辑删除了的维度数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/deleteDemPhysical",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "物理删除所有逻辑删除了的维度数据", httpMethod = "DELETE", notes = "物理删除所有逻辑删除了的维度数据")
	public com.hotent.base.model.CommonResult<Integer> deleteDemPhysical() throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 根据时间获取维度数据（数据同步）
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dems/getDemByTime",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据时间获取维度数据（数据同步）", httpMethod = "GET", notes = "根据时间获取维度数据（数据同步）")
	public List<Demension> getDemByTime(@ApiParam(name="btime",value="开始时间（格式：2018-01-01 12:00:00或2018-01-01）") @RequestParam(required=false) String btime,@ApiParam(name="etime",value="结束时间（格式：2018-02-01 12:00:00或2018-02-01）") @RequestParam(required=false) String etime) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 验证编码唯一性
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/isCodeExist",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询维度编码是否已存在", httpMethod = "GET", notes = "查询维度编码是否已存在")
	public CommonResult<Boolean> isCodeExist(@ApiParam(name="code",value="维度编码")
												@RequestParam(required=true) String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 查询所有机构各维度数据，并且按照从属关系组装
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dem/getOrgSelectListInit",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "初始化组织选择器控件数据", httpMethod = "GET", notes = "查询所有机构各维度数据，并且按照从属关系组装")
	public ObjectNode getOrgSelectListInit(@RequestParam(required=true) String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
}
