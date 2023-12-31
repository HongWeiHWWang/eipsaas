package com.hotent.form.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.FormHistoryRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * <pre> 
 * 描述：表单form_html历史数据 DAO接口
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-11-19 10:03:35
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface FormHistoryRecordDao extends BaseMapper<FormHistoryRecord> {


    /**
     * 根据表单ID创建表单HTML内容（表单HTML数据历史记录）
     * @param formId
     * @return
     */
    List<FormHistoryRecord> getFormHtmlByFormId(@Param("formId") String formId);

}
