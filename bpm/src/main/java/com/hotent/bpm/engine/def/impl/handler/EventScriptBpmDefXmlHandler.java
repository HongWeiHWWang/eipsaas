package com.hotent.bpm.engine.def.impl.handler;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.EventScript;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.hotent.base.util.Dom4jUtil;

/**
 * 事件脚本维护。
 * <pre>
 * 传入事件脚本对象。
 *  1.判断节点配置下是否有脚本配置。
 *  2.如果没有则添加一个scripts元素。
 *  3.获取脚本的xml添加到scripts下。
 *  &lt;ext:baseNode name="" nodeType="common" bpmnElement="">
 *  	&lt;ext:scripts>
 *      	&lt;ext:script scriptType="create">
 *          	&lt;ext:content>&lt;/ext:content>
 *          &lt;/ext:script>
 *      &lt;/ext:scripts>
 *  &lt;/ext:baseNode>
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-17-上午9:39:28
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class EventScriptBpmDefXmlHandler extends AbstractBpmDefXmlHandler<EventScript>{

	@Override
	protected String getXml(String defId, String nodeId,
			EventScript eventScript) {
		String eventType=eventScript.getScriptType().getKey();
		
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String defXml=def.getBpmnXml();
		
		String xParentPath="//ext:*[@bpmnElement='"+nodeId+"']";
		String xPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:scripts";
		String xScriptPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:scripts/ext:script[@scriptType='"+eventType+"']";
		Document doc=Dom4jUtil.loadXml(defXml);
		Element root=doc.getRootElement();
		
		String scriptsXml="<ext:scripts xmlns:ext=\""+BpmConstants.BPM_XMLNS+"\"></ext:scripts>";
		//没有scripts节点则直接添加一个。
		Element parentEl=(Element) root.selectSingleNode(xParentPath);
		Element scriptsEl=(Element) root.selectSingleNode(xPath);
		if(scriptsEl==null){
			Document scriptDom= Dom4jUtil.loadXml(scriptsXml);
			parentEl.add(scriptDom.getRootElement());
		}
		//构建脚本。
		String scriptXML=getScriptXml(eventScript);
		
		DefXmlHandlerUtil.handXmlDom(root, scriptXML, xPath, xScriptPath);
		
		return root.asXML();
		
	}
	
	/**
	 * 根据事件取得脚本
	 * @param eventScript
	 * @return  String
	 */
	private String getScriptXml(EventScript eventScript){
		String script=eventScript.getContent();
		if(StringUtil.isEmpty(script)) return "";
		
		String scriptXml="<ext:script xmlns:ext=\""+BpmConstants.BPM_XMLNS+"\" scriptType=\""+
		eventScript.getScriptType().getKey()+"\"><ext:content><![CDATA["+script+"]]></ext:content></ext:script>";
		
		return scriptXml;
	}
	

	
}
