package com.hotent.bpm.plugin.task.test.context;

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
import com.hotent.bpm.plugin.task.test.def.TestPluginDef;
import com.hotent.bpm.plugin.task.test.entity.TestPluginEntity;
import com.hotent.bpm.plugin.task.test.plugin.TestPlugin;

/**
 * 
 * @author co
 *
 */
public class TestPluginContext extends AbstractBpmTaskPluginContext {

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
		return TestPlugin.class;
	}

	
	@Override
	public String getPluginXml() {
		TestPluginDef pluginDef=(TestPluginDef) this.getBpmPluginDef();
		String xml = "";
		try {
			if(BeanUtils.isEmpty(pluginDef.getTestPluginEntity()))return xml;
			xml = JAXBUtil.marshall(pluginDef.getTestPluginEntity(), TestPluginEntity.class);
			xml = xml.replace("encoding=\"utf-8\"", "encoding=\"UTF-8\"");
			xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n", "");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xml;
	}

	@Override
	public String getJson() throws Exception {
		TestPluginDef pluginDef=(TestPluginDef) this.getBpmPluginDef();
		return JsonUtil.toJson(pluginDef.getTestPluginEntity());
	}
	

	@Override
	protected BpmPluginDef parseJson(String pluginJson) throws Exception {
		TestPluginDef def= new TestPluginDef() ;
		TestPluginEntity entity = JsonUtil.toBean(pluginJson, TestPluginEntity.class);
		def.setTestPluginEntity(entity);
		return def;
	}

	@Override
	protected BpmPluginDef parseElement(Element element) {
		String xml = XmlUtil.getXML(element);
		TestPluginDef def= new TestPluginDef() ;
		 try {
			TestPluginEntity test = (TestPluginEntity) JAXBUtil.unmarshall(xml,TestPluginEntity.class);
			def.setTestPluginEntity(test);
			return def;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return def;
	}
	
	
	@Override
	public String getTitle() {
		return "测试消息插件";
	}

}
