package com.hotent.form.persistence.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormPrintTemplate;
import com.hotent.form.persistence.dao.FormPrintTemplateDao;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.persistence.manager.FormPrintTemplateManager;

@Service("formPrintTemplateManager")
public class FormPrintTemplateManagerImpl extends BaseManagerImpl<FormPrintTemplateDao, FormPrintTemplate> implements FormPrintTemplateManager {
	@Resource
	FormManager bpmFormManager;
	@Resource
	FormMetaManager formMetaManager;
	
	@Override
    public void saveFormPrintTemplate(FormPrintTemplate formPrintTemplate) {

        List<FormPrintTemplate> list = baseMapper.getList(Wrappers.<FormPrintTemplate>lambdaQuery()
                .eq(FormPrintTemplate::getFormKey, formPrintTemplate.getFormKey())
                .eq(FormPrintTemplate::getPrintType, formPrintTemplate.getPrintType()));
        if(list.size()>0){
            formPrintTemplate.setIsMain("N");
        }else{
            formPrintTemplate.setIsMain("Y");
        }
        super.create(formPrintTemplate);
    }

    @Override
    public void setDefaultVersion(String formKey, String id, String printType) {
        FormPrintTemplate template=new FormPrintTemplate();
        template.setIsMain("N");
        baseMapper.update(template,(Wrappers.<FormPrintTemplate>lambdaQuery()
                .eq(FormPrintTemplate::getFormKey, formKey)
                .eq(FormPrintTemplate::getPrintType, printType)));
        template.setIsMain("Y");
        baseMapper.update(template ,(Wrappers.<FormPrintTemplate>lambdaQuery()
                .eq(FormPrintTemplate::getFormKey, formKey)
                .eq(FormPrintTemplate::getId, id)));
    }

    @Override
    public FormPrintTemplate getMainFormPrintTemplate(String formKey) {
        FormPrintTemplate formPrintTemplate=baseMapper.selectOne((Wrappers.<FormPrintTemplate>lambdaQuery()
                .eq(FormPrintTemplate::getFormKey, formKey)
                .eq(FormPrintTemplate::getIsMain, "Y")
                .eq(FormPrintTemplate::getPrintType, "word")));
        return formPrintTemplate;
    }

	@Override
	public List<FormPrintTemplate> getPrintTemplates(String formKey, String printType) {
		List<FormPrintTemplate> list = baseMapper.getList(Wrappers.<FormPrintTemplate>lambdaQuery()
                .eq(FormPrintTemplate::getFormKey, formKey)
                .eq(FormPrintTemplate::getPrintType, printType));
		return list;
	}
	
	/**
	 * 通过formKey删除缓存中的bpmForm
	 * @param formKey
	 */
	@CacheEvict(value = "form:bpmForm", key="#formKey")
	protected void removeFromCache(String formKey) {}
	
	@Override
	public void removeByIds(String ...ids){
		FormPrintTemplateManagerImpl bean = AppUtil.getBean(getClass());
		for (String id : ids) {
			FormPrintTemplate formPrintTemplate = this.get(id);
			if("form".equals(formPrintTemplate.getPrintType())) {
				//删除表单
				String formId = formPrintTemplate.getFormId();
				if(StringUtil.isNotEmpty(formId)) {
					Form bpmForm = bpmFormManager.get(formId);
					if (BeanUtils.isNotEmpty(bpmForm)) {
						String formKey = bpmForm.getFormKey();
						bpmFormManager.remove(bpmForm.getId());
						String fromDefId = bpmForm.getDefId();
						formMetaManager.remove(fromDefId);
						// 删除表单和元数据的时候。一并删除元数据和业务对象的关联关系
						formMetaManager.deleteBpmFormBo(fromDefId);
						bean.removeFromCache(formKey);
					}
				}
			}
			this.remove(id);
		}
	}

	@Override
	public IPage<FormPrintTemplate> getPrintList(QueryFilter<FormPrintTemplate> queryFilter) {
		return baseMapper.getPrintList(convert2IPage(queryFilter.getPageBean()),convert2Wrapper(queryFilter, currentModelClass()));
	}

	@Override
	public FormPrintTemplate getMailPrintTemplates(String formKey, String printType) {
		FormPrintTemplate formPrintTemplate = baseMapper.selectOne((Wrappers.<FormPrintTemplate>lambdaQuery()
				.eq(FormPrintTemplate::getIsMain, "Y")
				.eq(FormPrintTemplate::getFormKey, formKey)
                .eq(FormPrintTemplate::getPrintType, printType)));
		return formPrintTemplate;
	}
}
