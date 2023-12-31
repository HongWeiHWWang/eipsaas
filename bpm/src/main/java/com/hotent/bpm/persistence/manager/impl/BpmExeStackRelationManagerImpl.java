package com.hotent.bpm.persistence.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.BpmExeStackRelationDao;
import com.hotent.bpm.persistence.manager.BpmExeStackRelationManager;
import com.hotent.bpm.persistence.model.BpmExeStackRelation;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmExeStackRelationManager")
public class BpmExeStackRelationManagerImpl extends BaseManagerImpl<BpmExeStackRelationDao, BpmExeStackRelation> implements BpmExeStackRelationManager{

	@Override
	public BpmExeStackRelation getByToStackId(String stackId) {
		return baseMapper.getByToStackId(stackId);
	}

	@Override
	public BpmExeStackRelation getByFromStackId(String stackId) {
		return baseMapper.getByFromStackId(stackId);
	}

	@Override
	public List<BpmExeStackRelation> getListByProcInstId(String procInstId) {
		return baseMapper.getListByProcInstId(procInstId);
	}

	@Override
    @Transactional
	public void removeHisByInstId(String procInstId) {
		baseMapper.removeHisByInstId(procInstId);
	}

    @Override
    public BpmExeStackRelation getById(String instId,String fromId, String toId){
       return baseMapper.getById(instId, fromId, toId);
    }

}
