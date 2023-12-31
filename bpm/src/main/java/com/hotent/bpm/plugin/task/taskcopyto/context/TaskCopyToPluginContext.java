package com.hotent.bpm.plugin.task.taskcopyto.context;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmTaskPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.core.util.UserAssignRuleParser;
import com.hotent.bpm.plugin.task.taskcopyto.def.TaskCopyToPluginDef;
import com.hotent.bpm.plugin.task.taskcopyto.def.model.CopyToItem;
import com.hotent.bpm.plugin.task.taskcopyto.plugin.TaskCopyToPlugin;

public class TaskCopyToPluginContext extends AbstractBpmTaskPluginContext{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5862742798848847584L;
	private TaskCopyToPluginDef taskCopyToPluginDef = null;

	public List<EventType> getEventTypes() {
		List<EventType> eventTypes = new ArrayList<EventType>();
		eventTypes.add(EventType.TASK_COMPLETE_EVENT);
		return eventTypes;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends RunTimePlugin> getPluginClass() {
		return TaskCopyToPlugin.class;
	}

	public BpmPluginDef parse(Element element) {
		taskCopyToPluginDef = new TaskCopyToPluginDef();
		try {
			for(int i=0;i<element.getChildNodes().getLength();i++){
				Object obj = element.getChildNodes().item(i);
				if(obj instanceof Element){
					Element copyToElement =(Element)obj;
					if(copyToElement.getTagName().equals("copyTo")){
						String msgTypes = copyToElement.getAttribute("msgTypes");
						List<UserAssignRule> userAssignRules = UserAssignRuleParser.parse(copyToElement);
						
						CopyToItem copyToItem = new CopyToItem();
						copyToItem.setMsgTypes(msgTypes);
						copyToItem.setUserAssignRules(userAssignRules);
						taskCopyToPluginDef.getCopyToItems().add(copyToItem);
					}
				}
			}
			setBpmPluginDef(taskCopyToPluginDef);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return taskCopyToPluginDef;
	}

	@Override
	public String getPluginXml() {
		@SuppressWarnings("unused")
		TaskCopyToPluginDef bpmPluginDef=(TaskCopyToPluginDef) this.getBpmPluginDef();
		return null;
	}

	@Override
	public String getJson() {
		return null;
	}

	@Override
	protected BpmPluginDef parseJson(String pluginJson) {
		return null;
	}

	@Override
	protected BpmPluginDef parseElement(Element element) {
		TaskCopyToPluginDef taskCopyToPluginDef = new TaskCopyToPluginDef();
		try {
			for(int i=0;i<element.getChildNodes().getLength();i++){
				Object obj = element.getChildNodes().item(i);
				if(obj instanceof Element){
					Element copyToElement =(Element)obj;
					if(copyToElement.getTagName().equals("copyTo")){
						String msgTypes = copyToElement.getAttribute("msgTypes");
						List<UserAssignRule> userAssignRules = UserAssignRuleParser.parse(copyToElement);
						
						CopyToItem copyToItem = new CopyToItem();
						copyToItem.setMsgTypes(msgTypes);
						copyToItem.setUserAssignRules(userAssignRules);
						taskCopyToPluginDef.getCopyToItems().add(copyToItem);
					}
				}
			}
			setBpmPluginDef(taskCopyToPluginDef);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return taskCopyToPluginDef;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
