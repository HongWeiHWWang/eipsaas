package com.hotent.form.persistence.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.form.model.Form;
/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月6日
 */
public interface FormDao extends BaseMapper<Form>{
	
	/**
	 * 获取表单列表（分页 ）
	 * @param iPage
	 * @param wrapper
	 * @return
	 */
	IPage<Form> getFormQueryList(IPage<Form> iPage,@Param(Constants.WRAPPER) Wrapper<Form>  wrapper);
	
	/**
	 * 根据表单key获取主版本的表单对象数据。
	 * @param formKey 表单key
	 * @return BpmForm
	 */
	Form getMainByFormKey(String formKey);

	/**
	 * 根据formKey 取得所有表单数据。
	 * @param formKey 表单key
	 * @return List<BpmForm>
	 */
	List<Form> getByFormKey(String formKey);
	
	/**
	 * 根据formKey 获取表单打印模板
	 * @param formKey
	 * @return
	 */
	List<Form> getPrintByFormKey(String formKey);
	
	/**
	 * 根据formKey获得总记录数
	 * @param formKey  表单key
	 * @return
	 */
	Integer getBpmFormCountsByFormKey(String formKey);
	
	/**
	 * 根据formKey获得表单最新版本号
	 * @param formKey 表单key
	 * @return
	 */
	Integer getMaxVersionByFormKey(String formKey);

	/**
	 * 根据表单key设置所有的版本都不是主版本
	 * @param formKey表单key
	 */
	void updNotDefaultByFormKey(String formKey);
	
	/**
	 * 设置主版本
	 * @param formId formId
	 */
	void updDefaultByFormId(String formId);
	
	/**
	 * 根据表单定义ID获得表单
	 * @param defId 表单元数据定义ID
	 * @return
	 */
	List<Form> getByDefId(String defId);
	
	/**
	 * 通过bocode获取与bo绑定的表单
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Form> getByBoCodes(Map map);

	/**
	 * 获取实体
	 * @param defId
	 * @return
	 */
	List<Map<String,Object>> getBoEnt(String defId);
}
