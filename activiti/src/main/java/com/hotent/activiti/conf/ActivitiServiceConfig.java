package com.hotent.activiti.conf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.SpringExpressionManager;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.hotent.activiti.cache.ActivitiDefCache;
import com.hotent.activiti.def.impl.WebDefTransform;
import com.hotent.activiti.ext.identity.ActUserService;
import com.hotent.activiti.ext.listener.CallSubProcessEndListener;
import com.hotent.activiti.ext.listener.CallSubProcessStartListener;
import com.hotent.activiti.ext.listener.EndEventListener;
import com.hotent.activiti.ext.listener.StartEventListener;
import com.hotent.activiti.ext.listener.SubProcessEndListener;
import com.hotent.activiti.ext.listener.SubProcessStartListener;
import com.hotent.activiti.ext.listener.TaskCompleteListener;
import com.hotent.activiti.ext.listener.TaskCreateListener;
import com.hotent.activiti.ext.listener.TaskSignCreateListener;
import com.hotent.activiti.ext.servicetask.CustomServiceTask;
import com.hotent.activiti.ext.sign.ActCustomSignComplete;
import com.hotent.activiti.ext.sign.ActSignComplete;
import com.hotent.activiti.id.ActivitiIdGenerator;
import com.hotent.bpm.api.helper.identity.BpmIdentityBuilder;
import com.hotent.bpm.api.plugin.core.cmd.ExecutionCommand;
import com.hotent.bpm.api.plugin.core.cmd.TaskCommand;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.engine.task.service.CustomSignCompleteImpl;
import com.hotent.bpm.engine.task.service.SignCompleteImpl;
import com.hotent.bpm.helper.identity.DefaultBpmIdentityBuilder;
import com.hotent.bpm.natapi.def.DefTransform;
import com.hotent.bpm.persistence.manager.DefaultBpmDefinitionAccessor;
import com.hotent.bpm.plugin.core.cmd.PluginExecutionCommand;
import com.hotent.bpm.plugin.core.cmd.PluginTaskCommand;

@Configuration
public class ActivitiServiceConfig implements ApplicationContextAware{
	@Resource
	DataSource dataSource;
	@Resource
	PlatformTransactionManager transactionManager;
	
	private ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
	
	@Bean(name="activitiIdGenerator")
	public ActivitiIdGenerator getActivitiIdGenerator(){
		return new ActivitiIdGenerator();
	}
	
	@Bean(name="activitiDefCache")
	public ActivitiDefCache getActivitiDefCache(){
		return new ActivitiDefCache();
	}
	
	@Bean(name="webDefTransform")
	public DefTransform webDefTransform() {
		return new WebDefTransform();
	}

	@Bean(name="processEngineConfiguration")
	public ProcessEngineConfiguration getStandaloneProcessEngineConfiguration(@Qualifier("activitiIdGenerator") ActivitiIdGenerator activitiIdGenerator,
																			  @Qualifier("activitiDefCache") ActivitiDefCache activitiDefCache) {
		SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
		configuration.setDataSource(dataSource);
		configuration.setTransactionManager(transactionManager);
		configuration.setHistory("none");
		configuration.setDbIdentityUsed(false);
		configuration.setDatabaseSchemaUpdate("true");
		configuration.setJobExecutorActivate(false);
		configuration.setIdGenerator(activitiIdGenerator);
		configuration.setProcessDefinitionCache(activitiDefCache);
		configuration.setLabelFontName("宋体");
		configuration.setActivityFontName("宋体");
		// 使用Spring的表达式管理器来实现juel表达式的解析
		configuration.setExpressionManager(new SpringExpressionManager(context, configuration.getBeans()));
		return configuration;
	}
	
	@Bean(name="processEngine")
	public ProcessEngine getProsessEngien(@Qualifier("processEngineConfiguration") ProcessEngineConfiguration pro) {
		return pro.buildProcessEngine();
	}

	@Bean
	public RepositoryService getRepositoryService(@Qualifier("processEngine") ProcessEngine ProcessEngine) {
		return ProcessEngine.getRepositoryService();
	}

	@Bean
	public RuntimeService getRuntimeService(@Qualifier("processEngine") ProcessEngine ProcessEngine) {
		return ProcessEngine.getRuntimeService();
	}

	@Bean
	public TaskService getTaskService(@Qualifier("processEngine") ProcessEngine ProcessEngine) {
		return ProcessEngine.getTaskService();
	}

	@Bean
	public FormService getFormService(@Qualifier("processEngine") ProcessEngine ProcessEngine) {
		return ProcessEngine.getFormService();
	}

	@Bean
	public HistoryService getHistoryService(@Qualifier("processEngine") ProcessEngine ProcessEngine) {
		return ProcessEngine.getHistoryService();
	}

	@Bean
	public ManagementService getManagementService(@Qualifier("processEngine") ProcessEngine ProcessEngine) {
		return ProcessEngine.getManagementService();
	}

	@Bean
	public IdentityService getIdentityService(@Qualifier("processEngine") ProcessEngine ProcessEngine) {
		return ProcessEngine.getIdentityService();
	}

	@Bean(name="actUserService")
	public ActUserService actUserService(){
		return new ActUserService();
	}
	
	@Bean("signComplete") 
	public ActSignComplete signComplete(){
		ActSignComplete signComplete = new ActSignComplete();
		signComplete.setBpmSignComplete(new SignCompleteImpl());
		return signComplete;
	}
	
	/**
	 *  签署并审任务
	 * @return
	 */
	@Bean("customSignComplete") 
	public ActCustomSignComplete customSignComplete(){
		ActCustomSignComplete customSignComplete = new ActCustomSignComplete();
		customSignComplete.setBpmSignComplete(new CustomSignCompleteImpl());
		return customSignComplete;
	}
	
	//插件命令对象
	@Bean(name="pluginExecutionCommand")
	public PluginExecutionCommand pluginExecutionCommand(){
		
		return  new PluginExecutionCommand();
	}
	
	@Bean(name="pluginTaskCommand")
	public PluginTaskCommand pluginTaskCommand(){
		
		return  new PluginTaskCommand();
	}
	
	@Bean(name="startEventListener")
	public StartEventListener startEventListener(
			@Qualifier("pluginExecutionCommand")  PluginExecutionCommand pluginExecutionCommand){
		StartEventListener startEventListener = new StartEventListener();
		List<ExecutionCommand> list=new ArrayList<>();
		list.add(pluginExecutionCommand);
		startEventListener.setExecutionCommands(list);
		return  startEventListener;
	}
	
	@Bean(name="taskCreateListener")
	public TaskCreateListener taskCreateListener(
			@Qualifier("pluginExecutionCommand")  PluginExecutionCommand pluginExecutionCommand,
			@Qualifier("pluginTaskCommand")  PluginTaskCommand pluginTaskCommand){
		TaskCreateListener taskCreateListener = new TaskCreateListener();
		List<ExecutionCommand> list=new ArrayList<>();
		list.add(pluginExecutionCommand);
		taskCreateListener.setExecutionCommands(list);
		List<TaskCommand> taskCommands =new ArrayList<>();
		taskCommands.add(pluginTaskCommand);
		taskCreateListener.setTaskCommands(taskCommands);
		return  taskCreateListener;
	}
	
	@Bean(name="taskCompleteListener")
	public TaskCompleteListener taskCompleteListener(
			@Qualifier("pluginExecutionCommand")  PluginExecutionCommand pluginExecutionCommand,
			@Qualifier("pluginTaskCommand")  PluginTaskCommand pluginTaskCommand){
		TaskCompleteListener taskCompleteListener = new TaskCompleteListener();
		List<ExecutionCommand> list=new ArrayList<>();
		list.add(pluginExecutionCommand);
		taskCompleteListener.setExecutionCommands(list);
		List<TaskCommand> taskCommands =new ArrayList<>();
		taskCommands.add(pluginTaskCommand);
		taskCompleteListener.setTaskCommands(taskCommands);
		return  taskCompleteListener;
	}
	
	@Bean(name="taskSignCreateListener")
	public TaskSignCreateListener taskSignCreateListener(
			@Qualifier("pluginExecutionCommand")  PluginExecutionCommand pluginExecutionCommand,
			@Qualifier("pluginTaskCommand")  PluginTaskCommand pluginTaskCommand){
		TaskSignCreateListener taskSignCreateListener = new TaskSignCreateListener();
		List<ExecutionCommand> list=new ArrayList<>();
		list.add(pluginExecutionCommand);
		taskSignCreateListener.setExecutionCommands(list);
		List<TaskCommand> taskCommands =new ArrayList<>();
		taskCommands.add(pluginTaskCommand);
		taskSignCreateListener.setTaskCommands(taskCommands);
		return  taskSignCreateListener;
	}
	
	@Bean(name="customServiceTask")
	public CustomServiceTask customServiceTask(
			@Qualifier("pluginExecutionCommand")  PluginExecutionCommand pluginExecutionCommand){
		CustomServiceTask customServiceTask = new CustomServiceTask();
		List<ExecutionCommand> list=new ArrayList<>();
		list.add(pluginExecutionCommand);
		customServiceTask.setExecutionCommands(list);
		return  customServiceTask;
	}
	
	@Bean(name="endEventListener")
	public EndEventListener endEventListener(
			@Qualifier("pluginExecutionCommand")  PluginExecutionCommand pluginExecutionCommand){
		EndEventListener endEventListener = new EndEventListener();
		List<ExecutionCommand> list=new ArrayList<>();
		list.add(pluginExecutionCommand);
		endEventListener.setExecutionCommands(list);
		return  endEventListener;
	}
	
	@Bean(name="callSubProcessEndListener")
	public CallSubProcessEndListener callSubProcessEndListener(){
		return  new CallSubProcessEndListener();
		
	}
	
	@Bean(name="callSubProcessStartListener")
	public CallSubProcessStartListener callSubProcessStartListener(){
		return  new CallSubProcessStartListener();
		
	}
	@Bean(name="subProcessStartListener")
	public SubProcessStartListener subProcessStartListener(){
		return  new SubProcessStartListener();
		
	}
	@Bean(name="subProcessEndListener")
	public SubProcessEndListener subProcessEndListener(){
		return  new SubProcessEndListener();
		
	}
	@Bean(name="bpmDefinitionAccessor")
	public BpmDefinitionAccessor bpmDefinitionAccessor(){
		return  new DefaultBpmDefinitionAccessor();
		
	}
	
	@Bean(name="bpmIdentityBuilder")
	public BpmIdentityBuilder bpmIdentityBuilder(){
		return  new DefaultBpmIdentityBuilder();
		
	}
	
}
