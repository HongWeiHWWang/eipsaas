package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgAuthManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.demension.DemensionVo;
import com.hotent.uc.params.user.UserVo;
import com.hotent.uc.util.AuthFilterUtil;
import com.hotent.uc.util.OrgUtil;
import com.hotent.uc.util.UpdateMethod;

/**
 * 维度模块接口
 * @author zhangxw
 *
 */
@RestController
@RequestMapping("/api/demension/v1/")
@Api(tags="维度管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_UC})
public class DemensionController extends BaseController<DemensionManager, Demension> {
	
	@Resource
	DemensionManager demensionService;
	@Autowired
	OrgAuthManager orgAuthService;
	
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
		AuthFilterUtil.setDemAuthFilter(queryFilter);
		PageList<Demension> query = demensionService.query(queryFilter);
    	return query;
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
        AuthFilterUtil.setDemAuthFilter(filter);
        PageList<Demension> query = demensionService.query(filter);
        return query.getRows();
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
        AuthFilterUtil.setDemAuthFilter(filter);
        PageList<Demension> query = demensionService.query(filter);
        return query;
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
		QueryFilter filter = QueryFilter.build();
		//filter.setClazz(Demension.class);
		AuthFilterUtil.setDemAuthFilter(filter);
		PageList<Demension> query = demensionService.query(filter);
		return query.getRows();
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
		return demensionService.addDem(dem);
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
		return demensionService.deleteDem(codes);
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
		return demensionService.deleteDemByIds(ids);
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
	@UpdateMethod(type=DemensionVo.class)
	public CommonResult<String> updateDem(@ApiParam(name="dem",value="维度参数对象", required = true) @RequestBody  DemensionVo dem) throws Exception{
		return demensionService.updateDem(dem);
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
		return demensionService.getByCode(code);
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
		return demensionService.getUsersByDem(code);
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
		return demensionService.getOrgsByDem(code);
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
		return demensionService.setDefaultDem(code);
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
		return demensionService.cancelDefaultDem(code);
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
		return demensionService.getDefaultDemension();
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
		Integer num = demensionService.removePhysical();
		return OrgUtil.getRemovePhysiMsg(num);
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
		OrgExportObject exportObject = new OrgExportObject();
		exportObject.setBtime(btime);
		exportObject.setEtime(etime);
		return demensionService.getDemByTime(exportObject);
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
		return demensionService.isCodeExist(code);
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
		return demensionService.getOrgSelectListInit(code);
	}
	
}
