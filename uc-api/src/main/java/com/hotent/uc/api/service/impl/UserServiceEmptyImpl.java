package com.hotent.uc.api.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 类 {@code UserServiceEmptyImpl} 用户服务的实现
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
@Service
public class UserServiceEmptyImpl implements IUserService {
	private static final Log logger= LogFactory.getLog(UserServiceEmptyImpl.class);
    /**
     * 日志 {@value}
     */
	private final String WARN_MESSAGE = "[UCAPI]: There is no implements of UserServiceImpl";
	@Override
	public IUser getUserById(String userId) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public IUser getUserByAccount(String account) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public List<IUser> getUserListByGroup(String groupType, String groupId) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public List<IUser> getByEmail(String email) {
		logger.warn(WARN_MESSAGE);
		return null;
	}
	
	@Override
	public List<IUser> getUserByAccounts(String account) {
		logger.warn(WARN_MESSAGE);
		return null;
	}
}