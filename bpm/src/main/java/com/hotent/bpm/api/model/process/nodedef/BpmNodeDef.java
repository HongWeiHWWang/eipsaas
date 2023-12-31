package com.hotent.bpm.api.model.process.nodedef;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.FormInitItem;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.model.form.Form;

/**
 * 描述：流程任务节点 构建组：x5-bpmx-api 作者：csx 邮箱:chensx@jee-soft.cn 日期:2013-11-7-下午2:57:41
 * 版权：广州宏天软件有限公司版权所有
 */
public interface BpmNodeDef extends Serializable
{

	/**
	 * 是否多实例
	 */
	public final static String MULTI = "multi";

	/**
	 * 串行并行
	 */
	public final static String PARALLEL = "parallel";

	/**
	 * 子流程件。
	 */
	public final static String FLOWKEY = "flowKey";

	/**
	 * 流程定义ID
	 */
	public final static String PROCESS_DEF_ID = "defId";

	

	/**
	 * 取得节点的ID
	 * 
	 * @return
	 */
	String getNodeId();

	/**
	 * 取得节点的名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 取得节点的类型
	 * 
	 * @return
	 */
	NodeType getType();

	
	/**
	 * 设置节点的类型
	 * 
	 * @return
	 */
	void setType(NodeType type);
	
	/**
	 * 取得节点的排序
	 * 
	 * @return
	 */
	Integer getOrder();

	/**
	 * 设置节点的排序
	 * 
	 * @return
	 */
	void setOrder(Integer order);

	/**
	 * 取得通知类型。
	 * 
	 * @return String
	 */
	// String getNotifyType();

	/**
	 * 取得当前节点的所有入口节点集合
	 * 
	 * @return
	 */
	List<BpmNodeDef> getIncomeNodes();

	/**
	 * 取得当前节点的所有出口节点集合
	 * 
	 * @return
	 */
	List<BpmNodeDef> getOutcomeNodes();

	/**
	 * 获取跳出的任务节点
	 * 
	 * @return List&lt;BpmNodeDef>
	 */
	List<BpmNodeDef> getOutcomeTaskNodes();

	/**
	 * 取得当前节点的后续任务节点，只包括流程中的任务节点，不包括内部子流程和外部子流程的节点。
	 * 
	 * @param includeSignTask
	 *            是否包含会签节点。
	 * @return List&lt;BpmNodeDef>
	 */
	List<BpmNodeDef> getInnerOutcomeTaskNodes(boolean includeSignTask);

	/**
	 * 取得节点的事件插件
	 * 
	 * @return List&lt;BpmNodePlugin>
	 */
	List<BpmPluginContext> getBpmPluginContexts();



	/**
	 * 获取节点所在的流程定义。
	 * 
	 * @return BpmProcessDef
	 * @exception
	 * @since 1.0.0
	 */
	@JsonIgnore
	BpmProcessDef<?> getBpmProcessDef();

	/**
	 * 获得属性值集合
	 * 
	 * @return List&lt;String>
	 */
	String getAttribute(String name);

	/**
	 * 添加入口的节点定义。
	 * 
	 * @param bpmNodeDef
	 *            void
	 */
	void addIncomeNode(BpmNodeDef bpmNodeDef);

	/**
	 * 添加出口的节点定义。
	 * 
	 * @param bpmNodeDef
	 *            void
	 */
	void addOutcomeNode(BpmNodeDef bpmNodeDef);

	/**
	 * 
	 * 获取上级节点。
	 * 
	 * @return BpmProcessDef
	 */
	BpmNodeDef getParentBpmNodeDef();

	/**
	 * 取得实际的路径。
	 * 
	 * @return String
	 */
	String getRealPath();

	/**
	 * 获取根的流程定义。
	 * 
	 * @return BpmProcessDef
	 */
	@SuppressWarnings("rawtypes")
	BpmProcessDef getRootProcessDef();

	/**
	 * 获取表单定义。
	 * 
	 * @return Form
	 */
	Form getForm();
	
	
	Form getMobileForm();

	/**
	 * 根据插件类名获取插件实例上下文定义。
	 * @param cls
	 * @return BpmPluginContext
	 */
	<T> T getPluginContext(Class<T> cls);

	/**
	 * 设置子流程表单。
	 * 
	 * @param list
	 *            void
	 */
	void setSubFormList(List<Form> list);

	/**
	 * 获取子流程表单配置。
	 * 
	 * @return List&lt;Form>
	 */
	List<Form> getSubFormList();
	
	

	/**
	 * 根据流程定义获取表单配置。
	 * 
	 * @param bpmDefKey
	 * @return Form
	 */
	Form getSubForm(String bpmDefKey,FormType formType);

	/**
	 * 获取节点外向的分支条件表达式。
	 * 
	 * <pre>
	 * 	返回分支条件。
	 *  键为分支节点ID
	 *  值为分支脚本
	 * </pre>
	 * 
	 * @return Map&lt;String,String>
	 */
	Map<String, String> getConditions();

	/**
	 * 设置分支条件。
	 * 
	 * @param conditions
	 *            void
	 */
	void setConditions(Map<String, String> conditions);

	/**
	 * 添加条件
	 * 
	 * @param targetNode
	 *            目标节点
	 * @param condition
	 *            条件 void
	 */
	void addCondition(String targetNode, String condition);

	/**
	 * 获取节点脚本。
	 * 
	 * @return Map&lt;String,String>
	 */
	Map<ScriptType, String> getScripts();

	/**
	 * 添加脚本。
	 * 
	 * @param scriptType
	 *            类型
	 * @param script
	 *            脚本 void
	 */
	void addScript(ScriptType scriptType, String script);

	/**
	 * 取得节点表单初始化数据。
	 * 
	 * @return FormInitItem
	 */
	FormInitItem getFormInitItem();

	/**
	 * 取得节点表单初始化数据。
	 * 
	 * @param parentDefKey
	 *            流程key。
	 * @return FormInitItem
	 */
	FormInitItem getFormInitItemByParentKey(String parentDefKey);

	List<NodeProperties> getNodeProperties();

	void setNodeProperties(List<NodeProperties> nodeProperties);

	void addNodeProperties(NodeProperties prop);

	/**
	 * 取得节点属性信息。
	 * 
	 * @return NodeProperties
	 */
	NodeProperties getLocalProperties();

	/**
	 * 根据父key获取节点属性。
	 * 
	 * @param parentDefKey
	 * @return NodeProperties
	 */
	NodeProperties getPropertiesByParentDefKey(String parentDefKey);

	/**
	 * 获取节点按钮。
	 * 
	 * <pre>
	 * 1.获取节点配置按钮数据。
	 * 2.如果获取不到,则获取节点的默认按钮。
	 * </pre>
	 * 
	 * @return List&lt;Button>
	 */
	List<Button> getButtons();

	/**
	 * 按节点获得按钮类型。
	 * @param isInit	获取是否初始化的按钮。
	 * @return
	 */
	List<Button> getButtonsByType(boolean isInit);

	/**
	 * 是否有自定义按钮
	 */
	boolean isDefaultBtn();
}
