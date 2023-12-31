package com.hotent.bpm.persistence.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.def.BpmDefExtProperties;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.def.Restful;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.ProcessInstAopPluginContext;
import com.hotent.bpm.defxml.entity.ext.ExtDefinitions;
import com.hotent.bpm.defxml.entity.ext.ExtProcess;

/**
 * 流程定义扩展类
 * <pre> 
 * 描述：
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-3-下午4:58:40
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefaultBpmProcessDefExt implements BpmProcessDefExt{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6488971924132239577L;

	public DefaultBpmProcessDefExt(){
		
	}
	
	transient ExtProcess extProcess=null;
	
	
	/**
	 * 流程文件定义解析后的数据。
	 */
	private transient ExtDefinitions definitions;
	
	private List<BpmPluginContext> pluginContextList= new ArrayList<BpmPluginContext>();
	
	private List<ProcessInstAopPluginContext> processInstAopPluginContexts = null;
	
	
	private List<BpmVariableDef> varList=null; 
	
	private Map<String,List<BpmVariableDef>> nodeVariables = null;
	
	/**
	 * bo数据保存到数据库。
	 */
	private boolean boSaveToDb=true;
	
	private List<ProcBoDef>  boDefList=null;
	
	/**
	 * 流程实例表单。
	 */
	@SuppressWarnings("unused")
	private FormExt instForm=null;
	
	/**
	 * 手机实例表单
	 */
	@SuppressWarnings("unused")
	private FormExt instMobileForm=null;
	/**
	 * 获取全局表单。
	 */
	@SuppressWarnings("unused")
	private FormExt globalForm=null;
	
	/**
	 * 获取全局手机表单。
	 */
	@SuppressWarnings("unused")
	private FormExt globalMobileForm=null;
	/**
	 * 获取全局restful事件
	 */
	@SuppressWarnings("unused")
	private List<Restful> globalRestfulList = new ArrayList<Restful>();
	
	private List<Restful> globalAllRestfulList = new ArrayList<Restful>();
	
	private List<FormExt> allGlobalForm =new ArrayList<FormExt>();
	
	private List<FormExt> allInstForm =new ArrayList<FormExt>();
	
	/**
	 * 流程定义扩展属性。
	 */
	private BpmDefExtProperties extPropertys=null;
	
	public DefaultBpmProcessDefExt(ExtDefinitions definitions){
		this.definitions=definitions;
		this.extProcess= definitions.getExtProcess();
	}
	
	
	
	
	public ExtDefinitions getDefinitions() {
		return definitions;
	}

	public void setDefinitions(ExtDefinitions definitions) {
		this.definitions = definitions;
	}

	/**
	 * 获取节点扩展信息。
	 * @return 
	 * ExtProperties
	 */
	public BpmDefExtProperties getExtProperties() {
		return extPropertys;
	}
	
	public void setExtProperties(BpmDefExtProperties extPropertys){
		this.extPropertys = extPropertys;
	}

	
	/**
	 * 解析流程的全局插件。
	 * @return List&lt;BpmPluginContext>
	 */
	public List<BpmPluginContext> getBpmPluginContexts() {
		return pluginContextList;
	}
	
	public BpmPluginContext getBpmPluginContext(Class<?> clazz) {
		
		List<BpmPluginContext> Plugins = getBpmPluginContexts();
		if(BeanUtils.isEmpty(Plugins)) return null;
		
		for(BpmPluginContext pulgin : Plugins) {
			if(pulgin.getClass().isAssignableFrom(clazz))
				return pulgin;
		}
		return null;
	}
	
	
	/**
	 * 流程变量列表。
	 * @return 
	 * List&lt;VarialbeDef>
	 */
	public List<BpmVariableDef> getVariableList() {
		return varList;
	}
	
	/**
	 * 获取节点变量。
	 * @param nodeId
	 * @return List&lt;BpmVariableDef>
	 */
	public List<BpmVariableDef> getVariableList(String nodeId){
		
		if(!nodeVariables.containsKey(nodeId)) return Collections.emptyList();
		
		return nodeVariables.get(nodeId);
	}
	
	/**
	 * 获取BO定义列表。
	 * @return 
	 * List&lt;ProcBoDef>
	 */
	public List<ProcBoDef> getBoDefList(){
		if(BeanUtils.isEmpty(boDefList)) return new ArrayList<ProcBoDef>();
		return boDefList;
	}




	public List<ProcessInstAopPluginContext> getProcessInstAopPluginContexts() {
		return processInstAopPluginContexts;
	}
	
	
	/**
	 * 取得表单的流程实例表单。
	 * @return  com.hotent.runtime.api.model.process.nodedef.ext.extmodel.InstForm
	 */
	public FormExt getInstForm(){
		return this.getInstFormList(BpmConstants.LOCAL,false);
	}
	public FormExt getInstMobileForm(){
		
		return this.getInstFormList(BpmConstants.LOCAL,true);
	}
	
	/**
	 * 通过父类key获取全局表单。
	 * isMobile:true手机表单，false 电脑表单
	 * @return Form
	 */
	public FormExt getGlobalFormByDefKey(String parentFlowKey,boolean isMobile){
		return this.getGlobalFormList(parentFlowKey,  isMobile);
	}
	
	public FormExt getInstFormByDefKey(String parentFlowKey,boolean isMobile){
		return this.getInstFormList(parentFlowKey,  isMobile);
	}
	
	/**
	 * 根据父类key获取restful事件。
	 * @param parentFlowKey
	 * @return
	 */
	public List<Restful> getGlobalRestfulByDefKey(String parentFlowKey){
		return this.getGlobalRestfulList(parentFlowKey);
	}
	
	/**
	 * 获取全局表单。
	 * @return Form
	 */
	public FormExt getGlobalForm(){
		return this.getGlobalFormList(BpmConstants.LOCAL,false);
	}
	
	/**
	 * 获取全局手机表单。
	 * @return
	 */
	public FormExt getGlobalMobileForm(){
		return this.getGlobalFormList(BpmConstants.LOCAL,true);
	}
	
	/**
	 * 获取restful事件。
	 * @return Form
	 */
	public List<Restful> getGlobalRestful(){
		return this.getGlobalRestfulList(BpmConstants.LOCAL);
	}
	
	
	private FormExt  getGlobalFormList(String parentFlowKey ,boolean isMobile){
		if(StringUtil.isEmpty(parentFlowKey)) parentFlowKey = BpmConstants.LOCAL;
		
		FormType formType=isMobile?FormType.MOBILE:FormType.PC;
		
		for (FormExt form : allGlobalForm) {
			if(form.getParentFlowKey().equals(parentFlowKey)&&form.getFormType().equalsIgnoreCase(formType.toString())){
				return form;
			}
		}
		return null;
	}
	
	
	private FormExt  getInstFormList(String parentFlowKey ,boolean isMobile){
		if(StringUtil.isEmpty(parentFlowKey)) parentFlowKey = BpmConstants.LOCAL;
		
		FormType formType=isMobile?FormType.MOBILE:FormType.PC;
		
		for (FormExt form : allInstForm) {
			if(form.getParentFlowKey().equals(parentFlowKey)&&form.getFormType().equalsIgnoreCase(formType.toString())){
				return form;
			}
		}
		return null;
	}
	
	private List<Restful>  getGlobalRestfulList(String parentFlowKey){
		if(StringUtil.isEmpty(parentFlowKey)) parentFlowKey = BpmConstants.LOCAL;
		List<Restful> restfuls = new ArrayList<Restful>();
		for (Restful restful : globalAllRestfulList) {
			if(parentFlowKey.equals(restful.getParentDefKey())){
				restfuls.add(restful);
			}
		}
		return restfuls;
	}
	
	/**
	 * bo是否保存到数据库。
	 * @return boolean
	 */
	public boolean isBoSaveToDb() {
		return boSaveToDb;
	}
	/**
	 * 设置是否保存到数据库。
	 * @param boSaveToDb 
	 * void
	 */
	public void setBoSaveToDb(boolean boSaveToDb) {
		this.boSaveToDb = boSaveToDb;
	}




	public ExtProcess getExtProcess() {
		return extProcess;
	}




	public void setExtProcess(ExtProcess extProcess) {
		this.extProcess = extProcess;
	}




	public List<BpmPluginContext> getPluginContextList() {
		return pluginContextList;
	}




	public void setPluginContextList(List<BpmPluginContext> pluginContextList) {
		this.pluginContextList = pluginContextList;
	}




	public List<BpmVariableDef> getVarList() {
		return varList;
	}




	public void setVarList(List<BpmVariableDef> varList) {
		this.varList = varList;
	}




	public List<FormExt> getAllGlobalForm() {
		return allGlobalForm;
	}




	public void setAllGlobalForm(List<FormExt> allGlobalForm) {
		this.allGlobalForm = allGlobalForm;
	}
	


	public List<Restful> getGlobalAllRestfulList() {
		return globalAllRestfulList;
	}


	public void setGlobalAllRestfulList(List<Restful> globalAllRestfulList) {
		this.globalAllRestfulList = globalAllRestfulList;
	}




	public List<FormExt> getAllInstForm() {
		return allInstForm;
	}




	public void setAllInstForm(List<FormExt> allInstForm) {
		this.allInstForm = allInstForm;
	}




	public BpmDefExtProperties getExtPropertys() {
		return extPropertys;
	}




	public void setExtPropertys(BpmDefExtProperties extPropertys) {
		this.extPropertys = extPropertys;
	}




	public void setProcessInstAopPluginContexts(List<ProcessInstAopPluginContext> processInstAopPluginContexts) {
		this.processInstAopPluginContexts = processInstAopPluginContexts;
	}




	public void setBoDefList(List<ProcBoDef> boDefList) {
		this.boDefList = boDefList;
	}


	public Map<String,List<BpmVariableDef>> getNodeVariables() {
		return nodeVariables;
	}
	public void setNodeVariables(Map<String,List<BpmVariableDef>> nodeVariables) {
		this.nodeVariables = nodeVariables;
	}
	
}
