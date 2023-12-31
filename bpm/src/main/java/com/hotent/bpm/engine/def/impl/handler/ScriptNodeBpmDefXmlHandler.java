package com.hotent.bpm.engine.def.impl.handler;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;

/**
 * 脚本节点插件。
 * 参数保存脚本即可。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-26-上午9:49:19
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class ScriptNodeBpmDefXmlHandler extends AbstractBpmDefXmlHandler<String> {

	@Override
	protected String getXml(String defId, String nodeId, String script) {
		String xml=getXml(script);
		
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String defXml=def.getBpmnXml();
		Document doc=Dom4jUtil.loadXml(defXml);
		Element root=doc.getRootElement();
		
		root.addNamespace("sc", "http://www.jee-soft.cn/bpm/plugins/execution/script");
		
		String xPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:extPlugins/sc:scriptNode" ;
		String xParentPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:extPlugins" ;
		
		DefXmlHandlerUtil.handXmlDom(root, xml, xParentPath, xPath);
		
		//root.remove(new Namespace("sc", "http://www.jee-soft.cn/bpm/plugins/execution/script"));
		
		return root.asXML();
		
	}
	
	private String getXml(String script){

		if(StringUtil.isEmpty(script)) return "";
		StringBuffer sb=new StringBuffer();
		
		sb.append("<scriptNode xmlns=\"http://www.jee-soft.cn/bpm/plugins/execution/script\">");
		
		sb.append("<![CDATA[");
      
		sb.append(script);
		
		sb.append("]]>");
		sb.append("</scriptNode>");
		
		return sb.toString();
		
	}

}
