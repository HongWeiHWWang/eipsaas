package com.hotent.im.persistence.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hotent.base.dao.MyBatisDao;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.manager.impl.AbstractManagerImpl;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.im.persistence.dao.ImSessionUserDao;
import com.hotent.im.persistence.manager.ImMessageSessionManager;
import com.hotent.im.persistence.manager.ImSessionUserManager;
import com.hotent.im.persistence.model.ImMessageSession;
import com.hotent.im.persistence.model.ImSessionUser;
import com.hotent.im.service.MqttSendService;
import com.hotent.im.util.ImConstant;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 
 * <pre> 
 * 描述：会话用户 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:19
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("imSessionUserManager")
public class ImSessionUserManagerImpl extends AbstractManagerImpl<String, ImSessionUser> implements ImSessionUserManager{
	@Resource
	ImSessionUserDao imSessionUserDao;
	@Resource
	ImMessageSessionManager imMessageSessionManager;
	@Resource
	IUserService userManager;
	@Resource
	MqttSendService mqttSendService;
	@Resource
	CommonManager commonManager;
	@Value("${spring.profiles.platform}")
	private String dbType;//数据库类型
	
	@Override
	protected MyBatisDao<String, ImSessionUser> getDao() {
		return imSessionUserDao;
	}
	
	/**
	 * 获取会话列表
	 */
	@Override
	public List<ImSessionUser> mySessionList(QueryFilter queryFilter) {
		if("oracle".equals(dbType)){
			List<ImSessionUser> imSessionUserList = imSessionUserDao.mySessionListOracle(queryFilter.getParams());
			return imSessionUserList;
		}else{
			List<ImSessionUser> imSessionUserList = imSessionUserDao.mySessionListMysql(queryFilter.getParams());
			return imSessionUserList;
		}
	}
	
	@Override
	public void updateSessionIsShow(String account,String sessionCode, short isShow) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("account", account);
		map.put("sessionCode", sessionCode);
		map.put("isShow", isShow);
		imSessionUserDao.updateSessionIsShow(map);
	}
	
	@Override
	public void quitSession(String sessionCode, String account,String owner) {
		String userAlias = getUserAlias(sessionCode,account);
		Map<String,Object> p = new HashMap<String,Object>();
		p.put("sessionCode", sessionCode);
		p.put("userAccount", account);
		imSessionUserDao.quitSession(p);
		ImSessionUser imSessionUser = getSessionEarliestUser(sessionCode);
		//退出后还要看看是不是群组，如果是，需要更新一个群组，取第二个进入假如群聊的人
		if(account.equals(owner)){
			imMessageSessionManager.updateSessionOwner(sessionCode,imSessionUser.getUserAccount());
			imMessageSessionManager.updateEarliestAsOwner(sessionCode);
			owner = imSessionUser.getUserAccount();
		}
		//发送通知消息
		try {
			Map<String,Object> params = new HashMap<String,Object>();
			String content = "\"{\"title\":\"退出群组\",\"text\":\""+userAlias+"退出群组:"+imSessionUser.getSessionTitle()+"\"}\"";
			params.put("content",content);
			//imMessageSessionManager.sendSystemMessage(params,owner);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	public ImSessionUser getSessionEarliestUser(String sessionCode){
		List<ImSessionUser> list = imSessionUserDao.getSessionEarliestUser(sessionCode);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<ImSessionUser> getUserBySessionCode(String sessionCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sessionCode", sessionCode);
		return imSessionUserDao.getUserBySessionCode(params);
	}
	@Override
	public void updateLastReadTime(String account, String sessionCode, Date date) {
		imSessionUserDao.updateLastReadTime(account,sessionCode,date);
	}
	@Override
	public boolean checkUserByCodeAndAccout(String sessionCode, String userAccount) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sessionCode", sessionCode);
		params.put("userAccount", userAccount);
		return imSessionUserDao.userIsInTeamSession(params);
	}
	@Override
	public void invitation(String sessionCode, String userAccount) throws Exception {
		ImMessageSession imMessageSession = imMessageSessionManager.getBySessionCode(sessionCode);
		if(imMessageSession == null){
			throw new Exception("找不到会话");
		}
		String[] users = userAccount.split(",");
		Date currentDate = new Date();
		for(String account : users){
			if(!checkUserByCodeAndAccout(sessionCode, account)){
				IUser user = userManager.getUserByAccount(account);
				if(user != null){
					ImSessionUser isu = new ImSessionUser();
					isu.setId(UniqueIdUtil.getSuid());
					isu.setJoinTime(currentDate);
					isu.setLastReadTime(currentDate);
					isu.setIsShow(ImConstant.SESSION_SHOW);
					isu.setFrom(ContextUtil.getCurrentUser().getAccount());
					isu.setSessionCode(sessionCode);
					isu.setUserAlias(user.getFullname());
					isu.setSessionTitle(imMessageSession.getTitle());
					isu.setUserAccount(account);
					imSessionUserDao.create(isu);
				}
			}
		}
	}
	@Override
	public void updateUserAlias(String sessionCode, String userAlias,
			String userAccount) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sessionCode", sessionCode);
		params.put("userAlias", userAlias);
		params.put("userAccount", userAccount);
		imSessionUserDao.updateUserAlias(params);
	}
	@Override
	public String getUserAlias(String sessionCode, String account) {
		QueryFilter filter = QueryFilter.build();
		filter.addFilter("session_code_", sessionCode, QueryOP.EQUAL);
		filter.addFilter("user_account_", account, QueryOP.EQUAL);
		Map<String,Object> params = filter.getParams();
		String fieldSql = "user_alias_";
		params.put("fieldSql", fieldSql);
		List<ImSessionUser> imSessionUser = imSessionUserDao.getByDynamicSql(params);
		if(imSessionUser.size() > 0){
			return imSessionUser.get(0).getUserAlias();
		}
		return "";
	}
	@Override
	public void updateSessionTitle(String sessionCode,String sessionTitle) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sessionCode", sessionCode);
		params.put("sessionTitle", sessionTitle);
		imSessionUserDao.updateSessionTitle(params);
	}
	@Override
	public List<ImSessionUser> myTeamSessionList(QueryFilter queryFilter) {
		return imSessionUserDao.myTeamSessionList(queryFilter.getParams());
	}
	@Override
	public void updateIconByCode(Map<String, Object> params) {
		imSessionUserDao.updateIconByCode(params);
	}
	@Override
	public void updateIconByUser(Map<String, Object> p) {
		imSessionUserDao.updateIconByUser(p);
	}
	@Override
	public List<ImSessionUser> getP2PSessionIcon(QueryFilter queryFilter) {
		return imSessionUserDao.getP2PSessionIcon(queryFilter.getParams());
	}

	@Override
	public List<ImSessionUser> getUserByFilter(QueryFilter filter) {
		return imSessionUserDao.getUserByFilter(filter.getParams());
	}
	/**
	 * 获取所有用户的头像
	 * @param imSessionUserList
	 * @return
	 */
	@Override
	public List<IUser> getSessionUserIcon(List<ImSessionUser> imSessionUserList) {
		if(imSessionUserList.size() > 0){
			List<String> codeList = new ArrayList<String>();
			for(ImSessionUser imSessionUser :imSessionUserList){
				codeList.add(imSessionUser.getSessionCode());
			}
			QueryFilter filter = QueryFilter.build();
			filter.addFilter("session_code_", codeList, QueryOP.IN);
			Map<String,Object> params = filter.getParams();
			String whereSql =params.getOrDefault("whereSql", "").toString();
			if(StringUtils.isNotEmpty(whereSql)){
				//获取所有会话用户的头像
				String sql = "select DISTINCT(user_account_) as account from portal_im_session_user where "+whereSql;
				List<Map<String,Object>> users = commonManager.query(sql);
				List<String> accounts = new ArrayList<String>();
				for(Map<String,Object> user:users){
					accounts.add(user.getOrDefault("account", "").toString());
				}
				List<IUser> iuser = userManager.getUserByAccounts(String.join(",", accounts));
				return iuser;
			}
		}
		return null;
	}

	
	/*@Override
	public List<ImSessionUser> getByDynamicSql(String sessionCode) {
		QueryFilter filter = new DefaultQueryFilter();
		filter.addFilter("session_code_", sessionCode, QueryOP.EQUAL);
		Map<String,Object> params = filter.getParams();
		String fieldSql = "user_alias_,user_account_,session_title_,from_";
		params.put("fieldSql", fieldSql);
		params.put("whereSql", filter.getFieldLogic().getSql());
		return imSessionUserDao.getByDynamicSql(params);
	}*/
}
