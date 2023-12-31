package com.hotent.form.persistence.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.FormMeta;

/**
 * 表单元数据 Dao接口
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月6日
 */
public interface FormMetaDao extends BaseMapper<FormMeta> {
 
	/**
	 * 通过表单ID删除 表单BO关联表中的记录
	 * @param formId
	 */
	void deleteBpmFormBo(String formId);
	
	/**
	 * 创建表单BO关联记录
	 * @param boDefId
	 * @param formId
	 */
	void createBpmFormBo(@Param("id")String id,@Param("boDefId")String boDefId,@Param("formId")String formId);

	
	/**
	 * 通过表单ID获取关联的BODefID
	 * @param formId
	 * @return
	 */
	List<String> getBODefIdByFormId(String formId);

	List<FormMeta> getBODefByFormId(String formId);
	
	/**
	 * 根据formId获取bocode 列表。
	 * @param formId
	 * @return
	 */
	List<String> getBOCodeByFormId(String formId);

	/**
	 * 根据BODefId 获取相关的表单定义。
	 * @param code
	 * @return
	 */
	List<FormMeta> getByBODefId(String BODefId);
	
	/**
	 * 根据formKey 获取表单定义。
	 * @param formKey
	 * @return
	 */
	FormMeta getByKey(String formKey);

	/**
	 * 更新表单元素书的意见定义。
	 * @param id
	 * @param opinionJson
	 */
	@SuppressWarnings("rawtypes")
	void updateOpinionConf(Map map);
	
	/**
	 * 根据表单ID获取bo实体信息数据。
	 * 获取实体名称和实例关系。
	 * @param formId
	 * @return
	 */
	List<Map<String,String>> getEntInfoByFormId(String formId);
	
	/**
	 * 根据实体名获取表单列表。
	 * @param name
	 * @return
	 */
	List<FormMeta> getByEntName(String name);
	
	/**
	 * 根据表单获取表单元数据key。
	 * @param formKey
	 * @return
	 */
	String getMetaKeyByFormKey(String formKey);
	
	/**
	 * 根据entId 获取关联的表单定义。
	 * @param entId
	 * @return
	 */
	List<FormMeta> getByEntId(String entId);

    /**
     * 更加表单ID、关联锁版本查询表单信息
     * @param map
     * @return
     */
    FormMeta getFormDefByRev(Map<String,Object> map);
}
