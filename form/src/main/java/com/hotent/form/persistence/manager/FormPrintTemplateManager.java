package com.hotent.form.persistence.manager;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.QueryFilter;
import com.hotent.form.model.FormPrintTemplate;

public interface FormPrintTemplateManager extends BaseManager<FormPrintTemplate> {
    void saveFormPrintTemplate(FormPrintTemplate formPrintTemplate);

    public void setDefaultVersion(String formKey,String id, String printType);

    /**
     * 获取表单主模板(word套打)
     * @param formKey 表单key
     * @return
     */
    public FormPrintTemplate getMainFormPrintTemplate(String formKey);
    
    /**
     * 根据表单key和打印类型查询列表
     * @param formKey
     * @param printType
     * @return
     */
    List<FormPrintTemplate> getPrintTemplates(String formKey, String printType);
    
    void removeByIds(String ...ids);
    
    IPage<FormPrintTemplate> getPrintList(QueryFilter<FormPrintTemplate> queryFilter);
    
    /**
     * 根据表单key和打印类型获取主版本
     * @param formKey
     * @param printType
     * @return
     */
    FormPrintTemplate getMailPrintTemplates(String formKey, String printType);
}
