package com.hotent.form.persistence.manager.impl;

import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.form.model.CombinateDialog;
import com.hotent.form.persistence.dao.CombinateDialogDao;
import com.hotent.form.persistence.manager.CombinateDialogManager;

/**
 * 组合对话框
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("combinateDialogManager")
public class CombinateDialogManagerImpl extends BaseManagerImpl<CombinateDialogDao, CombinateDialog> implements CombinateDialogManager {
	@Override
	public CombinateDialog getByAlias(String alias) {
		return baseMapper.getByAlias(alias);
	}
}
