package com.hotent.form.persistence.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoEnt;
import com.hotent.form.model.FormMeta;

/**
 * 表单元数据处理接口
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月6日
 */
public interface FormMetaManager extends BaseManager<FormMeta>{
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
	void createBpmFormBo(String id, String boDefId, String formId);
	/**
	 * 根据bo定义ID获取对应的表单元数据定义。
	 * @param BODefId
	 * @return
	 */
	List<FormMeta> getByBODefId(String BODefId);
	
	/**
	 * 通过表单ID获取关联的BODefID
	 * @param formId
	 * @return
	 */
	List<String> getBODefIdByFormId(String formId);

	List<FormMeta> getBODefByFormId(String formId);

	/**
	 * 根据表单key获取表单元数据定义。
	 * @param formKey
	 * @return
	 */
	FormMeta getByKey(String formKey);

	/**
	 * 更新表单意见配置。
	 * @param id
	 * @param config
	 */
	void updateOpinionConf(String id, String opinionJson);
	
	/**
	 * 根据表单key获取表单元数据key。
	 * @param formKey
	 * @return
	 */
	String getMetaKeyByFormKey(String formKey);

	/**
	 * 根据formId获取bocode 列表。
	 * @param formDefId 表单ID
	 * @return
	 */
	List<String> getBOCodeByFormId(String formDefId);
	
	/**
	 * 根据组件key获取子表BoEnt
	 * @param formKey
	 * @return
	 * @throws IOException 
	 */
	List<BoEnt> getChildrenByFormKey(String formKey) throws IOException;
	
	/**
	 * 根据表单元数据id获取bodata
	 * @param formDefId
	 * @return
	 */
	List<BoData> getBoDataByFormDefId(String formDefId);
	
	/**
     * 更加表单ID、关联锁版本查询表单信息
     * @param map
     * @return
     */
    FormMeta getFormDefByRev(Map<String,Object> map);

	/**
	 * 流程任务表单列表(分页条件查询)数据
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList listJson(QueryFilter queryFilter) throws Exception;

	/**
	 * 流程任务表单列表(分页条件查询)数据
	 * @param queryFilter
	 * @param defId
	 * @param formType
	 * @param topDefKey
	 * @return
	 * @throws Exception
	 */
	PageList listJsonByBODef(QueryFilter queryFilter, String defId, String formType, String topDefKey) throws Exception;

	/**
	 * 加载编辑器设计模式的模板列表
	 * @param subject
	 * @param categoryId
	 * @param formDesc
	 * @param isSimple
	 * @return
	 * @throws Exception
	 */
	Map getChooseDesignTemplate(String subject, String categoryId, String formDesc, Boolean isSimple) throws Exception;
	
	/**
	 * 创建字段数据
	 * @param bpmFormDef
	 * @param entIdMap
	 * @throws IOException 
	 */
	void createFields(FormMeta bpmFormDef,Map<String,String> entIdMap) throws IOException;
	
	/**
	 * 导入表单时更新
	 * @param formDef
	 * @param entIdMap
	 */
	void createByImport(FormMeta formDef,Map<String,String> entIdMap);
	
	/**
	 * 导入表单时新增
	 * @param formDef
	 * @param entIdMap
	 */
	void updateByImport(FormMeta formDef,Map<String,String> entIdMap);
	
	public void createFormField(String expand, String formDefId) throws IOException;
	

}
