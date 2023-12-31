package com.hotent.bpm.api.model.process.nodedef.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.FormInitItem;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;
import com.hotent.bpm.api.plugin.core.task.TaskActionHandlerConfig;
import com.hotent.bpm.model.form.Form;

public class BaseBpmNodeDef implements BpmNodeDef {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2044846605763777657L;

	private String nodeId;
	private String name;
	private NodeType type;
	private Integer order = 0;

	private BpmNodeDef parentBpmNodeDef;
	private List<BpmNodeDef> incomeNodes = new ArrayList<BpmNodeDef>();
	private List<BpmNodeDef> outcomeNodes = new ArrayList<BpmNodeDef>();

	private List<BpmPluginContext> bpmPluginContexts = new ArrayList<BpmPluginContext>();
	private Map<String, String> attrMap = new HashMap<String, String>();

	private BpmProcessDef<BpmProcessDefExt> bpmProcessDef;

	/**
	 * 子流程配置和主流程相关的表单。
	 */
	private List<Form> subFormList = new ArrayList<Form>();

	/**
	 * 节点配置表单。
	 */
	private List<Form> forms;

	/**
	 * 节点后续条件。
	 */
	private Map<String, String> conditionMap = new HashMap<String, String>();
	/**
	 * 脚本定义。
	 */
	private Map<ScriptType, String> scriptMap = new HashMap<ScriptType, String>();

	/**
	 * 表单初始化项。
	 */
	private List<FormInitItem> formInitItems = new ArrayList<FormInitItem>();

	/**
	 * 节点属性。
	 */
	private List<NodeProperties> nodeProperties = new ArrayList<NodeProperties>();

	private List<Button> buttons = null;

	private List<Button> buttonList = null;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public List<BpmNodeDef> getIncomeNodes() {
		return incomeNodes;
	}

	@JsonBackReference
	public void setIncomeNodes(List<BpmNodeDef> incomeNodes) {
		this.incomeNodes = incomeNodes;
	}

	public List<BpmNodeDef> getOutcomeNodes() {
		return outcomeNodes;
	}

	@JsonBackReference
	public void setOutcomeNodes(List<BpmNodeDef> outcomeNodes) {
		this.outcomeNodes = outcomeNodes;
	}

	public List<BpmPluginContext> getBpmPluginContexts() {
		return bpmPluginContexts;
	}

	public void setBpmPluginContexts(List<BpmPluginContext> bpmPluginContexts) {
		if (BeanUtils.isNotEmpty(bpmPluginContexts)) {
			Collections.sort(bpmPluginContexts, new Comparator<BpmPluginContext>() {
				@Override
				public int compare(BpmPluginContext o1, BpmPluginContext o2) {
					int diff = o1.getOrder() - o2.getOrder();
					if (diff > 0) {
						return 1;
					} else if (diff < 0) {
						return -1;
					}
					return 0;
				}
			});
		}
		this.bpmPluginContexts = bpmPluginContexts;
	}

	public void setAttribute(String name, String value) {
		name = name.toLowerCase().trim();
		attrMap.put(name.toLowerCase(), value);

	}

	@Override
	public String getAttribute(String name) {
		name = name.toLowerCase().trim();
		return attrMap.get(name);

	}

	@Override
	public void addIncomeNode(BpmNodeDef bpmNodeDef) {
		this.incomeNodes.add(bpmNodeDef);
	}

	@Override
	public void addOutcomeNode(BpmNodeDef bpmNodeDef) {
		this.outcomeNodes.add(bpmNodeDef);

	}

	@Override
	@JsonIgnore
	public BpmNodeDef getParentBpmNodeDef() {
		return this.parentBpmNodeDef;
	}

	public void setParentBpmNodeDef(BpmNodeDef parentBpmNodeDef) {
		this.parentBpmNodeDef = parentBpmNodeDef;
	}

	@Override
	public String getRealPath() {
		String id = this.getNodeId();
		BpmNodeDef parent = getParentBpmNodeDef();
		while (parent != null) {
			id = parent.getNodeId() + "/" + id;
			parent = parent.getParentBpmNodeDef();
		}
		return id;
	}

	@JsonIgnore
	public BpmProcessDef<BpmProcessDefExt> getBpmProcessDef() {
		return bpmProcessDef;
	}

	public void setBpmProcessDef(BpmProcessDef<BpmProcessDefExt> bpmProcessDef) {
		this.bpmProcessDef = bpmProcessDef;
	}

	@JsonIgnore
	public BpmProcessDef<BpmProcessDefExt> getRootProcessDef() {
		BpmProcessDef<BpmProcessDefExt> processDef = this.bpmProcessDef;
		BpmProcessDef<BpmProcessDefExt> parent = processDef.getParentProcessDef();
		while (parent != null) {
			processDef = parent;
			parent = parent.getParentProcessDef();
		}
		return processDef;
	}

	private Form getForm(String formType) {
		if (BeanUtils.isEmpty(this.forms)) {
			return null;
		}
		for (Form form : forms) {
			if (form.getFormType().equals(formType)) {
				return form;
			}
		}
		return null;
	}

	public Form getForm() {
		return getForm(FormType.PC.value());
	}

	@Override
	public Form getMobileForm() {
		return getForm(FormType.MOBILE.value());
	}

	public void setForm(List<Form> forms) {
		this.forms = forms;
	}

	@Override
	@JsonIgnore
	public List<BpmNodeDef> getOutcomeTaskNodes() {
		return getNodeDefList(outcomeNodes);
	}

	private List<BpmNodeDef> getNodeDefList(List<BpmNodeDef> bpmNodeDefs) {
		List<BpmNodeDef> bpmNodeList = new ArrayList<BpmNodeDef>();
		for (BpmNodeDef def : bpmNodeDefs) {
			if (def.getType().equals(NodeType.CALLACTIVITY)) {
				continue;
			}
			if (NodeType.USERTASK.equals(def.getType()) || NodeType.SIGNTASK.equals(def.getType())) {
				bpmNodeList.add(def);
			} else if (NodeType.SUBPROCESS.equals(def.getType())) {
				SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef) def;
				// 取得子流程中的开始节点
				BpmNodeDef startNodeDef = subProcessNodeDef.getChildBpmProcessDef().getStartEvent();
				bpmNodeList.addAll(getNodeDefList(startNodeDef.getOutcomeNodes()));
			} else if (NodeType.END.equals(def.getType()) && def.getParentBpmNodeDef() != null
					&& NodeType.SUBPROCESS.equals(def.getParentBpmNodeDef().getType())) {
				SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef) def.getParentBpmNodeDef();
				bpmNodeList.addAll(getNodeDefList(subProcessNodeDef.getOutcomeNodes()));
			} else {
				bpmNodeList.addAll(getNodeDefList(def.getOutcomeNodes()));
			}
		}
		return bpmNodeList;
	}

	@Override
	@JsonIgnore
	public List<BpmNodeDef> getInnerOutcomeTaskNodes(boolean includeSignTask) {
		List<BpmNodeDef> bpmNodeList = getInnerOutcomeTaskNodes(outcomeNodes, includeSignTask);
		return bpmNodeList;
	}

	private List<BpmNodeDef> getInnerOutcomeTaskNodes(List<BpmNodeDef> bpmNodeDefs, boolean includeSignTask) {
		List<BpmNodeDef> bpmNodeList = new ArrayList<BpmNodeDef>();
		for (BpmNodeDef def : bpmNodeDefs) {
			if (NodeType.USERTASK.equals(def.getType())
					|| (includeSignTask && NodeType.SIGNTASK.equals(def.getType()))) {
				bpmNodeList.add(def);
			} else if (NodeType.SUBPROCESS.equals(def.getType()) || NodeType.CALLACTIVITY.equals(def.getType())
					|| NodeType.END.equals(def.getType())) {
				continue;
			} else {
				bpmNodeList.addAll(getInnerOutcomeTaskNodes(def.getOutcomeNodes(), includeSignTask));
			}
		}
		return bpmNodeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	@JsonIgnore
	public <T> T getPluginContext(Class<T> cls) {
		BpmPluginContext ctx = null;
		List<BpmPluginContext> list = this.bpmPluginContexts;
		for (BpmPluginContext context : list) {
			if (context.getClass().isAssignableFrom(cls)) {
				ctx = context;
				break;
			}
		}
		return (T) ctx;
	}

	@Override
	public List<Form> getSubFormList() {
		return this.subFormList;
	}

	@Override
	public Form getSubForm(String bpmDefKey, FormType formType) {
		if (BeanUtils.isEmpty(this.subFormList))
			return null;

		for (Form frm : subFormList) {
			if (bpmDefKey.equals(frm.getParentFlowKey()) && frm.getFormType().equals(formType.value())) {
				return frm;
			}
		}
		return null;
	}

	@Override
	public void setSubFormList(List<Form> list) {
		this.subFormList = list;
	}

	@Override
	public Map<String, String> getConditions() {
		return conditionMap;
	}

	@Override
	public void setConditions(Map<String, String> conditions) {
		this.conditionMap = conditions;
	}

	@Override
	public void addCondition(String targetNode, String condition) {
		conditionMap.put(targetNode, condition);
	}

	@Override
	public Map<ScriptType, String> getScripts() {
		return scriptMap;
	}

	@Override
	public void addScript(ScriptType scriptType, String script) {
		this.scriptMap.put(scriptType, script);

	}

	public List<FormInitItem> getFormInitItems() {
		return formInitItems;
	}

	public void setFormInitItems(List<FormInitItem> formInitItems) {
		this.formInitItems = formInitItems;
	}

	public void addFormInitItem(FormInitItem initItem) {
		this.formInitItems.add(initItem);
	}

	@Override
	public FormInitItem getFormInitItem() {
		if (BeanUtils.isEmpty(this.formInitItems))
			return null;

		FormInitItem myItem = null;
		for (FormInitItem item : this.formInitItems) {
			if (StringUtil.isNotEmpty(item.getParentDefKey())) {
				myItem = item;
				break;
			}
		}
		return myItem;

	}

	@Override
	public FormInitItem getFormInitItemByParentKey(String parentDefKey) {
		if (BeanUtils.isEmpty(this.formInitItems))
			return null;
		FormInitItem myItem = null;
		for (FormInitItem item : this.formInitItems) {
			if (StringUtil.isEmpty(item.getParentDefKey()))
				continue;
			if (parentDefKey.equals(item.getParentDefKey())) {
				myItem = item;
				break;
			}
		}
		return myItem;
	}

	public List<NodeProperties> getNodeProperties() {
		return nodeProperties;
	}

	public void setNodeProperties(List<NodeProperties> nodeProperties) {
		this.nodeProperties = nodeProperties;
	}

	public void addNodeProperties(NodeProperties prop) {
		nodeProperties.add(prop);
	}

	/**
	 * 取得节点属性信息。
	 * 
	 * @return NodeProperties
	 */
	public NodeProperties getLocalProperties() {
		for (NodeProperties prop : this.nodeProperties) {
			if (StringUtil.isNotEmpty(prop.getParentDefKey()) && BpmConstants.LOCAL.equals(prop.getParentDefKey())) {
				return prop;
			}
		}
		NodeProperties prop = new NodeProperties();
		prop.setNodeId(nodeId);
		return prop;
	}

	/**
	 * 根据父key获取节点属性。
	 * 
	 * @param parentDefKey
	 * @return NodeProperties
	 */
	public NodeProperties getPropertiesByParentDefKey(String parentDefKey) {
		for (NodeProperties prop : this.nodeProperties) {
			if (StringUtil.isNotEmpty(prop.getParentDefKey()) && prop.getParentDefKey().equals(parentDefKey)) {
				return prop;
			}
		}
		// 如果没有配置且父流程key不为空 则返回一个空的对象。
		if (StringUtil.isNotEmpty(parentDefKey)) {
			NodeProperties prop = new NodeProperties();
			prop.setNodeId(nodeId);
			prop.setParentDefKey(parentDefKey);
			return prop;
		}

		return null;
	}

	@Override
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}

	@Override
	public List<Button> getButtons() {
		if (this.buttons != null)
			return this.buttons;
		if (buttonList != null)
			return buttonList;
		List<Button> list = getButtonsByType(true);
		this.buttonList = list;
		return buttonList;
	}

	@Override
	public List<Button> getButtonsByType(boolean isInit) {
		List<Button> buttonList = new ArrayList<Button>();

		TaskActionHandlerConfig config = (TaskActionHandlerConfig) AppUtil.getBean(TaskActionHandlerConfig.class);
		List<? extends TaskActionHandlerDef> list = config.getActionHandlerDefList(isInit);

		for (TaskActionHandlerDef def : list) {
			// 开始节点
			if (NodeType.START.equals(this.type)
					&& def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_START)) {
				buttonList.add(new Button(def.getDescription(), def.getName(), def.isSupportScript()));
			}
			if (NodeType.USERTASK.equals(this.type)
					&& (def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_USERTASK)
							|| def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_BOTH))) {
				buttonList.add(new Button(def.getDescription(), def.getName(), def.isSupportScript()));
			}
			if (NodeType.SIGNTASK.equals(this.type)
					&& (def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_SIGNTASK)
							|| def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_BOTH))) {
				buttonList.add(new Button(def.getDescription(), def.getName(), def.isSupportScript()));
			}
			if (NodeType.CUSTOMSIGNTASK.equals(this.type) && attrMap.containsKey("signtype")
					&& CustomSignNodeDef.SIGNTYPE_PARALLEL.equals(attrMap.get("signtype"))
					&& (def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_CUSTOMSIGNTASK)
							|| def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_BOTH))) {
				buttonList.add(new Button(def.getDescription(), def.getName(), def.isSupportScript()));
			}
			if (NodeType.CUSTOMSIGNTASK.equals(this.type) && attrMap.containsKey("signtype")
					&& CustomSignNodeDef.SIGNTYPE_SEQUENTIAL.equals(attrMap.get("signtype"))
					&& (def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_CUSTOMSIGNTASK)
							|| def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_BOTH))) {
				buttonList.add(new Button(def.getDescription(), def.getName(), def.isSupportScript()));
			}
			if (NodeType.CUSTOMSIGNTASK.equals(this.type) && attrMap.containsKey("signtype")
					&& CustomSignNodeDef.SIGNTYPE_PARALLELAPPROVE.equals(attrMap.get("signtype"))
					&& def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_BOTH)) {
				buttonList.add(new Button(def.getDescription(), def.getName(), def.isSupportScript()));
			}
			if (def.getSupportType().equals(TaskActionHandlerDef.SUPPORT_TYPE_ALL)) {
				buttonList.add(new Button(def.getDescription(), def.getName(), def.isSupportScript()));
			}
		}

		return buttonList;
	}

	/**
	 * 是否存在自定义按钮
	 * 
	 * @return
	 */
	public boolean isDefaultBtn() {
		if (this.buttons != null)
			return false;
		return true;
	}

}
