/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hotent.activiti.editor.language.json.converter;

import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.EventDefinition;
import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.EndEventJsonConverter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.activiti.editor.constants.HtStencilConstants;

/**
 * @author Tijs Rademakers
 */
public class HtEndEventJsonConverter extends EndEventJsonConverter implements HtStencilConstants {

  public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap,
      Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {

    fillJsonTypes(convertersToBpmnMap);
    fillBpmnTypes(convertersToJsonMap);
  }

  public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
	  convertersToBpmnMap.put(STENCIL_EVENT_END_DEFAULT, HtEndEventJsonConverter.class);
  }

  public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    convertersToJsonMap.put(EndEvent.class, HtEndEventJsonConverter.class);
  }

  protected String getStencilId(BaseElement baseElement) {
    EndEvent endEvent = (EndEvent) baseElement;
    List<EventDefinition> eventDefinitions = endEvent.getEventDefinitions();
    if (eventDefinitions.size() != 1) {
      return STENCIL_EVENT_END_DEFAULT;
    }
    return STENCIL_EVENT_END_DEFAULT;
  }

  protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
    EndEvent endEvent = (EndEvent) baseElement;
    addEventProperties(endEvent, propertiesNode);
  }
}
