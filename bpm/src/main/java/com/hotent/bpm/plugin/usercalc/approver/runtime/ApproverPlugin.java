package com.hotent.bpm.plugin.usercalc.approver.runtime;

import java.util.ArrayList;
import java.util.List;

import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.uc.api.model.IUser;

public class ApproverPlugin  extends AbstractUserCalcPlugin{

	@Override
	public List<BpmIdentity> queryByPluginDef(
			BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) {
		
		String processInstanceId = (String) pluginSession.getVariables().get(BpmConstants.PROCESS_INST_ID);
		List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();
		
		List<BpmTaskOpinion> taskOpinionList =  pluginSession.getBpmxEngine().getBpmOpinionService().getTaskOpinions(processInstanceId);
		
		for(BpmTaskOpinion taskOpinion : taskOpinionList){
			IUser user = pluginSession.getOrgEngine().getUserService().getUserById(taskOpinion.getAuditor());
			if(user != null){ 
				BpmIdentity bpmIdentity= getBpmIdentityConverter().convertUser(user);
				bpmIdentities.add(bpmIdentity);
			}
		}
		
		return bpmIdentities;
	}

	@Override
	public boolean supportPreView() {
		return false;
	}

}
