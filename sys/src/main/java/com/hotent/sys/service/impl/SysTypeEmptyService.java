package com.hotent.sys.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hotent.base.query.QueryFilter;
import com.hotent.sys.persistence.model.ISysType;
import com.hotent.sys.service.ISysTypeService;



@Service
public class SysTypeEmptyService  implements ISysTypeService{

	private static final Log logger= LogFactory.getLog(SysTypeEmptyService.class);
	private final String WARN_MESSAGE = "[UCAPI]: There is no implements of SysTypeService";
	@Override
	public ISysType getById(String typeId) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public List<ISysType> getByParentId(Long parentId) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public ISysType getInitSysType(int isRoot, String parentId) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public boolean isKeyExist(String id, String typeGroupKey, String typeKey) {
		logger.warn(WARN_MESSAGE);
		return false;
	}

	@Override
	public List<ISysType> getByGroupKey(String groupKey, String currUserId) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public void delByIds(String id) {
		logger.warn(WARN_MESSAGE);
		
	}

	@Override
	public List<ISysType> getPrivByPartId(String parentId, String userId) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public void updSn(String typeId, int sn) {
		logger.warn(WARN_MESSAGE);
	}

	@Override
	public List<ISysType> getRootTypeByCategoryKey(String string) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public List<ISysType> getChildByTypeKey(String typeKey) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public ISysType getByKey(String typeKey) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public String getXmlByKey(String typeKey, String currUserId) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public ISysType getByTypeKeyAndGroupKey(String groupKey, String typeKey) {
		logger.warn(WARN_MESSAGE);
		return null;
	}

	@Override
	public List<ISysType> query(QueryFilter queryFilter2) {
		logger.warn(WARN_MESSAGE);
		return null;
	}
	
}
