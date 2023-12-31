package com.hotent.form.persistence.manager.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.bo.util.BoUtil;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormMeta;
import com.hotent.form.persistence.manager.*;
import com.hotent.form.service.FormService;
import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.form.model.FormBusSet;
import com.hotent.form.persistence.dao.FormBusSetDao;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表单数据处理设置
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("formBusSetManager")
public class FormBusSetManagerImpl extends BaseManagerImpl<FormBusSetDao, FormBusSet> implements FormBusSetManager{
	@Resource
	FormManager bpmFormManager;
	@Resource
	FormMetaManager bpmFormdefManager;
	@Resource
	FormRightManager bpmFormRightManager;
	@Resource
	FormBusManager formBusManager;
	@Resource
	FormService formService;
	@Resource
	BoDefManager bODefManager;

	@Override
	public FormBusSet getByFormKey(String formKey) {
		return baseMapper.getByFormKey(formKey);
	}
	
	@Override
	public boolean isExist(FormBusSet formSet) {
		return baseMapper.isExist(formSet)>0;
	}

	@Override
	public ObjectNode getDetail(String id, boolean readonly, String formKey) throws Exception {
		FormBusSet formBusSet = getByFormKey(formKey);

		Form form = bpmFormManager.getMainByFormKey(formKey);
		FormMeta formDef= bpmFormdefManager.get(form.getDefId());
		List<String> boCode = bpmFormdefManager.getBOCodeByFormId(formDef.getId());

		JsonNode permissionConf = bpmFormRightManager.getByFormKey(formKey, readonly);
		JsonNode permission = bpmFormRightManager.calcFormPermission(permissionConf);
		BoData boData =  formBusManager.getBoData(boCode.get(0),id);
		ObjectNode json = JsonUtil.getMapper().createObjectNode();
		json.put("data", BoUtil.toJSON(boData,true));
		json.put("boCode", boCode.get(0));
		json.put("permission", permission);
		json.put("formHtml", form.getFormHtml());
		json.put("formBusSet", JsonUtil.toJson(formBusSet));//  提交前后置脚本
		return json;
	}

	@Override
	public ObjectNode getTreeList(String formKey) throws Exception {
		Form form = (Form) formService.getByFormKey(formKey);

		FormMeta formDef= bpmFormdefManager.get(form.getDefId());
		List<String> boCode = bpmFormdefManager.getBOCodeByFormId(formDef.getId());
		BoDef boDef = bODefManager.getByAlias(boCode.get(0));
		BoEnt boEnt = boDef.getBoEnt();

		ObjectNode json = JsonUtil.getMapper().createObjectNode();
		json.put("name", form.getName()+"列表");
		json.put("alias",form.getFormKey()+"List");
		json.put("sql" , " select * from "+ boEnt.getTableName());
		json.put("dsName", boEnt.getDsName());

		List array = new ArrayList();
		Map addBtn = new HashMap();
		addBtn.put("name", "新增");
		addBtn.put("inRow", "0");
		addBtn.put("triggerType", "href");
		addBtn.put("urlPath", "/form/formBus/"+formKey+"/edit");
		array.add(addBtn);

		Map editBtn = new HashMap();
		editBtn.put("name", "编辑");
		editBtn.put("inRow", "1");
		editBtn.put("triggerType", "href");
		editBtn.put("urlPath", "/form/formBus/"+formKey+"/edit?id={id_}");
		array.add(editBtn);

		Map getBtn = new HashMap();
		getBtn.put("name", "明细");
		getBtn.put("inRow", "1");
		getBtn.put("triggerType", "href");
		getBtn.put("urlPath", "/form/formBus/"+formKey+"/get?id={id_}");
		array.add(getBtn);

		Map export = new HashMap();
		export.put("name", "导出");
		export.put("inRow", "0");
		export.put("triggerType", "onclick");
		export.put("urlPath", "exports()");
		array.add(export);
		json.put("buttonDef", JsonUtil.toJsonNode(array));
		return json;
	}

}
