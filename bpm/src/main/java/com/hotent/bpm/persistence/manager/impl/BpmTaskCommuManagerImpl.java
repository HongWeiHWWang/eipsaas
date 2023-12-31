package com.hotent.bpm.persistence.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.BpmTaskCommuDao;
import com.hotent.bpm.persistence.manager.BpmTaskCommuManager;
import com.hotent.bpm.persistence.model.BpmTaskCommu;

@Service("bpmTaskCommuManager")
public class BpmTaskCommuManagerImpl extends BaseManagerImpl<BpmTaskCommuDao, BpmTaskCommu> implements BpmTaskCommuManager{

	@Override
	public BpmTaskCommu getByTaskId(String taskId) {
		BpmTaskCommu commu=baseMapper.getByTaskId(taskId);
		return commu;
	}

    @Override
    public BpmTaskCommu getByInstId(String instId) {
        return baseMapper.getByInstId(instId);
    }
}
