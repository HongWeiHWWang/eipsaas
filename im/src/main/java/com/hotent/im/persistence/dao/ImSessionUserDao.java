package com.hotent.im.persistence.dao;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hotent.base.dao.MyBatisDao;
import com.hotent.im.persistence.model.ImSessionUser;

/**
 * 
 * <pre> 
 * 描述：会话用户 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:19
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface ImSessionUserDao extends MyBatisDao<String, ImSessionUser> {

	void updateSessionIsShow(Map<String, Object> params);

	void quitSession(String sessionCode, String userId);

	List<ImSessionUser> getUserBySessionCode(Map<String, Object> params);

	void updateLastReadTime(String account, String sessionCode, Date date);

	List<ImSessionUser> getByDynamicSql(Map<String, Object> params);

	List<ImSessionUser> getUserDetailByCode(Map<String, Object> params);

	boolean userIsInTeamSession(Map<String, Object> params);

	List<ImSessionUser> myTeamSessionList(Map params);

	List<ImSessionUser> getSessionEarliestUser(String sessionCode);

	List<ImSessionUser> getP2PSessionIcon(Map params);

	List<ImSessionUser> getUserByFilter(Map<String, Object> params);

	List<ImSessionUser> mySessionListOracle(Map<String, Object> params);

	List<ImSessionUser> mySessionListMysql(Map<String, Object> params);

	void updateSessionTitle(Map<String, Object> params);

	void quitSession(Map<String, Object> p);

	void updateUserAlias(Map<String, Object> params);

	void updateIconByUser(Map<String, Object> p);

	void updateIconByCode(Map<String, Object> params);

}
