/**
 * 
 */
package com.hotent.form.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.FormField;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月6日
 */
public interface FormFieldDao extends BaseMapper<FormField> {
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
	List<FormField> getByFormIdAndBoDefId(@Param("formId")String formId,@Param("boDefId")String boDefId);
	
	void removeByAttrId(String attrId);

}
