package com.hotent.bpmModel.manager.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpmModel.dao.BpmDeputyDao;
import com.hotent.bpmModel.manager.BpmDeputyManager;
import com.hotent.bpmModel.model.BpmDeputy;

@Service("bpmDeputyManager")
public class BpmDeputyManagerImpl extends BaseManagerImpl<BpmDeputyDao, BpmDeputy> implements BpmDeputyManager {
	@Override
	public List<BpmDeputy> getByAgentId(String agentId) {
		return baseMapper.getByAgentId(agentId);
	}
	@Override
	public BpmDeputy getByUserId(String userId) {
		return baseMapper.getByUserId(userId);
	}
}
