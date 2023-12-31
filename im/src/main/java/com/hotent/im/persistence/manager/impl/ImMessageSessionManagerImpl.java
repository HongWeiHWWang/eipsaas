package com.hotent.im.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.dao.MyBatisDao;
import com.hotent.base.manager.impl.AbstractManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.im.persistence.dao.ImMessageSessionDao;
import com.hotent.im.persistence.manager.ImGeneralContactManager;
import com.hotent.im.persistence.manager.ImMessageHistoryManager;
import com.hotent.im.persistence.manager.ImMessageSessionManager;
import com.hotent.im.persistence.manager.ImSessionUserManager;
import com.hotent.im.persistence.model.ImMessageHistory;
import com.hotent.im.persistence.model.ImMessageSession;
import com.hotent.im.persistence.model.ImSessionUser;
import com.hotent.im.service.MqttSendService;
import com.hotent.im.util.ImConstant;
import com.hotent.im.util.ImMqttUtil;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 
 * <pre> 
 * 描述：聊天会话 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:44:37
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("imMessageSessionManager")
public class ImMessageSessionManagerImpl extends AbstractManagerImpl<String, ImMessageSession> implements ImMessageSessionManager{
	@Resource
	ImMessageSessionDao imMessageSessionDao;
	@Resource
	ImSessionUserManager imSessionUserManager;
	@Resource
	IUserService userManager;
	@Resource
	ImMessageHistoryManager imMessageHistoryManager;
	@Resource
	ImGeneralContactManager imGeneralContactManager;
	@Resource
	MqttSendService mqttSendService;
	
	@Override
	protected MyBatisDao<String, ImMessageSession> getDao() {
		return imMessageSessionDao;
	}
	
	@Override
	public ImMessageSession getBySessionCode(String code) {
		List<ImMessageSession> list = imMessageSessionDao.getBySessionCode(code);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 创建点对点聊天会话
	 * zhangsan,lisi
	 */
	@Override
	public ImMessageSession createP2PSession(String scene, String userStr) throws Exception{
		IUser target = userManager.getUserByAccount(userStr);
		if(target == null){
			throw new Exception("用户不存在");
		}
		List<IUser> userList = new ArrayList<IUser>();
		userList.add(target);
		//加入当前用户
		IUser currentUser = (UserFacade) ContextUtil.getCurrentUser();
		userList.add(currentUser);
		String code = ImMqttUtil.generateP2PSessionCode(userList);
		//先判断是否已经存在会话
		List<ImMessageSession> imMessageSessions = imMessageSessionDao.getBySessionCode(code);
		ImMessageSession imMessageSession = null;
		if(imMessageSessions.size() == 0){
			imMessageSession = new ImMessageSession();
			imMessageSession.setId(UniqueIdUtil.getSuid());
			imMessageSession.setCode(code);
			imMessageSession.setTitle(target.getFullname());
			imMessageSession.setIcon("");
			imMessageSession.setScene(ImConstant.SESSION_SCENE_P2P);
			imMessageSession.setCreateTime(new Date());
			imMessageSession.setOwner(currentUser.getAccount());
			imMessageSessionDao.create(imMessageSession);
			createSessionUser(imMessageSession.getCode(),target,currentUser.getFullname());
			createSessionUser(imMessageSession.getCode(),currentUser,target.getFullname());
			//创建常用联系人
			imGeneralContactManager.createContact(currentUser,target);
		}else{
			imMessageSession = imMessageSessions.get(0);
			imSessionUserManager.updateSessionIsShow(currentUser.getAccount(),imMessageSession.getCode(), ImConstant.SESSION_SHOW);
		}
		return imMessageSession;
	}
	
	/**
	 * 创建群聊会话
	 *  zhangsan,lisi
	 */
	@Override
	public ImMessageSession createTeamSession(String scene, String userStr) throws Exception{
		List<IUser> userList = userManager.getUserByAccounts(userStr);
		for(IUser map : userList){
			
		}
		if(userList.size() == 0){
			throw new Exception("用户不存在");
		}
		//加入当前用户
		IUser currentUser = ContextUtil.getCurrentUser();
		boolean f = true;
		for(IUser user : userList){
			if(user.getUserId().equals(currentUser.getUserId())){
				f = false;
				break;
			}
		}
		if(f) userList.add(currentUser);
		String code = ImMqttUtil.generateTeamSessionCode(userList);
		//先判断是否已经存在会话
		List<ImMessageSession> imMessageSessions = imMessageSessionDao.getBySessionCode(code);
		ImMessageSession imMessageSession = null;
		if(imMessageSessions.size() == 0){
			String title = ImMqttUtil.generateSessionTile(userList);
			imMessageSession = new ImMessageSession();
			imMessageSession.setId(UniqueIdUtil.getSuid());
			imMessageSession.setCode(code);
			imMessageSession.setTitle(title);
			imMessageSession.setIcon("");
			imMessageSession.setScene(ImConstant.SESSION_SCENE_TEAM);
			imMessageSession.setCreateTime(new Date());
			imMessageSession.setOwner(currentUser.getAccount());
			imMessageSessionDao.create(imMessageSession);
			for(IUser user : userList){
				createSessionUser(imMessageSession.getCode(),user,title);
			}
		}else{
			imMessageSession = imMessageSessions.get(0);
			imSessionUserManager.updateSessionIsShow(currentUser.getAccount(),imMessageSession.getCode(), ImConstant.SESSION_SHOW);
		}
		return imMessageSession;
	}
	
	/**
	 * 创建session 用户的关联表
	 * @param sessionId
	 * @param currentUserId
	 * @param userArray
	 * @throws Exception
	 */
	private void createSessionUser(String sessionCode,IUser user,String sessionTitle) throws Exception{
		
		ImSessionUser imSessionUser = new ImSessionUser();
		imSessionUser.setId(UniqueIdUtil.getSuid());
		imSessionUser.setSessionCode(sessionCode);
		imSessionUser.setIsShow(ImConstant.SESSION_SHOW);
		imSessionUser.setLastReadTime(new Date());
		imSessionUser.setJoinTime(new Date());
		imSessionUser.setUserAccount(user.getAccount());
		imSessionUser.setUserAlias(user.getFullname());
		imSessionUser.setSessionTitle(sessionTitle);
		imSessionUser.setFrom(ContextUtil.getCurrentUser().getAccount());
		imSessionUserManager.create(imSessionUser);
	}
	
	
	
	@Override
	public void updateLastText(String sessionCode, String text) {
		imMessageSessionDao.updateLastText(sessionCode,text);
	}
	@Override
	public ImMessageSession getByCodeAndUser(String sessionCode,
			String account) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sessionCode",sessionCode);
		params.put("userAccount", account);
		List<ImMessageSession> list = imMessageSessionDao.getByCodeAndUser(params);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public ImMessageSession getTeamSessionDetail(String sessionCode) {
		
		QueryFilter filter = QueryFilter.build();
		filter.addFilter("code_", sessionCode, QueryOP.EQUAL);
		Map<String,Object> params = filter.getParams();
		String fieldSql = "code_,title_,icon_,scene_,create_time_,owner_,description_";
		params.put("fieldSql", fieldSql);
		List<ImMessageSession> imMessageSession = imMessageSessionDao.getByDynamicSql(params);
		if(imMessageSession.size() > 0){
			return imMessageSession.get(0);
		}
		return null;
	}
	@Override
	public void updateTeamMessage(Map<String, Object> params) {
		if(params.containsKey("title")){
			imMessageSessionDao.updateTitle(params);
			imSessionUserManager.updateSessionTitle(params.get("sessionCode").toString(),params.get("title").toString());
		}
		if(params.containsKey("description")){
			imMessageSessionDao.updateDescription(params);
		}
		if(params.containsKey("icon")){
			imMessageSessionDao.updateIcon(params);
		}
	}
	@Override
	public Map<String, Object> getSessionHistory(String sessionCode,String account) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		ImMessageSession imMessageSession = new ImMessageSession();
		String currentAccount = ContextUtil.getCurrentUser().getAccount();
		imMessageSession = this.getByCodeAndUser(sessionCode,currentAccount);
		if(imMessageSession == null){
			throw new Exception("没找到对应的的会话");
		}
		Date joinTime = null;
		//会话的人
		List<ImSessionUser> imSessionUserList = imSessionUserManager.getUserBySessionCode(sessionCode);
		Map<String,Object> users = new HashMap<String,Object>();
		for(ImSessionUser imSessionUser : imSessionUserList){
			if(currentAccount.equals(imSessionUser.getUserAccount())){
				joinTime = imSessionUser.getJoinTime();
			}
			users.put(imSessionUser.getUserAccount(), imSessionUser);
		}
		map.put("sessionUsers", users);
		//消息记录
		QueryFilter filter = QueryFilter.build();
		filter.addFilter("ims.code_", sessionCode, QueryOP.EQUAL);
		filter.addFilter("imh.send_time_", joinTime, QueryOP.GREAT_EQUAL);
		List<ImMessageHistory> messageList = this.imMessageHistoryManager.getMsgHistory(filter);
		Collections.reverse(messageList);
		map.put("messages", messageList);
		map.put("session", imMessageSession);
		map.put("initSessionTime",DateUtil.getCurrentTime());
		map.put("success",true);
		return map;
	}
	
	@Override
	public void updateMessageArrived(Map<String, Object> param) {
		imMessageSessionDao.updateMessageArrived(param);
	}
	@Override
	public void updateEarliestAsOwner(String sessionCode) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("sessionCode", sessionCode);
		imMessageSessionDao.updateEarliestAsOwner(params);
	}
	@Override
	public void updateSessionOwner(String sessionCode, String userAccount) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("sessionCode", sessionCode);
		params.put("userAccount", userAccount);
		imMessageSessionDao.updateSessionOwner(params);
	}
	
	@Override
	public CommonResult<String> sendSystemMessage(Map<String,Object> params, String account) {
		CommonResult<String> commonResult = null;
		try {
			String uuId = ImMqttUtil.generateUUID();
			Long sendTime = System.currentTimeMillis();
			String sessionCode = getSystemMessageSessionCode(account);
			params.put("sessionCode", sessionCode);
			params.put("type", ImConstant.MESSAGE_TYPE_SYSTEM);
			params.put("from",ImConstant.SYSTEM_SESSION_ACCOUNT);
			params.put("messageId", uuId);
			params.put("sendTime", sendTime);
			String sendText = params.toString();
			mqttSendService.send(sendText, account);
			
			ImMessageHistory immsg = new ImMessageHistory();
			immsg.setSendTime(System.currentTimeMillis());
        	immsg.setId(UniqueIdUtil.getSuid());
        	immsg.setMessageId(uuId);
        	immsg.setSessionCode(sessionCode);
        	immsg.setFrom(ImConstant.SYSTEM_SESSION_ACCOUNT);
        	immsg.setType(ImConstant.MESSAGE_TYPE_SYSTEM);
        	immsg.setContent(params.get("content").toString());
        	imMessageHistoryManager.create(immsg);
			
			Map<String,Object> param = new HashMap<String,Object>();
        	param.put("lastText", sendText);
        	param.put("sessionCode", sessionCode);
        	param.put("lastTextTime", new Date());
        	updateMessageArrived(param);
        	commonResult = new CommonResult<>(true, "发送成功", null);
		} catch (Exception  e) {
			e.printStackTrace();
			commonResult = new CommonResult<>(false, "发送失败", null);
		}
		return commonResult;
	}
	
	private String getSystemMessageSessionCode(String account) throws Exception{
		List<IUser> userList = new ArrayList<IUser>();
		IUser user = userManager.getUserByAccount(account);
		userList.add(user);
		IUser admin = userManager.getUserByAccount(ImConstant.SYSTEM_SESSION_ACCOUNT);
		if(admin == null){
			/*admin = new User();
			admin.setId(UniqueIdUtil.getSuid());
			admin.setFullname("系统消息");
			admin.setAccount(ImConstant.SYSTEM_SESSION_ACCOUNT);
			admin.setEmail("1013951798@qq.com");
			admin.setPhoto(ImConstant.SYSTEM_SESSION_ICON);*/
		}
		userList.add(admin);
		String code = ImMqttUtil.generateP2PSessionCode(userList);
		//先判断是否已经存在会话
		List<ImMessageSession> imMessageSessions = imMessageSessionDao.getBySessionCode(code);
		ImMessageSession imMessageSession = null;
		if(imMessageSessions.size() == 0){
			imMessageSession = new ImMessageSession();
			imMessageSession.setId(UniqueIdUtil.getSuid());
			imMessageSession.setCode(code);
			imMessageSession.setTitle(user.getFullname());
			imMessageSession.setIcon(ImConstant.SYSTEM_SESSION_ICON);
			imMessageSession.setScene(ImConstant.SESSION_SCENE_P2P);
			imMessageSession.setCreateTime(new Date());
			imMessageSession.setOwner(ImConstant.SYSTEM_SESSION_ACCOUNT);
			imMessageSessionDao.create(imMessageSession);
			createSessionUser(imMessageSession.getCode(),user,admin.getFullname());
			createSessionUser(imMessageSession.getCode(),admin,user.getFullname());
		}else{
			imMessageSession = imMessageSessions.get(0);
			imSessionUserManager.updateSessionIsShow(account,imMessageSession.getCode(), ImConstant.SESSION_SHOW);
		}
		return imMessageSession.getCode();
	}
}
