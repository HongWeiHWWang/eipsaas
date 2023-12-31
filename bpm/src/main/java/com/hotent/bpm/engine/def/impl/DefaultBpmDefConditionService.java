package com.hotent.bpm.engine.def.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.DesignerType;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.service.BpmDefConditionService;
import com.hotent.bpm.natapi.def.DefTransform;
import com.hotent.bpm.natapi.def.NatProDefinitionService;
import com.hotent.bpm.persistence.dao.BpmDefDataDao;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.model.BpmDefData;


/**
 * 保存条件实现。
 * @author ray
 *
 */
@Service
public class DefaultBpmDefConditionService implements BpmDefConditionService {
	
	private String bpmnXmlns="http://www.omg.org/spec/BPMN/20100524/MODEL";
	
	@Resource
	private BpmDefinitionManager bpmDefinitionManager;
	
	@Resource
	private BpmDefDataDao bpmDefDataDao; 
	
	@Resource
	NatProDefinitionService natProDefinitionService;
	

	@Override
	public void saveCondition(String defId, String nodeId,
			Map<String, String> map) throws Exception {
		BpmDefinition bpmDef= bpmDefinitionManager.getById(defId);
		
		String designXml=bpmDef.getDefXml();
		String bpmnXml=bpmDef.getBpmnXml();
		String designer=bpmDef.getDesigner();
		
		DefTransform transform=natProDefinitionService.getDefTransform(DesignerType.valueOf(designer));
		
		designXml= transform.converConditionXml(nodeId, map, designXml);
		
		if(DesignerType.WEB.getKey().equals(designer)){
			designXml= transform.converConditionXml(nodeId, map, bpmDef.getDefJson());
		}
		
		
		//编辑bpmnxml
		bpmnXml=converConditionXml(nodeId, map, bpmnXml);
		
		BpmDefData defData=new BpmDefData();
		defData.setId(defId);
		defData.setBpmnXml(bpmnXml);
		defData.setDefXml(designXml);
		defData.setDefJson(bpmDef.getDefJson());
		
		bpmDefinitionManager.updBpmData(defId, defData);
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static String converConditionXml(String nodeId,Map<String,String> map,String bpmnXml){
		Document newDoc = Dom4jUtil.loadXml(bpmnXml);
		Map<String,String> nsMap=new HashMap<String,String>();
		nsMap.put("bpmn2",DefTransform.bpmnNamespace);
		
		XPath xpath=newDoc.createXPath("//bpmn2:*[@sourceRef='"+ nodeId+"']");
		xpath.setNamespaceURIs(nsMap);
		
		List<Node> list=xpath.selectNodes(newDoc);
		
		for(Node node:list){
			Element el=(Element)node;
			String targetRef=el.attributeValue("targetRef");
			String condition=map.get(targetRef);
			if(StringUtil.isEmpty(targetRef)) continue;
			removeChild(el);
			Namespace namespace=new Namespace("",DefTransform.bpmnNamespace);
			Element conditionEl=new DOMElement("conditionExpression",namespace);
			Namespace namespaceXsi=new Namespace("xsi", DefTransform.xsiNamespace);
			QName qName=new QName("type", namespaceXsi);
			
			Attribute attr= new DOMAttribute(qName, "tFormalExpression");
			conditionEl.add(attr);
			CDATA cdata=new DOMCDATA( condition );
			conditionEl.add(cdata);
			el.add(conditionEl);
			
		}
		return newDoc.asXML();
		
	}
	
	@SuppressWarnings("rawtypes")
	private static void removeChild(Element el){
		List childs= el.elements();
		Iterator it=childs.iterator();
		while(it.hasNext()){
			el.remove((Node)it.next());
		}
		
	}
	
	

	
	

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getDecisionConditions(String defId, String nodeId) {
		BpmDefinition bpmDef= bpmDefinitionManager.getById(defId);
		String defXml=bpmDef.getBpmnXml();
		
		Map<String, String> map = new HashMap<String, String>();
		
		Document doc = Dom4jUtil.loadXml(defXml);
		
		Map<String,String> nsMap=new HashMap<String,String>();
		nsMap.put("bpmn2",bpmnXmlns);
		
		XPath xpath=doc.createXPath("//bpmn2:sequenceFlow[@sourceRef='"+ nodeId + "']");
		xpath.setNamespaceURIs(nsMap);

		// 添加分支流向
		List<Element> nodes = xpath.selectNodes(doc);
		for (Element el : nodes) {
			String id = el.attributeValue("targetRef");
			String condition = "";
			Element conditionNode = el.element("conditionExpression");
			if (conditionNode != null) {
				condition = conditionNode.getText().trim();
				//condition = StringUtil.trimPrefix(condition, "${");
				//condition = StringUtil.trimSuffix(condition, "}");
			}
			map.put(id, condition);
		}
		return map;
	}


}
