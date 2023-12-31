package com.hotent.base.handler;

/**
 * 设置临时忽略多租户的返回结果
 * <p>
 * 这个结果实现了AutoCloseable接口，在调用设置忽略方式时用try(MultiTenantIgnoreResult result = MultiTenantHandler.setThreadLocalIgnore())的方式调用，这样在try方法体中的代码执行完成后会自动关闭忽略。
 * </p>
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月26日
 */
public class MultiTenantIgnoreResult implements AutoCloseable{

	@Override
	public void close() throws Exception {
		MultiTenantHandler.removeThreadLocalIgnore();
	}
}
