package com.hotent.bpm.plugin.task.reminders.context;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.w3c.dom.Element;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmTaskPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.task.reminders.def.RemindersPluginDef;
import com.hotent.bpm.plugin.task.reminders.entity.ObjectFactory;
import com.hotent.bpm.plugin.task.reminders.entity.Reminders;
import com.hotent.bpm.plugin.task.reminders.plugin.RemindersPlugin;

/**
 * 脚本节点。
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：miaojf
 * 邮箱:miaojf@jee-soft.cn
 * 日期:2016-7-26
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class RemindersPluginContext extends AbstractBpmTaskPluginContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 269958940565457305L;



	public List<EventType> getEventTypes() {
		List<EventType> list=new ArrayList<EventType>();
		list.add(EventType.TASK_CREATE_EVENT);
		list.add(EventType.TASK_COMPLETE_EVENT);
		return list;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends RunTimePlugin> getPluginClass() {
		return RemindersPlugin.class;
	}

	
	@Override
	public String getPluginXml() {
		RemindersPluginDef pluginDef=(RemindersPluginDef) this.getBpmPluginDef();
		String xml = "";
		try {
			if(BeanUtils.isEmpty(pluginDef.getReminderList()))return xml;
			xml = JAXBUtil.marshall(RemindersPluginDef.getReminderExt(pluginDef), ObjectFactory.class);
			xml = xml.replace("encoding=\"utf-8\"", "encoding=\"UTF-8\"");
			xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n", "");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xml;
	}

	@Override
	public String getJson() throws Exception {
		RemindersPluginDef pluginDef=(RemindersPluginDef) this.getBpmPluginDef();
		return JsonUtil.toJson(pluginDef);
	}
	
	

	@Override
	protected BpmPluginDef parseJson(String pluginJson) throws Exception {
		RemindersPluginDef def = JsonUtil.toBean(pluginJson, RemindersPluginDef.class);
		return def;
	}

	@Override
	protected BpmPluginDef parseElement(Element element) {
		String xml = XmlUtil.getXML(element);
		RemindersPluginDef def ;
		 try {
			Reminders reminders = (Reminders) JAXBUtil.unmarshall(xml,ObjectFactory.class);
			def =RemindersPluginDef.getReminders(reminders);
			return def;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@Override
	public String getTitle() {
		return "任务催办";
	}
	
	@Override
	public int getOrder() {
		return 1;
	}

}
