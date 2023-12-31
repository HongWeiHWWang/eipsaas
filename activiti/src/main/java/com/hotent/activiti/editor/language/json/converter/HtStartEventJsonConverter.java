package com.hotent.activiti.editor.language.json.converter;

import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.BpmnJsonConverterUtil;
import org.activiti.editor.language.json.converter.StartEventJsonConverter;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.activiti.editor.constants.HtStencilConstants;

public class HtStartEventJsonConverter extends StartEventJsonConverter implements HtStencilConstants{
	
	public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap,
			Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {

		fillJsonTypes(convertersToBpmnMap);
		fillBpmnTypes(convertersToJsonMap);
	}

	public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
		convertersToBpmnMap.put(STENCIL_EVENT_START_DEFAULT, HtStartEventJsonConverter.class);
	}
	
	public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
	    convertersToJsonMap.put(StartEvent.class, HtStartEventJsonConverter.class);
    }
	
	protected String getStencilId(BaseElement baseElement) {
	    return STENCIL_EVENT_START_DEFAULT;
   }
	
   protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
	    StartEvent startEvent = new StartEvent();
	    startEvent.setInitiator(getPropertyValueAsString(PROPERTY_NONE_STARTEVENT_INITIATOR, elementNode));
	    String stencilId = BpmnJsonConverterUtil.getStencilId(elementNode);
	    if (STENCIL_EVENT_START_DEFAULT.equals(stencilId)) {
	      String formKey = getPropertyValueAsString(PROPERTY_FORMKEY, elementNode);
	      if (StringUtils.isNotEmpty(formKey)) {
	        startEvent.setFormKey(formKey);
	      }
    	  convertJsonToFormProperties(elementNode, startEvent);
	    	
	    }
	    return startEvent;
  }
}
