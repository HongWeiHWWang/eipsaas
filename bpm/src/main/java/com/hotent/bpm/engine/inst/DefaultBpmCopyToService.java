package com.hotent.bpm.engine.inst;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.constant.CopyToType;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.service.BpmCopyToService;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.CopyToManager;
import com.hotent.bpm.persistence.model.BpmCptoReceiver;
import com.hotent.bpm.persistence.model.CopyTo;
import com.hotent.uc.api.model.IUser;

@Service
public class DefaultBpmCopyToService implements BpmCopyToService{

	
	@Resource
	CopyToManager copyToManager;
	
	@Resource
	BpmTaskManager bpmTaskManager;
	
	/**
	 * 抄送给指定的用户。
	 */
	public void copyTo(List<IUser> receiverUsers,BpmProcessInstance bpmProcessInstance ,String nodeId){
		//调用接口做抄送处理
		copyTo(bpmProcessInstance, nodeId, 
							 CopyToType.COPYTO,
							receiverUsers);
	}

	private void copyTo(BpmProcessInstance instance, String nodeId,
			CopyToType copyToType,List<IUser> receiverUsers) {
		String copyId=UniqueIdUtil.getSuid();
		
		CopyTo copyTo = new CopyTo();
		copyTo.setId(copyId);
		copyTo.setInstId(instance.getId());
		copyTo.setBpmnInstId(instance.getBpmnInstId());
		copyTo.setNodeId(nodeId);
	
		copyTo.setSubject(instance.getSubject());
		
		copyTo.setCreateTime(LocalDateTime.now());
		copyTo.setType(copyToType.name().toLowerCase());
		copyTo.setStartorId(instance.getCreateBy());		
		copyTo.setStartor(instance.getCreator());
		copyTo.setTypeId(instance.getTypeId());
		
		for(IUser user:receiverUsers){
			String id=UniqueIdUtil.getSuid();
			BpmCptoReceiver receiver=new BpmCptoReceiver();
			receiver.setId(id);
			receiver.setCptoId(copyId);
			receiver.setReceiverId(user.getUserId());
			receiver.setReceiver(user.getFullname());
			copyTo.addReceiver(receiver);
		}
		
		copyToManager.create(copyTo);		
	}

}
