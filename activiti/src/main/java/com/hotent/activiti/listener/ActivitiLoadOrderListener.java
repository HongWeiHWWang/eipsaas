package com.hotent.activiti.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.hotent.base.util.StringUtil;

/**
 * 监听Activiti包加载顺序的监听器
 * <p>
 * 在Activiti的包中覆盖了activiti 5框架中的一些class文件，这些class的加载顺序必须优先加载，否则流程的部分功能会出现异常，这里添加一个监听器判断这些class的加载顺序是否按照预期。
 * </p>
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年7月6日
 */
@Component
public class ActivitiLoadOrderListener implements ApplicationListener<ApplicationReadyEvent>{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Class<?> clazz;
		try {
			clazz = Class.forName("org.activiti.bpmn.converter.BpmnXMLConverter");
			String jarFilePath = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
			if(StringUtil.isNotEmpty(jarFilePath) && jarFilePath.indexOf("activiti-bpmn-converter") > -1) {
				logger.error("activiti-bpmn-converter包在activiti包之前加载了，流程功能会出现异常，系统即将停止...");
				System.exit(0);
			}
		} catch (ClassNotFoundException e) {}
	}
}
