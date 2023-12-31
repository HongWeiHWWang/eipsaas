package com.hotent.bpm.engine.task.skip;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.inst.ISkipCondition;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.uc.api.impl.util.ContextUtil;

/**
 * 审批时新产生的任务执行人和当前执行人相同时可以跳过。
 * @author ray
 */
@Service
public class SameUserSkipCondition implements ISkipCondition {

	@Override
	public boolean canSkip(BpmTask task) {
		ActionCmd actionCmd = ContextThreadUtil.getActionCmd();
		if (task.getNodeId().equals(actionCmd.getTransitVars(BpmConstants.SKIP_NODE))) {
			 return true;
		}
		List<BpmIdentity> identityList = task.getIdentityList();
		if(BeanUtils.isEmpty(identityList) || identityList.size()>1 ) return false;
		// 跳过相同执行人
		String userId=ContextUtil.getCurrentUserId();
		BpmIdentity identity = identityList.get(0);
		if (identity.getId().equals(userId)) {
			return true;
		}
		return false;
	}

	@Override
	public String getTitle() {
		return "相同执行人";
	}

	@Override
	public String getType() {
		return "sameUser";
	}

}
