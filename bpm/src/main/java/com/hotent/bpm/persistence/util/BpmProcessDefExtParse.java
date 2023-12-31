package com.hotent.bpm.persistence.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.def.BpmDefExtProperties;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.def.ExtProperty;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDefComparator;
import com.hotent.bpm.api.model.process.nodedef.MultiInstanceDef;
import com.hotent.bpm.api.model.process.nodedef.ext.BaseBpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CallActivityNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CustomSignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.GateWayBpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.ServiceNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SubProcessNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.AbstractProcessInstAopPluginContext;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.PluginContext;
import com.hotent.bpm.api.plugin.core.context.ProcessInstAopPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.def.ProcessInstAopPluginDef;
import com.hotent.bpm.defxml.entity.Activity;
import com.hotent.bpm.defxml.entity.CallActivity;
import com.hotent.bpm.defxml.entity.EndEvent;
import com.hotent.bpm.defxml.entity.ExclusiveGateway;
import com.hotent.bpm.defxml.entity.Expression;
import com.hotent.bpm.defxml.entity.FlowElement;
import com.hotent.bpm.defxml.entity.Gateway;
import com.hotent.bpm.defxml.entity.InclusiveGateway;
import com.hotent.bpm.defxml.entity.LoopCharacteristics;
import com.hotent.bpm.defxml.entity.MultiInstanceLoopCharacteristics;
import com.hotent.bpm.defxml.entity.ParallelGateway;
import com.hotent.bpm.defxml.entity.Process;
import com.hotent.bpm.defxml.entity.SequenceFlow;
import com.hotent.bpm.defxml.entity.ServiceTask;
import com.hotent.bpm.defxml.entity.StartEvent;
import com.hotent.bpm.defxml.entity.SubProcess;
import com.hotent.bpm.defxml.entity.UserTask;
import com.hotent.bpm.defxml.entity.ext.BoDef;
import com.hotent.bpm.defxml.entity.ext.BoSaveMode;
import com.hotent.bpm.defxml.entity.ext.ExtDefinitions;
import com.hotent.bpm.defxml.entity.ext.ExtPlugins;
import com.hotent.bpm.defxml.entity.ext.ExtProcess;
import com.hotent.bpm.defxml.entity.ext.ExtProcess.BoList;
import com.hotent.bpm.defxml.entity.ext.ExtProcess.ExtNodes;
import com.hotent.bpm.defxml.entity.ext.ExtProcess.GlobalForm;
import com.hotent.bpm.defxml.entity.ext.ExtProcess.InstForm;
import com.hotent.bpm.defxml.entity.ext.ExtProperties;
import com.hotent.bpm.defxml.entity.ext.Form;
import com.hotent.bpm.defxml.entity.ext.MobileForm;
import com.hotent.bpm.defxml.entity.ext.VarDefs;
import com.hotent.bpm.defxml.entity.ext.VariableDef;
import com.hotent.bpm.model.form.FormCategory;
import com.hotent.bpm.model.process.nodedef.ext.extmodel.DefaultFormExt;
import com.hotent.bpm.model.var.DefaultBpmVariableDef;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDef;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.HandlerFactory;
import com.hotent.bpm.persistence.model.NodeHandler;


public class BpmProcessDefExtParse {
	
	
	
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(BpmProcessDefExtParse.class);

	private static BpmProcessDefExtParse parser = new BpmProcessDefExtParse();
	
	private BpmProcessDefExtParse(){}

	public static BpmProcessDefExtParse getInstance(){   return parser;  }
	
	
	/***
	 * 处理流程定义
	 * @throws Exception 
	 */
	public void handProcessDef(DefaultBpmProcessDef bpmProcessDef,ExtDefinitions def, Process process) throws Exception {
		DefaultBpmProcessDefExt processDefExt = new DefaultBpmProcessDefExt(def);
		ExtProcess extProcess= def.getExtProcess();
		bpmProcessDef.setProcessDefExt(processDefExt);
		if(extProcess!=null) {
			//处理流程的全局插件
			handBpmPluginContexts(processDefExt,extProcess);
			
			//处理节点扩展信息
			handExtProperties(processDefExt,extProcess);
			
			//处理流程变量列表
			handVariables(processDefExt,extProcess);
			// 处理节点流程变量
			handNodeVariables(processDefExt,extProcess,bpmProcessDef.getBpmnNodeDefs());
			
			//BO定义列表
			handBoDefList(processDefExt,extProcess);
			
			//流程实例表单
			handInstFormList(processDefExt,extProcess);
			
			//处理所有的全局表单
			handGlobalFormList(processDefExt,extProcess);
		}
		
		
		List<JAXBElement<? extends FlowElement>> jaxbElementFlowElements =  process.getFlowElement();
		//设置流程定义的节点数据。
		setProcessDefNodes(null,jaxbElementFlowElements,bpmProcessDef);
		//处理节点配置。
		handNodeSetting(bpmProcessDef.getBpmnNodeDefs());
		
		bpmProcessDef.setProcessDefExt(processDefExt);
	}
	
	/**
	 * 解析节点扩展信息。
	 * @param extProcess 
	 */
	private void handExtProperties(DefaultBpmProcessDefExt processDefExt, ExtProcess extProcess) {
		ExtProperties extProperties= extProcess.getExtProperties();
		processDefExt.setExtProperties(convertExtProperties(extProperties));
	}

	/**
	 * 流程定义扩展属性。
	 * @param ext
	 * @return  BpmDefExtProperties
	 */
	private BpmDefExtProperties convertExtProperties(ExtProperties ext){
		BpmDefExtProperties tmp=new BpmDefExtProperties();
		String subjectRule=ext.getSubjectRule();
		if(StringUtil.isNotEmpty(subjectRule)){
			tmp.setSubjectRule(subjectRule);
		}
		tmp.setDescription(ext.getDescription());
		tmp.setStartNotifyType(ext.getStartNotifyType());
		tmp.setArchiveNotifyType(ext.getArchiveNotifyType());
		tmp.setNotifyType(ext.getNotifyType());
		tmp.setSkipFirstNode(ext.isSkipFirstNode());
		tmp.setFirstNodeUserAssign(ext.isFirstNodeUserAssign());
		tmp.setSkipSameUser(ext.isSkipSameUser());
		tmp.setAllowCopyTo(ext.isAllowCopyTo());
		tmp.setAllowTransTo(ext.isAllowTransTo());
		tmp.setUseMainForm(ext.getUseMainForm());
		tmp.setAllowReference(ext.isAllowReference());
		tmp.setAllowRefCounts(ext.getAllowRefCounts());
		tmp.setDateType(ext.getDateType());
		tmp.setDueTime(ext.getDueTime());
		
		//跳过任务当执行人为空时
		tmp.setSkipExecutorEmpty(ext.isSkipExecutorEmpty());
		//允许任务执行人为空。
		tmp.setAllowExecutorEmpty(ext.isAllowExecutorEmpty());
		//设置测试状态通知类型。
		tmp.setTestNotifyType(ext.getTestNotifyType());
		//查看已办数据版本
		tmp.setDoneDataVersion(ext.getDoneDataVersion());
		
		List<ExtProperty> rtnList=new ArrayList<ExtProperty>();
		
		List<com.hotent.bpm.defxml.entity.ext.ExtProperty> list= ext.getExtProperty();
		
		if(BeanUtils.isNotEmpty(list)){
			for(com.hotent.bpm.defxml.entity.ext.ExtProperty ex:list){
				ExtProperty extProp=new ExtProperty(ex.getName(),ex.getValue());
				rtnList.add(extProp);
			}
		}
		
		//增加跳转规则。
		tmp.setSkipRules(ext.getSkipRules());

		tmp.setExtProperty(rtnList);
		return tmp;
		
	}
	
	/**
	 * 解析流程的全局插件。
	 * @return List&lt;BpmPluginContext>
	 */
	private void handBpmPluginContexts(DefaultBpmProcessDefExt processDefExt, ExtProcess extProcess){
		ExtPlugins extPlugins= extProcess.getExtPlugins();
		if(extPlugins==null)return ;				
		
		ArrayList<BpmPluginContext> pluginContextList=new ArrayList<BpmPluginContext>();
		ArrayList<ProcessInstAopPluginContext> processInstAopPluginContexts = new ArrayList<ProcessInstAopPluginContext>();
		
		List<Object> list= extPlugins.getAny();
		for(Object obj:list){
			if(!(obj instanceof Element  )) continue;

			Element el=(Element)obj;
			//获取插件上下文定义。
			PluginContext pluginContext=(BpmPluginContext)AppUtil.getBean(el.getLocalName() + PluginContext.PLUGINCONTEXT);
			
			if(pluginContext==null) continue;
			
			if(pluginContext instanceof BpmPluginContext){
				
				//解析插件的XML数据。
				BpmPluginDef bpmPluginDef = pluginContext.parse(el);
				((AbstractBpmPluginContext)pluginContext).setBpmPluginDef(bpmPluginDef);
				//获取插件列表。
				pluginContextList.add((BpmPluginContext)pluginContext);
			}else if(pluginContext instanceof ProcessInstAopPluginContext){
				ProcessInstAopPluginDef processInstAopPluginDef = (ProcessInstAopPluginDef)pluginContext.parse(el);
				((AbstractProcessInstAopPluginContext)pluginContext).setProcessInstAopPluginDef(processInstAopPluginDef);
				processInstAopPluginContexts.add((ProcessInstAopPluginContext)pluginContext);
			}
		}
		
		processDefExt.setPluginContextList(pluginContextList);
		processDefExt.setProcessInstAopPluginContexts(processInstAopPluginContexts);
	}
	
	
	/**
	 * 解析流程变量列表。
	 * @return 
	 * List&lt;VarialbeDef>
	 */
	private void handVariables(DefaultBpmProcessDefExt processDefExt, ExtProcess extProcess) {
		VarDefs varDefs= extProcess.getVarDefs();
		if(varDefs==null) return ;
		List<VariableDef> list = varDefs.getVariableDef();
		
		ArrayList<BpmVariableDef> varList=new ArrayList<BpmVariableDef>();
		for(VariableDef v:list){
			DefaultBpmVariableDef pv=new DefaultBpmVariableDef();
			pv.setDataType(v.getType()!=null?v.getType().value():"string");
			pv.setName(v.getName());
			pv.setRequired(v.isIsRequired()==null?false:v.isIsRequired());
			pv.setVarKey(v.getKey());
			pv.setDescription(v.getDescription());
			Object val=DefaultBpmVariableDef.getValue(pv.getDataType(), v.getDefaultVal());
			pv.setDefaultVal(val);
			varList.add(pv);
		}
		
		processDefExt.setVarList(varList);
	}
	
	/**
	 * 解析流程变量列表。
	 * @return 
	 * List&lt;VarialbeDef>
	 */
	private void handNodeVariables(DefaultBpmProcessDefExt processDefExt, ExtProcess extProcess, List<BpmNodeDef> list) {
		ExtNodes extNodes= extProcess.getExtNodes();
		List<Object> baseNodes=extNodes.getUserNodeOrSignNodeOrBaseNode();
		Map<String,List<BpmVariableDef>> nodeVariables = new HashMap<String, List<BpmVariableDef>>();
		
		for(Object baseNode:baseNodes){
			String nodeId=BpmDefAccessorUtil.getNodeId(baseNode);
			List<BpmVariableDef> varList=new ArrayList<BpmVariableDef>();
			

			VarDefs varDefs=BpmDefAccessorUtil.getVarDefs(baseNode);
			if(varDefs==null) continue;
			
			List<VariableDef> varDefList=varDefs.getVariableDef();
			for(VariableDef v:varDefList){
				DefaultBpmVariableDef pv=new DefaultBpmVariableDef();
				pv.setDataType(v.getType().value());
				pv.setName(v.getName());
				pv.setRequired(v.isIsRequired()==null?false:v.isIsRequired());
				pv.setVarKey(v.getKey());
				Object val=DefaultBpmVariableDef.getValue(pv.getDataType(), v.getDefaultVal());
				pv.setDefaultVal(val);
				varList.add(pv);
			}
			
			nodeVariables.put(nodeId, varList);
		}
		processDefExt.setNodeVariables(nodeVariables);
	}
	
	/**
	 * 获取BO定义列表。
	 * @return 
	 * List&lt;ProcBoDef>
	 */
	private void handBoDefList(DefaultBpmProcessDefExt processDefExt, ExtProcess extProcess){
		BoList boList= extProcess.getBoList();
		
		if(boList==null) return ;
		
		BoSaveMode saveMode= boList.getSaveMode();
		
		if(BeanUtils.isEmpty(saveMode)){
			saveMode = BoSaveMode.DATABASE;
		}
		//设置BO保存的模式。
		processDefExt.setBoSaveToDb(saveMode.equals(BoSaveMode.DATABASE));
		
		List<BoDef> list= boList.getBoDef();
		ArrayList<ProcBoDef> boDefList = new ArrayList<ProcBoDef>();
		for(BoDef bo:list){
			ProcBoDef procBodef=new ProcBoDef(bo.getName(),bo.getKey());
			procBodef.setParentDefKey(bo.getParentDefKey());
			procBodef.setRequired(bo.isIsRequired());
			
			boDefList.add(procBodef);
		}
		processDefExt.setBoDefList(boDefList);
	}

	/**
	 * 取得表单的流程实例表单。
	 * @return  com.hotent.runtime.api.model.process.nodedef.ext.extmodel.InstForm
	 */
	private void handInstFormList(DefaultBpmProcessDefExt processDefExt, ExtProcess extProcess){
		
		InstForm instForm= extProcess.getInstForm();
		if(instForm == null) return;
		List<FormExt> forms = new ArrayList<FormExt>();
		
		List<Form> list = instForm.getFormOrMobileForm();
		//com.hotent.runtime.core.defxml.entity.ext.FormExt
		for (Form frm : list) {
			String pareFlowKey = frm.getParentFlowKey();
			FormExt form= new DefaultFormExt();
			form.setParentFlowKey(pareFlowKey);
			form.setName(frm.getName());
			if(frm instanceof MobileForm){
				form.setFormType(FormType.MOBILE.value());
			}
			else{
				com.hotent.bpm.defxml.entity.ext.FormExt frmExt=(com.hotent.bpm.defxml.entity.ext.FormExt)frm;
				form.setPrevHandler(frmExt.getPrevHandler());
				form.setPostHandler(frmExt.getPostHandler());
			}
			form.setFormValue(frm.getFormValue());
			form.setFormExtraConf(frm.getFormExtraConf());
			form.setHelpFile(frm.getHelpFile());
			if(frm.getType() != null){
				form.setType(FormCategory.fromValue(frm.getType().value()));
			}
				
			forms.add(form);
		}
		processDefExt.setAllInstForm(forms);
	}
	
	private void  handGlobalFormList(DefaultBpmProcessDefExt processDefExt, ExtProcess extProcess){
		GlobalForm globalForm= extProcess.getGlobalForm();
		if(globalForm == null) return;
		List<FormExt> forms = new ArrayList<FormExt>();
		
		List<Form> list = globalForm.getFormOrMobileForm();
		//com.hotent.runtime.core.defxml.entity.ext.FormExt
		for (Form frm : list) {
			String pareFlowKey = frm.getParentFlowKey();
			FormExt form= new DefaultFormExt();
			form.setParentFlowKey(pareFlowKey);
			form.setName(frm.getName());
			if(frm instanceof MobileForm){
				form.setFormType(FormType.MOBILE.value());
			}
			else{
				com.hotent.bpm.defxml.entity.ext.FormExt frmExt=(com.hotent.bpm.defxml.entity.ext.FormExt)frm;
				form.setPrevHandler(frmExt.getPrevHandler());
				form.setPostHandler(frmExt.getPostHandler());
			}
			form.setFormValue(frm.getFormValue());
			form.setFormExtraConf(frm.getFormExtraConf());
			form.setHelpFile(frm.getHelpFile());
			if(frm.getType() != null){
				form.setType(FormCategory.fromValue(frm.getType().value()));
			}
				
			forms.add(form);
		}
		processDefExt.setAllGlobalForm(forms);
	}
	
	
	/**
	 * 处理节点插件。
	 * <pre>
	 * 如果是子流程中的节点，那么插件的节点ID需要如下写法：
	 * 
	 * &lt;ext:extNode nodeType="" name="" bpmnElement="subprocess1/userTask1">
     *   		&lt;ext:extPlugins>
     *   			
     *   		&lt;/ext:extPlugins>
     *  &lt;/ext:extNode>
	 * </pre>
	 * @param list 
	 * void
	 * @throws Exception 
	 */
	private void handNodeSetting(List<BpmNodeDef> list) throws Exception{
		for(BpmNodeDef def:list){
			handNode((BaseBpmNodeDef) def);
			
			if(!(def instanceof SubProcessNodeDef)) continue;
			
			SubProcessNodeDef subDef=(SubProcessNodeDef)def;
			BpmProcessDef<?> proc= subDef.getChildBpmProcessDef();
			
			if(proc!=null){
				List<BpmNodeDef> templist=proc.getBpmnNodeDefs();
				handNodeSetting(templist);
			}
		}
	}
	
	
	
	
	/**
	 * 设置流程定义的节点定义。 
	 * @param parentNodeDef
	 * @param jaxbElementFlowElements
	 * @param bpmProcessDef
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setProcessDefNodes(BpmNodeDef parentNodeDef, 
			List<JAXBElement<? extends FlowElement>> jaxbElementFlowElements,
			DefaultBpmProcessDef bpmProcessDef){
		//将流程节点转换成map，键表示节点名称，值为节点的元素。
		Map<String,FlowElement> nodeMap=getNodeList(jaxbElementFlowElements);
		//获取节点连线
		List<SequenceFlow> seqList=getSequenceFlowList(jaxbElementFlowElements);
		
		Map<String,BpmNodeDef> nodeDefMap= getBpmNodeDef(nodeMap,parentNodeDef,bpmProcessDef);
		
		//设置节点之间的关联关系。
		setRelateNodeDef(nodeDefMap,seqList);
		
		
		//设置节点的流程定义。
		List<BpmNodeDef> nodeDefList=new ArrayList<BpmNodeDef>(nodeDefMap.values());
		//节点排序
		Collections.sort(nodeDefList, new BpmNodeDefComparator());
		
		//设置流程定义列表。
		bpmProcessDef.setBpmnNodeDefs(nodeDefList);
		
		for(BpmNodeDef nodeDef:nodeDefList){
			BaseBpmNodeDef node=(BaseBpmNodeDef)nodeDef;
			node.setBpmProcessDef((BpmProcessDef)bpmProcessDef);
		}
		
		
	}
	
	/**
	 * 处理节点插件。
	 * @param nodeDef 
	 * void
	 * @throws Exception 
	 */
	private void handNode(BaseBpmNodeDef nodeDef) throws Exception{
		String nodeId=nodeDef.getNodeId();
		//打印节点。
		LOGGER.debug("nodeId:{}", nodeId);
		BpmProcessDef<BpmProcessDefExt> processDef=nodeDef.getRootProcessDef();
		DefaultBpmProcessDefExt processDefExt=(DefaultBpmProcessDefExt) processDef.getProcessDefExt();
		ExtDefinitions def= processDefExt.getDefinitions();
		
		ExtProcess extProcess= def.getExtProcess();
		if(extProcess==null) return ;
		
		ExtNodes extNodes= extProcess.getExtNodes();
		
		if(extNodes==null) return ;
		
		List<Object> nodeList= extNodes.getUserNodeOrSignNodeOrBaseNode();
		
		
		
		if(BeanUtils.isEmpty(nodeList)) return;
		
		for(Object extNode:nodeList){
			String bpmnNodeId= BpmDefAccessorUtil.getNodeId(extNode);
			//extNode.getBpmnElement();
			if(!bpmnNodeId.equalsIgnoreCase(nodeId)){
				continue;
			}
			NodeHandler nodeHanler= HandlerFactory.createHandler(nodeDef);
			
			if(nodeHanler!=null){
				nodeHanler.handNode(nodeDef,extNode);
			}				
		}
	}
	
	
	
	
	
	/**
	 * 将流程节点转换成节点BpmNodeDef Map
	 * @param nodeMap			流程节点Map
	 * @param parentNodeDef		父级节点定义
	 * @return 
	 * Map&lt;String,BpmNodeDef>
	 */
	private Map<String,BpmNodeDef> getBpmNodeDef(Map<String,FlowElement> nodeMap,
			BpmNodeDef parentNodeDef,DefaultBpmProcessDef bpmProcessDef){
		Map<String,BpmNodeDef> map=new HashMap<String, BpmNodeDef>();
		Set<Entry<String,FlowElement>> set= nodeMap.entrySet();
		for(Iterator<Entry<String,FlowElement>> it=set.iterator();it.hasNext(); ){
			Entry<String,FlowElement> ent=it.next();
			FlowElement flowEl=ent.getValue();
			
			BaseBpmNodeDef nodeDef= getNodeDef(parentNodeDef,flowEl,bpmProcessDef);
			
			map.put(ent.getKey(), nodeDef);
		}
		return map;
	}
	
	/**
	 * 设置节点之间的关联。
	 * @param nodeDefMap	节点定义MAP
	 * @param seqList 		SequenceFlow列表。
	 * void
	 */
	private void setRelateNodeDef(Map<String,BpmNodeDef> nodeDefMap,List<SequenceFlow> seqList){
		for(SequenceFlow seq:seqList){
		    if(BeanUtils.isNotEmpty((FlowElement)seq.getSourceRef()) && BeanUtils.isNotEmpty((FlowElement)seq.getTargetRef())) {
                FlowElement source = (FlowElement) seq.getSourceRef();
                FlowElement target = (FlowElement) seq.getTargetRef();
                BpmNodeDef sourceDef = nodeDefMap.get(source.getId());
                BpmNodeDef targetDef = nodeDefMap.get(target.getId());
                sourceDef.addOutcomeNode(targetDef);
                targetDef.addIncomeNode(sourceDef);
                //设置节点条件。
                Expression conditionEx = seq.getConditionExpression();
                if (conditionEx != null) {
                    String condition = "";
                    List<?> list = conditionEx.getContent();
                    for (Object str : list) {
                        condition += str.toString();
                    }
                    // condition=StringUtil.trimPrefix(condition, "${");
                    // condition=StringUtil.trimSuffix(condition, "}");
                    sourceDef.addCondition(targetDef.getNodeId(), condition);
                }
            }
		}
	}
	
	/**
	 * 添加流程节点。
	 * @param list
	 * @return 
	 * Map<String,FlowElement>
	 */
	private Map<String,FlowElement> getNodeList(List<JAXBElement<? extends FlowElement>> list){
		Map<String,FlowElement> map=new HashMap<String,FlowElement>();
		for(JAXBElement<? extends FlowElement> jAXBElement:list){
			FlowElement flowElement = jAXBElement.getValue();
			addNode(flowElement,map,aryNodeElement);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private Class<FlowElement>[] aryNodeElement=new  Class[]{
			StartEvent.class,
			EndEvent.class,
			ParallelGateway.class,
			InclusiveGateway.class,
			ExclusiveGateway.class,
			UserTask.class,
			ServiceTask.class,
			CallActivity.class,
			SubProcess.class
	};
	
	/**
	 * 获取流程的连线。
	 * @param list
	 * @return 
	 * List&lt;SequenceFlow>
	 */
	private List<SequenceFlow> getSequenceFlowList(List<JAXBElement<? extends FlowElement>> list){
		List<SequenceFlow> nodeList=new ArrayList<SequenceFlow>();
		for(JAXBElement<? extends FlowElement> jAXBElement:list){
			FlowElement flowElement = jAXBElement.getValue();
			if(flowElement instanceof SequenceFlow){
				nodeList.add((SequenceFlow)flowElement);
			}
		}
		return nodeList;
	}
	
	
	/**
	 * 将FlowElement 转换成DefaultBpmNodeDef定义。
	 * @param parentNodeDef			父级节点定义，第一级节点父节点为空。
	 * @param flowElement			当前节点的节点的定义。
	 * @param bpmProcessDef			当前流程定义。
	 * @return 
	 * BaseBpmNodeDef
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private BaseBpmNodeDef getNodeDef(BpmNodeDef parentNodeDef, FlowElement flowElement ,DefaultBpmProcessDef bpmProcessDef){
		
		BaseBpmNodeDef nodeDef=null;
		if(flowElement instanceof StartEvent){//开始节点
			nodeDef=new BaseBpmNodeDef();
			nodeDef.setType(NodeType.START);
		}
		else if(flowElement instanceof EndEvent){//结束节点
			nodeDef=new BaseBpmNodeDef();
			nodeDef.setType(NodeType.END);
		}
		else if(flowElement instanceof Gateway){//网关节点
			nodeDef=new GateWayBpmNodeDef();
			// 同步网关
			if(flowElement instanceof ParallelGateway){
				nodeDef.setType(NodeType.PARALLELGATEWAY);
			}
			//条件网关
			else if(flowElement instanceof InclusiveGateway){
				nodeDef.setType(NodeType.INCLUSIVEGATEWAY);
			}
			//分支网关
			else if(flowElement instanceof ExclusiveGateway){
				nodeDef.setType(NodeType.EXCLUSIVEGATEWAY);
			}
		}
		else if(flowElement instanceof Activity){
			
			String multi=getNodeDefLoop((Activity) flowElement);
			if(flowElement instanceof UserTask){//用户节点
				Map<QName, String> otherAttributes = flowElement.getOtherAttributes();
				QName signType = new QName("signType");
				if(BpmConstants.NO.equals(multi)){//普通用户节点
					UserTaskNodeDef userTaskDef=new UserTaskNodeDef();
					nodeDef=userTaskDef;
					nodeDef.setType(NodeType.USERTASK);
					
				}
				else{
					if(otherAttributes.containsKey(signType) && StringUtils.isNotEmpty(otherAttributes.get(signType)) ) {
						//签署并审节点
						CustomSignNodeDef customSignNodeDef=new CustomSignNodeDef();
						boolean isParallel= BpmConstants.MULTI_INSTANCE_PARALLEL.equals(multi);
						customSignNodeDef.setParallel(isParallel);
						customSignNodeDef.setSignType(otherAttributes.get(signType));
						nodeDef=customSignNodeDef;
						nodeDef.setType(NodeType.CUSTOMSIGNTASK);
						nodeDef.setAttribute("signtype", otherAttributes.get(signType));
						
					}else {
						//会签节点
						SignNodeDef signNodeDef=new SignNodeDef();
						boolean isParallel= BpmConstants.MULTI_INSTANCE_PARALLEL.equals(multi);
						signNodeDef.setParallel(isParallel);
						nodeDef=signNodeDef;
						nodeDef.setType(NodeType.SIGNTASK);
					}
					
				}
			}
			//自动节点处理方式。
			else if(flowElement instanceof ServiceTask){
				//nodeDef=handServiceTask(parentNodeDef,flowElement,bpmProcessDef);
				nodeDef = new ServiceNodeDef();
				
			}
			//外部子流程
			else if(flowElement instanceof CallActivity){
				
				CallActivityNodeDef callNodeDef=new CallActivityNodeDef();
				CallActivity call=(CallActivity)flowElement;
				String flowKey=call.getCalledElement();
				callNodeDef.setType(NodeType.CALLACTIVITY);
				callNodeDef.setFlowKey(flowKey);
				
				setMulti(callNodeDef,multi);
				//外部子流程就不进行递归了。
				//如果需要调取子流程的定义，通过流程定义ID获取流程定义。
				nodeDef=callNodeDef;
				
			}
			//子流程节点
			else if(flowElement instanceof SubProcess){
				SubProcessNodeDef subProcessDef=new SubProcessNodeDef();
				
				setMulti(subProcessDef,multi);
				
				nodeDef=subProcessDef;
				nodeDef.setNodeId(flowElement.getId());
				nodeDef.setName(flowElement.getName()) ;
				nodeDef.setParentBpmNodeDef(parentNodeDef);
				
				subProcessDef.setBpmProcessDef((BpmProcessDef)bpmProcessDef);
				SubProcess subProcess=(SubProcess)flowElement;
				//处理子流程节点
				handSubProcess(nodeDef,subProcess,bpmProcessDef);
			}
		}

		nodeDef.setParentBpmNodeDef(parentNodeDef);
		nodeDef.setNodeId(flowElement.getId());
		nodeDef.setName(flowElement.getName()) ;
		nodeDef.setOrder(BpmUtil.getFlowElementOrder(flowElement));
		return nodeDef;
	}
	
	private void setMulti(MultiInstanceDef nodeDef,String multi){
		if(BpmConstants.NO.equals(multi)){
			nodeDef.setSupportMuliInstance(false);
		}
		else{
			nodeDef.setSupportMuliInstance(true);
			boolean isParallel=BpmConstants.MULTI_INSTANCE_PARALLEL.equals(multi);
			nodeDef.setParallel(isParallel);
		}
	}
	
	/**
	 * 添加流程节点。 
	 * @param flowElement
	 * @param map
	 * @param flowTypes 
	 * void
	 */
	@SuppressWarnings("unchecked")
	private void addNode(FlowElement flowElement,Map<String,FlowElement> map,Class<? extends FlowElement>... flowTypes){
		for(Class<? extends FlowElement> flowType:flowTypes){
			if(flowType.isInstance(flowElement)){
				map.put(flowElement.getId(), flowElement);
				break;
			}
		}
	}
	

	
	/**
	 * 处理子流程节点。
	 * @param nodeDef
	 * @param subProcess 
	 * void
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void handSubProcess(BaseBpmNodeDef nodeDef,SubProcess subProcess,DefaultBpmProcessDef parentProcessDef){
		DefaultBpmProcessDef bpmProcessDef=new DefaultBpmProcessDef();
		bpmProcessDef.setProcessDefinitionId(subProcess.getId());
		bpmProcessDef.setName(subProcess.getName());
		bpmProcessDef.setDefKey(subProcess.getId());
		bpmProcessDef.setParentProcessDef(parentProcessDef);
		
		SubProcessNodeDef subNodeDef=(SubProcessNodeDef)nodeDef;

		subNodeDef.setBpmProcessDef((BpmProcessDef)parentProcessDef);
		subNodeDef.setChildBpmProcessDef((BpmProcessDef)bpmProcessDef);
		List<JAXBElement<? extends FlowElement>>  list=subProcess.getFlowElement();
		setProcessDefNodes(nodeDef,list,bpmProcessDef);
	}
	
	/**
	 * 设置多实例属性。
	 * @param nodedef
	 * @param flowElement 
	 * void
	 */
	private String getNodeDefLoop(Activity flowElement){
		JAXBElement<? extends LoopCharacteristics> jaxbloop= flowElement.getLoopCharacteristics();
		if(jaxbloop==null) return "no";
		MultiInstanceLoopCharacteristics loop=(MultiInstanceLoopCharacteristics) jaxbloop.getValue();
		return loop.isIsSequential()?"sequence":"parallel";
		
	}

	
	

}
