package com.hotent.bpm.plugin.execution.script.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmExecutionPluginContext;
import com.hotent.bpm.api.plugin.core.context.PluginContext;
import com.hotent.bpm.api.plugin.core.context.PluginParse;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.execution.message.def.MessagePluginDef;
import com.hotent.bpm.plugin.execution.script.def.ScriptNodePluginDef;
import com.hotent.bpm.plugin.execution.script.plugin.ScriptNodePlugin;
import com.hotent.bpm.plugin.usercalc.script.def.ScriptPluginDef;
import com.jamesmurty.utils.XMLBuilder;

/**
 * 脚本节点。
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-24-下午2:55:44
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ScriptNodePluginContext extends AbstractBpmExecutionPluginContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5958682303600423597L;

	public List<EventType> getEventTypes() {
		List<EventType> list=new ArrayList<EventType>();
		list.add(EventType.AUTO_TASK_EVENT);
		list.add(EventType.START_POST_EVENT);
		list.add(EventType.END_POST_EVENT);
		return list;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends RunTimePlugin> getPluginClass() {
		return ScriptNodePlugin.class;
	}

	
	@Override
	public String getPluginXml() {
		
		ScriptNodePluginDef pluginDef=(ScriptNodePluginDef) this.getBpmPluginDef();
		try {
			XMLBuilder xmlBuilder = XMLBuilder.create("scriptNode")
					.a("xmlns", "http://www.jee-soft.cn/bpm/plugins/execution/scriptNode");	
			
			xmlBuilder.cdata(pluginDef.getScript());

			return xmlBuilder.asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String getJson() throws IOException {
		ScriptNodePluginDef  pluginDef=(ScriptNodePluginDef)this.getBpmPluginDef();
		ObjectNode config=(ObjectNode) JsonUtil.toJsonNode(pluginDef);
		config.put("pluginType", this.getType());

		return JsonUtil.toJson(config);
	}
	
	

	@Override
	protected BpmPluginDef parseJson(String pluginJson) throws IOException {
		ObjectNode jsonObject=(ObjectNode) JsonUtil.toJsonNode(pluginJson);
		ScriptNodePluginDef def=new ScriptNodePluginDef();
		String script=jsonObject.get("script").asText();
		def.setScript(script);
		
		return def;
	}

	@Override
	protected BpmPluginDef parseElement(Element element) {
		ScriptNodePluginDef def=new ScriptNodePluginDef();
		String script=element.getTextContent();
		def.setScript(script);
		return def;
	}
	
//	public static void main(String[] args) throws TransformerException, ParserConfigurationException, FactoryConfigurationError {
//		ScriptNodePluginDef def=new ScriptNodePluginDef();
//		def.setScript("aaaa");
//		ScriptNodePluginContext context=new ScriptNodePluginContext();
//		context.setBpmPluginDef(def);
//		System.out.println(context.getJson());
//		
//		XMLBuilder xmlBuilder = XMLBuilder.create("scriptNode")
//				.a("xmlns", "http://www.jee-soft.cn/bpm/plugins/execution/scriptNode");	
//		
//		xmlBuilder.cdata(def.getScript());
//		
//		System.out.println(xmlBuilder.asString());
//	}

	@Override
	public String getTitle() {
		return "脚本";
	}

}
