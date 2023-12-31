package com.hotent.bpm.persistence.manager.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.persistence.dao.BpmTaskCandidateDao;
import com.hotent.bpm.persistence.manager.BpmTaskCandidateManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.DefaultBpmTaskCandidate;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmTaskCandidateManager")
public class BpmTaskCandidateManagerImpl extends BaseManagerImpl<BpmTaskCandidateDao, DefaultBpmTaskCandidate> implements BpmTaskCandidateManager{
	@Resource
	BpmTaskCandidateDao bpmTaskCandidateDao;
	@Resource
	IUserService userServiceImpl;
	@Resource
	BpmTaskManager bpmTaskManager;

	@Override
    @Transactional
	public void addCandidate(BpmTask task, List<BpmIdentity> list) {
		String taskId=task.getId();
		String instId=task.getProcInstId();
		
		for(BpmIdentity identity:list){
			String type=identity.getType();
			//用户
			if(BpmIdentity.TYPE_USER.equals(type)){
				DefaultBpmTaskCandidate candidate=new DefaultBpmTaskCandidate(taskId,BpmIdentity.TYPE_USER,identity.getId(),instId);
				create(candidate);
			}
			//用户组合。
			else if(BpmIdentity.TYPE_GROUP_USER.equals(type)){
				String[] aryId=identity.getId().split(",");
				for(String userId:aryId){
					DefaultBpmTaskCandidate candidate=new DefaultBpmTaskCandidate(taskId,BpmIdentity.TYPE_USER,userId,instId);
					create(candidate);
				}
			}
			//组织
			else if(BpmIdentity.TYPE_GROUP.equals(type)){
				//抽取用户。
				if(ExtractType.EXACT_EXACT_USER.equals(identity.getExtractType())){
					List<IUser> userList= userServiceImpl.getUserListByGroup(identity.getGroupType(), identity.getId());
					for(IUser user:userList){
						DefaultBpmTaskCandidate candidate=new DefaultBpmTaskCandidate(taskId,BpmIdentity.TYPE_USER,user.getUserId(),instId);
						create(candidate);
					}
				}//延迟抽取
				else{
					DefaultBpmTaskCandidate candidate=new DefaultBpmTaskCandidate(taskId,identity.getGroupType(),identity.getId(),instId);
					create(candidate);
				}
			}
		}
	}
	@Override
    @Transactional
	public void removeByTaskId(String taskId) {
		bpmTaskCandidateDao.removeByTaskId(taskId);
		
	}

	@Override
	public List<DefaultBpmTaskCandidate> queryByTaskId(String taskId) {
		return bpmTaskCandidateDao.queryByTaskId(taskId);
	}
	
	@Override
	public DefaultBpmTaskCandidate getByTaskIdExeIdType(String taskId,
			String executorId, String type) {
		return bpmTaskCandidateDao.getByTaskIdExeIdType(taskId,executorId,type);
	}

	@Override
    @Transactional
	public void delByInstList(List<String> instList) {
		bpmTaskCandidateDao.delByInstList(instList);
	}

	@Override
	public List<DefaultBpmTaskCandidate> getByInstList(List<String> instList) {
		return bpmTaskCandidateDao.getByInstList(instList);
	}

	@Override
    @Transactional
	public void updateExecutor(Map<String, Object> params) {
		bpmTaskCandidateDao.updateExecutor(params);
	}


	@Override
    @Transactional
	public void addCandidate(String taskId, List<BpmIdentity> list) {
		BpmTask bpmTask= bpmTaskManager.get(taskId);
		addCandidate(bpmTask, list);
	}
	
}
