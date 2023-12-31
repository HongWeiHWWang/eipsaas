package com.hotent.bpm.persistence.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.BpmDefActDao;
import com.hotent.bpm.persistence.manager.BpmDefActManager;
import com.hotent.bpm.persistence.model.BpmDefAct;
import org.springframework.transaction.annotation.Transactional;


/**
 * 对象功能:流程定义权限明细 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 14:10:50
 */
@Service
public class BpmDefActManagerImpl extends BaseManagerImpl<BpmDefActDao, BpmDefAct> implements BpmDefActManager{

	@Override
	public List<BpmDefAct> getByAuthorizeId(String authorizeId) {
		
		return baseMapper.getByAuthorizeId(authorizeId);
	}



	@Override
    @Transactional
	public void delByAuthorizeId(String authorizeId) {
		baseMapper.delByAuthorizeId(authorizeId);
	}



	@Override
	public List<BpmDefAct> getActRightByUserMap(Map<String, String> userRightMapStr, String authorizeType,
			String defKey) {
		return baseMapper.getActRightByUserMap(userRightMapStr, authorizeType, defKey);
	}



	@Override
	public List<BpmDefAct> getAll(Map<String, Object> params) {
		return baseMapper.getAll(params);
	}
	
	
	
}
