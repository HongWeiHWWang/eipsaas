package com.hotent.bpm.plugin.task.tasknotify.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.plugin.core.util.UserAssignRuleParser;
import com.hotent.bpm.plugin.task.tasknotify.def.TaskNotifyPluginDef;
import com.hotent.bpm.plugin.task.tasknotify.def.model.NotifyItem;
import com.hotent.bpm.plugin.task.tasknotify.def.model.NotifyVo;
import com.hotent.bpm.plugin.task.userassign.context.EnumTypeProcessor;
import com.jamesmurty.utils.XMLBuilder;
/**
 * 
 * <pre> 
 * 描述：通知插件工具类
 * 构建组：x5-bpmx-plugin
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-5-6-下午5:38:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class NotifyUtil {
	/**
	 * 
	 * 根据notify节点的父节点（如onCreate、onComplete、onEnd等节点）来解析该父节点包含的所有notify节点，
	 * 结果解析为NotifyItem的集合。
	 * @param parentOfNotifyElement
	 * @return 
	 * List<NotifyItem>
	 */
	public static List<NotifyItem> parseNotifyItems(Element parentOfNotifyElement){
		List<NotifyItem> notifyItems = new ArrayList<NotifyItem>();
		NodeList nodeList= parentOfNotifyElement.getChildNodes();
		if(nodeList==null || nodeList.getLength()==0) return notifyItems;
		for(int i = 0; i<nodeList.getLength();i++){
			Object obj = nodeList.item(i);
			if(obj instanceof Element && ((Element) obj).getTagName().equals("notify")){
				Element notifyEl = (Element)obj;				
				
				List<UserAssignRule> userAssignRules = UserAssignRuleParser.parse(notifyEl);
				String msgTypes = notifyEl.getAttribute("msgTypes");				
			
				NotifyItem notifyItem = new NotifyItem();
				notifyItem.setUserAssignRules(userAssignRules);
				notifyItem.setMsgTypes(msgTypes);
					
				
				notifyItems.add(notifyItem);
			}			
		}		
		return notifyItems;
	}
	
	/**
	 * 根据JSON获取notifyvo。
	 * @param jsonAry
	 * @return 
	 * NotifyVo
	 * @throws Exception 
	 */
	public static NotifyVo getNotifyVo(ArrayNode jsonAry) throws Exception{
		NotifyVo vo=new NotifyVo();
		if(jsonAry.size()==0) return vo;
		List<NotifyItem> notifys=new ArrayList<NotifyItem>();
		
		for(Object obj:jsonAry){
			ObjectNode notifyItemJson=(ObjectNode) JsonUtil.toJsonNode(obj);
			NotifyItem notifyItem= getNotifyItemByJson(notifyItemJson);
			notifys.add(notifyItem);
		}
		vo.setNotifyItemList(notifys);
		return vo;
		
	}
	
	/**
	 * 根据json 获取 NotifyItem。
	 * @param obj
	 * @return 
	 * NotifyItem
	 * @throws Exception 
	 */
	public static NotifyItem getNotifyItemByJson(ObjectNode obj) throws Exception{
		NotifyItem notifyItem=new NotifyItem();
		String messageTypes=obj.get("msgTypes").asText();
		ArrayNode rules=(ArrayNode) obj.get("userAssignRules");
		notifyItem.setMsgTypes(messageTypes);
		
		List<UserAssignRule> ruleList=new ArrayList<UserAssignRule>();
		for(Object ruleObj:rules){
			ObjectNode jsonObj= (ObjectNode) JsonUtil.toJsonNode(ruleObj);
			UserAssignRule rule=UserAssignRuleParser.getUserAssignRule(jsonObj);
			ruleList.add(rule);
		}
		notifyItem.setUserAssignRules(ruleList);
		
		return notifyItem;
	}
	
	/**
	 * 构建XMLnotify 部分。
	 * @param completeVo
	 * @param xmlBuilder 
	 * void
	 */
	public static void handXmlBuilder(NotifyVo completeVo,XMLBuilder xmlBuilder){
		List<NotifyItem> notifyItems= completeVo.getNotifyItemList();
		for(NotifyItem item:notifyItems){
			xmlBuilder=xmlBuilder.e("notify").a("xmlns", "http://www.jee-soft.cn/bpm/plugins/task/baseNotify")
			.a("msgTypes", item.getMessageTypes());
			
			List<UserAssignRule> rules= item.getUserAssignRules();
			UserAssignRuleParser.handXmlBulider(xmlBuilder, rules);
			xmlBuilder = xmlBuilder.up();
		}
	}
	
	/**
	 * 获取通知的所有用户规则。
	 * @param listVo
	 * @return 
	 * List&lt;UserAssignRule>
	 */
	public static List<UserAssignRule> getRules(Collection<NotifyVo> listVo){
		List<UserAssignRule> list=new ArrayList<UserAssignRule>();
		for(NotifyVo vo:listVo){
			List<NotifyItem> notifyItems= vo.getNotifyItemList();
			for(NotifyItem item:notifyItems){
				list.addAll(item.getUserAssignRules());
			}
		}
		return list;
	}
	
	
	@SuppressWarnings("deprecation")
	public static void  getJsonConfig(ObjectNode config, Collection<NotifyVo> assignRules) throws IOException{
 		@SuppressWarnings("unused")
		List<UserAssignRule> ruleList= NotifyUtil.getRules(assignRules);
		
		
		config.put(EventType.class.toString(), JsonUtil.toJsonNode(new EnumTypeProcessor<ExtractType>().getTypeName()));
		
		config.put(NotifyItem.class.toString(), JsonUtil.toJson(new String[]{"msgTypes"}));
		config.put(NotifyVo.class.toString(),JsonUtil.toJson(new String[]{"eventType"}));
		
		
		config.put(NotifyVo.class.getName(),"notify");
		config.put(NotifyItem.class.getName(),"msgTypes");
		
		
		TaskNotifyPluginDef def=JsonUtil.toBean(config, TaskNotifyPluginDef.class);
		Map<EventType, NotifyVo> map= def.getNotifyVos();
		Map<EventType, List<NotifyItem>> rtnMap=convertNotifys(map);
		
		config.set(TaskNotifyPluginDef.class.getName(), (ObjectNode) rtnMap);
		
		//UserAssignRuleParser.handJsonConfig(config, ruleList);
		
	}
	
	private static Map<EventType, List<NotifyItem>> convertNotifys(Map<EventType, NotifyVo> map){
		Map<EventType, List<NotifyItem>> rtnMaps=new HashMap<EventType, List<NotifyItem>>();
		Set<EventType> set=map.keySet();
		for(Iterator<EventType> it=set.iterator();it.hasNext();){
			EventType eventType=it.next();
			NotifyVo notifyVo=map.get(eventType);
			rtnMaps.put(eventType, notifyVo.getNotifyItemList());
		}
		return rtnMaps;
	}
	
}
