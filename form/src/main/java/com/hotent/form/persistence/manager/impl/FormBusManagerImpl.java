package com.hotent.form.persistence.manager.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.bo.bodef.BoDefService;
import com.hotent.bo.instance.BoDataHandler;
import com.hotent.bo.instance.BoInstanceFactory;
import com.hotent.bo.instance.DataTransform;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoResult;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormBusSet;
import com.hotent.form.model.FormMeta;
import com.hotent.form.persistence.dao.FormBusSetDao;
import com.hotent.form.persistence.manager.FormBusManager;
import com.hotent.form.persistence.manager.FormBusSetManager;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;

/**
 * 表单数据处理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("formBusManager")
public class FormBusManagerImpl extends BaseManagerImpl<FormBusSetDao, FormBusSet> implements FormBusManager{
	@Resource
	BoInstanceFactory boInstanceFactory;
	@Resource
	BoDefService boDefService;
	@Resource
	DataTransform dataTransform;
	@Resource
	FormBusSetManager formBusSetManager;
	@Resource
	FormManager bpmFormManager;
	@Resource
	FormMetaManager bpmFormdefManager;
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	
	@Override
	public BoData getBoData(String boKey, String id) throws IOException {
		BoDataHandler boDataHandler = boInstanceFactory.getBySaveType("database");
		if(boDataHandler==null){
			return null;
		}
		if(StringUtil.isNotEmpty(id)){
			return boDataHandler.getById(id, boKey);
		}
		return boDataHandler.getByBoDefAlias(boKey);
	}

	@Override
	@Transactional
	public void saveData(String formKey, String json) throws IOException {
		BoDataHandler boDataHandler= boInstanceFactory.getBySaveType("database");
		String boCode  =getBoCodeByForm(formKey);
		
		BoDef boDef= boDefService.getByAlias(boCode);
		BoData curData= dataTransform.parse(json);
		if(boDef!=null){
			BoEnt boEnt=boDef.getBoEnt();
			curData.setBoEnt(boEnt);
			curData.setBoDef(boDef);
		}
		FormBusSet busSet = formBusSetManager.getByFormKey(formKey);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("boData", curData);
		if(busSet!=null){
			//前置脚本
			if(StringUtil.isNotEmpty(busSet.getPreScript())){
				groovyScriptEngine.execute(busSet.getPreScript(), param);
			}
		}
		
		// 保存
		List<BoResult> listResult = boDataHandler.save("", "", curData);
		//后置脚本
		if(busSet!=null&&StringUtil.isNotEmpty(busSet.getAfterScript())){
			groovyScriptEngine.execute(busSet.getAfterScript(), param);
		}
		
		if(BeanUtils.isNotEmpty(listResult)){
			if("add".equals(listResult.get(0).getAction())){
				ThreadMsgUtil.addMsg("添加成功！");
			}else{
				ThreadMsgUtil.addMsg("编辑成功！");
			}
		}
	}
	
	@Override
	@Transactional
	public void removeByIds(String[] aryIds, String formKey) {
		BoDataHandler boDataHandler= boInstanceFactory.getBySaveType("database");
		String boCode = getBoCodeByForm(formKey);
		boDataHandler.removeBoData(boCode,aryIds);
	}
	
	private String getBoCodeByForm(String formKey){
		Form form = bpmFormManager.getMainByFormKey(formKey);
		if(form==null){
			return "";
		}
		FormMeta formDef= bpmFormdefManager.get(form.getDefId());
		List<String> boCode = bpmFormdefManager.getBOCodeByFormId(formDef.getId());
		
		if(boCode.size()!=1) throw new RuntimeException(formKey+"表单所对应的BO数据不支持修改操作！");
		return boCode.get(0);
	}

	@Override
	public JsonNode getList(String formKey, Map<String, Object> param) throws IOException {
		String boCode =  getBoCodeByForm(formKey);
		BoDataHandler boDataHandler= boInstanceFactory.getBySaveType("database");
		
		List<Map<String, Object>> list =boDataHandler.getList(boCode,param);
		JsonNode jsonArray = JsonUtil.toJsonNode(list);
		return jsonArray;
	}
}
