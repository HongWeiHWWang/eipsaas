package com.hotent.bpm.plugin.core.task.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import com.hotent.base.util.AppUtil;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.exception.NoTaskActionHandlerException;
import com.hotent.bpm.api.plugin.core.context.TaskActionHandlerContext;
import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;
import com.hotent.bpm.api.plugin.core.runtime.TaskActionHandler;
import com.hotent.bpm.api.plugin.core.task.TaskActionHandlerConfig;
import com.hotent.bpm.plugin.core.task.def.DefaultTaskActionHandlerDef;
import com.hotent.bpm.plugin.core.task.entity.TaskAction;
import com.hotent.bpm.plugin.core.task.entity.TaskActions;
/**
 * 此类在系统加载的时候读取按钮配置文件(taskActionPlugins.xml)，并加载到系统中。
 * <pre>
 * 这个XML定义了界面的操作按钮。
 * 这个类配置到了
 * x5-bpmx-plugin-core/conf/x5-bpmx-plugin-core.xml文件中。
 *
	&lt;bean id="taskActionHandlerConfig" class="com.hotent.bpm.plugin.core.task.config.DefaultTaskActionHandlerConfig"  scope="singleton" init-method="init">
		&lt;property name="actionConfigXml" value="/conf/taskActionPlugins.xml"/>
		&lt;property name="taskActionHandleContext" ref="taskActionHandlerContext">&lt;/property>
	&lt;/bean>
	&lt;bean id="taskActionHandlerContext" class="com.hotent.bpm.plugin.core.task.context.DefaultTaskActionHandlerContext">&lt;/bean>
 *  
 * 描述：任务处理插件默认的配置实现
 * 构建组：x5-bpmx-plugin-core
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-3-19-上午9:37:58
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefaultTaskActionHandlerConfig implements TaskActionHandlerConfig{
	
	private String actionConfigXml="/conf/taskActionPlugins.xml";
	
	private TaskActionHandlerContext taskActionHandleContext;	
	
	/**
	 * 获取所有按钮定义。
	 */
	private List<TaskActionHandlerDef> actionHandlerDefs=new ArrayList<TaskActionHandlerDef>();

	public void init() {
		try {
			InputStream	is=DefaultTaskActionHandlerConfig.class.getResourceAsStream(actionConfigXml);   
			
			TaskActions taskActions=(TaskActions)JAXBUtil.unmarshall(is, TaskActions.class);		
			
			for(TaskAction taskAction:taskActions.getTaskAction()){				
				//将jaxb对象复制到任务操作插件定义中
				DefaultTaskActionHandlerDef def = actionToActionHandlerDef(taskAction);
				
				actionHandlerDefs.add(def);
				//任务名称
				String taskName = def.getName();
				//从xml中获得类
				String handlerClass=def.getHandlerClass();
				//设置插件定义到上下文
				taskActionHandleContext.getTaskActionHandlerDefs().put(taskName, def);
				//如果为空则跳过。
				if(StringUtil.isEmpty(handlerClass)) continue;				
				//设置句柄到插件上下文
				taskActionHandleContext.getTaskActionHandlers().put(taskName, handlerClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private DefaultTaskActionHandlerDef actionToActionHandlerDef(TaskAction taskAction){
		
		DefaultTaskActionHandlerDef def = new DefaultTaskActionHandlerDef();
		def.setName(taskAction.getName());
		def.setDescription(taskAction.getDescription());
		def.setSupportType(taskAction.getSupportType().value());
		def.setHandlerClass(taskAction.getHandlerClass());
		def.setActionType(ActionType.fromKey(taskAction.getActionType().value()));
		def.setSupportScript(taskAction.isScript());
		def.setDefaultInit(taskAction.isInit());
		return def;
	}
	
	@Override
	public TaskActionHandler getTaskActionHandler(String actionName) {
		boolean rtn= taskActionHandleContext.getTaskActionHandlers().containsKey(actionName);
		if(!rtn){
			throw new NoTaskActionHandlerException("No TaskActionHandler found by "+ actionName);
		}
		String name=taskActionHandleContext.getTaskActionHandlers().get(actionName);
		return (TaskActionHandler)AppUtil.getBean(name);
	}

	@Override
	public TaskActionHandlerDef getTaskActionHandlerDef(String actionName) {
		boolean rtn= taskActionHandleContext.getTaskActionHandlerDefs().containsKey(actionName);
		if(!rtn){
			return null; //自定义按钮
			//throw new NoTaskActionHandlerException("No TaskActionHandlerDef found by "+ actionName);
		}
		return taskActionHandleContext.getTaskActionHandlerDefs().get(actionName);
	}

	public void setActionConfigXml(String actionConfigXml) {
		this.actionConfigXml = actionConfigXml;
	}

	public void setTaskActionHandleContext(
			TaskActionHandlerContext taskActionHandleContext) {
		this.taskActionHandleContext = taskActionHandleContext;
	}

	@Override
	public Collection<TaskActionHandlerDef> getActionHandlerDefList() {
		
		return taskActionHandleContext.getTaskActionHandlerDefs().values();
	}

	@Override
	public List<TaskActionHandlerDef> getAllActionHandlerDefList() {
		return actionHandlerDefs;
	}

	@Override
	public List<? extends TaskActionHandlerDef> getActionHandlerDefList(boolean isInit) {
		List<TaskActionHandlerDef> list=new ArrayList<TaskActionHandlerDef>();
		for(TaskActionHandlerDef def:this.actionHandlerDefs){
			DefaultTaskActionHandlerDef actionDef=(DefaultTaskActionHandlerDef)def;
			if(isInit==actionDef.isDefaultInit()){
				list.add(def);
			}
		} 
		return list;
	}
	
}
