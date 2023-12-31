package com.hotent.form.persistence.manager;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.bo.model.BoDef;
import com.hotent.form.model.Form;
import com.hotent.form.param.FormPreviewDataParam;
import com.hotent.form.vo.BpmFormVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 业务表单处理接口
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月6日
 */
public interface FormManager extends BaseManager<Form>{
	
	/**
	 * 获取表单列表（分页）
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	IPage<Form> getFormQueryList(QueryFilter<Form> queryFilter) throws Exception;
	
	/**
	 * 通过模板生成表单Html
	 * @param formId 表单ID
	 * @param mainFieldTemplate 主模板
	 * @param subFieldListTemplate 复合字段模板
	 * @return
	 * @throws Exception
	 */
	String getHtml(String formId, String mainFieldTemplate, String subFieldListTemplate) throws Exception;
	
	/**
	 *根据表单key获取主版本的表单对象数据。
	 * @param formKey 表单key
	 * @return BpmForm
	 */
	Form getMainByFormKey(String formKey);
	
	/**
	 * 根据formKey 取得表单定义。
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
	 * 通过bocode获取与bo绑定的表单
	 * @param codes
	 * @param filter 
	 * @return
	 */
	List<Form> getByBoCodes(List<String> codes,String formType, QueryFilter<Form> filter);
	
	/**
	 * 根据formKey获得总记录数
	 * @param formKey  表单key
	 * @return
	 */
	Integer getBpmFormCountsByFormKey(String formKey);
	
	
	/**
	 * 根据表单定义id创建新的表单版本。 表单定义ID
	 * 
	 * @param formId  
	 * 				自定义表单Id
	 *           
	 * @throws Exception
	 */
	void newVersion(String formId) throws Exception ;

	/**
	 * 设为默认版本。
	 * 
	 * @param formId
	 *            自定义表单Id
	 * @param formKey
	 *            表单key
	 */
	void setDefaultVersion(String formId, String formKey) ;
	
	/**
	 * 发布
	 * 
	 * @param formId
	 *            自定义表单Id	
	 */
	void publish(String formId);
	
	
	/**
	 * 根据表单定义ID获得表单
	 * @param defId 表单元数据定义ID
	 * @return
	 */
	List<Form> getByDefId(String defId);
	
	/**
	 * 表单导入
	 * @param 
	 */
	void importForms(ObjectNode obj,String typeId);
	
	/**
	 * 表单导入
	 * @param formXml
	 * @param importBoDef 
	 * @throws Exception
	 */
	void importByFormXml(String formXml, List<BoDef> importBoDef,Map<String,String> nameMap) throws Exception;
	
	/**
	 * 是否导出表单
	 * @param idList
	 * @param containBo
	 * @return
	 */
	Map<String,String> exportForms(List<String> idList, boolean containBo);
	
	/**
	 * 生成字段的html
	 * @param defId 表单元数据ID
	 * @param attrId boAttrId
	 * @param formType 表单类型
	 * @return
	 */
	String genByField(String defId, String attrId, String formType);
	
	/**
	 * 删除表单。
	 * @param aryIds 表单元数据定义ID
	 */
	void remove(String[] aryIds);
	
	/**
	 * 通过模板生成拖拽表单Html
	 * @param expand
	 * @param tableNames
	 * @param ganged
	 * @return
	 */
	String getDesignHtml(JsonNode expand,String tableNames,String ganged,JsonNode formDefNode) throws Exception;
	
	/**
	 * 表单设计数据
	 * @param formData
	 * @return
	 * @throws Exception
	 */
	void saveDesign(String formData) throws Exception;
	
	/**
	 * 保存表单元数据
	 * @param bpmFormVo
	 * @throws Exception
	 */
	void saveFormDef(BpmFormVo bpmFormVo) throws Exception;

    /**
     * 更新表单的自定义脚本
     * @param formId
     * @param divJs
     * @return
     * @throws Exception
     */
    CommonResult<String> saveFormJs(String formId,String divJs  ,String formHtml);

    CommonResult<String> updateFormHistoryRecord(String formId,String formHtml);
	
	/**
	 * 获取手机表单设计的html
	 * @param expand
	 * @param tableNames
	 * @param ganged
	 * @return
	 * @throws Exception 
	 */
	String getMobileDesignHtml(JsonNode expand, String tableNames,
			String ganged) throws Exception;
	/**
	 * 生成表单html
	 * @param bpmFormVo
	 */
	void generateFrom(BpmFormVo bpmFormVo)  throws Exception;
	
	/**
	 * pc 表单转换为手机表单
	 * @param formId
	 */
	void pcForm2MobileForm(String formId)  throws Exception;

	/**
	 * 获取表单绑定数据
	 * @param defId
	 * @param formKey
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getBindRelation(String defId, String formKey) throws Exception;

	/**
	 * 获取实体
	 * @param pcAlias
	 * @param mobileAlias
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getFormData(String pcAlias,String mobileAlias) throws Exception;

	/**
	 * Vue表单预览
	 * @param formId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getPreviewDesignVueData(String formId) throws Exception;

	/**
	 * 表单预览
	 * @param param
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getPreviewDesignData(FormPreviewDataParam param) throws Exception;

	/**
	 * 获取表单权限
	 * @param id
	 * @param defId
	 * @param formType
	 * @return
	 * @throws Exception
	 */
	JsonNode getRightData(String id, String defId, String formType) throws Exception;

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
	 * 根据模板产生html
	 * @param formId
	 * @param tableNames
	 * @param templateAlias
	 * @param formDefId
	 * @param formType
	 * @param response
	 * @throws Exception
	 */
	void getGenByTemplate(String formId, String tableNames, String templateAlias, String formDefId, String formType, HttpServletResponse response) throws Exception;

	/**
	 * 获取表单已设置的信息
	 * @param formId
	 * @return
	 * @throws Exception
	 */
	ObjectNode getFormDesign(String formId) throws Exception;
	
	/**
	 * 保存表单打印模板
	 * @param form
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> savePrintTemplate(Form form) throws Exception;

	/**
	 * 切换版本时候刷新permission
	 * @throws Exception
	 */
	void updatePermissionByKey(String formKey) throws Exception;
	
	/**
	 * 将导入文件暂存在缓存中
	 * @param fileId
	 * @param fileJson
	 * @return
	 */
	String putImportFileInCache(String fileId, String fileJson);
	
	/**
	 * 从缓存中取出暂存文件
	 * @param fileId
	 * @return
	 */
	String getImportFileFromCache(String fileId);
	
	/**
	 * 删除缓存中的暂存文件
	 * @param fileId
	 * @return
	 */
	void delImportFileFromCache(String fileId);
}
