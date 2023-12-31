package com.hotent.bpm.api.model.delegate;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
/**
 * <pre> 
 * 描述：流程变量
 * 构建组：x5-bpmx-native-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-12-17-下午6:35:41
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmVariable {

	Map<String, Object> getVariables();

	Map<String, Object> getVariablesLocal();

	Object getVariable(String variableName);

	Object getVariableLocal(String variableName);

	Set<String> getVariableNames();

	Set<String> getVariableNamesLocal();

	void setVariable(String variableName, Object value);

	Object setVariableLocal(String variableName, Object value);

	void setVariables(Map<String, ? extends Object> variables);

	void setVariablesLocal(Map<String, ? extends Object> variables);

	boolean hasVariables();

	boolean hasVariablesLocal();

	boolean hasVariable(String variableName);

	boolean hasVariableLocal(String variableName);

	void createVariableLocal(String variableName, Object value);

	void removeVariable(String variableName);

	void removeVariableLocal(String variableName);

	void removeVariables(Collection<String> variableNames);

	void removeVariablesLocal(Collection<String> variableNames);

	void removeVariables();

	void removeVariablesLocal();
}
