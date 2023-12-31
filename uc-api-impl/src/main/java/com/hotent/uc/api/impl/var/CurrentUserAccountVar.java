package com.hotent.uc.api.impl.var;

import com.hotent.uc.api.impl.util.ContextUtil;

/**
 * 类 {@code CurrentUserAccountVar} 当前用户账号
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月9日
 */
public class CurrentUserAccountVar implements IContextVar {

	@Override
	public String getTitle() {
		return "当前用户账号";
	}

	@Override
	public String getAlias() {
		return "curUserAccount";
	}

	@Override
	public String getValue() {
		return ContextUtil.getCurrentUser().getAccount();
	}

}
