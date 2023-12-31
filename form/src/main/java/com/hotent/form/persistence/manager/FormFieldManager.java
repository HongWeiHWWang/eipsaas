package com.hotent.form.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.FormField;

/**
 * 表单字段处理接口
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface FormFieldManager extends BaseManager<FormField>{
	/**
	 * 根据外键获取子表明细列表
	 * @param formId
	 * @return
	 */
	List<FormField> getByFormId(String formId);
	
	/**
	 * 通过表单ID获取字段列表(不包含属于分组的字段)
	 * @param formId
	 * @return
	 */
	List<FormField> getOnlyByFormId(String formId);
	
	/**
	 * 通过分组ID获取字段列表
	 * @param groupId
	 * @return
	 */
	List<FormField> getByGroupId(String groupId);
	
	/**
	 * 根据外键删除子表记录
	 * @param formId
	 * @return
	 */
	void delByMainId(String formId);
	
	/**
	 * 根据formId 获取列表。
	 * @param formId
	 * @return
	 */
	List<FormField> getExtByFormId(String formId);
	
	/**
	 * 根据业务对象id, 获取主对象的字段信息
	 * @param boDefId
	 * @return
	 */
	List<FormField> getByboDefId(String boDefId);
	
	/**
	 * 根据 表单id 和  业务对象id, 获取主对象的字段信息
	 * @param formId
	 * @param boDefId
	 * @return
	 */
	List<FormField> getByFormIdAndBoDefId(String formId, String boDefId);
	
	/**
	 * 通过bo属性id删除数据
	 * @param attrId
	 */
	void removeByAttrId(String attrId);
}
