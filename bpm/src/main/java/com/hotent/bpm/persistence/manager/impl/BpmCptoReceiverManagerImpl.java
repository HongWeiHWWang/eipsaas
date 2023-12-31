package com.hotent.bpm.persistence.manager.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.BpmCptoReceiverDao;
import com.hotent.bpm.persistence.manager.BpmCptoReceiverManager;
import com.hotent.bpm.persistence.model.BpmCptoReceiver;

@Service("bpmCptoReceiverManager")
public class BpmCptoReceiverManagerImpl extends BaseManagerImpl<BpmCptoReceiverDao, BpmCptoReceiver> implements BpmCptoReceiverManager{
	@Resource
	BpmCptoReceiverDao bpmCptoReceiverDao;

	@Override
	public BpmCptoReceiver getByCopyToId(String copToId,String userId) {
		return bpmCptoReceiverDao.getByCopyToId(copToId,userId);
	}
}
