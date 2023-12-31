package com.hotent.form.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.FormTemplate;


public interface FormTemplateDao extends BaseMapper<FormTemplate> {
	/**
	 * 删除所有的数据
	 */
	public void delSystem();
	
	/**
	 * 根据别名获取模版。
	 * @param alias
	 * @return
	 */
	public FormTemplate getByTemplateAlias(String alias);
	
	/**
	 * 获取模版是否有数据。
	 * @return
	 */
	public Integer getHasData();
	/**
	 * 根据模版类型取得模版列表。
	 * @param type
	 * @return
	 */
	public List<FormTemplate> getTemplateType(@Param("templateType")String templateType);

    /**
     * 更加模板ID、关联锁版本查询模板信息
     * @param map
     * @return
     */
    public FormTemplate getTemplateByRev(Map<String,Object> map);

	/**
	 * 设置默认模板
	 * @param templateId
	 */
	void setDefault(String templateId);
	/**
	 * 设置非默认模板
	 * @param
	 */
	void setNotDefault(String templateType);
}