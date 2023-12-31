package com.hotent.im.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.im.persistence.manager.ImMessageSessionManager;
import com.hotent.im.persistence.manager.ImSessionUserManager;
import com.hotent.im.persistence.model.ImSessionUser;
import com.hotent.im.util.ImConstant;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 
 * <pre> 
 * 描述：会话用户 控制器类
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:18
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@Api(tags="即时通讯会话用户")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
@RequestMapping("/im/imSessionUser/v1")
public class ImSessionUserController extends BaseController{
	@Resource
	ImSessionUserManager imSessionUserManager;
	@Resource
	IUserService userManager;
	@Resource
	ImMessageSessionManager imMessageSessionManager;
	@Resource
	CommonManager commonManager;
	
	@RequestMapping(value = "quitTeamSession",method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "退出群组", httpMethod = "POST", notes = "退出群组")
	@ResponseBody
	public Map<String,Object> quitTeamSession(@ApiParam(name="sessionCode",value="会话编号")@RequestParam String sessionCode,@ApiParam(name="owner",value="会话创建人")@RequestParam String owner) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String account = ContextUtil.getCurrentUser().getAccount();
			imSessionUserManager.quitSession(sessionCode,account,owner);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		return map;
	}

	@RequestMapping(value = "remove",method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "逻辑删除会话", httpMethod = "POST", notes = "逻辑删除会话")
	@ResponseBody
	public Map<String,Object> remove(@ApiParam(name="sessionCode",value="会话编号")@RequestParam String sessionCode) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			imSessionUserManager.updateSessionIsShow(ContextUtil.getCurrentUser().getAccount(),sessionCode,ImConstant.SESSION_HIDE);
			map.put("success", true);
			map.put("sessionCode", sessionCode);
		} catch (Exception e) {
			map.put("sucess", false);
		}
		return map;
	}
	
	/**
	 * 获取会话列表
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "mySessionList",method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "我的会话列表", httpMethod = "POST", notes = "我的会话列表")
	@ResponseBody
	public Map<String,Object> mySessionList(@ApiParam(name="imSessionUser",value="请求参数")@RequestBody(required = false) ImSessionUser imSessionUser) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			QueryFilter queryFilter= QueryFilter.build();
			queryFilter.addFilter("isu.user_account_", ContextUtil.getCurrentUser().getAccount(), QueryOP.EQUAL);
			queryFilter.addFilter("isu.is_show_", ImConstant.SESSION_SHOW, QueryOP.EQUAL);
			if(imSessionUser != null && imSessionUser.getSessionCode() != null){
				queryFilter.addFilter("isu.session_code_",imSessionUser.getSessionCode(), QueryOP.EQUAL);
			}
			queryFilter.setPageBean(new PageBean(1, 1000));
			List<ImSessionUser> imSessionUserList=imSessionUserManager.mySessionList(queryFilter);
			List<IUser> users = imSessionUserManager.getSessionUserIcon(imSessionUserList);
			map.put("success", true);
			map.put("users", users);
			map.put("imSessionUserList", imSessionUserList);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "myTeamSessionList",method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "我的群组列表", httpMethod = "POST", notes = "我的群组列表")
	@ResponseBody
	public Map<String,Object> myTeamSessionList() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			QueryFilter queryFilter= QueryFilter.build();
			queryFilter.addFilter("isu.user_account_", ContextUtil.getCurrentUser().getAccount(), QueryOP.EQUAL);
			queryFilter.addFilter("ims.scene_", ImConstant.SESSION_SCENE_TEAM, QueryOP.EQUAL);
			queryFilter.addFilter("isu.is_show_", ImConstant.SESSION_SHOW, QueryOP.EQUAL);
			List<ImSessionUser> imSessionUserList= imSessionUserManager.myTeamSessionList(queryFilter);
			map.put("imSessionUserList", imSessionUserList);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}		
		return map;
	}
	
	@RequestMapping(value = "showSession",method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "显示一个会话", httpMethod = "POST", notes = "显示一个会话")
	@ResponseBody
	public Map<String,Object> showSession(@ApiParam(name="imSessionUser",value="请求参数")@RequestBody(required = false) ImSessionUser imSessionUser) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			QueryFilter queryFilter=QueryFilter.build();
			queryFilter.addFilter("isu.session_code_",imSessionUser.getSessionCode(), QueryOP.EQUAL);
			queryFilter.addFilter("isu.user_account_", ContextUtil.getCurrentUser().getAccount(), QueryOP.EQUAL);
			List<ImSessionUser> imSessionUserList=imSessionUserManager.mySessionList(queryFilter);
			imSessionUserManager.updateSessionIsShow(null,imSessionUser.getSessionCode(), ImConstant.SESSION_SHOW);
			for(ImSessionUser  isu : imSessionUserList){
				isu.setIsShow(ImConstant.SESSION_SHOW);
				isu.setLastReadTime(new Date());
			}
			map.put("success", true);
			map.put("imSessionUserList", imSessionUserList);
		}catch(Exception e){
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping(value = "invitation",method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "邀请人加入群组", httpMethod = "POST", notes = "邀请人加入群组")
	public CommonResult<String> invitation(@ApiParam(name="sessionCode",value="会话编号")@RequestParam String sessionCode,@ApiParam(name="userAccount",value="用户帐号")@RequestParam String userAccount) throws Exception{
		CommonResult<String> commonResult= null;
		try {
			imSessionUserManager.invitation(sessionCode,userAccount);
			commonResult = new CommonResult<String>(true,"邀请成功","");
		} catch (Exception e) {
			commonResult = new CommonResult<String>(false,"邀请失败","");
		}
		return commonResult;
	}
	
	
	@RequestMapping(value = "updateTeamMessage",method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "更新群组信息", httpMethod = "POST", notes = "更新群组信息")
	@ResponseBody
	public Map<String,Object> updateTeamMessage(@ApiParam(name="params",value="请求参数")@RequestBody Map<String,Object> params) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String userAccount = ContextUtil.getCurrentUser().getAccount();
			if(!params.containsKey("sessionCode")){
				map.put("success",false);
			}else{
				if(params.containsKey("userAlias")){
					imSessionUserManager.updateUserAlias(params.getOrDefault("sessionCode","").toString(),params.getOrDefault("userAlias", "").toString(),userAccount);
				}
				if(params.containsKey("icon")){
					String icon = ImConstant.DEFAULT_DOWNLOAD_URL + params.getOrDefault("icon", "").toString();
					params.put("icon", icon);
					map.put("icon", icon);
				}
				imMessageSessionManager.updateTeamMessage(params);
			}
			map.put("success",true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success",false);
		}
		return map;
	}
}
