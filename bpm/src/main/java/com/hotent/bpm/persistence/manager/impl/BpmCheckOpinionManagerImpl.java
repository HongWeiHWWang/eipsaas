package com.hotent.bpm.persistence.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.bpm.api.constant.InterPoseType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.persistence.dao.BpmCheckOpinionDao;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmInterposeRecoredManager;
import com.hotent.bpm.persistence.model.BpmInterposeRecored;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.util.BpmUtil;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmCheckOpinionManager")
public class BpmCheckOpinionManagerImpl extends BaseManagerImpl<BpmCheckOpinionDao, DefaultBpmCheckOpinion> implements BpmCheckOpinionManager{
	@Resource
	BpmCheckOpinionDao bpmCheckOpinionDao;

	@Override
	public DefaultBpmCheckOpinion getByTaskId(String taskId) {
		return bpmCheckOpinionDao.getByTaskId(taskId);
	}

    @Override
    public DefaultBpmCheckOpinion getTaskKeyByTaskId(String taskId) {
        return bpmCheckOpinionDao.getTaskKeyByTaskId(taskId).get(0);
    }

    @Override
    public DefaultBpmCheckOpinion getTaskKeyByNodeId(String nodeId, String instId) {
         return bpmCheckOpinionDao.getTaskKeyByNodeId(nodeId,instId).get(0);
    }

    @Override
    @Transactional
	public void archiveHistory(String instId) {
		bpmCheckOpinionDao.archiveHistory(instId);
	}
	
	@Override
	public List<DefaultBpmCheckOpinion> getByInstId(String instId) {
		//取得顶级的流程实例ID
		String supInstId=getTopInstId(instId);
		List<String> instList=getListByInstId(supInstId);
		return bpmCheckOpinionDao.getByInstIds(instList);
	}
	
	@Override
    @Transactional
	public void create(DefaultBpmCheckOpinion checkOpinion) {
		if(StringUtil.isNotEmpty(checkOpinion.getAuditor()) && StringUtil.isEmpty(checkOpinion.getOrgId())){
			BpmUtil.setOpinionOrgInfo(checkOpinion.getAuditor(), checkOpinion);
		}
		if (StringUtil.isNotEmpty(ThreadMsgUtil.getMapMsg("leaderId")) && !ThreadMsgUtil.getMapMsg("leaderId").equals("undefined") && 
				StringUtil.isEmpty(checkOpinion.getAgentLeaderId()) && !OpinionStatus.AWAITING_CHECK.getKey().equals(checkOpinion.getStatus())) {
			checkOpinion.setAgentLeaderId(ThreadMsgUtil.getMapMsg("leaderId"));
		}
		
		super.create(checkOpinion);
	}
	
	@Override
    @Transactional
	public void update(DefaultBpmCheckOpinion checkOpinion) {
		if(StringUtil.isNotEmpty(checkOpinion.getAuditor()) && StringUtil.isEmpty(checkOpinion.getOrgId())){
			BpmUtil.setOpinionOrgInfo(checkOpinion.getAuditor(), checkOpinion);
		}
		if (StringUtil.isNotEmpty(ThreadMsgUtil.getMapMsg("leaderId")) && !ThreadMsgUtil.getMapMsg("leaderId").equals("undefined") && StringUtil.isEmpty(checkOpinion.getAgentLeaderId())) {
			checkOpinion.setAgentLeaderId(ThreadMsgUtil.getMapMsg("leaderId"));
		}
		
		super.update(checkOpinion);
	}
	
	/**
	 * 向上查询得到顶级的流程实例。
	 * @param instId
	 * @return String
	 */
	@Override
	public String getTopInstId(String instId){
		String rtn=instId;
		String supInstId=bpmCheckOpinionDao.getSupInstByInstId(instId);
		while(StringUtil.isNotZeroEmpty(supInstId)){
			rtn=supInstId;
			supInstId=bpmCheckOpinionDao.getSupInstByInstId(supInstId);
		}
		return rtn;
	}

	/**
	 * 向下查询流程实例。
	 * @param supperId
	 * @param instList 
	 * void
	 */
	private void getChildInst(String supperId,List<String> instList){
		List<String> list=bpmCheckOpinionDao.getBySupInstId(supperId);
		if(BeanUtils.isEmpty(list)) return ;
		for(String instId:list){
			instList.add(instId);
			getChildInst(instId,instList);
		}
	}
	
	
	@Override
	public List<String> getListByInstId(String supInstId) {
		List<String> instList=new ArrayList<String>();
		instList.add(supInstId);
		//递归往下查询
		getChildInst(supInstId,instList);
		return instList;
	}
	
	
	@Override
    @Transactional
	public void delByInstList(List<String> instList) {
		bpmCheckOpinionDao.delByInstList(instList);
	}
	@Override
	public List<DefaultBpmCheckOpinion> getByInstNodeId(String instId, String nodeId) {
		return bpmCheckOpinionDao.getByInstNodeId(instId,nodeId);
	}
	@Override
    @Transactional
	public void updStatusByWait(String taskId, String status) {
		bpmCheckOpinionDao.updStatusByWait(taskId,status);
	}

	@Override
	public List<DefaultBpmCheckOpinion> getByInstIdsAndWait(List<String> list) {
		return bpmCheckOpinionDao.getByInstIdsAndWait(list);
	}
	
	@Override
	public List<DefaultBpmCheckOpinion> getFormOpinionByInstId(String instId) {
		List<DefaultBpmCheckOpinion> rtnList=new ArrayList<DefaultBpmCheckOpinion>();
		List<DefaultBpmCheckOpinion> list= getByInstId(instId);
		for(DefaultBpmCheckOpinion opinion:list){
			if(StringUtil.isNotEmpty(opinion.getFormName())){
				rtnList.add(opinion);
			}
		}
		return rtnList;
	}
	@Override
	public DefaultBpmCheckOpinion getByTaskIdStatus(String taskId,String taskAction) {
		return bpmCheckOpinionDao.getByTaskIdAction(taskId,taskAction);
	}
	@Override
    @Transactional
	public CommonResult<String> updateFlowOpinions(ObjectNode obj) {
		ObjectNode opinion;
		try {
			 opinion=JsonUtil.toBean(obj.get("opinions").asText(), ObjectNode.class);
		     bpmCheckOpinionDao.updateOpinion(new DefaultBpmCheckOpinion(opinion.get("id").asText(),opinion.get("opinion").asText(),opinion.get("files").asText()));
		     BpmInterposeRecored bpmInterposeRecored = new BpmInterposeRecored(opinion.get("procInstId").asText(), obj.get("cause").asText(), InterPoseType.MODIFY_OPINION , "");
			 BpmInterposeRecoredManager manager = AppUtil.getBean(BpmInterposeRecoredManager.class);
			 manager.create(bpmInterposeRecored);
			 return new CommonResult<>(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<>(false, e.getMessage());
		}
	}
	@Override
    @Transactional
	public CommonResult<String> delFlowOpinions(String id, String opinion) {
			try {
				//BpmInterposeRecored bpmInterposeRecored = new BpmInterposeRecored(id, opinion, InterPoseType.DEL_OPINION , "");
				 super.remove(id);
				 BpmInterposeRecoredManager manager = AppUtil.getBean(BpmInterposeRecoredManager.class);
				 manager.remove(id);
				//manager.create(bpmInterposeRecored);
				return new CommonResult<>(true, "删除成功");
			} catch (Exception e) {
				e.printStackTrace();
				return new CommonResult<>(false, e.getMessage());
			}
	}

    @Override
    public DefaultBpmCheckOpinion getBpmOpinion(String instId, String nodeId, String dbType) {

        return bpmCheckOpinionDao.getBpmOpinion(instId,nodeId,dbType);
    }

    @Override
    public DefaultBpmCheckOpinion getByTeam(String instId, String noticeId,OpinionStatus opinionStatus, String commuUser) {
        return bpmCheckOpinionDao.getByTeam(instId,noticeId,opinionStatus.getKey(),commuUser);
    }

    @Override
    @Transactional
    public void checkOpinionIsRead(String id){
        bpmCheckOpinionDao.checkOpinionIsRead(id);
    }
	@Override
	public List<DefaultBpmCheckOpinion> getByRevokeParentTaskId(String parentTaskId) {
		return bpmCheckOpinionDao.getByRevokeParentTaskId(parentTaskId);
	}

    @Override
    @Transactional
    public void retrieveBpmTask(Map<String,Object> params) {
        bpmCheckOpinionDao.retrieveBpmTask(params);
    }

	@Override
    @Transactional
	public void updateQualfieds(List<String> instIds,String transfer,String transfered,String transferName,String transferedName) {
		
		List<DefaultBpmCheckOpinion> list = bpmCheckOpinionDao.getByInstIds(instIds);
		if (BeanUtils.isEmpty(list)) {
			return;
		}
		for (DefaultBpmCheckOpinion opinion : list) {
			if (StringUtil.isNotEmpty(opinion.getQualfiedNames()) && opinion.getQualfiedNames().indexOf(transferName)!=-1){
				String qualfieds = opinion.getQualfieds();
				String qualfiedsName = opinion.getQualfiedNames();
				if (StringUtil.isNotEmpty(qualfieds)) {
					qualfieds = qualfieds.replace(transfer,transfered).replace(transferName,transferedName);
				}
				qualfiedsName = qualfiedsName.replace(transferName,transferedName);
				opinion.setQualfieds(qualfieds);
				opinion.setQualfiedNames(qualfiedsName);
				bpmCheckOpinionDao.updateQualfieds(opinion);
			}
		}
	}
	@Override
	public List<DefaultBpmCheckOpinion> getByParentId(String parentId) {
		return bpmCheckOpinionDao.getByParentTaskIdAndStatus(parentId, null);
	}

	@Override
    @Transactional
	public void updateExtraProps(DefaultBpmCheckOpinion bpmCheckOpinion) {
		
	}

	@Override
	public List<DefaultBpmCheckOpinion> getByInstNodeIdAgree(String instId,
			String fromNodeId) {
		return baseMapper.getByInstNodeIdAgree(instId, fromNodeId);
	}

	@Override
	public List<DefaultBpmCheckOpinion> getByInstNodeIdStatus(String instId,
			String revokeNodeId, Object object) {
		return baseMapper.getByInstNodeIdStatus(instId, revokeNodeId, BeanUtils.isNotEmpty(object)?object.toString():null);
	}
}
