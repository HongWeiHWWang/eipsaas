package com.hotent.form.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.FormDataTemplate;

/**
 * 业务数据模板
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface FormDataTemplateDao extends BaseMapper<FormDataTemplate> {
	/**
	 * 根据表单key获取
	 * @param formKey
	 * @return
	 */
	List<FormDataTemplate> getByFormKey(String formKey);
	/**
	 * 根据数据报表别名获取
	 * @param alias
	 * @return
	 */
	FormDataTemplate getByAlias(String alias);

	Integer getCountByAlias(@Param("alias")String alias);
	
	/**
	 * 根据表单key删除
	 * @param formKey
	 */
	void removeByFormKey(String formKey);
	
	/**
	 * 获取所有业务数据模板的表单key
	 * @return
	 */
	List<String> getAllFormKeys();
	
}
