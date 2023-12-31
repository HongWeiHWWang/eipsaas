package com.hotent.bpm.persistence.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.BpmCommuReceiverDao;
import com.hotent.bpm.persistence.manager.BpmCommuReceiverManager;
import com.hotent.bpm.persistence.model.BpmCommuReceiver;

@Service("bpmCommuReceiverManager")
public class BpmCommuReceiverManagerImpl extends BaseManagerImpl<BpmCommuReceiverDao, BpmCommuReceiver> implements BpmCommuReceiverManager{
	@Resource
	BpmCommuReceiverDao bpmCommuReceiverDao;	
	
	@Override
	public BpmCommuReceiver getByCommuUser(String commuId, String receiverId) {
		return bpmCommuReceiverDao.getByCommuUser(commuId,receiverId);
	}

    @Override
    public BpmCommuReceiver getByCommuId(String commuId) {
        return bpmCommuReceiverDao.getByCommuId(commuId);
    }

	@Override
	public List<BpmCommuReceiver> getByCommuStatus(String commuId, String status) {
		return bpmCommuReceiverDao.getByCommuStatus(commuId,status);
	}


	@Override
	public boolean checkHasCommued(String commuId, String receiverId) {
		Integer size = (Integer) bpmCommuReceiverDao.checkHasCommued(commuId,receiverId);
		return size>1;
	}
}
