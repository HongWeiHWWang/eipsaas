package com.hotent.bpm.plugin.core.context;

import java.io.IOException;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.constant.LogicType;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.PluginParse;
import com.hotent.bpm.api.plugin.core.context.UserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.def.BpmUserCalcPluginDef;
import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

/**
 * 用户运算逻辑抽象类。
 * <pre> 
 * 所有用户计算类都继承此类。
 * 构建组：x5-bpmx-plugin-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-21-下午10:00:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public abstract class AbstractUserCalcPluginContext  implements UserCalcPluginContext,PluginParse{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5879794029370692154L;
	private BpmPluginDef bpmPluginDef;

	@Override
	public BpmPluginDef getBpmPluginDef() {
		return bpmPluginDef;
	}

	public void setBpmPluginDef(BpmPluginDef bpmPluginDef) {
		this.bpmPluginDef = bpmPluginDef;
	}
	
	/**
	 * 解析插件定义。
	 * @param element
	 * @return BpmPluginDef
	 */
	protected abstract BpmPluginDef parseElement(Element element);

	/**
	 * 在父类设置逻辑类型和抽取类型。
	 */
	public BpmPluginDef parse(Element element) {
		String logicCal=element.getAttribute("logicCal");
		String extract=element.getAttribute("extract");

		BpmUserCalcPluginDef def=(BpmUserCalcPluginDef) parseElement(element);
		
		def.setExtract(ExtractType.fromKey(extract));
		def.setLogicCal(LogicType.fromKey(logicCal));
		
		setBpmPluginDef(def);
		
		return def;

	}
	
	@Override
	public String getType() {
		return StringUtil.lowerFirst(this.getClass().getSimpleName().replaceAll(BpmPluginContext.PLUGINCONTEXT, ""));
	}

	
	
	protected abstract BpmPluginDef parseJson(ObjectNode pluginJson);
	
	
	
	@Override
	public void parse(String pluginDefJson) throws IOException {
		ObjectNode jsonObject=(ObjectNode) JsonUtil.toJsonNode(pluginDefJson);
		AbstractUserCalcPluginDef bpmPluginDef=(AbstractUserCalcPluginDef) parseJson(jsonObject);
		
		String extract=jsonObject.get("extract").asText();
		String logicCal=jsonObject.get("logicCal").asText();
		
		bpmPluginDef.setExtract(ExtractType.fromKey(extract));
		bpmPluginDef.setLogicCal(LogicType.fromKey(logicCal));
		
		setBpmPluginDef(bpmPluginDef);
	}
	
	
	
	

	@Override
	public String getJson() {
		return "";
	}
	
}
