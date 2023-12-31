package com.hotent.form.persistence.manager.impl;


import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.form.persistence.dao.FormDataTemplateDraftDao;
import com.hotent.form.model.FormDataTemplateDraft;
import com.hotent.form.persistence.manager.FormDataTemplateDraftManager;

/**
 * 
 * <pre> 
 * 描述：数据报表草稿数据 处理实现类
 * 构建组：x7
 * 作者:pangq
 * 邮箱:pangq@jee-soft.cn
 * 日期:2020-06-13 13:45:06
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("formDataTemplateDraftManager")
public class FormDataTemplateDraftManagerImpl extends BaseManagerImpl<FormDataTemplateDraftDao, FormDataTemplateDraft> implements FormDataTemplateDraftManager{
	
}
