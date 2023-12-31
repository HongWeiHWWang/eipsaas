package com.hotent.form.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.FormBusSet;
/**
 * 表单业务数据保存设置 DAO
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月8日
 */
public interface FormBusSetDao extends BaseMapper<FormBusSet> {

	FormBusSet getByFormKey(String formKey);
	
	/**
	 * 判断业务数据保存设置是否存在。
	 * @param formSet
	 * @return
	 */
	Integer isExist(FormBusSet formSet);
	
	/**
	 * 根据表单键删除业务数据设置。
	 * @param formKey
	 */
	void removeByFormKey(String formKey);
}
