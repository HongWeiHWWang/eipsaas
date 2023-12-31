package com.hotent.uc.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hotent.uc.api.model.GroupType;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.service.IUserGroupService;


/**
 * 类 {@code DefaultUserGroupEmptyService} 用户与组关系的实现：通过用户找组，通过组找人等
 * @author Administrator
 */
@Service
public class DefaultUserGroupEmptyService implements IUserGroupService {

	private static final Log logger= LogFactory.getLog(DefaultUserGroupEmptyService.class);
    /**
     * 日志 {@value}
     */
	private final String WARN_MESSAGE = "[UCAPI]: There is no implements of DefaultUserGroupService";



	@Override
	public List<IGroup> getGroupsByUserIdOrAccount(String userId) {
		logger.warn(WARN_MESSAGE);
		return new ArrayList<>();
	}



	@Override
	public List<GroupType> getGroupTypes() {
		logger.warn(WARN_MESSAGE);
		return new ArrayList<>();
	}

	@Override
	public List<IGroup> getGroupsByUserIdOrAccount(String groupType, String userId) {
		logger.warn(WARN_MESSAGE);
		return new ArrayList<>();
	}

	@Override
	public IGroup getGroupByIdOrCode(String groupType, String code) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public Map<String, List<IGroup>> getGroupsMapUserIdOrAccount(String id) {
		logger.warn(WARN_MESSAGE);
		return null;
	}
	

}
