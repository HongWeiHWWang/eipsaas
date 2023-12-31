package com.hotent.base.groovy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.hotent.base.util.BeanUtils;

import groovy.lang.GroovyShell;

/**
 * groovy脚本引擎
 * <pre>
 * 
 * 配置方法如下：
 * &lt;bean id="scriptEngine" class="com.hotent.base.core.engine.script.GroovyScriptEngine">
 *		&lt;property name="bindingInterface">
 *			&lt;list>
 *				&lt;value>com.hotent.base.core.engine.script.IScript&lt;/value>
 *				&lt;value>com.hotent.base.core.engine.script.IUserScript&lt;/value>
 *				&lt;value>com.hotent.base.manager.api.Manager&lt;/value>
 *			&lt;/list>
 *		&lt;/property>
 *	&lt;/bean>
 *
 * bindingInterface:表示系统中继承了这些接口的类可以在脚本引擎中进行使用。
 * 
 * 上面的配置，说明：
 * 1.实现了IScript接口的类;
 * 2.实现了IUserScript接口的类;
 * 3.实现了Manager接口的类。
 * 可以在脚本中使用。
 * 
 * </pre>
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月11日
 */
public class GroovyScriptEngine implements BeanPostProcessor {
	private Log logger = LogFactory.getLog(getClass());
	private GroovyBinding binding = new GroovyBinding();
	/** 绑定接口 */
	private List<String> bindingInterface;

	/**
	 * @return the bindingInterface
	 */
	public List<String> getBindingInterface() {
		return bindingInterface;
	}

	/**
	 * @param bindingInterface
	 *            the bindingInterface to set
	 */
	public void setBindingInterface(List<String> bindingInterface) {
		this.bindingInterface = bindingInterface;
	}

	/**
	 * 执行groovy代码无返回。
	 * 
	 * @param script
	 */
	public void execute(String script) {
		executeObject(script, null);
	}

	/**
	 * 执行groovy代码无返回。
	 * 
	 * @param script
	 *            脚本
	 * @param vars
	 *            变量
	 */
	public void execute(String script, Map<String, Object> vars) {
		executeObject(script, vars);
	}

	/**
	 * 执行groovy代码返回布尔型。
	 * 
	 * @param script
	 * @return
	 * @throws Exception
	 */
	public boolean executeBoolean(String script, Map<String, Object> vars) {
		return (Boolean) executeObject(script, vars);
	}

	/**
	 * 执行脚本返回字符串数据。
	 * 
	 * @param script
	 * @return 字符串数据
	 */
	public String executeString(String script, Map<String, Object> vars) {
		return (String) executeObject(script, vars);
	}

	/**
	 * 执行脚本返回整形数据。
	 * 
	 * @param script
	 * @return 整形数据
	 */
	public int executeInt(String script, Map<String, Object> vars) {
		return (Integer) executeObject(script, vars);
	}

	/**
	 * 执行脚本返回浮点型数据。
	 * 
	 * @param script
	 * @return 浮点型数据
	 */
	public float executeFloat(String script, Map<String, Object> vars) {
		return (Float) executeObject(script, vars);
	}

	/**
	 * 执行脚本返回对象数据。
	 * 
	 * @param script
	 * @return 对象数据
	 */
	public Object executeObject(String script, Map<String, Object> vars) {
		//在执行时清除变量。
		binding.clearVariables();
		logger.debug("执行:" + script);
		GroovyShell shell = new GroovyShell(binding);
		this.setParameters(shell, vars);

		script = script.replace("&apos;", "'").replace("&quot;", "\"")
				.replace("&gt;", ">").replace("&lt;", "<")
				.replace("&nuot;", "\n").replace("&amp;", "&");

		Object rtn = shell.evaluate(script);
		return rtn;

	}

	/**
	 * 设置执行参数。
	 * 
	 * @param shell
	 * @param vars
	 */
	private void setParameters(GroovyShell shell, Map<String, Object> vars) {
		if (vars == null) return;
		Set<Map.Entry<String, Object>> set = vars.entrySet();
		for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it
				.hasNext();) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
					.next();
			shell.setVariable(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 给groovy脚本引擎注入对象的引用。
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		List<Class<?>> list=new ArrayList<Class<?>>();
		for (String str : bindingInterface) {
			try{
				Class<?> clazz = Class.forName(str);
				list.add(clazz);
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
				logger.debug(e.getException());
			}
		}
		for(Class<?> clazz:list){
			boolean isImpl = BeanUtils.isInherit(bean.getClass(), clazz);
			if(!isImpl) continue;
			if (isImpl){
				binding.setProperty(beanName, bean);
			}
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

}
