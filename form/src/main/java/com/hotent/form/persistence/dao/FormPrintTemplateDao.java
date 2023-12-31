package com.hotent.form.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.form.model.FormPrintTemplate;

public interface FormPrintTemplateDao  extends BaseMapper<FormPrintTemplate> {
	
    @Select("select * from FORM_PRINT_TEMPLATE ${ew.customSqlSegment}")
    List<FormPrintTemplate> getList(@Param(Constants.WRAPPER) Wrapper<FormPrintTemplate> wrapper);
    
    IPage<FormPrintTemplate> getPrintList(IPage<FormPrintTemplate> iPage,@Param(Constants.WRAPPER) Wrapper<FormPrintTemplate> wrapper);

}
