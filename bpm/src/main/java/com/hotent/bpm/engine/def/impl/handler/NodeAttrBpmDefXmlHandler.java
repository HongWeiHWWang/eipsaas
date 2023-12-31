package com.hotent.bpm.engine.def.impl.handler;

import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;

/**
 * 节点属性的配置。
 * <pre>
 * 目前包括：
 * notifyType：通知类型
 * allowExecutorEmpty：允许执行人为空
 * skipExecutorEmpty：执行人为空时跳过
 * 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-2-下午6:04:59
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class NodeAttrBpmDefXmlHandler extends AbstractBpmDefXmlHandler<Map<String,String>>  {

	/**
	 * map的键值：
	 * notifyType：通知类型
	 * allowExecutorEmpty：允许执行人为空
	 * skipExecutorEmpty：执行人为空时跳过
	 */
	@Override
	protected String getXml(String defId, String nodeId, Map<String, String> attrMap) {
		
		BpmDefinition bpmDef= bpmDefinitionManager.getById(defId);
		String defXml=bpmDef.getBpmnXml();
		Document doc=Dom4jUtil.loadXml(defXml);
		Element root=doc.getRootElement();
	
		String xPath="//ext:*[@bpmnElement='"+nodeId+"']";
		Element nodeEl= (Element) root.selectSingleNode(xPath);
		
		String notifyType=attrMap.get("notifyType");
		String allowExecutorEmpty=attrMap.get("allowExecutorEmpty");
		String skipExecutorEmpty=attrMap.get("skipExecutorEmpty");
		if(StringUtil.isNotEmpty(notifyType)){
			nodeEl.addAttribute("notifyType", notifyType);
		}
		if(StringUtil.isNotEmpty(allowExecutorEmpty)){
			nodeEl.addAttribute("allowExecutorEmpty", allowExecutorEmpty);
		}
		if(StringUtil.isNotEmpty(skipExecutorEmpty)){
			nodeEl.addAttribute("skipExecutorEmpty", skipExecutorEmpty);
		}
		
		String choiceExcutor=attrMap.get("choiceExcutor");
		if(StringUtil.isNotEmpty(choiceExcutor)){
			nodeEl.addAttribute("choiceExcutor", choiceExcutor);
		}
		String allowEditUrgentState=attrMap.get("allowEditUrgentState");
		if(StringUtil.isNotEmpty(allowEditUrgentState)){
			nodeEl.addAttribute("allowEditUrgentState", allowEditUrgentState);
		}
		
		String allowSmsApproval=attrMap.get("allowSmsApproval");
		if(StringUtil.isNotEmpty(allowSmsApproval)){
			nodeEl.addAttribute("allowSmsApproval", allowSmsApproval);
		}
		
		String rtnXml=doc.asXML();
		return rtnXml;
	}
	
	

}
