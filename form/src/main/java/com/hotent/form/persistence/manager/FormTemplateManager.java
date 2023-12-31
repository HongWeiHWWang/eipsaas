package com.hotent.form.persistence.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.FormTemplate;

/**
 * 表单模板管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface FormTemplateManager extends BaseManager<FormTemplate>{
	/**
	 * 根据模版别名取得模版。
	 * @param alias
	 * @return
	 */
	public FormTemplate getByTemplateAlias(String alias);
	
	/**
	 * 获取所有的系统原始模板
	 * @return
	 * @throws Exception 
	 */
	public void initAllTemplate() throws Exception;
	
	/**
	 * 当模版数据为空时，将form目录下的模版添加到数据库中。
	 */
	public void init()  throws Exception;
	
	/**
	 * 检查模板别名是否唯一
	 * @param alias
	 * @return
	 */
	public boolean isExistAlias(String alias);
	
	/**
	 * 将用户自定义模板备份
	 * @param id
	 */
	public void backUpTemplate(String id);
	
	/**
	 * 根据模版类型取得模版列表。
	 * @param type
	 * @return
	 */
	public List<FormTemplate> getTemplateType(String type);

	public Map<String,Object> getTemplateTypeMap(String ...types);

	/**
	 * 获取主表模版
	 * @return
	 */
	public List<FormTemplate> getAllMainTableTemplate(boolean isPC) ;
	
	/**
	 * 获取子表模版。
	 * @return
	 */
	public List<FormTemplate> getAllSubTableTemplate(boolean isPC) ;

	/**
	 * 选择模板
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> selectTemplate(String defId, int isSimple, String templatesId, String formType) throws Exception;

	/**
	 * 获取宏模版。
	 * @return
	 */
	public List<FormTemplate> getAllMacroTemplate() ;
	
	/**
	 * 获取表管理模版。
	 * @return
	 */
	public List<FormTemplate> getAllTableManageTemplate() ;
	/**
	 * 获取列表模版。
	 * @return
	 */
	public List< FormTemplate> getListTemplate() ;
	
	/**
	 * 获取明细模版。
	 * @return
	 */
	public List< FormTemplate> getDetailTemplate() ;
	
	/**
	 * 获取数据模版。
	 * @return
	 */
	public List< FormTemplate> getDataTemplate();
	
	/**
	 * 获取查询数据模版。
	 * @return
	 */
	public List< FormTemplate> getQueryDataTemplate() ;

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
	void setDefault(String templateId,String templateType);
}
