package com.hotent.activiti.def.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.XPath;
import org.dom4j.dom.DOMAttribute;
import org.dom4j.dom.DOMCDATA;
import org.dom4j.dom.DOMElement;

import com.hotent.activiti.def.BpmDefUtil;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.natapi.def.DefTransform;


public class EclipseDefTransform implements DefTransform {

	@Override
	public String convert(String id, String name, String designXml) throws Exception{
		return BpmDefUtil.transBpmDef(id, name, designXml);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String converConditionXml(String nodeId, Map<String, String> map, String designXml) {
		//String xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL";
//		InputStream is= EclipseDefTransform.class.getClassLoader().getResourceAsStream("condition.bpmn");
		Document doc=Dom4jUtil.loadXml(designXml);
		Map<String,String> nsMap=new HashMap<String,String>();
		nsMap.put("bpmn2",bpmnNamespace);
		
		XPath xpath=doc.createXPath("//bpmn2:*[@sourceRef='"+ nodeId+"']");
		xpath.setNamespaceURIs(nsMap);
		List<Node> list=xpath.selectNodes(doc);
		for(Node node:list){
			Element el=(Element)node;
			String targetRef=el.attributeValue("targetRef");
			String condition=map.get(targetRef);
			if(StringUtil.isEmpty(targetRef)) continue;
			removeChild(el);
			Namespace namespace=new Namespace("",bpmnNamespace);
			Element conditionEl=new DOMElement("conditionExpression",namespace);
			Namespace namespaceXsi=new Namespace("xsi", xsiNamespace);
			QName qName=new QName("type", namespaceXsi);
			
			Attribute attr= new DOMAttribute(qName, "tFormalExpression");
			conditionEl.add(attr);
			CDATA cdata=new DOMCDATA( condition );
			conditionEl.add(cdata);
			el.add(conditionEl);
			
		}
		return doc.asXML();
	}
	
	@SuppressWarnings("rawtypes")
	private void removeChild(Element el){
		List childs= el.elements();
		Iterator it=childs.iterator();
		while(it.hasNext()){
			el.remove((Node)it.next());
		}
		
	}

	

	

}
