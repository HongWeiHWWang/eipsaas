package com.hotent.bpm.plugin.usercalc.customQuery.content;

import java.io.IOException;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.customQuery.def.CustomQueryPluginDef;
import com.hotent.bpm.plugin.usercalc.customQuery.runtime.CustomQueryPlugin;

/**
 * 关联查询作为流程审批人的上下文对象
 *
 * @company 广州宏天软件股份有限公司
 * @author zhangxw
 * @email zhangxw@jee-soft.cn
 * @date 2020年3月25日
 */
public class CustomQueryPluginContext extends AbstractUserCalcPluginContext {
	private static final long serialVersionUID = -5261531157333798291L;

	@Override
	public String getDescription() {
		CustomQueryPluginDef def = (CustomQueryPluginDef)this.getBpmPluginDef();
		if(def==null) return "";
		return def.getDescription();
	}

	@Override
	public String getTitle() {
		return "关联查询";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return CustomQueryPlugin.class;
	}
	
	/**
	 * <CustomQuery alias="" name="" />
	 */
	@Override
	public String getPluginXml() {
		BpmPluginDef bpmPluginDef = getBpmPluginDef();
		CustomQueryPluginDef def = (CustomQueryPluginDef)bpmPluginDef;
		if(def==null) return "";
		StringBuffer sb = new StringBuffer();
		sb.append("<customQuery xmlns=\"http://www.jee-soft.cn/bpm/plugins/userCalc/customQuery\" ");
		sb.append("  logicCal=\"" + def.getLogicCal().getKey() + "\" extract=\"" + def.getExtract().getKey()+ "\" alias=\"" + def.getAlias()
				+ "\" description=\"" + def.getDescription()+ "\" valueField=\"" + def.getValueField()+ "\" name=\"" + def.getName() + "\">");
		sb.append("<params>");
		sb.append("<![CDATA[");
		sb.append(def.getParams());
		sb.append("]]>");
		sb.append("</params>");
		
		sb.append("</customQuery>");
		return sb.toString();
	}
	
	@Override
	protected BpmPluginDef parseElement(Element element) {
		CustomQueryPluginDef def = new CustomQueryPluginDef();
		String alias = element.getAttribute("alias");
		String name = element.getAttribute("name");
		String description = element.getAttribute("description");
		String valueField = element.getAttribute("valueField");
		def.setAlias(alias);
		def.setName(name);
		def.setDescription(description);
		def.setValueField(valueField);
		Element paramsEl= XmlUtil.getChildNodeByName(element, "params");
		def.setParams(paramsEl==null?"":paramsEl.getTextContent());
		return def;
	}

	@Override
	protected BpmPluginDef parseJson(ObjectNode pluginJson) throws Exception {
		CustomQueryPluginDef def = new CustomQueryPluginDef();
		String alias = JsonUtil.getString(pluginJson, "alias");
		String name = JsonUtil.getString(pluginJson, "name");
		String description = JsonUtil.getString(pluginJson, "description");
		String valueField = JsonUtil.getString(pluginJson, "valueField");
		def.setAlias(alias);
		def.setName(name);
		def.setDescription(description);
		def.setValueField(valueField);
		if(JsonUtil.isContainsKey(pluginJson,"params")){
			String params = null;
			try {
				if(StringUtil.isNotEmpty(pluginJson.get("params").asText())){
					params = pluginJson.get("params").asText();
				}else{
					params = JsonUtil.toJson(pluginJson.get("params"));
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			def.setParams(params);
		}
		return def;
	}
}
