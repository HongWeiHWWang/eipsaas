package com.hotent.im.persistence.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hotent.base.manager.Manager;
import com.hotent.base.query.QueryFilter;
import com.hotent.im.persistence.model.ImSessionUser;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre> 
 * 描述：会话用户 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:19
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface ImSessionUserManager extends Manager<String, ImSessionUser>{

	List<ImSessionUser> mySessionList(QueryFilter queryFilter);

	void updateSessionIsShow(String account, String sessionCode, short sessionShow);

	void quitSession(String sessionCode, String userId, String string);

	List<ImSessionUser> getUserBySessionCode(String sessionCode);

	void updateLastReadTime(String account, String sessionCode, Date date);

	boolean checkUserByCodeAndAccout(String sessionCode, String userAccount);

	void invitation(String sessionCode, String userAccount) throws Exception;

	void updateUserAlias(String string, String string2, String userAccount);

	String getUserAlias(String sessionCode, String account);

	void updateSessionTitle(String sessionCode,String sessionTitle);

	List<ImSessionUser> myTeamSessionList(QueryFilter queryFilter);
	
	ImSessionUser getSessionEarliestUser(String sessionCode);

	void updateIconByCode(Map<String, Object> params);

	void updateIconByUser(Map<String, Object> p);

	List<ImSessionUser> getP2PSessionIcon(QueryFilter queryFilter);

	List<ImSessionUser> getUserByFilter(QueryFilter filter);

	List<IUser> getSessionUserIcon(List<ImSessionUser> imSessionUserList);

}
