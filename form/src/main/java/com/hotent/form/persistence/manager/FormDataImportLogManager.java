package com.hotent.form.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.FormDataImportLog;

/**
 * 
 * <pre> 
 * 描述：form_data_import_log 处理接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-06-18 14:10:21
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface FormDataImportLogManager extends BaseManager<FormDataImportLog>{
	
	void deleteByPid(String pid);
}
