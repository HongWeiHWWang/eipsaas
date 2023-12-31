package com.hotent.bpm.engine.task.skip;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.inst.ISkipCondition;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.model.process.task.SkipResult;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;

/**
 * 只要审批过就允许跳过。
 * @author ray
 *
 */
@Service
public class ApproverSkipCondition implements ISkipCondition {
	
	@Resource
	private BpmCheckOpinionManager bpmCheckOpinionManager;
	

	@Override
	public boolean canSkip(BpmTask task) {
		String instId=task.getProcInstId();
		List<DefaultBpmCheckOpinion> list= bpmCheckOpinionManager.getByInstId(instId);
		for(DefaultBpmCheckOpinion opinion:list){
			if(opinion.getTaskKey().equals(task.getNodeId())) continue;
			if(isChecked(opinion.getAuditor(), task.getIdentityList())){
				ContextThreadUtil.putCommonVars(SkipResult.SKIP_APPROVER_AUDITOR, opinion.getAuditor());
				ContextThreadUtil.putCommonVars(SkipResult.SKIP_APPROVER_AUDITORNAME, opinion.getAuditorName());
				return true;
			}
		}
		return false;
	}

	//是否已审核过
	private boolean isChecked(String auditor,List<BpmIdentity> identitys){
		if(BeanUtils.isEmpty(identitys)) return false;
		for (BpmIdentity bpmIdentity : identitys) {
			if(bpmIdentity.getId().equals(auditor)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getTitle() {
		return "审批跳过";
	}

	@Override
	public String getType() {
		return "approver";
	}

}
