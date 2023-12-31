package com.hotent.form.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.CombinateDialog;

/**
 * 组合对话框管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface CombinateDialogManager extends BaseManager<CombinateDialog> {
	/**
	 * 通过别名获取组合对话框
	 * @param alias
	 * @return
	 */
	CombinateDialog getByAlias(String alias);
}
