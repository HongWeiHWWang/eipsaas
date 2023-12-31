package com.hotent.bpm.engine.task.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.service.BpmSignDataService;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.persistence.dao.BpmSignDataDao;
import com.hotent.bpm.persistence.model.BpmSignData;

/**
 * 读取会签审批过的人员。
 * @author ray
 *
 */
@Service
public class DefaultBpmSignDataService implements BpmSignDataService {
	
	@Resource
	BpmSignDataDao bpmSignDataDao;

	@Override
	public List<BpmIdentity> getHistoryAuditor(String executeId, String nodeId) {
		List<BpmIdentity> identityList=new ArrayList<BpmIdentity>();
		List<BpmSignData> list=bpmSignDataDao.getVoteByExecuteNode(executeId, nodeId, 0);
		for(BpmSignData data:list){
			if("no".equals(data.getVoteResult())) continue;
			BpmIdentity identity=  DefaultBpmIdentity.getIdentityByUserId(data.getVoteId(), data.getVoter());
			identityList.add(identity);
		}
		return identityList;
	}

}
