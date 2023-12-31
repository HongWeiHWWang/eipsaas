package com.hotent.form.persistence.manager.impl;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.form.model.FormHistoryRecord;
import com.hotent.form.persistence.dao.FormHistoryRecordDao;
import com.hotent.form.persistence.manager.FormHistoryRecordManager;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 
 * <pre> 
 * 描述：表单form_html历史数据 处理实现类
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-11-19 10:03:35
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("formHistoryRecordManager")
public class FormHistoryRecordManagerImpl extends BaseManagerImpl<FormHistoryRecordDao,FormHistoryRecord> implements FormHistoryRecordManager {

    @Override
    public List<FormHistoryRecord> getFormHtmlByFormId(String formId) {
        return baseMapper.getFormHtmlByFormId(formId);
    }
}
