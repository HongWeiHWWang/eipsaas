package com.hotent.bpm.plugin.execution.message.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.constant.LogicType;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmExecutionPluginContext;
import com.hotent.bpm.api.plugin.core.context.UserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.core.util.UserAssignRuleParser;
import com.hotent.bpm.plugin.execution.message.def.HtmlSetting;
import com.hotent.bpm.plugin.execution.message.def.MessagePluginDef;
import com.hotent.bpm.plugin.execution.message.def.PlainTextSetting;
import com.hotent.bpm.plugin.execution.message.entity.Message;
import com.hotent.bpm.plugin.execution.message.entity.Message.Html;
import com.hotent.bpm.plugin.execution.message.entity.Message.PlainText;
import com.hotent.bpm.plugin.execution.message.plugin.MessagePlugin;
import com.hotent.bpm.plugin.usercalc.cusers.context.CusersPluginContext;
import com.hotent.bpm.plugin.usercalc.cusers.def.CusersPluginDef;
import com.jamesmurty.utils.XMLBuilder;

/**
 * 消息插件。
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-21-下午10:17:47
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class MessagePluginContext extends AbstractBpmExecutionPluginContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7968222573049167392L;

	public List<EventType> getEventTypes() {
		List<EventType> list=new ArrayList<EventType>();
		list.add(EventType.AUTO_TASK_EVENT);
		return list;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends RunTimePlugin> getPluginClass() {
		return MessagePlugin.class;
	}
	
	@SuppressWarnings("unused")
	private static MessagePluginDef getMessageDef(){
		List<UserAssignRule> ruleList=new ArrayList<UserAssignRule>();
		MessagePluginDef def=new MessagePluginDef();
		HtmlSetting htmlSetting=new HtmlSetting();
		
		UserAssignRule rule=new UserAssignRule();
		rule.setCondition("aaa>0");
		rule.setGroupNo(1);
		
		CusersPluginContext ctx=new CusersPluginContext();
		CusersPluginDef cdef=new CusersPluginDef();
		cdef.setAccount("zhangyg");
		cdef.setExtract(ExtractType.EXACT_NOEXACT);
		cdef.setSource("spec");
		cdef.setUserName("zhangyg");
		cdef.setLogicCal(LogicType.OR);
		ctx.setBpmPluginDef(cdef);
		
		List<UserCalcPluginContext> calcPluginContextList=new ArrayList<UserCalcPluginContext>();
		
		calcPluginContextList.add(ctx);
		
		rule.setCalcPluginContextList(calcPluginContextList);
		
		ruleList.add(rule);
		htmlSetting.setRuleList(ruleList);
		
		
		
		PlainTextSetting pSetting=new PlainTextSetting();
		def.setExternalClass("com.hotent.Demo");
		def.setHtmlSetting(htmlSetting);
		def.setPlainTextSetting(pSetting);
		
		return def;
	}
	
//	public static void main(String[] args) {
		
		
//		JsonConfig config=new JsonConfig();
//		
//		MessagePluginDef def= getMessageDef();
//		
//		List<UserAssignRule> ruleList=new ArrayList<UserAssignRule>();
//		if(def.getHtmlSetting().getRuleList()!=null){
//			ruleList.addAll(def.getHtmlSetting().getRuleList());
//		}
//		
//		
//		UserAssignRuleParser.handJsonConfig(config, ruleList);
//		
//		JSON json= JSONSerializer.toJSON(def,config);
//		
//		System.out.println(json.toString());
		
//		MessagePluginContext ctx=new MessagePluginContext();
//		String xml=ctx.getPluginXml();
//		System.out.println(xml);
//	}


	/**
	 * 插件的XML格式。
	 *<pre>
	 *&lt;?xml version="1.0" encoding="UTF-8"?>
	*&lt;message xmlns="http://www.jee-soft.cn/bpm/plugins/execution/message" >
    *&lt;html msgType="">
    *    &lt;userRule xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/base" groupNo="1">
    *        &lt;calcs>&lt;/calcs>
    *    &lt;/userRule>
    *    &lt;subject>&lt;/subject>
    *    &lt;content>&lt;/content>
    *&lt;/html>
    *&lt;plainText msgType="">
    *    &lt;userRule xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/base" groupNo="1">
    *        &lt;calcs>&lt;/calcs>
    *    &lt;/userRule>
    *    &lt;content>&lt;/content>
    *&lt;/plainText>
	*&lt;/message>
	*</pre>
	 */
	@Override
	public String getPluginXml() {
		MessagePluginDef pluginDef=(MessagePluginDef) this.getBpmPluginDef();
//		MessagePluginDef pluginDef=getMessageDef();
		try {
			XMLBuilder xmlBuilder = XMLBuilder.create("message")
					.a("xmlns", "http://www.jee-soft.cn/bpm/plugins/execution/message");	
			
			if(StringUtil.isNotEmpty(pluginDef.getExternalClass()))
				xmlBuilder.a("externalClass", pluginDef.getExternalClass());
			
			HtmlSetting setting=pluginDef.getHtmlSetting();
			xmlBuilder=xmlBuilder.e("html").a("msgType", setting.getMsgType())
			.e("subject").d(setting.getSubject()).up()
			.e("content").d(setting.getContent()).up(); 
			
			UserAssignRuleParser.handXmlBulider(xmlBuilder, setting.getRuleList());
			
			xmlBuilder=xmlBuilder.up();
			
			PlainTextSetting textSetting=pluginDef.getPlainTextSetting();
			xmlBuilder=xmlBuilder.e("plainText").a("msgType", textSetting.getMsgType())
					.e("content").d(textSetting.getContent()).up();
			
			UserAssignRuleParser.handXmlBulider(xmlBuilder, textSetting.getRuleList());

			return xmlBuilder.asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	
	}

	@Override
	public String getJson() throws IOException {
		MessagePluginDef pluginDef=(MessagePluginDef)this.getBpmPluginDef();
		
		ObjectNode config=(ObjectNode) JsonUtil.getMapper().createObjectNode();
		config.put("externalClass", pluginDef.getExternalClass());
		config.put("pluginName", pluginDef.getPluginName());
		if(pluginDef.getHtmlSetting()!=null){
			HtmlSetting htmlSetting = pluginDef.getHtmlSetting();
			ObjectNode setting=(ObjectNode) JsonUtil.toJsonNode(htmlSetting);
			ArrayNode ruleList = JsonUtil.getMapper().createArrayNode();
			if (BeanUtils.isNotEmpty(htmlSetting.getRuleList())) {
				UserAssignRuleParser.handJsonConfig(ruleList, htmlSetting.getRuleList());
			}
			setting.remove("ruleList");
			setting.set("ruleList", ruleList);
			config.set("htmlSetting", setting);
		}
		if(pluginDef.getPlainTextSetting()!=null){
			PlainTextSetting plainSetting=pluginDef.getPlainTextSetting();
			ObjectNode setting=(ObjectNode) JsonUtil.toJsonNode(plainSetting);
			ArrayNode ruleList = JsonUtil.getMapper().createArrayNode();
			if (BeanUtils.isNotEmpty(plainSetting.getRuleList())) {
				UserAssignRuleParser.handJsonConfig(ruleList, plainSetting.getRuleList());
			}
			setting.remove("ruleList");
			setting.set("ruleList", ruleList);
			config.set("plainTextSetting", setting);
		}
	
		config.put("pluginType", this.getType());
		
		return JsonUtil.toJson(config);
	}
	
	/**
	* {"externalClass":"com.hotent.Demo","htmlSetting":{"content":"","msgType":[],
	*"ruleList":[{"calcs":[{"account":"zhangyg","extract":"no","logicCal":"or","pluginName":"",
	*	"source":"spec","userName":"zhangyg","var":"","pluginType":"cusers","description":"zhangyg"}],
	*"condition":"aaa>0","conditionMode":"","description":"","groupNo":1,"name":""}],"subject":""},
	*"plainTextSetting":{"content":"","msgType":"","ruleList":[]},"pluginName":""}
	 * @throws Exception 
	 */
	@Override
	protected BpmPluginDef parseJson(String pluginJson) throws Exception {
		ObjectNode ObjectNode=(ObjectNode) JsonUtil.toJsonNode(pluginJson);

		MessagePluginDef pluginDef=new MessagePluginDef();
		
		String externalClass = JsonUtil.getString(ObjectNode, "externalClass");
		
		handHtmlSetting(pluginDef,ObjectNode);
		
		handPlainTextSetting(pluginDef,ObjectNode);
	
		pluginDef.setExternalClass(externalClass);
		return pluginDef;
	}
	
	/**
	 * 处理消息节点的PlainTextSetting部分。
	 * @param pluginDef
	 * @param ObjectNode 
	 * void
	 * @throws Exception 
	 */
	private void handPlainTextSetting(MessagePluginDef pluginDef,ObjectNode ObjectNode) throws Exception{
		ObjectNode handPlainJsonObject = (ObjectNode) ObjectNode.get("plainTextSetting");
		PlainTextSetting plainTextSetting=new PlainTextSetting();
		handPublicSetting(handPlainJsonObject,plainTextSetting);
		pluginDef.setPlainTextSetting(plainTextSetting);
	}
	
	private void handHtmlSetting(MessagePluginDef pluginDef,ObjectNode ObjectNode) throws Exception{
		ObjectNode htmlJsonObject = (ObjectNode) ObjectNode.get("htmlSetting");
		String subject =htmlJsonObject.get("subject").asText();
		HtmlSetting htmlSetting=new HtmlSetting();
		handPublicSetting(htmlJsonObject,htmlSetting);
		htmlSetting.setSubject(subject);
		pluginDef.setHtmlSetting(htmlSetting);
	}
	
	/**
	 * 处理公共部分的JSON。
	 * @param ObjectNode
	 * @param plainTextSetting 
	 * void
	 * @throws Exception 
	 */
	private void handPublicSetting(ObjectNode ObjectNode,PlainTextSetting plainTextSetting) throws Exception{
		String msgType=JsonUtil.getString(ObjectNode, "msgType");
		if("".equals(msgType)) return; 
		String content=ObjectNode.get("content").asText();
		
		ArrayNode rulesAry=(ArrayNode) ObjectNode.get("ruleList");
		
		List<UserAssignRule> ruleList=getRulesByJsonArray(rulesAry);
		
		plainTextSetting.setContent(content);
		plainTextSetting.setMsgType(msgType);
		plainTextSetting.setRuleList(ruleList);
	}
	
	/**
	 * 根据JSONArray返回用户规则列表。
	 * @param jsonAry
	 * @return  List&lt;UserAssignRule>
	 * @throws Exception 
	 */
	private List<UserAssignRule> getRulesByJsonArray(ArrayNode jsonAry) throws Exception{
		List<UserAssignRule> rules=new ArrayList<UserAssignRule>();
		if(BeanUtils.isEmpty(jsonAry)) return rules;
		
		for(Object obj:jsonAry){
			UserAssignRule rule= UserAssignRuleParser.getUserAssignRule((ObjectNode) obj);
			rules.add(rule);
		}
		return rules;
		
	}

	@Override
	protected BpmPluginDef parseElement(Element element) {
		String xml = XmlUtil.getXML(element);
		MessagePluginDef pluginDef=new MessagePluginDef();
		try {
			Message message = (Message)JAXBUtil.unmarshall(xml,com.hotent.bpm.plugin.execution.message.entity.ObjectFactory.class);
			//外部数据获取类。
			String externalClass=message.getExternalClass();
			 
			if(StringUtil.isNotEmpty(externalClass)){
				pluginDef.setExternalClass(externalClass);
			}
			
			PlainText plainText= message.getPlainText();
			Html html= message.getHtml();
			
			if(plainText!=null){
				PlainTextSetting plain=new PlainTextSetting();
				plain.setMsgType(plainText.getMsgType());
				plain.setContent(plainText.getContent());
				
				Element el= XmlUtil.getChildNodeByName(element, "plainText");
				
				List<UserAssignRule> list=UserAssignRuleParser.parse(el);
				
				plain.setRuleList(list);
				
				pluginDef.setPlainTextSetting(plain);
			}
			
			if(html!=null){
				HtmlSetting htmlSetting=new HtmlSetting();
				htmlSetting.setSubject(html.getSubject());
				htmlSetting.setMsgType(html.getMsgType());
				htmlSetting.setContent(html.getContent());
				
				Element el= XmlUtil.getChildNodeByName(element, "html");
				
				
				List<UserAssignRule> list=UserAssignRuleParser.parse(el);
				
				htmlSetting.setRuleList(list);
				
				pluginDef.setHtmlSetting(htmlSetting);
			}
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pluginDef;
	}

	@Override
	public String getTitle() {
		
		return "消息节点";
	}
	
	

}
