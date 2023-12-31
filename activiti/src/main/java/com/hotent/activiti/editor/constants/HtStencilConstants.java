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
package com.hotent.activiti.editor.constants;

/**
 * @author Tijs Rademakers
 */
public interface HtStencilConstants {

  // stencil items
  final String STENCIL_EVENT_START_DEFAULT = "StartEvent";

  final String STENCIL_EVENT_END_DEFAULT = "EndEvent";

  final String STENCIL_TASK_SIGN = "SignTask";
  
  final String STENCIL_TASK_CUSTOME_SIGN = "CustomSignTask";
  
  final String STENCIL_TASK_MESSAGE = "MessageTask";
  final String STENCIL_TASK_STARTSUBTASK = "StartSubFlowTask";
  
  final String STENCIL_XORGATEWAY_EVENT = "XORGateway";
  final String STENCIL_ANDGATEWAY_EVENT = "ANDGateway";
  final String STENCIL_ORGATEWAY_EVENT = "ORGateway";
  // 会签类型
  final String PROPERTY_SIGN_TYPE = "signtype";
  
}
