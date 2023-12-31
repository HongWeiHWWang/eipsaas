package com.hotent.service.ws.cxf.invok;

import com.hotent.base.service.InvokeCmd;
import com.hotent.base.service.InvokeResult;

/**
 * CXF框架调用接口执行器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public interface CxfInvokService {
	/**
	 * 调用接口
	 * @param invokeCmd
	 * @return
	 * @throws Exception
	 */
	InvokeResult invoke(InvokeCmd invokeCmd) throws Exception;
}
