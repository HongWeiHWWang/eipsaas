package com.hotent.form.persistence.manager;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.FormBusSet;

/**
 * 表单数据处理设置管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface FormBusSetManager extends BaseManager<FormBusSet>{
	
	/**
	 * 通过表单key获取表单数据处理设置
	 * @param formKey
	 * @return
	 */
	FormBusSet getByFormKey(String formKey);
	
	/**
	 * 判断业务数据保存设置是否存在。
	 * @param formSet
	 * @return
	 */
	boolean isExist(FormBusSet formSet);

	/**
	 * 表单业务设置明细页面
	 * @param id
	 * @param readonly
	 * @param formKey
	 * @return
	 * @throws Exception
	 */
	ObjectNode getDetail(String id, boolean readonly, String formKey) throws Exception;

	/**
	 * 获取树列表
	 * @param formKey
	 * @return
	 * @throws Exception
	 */
	ObjectNode getTreeList(String formKey) throws Exception;
}
