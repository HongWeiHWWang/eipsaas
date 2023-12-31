package com.hotent.activiti.def.impl;

import java.util.Map;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotent.activiti.editor.language.json.converter.HtBpmnJsonConverter;
import com.hotent.bpm.natapi.def.DefTransform;

public class WebDefTransform implements DefTransform {

	@Override
	public String convert(String id, String name, String designerXml) {
		String bpmnXml = "";
		try {
			JsonNode modelNode =  new ObjectMapper().readTree(designerXml);
			BpmnModel bpmnModel = new HtBpmnJsonConverter().convertToBpmnModel(modelNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();  
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
			bpmnXml = new String(bpmnBytes,"utf-8");
		} catch (Exception e) {
		}
		return bpmnXml;
	}

	@Override
	public String converConditionXml(String nodeId, Map<String, String> map,
			String xml) {
		return "";
	}
}