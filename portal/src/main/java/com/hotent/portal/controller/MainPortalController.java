package com.hotent.portal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.portal.model.SysIndexLayoutManage;
import com.hotent.portal.model.SysIndexMyLayout;
import com.hotent.portal.persistence.manager.SysIndexLayoutManageManager;
import com.hotent.portal.persistence.manager.SysIndexMyLayoutManager;
import com.hotent.portal.persistence.manager.SysLayoutSettingManager;
import com.hotent.portal.persistence.manager.SysLayoutToolsManager;
import com.hotent.portal.persistence.manager.SysMessageManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/portal/main/v1/")
@Api(tags="门户布局")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class MainPortalController {
	@Resource
	SysMessageManager sysMessageManager;
	@Resource
	SysIndexMyLayoutManager sysIndexMylayoutService;
	@Resource
	SysIndexLayoutManageManager sysIndexlayoutManageService;
	@Resource
	SysLayoutSettingManager sysLayoutSettingManager;
	@Resource
	SysLayoutToolsManager sysLayoutToolsManager;
	@Resource 
	IUserService ius;
	@Resource
	UCFeignService ucFeignService;
	
	/**
	 * 管理端的布局
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="myHome",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "管理端的布局", httpMethod = "GET", notes = "管理端的布局")
	public CommonResult<String> myHome() throws Exception {
		SysIndexLayoutManage layout = this.traceLayout(SysIndexLayoutManage.TYPE_MNG);
		return new CommonResult<String>(true,"获取成功！",layout==null ? "" : layout.getDesignHtml());
	}
	/**
	 * 应用端的布局
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="vueFrontHome",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "应用端的布局", httpMethod = "GET", notes = "应用端的布局")
	public CommonResult<String> myVueFrontHome() throws Exception {
		String designHtml =null;
		//先查询个人门户里面是否有布局
		QueryFilter<SysIndexMyLayout> query=QueryFilter.build();
		query.addFilter("USER_ID", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
		query.addFilter("VALID_", 1, QueryOP.EQUAL);
		List<SysIndexMyLayout> SysIndexMyLayouts = sysIndexMylayoutService.queryNoPage(query);
		if(BeanUtils.isNotEmpty(SysIndexMyLayouts)&& SysIndexMyLayouts.size()>0){
			designHtml = SysIndexMyLayouts.get(0).getDesignHtml();
		}else{
			SysIndexLayoutManage layout = this.traceLayout(SysIndexLayoutManage.TYPE_APPLICATION);
			if(BeanUtils.isEmpty(layout)){
				designHtml = "";
			}else{
				designHtml = layout.getDesignHtml();
			}
		}
		return new CommonResult<String>(true,"获取成功！",designHtml);
	}
	/**
	 * 手机端的布局
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="myMobileHome",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "手机端的布局", httpMethod = "GET", notes = "手机端的布局")
	public CommonResult<String> myMobileHome() throws Exception {
		SysIndexLayoutManage layout = this.traceLayout(SysIndexLayoutManage.TYPE_MOBILE);
		return new CommonResult<String>(true,"获取成功！",layout==null ? "" : layout.getDesignHtml());
	}
	
	/**
	 * 根据规则查询出布局
	 * @param layoutType 布局类型：
	 * @return
	 * @throws IOException 
	 */
	private SysIndexLayoutManage traceLayout(Short layoutType) throws IOException {
		
		IUser user = ContextUtil.getCurrentUser();
		SysIndexLayoutManage layout =null;
		//1、获取主岗位、主组织（优先获取默认维度的主岗位、主组织，没有时获取其他维度的）；
		ObjectNode mainPost = ucFeignService.getMainPostOrOrgByUserId(user.getUserId());
		if(BeanUtils.isNotEmpty(mainPost)){
			String orgId = JsonUtil.getString(mainPost, "orgId");
			layout = sysIndexlayoutManageService.getEnableByOrgIdAndType(orgId, layoutType);
		}
		//2、从父部门共享过来的；
		if(BeanUtils.isEmpty(layout)){
			ArrayNode myOrgs = ucFeignService.getOrgListByUserId(user.getUserId());
			Set<String> shareOrgIds = new HashSet<>();
			if(BeanUtils.isNotEmpty(myOrgs)){
				for (Object org : myOrgs) {
					String path = JsonUtil.toJsonNode(org).get("path").asText();
					path = path.substring(0, path.length()-1);
					String[] pathArr = path.replace(".", ",").split(",");
					for(int i=pathArr.length-1;i >=0;i--){
						shareOrgIds.add(pathArr[i]);
					}
				}
				if(shareOrgIds.size() > 0) {
					layout = sysIndexlayoutManageService.getSharedByOrgIds(new ArrayList<>(shareOrgIds), layoutType);
				}
			}
		}
		//3、如果从部门树都查询不到布局，则获取系统默认布局。该默认布局系统初始化就有。
		if(BeanUtils.isEmpty(layout)){
			try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
				if(SysIndexLayoutManage.TYPE_MNG == layoutType){
					layout = sysIndexlayoutManageService.get(SysIndexLayoutManage.MNG_DEFAULT_ID);
				}else if(SysIndexLayoutManage.TYPE_MOBILE == layoutType){
					layout = sysIndexlayoutManageService.get(SysIndexLayoutManage.MOBILE_DEFAULT_ID);
				}else if(SysIndexLayoutManage.TYPE_APPLICATION == layoutType){
					layout = sysIndexlayoutManageService.get(SysIndexLayoutManage.APPLICATION_DEFAULT_ID);
				}
			}
			catch(Exception e) {
				throw new BaseException(e.getMessage());
			}
		}
		return layout;
	}

	@RequestMapping(value="/appProperties",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取手机app配置", httpMethod = "GET", notes = "获取手机app配置")
	public ResponseEntity<?> appProperties() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			/*MqttProperty mqttProperty = AppUtil.getBean(MqttProperty.class);
			map.put("host",mqttProperty.getHost());
			map.put("port",mqttProperty.getPort());
			map.put("initServerTime",System.currentTimeMillis());
			map.put("appVersion",SysPropertyUtil.getByAlias("app.version",""));*/
		} catch (Exception e) {
			e.printStackTrace();
			ResponseEntity.badRequest().body(map);
		}
		return ResponseEntity.ok(map);
	}
	
	
	@RequestMapping(value="getOnLineCount",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取当前在线人数", httpMethod = "GET", notes = "获取当前在线人数")
	public String getOnLineCount(HttpServletRequest httpServletRequest){
		HttpSession session = httpServletRequest.getSession();
        Object count=session.getServletContext().getAttribute("count");
        if(BeanUtils.isEmpty(count)){
        	count = "1";
        }
        return (String) count;
	}
	
	@RequestMapping(value="getCurrentUser",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取当前用户", httpMethod = "GET", notes = "获取当前用户")
	public IUser getCurrentUser(HttpServletRequest httpServletRequest){
		IUser curUser = ContextUtil.getCurrentUser();
		return curUser;
	}
	
	@RequestMapping(value="getImHost",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取im服务配置", httpMethod = "GET", notes = "获取im服务配置")
	public Map<String,Object> getImHost(HttpServletRequest httpServletRequest){
		Map<String,Object> map = new HashMap<String,Object>();
		/*MqttProperty mqttProperty = AppUtil.getBean(MqttProperty.class);
		map.put("host",mqttProperty.getHost());
		map.put("port",mqttProperty.getPort());*/
		map.put("account",ContextUtil.getCurrentUser().getAccount());
		map.put("initServerTime",System.currentTimeMillis());
		return map;
	}
}