package com.hotent.bpmModel.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.manager.BpmDefAuthorizeManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.model.BpmDefAuthorize;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



/**
 * 对象功能:流程分管授权  控制器类
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/flow/defAuthorize/v1/")
@Api(tags="流程分管授权")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class  DefAuthorizeController extends BaseController<BpmDefAuthorizeManager, BpmDefAuthorize>{
	@Resource
	private BpmDefinitionManager bpmDefinitionManager;	
	
	/**
	 * 取得流程定义权限列表
	 * TODO方法名称描述
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception 
	 * PageJson
	 */
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "取得流程定义权限列表", httpMethod = "POST", notes = "取得流程定义权限列表")
	public PageList<BpmDefAuthorize> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<BpmDefAuthorize> queryFilter)throws Exception{
		PageList<BpmDefAuthorize> pageList = baseService.getAuthorizeListByFilter(queryFilter);
		return pageList;
	}
	
	/**
	 * 保存新增或修改授权信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存新增或修改授权信息", httpMethod = "POST", notes = "保存新增或修改授权信息")
	public CommonResult<String> save(
			@ApiParam(name="author",value="授权信息", required = true) @RequestBody BpmDefAuthorize author) throws Exception {
		//用户
		IUser user = ContextUtil.getCurrentUser();
		String id=author.getId();
		//用ID判断是修改还是新增
		if(StringUtil.isNotEmpty(id)){
			author.setId(id);
		}else{
			author.setId("");
			//增加流程分管授权查询判断
			author.setCreator(user.getFullname());
		}
		
		String myId = baseService.saveOrUpdateAuthorize(author);
		if(StringUtil.isNotEmpty(myId)){
			return new CommonResult<String>(true,"保存授权信息成功！","");
		}else{
			return new CommonResult<String>(false,"保存授权信息失败！","");
		}
	}
	
	
	/**
	 * 删除流程分管授权信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="del",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除流程分管授权信息", httpMethod = "DELETE", notes = "删除流程分管授权信息")
	public CommonResult<String> del(
			@ApiParam(name="ids",value="授权信息id字符串", required = true) @RequestParam String ids) throws Exception {
		try{
			String[] aryIds = ids.split(",");
			baseService.deleteAuthorizeByIds(aryIds);
		
			return new CommonResult<String>(true,"删除授权信息成功！","");
		}catch(Exception ex){
			return new CommonResult<String>(false,"删除授权信息失败！","");
		}
	}
	
	
	/**
	 * 获得流程分管授权详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="defAuthorizeGet",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获得流程分管授权详情", httpMethod = "GET", notes = "获得流程分管授权详情")
	public Object defAuthorizeGet(
			@ApiParam(name="id",value="分管授权id", required = true) @RequestParam String id) throws Exception {
		BpmDefAuthorize bpmDefAuthorize = null;
		if(StringUtil.isNotEmpty(id)){
			bpmDefAuthorize = baseService.getAuthorizeById(id);
		}else{
			bpmDefAuthorize =new BpmDefAuthorize();
		}
		return bpmDefAuthorize;
	}	
	
	/**
	 * 获得默认权限类型
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getPermissionList",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获得默认权限类型", httpMethod = "GET", notes = "获得默认权限类型")
	public Object getPermissionList() throws Exception {
		return AppUtil.getBean("defaultObjectRightType");
	}	
	
}
