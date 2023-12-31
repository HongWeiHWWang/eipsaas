package com.hotent.activiti.def.impl;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMCDATA;

import com.hotent.activiti.def.BpmDefUtil;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.bpm.natapi.def.DefTransform;



public class FlashDefTransform implements DefTransform {

	@Override
	public String convert(String id, String name, String designXml) throws Exception{
		
		return BpmDefUtil.transFlashBpmDef(id, name, designXml);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String converConditionXml(String nodeId, Map<String, String> map,String xml) {

		Document doc = Dom4jUtil.loadXml(xml);
		Element root = doc.getRootElement();

		Element node = (Element) root.selectSingleNode("//bg:Gateway[@id='"	+ nodeId + "']");
		Element portsEl = node.element("ports");
		List portList = portsEl.elements();

		for (int i = 0; i < portList.size(); i++) {
			Element portEl = (Element) portList.get(i);
			if (portEl.attribute("x") == null && portEl.attribute("y") == null)
				continue;
			String id = portEl.attributeValue("id");
			Element outNode = (Element) root.selectSingleNode("//bg:SequenceFlow[@startPort='" + id+ "']");
			if(outNode==null) continue;
				
			String outPort = outNode.attributeValue("endPort");
			Element tmpNode = (Element) root.selectSingleNode("//ciied:Port[@id='" + outPort + "']");
			Element taskNode = tmpNode.getParent().getParent();
			String taskKey = taskNode.attributeValue("id");

			Element conditionEl = outNode.element("Condition");
			if (conditionEl != null)
				outNode.remove(conditionEl);
			// 有条件的情况，处理条件
			if(!map.containsKey(taskKey)) continue;

			String condition = map.get(taskKey);
			Element elAdd = outNode.addElement("Condition");
			elAdd.addText(new DOMCDATA( condition ).asXML());

		}
		return doc.asXML();
		
		
	}

	
}
