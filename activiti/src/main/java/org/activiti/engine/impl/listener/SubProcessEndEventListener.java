package org.activiti.engine.impl.listener;

import org.activiti.engine.impl.event.SubProcessEndEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.model.def.SubProcessStartOrEndEventModel;

/**
 * 内嵌子流程结束时事件
 */
@Service
public class SubProcessEndEventListener implements ApplicationListener<SubProcessEndEvent>, Ordered{

	@Override
	public int getOrder()
	{
		return 1;
	}

	@Override
	public void onApplicationEvent(SubProcessEndEvent ev)
	{
		SubProcessStartOrEndEventModel eventModel = (SubProcessStartOrEndEventModel) ev.getSource();
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		cmd.addTransitVars("CurrentEventType", "SubProcessStartOrEndEvent");
		cmd.addTransitVars("SubProcessStartOrEndEventModel", eventModel);
 
	}
}
