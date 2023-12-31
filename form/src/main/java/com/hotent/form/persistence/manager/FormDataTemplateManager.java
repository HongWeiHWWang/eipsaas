package com.hotent.form.persistence.manager;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoEnt;
import com.hotent.form.model.FormDataTemplate;
import com.hotent.form.model.FormDataTemplateDraft;
import com.hotent.form.param.DataTemplateQueryVo;
import com.hotent.form.vo.ExportSubVo;

/**
 * 业务数据模板管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface FormDataTemplateManager extends BaseManager<FormDataTemplate>{
    /**
     * 数据报表导出接口。
     * @param list		数据报表ID列表。
     * @return  String		导出数据报表成字符串。
     * @throws Exception
     */
    Map<String,String> exportDef(List<String> list) throws Exception;

    /**
     * 导入数据报表
     * @param 解压后文件的位置。
     * void
     * @return
     */
    CommonResult<String> importDef(ObjectNode objectNode, String typeId);

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
	/**
	 * 根据表单key删除
	 * @param formKey
	 */
	void removeByFormKey(String formKey);
	/**
	 * 根据formKey 获取业务数据模板相关信息
	 * @param alias
	 * @return
	 * @throws IOException
	 */
	ObjectNode getByFormKey(String formKey, String boId) throws IOException;

	/**
	 * 根据id 获取业务数据模板相关信息
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ObjectNode getByTemplateId(String id, String boId) throws IOException;

	/**
	 * 根据formKey 获取业务数据模板
	 * @param formKey
	 * @return
	 */
	List<FormDataTemplate> getTemplateByFormKey(String formKey);

	/**
	 * 根据数据报表别名获取
	 * @param alias
	 * @return
	 */
	FormDataTemplate getByAlias(String alias);
	/**
	 * 根据id 获取业务数据模板导出信息
	 * @param id
	 * @return
	 * @throws IOException
	 */
	FormDataTemplate getExportDisplay(String id) throws IOException;

	/**
	 * 保存业务数据模板
	 * @param bpmDataTemplate
	 * @param resetTemp
	 * @throws Exception
	 */
	void save(FormDataTemplate bpmDataTemplate, boolean resetTemp) throws Exception;
	
	/**
	 * 获取业务数据模板的展示html
	 * @param alias
	 * @param params
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	String getDisplay(String alias, Map<String, Object> params,
			Map<String, Object> queryParams) throws Exception;
	/**
	 * 获取表单
	 * @param formKey
	 * @return
	 */
	Map<String,Object> getFormByFormKey(String formKey);

	/**
	 * 保存bo业务对象数据
	 * @param jsonObject
	 * @param boAlias
	 * @param delDraftId 需要删除的草稿的id
	 * @throws IOException
	 */
	void boSave(ObjectNode jsonObject, String boAlias, String delDraftId) throws Exception;

	/**
	 * 删除
	 * @param ids
	 * @param boAlias
	 */
	void boDel(String[] ids, String boAlias);

	/**
	 * 获取过滤sql语句
	 * @param filterField
	 * @param dsName
	 * @param param
	 * @return
	 * @throws IOException
	 */
	String getFilterSql(String filterField,String dsName,Map<String, Object> param) throws IOException;

	/**
	 * 获取数据权限过滤语句
	 * @param dataPermission
	 * @param fieldPre
	 * @return
	 * @throws IOException
	 */
	String getDataPermissionSql(String dataPermission,String fieldPre) throws IOException;

	/**
	 * 获取所有业务数据模板的表单key
	 * @return
	 */
	Set<String> getAllFormKeys();

	/**
	 * 导入子表数据
	 */
	void importData(List<MultipartFile> file,String refId,String alias) throws Exception;

	/**
	 * 根据表单Key, boAlias 获取表单html， 权限， bo数据结构
	 * @param formKey
	 * @param boAlias
	 * @param id
	 * @param action
	 * @return
	 * @throws IOException
	 */
	Map<String, Object> getFormData(String formKey, String boAlias, String id, String action, String recordId) throws Exception;

	/**
	 * 导出数据
	 * @param response
	 * @param formKey
	 * @param getType
	 * @param filterKey
	 * @param expField
	 * @param queryFilter
	 * @throws Exception
	 */
	void exportData(HttpServletResponse response, String formKey, String getType, String filterKey, String expField, QueryFilter queryFilter) throws Exception;

	PageList getList(FormDataTemplate template, DataTemplateQueryVo dataTemplateQueryVo) throws Exception;

	/**
	 * 根据实体别名和外键id获取子表数据(分页)
	 * @param queryFilter
	 * @param alias
	 * @param refId
	 * @return
	 * @throws Exception
	 */
	PageList<Map<String,Object>> getSubDataPagination(QueryFilter queryFilter, String alias, String refId) throws Exception;

	/**
	 * 根据实体别名和外键id获取子表数据
	 * @param alias
	 * @param refId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getSubData(String alias, String refId) throws Exception;

	/**
	 * 导出子表数据
	 * @param alias
	 * @param refId
	 * @param getType
	 * @param filterKey
	 * @param expField
	 * @param queryFilter
	 * @throws Exception
	 */
	void exportSub(HttpServletResponse response, ExportSubVo vo) throws Exception;
	/**
	 * 下载用于导入bo主表的excel模板
	 * @param response
	 * @param alias 数据模板的别名
	 * @throws IOException
	 * @throws Exception
	 */
	void downloadMainTempFile(HttpServletResponse response, String alias) throws IOException, Exception;
	/**
	 * 导入主表数据
	 * @param files
	 * @param alias 数据模板别名
	 * @throws Exception 
	 */
	void importMain(List<MultipartFile> files, String alias) throws Exception;
	/**
	 * 保存业务模板草稿数据
	 * @param boData 业务数据
	 * @param tempAlias 数据模板别名
	 * @param draftId 草稿id
	 */
	void boSaveDraft(FormDataTemplateDraft dataTemplateDraft);
	/**
	 * 获取草稿数据
	 * @param draftId
	 * @return
	 * @throws IOException 
	 */
	Map<String, Object> getTempDraftData(String draftId) throws IOException;
	
	/**
	 * 导入主表数据
	 * @param files
	 * @param alias 数据模板别名
	 * @throws IOException 
	 * @throws Exception 
	 */
	
	void checkAndImportData(List<Map<String, Object>> rows, BoEnt boEnt, Map<String, BoAttribute> columnMap, String bindFilld,
			String fillValue, FormDataTemplate template) throws Exception;
	
	List<Map<String, Object>> resolutionExcel(MultipartFile f, Map<String, BoAttribute> columnMap, String orElse);
}
