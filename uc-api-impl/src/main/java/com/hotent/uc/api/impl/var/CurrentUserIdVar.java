package com.hotent.uc.api.impl.var;

import com.hotent.uc.api.impl.util.ContextUtil;

/**
 * 接口 {@code CurrentUserIdVar} 当前用户ID
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月9日
 */
public class CurrentUserIdVar implements IContextVar {

	@Override
	public String getTitle() {
		return "当前用户ID";
	}

	@Override
	public String getAlias() {
		return "curUserId";
	}

	@Override
	public String getValue() {
		return ContextUtil.getCurrentUserId();
	}

}
