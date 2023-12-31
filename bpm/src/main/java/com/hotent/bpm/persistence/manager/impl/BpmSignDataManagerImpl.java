package com.hotent.bpm.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.VoteResult;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.persistence.dao.BpmSignDataDao;
import com.hotent.bpm.persistence.manager.ActExecutionManager;
import com.hotent.bpm.persistence.manager.BpmSignDataManager;
import com.hotent.bpm.persistence.model.ActExecution;
import com.hotent.bpm.persistence.model.BpmSignData;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;

@Service("bpmSignDataManager")
public class BpmSignDataManagerImpl extends BaseManagerImpl<BpmSignDataDao, BpmSignData> implements BpmSignDataManager{

	@Resource
	ActExecutionManager actExecutionManager;
	
	@Override
	public List<BpmSignData> getVoteByExecuteNode(String executeId, String nodeId,Integer isActive) {
		return baseMapper.getVoteByExecuteNode(executeId,nodeId,isActive);
	}
	
	/**
	 * 获取会签对象。
	 * @param bpmTask
	 * @param taskExecutor
	 * @param batch
	 * @return   BpmSignData
	 */
	public BpmSignData getSignData(BpmTask bpmTask,String executeId, BpmIdentity bpmIdentity) {
		
		
		BpmSignData signData=new BpmSignData();
		signData.setId(UniqueIdUtil.getSuid());
		signData.setDefId(bpmTask.getProcDefId());
		signData.setInstId(bpmTask.getProcInstId());
		//获取线程ID
		signData.setExecuteId(executeId);
		signData.setActInstId(bpmTask.getBpmnInstId());
		signData.setNodeId(bpmTask.getNodeId());
		signData.setTaskId(bpmTask.getTaskId());
		signData.setQualifiedId(bpmIdentity.getId());
		signData.setQualifiedName(bpmIdentity.getName());
		signData.setCreateTime(LocalDateTime.now());
		signData.setVoteResult(VoteResult.NO.getKey());
		signData.setIsActive(1);
		return signData;
	}
	
	@Override
    @Transactional
	public void addSignData(String defId, String instId, String actInstId,String executeId,
			String nodeId, String taskId, String qualifiedId,
			String qualifiedName,Short index,String signType) {
		BpmSignData signData=new BpmSignData();
		signData.setId(UniqueIdUtil.getSuid());
		signData.setDefId(defId);
		signData.setInstId(instId);
		signData.setExecuteId(executeId);
		signData.setActInstId(actInstId);
		signData.setNodeId(nodeId);
		signData.setTaskId(taskId);
		signData.setQualifiedId(qualifiedId);
		signData.setQualifiedName(qualifiedName);
		signData.setCreateTime(LocalDateTime.now());
		signData.setVoteResult(VoteResult.NO.getKey());
		signData.setIndex(index);
		signData.setIsActive(1);
		signData.setType(signType);
		super.create(signData);
	}
	

	@Override
    @Transactional
	public void delByInstList(List<String> instList) {
		baseMapper.delByInstList(instList);
	}

	@Override
    @Transactional
	public void vote(String executeId, String nodeId, Short index,
			String actionName) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("executeId", executeId);
		params.put("nodeId", nodeId);
		params.put("index", index);
		BpmSignData signData= baseMapper.getByExeNodeIndex(executeId,nodeId,index);
		//处理特殊情况未找到对应会签数据时，找父executeId（如条件同步网关内的会签节点）
		if(BeanUtils.isEmpty(signData)){
			ActExecution execution = actExecutionManager.get(executeId);
			if(BeanUtils.isNotEmpty(execution)){
				signData = baseMapper.getByExeNodeIndex(execution.getParentId(),nodeId,index);
			}
		}
		IUser user=ContextUtil.getCurrentUser();
		String userId=BpmConstants.SYSTEM_USER_ID;
		String userName=BpmConstants.SYSTEM_USER_NAME;
		if(user!=null){
			userId=user.getUserId();
			userName=user.getFullname();
		}
		signData.setVoteResult(actionName);
		signData.setVoteId(userId);
		signData.setVoter(userName);
		signData.setVoteTime(LocalDateTime.now());
		//更新会签数据。
		super.update(signData);
		
	}

	@Override
    @Transactional
	public void updByNotActive(String actExecuteId, String nodeId) {
		baseMapper.updByNotActive(actExecuteId,nodeId);
		
	}

	@Override
    @Transactional
	public void removeByNotActive(String actExecuteId, String nodeId) {
		baseMapper.removeByNotActive(actExecuteId,nodeId);
	}

	@Override
	public BpmSignData getByInstanIdAndUserId(String instancId, String userId,String taskId) {
		return baseMapper.getByInstanIdAndUserId(instancId, userId, taskId);
	}

	@Override
    @Transactional
	public List<BpmIdentity> getByInstanIdAndNodeIdAndNo(String instId, String nodeId) {
		
		List<BpmSignData> bpmSignDatas = baseMapper.getByInstanIdAndNodeIdAndNo(instId,nodeId);
		List<BpmIdentity> result = new ArrayList<BpmIdentity>();
		for (BpmSignData bpmSignData : bpmSignDatas) {
			BpmIdentity bpmIdentity = new DefaultBpmIdentity();
			bpmIdentity.setType(IdentityType.USER);
			bpmIdentity.setId(bpmSignData.getQualifiedId());
			bpmIdentity.setName(bpmSignData.getQualifiedName());
			result.add(bpmIdentity);
		}
		
		baseMapper.deleteByInstanIdAndNodeIdAndNo(instId,nodeId);		
		// 将已经投票的数据更新为活动状态
		baseMapper.updByActive(instId,nodeId);
		return result;
	}
	
}
