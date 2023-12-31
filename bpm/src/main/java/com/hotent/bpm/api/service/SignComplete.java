package com.hotent.bpm.api.service;

import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

/**
 * 会签完成接口。
 * <pre> 
 *  系统中提供默认的实现，用户可以自己实现自己的会签完成处理。
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-1-上午10:37:13
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface SignComplete {
	
	/**
	 * 判断会签流程是否可以结束。
	 * @param bpmDelegateExecution
	 * @return
	 * @throws Exception 
	 */
	boolean isComplete(BpmDelegateExecution bpmDelegateExecution) throws Exception;
}
