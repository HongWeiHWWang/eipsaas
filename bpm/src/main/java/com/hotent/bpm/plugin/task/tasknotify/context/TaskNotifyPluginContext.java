package com.hotent.bpm.plugin.task.tasknotify.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.constant.LogicType;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmTaskPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.task.tasknotify.def.TaskNotifyPluginDef;
import com.hotent.bpm.plugin.task.tasknotify.def.model.NotifyItem;
import com.hotent.bpm.plugin.task.tasknotify.def.model.NotifyVo;
import com.hotent.bpm.plugin.task.tasknotify.entity.OnComplete;
import com.hotent.bpm.plugin.task.tasknotify.entity.OnCreate;
import com.hotent.bpm.plugin.task.tasknotify.entity.TaskNotify;
import com.hotent.bpm.plugin.task.tasknotify.plugin.TaskNotifyPlugin;
import com.hotent.bpm.plugin.task.tasknotify.util.NotifyUtil;
import com.hotent.bpm.plugin.usercalc.cusers.context.CusersPluginContext;
import com.hotent.bpm.plugin.usercalc.cusers.def.CusersPluginDef;
import com.jamesmurty.utils.XMLBuilder;


public class TaskNotifyPluginContext extends AbstractBpmTaskPluginContext {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6584297241598862949L;


	@SuppressWarnings("rawtypes")
	public Class<? extends RunTimePlugin> getPluginClass() {
		return TaskNotifyPlugin.class;
	}		
	

	public List<EventType> getEventTypes() {
		List<EventType> eventTypes = new ArrayList<EventType>();
		eventTypes.add(EventType.TASK_POST_CREATE_EVENT);
		eventTypes.add(EventType.TASK_COMPLETE_EVENT);
		return eventTypes;
	}

	private NotifyVo convert(OnCreate onCreate,Element pluginEl){
		NotifyVo notifyVo = new NotifyVo();
		notifyVo.setEventType(EventType.TASK_POST_CREATE_EVENT);					
		Element onCreateEl = XmlUtil.getChildNodeByName(pluginEl, "onCreate");
		List<NotifyItem> notifyItems = NotifyUtil.parseNotifyItems(onCreateEl);
		notifyVo.setNotifyItemList(notifyItems);
		
		return notifyVo;
	}
	
	private NotifyVo convert(OnComplete onComplete,Element pluginEl){
		NotifyVo notifyVo = new NotifyVo();
		notifyVo.setEventType(EventType.TASK_COMPLETE_EVENT);
		Element onCompleteEl = XmlUtil.getChildNodeByName(pluginEl, "onComplete");
		List<NotifyItem> notifyItems = NotifyUtil.parseNotifyItems(onCompleteEl);
		notifyVo.setNotifyItemList(notifyItems);
		return notifyVo;
	}

	/**
	 * 任务通知。
	 * <pre>
	 *&lt;taskNotify xmlns="http://www.jee-soft.cn/bpm/plugins/task/taskNotify">
     *&lt;onCreate>
     *   &lt;notify xmlns="http://www.jee-soft.cn/bpm/plugins/task/baseNotify" msgTypes=""  >
     *       &lt;userRule xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/base" groupNo="">
     *           &lt;calcs>&lt;/calcs>
     *       &lt;/userRule>            
     *   &lt;/notify>
     *&lt;/onCreate>
     *&lt;onComplete>
     *   &lt;notify xmlns="http://www.jee-soft.cn/bpm/plugins/task/baseNotify" msgTypes=""></notify>
     *&lt;/onComplete>
	 *&lt;/taskNotify>
	 *</pre>
	 */
	@Override
	public String getPluginXml() {
		
		TaskNotifyPluginDef def=(TaskNotifyPluginDef) this.getBpmPluginDef();
		try {
			XMLBuilder xmlBuilder = XMLBuilder.create("taskNotify")
					.a("xmlns", "http://www.jee-soft.cn/bpm/plugins/task/taskNotify");
			
			xmlBuilder= xmlBuilder.e("onCreate");
			
			NotifyVo createVo=def.getNotifyVos().get(EventType.TASK_POST_CREATE_EVENT);
			if(createVo!=null){
				NotifyUtil.handXmlBuilder(createVo,xmlBuilder);
			}
			
			xmlBuilder=xmlBuilder.up();
			xmlBuilder=xmlBuilder.e("onComplete");
			
			NotifyVo completeVo=def.getNotifyVos().get(EventType.TASK_COMPLETE_EVENT);
			if(completeVo!=null){
				NotifyUtil.handXmlBuilder(completeVo,xmlBuilder);
				
			}
			return xmlBuilder.asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	public static void main(String[] args) {
		TaskNotifyPluginContext ctx=new TaskNotifyPluginContext();
		TaskNotifyPluginDef def=new TaskNotifyPluginDef();
		ctx.setBpmPluginDef(def);
		
		Map<EventType, NotifyVo> map= def.getNotifyVos();
		
		NotifyVo createVo=new NotifyVo();
		NotifyVo completeVo=new NotifyVo();
		
		createVo.setEventType(EventType.TASK_POST_CREATE_EVENT);
		NotifyItem notifyItem=new NotifyItem();
		List<NotifyItem> itemList=new ArrayList<NotifyItem>();
		itemList.add(notifyItem);
		
		List<UserAssignRule> assignRules=new ArrayList<UserAssignRule>();
		
		UserAssignRule rule=new UserAssignRule();
		rule.setCondition("aaa>0");
		rule.setGroupNo(1);
		
		CusersPluginContext cuserctx=new CusersPluginContext();
		CusersPluginDef cdef=new CusersPluginDef();
		cdef.setAccount("zhangyg");
		cdef.setExtract(ExtractType.EXACT_NOEXACT);
		cdef.setSource("spec");
		cdef.setUserName("zhangyg");
		cdef.setLogicCal(LogicType.OR);
		cuserctx.setBpmPluginDef(cdef);
		
		rule.getCalcPluginContextList().add(cuserctx);
		
		assignRules.add(rule);
	//	assignRules.add(rule);
		
		
		notifyItem.setMsgTypes("sms,mail");
		notifyItem.setUserAssignRules(assignRules);
		
		createVo.setNotifyItemList(itemList);
		
		completeVo.setEventType(EventType.TASK_COMPLETE_EVENT);
		//completeVo.setNotifyItemList(itemList);
	
		map.put(EventType.TASK_POST_CREATE_EVENT, createVo);
		map.put(EventType.TASK_COMPLETE_EVENT, completeVo);
		
		//NotifyVo notifyVo=map.get(EventType.TASK_POST_CREATE_EVENT);

		
		//System.err.println(xml);
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getJson() throws IOException {
		TaskNotifyPluginDef def=(TaskNotifyPluginDef) this.getBpmPluginDef();
		
		Collection<NotifyVo> assignRules=def.getNotifyVos().values();
		
		ObjectNode config=JsonUtil.getMapper().createObjectNode();
		
		NotifyUtil.getJsonConfig(config,assignRules);
		
		ObjectNode json= (ObjectNode) config.putAll((ObjectNode)JsonUtil.toJsonNode(def));
		
		return json.toString();
	}
	
	

	/**
	 * 传入的JSON格式。
	 * <pre>
	 * {"postTaskCreate":
	 *	[{"messageTypes":"sms,mail","rules":
	 *		[
	 *			{"calcs":
	 *				[{"account":"zhangyg","extract":"no","logicCal":"or","pluginName":"","source":"spec","userName":"zhangyg","var":"","pluginType":"cusers","description":"zhangyg"}]
	 *				,"condition":"aaa>0","conditionMode":"","description":"","groupNo":1,"name":""}
	 *		]
	 *	}]
	 *,
	 *"taskComplete":[]
	 *}
	 *</pre>
	 * @throws Exception 
	 */
	@Override
	protected BpmPluginDef parseJson(String pluginJson) throws Exception {
		TaskNotifyPluginDef notifyPluginDef = new TaskNotifyPluginDef();
		
		ObjectNode jsonObject=JsonUtil.getMapper().createObjectNode();
		ArrayNode createJson=(ArrayNode) jsonObject.get(EventType.TASK_POST_CREATE_EVENT.getKey());
		ArrayNode completeJson=(ArrayNode) jsonObject.get(EventType.TASK_COMPLETE_EVENT.getKey());
		
		NotifyVo createVo= NotifyUtil . getNotifyVo(createJson);
		NotifyVo completeVo= NotifyUtil . getNotifyVo(completeJson);
		
		notifyPluginDef.addNotifyVo(EventType.TASK_POST_CREATE_EVENT, createVo);
		notifyPluginDef.addNotifyVo(EventType.TASK_COMPLETE_EVENT, completeVo);
		
		return notifyPluginDef;
	}
	
	

	@Override
	protected BpmPluginDef parseElement(Element element) {
		String xml = XmlUtil.getXML(element);
		TaskNotifyPluginDef notifyPluginDef = new TaskNotifyPluginDef();
		try {
			TaskNotify taskNotify = (TaskNotify)JAXBUtil.unmarshall(xml,com.hotent.bpm.plugin.task.tasknotify.entity.ObjectFactory.class);
			List<NotifyVo> notifyVoList = new ArrayList<NotifyVo>();
			OnCreate onCreate=taskNotify.getOnCreate();
			if(onCreate!=null){
				NotifyVo notityVoOnCreate = convert(onCreate,element);									
				notifyVoList.add(notityVoOnCreate);
				notifyPluginDef.getNotifyVos().put(EventType.TASK_POST_CREATE_EVENT, notityVoOnCreate);
			}			
			OnComplete onComplete = taskNotify.getOnComplete();
			if(onComplete!=null){
				NotifyVo notifyVoOnComplete = convert(onComplete,element);
				notifyVoList.add(notifyVoOnComplete);		
				notifyPluginDef.getNotifyVos().put(EventType.TASK_COMPLETE_EVENT, notifyVoOnComplete);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return notifyPluginDef;
	}


	@Override
	public String getTitle() {
		return "任务通知操送";
	}
}
