package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.PluginParse;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;


/**
 * 流程插件如果处理节点插件。则传递节点ID。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-11-下午5:10:55
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class PluginsBpmDefXmlHandler extends AbstractBpmDefXmlHandler<List<BpmPluginContext>> {

	@Override
	protected String getXml(String defId, String nodeId,
			List<BpmPluginContext> list) {
		String xml=getByPluginList(list);
		 
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String defXml=def.getBpmnXml();
		
		String xParentPath="//ext:extProcess";
		String xPath="//ext:extProcess/ext:extPlugins";
		
		if(StringUtil.isNotEmpty(nodeId)){
			xParentPath = "//ext:*[@bpmnElement='"+nodeId+"']";
			xPath = xParentPath+"/ext:extPlugins";
		}
		
		String rtnXml= DefXmlHandlerUtil.getXml(defXml, xml, xParentPath, xPath);
		return rtnXml;
		
	}

	
	private String getByPluginList(List<BpmPluginContext> list){
		if(BeanUtils.isEmpty(list)) return "";
		StringBuffer sb=new StringBuffer();
		sb.append("<ext:extPlugins xmlns:ext=\""+BpmConstants.BPM_XMLNS+"\">\n");
		for(BpmPluginContext ctx:list){
			if(!(ctx instanceof PluginParse)) continue;
				
			PluginParse parse=(PluginParse)ctx;
			sb.append(parse.getPluginXml());
			
		}
		
		sb.append("</ext:extPlugins>");
		
		return sb.toString();
		
	}
}
