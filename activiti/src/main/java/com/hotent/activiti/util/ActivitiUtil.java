package com.hotent.activiti.util;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;

import com.hotent.base.util.AppUtil;

/**
 * 获取executor工具类。
 * <pre> 
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-12-31-下午4:07:12
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ActivitiUtil {

	/**
	 * 获取流程引擎命令执行对象，用于执行CMD对象。
	 * @return 
	 * CommandExecutor
	 */
	public static CommandExecutor getCommandExecutor(){
		ProcessEngineImpl engine = (ProcessEngineImpl)AppUtil.getBean(ProcessEngine.class) ;
		CommandExecutor cmdExecutor=engine.getProcessEngineConfiguration().getCommandExecutor();
		return cmdExecutor;
	}

}
