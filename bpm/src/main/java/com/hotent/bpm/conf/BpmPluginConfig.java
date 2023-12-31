package com.hotent.bpm.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotent.bpm.api.engine.BpmxEngine;
import com.hotent.bpm.api.engine.BpmxEngineFactory;
import com.hotent.bpm.api.inst.ISkipCondition;
import com.hotent.bpm.api.plugin.core.cmd.ExecutionCommand;
import com.hotent.bpm.api.plugin.core.cmd.TaskCommand;
import com.hotent.bpm.api.plugin.core.context.PluginContext;
import com.hotent.bpm.api.plugin.core.context.TaskActionHandlerContext;
import com.hotent.bpm.api.plugin.core.execution.sign.SignActionHandler;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginFactory;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.plugin.core.task.TaskActionHandlerConfig;
import com.hotent.bpm.engine.DefaultBpmxEngine;
import com.hotent.bpm.engine.def.DefXmlTransForm;
import com.hotent.bpm.engine.def.DefXmlUpdate;
import com.hotent.bpm.engine.def.impl.handler.PluginContextContainer;
import com.hotent.bpm.engine.def.impl.update.BoDefXmlUpdate;
import com.hotent.bpm.engine.def.impl.update.GlobalFormDefXmlUpdate;
import com.hotent.bpm.engine.def.impl.update.InstFormDefXmlUpdate;
import com.hotent.bpm.engine.def.impl.update.MobileInstXmlUpdate;
import com.hotent.bpm.engine.def.impl.update.NodeDefXmlUpdate;
import com.hotent.bpm.engine.def.impl.update.PluginsDefXmlUpdate;
import com.hotent.bpm.engine.def.impl.update.PropertiesDefXmlUpdate;
import com.hotent.bpm.engine.def.impl.update.VarDefXmlUpdate;
import com.hotent.bpm.engine.execution.sign.handler.ApproveSignActionHandler;
import com.hotent.bpm.engine.execution.sign.handler.BackSignActionHandler;
import com.hotent.bpm.engine.execution.sign.handler.SignActionHandlerContainer;
import com.hotent.bpm.engine.graph.DefaultFlowStatusService;
import com.hotent.bpm.engine.task.handler.TaskActionApproveHandler;
import com.hotent.bpm.engine.task.skip.AllSkipCondition;
import com.hotent.bpm.engine.task.skip.ApproverSkipCondition;
import com.hotent.bpm.engine.task.skip.EmptyUserSkipCondition;
import com.hotent.bpm.engine.task.skip.SameUserSkipCondition;
import com.hotent.bpm.factory.DefaultBpmxEngineFactory;
import com.hotent.bpm.helper.identity.DefaultBpmIdentityConverter;
import com.hotent.bpm.persistence.manager.impl.BpmProStatusManagerImpl;
import com.hotent.bpm.plugin.core.cmd.PluginExecutionCommand;
import com.hotent.bpm.plugin.core.cmd.PluginTaskCommand;
import com.hotent.bpm.plugin.core.factory.DefaultBpmPluginFactory;
import com.hotent.bpm.plugin.core.factory.DefaultBpmPluginSessionFactory;
import com.hotent.bpm.plugin.core.task.config.DefaultTaskActionHandlerConfig;
import com.hotent.bpm.plugin.core.task.context.DefaultTaskActionHandlerContext;
import com.hotent.bpm.plugin.execution.globalRestful.context.GlobalRestFulsPluginContext;
import com.hotent.bpm.plugin.execution.globalRestful.def.GlobalRestfulInvokePluginDef;
import com.hotent.bpm.plugin.execution.globalRestful.plugin.GlobalRestfulInvokePlugin;
import com.hotent.bpm.plugin.execution.message.context.MessagePluginContext;
import com.hotent.bpm.plugin.execution.message.def.MessagePluginDef;
import com.hotent.bpm.plugin.execution.message.plugin.MessagePlugin;
import com.hotent.bpm.plugin.execution.procnotify.context.ProcNotifyPluginContext;
import com.hotent.bpm.plugin.execution.procnotify.def.ProcNotifyPluginDef;
import com.hotent.bpm.plugin.execution.procnotify.plugin.ProcNotifyPlugin;
import com.hotent.bpm.plugin.execution.script.context.ScriptNodePluginContext;
import com.hotent.bpm.plugin.execution.script.def.ScriptNodePluginDef;
import com.hotent.bpm.plugin.execution.script.plugin.ScriptNodePlugin;
import com.hotent.bpm.plugin.execution.webservice.context.WebServicePluginContext;
import com.hotent.bpm.plugin.execution.webservice.def.WebServiceNodePluginDef;
import com.hotent.bpm.plugin.execution.webservice.plugin.WebServiceTaskPlugin;
import com.hotent.bpm.plugin.task.reminders.context.RemindersPluginContext;
import com.hotent.bpm.plugin.task.reminders.plugin.RemindersPlugin;
import com.hotent.bpm.plugin.task.restful.context.RestFulsPluginContext;
import com.hotent.bpm.plugin.task.restful.plugin.RestfulInvokePlugin;
import com.hotent.bpm.plugin.task.tasknotify.context.TaskNotifyPluginContext;
import com.hotent.bpm.plugin.task.tasknotify.def.TaskNotifyPluginDef;
import com.hotent.bpm.plugin.task.tasknotify.plugin.TaskNotifyPlugin;
import com.hotent.bpm.plugin.task.test.context.TestPluginContext;
import com.hotent.bpm.plugin.task.test.plugin.TestPlugin;
import com.hotent.bpm.plugin.task.userassign.context.UserAssignPluginContext;
import com.hotent.bpm.plugin.task.userassign.context.UserCopyToPluginContext;
import com.hotent.bpm.plugin.task.userassign.def.UserAssignPluginDef;
import com.hotent.bpm.plugin.task.userassign.plugin.UserAssignPlugin;
import com.hotent.bpm.plugin.task.userassign.plugin.UserQueryPlugin;
import com.hotent.bpm.plugin.usercalc.approver.context.ApproverPluginContext;
import com.hotent.bpm.plugin.usercalc.approver.def.ApproverPluginDef;
import com.hotent.bpm.plugin.usercalc.approver.runtime.ApproverPlugin;
import com.hotent.bpm.plugin.usercalc.cusers.context.CusersPluginContext;
import com.hotent.bpm.plugin.usercalc.cusers.def.CusersPluginDef;
import com.hotent.bpm.plugin.usercalc.cusers.runtime.CusersPlugin;
import com.hotent.bpm.plugin.usercalc.customQuery.content.CustomQueryPluginContext;
import com.hotent.bpm.plugin.usercalc.customQuery.def.CustomQueryPluginDef;
import com.hotent.bpm.plugin.usercalc.customQuery.runtime.CustomQueryPlugin;
import com.hotent.bpm.plugin.usercalc.depHead.context.DepHeadPluginContext;
import com.hotent.bpm.plugin.usercalc.depHead.def.DepHeadPluginDef;
import com.hotent.bpm.plugin.usercalc.depHead.runtime.DepHeadPlugin;
import com.hotent.bpm.plugin.usercalc.hrScript.context.HrScriptPluginContext;
import com.hotent.bpm.plugin.usercalc.hrScript.def.HrScriptPluginDef;
import com.hotent.bpm.plugin.usercalc.hrScript.runtime.HrScriptPlugin;
import com.hotent.bpm.plugin.usercalc.job.context.JobPluginContext;
import com.hotent.bpm.plugin.usercalc.job.def.JobPluginDef;
import com.hotent.bpm.plugin.usercalc.job.runtime.JobPlugin;
import com.hotent.bpm.plugin.usercalc.org.context.OrgPluginContext;
import com.hotent.bpm.plugin.usercalc.org.def.OrgPluginDef;
import com.hotent.bpm.plugin.usercalc.org.runtime.OrgPlugin;
import com.hotent.bpm.plugin.usercalc.position.context.PositionPluginContext;
import com.hotent.bpm.plugin.usercalc.position.def.PositionPluginDef;
import com.hotent.bpm.plugin.usercalc.position.runtime.PositionPlugin;
import com.hotent.bpm.plugin.usercalc.samenode.context.SameNodePluginContext;
import com.hotent.bpm.plugin.usercalc.samenode.def.SameNodePluginDef;
import com.hotent.bpm.plugin.usercalc.samenode.runtime.SameNodePlugin;
import com.hotent.bpm.plugin.usercalc.script.context.ScriptPluginContext;
import com.hotent.bpm.plugin.usercalc.script.def.ScriptPluginDef;
import com.hotent.bpm.plugin.usercalc.script.runtime.ScriptPlugin;


@Configuration
public class BpmPluginConfig {
	
	@Value("${system.bpm.engineName}")
	private String engineName;
	
	@Resource
	AllSkipCondition allSkipCondition;
	@Resource
	SameUserSkipCondition sameUserSkipCondition;
	@Resource
	ApproverSkipCondition approverSkipCondition;
	@Resource
	EmptyUserSkipCondition emptyUserSkipCondition;
	
	@Resource
	ObjectMapper objectMapper;
	
	
	//消息节点插件
	@Bean("messagePluginContext") 
	public MessagePluginContext messagePluginContext(){
		MessagePluginContext messagePluginContext = new MessagePluginContext();
		messagePluginContext.setBpmPluginDef(new MessagePluginDef());
		return messagePluginContext;
	}
	
	@Bean("messagePlugin") 
	public MessagePlugin messagePlugin(){
		return new MessagePlugin();
	}
	
	//脚本节点插件
	@Bean("scriptNodePluginContext") 
	public ScriptNodePluginContext scriptNodePluginContext(){
		ScriptNodePluginContext scriptNodePluginContext = new ScriptNodePluginContext();
		scriptNodePluginContext.setBpmPluginDef(new ScriptNodePluginDef());
		return scriptNodePluginContext;
	}
	@Bean("scriptNodePlugin") 
	public ScriptNodePlugin scriptNodePlugin(){
		return new ScriptNodePlugin();
	}
	@Bean("webServicePluginContext") 
	public WebServicePluginContext webServicePluginContext(){
		WebServicePluginContext webServicePluginContext = new WebServicePluginContext();
		webServicePluginContext.setBpmPluginDef(new WebServiceNodePluginDef());
		return webServicePluginContext;
	}
	@Bean("webServicePlugin") 
	public WebServiceTaskPlugin webServicePlugin(){
		return new WebServiceTaskPlugin();
	}
	
	@Bean("autoTaskPluginList") 
	public List<PluginContext> autoTaskPluginList(
			@Qualifier("scriptNodePluginContext") ScriptNodePluginContext scriptNodePluginContext,
			@Qualifier("webServicePluginContext") WebServicePluginContext webServicePluginContext,
			@Qualifier("messagePluginContext") MessagePluginContext messagePluginContext){
			List<PluginContext> pluginList =new ArrayList<>();
			pluginList.add(messagePluginContext);
			pluginList.add(webServicePluginContext);
			pluginList.add(scriptNodePluginContext);
		return pluginList;
	}
	
	@Bean("pluginContextContainer") 
	public PluginContextContainer pluginContextContainer(
			@Qualifier("autoTaskPluginList") List<PluginContext> autoTaskPluginList
			){
		PluginContextContainer contextContainer=new PluginContextContainer();
		contextContainer.setPluginList(autoTaskPluginList);
		return contextContainer;
	}
	
	//用户插件
	@Bean("cusersPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CusersPluginContext cusersPluginContext(){
		CusersPluginContext cusersPluginContext = new CusersPluginContext();
		cusersPluginContext.setBpmPluginDef(new CusersPluginDef());
		return cusersPluginContext;
	}
	@Bean("cusersPlugin") 
	public CusersPlugin cusersPlugin(){
		return  new CusersPlugin();
	}
	
	@Bean("hrScriptPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public HrScriptPluginContext hrScriptPluginContext(){
		 HrScriptPluginContext hrScriptPluginContext = new HrScriptPluginContext();
		 hrScriptPluginContext.setBpmPluginDef(new HrScriptPluginDef());
		 return hrScriptPluginContext;
	}
	@Bean("hrScriptPlugin") 
	public HrScriptPlugin hrScriptPlugin(){
		return new HrScriptPlugin();
	}
	@Bean("scriptPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ScriptPluginContext scriptPluginContext(){
		 ScriptPluginContext scriptPluginContext = new ScriptPluginContext();
		 scriptPluginContext.setBpmPluginDef(new ScriptPluginDef());
		 return scriptPluginContext;
	}
	@Bean("scriptPlugin") 
	public ScriptPlugin scriptPlugin(){
		return new ScriptPlugin();
	}
	@Bean("sameNodePluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public SameNodePluginContext sameNodePluginContext(){
		 SameNodePluginContext sameNodePluginContext = new SameNodePluginContext();
		 sameNodePluginContext.setBpmPluginDef(new SameNodePluginDef());
		 return sameNodePluginContext;
	}
	@Bean("sameNodePlugin") 
	public SameNodePlugin sameNodePlugin(){
		return new SameNodePlugin();
	}
	
	@Bean("approverPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ApproverPluginContext approverPluginContext(){
		 ApproverPluginContext approverPluginContext = new ApproverPluginContext();
		 approverPluginContext.setBpmPluginDef(new ApproverPluginDef());
		 return approverPluginContext;
	}
	
	@Bean("approverPlugin") 
	public ApproverPlugin approverPlugin(){
		return new ApproverPlugin();
	}
	
	
	// 岗位作为审批人配置器
	@Bean("positionPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public PositionPluginContext positionPluginContext(){
		PositionPluginContext positionPluginContext = new PositionPluginContext();
		positionPluginContext.setBpmPluginDef(new PositionPluginDef());
		return positionPluginContext;
	}
	
	// 岗位作为审批人执行器
	@Bean("positionPlugin") 
	public PositionPlugin positionPlugin(){
		return new PositionPlugin();
	}
	
	// 部门作为审批人配置器
	@Bean("orgPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public OrgPluginContext orgPluginContext(){
		OrgPluginContext orgPluginContext = new OrgPluginContext();
		orgPluginContext.setBpmPluginDef(new OrgPluginDef());
		return orgPluginContext;
	}
	
	// 部门作为审批人执行器
	@Bean("orgPlugin") 
	public OrgPlugin orgPlugin(){
		return new OrgPlugin();
	}
	
	// 岗位组作为审批人配置器
	@Bean("jobPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public JobPluginContext jobPluginContext(){
		JobPluginContext jobPluJobContext = new JobPluginContext();
		jobPluJobContext.setBpmPluginDef(new JobPluginDef());
		return jobPluJobContext;
	}
	
	// 岗位组作为审批人执行器
	@Bean("jobPlugin") 
	public JobPlugin JobPlugin(){
		return new JobPlugin();
	}
	
	// 部门负责人作为审批人配置器
	@Bean("depHeadPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public DepHeadPluginContext depHeadPluginContext(){
		 DepHeadPluginContext depHeadPluginContext = new DepHeadPluginContext();
		 depHeadPluginContext.setBpmPluginDef(new DepHeadPluginDef());
		 return depHeadPluginContext;
	}
	
	// 部门负责人作为审批人执行器
	@Bean("depHeadPlugin") 
	public DepHeadPlugin depHeadPlugin(){
		return new DepHeadPlugin();
	}
	
	//关联数据作为审批人配置器
	@Bean("customQueryPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CustomQueryPluginContext customQueryPluginContext(){
		CustomQueryPluginContext customQueryPluginContext = new CustomQueryPluginContext();
		customQueryPluginContext.setBpmPluginDef(new CustomQueryPluginDef());
		return customQueryPluginContext;
	}
	
	//关联数据作为审批人执行器
	@Bean("customQueryPlugin") 
	public CustomQueryPlugin customQueryPlugin(){
		return new CustomQueryPlugin();
	}
	
	@Bean("nodeUserPluginList") 
	public List<Object> nodeUserPluginList(
			@Qualifier("cusersPluginContext") CusersPluginContext cusersPluginContext,
			@Qualifier("positionPluginContext") PositionPluginContext positionPluginContext,
			@Qualifier("hrScriptPluginContext") HrScriptPluginContext hrScriptPluginContext,
			@Qualifier("scriptPluginContext") ScriptPluginContext scriptPluginContext,
			@Qualifier("orgPluginContext") OrgPluginContext orgPluginContext,
			@Qualifier("jobPluginContext") JobPluginContext jobPluginContext,
			@Qualifier("depHeadPluginContext") DepHeadPluginContext depHeadPluginContext,
			@Qualifier("customQueryPluginContext") CustomQueryPluginContext customQueryPluginContext){
		List<Object> list=new ArrayList<>();
		list.add(cusersPluginContext);
		list.add(positionPluginContext);
		list.add(orgPluginContext);
		list.add(jobPluginContext);
		list.add(depHeadPluginContext);
		list.add(hrScriptPluginContext);
		list.add(scriptPluginContext);
		list.add(customQueryPluginContext);
		return list;
	}

    @Bean("userCopyToPluginContext")
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UserCopyToPluginContext userCopyToPluginContext(){
        UserCopyToPluginContext userCopyToPluginContext = new UserCopyToPluginContext();
        userCopyToPluginContext.setBpmPluginDef(new UserAssignPluginDef());
        return userCopyToPluginContext;
    }
	
	@Bean("userAssignPluginContext") 
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public UserAssignPluginContext userAssignPluginContext(){
		UserAssignPluginContext userAssignPluginContext = new UserAssignPluginContext();
		userAssignPluginContext.setBpmPluginDef(new UserAssignPluginDef());
		return userAssignPluginContext;
	}
	
	@Bean("userAssignPlugin") 
	public UserAssignPlugin userAssignPlugin(){
		
		return new UserAssignPlugin();
	}
	
	@Bean("userQueryPlugin") 
	public UserQueryPlugin userQueryPlugin(){
		return new UserQueryPlugin();
	}
	
	//通知插件
	@Bean("taskNotifyPluginContext") 
	public TaskNotifyPluginContext taskNotifyPluginContext(){
		 TaskNotifyPluginContext taskNotifyPluginContext = new TaskNotifyPluginContext();
		 taskNotifyPluginContext.setBpmPluginDef(new TaskNotifyPluginDef());
		 return taskNotifyPluginContext;
	}
	
	@Bean("taskNotifyPlugin") 
	public TaskNotifyPlugin taskNotifyPlugin(){
		return new TaskNotifyPlugin();
	}
	
	@Bean("procNotifyPluginContext") 
	public ProcNotifyPluginContext procNotifyPluginContext(){
		 ProcNotifyPluginContext procNotifyPluginContext = new ProcNotifyPluginContext();
		 procNotifyPluginContext.setBpmPluginDef(new ProcNotifyPluginDef());
		 return procNotifyPluginContext;
	}
	
	@Bean("procNotifyPlugin") 
	public ProcNotifyPlugin procNotifyPlugin(){
		return new ProcNotifyPlugin();
	}
	
	
	//全局restful 插件
	@Bean("globalRestFulsPluginContext") 
	public GlobalRestFulsPluginContext globalRestFulsPluginContext(){
		GlobalRestFulsPluginContext globalRestFulsPluginContext = new GlobalRestFulsPluginContext();
		 globalRestFulsPluginContext.setBpmPluginDef(new GlobalRestfulInvokePluginDef());
		 return globalRestFulsPluginContext;
	}
	
	@Bean("globalRestfulPlugin") 
	public GlobalRestfulInvokePlugin globalRestfulPlugin(){
		return new GlobalRestfulInvokePlugin();
	}
	
	
	//节点restful 插件
	@Bean("restFulsPluginContext") 
	public RestFulsPluginContext restFulsPluginContext(){
		RestFulsPluginContext restFulsPluginContext = new RestFulsPluginContext();
		restFulsPluginContext.setBpmPluginDef(new GlobalRestfulInvokePluginDef());
		return restFulsPluginContext;
	}
	
	@Bean("restFulsPlugin") 
	public RestfulInvokePlugin restFulsPlugin(){
		return new RestfulInvokePlugin();
	}
	
	// 催办插件注册
	@Bean("remindersPluginContext") 
	public RemindersPluginContext remindersPluginContext(){
		return new RemindersPluginContext();
	}
	
	@Bean("remindersPlugin") 
	public RemindersPlugin remindersPlugin(){
		return new RemindersPlugin();
	}
	
	
	@Bean(name="taskActionHandlerContext")
	public TaskActionHandlerContext getTaskActionHandlerContext(){
		return new DefaultTaskActionHandlerContext();
	}
	
	@Bean(initMethod="init")
	public TaskActionHandlerConfig getTaskActionHandlerConfig(@Qualifier("taskActionHandlerContext") TaskActionHandlerContext taskActionHandlerContext){
		DefaultTaskActionHandlerConfig taskActionHandlerConfig = new DefaultTaskActionHandlerConfig();
		taskActionHandlerConfig.setActionConfigXml("/conf/taskActionPlugins.xml");
		taskActionHandlerConfig.setTaskActionHandleContext(taskActionHandlerContext);
		return taskActionHandlerConfig;
	}
	
	@Bean
	public BpmPluginSessionFactory getBpmPluginSessionFactory(){
		return new DefaultBpmPluginSessionFactory();
	}
	
	@Bean
	public BpmPluginFactory getBpmPluginFactory(){
		return new DefaultBpmPluginFactory();
	}
	
	@Bean
	public ExecutionCommand getExecutionCommand(){
		return new PluginExecutionCommand();
	}
	
	@Bean
	public TaskCommand getTaskCommand(){
		return new PluginTaskCommand();
	}
	
	//流程定义更新 
	@Bean
	public DefXmlTransForm getDefXmlTransForm(){
		DefXmlTransForm defXmlTransForm = new DefXmlTransForm();
	    List<DefXmlUpdate> xmlUpdateList=new ArrayList<DefXmlUpdate>();
	    xmlUpdateList.add(new BoDefXmlUpdate());
	    xmlUpdateList.add(new GlobalFormDefXmlUpdate());
	    xmlUpdateList.add(new InstFormDefXmlUpdate());
	    xmlUpdateList.add(new NodeDefXmlUpdate());
	    xmlUpdateList.add(new PluginsDefXmlUpdate());
	    xmlUpdateList.add(new PropertiesDefXmlUpdate());
	    xmlUpdateList.add(new VarDefXmlUpdate());
	    xmlUpdateList.add(new MobileInstXmlUpdate());
	    defXmlTransForm.setXmlUpdateList(xmlUpdateList);
		return defXmlTransForm;
	}
	
	@Bean
	public BpmxEngineFactory getDefaultBpmxEngineFactory(){
		DefaultBpmxEngineFactory bpmxEngineFactory=new DefaultBpmxEngineFactory();
		List<BpmxEngine> bpmxEngines =new ArrayList<>();
		bpmxEngines.add(new DefaultBpmxEngine());
		bpmxEngineFactory.setDefaultEngineName("engineName");
		return bpmxEngineFactory;
	}
	
	@Bean("skipRules")
    public List<ISkipCondition> skipRules(){
        List<ISkipCondition> list = new ArrayList<>();
        list.add(allSkipCondition);
        list.add(sameUserSkipCondition);
        list.add(approverSkipCondition);
        list.add(emptyUserSkipCondition);
        return list;
    }
	@Bean(name="buttonsMap")
	public Map<String, String> getButtonsMapFactory(){
		Map<String, String> buttonsMap = new HashMap<String, String>();
		buttonsMap.put("NORMAL", "inqu,agree,abandon,oppose,reject,backToStart,rejectToAnyNode,flowImage,approvalHistory,print,saveDraft,endProcess,lockUnlock,delegate,startCommu,startTrans,addSign,instanceTrans,taskDelay");
		buttonsMap.put("AGENT", "inqu,agree,abandon,oppose,reject,backToStart,rejectToAnyNode,flowImage,approvalHistory,print,saveDraft,endProcess,lockUnlock,delegate,startCommu,startTrans,addSign");
		buttonsMap.put("DELIVERTO", "inqu,agree,abandon,oppose,reject,backToStart,rejectToAnyNode,flowImage,approvalHistory,print,saveDraft,endProcess,lockUnlock,delegate,startCommu,startTrans,addSign");
		buttonsMap.put("TRANSFORMED", "agreeTrans,opposeTrans,flowImage,approvalHistory,print");
		buttonsMap.put("TRANSFORMING", "flowImage,approvalHistory,print");
		buttonsMap.put("COMMU", "commu,flowImage,approvalHistory,print");
		buttonsMap.put("APPROVELINEED", "agree,reject,flowImage,approvalHistory,print");
		buttonsMap.put("SIGNSEQUENCEED", "agree,signSequence,flowImage,approvalHistory,print");
		buttonsMap.put("SIGNLINEED", "agree,signLine,flowImage,approvalHistory,print");
		buttonsMap.put("BACK", "inqu,agree,abandon,oppose,reject,backToStart,rejectToAnyNode,flowImage,approvalHistory,print,saveDraft,endProcess,lockUnlock,delegate,startCommu,startTrans,addSign,instanceTrans,taskDelay");
		buttonsMap.put("ADDSIGN", "inqu,agree,abandon,oppose,reject,backToStart,rejectToAnyNode,flowImage,approvalHistory,print,saveDraft,endProcess,lockUnlock,delegate,startCommu,startTrans,addSign,instanceTrans,taskDelay");
		return buttonsMap;
	}
	
	
	
	@Bean("statusColorMap") 
	public Map<String, String> statusColorMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("agree","#00FF00");
		map.put("complete","#4A4A4A");
		map.put("pending","#FF0000");
		map.put("oppose","#0000FF");
		map.put("back","#8A0902");
		map.put("backToStart","#FFA500");
		map.put("recover","#023B62");
		map.put("recoverToStart","#F23B62");
		map.put("sign_pass","#338848");
		map.put("sign_no_pass","#82B7D7");
		map.put("manual_end","#EEAF97");
		map.put("submit","#F89800");
		map.put("resubmit","#FFE76E");
		map.put("suspend","#C33A1F");
		map.put("signBackCancel","#CCCCCC" );
		return map;
	}
	
	@Bean("flowStatusService") 
	public DefaultFlowStatusService flowStatusService(@Qualifier("statusColorMap") Map<String, String> statusColorMap){
		DefaultFlowStatusService flowStatusService = new DefaultFlowStatusService();
		flowStatusService.setStatusColor(statusColorMap);
		return flowStatusService;
	}
	
	@Bean("bpmProStatusManager") 
	public BpmProStatusManagerImpl bpmProStatusManager(@Qualifier("statusColorMap") Map<String, String> statusColorMap){
		BpmProStatusManagerImpl bpmProStatusManager = new BpmProStatusManagerImpl();
		bpmProStatusManager.setStatusColor(statusColorMap);
		return bpmProStatusManager;
	}
	
	@Bean("taskActionAgreeHandler") 
	public TaskActionApproveHandler taskActionAgreeHandler(@Qualifier("statusColorMap") Map<String, String> statusColorMap){
		TaskActionApproveHandler taskActionAgreeHandler = new TaskActionApproveHandler();
		return taskActionAgreeHandler;
	}
	
	@Bean("approveSignActionHandler") 
	public ApproveSignActionHandler approveSignActionHandler(){
		ApproveSignActionHandler approveSignActionHandler = new ApproveSignActionHandler();
		return approveSignActionHandler;
	}
	
	@Bean("backSignActionHandler") 
	public BackSignActionHandler backSignActionHandler(){
		BackSignActionHandler backSignActionHandler = new BackSignActionHandler();
		return backSignActionHandler;
	}
	
	@Bean("signActionHandlerContainer") 
	public SignActionHandlerContainer signActionHandlerContainer(
			@Qualifier("approveSignActionHandler") ApproveSignActionHandler approveSignActionHandler,
			@Qualifier("backSignActionHandler") BackSignActionHandler backSignActionHandler
			){
		SignActionHandlerContainer actionHandlerContainer = new SignActionHandlerContainer();
		Map<String,SignActionHandler> actionHandlers = new HashMap<String, SignActionHandler>();
		actionHandlers.put("approve", approveSignActionHandler);
		actionHandlers.put("back", backSignActionHandler);
		actionHandlers.put("backToStart", backSignActionHandler);
		actionHandlers.put("backTo", backSignActionHandler);
		actionHandlerContainer.setActionHandlers(actionHandlers);
		return actionHandlerContainer;
	}
	
	
	@Bean("bpmIdentityConverter") 
	public DefaultBpmIdentityConverter bpmIdentityConverter(){
		DefaultBpmIdentityConverter bpmIdentityConverter = new DefaultBpmIdentityConverter();
		return bpmIdentityConverter;
	}
	
	// 测试插件
	@Bean("testPluginContext") 
	public TestPluginContext testPluginContext(){
		return new TestPluginContext();
	}
	
	@Bean("testPlugin") 
	public TestPlugin testPlugin(){
		return new TestPlugin();
	}
		
}
