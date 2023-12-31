package com.hotent.bpm.plugin.core.task.def;

import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;


public class DefaultTaskActionHandlerDef implements TaskActionHandlerDef{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1333021338574009234L;
	
	protected String description;
    protected String supportType="";
    protected String handlerClass="";
    protected String name="";
    protected ActionType actionType;
    
    protected boolean supportScript=false;
    protected boolean defaultInit=false;
    
    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSupportType() {
		return supportType;
	}
	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}
	public String getHandlerClass() {
		return handlerClass;
	}
	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ActionType getActionType() {
		return actionType;
	}
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	public boolean isSupportScript() {
		return supportScript;
	}
	public void setSupportScript(boolean supportScript) {
		this.supportScript = supportScript;
	}
	public boolean isDefaultInit() {
		return defaultInit;
	}
	public void setDefaultInit(boolean defaultInit) {
		this.defaultInit = defaultInit;
	}
    
}
