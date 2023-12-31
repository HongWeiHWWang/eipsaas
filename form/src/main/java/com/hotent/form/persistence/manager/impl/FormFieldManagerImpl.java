package com.hotent.form.persistence.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.form.model.FormField;
import com.hotent.form.persistence.dao.FormFieldDao;
import com.hotent.form.persistence.manager.FormFieldManager;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FormFieldManagerImpl extends BaseManagerImpl<FormFieldDao, FormField> implements FormFieldManager{

	@Override
	public List<FormField> getByFormId(String formId) {
		return baseMapper.getByFormId(formId);
	}

	@Override
	public List<FormField> getOnlyByFormId(String formId) {
		return baseMapper.getOnlyByFormId(formId);
	}

	@Override
	public List<FormField> getByGroupId(String groupId) {
		return baseMapper.getByGroupId(groupId);
	}

	@Override
	@Transactional
	public void delByMainId(String formId) {
		baseMapper.delByMainId(formId);
	}

	@Override
	public List<FormField> getExtByFormId(String formId) {
		return baseMapper.getExtByFormId(formId);
	}

	@Override
	public List<FormField> getByboDefId(String boDefId) {
		return baseMapper.getByboDefId(boDefId);
	}

	@Override
	public List<FormField> getByFormIdAndBoDefId(String formId, String boDefId) {
		return baseMapper.getByFormIdAndBoDefId(formId, boDefId);
	}

	@Override
	@Transactional
	public void removeByAttrId(String attrId) {
		baseMapper.removeByAttrId(attrId);
	}
}
