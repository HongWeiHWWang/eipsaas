package com.hotent.bpm.plugin.task.userassign.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmTaskPluginContext;
import com.hotent.bpm.api.plugin.core.context.UserQueryPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.api.plugin.core.runtime.UserQueryPlugin;
import com.hotent.bpm.plugin.core.util.UserAssignRuleParser;
import com.hotent.bpm.plugin.task.userassign.def.UserAssignPluginDef;
import com.hotent.bpm.plugin.task.userassign.plugin.UserAssignPlugin;
import com.jamesmurty.utils.XMLBuilder;

/**
 * 流程审批用户解析插件。
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-25-上午9:19:35
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class UserAssignPluginContext extends  AbstractBpmTaskPluginContext implements UserQueryPluginContext{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7198378219954534416L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getPluginClass() {
		return UserAssignPlugin.class;
	}
	
	@SuppressWarnings("rawtypes")
	public Class<? extends RunTimePlugin> getUserQueryPluginClass() {
		return UserQueryPlugin.class;
	}
		

	public List<EventType> getEventTypes() {
		List<EventType> eventTypes = new ArrayList<EventType>();
		eventTypes.add(EventType.TASK_CREATE_EVENT);
		return eventTypes;
	}

	@Override
	public String getPluginXml() {		
		UserAssignPluginDef def =(UserAssignPluginDef) getBpmPluginDef();
		if(def.getRuleList().size()==0){
			return "";
		}
		try {
			XMLBuilder xmlBuilder = XMLBuilder.create("userAssign")
					.a("xmlns", "http://www.jee-soft.cn/bpm/plugins/task/userAssign");	
			
			UserAssignRuleParser.handXmlBulider(xmlBuilder, def.getRuleList());

			return xmlBuilder.asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	


	/**
	 * 获取插件的JSON数据。
	 * [
	 *{"calcs":[
	 *					{"extractType":"no","groupKeys":"zhu","groupNames":"广州","groupType":"org","logicType":"or","pluginName":"","pluginType":"group"},
	 *					{"extractType":"no","logicType":"or","pluginName":"","script":"return false","pluginType":"hrScript"}
	 *				 ],
	 *				 "condition":"total>1","conditionMode":"1","description":"","groupNo":1,"name":""}
	 *]
	 * @throws IOException 
	 */
	@Override
	public String getJson() throws IOException {
		return getJsonByParentFlowKey(BpmConstants.LOCAL);
	}
	/***
	 *  通过 flowKey 获取指定类型的用户抽取列表
	 * @param FlowKey
	 * @return
	 * @throws IOException 
	 */
	public String getJsonByParentFlowKey(String flowKey) throws IOException{
		if(StringUtil.isEmpty(flowKey))  flowKey = BpmConstants.LOCAL;
		
		List<UserAssignRule> ruleList=((UserAssignPluginDef)this.getBpmPluginDef()).getRuleList();
		if(BeanUtils.isEmpty(ruleList)) return "[]";
		
		List<UserAssignRule> rules = new ArrayList<UserAssignRule>();
		for(UserAssignRule rule : ruleList){
			if(StringUtil.isEmpty(rule.getParentFlowKey()))  rule.setParentFlowKey(BpmConstants.LOCAL); //如果为空，改为local_
			
			if(rule.getParentFlowKey().equals(flowKey))  rules.add(rule);
		}
		if(rules.size() ==0) return "[]";
		
		ArrayNode config=JsonUtil.getMapper().createArrayNode();
		UserAssignRuleParser.handJsonConfig(config, rules);
		
		return JsonUtil.toJson(config);
	}
	

	@Override
	protected BpmPluginDef parseElement(Element element) {
		UserAssignPluginDef userAssignPluginDef=new UserAssignPluginDef();
		List<UserAssignRule> userAssignRules = UserAssignRuleParser.parse(element);
		userAssignPluginDef.setRuleList(userAssignRules);
		return userAssignPluginDef;
	}

	
	/**
	 * 根据JSON 解析插件定义。
	 * @throws Exception 
	 */
	@Override
	protected BpmPluginDef parseJson(String pluginJson) throws Exception {
		
		UserAssignPluginDef def=new UserAssignPluginDef();
		if(StringUtil.isEmpty(pluginJson)) return def;
		ArrayNode ArrayNode=(ArrayNode) JsonUtil.toJsonNode(pluginJson);
		List<UserAssignRule> ruleList=new ArrayList<UserAssignRule>();
		for(JsonNode obj:ArrayNode){
			ObjectNode objNode=(ObjectNode)obj;
			UserAssignRule rule=UserAssignRuleParser.getUserAssignRule(objNode);
			ruleList.add(rule);
		}
		def.setRuleList(ruleList);
		return def;
	}

	@Override
	public String getTitle() {
		return "用户分配插件";
	}
}
