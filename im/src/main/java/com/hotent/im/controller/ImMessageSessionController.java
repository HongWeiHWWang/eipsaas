package com.hotent.im.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.JsonUtil;
import com.hotent.im.persistence.manager.ImMessageHistoryManager;
import com.hotent.im.persistence.manager.ImMessageSessionManager;
import com.hotent.im.persistence.manager.ImSessionUserManager;
import com.hotent.im.persistence.model.ImMessageHistory;
import com.hotent.im.persistence.model.ImMessageSession;
import com.hotent.im.persistence.model.ImSessionUser;
import com.hotent.im.service.MqttSendService;
import com.hotent.im.util.ImConstant;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.service.IUserService;


/**
 * 
 * <pre> 
 * 描述：聊天会话 控制器类
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:44:36
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RequestMapping("/im/imMessageSession/v1")
@RestController
@Api(tags="即时通讯会话")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class ImMessageSessionController extends BaseController{
	@Resource
	ImMessageSessionManager imMessageSessionManager;
	@Resource
	ImMessageHistoryManager imMessageHistoryManager;
	@Resource
	MqttSendService mqttSendService;
	@Resource
	ImSessionUserManager imSessionUserManager;
	@Resource
	IUserService userManager;
	
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
	
	
	/**
	 * 发起一个会话,p2p和 team
	 * createSession
	 */
	@RequestMapping(value = "createSession",method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "创建会话", httpMethod = "POST", notes = "创建会话")
	@ResponseBody
	public Map<String,Object> createSession(@ApiParam(name="scene",value="会话类型")@RequestParam String scene,@ApiParam(name="userStr",value="用户帐号")@RequestParam String userStr) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		ImMessageSession imMessageSession = new ImMessageSession();
		try {
			switch (scene) {
				case ImConstant.SESSION_SCENE_TEAM:
					imMessageSession = imMessageSessionManager.createTeamSession(scene,userStr);
					break;
				case ImConstant.SESSION_SCENE_P2P:
					imMessageSession = imMessageSessionManager.createP2PSession(scene,userStr);
					break;
				default:
					throw new Exception("没有创建会话类型");
				}
			map.put("success", true);
			map.put("imMessageSession", imMessageSession);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	
	@RequestMapping(value = "initSession",method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "创建会话", httpMethod = "GET", notes = "创建会话")
	@ResponseBody
	public Map<String,Object> initSession(@ApiParam(name="scene",value="会话编号")@RequestParam String sessionCode) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		ImMessageSession imMessageSession = new ImMessageSession();
		//messageList.add(new ImMessageHistory());
		try {
			String account = current();
			imMessageSession = imMessageSessionManager.getByCodeAndUser(sessionCode,account);
			if(imMessageSession == null){
				throw new Exception("没找到对应的的会话");
			}
			Date joinTime = null;
			//会话的人
			List<ImSessionUser> imSessionUserList = imSessionUserManager.getUserBySessionCode(sessionCode);
			Map<String,Object> users = new HashMap<String,Object>();
			for(ImSessionUser imSessionUser : imSessionUserList){
				if(account.equals(imSessionUser.getUserAccount())){
					imSessionUser.setLastReadTime(new Date());
					joinTime = imSessionUser.getJoinTime();
				}
				users.put(imSessionUser.getUserAccount(), imSessionUser);
			}
			map.put("sessionUsers", users);
			map.put("currentUser", account);
			map.put("session", imMessageSession);
			map.put("initSessionTime",LocalDateTime.now());
			map.put("success",true);
			map.put("extraDestination",ImConstant.MQTT_RECEIVE_DESTINATION);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("message", "初始化会话失败");
		map.put("success",false);
		return map;
	}
	
	@RequestMapping(value = "refreshSessionHistory",method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取历史消息", httpMethod = "GET", notes = "获取历史消息")
	@ResponseBody
	public Map<String,Object> refreshSessionHistory(@ApiParam(name="params",value="参数")@RequestBody(required=false) ArrayNode arrayNode) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			Map<String,Object> m = new HashMap<String,Object>();
			for (JsonNode o : arrayNode) {
				try {
					String sessionCode = o.get("sessionCode").asText();
					QueryFilter filter = QueryFilter.build();
					filter.setPageBean(new PageBean(1, 1000));
					if(o.has("sendTime")){
						filter.addFilter("imh.send_time_",new Date(o.get("sendTime").asLong()), QueryOP.GREAT);
					}
					filter.addFilter("imh.session_code_", sessionCode, QueryOP.IN);
					List<ImMessageHistory> messageList = imMessageHistoryManager.getMsgHistory(filter);
					m.put(sessionCode, messageList);
				} catch (Exception e) {
					continue;
				}
			}
			map.put("history",m);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "initTeamSessionDetail",method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取群组信息", httpMethod = "GET", notes = "获取群组信息")
	@ResponseBody
	public Map<String,Object> initTeamSessionDetail(@ApiParam(name="param",value="参数")@RequestParam String sessionCode) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			ImMessageSession imMessageSession = imMessageSessionManager.getTeamSessionDetail(sessionCode);
			String userAlias = imSessionUserManager.getUserAlias(sessionCode,ContextUtil.getCurrentUser().getAccount());
			String ownerName = imSessionUserManager.getUserAlias(sessionCode, imMessageSession.getOwner());
			imMessageSession.setOwnerName(ownerName);
			imMessageSession.setUserAlias(userAlias);
			List<ImSessionUser> userList = imSessionUserManager.getUserBySessionCode(sessionCode);
			map.put("success", true);
			map.put("userList", userList);
			map.put("userAlias", userAlias);
			map.put("imMessageSession", imMessageSession);
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", "获取群组设置失败");
			e.printStackTrace();
		}
		return map;
	}
}
