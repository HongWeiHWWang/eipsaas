package com.hotent.bpm.util;

import java.lang.reflect.Method;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;

/**
 * Handler 处理工具类。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-9-4-下午2:48:54
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class HandlerUtil {
	
	/**
	 * 验证前置和后置处理器是否有效。
	 * @param handler	
	 * @return int
	 * 0 有效，-1，格式不对，-2 没有找到service类，-3没有找到对应的方法，-4，未知的错误。
	 */
	@SuppressWarnings("unused")
	public static int isHandlerValid(String handler) {

		if (handler.indexOf(".") == -1) return -1;
		String[] aryHandler = handler.split("[.]");
		if (aryHandler.length != 2)	return -1;
		String beanId = aryHandler[0];
		String method = aryHandler[1];
		Object serviceBean = null;
		try {
			serviceBean = AppUtil.getBean(beanId);
		} catch (Exception ex) {
			return -2;
		}
		if (serviceBean == null) return -2;

		try {
			Method invokeMethod = serviceBean.getClass().getDeclaredMethod(
					method, new Class[] { ActionCmd.class });
			return 0;
		} catch (NoSuchMethodException e) {
			return -3;
		} catch (Exception e) {
			return -4;
		}
	}
	
	/**
	 * 调用处理器。
	 * @param processCmd
	 * @param handler
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws Exception 
	 * void
	 */
	public static void invokeHandler(ActionCmd processCmd, String handler) throws Exception  {
		
		if(StringUtil.isEmpty(handler)) return;
		String[] aryHandler = handler.split("[.]");
		if(aryHandler==null || aryHandler.length!=2) return;
		String beanId = aryHandler[0];
		String method = aryHandler[1];
		// 触发该Bean下的业务方法
		Object serviceBean = AppUtil.getBean(beanId);
		
		if(serviceBean==null) return;
		
		Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method, new Class[] { ActionCmd.class });
		invokeMethod.invoke(serviceBean, processCmd);
	}

}
