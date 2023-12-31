package com.hotent.base.service.impl;

import java.util.Map;
import org.springframework.stereotype.Service;
import com.hotent.base.service.InvokeCmd;
import com.hotent.base.service.InvokeResult;
import com.hotent.base.service.ServiceClient;

/**
 * ServiceClient接口的空实现
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月20日
 */
@Service
public class ServiceClientEmptyImpl implements ServiceClient{

	@Override
	public InvokeResult invoke(InvokeCmd invokeCmd) {
		return null;
	}

	@Override
	public InvokeResult invoke(String alias, Map<String, Object> map) {
		return null;
	}
}
