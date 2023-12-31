package com.hotent.runtime.manager.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.api.constant.DecideType;
import com.hotent.bpm.exception.ApproveTaskException;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.runtime.dao.BpmTaskTransDao;
import com.hotent.runtime.manager.BpmTaskTransManager;
import com.hotent.runtime.model.BpmTaskTrans;

@Service("bpmTaskTransManager")
public class BpmTaskTransManagerImpl extends BaseManagerImpl<BpmTaskTransDao, BpmTaskTrans> implements BpmTaskTransManager{
	
    @Resource
    DefaultTaskTransService deafultTaskTransService;
	@Override
	public BpmTaskTrans getByTaskId(String taskId) {
		
		return baseMapper.getByTaskId(taskId);
	}

    @Override
    @Transactional
    public void taskToInquReply(DefaultBpmCheckOpinion dbo)throws Exception{
        //征询回复
        if(dbo.getStatus().equals("inqu_reply")){
            deafultTaskTransService.taskToInquReply(dbo.getTaskId(), DecideType.AGREE.getKey(), "inner", dbo.getOpinion(),false,dbo.getFiles(),dbo.getZfiles());
        }else{
            throw new ApproveTaskException("当前任务已办理，不可重复办理，");
        }
    }

	@Override
	@Transactional
	public void removeByInstId(String instanceId) {
		baseMapper.removeByInstId(instanceId);
	}
}
