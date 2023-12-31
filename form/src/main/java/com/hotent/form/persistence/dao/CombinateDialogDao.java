package com.hotent.form.persistence.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.CombinateDialog;

/**
 * 组合对话框
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface CombinateDialogDao extends BaseMapper<CombinateDialog> {
	CombinateDialog getByAlias(String alias);
}
