package com.hotent.bpm.persistence.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.BpmTaskReadDao;
import com.hotent.bpm.persistence.manager.BpmTaskReadManager;
import com.hotent.bpm.persistence.model.BpmTaskRead;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmTaskReadManager")
public class BpmTaskReadManagerImpl extends BaseManagerImpl<BpmTaskReadDao, BpmTaskRead> implements BpmTaskReadManager{

	@Override
    @Transactional
	public void delByInstList(List<String> instList) {
		baseMapper.delByInstList(instList);
	}
}
