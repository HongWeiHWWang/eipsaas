package com.hotent.form.persistence.manager.impl;


import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.form.persistence.dao.FormDataImportLogDao;
import com.hotent.form.model.FormDataImportLog;
import com.hotent.form.persistence.manager.FormDataImportLogManager;

/**
 * 
 * <pre> 
 * 描述：form_data_import_log 处理实现类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-06-18 14:10:21
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("formDataImportLogManager")
public class FormDataImportLogManagerImpl extends BaseManagerImpl<FormDataImportLogDao, FormDataImportLog> implements FormDataImportLogManager{

	@Override
	public void deleteByPid(String pid) {
		baseMapper.deleteByPid(pid);
	}
	
}
