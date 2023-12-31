package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmCallLog;

/**
 * 
 * <pre> 
 * 描述：sys_call_log 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2017-10-26 11:40:50
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmCallLogManager  extends BaseManager<BpmCallLog> {
	/**
	 * 重新调用接口
	 * @param id
	 */
	void reinvoke(String id) throws Exception;
	
	/**
	 * 标记为成功
	 * @param id
	 */
	void signSuccess(String id);
}
