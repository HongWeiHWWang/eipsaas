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
package com.hotent.activiti.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;


/**
 * @author Tom Baeyens
 */
public class GetExecutionVariableCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String executionId;
  protected String variableName;
  protected boolean isLocal;

  public GetExecutionVariableCmd(String executionId, String variableName, boolean isLocal) {
    this.executionId = executionId;
    this.variableName = variableName;
    this.isLocal = isLocal;
  }

  public Object execute(CommandContext commandContext) {
    if(executionId == null) {
      throw new ActivitiIllegalArgumentException("executionId is null");
    }
    if(variableName == null) {
      throw new ActivitiIllegalArgumentException("variableName is null");
    }
    
    ExecutionEntity execution = commandContext
      .getExecutionEntityManager()
      .findExecutionById(executionId);
    
    if (execution==null) {
      //throw new ActivitiObjectNotFoundException("execution "+executionId+" doesn't exist", Execution.class);
    	return null;
    }
    
    Object value;
    
    if (isLocal) {
      value = execution.getVariableLocal(variableName);
    } else {
      value = execution.getVariable(variableName);
    }
    
    return value;
  }
}
