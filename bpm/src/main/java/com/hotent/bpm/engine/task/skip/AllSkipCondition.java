package com.hotent.bpm.engine.task.skip;

import org.springframework.stereotype.Service;

import com.hotent.bpm.api.inst.ISkipCondition;
import com.hotent.bpm.api.model.process.task.BpmTask;

/**
 * 任何情况下都可以跳过。
 * @author ray
 *
 */
@Service
public class AllSkipCondition implements ISkipCondition {

	@Override
	public boolean canSkip(BpmTask task) {
		
		return true;
	}

	@Override
	public String getTitle() {
		return "无条件跳过";
	}

	@Override
	public String getType() {
		return "all";
	}


}
