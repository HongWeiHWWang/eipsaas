package com.hotent.form.persistence.manager.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.CachePut;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.cache.annotation.FirstCache;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.SystemException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.template.impl.FreeMarkerEngine;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.DateUtil;
import com.hotent.bo.bodef.BoDefService;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoDefXml;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.bo.util.BoUtil;
import com.hotent.form.enums.FormType;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormField;
import com.hotent.form.model.FormHistory;
import com.hotent.form.model.FormHistoryRecord;
import com.hotent.form.model.FormImportXml;
import com.hotent.form.model.FormMeta;
import com.hotent.form.model.FormPrintTemplate;
import com.hotent.form.model.FormRight;
import com.hotent.form.model.FormTemplate;
import com.hotent.form.model.FormXml;
import com.hotent.form.param.FormPreviewDataParam;
import com.hotent.form.persistence.dao.FormDao;
import com.hotent.form.persistence.manager.FormDataTemplateManager;
import com.hotent.form.persistence.manager.FormFieldManager;
import com.hotent.form.persistence.manager.FormHistoryManager;
import com.hotent.form.persistence.manager.FormHistoryRecordManager;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.persistence.manager.FormPrintTemplateManager;
import com.hotent.form.persistence.manager.FormRightManager;
import com.hotent.form.persistence.manager.FormTemplateManager;
import com.hotent.form.service.FormService;
import com.hotent.form.util.FormUtil;
import com.hotent.form.util.FreeMakerUtil;
import com.hotent.form.util.JsoupUtil;
import com.hotent.form.vo.BpmFormVo;
import com.hotent.i18n.util.I18nUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;

/**
 * 表单管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("bpmFormManager")
@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class FormManagerImpl extends BaseManagerImpl<FormDao, Form> implements FormManager {
	@Resource
	FormFieldManager formFieldManager;
	@Resource
	FormTemplateManager formTemplateManager;
	@Resource
	FormDataTemplateManager dataTemplateManager;
	@Resource
	FreeMarkerEngine freemarkEngine;
	@Resource
	BoDefService boDefService;
	@Resource
	FormMetaManager formMetaManager;
	@Resource
	FormHistoryManager formHistoryManager;
	@Resource
	PortalFeignService portalFeignService;
	@Resource
	BoDefManager boDefManager;
    @Resource
    FormHistoryRecordManager formHistoryRecordManager;
	@Resource
	BoDefManager bODefManager;
	@Resource
	FormService formService;
	@Resource
	FormRightManager bpmFormRightManager;
	@Resource
	FormPrintTemplateManager formPrintTemplateManager;

    @Override
	public IPage<Form> getFormQueryList(QueryFilter<Form> queryFilter) throws Exception {
		return baseMapper.getFormQueryList(convert2IPage(queryFilter.getPageBean()), convert2Wrapper(queryFilter, currentModelClass()));
	}

	@Override
	public Form get(Serializable entityId) {
		Form bpmForm = super.get(entityId);
		String html=bpmForm.getFormHtml();
		// i18n格式化表单
		String formHtml = I18nUtil.replaceTemp(html, StringPool.FORM_REG, LocaleContextHolder.getLocale());
		bpmForm.setFormHtml(formHtml);
		return bpmForm;
	}

	@Override
	public String getHtml(String formId, String mainFieldTemplate, String subFieldListTemplate) throws Exception {
		String[] tableNameArray = mainFieldTemplate.split(",");
		String[] templateAliasArray = subFieldListTemplate.split(",");

		FormMeta bpmFormdef = formMetaManager.get(formId);
		String html = "";
		if (bpmFormdef != null) {
			JsonNode fieldList = bpmFormdef.getFieldList();
			for (int i = 0; i < tableNameArray.length; i++) {
				String tableName = tableNameArray[i];

				JsonNode tableField = null; // 获取表对象
				for (int j = 0; j < fieldList.size(); j++) {
					JsonNode table = fieldList.get(j);
					if (tableName.equals(table.get("name"))) {
						tableField = table;
						break;
					}
				}
				// 当前模板
				FormTemplate template = formTemplateManager.getByTemplateAlias(templateAliasArray[i]);
				String macroTemplate = formTemplateManager.getByTemplateAlias(template.getMacrotemplateAlias())
						.getHtml();
				if (tableField == null || template == null)
					continue;

				boolean isSub = tableField.get("type").equals("sub");
				ArrayNode fieldLists = null;
				if (isSub) {
					fieldLists = (ArrayNode) tableField.get("children");
				} else {
					JsonNode expand = JsonUtil.toJsonNode(bpmFormdef.getExpand());
					JsonNode separators = null;
					if (BeanUtils.isNotEmpty(expand)) {
						separators = expand.get("separators");
					}
					getFieldList(fieldLists, tableField.get("children"), separators);
				}

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("table", tableField);
				map.put("isSub", isSub);
				map.put("mainGroup", "基本信息");
				map.put("formDesc", tableField.get("desc"));
				map.put("fieldList", fieldLists);
				map.put("ganged", bpmFormdef.getGanged());
				/*
				 * FreeMakerUtil freeMakerUtil = new FreeMakerUtil(); map.put("util",
				 * freeMakerUtil);
				 */
				html += freemarkEngine.parseByTempName(macroTemplate + template.getHtml(), map);

			}
		}
		return html;
	}

	private void getFieldList(ArrayNode fieldLists, JsonNode fields, JsonNode separators) {
		if (BeanUtils.isNotEmpty(fields)) {
			ArrayNode basefields = JsonUtil.getMapper().createObjectNode().arrayNode();
			ObjectNode mainSep = JsonUtil.getMapper().createObjectNode();
			if (BeanUtils.isNotEmpty(separators)) {
				for (int i = 0; i < separators.size(); i++) {
					ObjectNode oj = (ObjectNode) separators.get(i);
					oj.put("fields", JsonUtil.getMapper().createObjectNode());
					if (separators.get(i).get("key") != null) {
						mainSep = (ObjectNode) separators.get(i);
					}
				}
			}
			for (int i = 0; i < fields.size(); i++) {
				JsonNode obj = fields.get(i);
				String separator = obj.get("separator").asText();
				if (StringUtil.isZeroEmpty(separator)) {
					basefields.arrayNode();
				} else {
					for (int m = 0; m < separators.size(); m++) {
						ArrayNode sepObj = (ArrayNode) separators.get(m);
						if (separator.equals(sepObj.get("key"))) {
							((ArrayNode) sepObj.get("fields")).add(obj);
						}
					}
				}
			}
			mainSep.set("fields", basefields);
			fieldLists.add(mainSep);
			for (int m = 0; m < separators.size(); m++) {
				ArrayNode sepObj = (ArrayNode) separators.get(m);
				if (BeanUtils.isNotEmpty(sepObj.get("fields")) && !sepObj.get("key").equals("0")) {
					fieldLists.add(sepObj);
				}
			}
		}
	}

	@Override
	public Form getMainByFormKey(String formKey) {
		return baseMapper.getMainByFormKey(formKey);
	}

	@Override
	public List<Form> getByFormKey(String formKey) {
		return baseMapper.getByFormKey(formKey);
	}

	@Override
	public List<Form> getPrintByFormKey(String formKey) {
		return baseMapper.getPrintByFormKey(formKey);
	}

	@Override
	public List<Form> getByBoCodes(List<String> codes, String formType, QueryFilter<Form> filter) {
		List<Form> list = null;
		try {
			Map map = new HashMap();
			map.put("codes", codes);
			map.put("formType", formType);
			map.put("formKey", filter.getParams().get("formKey"));
			map.put("name", filter.getParams().get("name"));
			list = baseMapper.getByBoCodes(map);
		} catch (SystemException e) {
			e.printStackTrace();
			list = new ArrayList<Form>();
		}
		return list;
	}

	@Override
	public Integer getBpmFormCountsByFormKey(String formKey) {
		return baseMapper.getBpmFormCountsByFormKey(formKey);
	}

	@Override
	@Transactional
	public void newVersion(String formId) throws Exception {
		Form bpmform = this.get(formId);
		createNewVersionForm(bpmform);
	}

	// 拷贝表单权限
	private void createNewVersionForm(Form bpmform) {
		if (bpmform != null) {
			Integer rtn = baseMapper.getMaxVersionByFormKey(bpmform.getFormKey());
			String newFormId = UniqueIdUtil.getSuid();
			// 创建新的版本
			Form newBpmForm = bpmform;
			newBpmForm.setId(newFormId);
			newBpmForm.setIsMain('N');
			newBpmForm.setStatus(Form.STATUS_DRAFT);
			newBpmForm.setVersion(rtn + 1);
			this.create(newBpmForm);
			publish(newFormId);
			setDefaultVersion(newFormId, newBpmForm.getFormKey());
		}
	}

	@Override
	@Transactional
	public void setDefaultVersion(String formId, String formKey) {
		baseMapper.updNotDefaultByFormKey(formKey);
		baseMapper.updDefaultByFormId(formId);
		Form form = super.get(formId);
		if(BeanUtils.isNotEmpty(form)){
			FormMeta byKey = formMetaManager.getByKey(formKey);
			byKey.setExpand(form.getExpand());
			formMetaManager.update(byKey);
		}
		removeFromCache(formKey);
	}
	
	/**
	 * 通过formKey删除缓存中的bpmForm
	 * @param formKey
	 */
	private void removeFromCache(String formKey) {
		FormManagerImpl bean = AppUtil.getBean(getClass());
		bean.delFromCache(formKey);
	}
	
	@CacheEvict(value = "form:bpmForm", key="#formKey")
	protected void delFromCache(String formKey) {}

	@Override
	@Transactional
	public void publish(String formId) {
		Form formDef = this.get(formId);
		if (formDef != null) {
			formDef.setStatus(Form.STATUS_DEPLOY);
			this.update(formDef);
		}
	}

	@Override
	public List<Form> getByDefId(String defId) {
		return baseMapper.getByDefId(defId);
	}

	@Override
	@Transactional
	public void importForms(ObjectNode obj,String typeId) {
		String formXmlStr = obj.get("formXmlStr").asText();
		try {
			// bo导入
			String boXmlStr = obj.get("boXmlStr").asText();
			if (StringUtil.isNotEmpty(boXmlStr)) {
				BoDefXml boXml = (BoDefXml) JAXBUtil.unmarshall(boXmlStr, BoDefXml.class);
				List<BoDef> boDefs = boXml.getDefList();
				Map<String,String> nameMap = getEntIdMap(boDefs, null);
				List<BoDef> importBoDef = boDefService.importBoDef(boDefs);
				// 表单导入
				importByFormXml(formXmlStr, typeId, importBoDef, nameMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("表单导入失败" + e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public void importByFormXml(String xml,List<BoDef> importBoDef,Map<String,String> nameMap) throws Exception {
		FormImportXml formImportXml = (FormImportXml) JAXBUtil.unmarshall(xml, FormImportXml.class);
		List<FormXml> formXmlList = formImportXml.getFormXmlList();
		 Map<String,String> entIdMap = this.getEntIdMap(importBoDef,nameMap);
		for (FormXml formXml : formXmlList) {
			FormMeta formDef = formXml.getBpmFormDef();
			List<String> boCodes = formXml.getBoCodes();
			Form form = formXml.getBpmForm();
			// 更新bpmFormDef
			{
				FormMeta oldFormDef = formMetaManager.getByKey(formDef.getKey());
				// 重新构建bo关系json
				Map<String, BoDef> boDefMap = new HashMap<String, BoDef>();
				ObjectNode expand = (ObjectNode) JsonUtil.toJsonNode(formDef.getExpand());
				ArrayNode boDefList = JsonUtil.getMapper().createArrayNode();
				for (String boCode : boCodes) {
					BoDef boDef = boDefService.getByAlias(boCode);
					boDefMap.put(boCode, boDef);

					ObjectNode boDefJson = JsonUtil.getMapper().createObjectNode();
					boDefJson.put("id", boDef.getId());
					boDefJson.put("alias", boDef.getAlias());
					boDefJson.put("desc", boDef.getDescription());
					boDefList.add(boDefJson);
				}
				expand.set("boDefList", boDefList);
				//对expand的entId进行修改
				if(BeanUtils.isNotEmpty(importBoDef)){
					updateEntId(importBoDef,expand);
				}

				formDef.setExpand(expand.toString());
				if (oldFormDef != null) {
					formDef.setId(oldFormDef.getId());
					formMetaManager.updateByImport(formDef, entIdMap);
					ThreadMsgUtil.addMsg("表单元数据定义 ”" + formDef.getName() + "“[" + formDef.getKey() + "] 已经存在，更新成功！");
				} else {
					formDef.setId(UniqueIdUtil.getSuid());
					formMetaManager.createByImport(formDef, entIdMap);
					ThreadMsgUtil.addMsg("表单元数据定义 “" + formDef.getName() + "”[" + formDef.getKey() + "] 添加成功！");
				}

				formMetaManager.deleteBpmFormBo(formDef.getId());
				for (JsonNode obj : boDefList) {
					JsonNode boDefJson = obj;
					// 创建表单和BO的关联关系。
					formMetaManager.createBpmFormBo(UniqueIdUtil.getSuid(), boDefJson.get("id").asText() + "", formDef.getId());
				}
			}
			/**
			 * 处理表单列表 存在则发布新版本。否则创建。设置默认版本并发布。
			 */
			{
				Form oldForm = getMainByFormKey(form.getFormKey());
				form.setDefId(formDef.getId());
				form.setStatus(Form.STATUS_DEPLOY);
				if (oldForm != null) {
					form.setId(oldForm.getId());
					createNewVersionForm(form);
					ThreadMsgUtil.addMsg("表单”" + form.getName() + "”[" + form.getFormKey() + "] 已经存在，更新并发布新版本成功！");
				} else {
					form.setVersion(1);
					form.setIsMain('Y');
					create(form);
					ThreadMsgUtil.addMsg("表单“" + form.getName() + "“[" + form.getFormKey() + "] 保存并发布成功！");
				}
			}

			// 检测表单源数据定义对应的bpmFormField是否存在，如果不存在则创建
			List<FormField> fields = formFieldManager.getByFormId(formDef.getId());
			if (BeanUtils.isEmpty(fields)) {
				formMetaManager.createFields(formDef,entIdMap);
			}
		}
	}

	@Transactional
	public void importByFormXml(String xml, String typeId,List<BoDef> importBoDef,Map<String,String> nameMap) throws Exception {
		FormImportXml formImportXml = (FormImportXml) JAXBUtil.unmarshall(xml, FormImportXml.class);
		List<FormXml> formXmlList = formImportXml.getFormXmlList();
        ObjectNode sysType = portalFeignService.getSysTypeById(typeId);
        Map<String,String> entIdMap = this.getEntIdMap(importBoDef,nameMap);
		for (FormXml formXml : formXmlList) {
			FormMeta formDef = formXml.getBpmFormDef();
			List<String> boCodes = formXml.getBoCodes();
			Form form = formXml.getBpmForm();
			if (BeanUtils.isNotEmpty(sysType)) {
				form.setTypeId(sysType.get("id").asText());
				form.setTypeName(sysType.get("name").asText());
			}
			// 更新bpmFormDef
			{
				FormMeta oldFormDef = formMetaManager.getByKey(formDef.getKey());

				// 重新构建bo关系json
				Map<String, BoDef> boDefMap = new HashMap<String, BoDef>();
				ObjectNode expand = (ObjectNode) JsonUtil.toJsonNode(formDef.getExpand());


				//对expand的entId进行修改
				updateEntId(importBoDef,expand);



				ArrayNode boDefList = JsonUtil.getMapper().createArrayNode();
				for (String boCode : boCodes) {
					BoDef boDef = boDefService.getByAlias(boCode);
					boDefMap.put(boCode, boDef);

					ObjectNode boDefJson = JsonUtil.getMapper().createObjectNode();
					boDefJson.put("id", boDef.getId());
					boDefJson.put("alias", boDef.getAlias());
					boDefJson.put("desc", boDef.getDescription());
					boDefList.add(boDefJson);

				}
				expand.set("boDefList", boDefList);
				formDef.setExpand(expand.toString());
				if (oldFormDef != null) {
					formDef.setId(oldFormDef.getId());
					formMetaManager.updateByImport(formDef, entIdMap);
					ThreadMsgUtil.addMsg("表单元数据定义 ”" + formDef.getName() + "“[" + formDef.getKey() + "] 已经存在，更新成功！");
				} else {
					formDef.setId(UniqueIdUtil.getSuid());
					formMetaManager.createByImport(formDef, entIdMap);
					ThreadMsgUtil.addMsg("表单元数据定义 “" + formDef.getName() + "”[" + formDef.getKey() + "] 添加成功！");
				}

				formMetaManager.deleteBpmFormBo(formDef.getId());
				for (JsonNode obj : boDefList) {
					JsonNode boDefJson = obj;
					// 创建表单和BO的关联关系。
					formMetaManager.createBpmFormBo(UniqueIdUtil.getSuid(), boDefJson.get("id").asText() + "",
							formDef.getId());
				}
			}
			/**
			 * 处理表单列表 存在则发布新版本。否则创建。设置默认版本并发布。
			 */
			{
				Form oldForm = getMainByFormKey(form.getFormKey());

				form.setDefId(formDef.getId());
				form.setStatus(Form.STATUS_DEPLOY);
				if (oldForm != null) {
					form.setId(oldForm.getId());
					form.setUpdateTime(LocalDateTime.now());
					createNewVersionForm(form);
					ThreadMsgUtil.addMsg("表单”" + form.getName() + "”[" + form.getFormKey() + "] 已经存在，更新并发布新版本成功！");
				} else {
					form.setVersion(1);
					form.setIsMain('Y');
					form.setTypeId(typeId);
					form.setUpdateTime(LocalDateTime.now());
					create(form);
					ThreadMsgUtil.addMsg("表单“" + form.getName() + "“[" + form.getFormKey() + "] 保存并发布成功！");
				}
			}

			// 检测表单源数据定义对应的bpmFormField是否存在，如果不存在则创建
			List<FormField> fields = formFieldManager.getByFormId(formDef.getId());
			if (BeanUtils.isEmpty(fields)) {
				formMetaManager.createFields(formDef,entIdMap);
			}
		}

	}

	private Map<String,String> getEntIdMap(List<BoDef> importBoDef,Map<String,String> nameMap){
		 Map<String,String> entIdMap = new HashMap<String,String>();
		if(BeanUtils.isNotEmpty(importBoDef)){
        	for (BoDef def : importBoDef) {
				if(BeanUtils.isNotEmpty(def.getBoEnt())){
					//主Ent
					BoEnt ent = def.getBoEnt();
					if(BeanUtils.isNotEmpty(ent.getBoAttrList())){
						if(BeanUtils.isNotEmpty(nameMap)){
							entIdMap.put(nameMap.get(ent.getName()), ent.getId());
						}else{
							entIdMap.put(ent.getName(), ent.getBoAttrList().get(0).getEntId());
						}
					}
					//子ent
					if(BeanUtils.isNotEmpty(ent.getChildEntList())){
						List<BoEnt> subEnts = ent.getChildEntList();
						for (BoEnt subEnt : subEnts) {
							if(BeanUtils.isNotEmpty(subEnt.getBoAttrList())){
								if(BeanUtils.isNotEmpty(nameMap)){
									entIdMap.put(nameMap.get(subEnt.getName()), subEnt.getId());
								}else{
									entIdMap.put(subEnt.getName(), subEnt.getBoAttrList().get(0).getEntId());
								}
							}
							//孙ent
							if(BeanUtils.isNotEmpty(subEnt.getChildEntList())){
								List<BoEnt> sunEnts = subEnt.getChildEntList();
								for (BoEnt sunEnt : sunEnts) {
									if(BeanUtils.isNotEmpty(sunEnt.getBoAttrList())){
										if(BeanUtils.isNotEmpty(subEnt.getBoAttrList())){
											if(BeanUtils.isNotEmpty(nameMap)){
												entIdMap.put(nameMap.get(sunEnt.getName()), sunEnt.getId());
											}else{
												entIdMap.put(sunEnt.getName(), sunEnt.getBoAttrList().get(0).getEntId());
											}
										}
									}

								}
							}
						}
					}
				}
			}
        }
		return entIdMap;
	}

	private void updateEntId(List<BoDef> importBoDef,ObjectNode expand) {
		Map<String, BoEnt> boEntMap=new HashMap<>();
		for (BoDef boDef : importBoDef) {
			boEntMap.put(boDef.getAlias(), boDef.getBoEnt());
		}
		JsonNode list = expand.get("list");
		for (JsonNode jsonNode : list) {
			JsonNode jsonNode2 = jsonNode.get("boDefAlias");
			if(BeanUtils.isEmpty(jsonNode2)){
				continue;
			}
			String boDefAlias = jsonNode2.asText();
			 ObjectNode objectNode = (ObjectNode) jsonNode;
			 objectNode.put("entId", boEntMap.get(boDefAlias).getId());
		}

	}

	@Override
	public Map<String, String> exportForms(List<String> idList, boolean containBo) {
		BoDefXml bodefXml = new BoDefXml();
		FormImportXml formImport = new FormImportXml();
		Map<String, String> map = new HashMap<String, String>();
		// 取出表单
		for (String formId : idList) {
			FormXml formXml = new FormXml();

			Form form = this.get(formId);
			FormMeta formDef = formMetaManager.get(form.getDefId());
			List<String> boCodes = formMetaManager.getBOCodeByFormId(form.getDefId());

			// 表单 和表单定义
			formXml.setBpmForm(form);
			formXml.setBpmFormDef(formDef);
			formXml.setBoCodes(boCodes);
			if (containBo) {
				List<String> boIds = formMetaManager.getBODefIdByFormId(form.getDefId());
				for (String boId : boIds) {
					BoDef bodef = boDefService.getByDefId(boId);
					bodefXml.addBodef(bodef);
				}
			}

			formImport.addFormXml(formXml);
		}

		try {
			map.put("form.xml", JAXBUtil.marshall(formImport, FormImportXml.class));
			if (containBo)
				map.put("bo.xml", JAXBUtil.marshall(bodefXml, BoDefXml.class));
		} catch (JAXBException e) {
			throw new RuntimeException("导出表单失败" + e.getMessage(), e);
		}
		return map;
	}

	@Override
	public String genByField(String defId, String attrId, String formType) {
		FormMeta def = formMetaManager.get(defId);
		String html = "";
		if (def != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (def.getExpand() != null) {
				try {
					JsonNode expand = JsonUtil.toJsonNode(def.getExpand());
					JsonNode tables = expand.get("fields");
					JsonNode field = JsonUtil.getMapper().createObjectNode();
					Boolean isSub = false;
					for (JsonNode table : tables) {
						JsonNode fields = table.get("children");
						for (int i = 0; i < fields.size(); i++) {
							JsonNode f = fields.get(i);
							if (f.get("boAttrId").equals(attrId)) {
								field = f;
								isSub = !table.get("type").equals("main");
							}
						}
					}
					map.put("isSub", isSub);
					map.put("field", field);
				} catch (IOException e) {
					throw new BaseException(e.getMessage());
				}
			}

			String macroTemplate = formTemplateManager
					.getByTemplateAlias("mobile".equals(formType) ? "mobileFieldMacro" : "fieldControl").getHtml();

			String template = "<@input field=field isSub=isSub/>" + macroTemplate;
			try {
				html = freemarkEngine.parseByTempName(template, map);
			} catch (Exception e) {
				throw new BaseException(e.getMessage());
			}
		}
		return html;
	}

	@Override
	@Transactional
	public void update(Form bpmForm) {
		super.update(bpmForm);
		removeFromCache(bpmForm.getFormKey());
	}

	@Override
	@Transactional
	public void remove(Serializable id) {
		Form bpmForm = this.get(id);
		removeFromCache(bpmForm.getFormKey());
		super.remove(id);
	}

	@Override
	@Transactional
	public void remove(String[] aryIds) {
		for (String id : aryIds) {
			Form bpmForm = this.get(id);
			if (BeanUtils.isNotEmpty(bpmForm)) {
				String formKey = bpmForm.getFormKey();
				if ("Y".equals(String.valueOf(bpmForm.getIsMain()))) {
					List<Form> list = baseMapper.getByFormKey(formKey);
					for (Form bpmform : list) {
						this.remove(bpmform.getId());
					}
					//删除表单后，删除表单的业务数据模板
					dataTemplateManager.removeByFormKey(formKey);
				} else {
					this.remove(id);
				}

				String fromDefId = bpmForm.getDefId();
				formMetaManager.remove(fromDefId);
				// 删除表单和元数据的时候。一并删除元数据和业务对象的关联关系
				formMetaManager.deleteBpmFormBo(fromDefId);
				removeFromCache(bpmForm.getFormKey());
			}
		}
	}

	@Override
	@Transactional
	public CommonResult<String> saveFormJs(String formId, String divJs, String formHtml) {
		Form form = this.get(formId);
        if(BeanUtils.isNotEmpty(form)){
            form.setDiyJs(divJs);
            form.setFormHtml(formHtml);
            this.update(form);//更新表单数据
            //根据表单ID查询表单HTML内容（表单HTML数据历史记录）
            FormHistoryRecord entity = new FormHistoryRecord();
            entity.setId(UniqueIdUtil.getSuid());
            entity.setFormId(formId);
            entity.setFormHtml(formHtml);
            formHistoryRecordManager.create(entity);//根据表单ID创建表单HTML内容（表单HTML数据历史记录）
            removeFromCache(form.getFormKey());
            return new CommonResult<>(true, "保存成功");
        }else{
            return new CommonResult<>(false, "请先保存表单内容再添加自定义脚本");
        }
	}

    @Override
    @Transactional
    public CommonResult<String> updateFormHistoryRecord(String formId, String formHtml) {
        Form form = this.get(formId);
        if(BeanUtils.isNotEmpty(form)){
            form.setFormHtml(formHtml);
            this.update(form);//更新表单数据
            removeFromCache(form.getFormKey());
            return new CommonResult<>(true, "恢复成功");
        }else{
            return new CommonResult<>(false, "请先保存表单内容");
        }
    }


    @Override
	@Transactional
	public void saveDesign(String formData) throws Exception {
		Form form = new Form();
		FormMeta bpmFormdef = new FormMeta();

		JsonNode paramNode = JsonUtil.toJsonNode(formData);

		JsonNode formJson = paramNode.get("form");
		JsonNode expandJson = paramNode.get("expand");

		String expand = JsonUtil.toJson(expandJson);
		String ganged = paramNode.get("ganged").asText();
		String opinion = paramNode.get("opinion").asText();

		String rev = paramNode.get("rev").asText();
		String formId = formJson.get("formId").asText();
		// 是否发布新版本表单
		String newType = formJson.get("newType").asText();

		if (StringUtil.isNotEmpty(formId)) {
			form = this.get(formId);
			bpmFormdef = formMetaManager.get(form.getDefId());
			JsonNode bpmFormdefNode = paramNode.get("formDef");
			bpmFormdef.setMacroAlias(bpmFormdefNode.get("macroAlias").asText());
			bpmFormdef.setMainAlias(bpmFormdefNode.get("mainAlias").asText());
			bpmFormdef.setSubEntity(bpmFormdefNode.get("subEntity").asText());
		} else {
			JsonNode bpmFormdefNode = paramNode.get("formDef");
			bpmFormdef.setType(bpmFormdefNode.get("type").asText());
			bpmFormdef.setTypeId(bpmFormdefNode.get("typeId").asText());
			bpmFormdef.setName(bpmFormdefNode.get("name").asText());
			bpmFormdef.setKey(bpmFormdefNode.get("key").asText());
			bpmFormdef.setDesc(bpmFormdefNode.get("desc").asText());
			bpmFormdef.setMacroAlias(bpmFormdefNode.get("macroAlias").asText());
			bpmFormdef.setMainAlias(bpmFormdefNode.get("mainAlias").asText());
			bpmFormdef.setSubEntity(bpmFormdefNode.get("subEntity").asText());
			String formKey = bpmFormdef.getKey();
			if (formMetaManager.getByKey(formKey) != null) {
				throw new RuntimeException("表单已经存在！key:" + formKey);
			}
			form.setName(formJson.get("name").asText());
			form.setFormKey(formJson.get("formKey").asText());
			form.setTypeId(bpmFormdef.getTypeId());
			form.setTypeName(bpmFormdef.getType());
			form.setFormType(formJson.get("formType").asText());
		}
		bpmFormdef.setGanged(ganged);
		bpmFormdef.setExpand(expand);
		if (BeanUtils.isNotEmpty(bpmFormdef.getId())) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", bpmFormdef.getId());
			map.put("rev", rev);
			FormMeta bpmFormDef1 = formMetaManager.getFormDefByRev(map);
			if (BeanUtils.isNotEmpty(bpmFormDef1)) {
				formMetaManager.update(bpmFormdef);
			} else {
				throw new RuntimeException("此表单不是最新版本，请重新获取再修改");
			}
		} else {
			bpmFormdef.setId(UniqueIdUtil.getSuid());
			formMetaManager.create(bpmFormdef);
		}
		formMetaManager.updateOpinionConf(bpmFormdef.getId(), opinion);
		String formHtml = "";
		String formI18nName = expandJson.has("designFormName") ? expandJson.get("designFormName").asText()
				: form.getName();
		if (FormType.MOBILE.value().equals(form.getFormType())) {
			formHtml = getMobileDesignHtml(expandJson, formI18nName, ganged);
		} else {
			formHtml = getDesignHtml(expandJson, formI18nName, ganged, JsonUtil.toJsonNode(bpmFormdef));
		}
		form.setFormHtml(formHtml);
		if (StringUtil.isEmpty(form.getId())) {
			form.setDefId(bpmFormdef.getId());
			List<Form> bpmForm = this.getByFormKey(form.getFormKey());
			if (BeanUtils.isNotEmpty(bpmForm))
				throw new RuntimeException("KEY【" + form.getFormKey() + "】对应的表单已存在");
			form.setId(UniqueIdUtil.getSuid());
			this.create(form);
		} else {
			if (newType.equals("new")) {
				// 发布新版
				Integer rtn = baseMapper.getMaxVersionByFormKey(form.getFormKey());
				String newFormId = UniqueIdUtil.getSuid();

				// 设置新版本属性
				Form newBpmForm = form;
				newBpmForm.setId(newFormId);
				newBpmForm.setIsMain('N');
				newBpmForm.setStatus(Form.STATUS_DRAFT);
				newBpmForm.setVersion(rtn + 1);
				this.create(newBpmForm);
				publish(newFormId);
				setDefaultVersion(newFormId, newBpmForm.getFormKey());
			} else {
				form.setName(formJson.get("name").asText());
				this.update(form);
			}
		}
		FormHistory bpmFormDefHi = new FormHistory(form); // 保持表单的操作记录
		formHistoryManager.create(bpmFormDefHi);

	}

	private void updatePermissionm(String asText) throws IOException {
		//1.获取这个表单的所有旧数据
		QueryFilter<FormRight> queryFilter= QueryFilter.build();
		queryFilter.addFilter("FORM_KEY_", asText, QueryOP.EQUAL);
		List<FormRight> queryNoPage = bpmFormRightManager.queryNoPage(queryFilter);

		//获取非实例表单
		JsonNode Default = bpmFormRightManager.getDefaultByFormDefKey(asText, false);
		//获取实例表单权限
		JsonNode instaPermissionm = bpmFormRightManager.getDefaultByFormDefKey(asText, false);

		for (FormRight formRight : queryNoPage) {
			String permission = formRight.getPermission();
			JsonNode oldPermission = JsonUtil.toJsonNode(permission);
			JsonNode oldTable = oldPermission.get("table");
			JsonNode newPermission=null;

			if(formRight.getPermissionType()==1){
				newPermission=JsonUtil.toJsonNode(JsonUtil.toJson(Default));
			}else{
				newPermission=JsonUtil.toJsonNode(JsonUtil.toJson(instaPermissionm));
			}
			JsonNode newTable = newPermission.get("table");
			//比较表是否有差异.
			Iterator<String> fieldNames = newTable.fieldNames();


			while(fieldNames.hasNext()){
				String key = fieldNames.next();
				if(BeanUtils.isNotEmpty(oldTable.get(key))){
					//对比字段
					JsonNode oldFields = oldTable.get(key).get("fields");
					JsonNode newFields = newTable.get(key).get("fields");

					if (BeanUtils.isEmpty(newFields)) {
						continue;
					}
					
					//字表的添加、删除、权限不重置
					JsonNode oldRights = oldTable.get(key).get("rights");
					JsonNode newRights = newTable.get(key).get("rights");
					if(BeanUtils.isNotEmpty(oldRights)){
						Iterator<String> newFieldsKey = newRights.fieldNames();
						while(newFieldsKey.hasNext()){
							String FieldsKey = newFieldsKey.next();
							if(BeanUtils.isNotEmpty(oldRights.get(FieldsKey))){
								ObjectNode newField=(ObjectNode)newRights;
								newField.set(FieldsKey, oldRights.get(FieldsKey));
							}
						}
					}

					Iterator<String> newFieldsKey = newFields.fieldNames();
					while(newFieldsKey.hasNext()){
						String FieldsKey = newFieldsKey.next();
						if(BeanUtils.isNotEmpty(oldFields.get(FieldsKey))){
							ObjectNode newField=(ObjectNode)newFields;
							newField.set(FieldsKey, oldFields.get(FieldsKey));
						}
					}
				}

			}
			String json = JsonUtil.toJson(newPermission);
			if(!json.equals(formRight.getPermission())){
				formRight.setPermission(json);
				bpmFormRightManager.update(formRight);
			}

		}
	}

	@Override
	@Transactional
	public void saveFormDef(BpmFormVo bpmFormVo) throws Exception {
		FormMeta bpmFormdef = bpmFormVo.getBpmFormDef();
		Form form = bpmFormVo.getBpmForm();
		form.setExpand(bpmFormdef.getExpand());
		if (StringUtil.isEmpty(bpmFormdef.getId())) {
			formMetaManager.create(bpmFormdef);
			form.setDefId(bpmFormdef.getId());
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("id", bpmFormdef.getId());
			map.put("rev", bpmFormdef.getRev());
			FormMeta bpmFormDef1 = formMetaManager.getFormDefByRev(map);
			if (BeanUtils.isEmpty(bpmFormDef1)) {
				throw new BaseException("此表单不是最新版本，请重新获取再修改");
			}
			formMetaManager.update(bpmFormdef);
		}

		genFromHtml(bpmFormVo);

		if (StringUtil.isEmpty(form.getId())) {
			form.setStatus(Form.STATUS_DRAFT);
			form.setUpdateTime(DateUtil.getCurrentDate());
			this.create(form);
		} else if ("newForm".equals(bpmFormVo.getNewForm())) {
			//表单为草稿状态时，发布新版，
			if(Form.STATUS_DRAFT.equals(form.getStatus())) {
				publish(form.getId());
			}
			// 发布新版
			Integer rtn = baseMapper.getMaxVersionByFormKey(form.getFormKey());
			String newFormId = UniqueIdUtil.getSuid();

			setDefaultVersion(newFormId, form.getFormKey());
			// 设置新版本属性
			Form newBpmForm = form;
            Form oldBpmForm = baseMapper.selectById(form.getId());
            if(StringUtil.isNotEmpty(oldBpmForm.getDiyJs())){
                newBpmForm.setDiyJs(oldBpmForm.getDiyJs());
            }
			newBpmForm.setId(newFormId);
			newBpmForm.setIsMain('Y');
			newBpmForm.setVersion(rtn + 1);
			newBpmForm.setUpdateTime(DateUtil.getCurrentDate());
			newBpmForm.setStatus(Form.STATUS_DEPLOY);
			this.create(newBpmForm);
		} else {
			this.update(form);
			FormHistory bpmFormDefHi = new FormHistory(bpmFormVo.getBpmForm()); // 保持表单的操作记录
			formHistoryManager.create(bpmFormDefHi);
		}
		//考虑一种场景,如果流程已经设置完成后,又去建模中新加了一个子表,并且在表单中设置了这个子表,此时启动流程表单会因为Permission没有设置而前端报错
		//在这里需要获取旧的Permissionm与新的Permission进行比对并重新设置
		updatePermissionm(bpmFormVo.getBpmForm().getFormKey());
	}

	@Override
	@Transactional
	public void generateFrom(BpmFormVo bpmFormVo) throws Exception {
		genFromHtml(bpmFormVo);
		formMetaManager.update(bpmFormVo.getBpmFormDef());
		this.update(bpmFormVo.getBpmForm());
		FormHistory bpmFormDefHi = new FormHistory(bpmFormVo.getBpmForm()); // 保持表单的操作记录
		formHistoryManager.create(bpmFormDefHi);
	}

	private String genFromHtml(BpmFormVo bpmFormVo) throws Exception {
		FormMeta bpmFormDef = bpmFormVo.getBpmFormDef();
		String expand = bpmFormDef.getExpand();
		JsonNode expandJsonNode = JsonUtil.toJsonNode(expand);
		JsonNode field = expandJsonNode.get("list");
		List layoutList = new ArrayList();
		field.forEach(obj -> {
			try {
				layoutList.add(JsonUtil.toMap(JsonUtil.toJson(obj)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		FreeMakerUtil freeMakerUtil = new FreeMakerUtil();
		Map<String, Object> mapObj = new HashMap<>();
		mapObj.put("layoutList", layoutList);
		mapObj.put("expandMap", JsonUtil.toMap(expand));
		mapObj.put("util", freeMakerUtil);

		FormTemplate fieldControl = formTemplateManager.getByTemplateAlias("fieldControl");
		List<FormTemplate> bpmFormTpls = formTemplateManager.getTemplateType(FormTemplate.FORM_DESIGN);
		StringBuffer formDesignFtl = new StringBuffer(fieldControl.getHtml());
		for (FormTemplate bpmFormTemplate : bpmFormTpls) {
			formDesignFtl.append(bpmFormTemplate.getHtml());
		}
		String html = freemarkEngine.parseByTemplate(formDesignFtl.toString(), mapObj);
		bpmFormVo.getBpmForm().setFormHtml(html);
		return html;
	}

	@Override
	@Transactional
	public void pcForm2MobileForm(String formId) throws Exception {
		Form bpmForm = this.get(formId);
		FormMeta bpmFormDef = formMetaManager.get(bpmForm.getDefId());

		Form mainByFormKey = baseMapper.getMainByFormKey(bpmForm.getFormKey()+"mobile");
		if(BeanUtils.isNotEmpty(mainByFormKey)) {
			throw new BaseException(String.format("已经生成表单key为【%s】的表单",mainByFormKey.getFormKey()));
		}
		BpmFormVo bpmFormVo = new BpmFormVo();
		bpmForm.setId("");
		bpmForm.setFormKey(bpmForm.getFormKey()+"mobile");
		bpmForm.setFormType("mobile");
		bpmFormDef.setId("");
		bpmFormDef.setKey(bpmFormDef.getKey()+"mobile");
		bpmFormDef.setExpand(transformationMobileExpand(bpmFormDef.getExpand()));
		bpmFormVo.setBpmForm(bpmForm);
		bpmFormVo.setBpmFormDef(bpmFormDef);
		saveFormDef(bpmFormVo);
	}

	@Override
	public Map<String, Object> getBindRelation(String defId,String formKey) throws Exception {
		List<Map<String, Object>> boEnt = baseMapper.getBoEnt(defId);
		List<Map<String, String>> bpmDefinitionData = boDefManager.getBpmDefinitionData(boEnt.get(0).get("alias_").toString());
		Map<String, Object> map = new HashMap<>();
		map.put("bpmData",bpmDefinitionData);
		map.put("entData",boEnt);
		return map;
	}

	@Override
	public Map<String, Object> getFormData(String pcAlias, String mobileAlias) throws Exception {
		QueryWrapper<Form> queryWrapper = new QueryWrapper<>();
		Map<String,Object> map = new HashMap<>();
		if(StringUtil.isNotEmpty(pcAlias)) {
			QueryWrapper<Form> eq = queryWrapper.eq("form_key_", pcAlias).eq("form_type_", "pc").eq("is_main_","Y");
			List<Form> pcForms = baseMapper.selectList(eq);
			List<Map<String, Object>> boEnt = baseMapper.getBoEnt(pcForms.get(0).getDefId());
			map.put("pcEnt",boEnt.get(0));
		}
		if(StringUtil.isNotEmpty(mobileAlias)) {
			QueryWrapper<Form> eq = queryWrapper.eq("form_key_", mobileAlias).eq("form_type_", "mobile").eq("is_main_", "Y");
			List<Form> mobileForms = baseMapper.selectList(eq);
			List<Map<String, Object>> boEnt = baseMapper.getBoEnt(mobileForms.get(0).getDefId());
			map.put("mobileEnt",boEnt.get(0));
		}
		return map;
	}

	@Override
	public Map<String, Object> getPreviewDesignVueData(String formId) throws Exception {
		Form bpmForm = get(formId);
		FormMeta formDef = formMetaManager.get(bpmForm.getDefId());
		JsonNode jsonNode = JsonUtil.toJsonNode(formDef.getExpand());
		String bos = "";
		if (BeanUtils.isNotEmpty(jsonNode)) {
			bos = JsonUtil.toJson(jsonNode.get("boDefList"));
		}
		List<String> boCode = new ArrayList<String>();
		ArrayNode bosArray = JsonUtil.toBean(bos, ArrayNode.class);
		for (int i = 0; i < bosArray.size(); i++) {
			JsonNode obj = bosArray.get(i);
			BoDef boDef = bODefManager.get(obj.get("id").asText());
			if (BeanUtils.isNotEmpty(boDef)) {
				boCode.add(boDef.getAlias());
			}
		}
		List<BoData> boJson = formService.getBoDataByBoKeys(boCode);
		JsonNode object = BoUtil.hanlerData(boJson);
		JsonNode permissionConf = bpmFormRightManager.getByFormKey(bpmForm.getFormKey(), false);
		JsonNode permission = bpmFormRightManager.calcFormPermission(permissionConf);
		Map<String,Object> resultMap = new HashMap<String, Object>();

		resultMap.put("bpmForm", bpmForm);
		resultMap.put("permission", permission);
		resultMap.put("data", object);
		return resultMap;
	}

	@Override
	public Map<String, Object> getPreviewDesignData(FormPreviewDataParam param) throws Exception {
		ObjectNode expand = JsonUtil.getMapper().createObjectNode();
		String expandStr = "";
		Form bpmForm=new Form();
		String id = param.getId(),design = param.getDesign();
		ArrayNode bosArray = null;
		String formDefId = "";
		if(StringUtil.isNotEmpty(id)&&StringUtil.isEmpty(design)){
			bpmForm = get(id);
			if(BeanUtils.isNotEmpty(bpmForm)){
				FormMeta formDef = formMetaManager.get(bpmForm.getDefId());
				if(BeanUtils.isNotEmpty(formDef)){
					formDefId = formDef.getId();
					expandStr = formDef.getExpand();
					expand = JsonUtil.toBean(expandStr, ObjectNode.class);
				}
			}
		}else{
			ObjectNode designJson = JsonUtil.toBean(design, ObjectNode.class);
			expand = (ObjectNode) designJson.get("expand");
			expandStr = JsonUtil.toJson(expand);
			String ganged = JsonUtil.toJson(designJson.get("ganged"));
			String tableNames = "";
			String form = param.getForm();
			JsonNode formDefNode = designJson.get("formDef");
			if(StringUtil.isNotEmpty(form)){
				ObjectNode formNode = (ObjectNode) JsonUtil.toJsonNode(form);
				tableNames = formNode.get("name").asText();
			}
			String formHtml = "";
			if(FormType.MOBILE.value().equals(param.getFormType())){
				formHtml = getMobileDesignHtml(expand, tableNames, ganged);
			}else{
				formHtml = getDesignHtml(expand, tableNames, ganged,formDefNode);
			}
			if(StringUtil.isNotEmpty(formHtml)){
				bpmForm.setFormHtml(formHtml);
			}
		}

		bosArray = (ArrayNode) expand.get("boDefList");
		JsonNode permissionConf = bpmFormRightManager.getDefaultByDesign(formDefId,expandStr, false);
		JsonNode permission = bpmFormRightManager.calcFormPermission(permissionConf);


		List<String> boCode = new ArrayList<String>();
		for (int i = 0; i < bosArray.size(); i++) {
			JsonNode obj = bosArray.get(i);
			BoDef boDef = bODefManager.get(obj.get("id").asText());
			if(BeanUtils.isNotEmpty(boDef)){
				boCode.add(boDef.getAlias());
			}
		}

		//  只有一个BoData
		List<BoData> boJson = formService.getBoDataByBoKeys(boCode);

		JsonNode object = BoUtil.hanlerData(boJson);

		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("bpmForm", bpmForm);
		resultMap.put("permission", permission);
		resultMap.put("data", object);
		return resultMap;
	}

	@Override
	public JsonNode getRightData(String id, String defId, String formType) throws Exception {
		Form bpmForm=new Form();
		if(StringUtil.isNotEmpty(id)){
			bpmForm = get(id);
			formType =bpmForm.getFormType();
		}
		if(StringUtil.isNotEmpty(bpmForm.getDefId())){
			defId = bpmForm.getDefId();
		}
		FormMeta formDef= formMetaManager.get(defId);

		JsonNode permissionConf = null;
		if(StringUtil.isNotEmpty(bpmForm.getFormKey())){
			permissionConf = bpmFormRightManager.getByFormKey(bpmForm.getFormKey(), false);
		}else{
			permissionConf = bpmFormRightManager.getDefaultByFormDefKey(formDef.getKey(), false);
		}

		JsonNode permission = bpmFormRightManager.calcFormPermission(permissionConf);
		return permission;
	}

	@Override
	public Map getChooseDesignTemplate(String subject, String categoryId, String formDesc, Boolean isSimple) throws Exception {
		Map mv = new HashMap();
		String templatePath = FormUtil.getDesignTemplatePath();
		String xml = FileUtil.readFile(templatePath + "designtemps.xml");
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		List<Element> list = root.elements();
		String reStr = "[";
		for (Element element : list) {
			String alias = element.attributeValue("alias");
			String name = element.attributeValue("name");
			String templateDesc = element.attributeValue("templateDesc");
			if (!reStr.equals("["))
				reStr += ",";
			reStr += "{name:'" + name + "',alias:'" + alias+ "',templateDesc:'" + templateDesc + "'}";
		}
		reStr += "]";
		mv.put("subject", subject);
		mv.put("categoryId", categoryId);
		mv.put("formDesc", formDesc);
		mv.put("temps", reStr);
		mv.put("isSimple", isSimple);
		return mv;
	}

	@Override
	public void getGenByTemplate(String formId, String tableNames, String templateAlias, String formDefId, String formType, HttpServletResponse response) throws Exception {
		Form form = get(formId);
		if(form != null)formDefId = form.getDefId();

		String	html = getHtml(formDefId, tableNames, templateAlias);

		PrintWriter out = response.getWriter();
		html = JsoupUtil.prettyHtml(html);
		StringBuffer outHtml = new StringBuffer();
		String script="<script type='text/javascript'>"
				+"function validForm(scope){"
				+" return true;"
				+"}"
				+"</script>";

		outHtml.append(script);
		if(StringUtil.isNotEmpty(formType) && formType.equals(FormType.MOBILE.value())){
			outHtml.append("<div style=\"height: 100%;overflow: auto;\">");
			outHtml.append(html);
			outHtml.append("</div>");
		}else{
			outHtml.append(html);
		}
		out.println(outHtml);
	}

	@Override
	public ObjectNode getFormDesign(String formId) throws Exception {
		Form form = null;
		FormMeta formDef = null;
		ObjectNode resultJson = JsonUtil.getMapper().createObjectNode();
		if(StringUtil.isNotEmpty(formId)){
			ObjectNode designJson = JsonUtil.getMapper().createObjectNode();
			form = get(formId);
			if(BeanUtils.isNotEmpty(form)){
				formDef = formMetaManager.get(form.getDefId());
				if(BeanUtils.isNotEmpty(formDef)){
					resultJson.put("ganged", formDef.getGanged());
					resultJson.put("opinion", formDef.getOpinionConf());
					resultJson.put("defId",formDef.getId());
					resultJson.put("desc", formDef.getDesc());
					resultJson.put("name",formDef.getName());
					resultJson.put("rev",formDef.getRev());
					JsonNode jsonNode = JsonUtil.toJsonNode(formDef.getExpand());
					if(BeanUtils.isNotEmpty(jsonNode)){
						resultJson.put("bos", JsonUtil.toJson(jsonNode.get("boDefList")));
						resultJson.put("fields", JsonUtil.toJson(jsonNode.get("fields")));
						resultJson.put("flowField", jsonNode.has("flowField")?jsonNode.get("flowField").asText():"");
						resultJson.put("includeFiles", jsonNode.has("includeFiles")?jsonNode.get("includeFiles").asText():"");
						resultJson.put("designFormName", jsonNode.has("designFormName")?jsonNode.get("designFormName").asText():"");
						if(jsonNode.has("treeCtrl")){
							resultJson.set("treeCtrl", jsonNode.get("treeCtrl"));
						}

					}
					designJson.put("type", formDef.getType());
				}
			}
			ObjectNode formJson = JsonUtil.getMapper().createObjectNode();
			designJson.set("form", formJson);
			resultJson.put("formName",form.getName());
			resultJson.put("formKey",form.getFormKey());
			resultJson.put("formType",form.getFormType());
			resultJson.put("version",form.getVersion());
			resultJson.put("status",form.getStatus());
			resultJson.put("typeId",form.getTypeId());
			resultJson.put("typeName",form.getTypeName());
			resultJson.put("desc",form.getDesc());
			resultJson.put("macroAlias",formDef.getMacroAlias());
			resultJson.put("mainAlias",formDef.getMainAlias());
			resultJson.put("subEntity",formDef.getSubEntity());
			resultJson.put("isPrint", form.getIsPrint());
			resultJson.set("design",designJson);
		}
		return resultJson;
	}

	private String transformationMobileExpand(String expandStr) throws Exception {
		if (!StringUtil.isEmpty(expandStr)) {
			JsonNode expand = JsonUtil.toJsonNode(expandStr);
			ArrayNode table = (ArrayNode) expand.findValue("list");
			ArrayNode tempArray = JsonUtil.getMapper().createArrayNode();
			for (JsonNode tableNode : table) {
				if (tableNode.has("ctrlType") && Form.GRID_LAYOUT.equals(tableNode.get("ctrlType").asText()) ) {
					ArrayNode columns = (ArrayNode) tableNode.findValue("columns");
					for (JsonNode column : columns) {
						if (column.has("list")) {
							ArrayNode fields = (ArrayNode) column.findValue("list");
							for (JsonNode jsonNode : fields) {
								tempArray.add(jsonNode);
							}
						}
					}
				}else if(tableNode.has("ctrlType") && (Form.ACCORDION_LAYOUT.equals(tableNode.get("ctrlType").asText())||Form.TAB_LAYOUT.equals(tableNode.get("ctrlType").asText()))){
					ArrayNode columns = (ArrayNode) tableNode.findValue("columns");
					for (JsonNode column : columns) {
						if (column.has("list")) {
							ArrayNode fields = (ArrayNode) column.findValue("list");
							for (JsonNode jsonNode : fields) {
								if(Form.GRID_LAYOUT.equals(jsonNode.get("ctrlType").asText())){
									ArrayNode gridColumns = (ArrayNode) jsonNode.findValue("columns");
									for (JsonNode gridColumn : gridColumns) {
										if (column.has("list")) {
											ArrayNode gridFields = (ArrayNode) gridColumn.findValue("list");
											for (JsonNode gridjsonNode : gridFields) {
												tempArray.add(gridjsonNode);
											}
										}
									}
								}else{
									tempArray.add(jsonNode);
								}
							}
						}
					}
				}else {
					tempArray.add(tableNode);
				}
			}
			ObjectNode objectNode = (ObjectNode) expand;
			objectNode.set("list", tempArray);
			return JsonUtil.toJson(objectNode);
		}
		return "";
	}

	@Override
	public String getDesignHtml(JsonNode expand, String tableNames, String ganged, JsonNode formDefNode)
			throws Exception {
		List<Map<String, Object>> jary = new ArrayList<Map<String, Object>>();
		ArrayNode fieldList = (ArrayNode) expand.get("fields");
		for (JsonNode jsonNode : fieldList) {
			if ("main".equals(jsonNode.get("type").asText())) {
				ArrayNode arr = (ArrayNode) jsonNode.get("children");
				for (JsonNode fieldNode : arr) {
					jary.add(JsonUtil.toMap(fieldNode.toString()));
				}
			}
		}
		// 是否包含里程碑控件
		boolean hasStepControl = false;
		Map<String, Object> stepAttr = new HashMap<String, Object>();

		// 控件类型为文本时解析里面表达式
		Iterator<Map<String, Object>> jaryIt = jary.iterator();
		while (jaryIt.hasNext()) {
			Map<String, Object> map = jaryIt.next();
			if ("textFixed".equals(map.get("ctrlType"))) {
				String desc = map.get("desc").toString();
				desc = desc.replaceAll("\"", "\'");
				map.remove("desc");
				map.put("desc", desc);
			}
			if ("stepControl".equals(map.get("ctrlType"))) {
				hasStepControl = true;
				stepAttr = map;
				// 因为知道控件位置的原因，把他剔除
				jaryIt.remove();
			}
		}

		String mainAlias = "";
		String macroAlias = "";
		String subEntity = "";
		// 判断是表单定义时是否设置了表单模板 没有使用默认模板
		if (formDefNode.get("mainAlias").asText().equals("null")) {
			mainAlias = "dragColumn";
		} else {
			mainAlias = formDefNode.get("mainAlias").asText();
		}
		//
		if (formDefNode.get("macroAlias").asText().equals("null")) {
			macroAlias = "fieldControl";
		} else {
			macroAlias = formDefNode.get("macroAlias").asText();
		}
		//
		if (formDefNode.get("subEntity").asText().equals("null")) {
			subEntity = "subDragColumn";
		} else {
			subEntity = formDefNode.get("subEntity").asText();
		}

		// 当前模板
		FormTemplate mainTemplateObj = formTemplateManager.getByTemplateAlias(mainAlias);
		String mainTemplate = mainTemplateObj.getHtml();
		String subTemplate = formTemplateManager.getByTemplateAlias(subEntity).getHtml();
		String macroTemplate = formTemplateManager.getByTemplateAlias(macroAlias).getHtml();
		String divContainer = formTemplateManager.getByTemplateAlias(FormTemplate.DIV_CONTAINER).getHtml();
		FreeMakerUtil freeMakerUtil = new FreeMakerUtil();
		// 解析控件类型为iframe的链接地址参数变量
		for (int i = 0; i < jary.size(); i++) {
			if ("iframe".equals(jary.get(i).get("ctrlType"))) {
				String iframeSrc = jary.get(i).get("iframeSrc").toString();
				String baseUrl = portalFeignService.getPropertyByAlias("iframeUrl");
				if (iframeSrc.indexOf("iframeUrl") != 1) {
					iframeSrc = iframeSrc.replace("${iframeUrl}", baseUrl);
				}
				jary.get(i).put("iframeSrc", iframeSrc);
			}
		}

		// 如果表单中使用了多页签控件，则用多页签表单模板解析
		if (isContainsTabs(jary)) {
			List<Map<String, Object>> tabs = getTabsHtml(jary);
			List<Map<String, Object>> newTabs = new ArrayList<>();

			boolean _hasStepControl = hasStepControl;
			for (Map<String, Object> tab : tabs) {
				List<Map<String, Object>> fields = (List<Map<String, Object>>) tab.get("fields");
				// 判断多页签里面有没有使用折叠面板
				if (isContainsCollapse(fields)) {
					List<Map<String, Object>> collapse = new ArrayList<>();
					Map<String, Object> mapObj = new HashMap<>();
					mapObj.put("formDesc", tableNames);
					mapObj.put("ganged", ganged);
					mapObj.put("expand", expand);
					mapObj.put("expandMap", JsonUtil.toMap(expand.toString()));
					mapObj.put("util", freeMakerUtil);
					mapObj.put("mainTemplate", mainTemplate);
					mapObj.put("subTemplate", subTemplate);
					mapObj.put("macroTemplate", macroTemplate);
					mapObj.put("divContainer", divContainer);
					List<Map<String, Object>> newCollapse = getFields(getCollapseHtml(fields), mapObj);

					for (Map<String, Object> field : newCollapse) {
						if (!field.containsKey("tabHtml")) {
							Map<String, Object> map = new HashMap<>();
							map.put("formDesc", tableNames);
							map.put("fieldList", field.get("fields"));
							map.put("ganged", ganged);
							map.put("includeFiles",
									BeanUtils.isNotEmpty(expand.get("includeFiles"))
											? expand.get("includeFiles").asText()
											: "");
							map.put("util", freeMakerUtil);
							map.put("isTabs", true);
							map.put("hasStepControl", _hasStepControl);
							map.put("stepAttr", stepAttr);
							handlerTrGroup(map);
							field.put("isCollapse", field.containsKey("collapseField"));
							String tabHtml = freemarkEngine
									.parseByTemplate(mainTemplate + macroTemplate + subTemplate + divContainer, map);
							field.put("tabHtml", tabHtml);
						}
						collapse.add(field);

					}
					_hasStepControl = hasStepControl;
					if (collapse.size() > 0) {
						String tabsTemplate = formTemplateManager.getByTemplateAlias("collapseColumn").getHtml();
						Map<String, Object> tabMap = new HashMap<>();
						tabMap.put("util", freeMakerUtil);
						tabMap.put("tabs", collapse);
						tabMap.put("formDesc", tableNames);
						String html = freemarkEngine.parseByTemplate(tabsTemplate, tabMap);
						Map<String, Object> table = new HashMap<>();
						table.put("tabHtml", html);
						Map<String, Object> tabField = (Map<String, Object>) tab.get("tabField");
						table.put("tabField", tabField);
						newTabs.add(table);
					}
				} else {
					Map<String, Object> map = new HashMap<>();
					map.put("formDesc", tableNames);
					map.put("fieldList", tab.get("fields"));
					map.put("ganged", ganged);
					map.put("includeFiles",
							BeanUtils.isNotEmpty(expand.get("includeFiles")) ? expand.get("includeFiles").asText()
									: "");
					map.put("util", freeMakerUtil);
					map.put("isTabs", true);
					map.put("hasStepControl", _hasStepControl);
					map.put("stepAttr", stepAttr);
					handlerTrGroup(map);
					tab.put("isCollapse", tab.containsKey("collapseField"));

					String tabHtml = freemarkEngine
							.parseByTemplate(mainTemplate + macroTemplate + subTemplate + divContainer, map);
					tab.put("tabHtml", tabHtml);
					newTabs.add(tab);
				}
			}
			String tabsTemplate = formTemplateManager.getByTemplateAlias("tabsColumn").getHtml();
			Map<String, Object> tabMap = new HashMap<>();
			tabMap.put("formDesc", tableNames);
			tabMap.put("util", freeMakerUtil);
			tabMap.put("tabs", newTabs);
			String html = freemarkEngine.parseByTemplate(tabsTemplate, tabMap);
			return html;
		} else {
			if (isContainsCollapse(jary)) {
				List<Map<String, Object>> collapse = getCollapseHtml(jary);
				Map<String, Object> mapObj = new HashMap<>();
				mapObj.put("formDesc", tableNames);
				mapObj.put("ganged", ganged);
				mapObj.put("expand", expand);
				mapObj.put("expandMap", JsonUtil.toMap(expand.toString()));
				mapObj.put("util", freeMakerUtil);
				mapObj.put("mainTemplate", mainTemplate);
				mapObj.put("subTemplate", subTemplate);
				mapObj.put("macroTemplate", macroTemplate);
				List<Map<String, Object>> newCollapse = getFields(collapse, mapObj);
				String tabsTemplate = formTemplateManager.getByTemplateAlias("collapseColumn").getHtml();
				Map<String, Object> tabMap = new HashMap<>();
				tabMap.put("formDesc", tableNames);
				tabMap.put("util", freeMakerUtil);
				tabMap.put("tabs", newCollapse);
				String html = freemarkEngine.parseByTemplate(tabsTemplate, tabMap);
				return html;
			} else {
				Map<String, Object> map = new HashMap<>();
				map.put("formDesc", tableNames);
				map.put("fieldList", jary);
				map.put("ganged", ganged);
				map.put("expand", expand);
				map.put("expandMap", JsonUtil.toMap(expand.toString()));
				map.put("includeFiles",
						BeanUtils.isNotEmpty(expand.get("includeFiles")) ? expand.get("includeFiles").asText() : "");
				map.put("util", freeMakerUtil);
				map.put("isTabs", false);
				map.put("hasStepControl", hasStepControl);
				map.put("stepAttr", stepAttr);
				handlerTrGroup(map);
				String html = freemarkEngine.parseByTemplate(mainTemplate + macroTemplate + subTemplate + divContainer,
						map);
				return html;
			}
		}
	}

	private List<Map<String, Object>> getFields(List<Map<String, Object>> collapse, Map<String, Object> mapObj)
			throws Exception {
		List<Map<String, Object>> newCollapse = new ArrayList<>();
		String tableNames = (String) mapObj.get("formDesc");
		String ganged = (String) mapObj.get("ganged");
		JsonNode expand = (JsonNode) mapObj.get("expand");
		FreeMakerUtil freeMakerUtil = (FreeMakerUtil) mapObj.get("util");
		String mainTemplate = (String) mapObj.get("mainTemplate");
		String subTemplate = (String) mapObj.get("subTemplate");
		String divContainer = (String) mapObj.get("divContainer");
		String macroTemplate = (String) mapObj.get("macroTemplate");

		for (Map<String, Object> collaps : collapse) {
			List<Map<String, Object>> newList = (List<Map<String, Object>>) collaps.get("fields");
			if ("collapseEnd".equals(newList.get(newList.size() - 1).get("ctrlType"))) {
				newList.remove(newList.size() - 1);
				Map<String, Object> map = new HashMap<>();
				map.put("formDesc", tableNames);
				map.put("fieldList", newList);
				map.put("ganged", ganged);
				map.put("includeFiles",
						BeanUtils.isNotEmpty(expand.get("includeFiles")) ? expand.get("includeFiles").asText() : "");
				map.put("util", freeMakerUtil);
				map.put("isTabs", true);
				handlerTrGroup(map);
				String tabHtml = freemarkEngine
						.parseByTemplate(mainTemplate + macroTemplate + subTemplate + divContainer, map);
				collaps.put("tabHtml", tabHtml);
				collaps.put("isCollaps", true);
				if (collaps.containsKey("tabField")) {
					Map<String, Object> tabFieldMap = (Map<String, Object>) collaps.get("tabField");
					collaps.put("isShow", tabFieldMap.get("isShow"));
				}

				newCollapse.add(collaps);
			} else {
				if (!collaps.containsKey("tabField")) {
					Map<String, Object> map = new HashMap<>();
					map.put("formDesc", tableNames);
					map.put("fieldList", newList);
					map.put("ganged", ganged);
					map.put("includeFiles",
							BeanUtils.isNotEmpty(expand.get("includeFiles")) ? expand.get("includeFiles").asText()
									: "");
					map.put("util", freeMakerUtil);
					map.put("isTabs", true);
					handlerTrGroup(map);
					String tabHtml = freemarkEngine
							.parseByTemplate(mainTemplate + macroTemplate + subTemplate + divContainer, map);
					Map<String, Object> table = new HashMap<>();
					table.put("tabHtml", tabHtml);
					table.put("isCollaps", false);
					newCollapse.add(table);
				} else {
					List<Map<String, Object>> collapsFields = new ArrayList<>();
					List<Map<String, Object>> tableFields = new ArrayList<>();
					boolean ref = false;
					for (int i = 0; i < newList.size(); i++) {
						if (ref) {
							tableFields.add(newList.get(i));
						} else if (!"collapseEnd".equals(newList.get(i).get("ctrlType"))) {
							collapsFields.add(newList.get(i));
						}
						if ("collapseEnd".equals(newList.get(i).get("ctrlType"))) {
							ref = true;
						}
					}
					if (collapsFields.size() > 0) {
						Map<String, Object> c = new HashMap<>();
						// 把mapAA的元素复制到mapBB中
						Map<String, Object> map = new HashMap<>();
						map.put("formDesc", tableNames);
						map.put("fieldList", collapsFields);
						map.put("ganged", ganged);
						map.put("includeFiles",
								BeanUtils.isNotEmpty(expand.get("includeFiles")) ? expand.get("includeFiles").asText()
										: "");
						map.put("util", freeMakerUtil);
						map.put("isTabs", true);
						handlerTrGroup(map);
						String tabHtml = freemarkEngine.parseByTemplate(mainTemplate + macroTemplate + subTemplate,
								map);
						if (collaps.containsKey("tabField")) {
							Map<String, Object> tabFieldMap = (Map<String, Object>) collaps.get("tabField");
							c.put("isShow", tabFieldMap.get("isShow"));
						}
						c.put("tabHtml", tabHtml);
						c.put("isCollaps", true);
						c.put("tabField", collaps.get("tabField"));
						newCollapse.add(c);
					}
					if (tableFields.size() > 0) {
						Map<String, Object> map = new HashMap<>();
						map.put("formDesc", tableNames);
						map.put("fieldList", tableFields);
						map.put("ganged", ganged);
						map.put("includeFiles",
								BeanUtils.isNotEmpty(expand.get("includeFiles")) ? expand.get("includeFiles").asText()
										: "");
						map.put("util", freeMakerUtil);
						map.put("isTabs", true);
						handlerTrGroup(map);
						String tabHtml = freemarkEngine.parseByTemplate(mainTemplate + macroTemplate + subTemplate,
								map);
						Map<String, Object> table = new HashMap<>();
						if (collaps.containsKey("tabField")) {
							Map<String, Object> tabFieldMap = (Map<String, Object>) collaps.get("tabField");
							table.put("isShow", tabFieldMap.get("isShow"));
						}
						table.put("tabHtml", tabHtml);
						table.put("isCollaps", false);
						newCollapse.add(table);
					}
				}

			}
		}
		return newCollapse;

	}

	/**
	 * 如果是包含多页签控件，则将表单字段按页签分组
	 *
	 * @param fieldList
	 * @return
	 */
	private List<Map<String, Object>> getTabsHtml(List<Map<String, Object>> fieldList) {
		List<Map<String, Object>> fields = new ArrayList<>();
		List<Map<String, Object>> tabs = new ArrayList<>();
		int index = 0;
		Map<String, Object> tabField = new HashMap<>();
		for (Map<String, Object> field : fieldList) {
			if ("tabs".equals(field.get("ctrlType")) || "tabCheck".equals(field.get("ctrlType"))) {
				if (index > 0) {
					Map<String, Object> tab = new HashMap<>();
					tab.put("tabField", tabField);
					tab.put("fields", fields);
					tabs.add(tab);
					fields = new ArrayList<>();
				}
				if ("tabCheck".equals(field.get("ctrlType"))) {
					field.put("nextCheck",
							field.containsKey("nextCheck") && !(boolean) field.get("nextCheck") ? "n" : "y");
				}
				tabField = field;
			} else {
				if (index == 0) {
					// Tabs标签页
					tabField = new HashMap<>();
					String uuid = UniqueIdUtil.getSuid();
					tabField.put("name", "Tabs标签页");
					tabField.put("desc", "默认信息");
					tabField.put("ctrlType", "tabs");
					tabField.put("uuid", uuid);
					tabField.put("widthClass", "col-md-12");
				}
				fields.add(field);
			}
			index++;
		}
		if (BeanUtils.isNotEmpty(fields)) {
			Map<String, Object> tab = new HashMap<>();
			tab.put("tabField", tabField);
			tab.put("fields", fields);
			tabs.add(tab);
		}
		return tabs;
	}

	/**
	 * 如果是包含折叠面板控件，则将表单字段按页签分组
	 *
	 * @param fieldList
	 * @return
	 */
	private List<Map<String, Object>> getCollapseHtml(List<Map<String, Object>> fieldList) {
		List<Map<String, Object>> fields = new ArrayList<>();
		List<Map<String, Object>> collapses = new ArrayList<>();
		int index = 0;
		Map<String, Object> tabField = new HashMap<>();
		for (Map<String, Object> field : fieldList) {
			if ("collapse".equals(field.get("ctrlType"))) {
				if (index > 0) {
					Map<String, Object> tab = new HashMap<>();
					if (tabField.size() > 0) {
						tab.put("tabField", tabField);
					}
					tab.put("fields", fields);
					tab.put("lableColor", field.get("isShow"));
					collapses.add(tab);
					fields = new ArrayList<>();
				}
				tabField = field;
			} else {
				fields.add(field);
			}
			index++;
		}
		if (BeanUtils.isNotEmpty(fields)) {
			Map<String, Object> tab = new HashMap<>();
			if (tabField.size() > 0) {
				tab.put("tabField", tabField);
			}
			tab.put("fields", fields);
			collapses.add(tab);
		}
		return collapses;
	}

	// 验证当前表单中是否包含tabs多页签控件
	private boolean isContainsTabs(List<Map<String, Object>> fieldList) {
		if (BeanUtils.isNotEmpty(fieldList)) {
			for (Map<String, Object> map : fieldList) {
				if (map.containsKey("ctrlType")
						&& ("tabs".equals(map.get("ctrlType")) || "tabCheck".equals(map.get("ctrlType")))) {
					return true;
				}
			}
		}
		return false;
	}

	// 验证当前表单中是否包含tabs多页签控件
	private boolean isContainsCollapse(List<Map<String, Object>> fieldList) {
		if (BeanUtils.isNotEmpty(fieldList)) {
			for (Map<String, Object> map : fieldList) {
				if (map.containsKey("ctrlType") && "collapse".equals(map.get("ctrlType"))) {
					return true;
				}
			}
		}
		return false;
	}

	// 处理表单的TR中多列分组
	private void handlerTrGroup(Map<String, Object> map) {
		List<Map<String, Object>> trGroup = new ArrayList<>();// 主表
		List<Map<String, Object>> fieldList = (List<Map<String, Object>>) map.get("fieldList");
		Integer maxCount = 1;
		if (BeanUtils.isEmpty(fieldList)) {
			map.put("maxCol", maxCount);
			map.put("trGroup", trGroup);
			return;
		}
		Float currentCol = 0F;
		Integer count = 0;

		Map<String, Object> currentGroup = new HashMap<>();
		currentGroup.put("fields", new ArrayList<Map<String, Object>>());
		trGroup.add(currentGroup);
		for (Map<String, Object> field : fieldList) {
			String widthClass = MapUtil.getString(field, "widthClass");
			Float col = getColByWidthClass(widthClass);
			String ctrlType = MapUtil.getString(field, "ctrlType");
			if (BeanUtils.isNotEmpty(currentGroup.get("isSub")) && currentGroup.get("isSub").equals(true)) {
				currentCol = 0f;
			}
			if (currentCol == 0 || (currentCol + col) <= 1) {
				count++;
				if (count > maxCount) {
					maxCount = count;
				}
				currentCol += col;
				if (BeanUtils.isNotEmpty(currentGroup.get("isSub")) && currentGroup.get("isSub").equals(true)) {
					currentGroup = new HashMap<>();
					currentGroup.put("fields", new ArrayList<Map<String, Object>>());
					trGroup.add(currentGroup);
					((List<Map<String, Object>>) currentGroup.get("fields")).add(field);
				} else {
					((List<Map<String, Object>>) currentGroup.get("fields")).add(field);
				}
				currentGroup.put("count", count);
			} else {
				currentGroup = new HashMap<>();
				currentCol = col;
				count = 1;
				List<Map<String, Object>> fs = new ArrayList<>();
				fs.add(field);
				currentGroup.put("fields", fs);
				currentGroup.put("count", count);
				trGroup.add(currentGroup);
			}
			if (BeanUtils.isNotEmpty(field.get("children"))) {
				if ("divContainer".equals(ctrlType)) {
					continue;
				}
				if ("sub".equals(ctrlType)) {
					currentGroup.put("isSub", true);
				}
				List<Map<String, Object>> childrenList = (List<Map<String, Object>>) field.get("children");// 字表字段集合（整理前）
				List<Map<String, Object>> trGroupSup = new ArrayList<>();// 主表
				Map<String, Object> filedEntity = new HashMap<>();// 字表字段对象
				filedEntity.put("childrens", new ArrayList<Map<String, Object>>());
				trGroupSup.add(filedEntity);
				for (Map<String, Object> children : childrenList) {
					String widthClassSup = MapUtil.getString(children, "widthClass");
					Float colSup = getColByWidthClass(widthClassSup);
					if (colSup == 0 || (currentCol + colSup) <= 1) {
						count++;
						if (count > maxCount) {
							maxCount = count;
						}
						currentCol += colSup;
						((List<Map<String, Object>>) filedEntity.get("childrens")).add(children);
						filedEntity.put("count", count);
					} else {
						filedEntity = new HashMap<>();
						currentCol = colSup;
						count = 1;
						List<Map<String, Object>> fs = new ArrayList<>();
						fs.add(children);
						filedEntity.put("childrens", fs);
						filedEntity.put("count", count);
						trGroupSup.add(filedEntity);
					}
				}
				List<Map<String, Object>> list = ((List<Map<String, Object>>) currentGroup.get("fields"));
				for (Map<String, Object> obj : list) {
					obj.put("trGroupSup", trGroupSup);
				}
			}
		}
		map.put("maxCol", maxCount);
		map.put("trGroup", trGroup);
	}

	// 通过宽度样式获取列占比
	private Float getColByWidthClass(String widthClass) {
		Assert.notNull(widthClass, "widthClass不能为空");
		Float result = 0F;
		switch (widthClass) {
		case "col-md-12":
			result = 1F;
			break;
		case "col-md-6":
			result = 2F;
			break;
		case "col-md-4":
			result = 3F;
			break;
		case "col-md-3":
			result = 4F;
			break;
		default:
			throw new SystemException("widthClass的值不在允许的范围内");
		}
		return 1 / result;
	}

	@Override
	public String getMobileDesignHtml(JsonNode expand, String tableNames, String ganged) throws Exception {
		List<Map<String, Object>> jary = new ArrayList<Map<String, Object>>();
		ArrayNode fieldList = (ArrayNode) expand.get("fields");
		for (JsonNode jsonNode : fieldList) {
			if ("main".equals(jsonNode.get("type").asText())) {
				ArrayNode arr = (ArrayNode) jsonNode.get("children");
				for (JsonNode fieldNode : arr) {
					jary.add(JsonUtil.toMap(fieldNode.toString()));
				}
			}
		}

		// 当前模板
		FormTemplate mainTemplateObj = formTemplateManager.getByTemplateAlias("mobileMainTemplate");
		String mainTemplate = mainTemplateObj.getHtml();
		String subTemplate = formTemplateManager.getByTemplateAlias("blockSubTemplate").getHtml();
		String macroTemplate = formTemplateManager.getByTemplateAlias(mainTemplateObj.getMacrotemplateAlias())
				.getHtml();

		FreeMakerUtil freeMakerUtil = new FreeMakerUtil();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("formDesc", tableNames);
		map.put("fieldList", jary);
		map.put("ganged", ganged);
		map.put("includeFiles",
				BeanUtils.isNotEmpty(expand.get("includeFiles")) ? expand.get("includeFiles").asText() : "");
		map.put("util", freeMakerUtil);
		String html = freemarkEngine.parseByTemplate(mainTemplate + macroTemplate + subTemplate, map);
		return html;
	}

	@Override
	public CommonResult<String> savePrintTemplate(Form form) throws Exception {
		IUser user=ContextUtil.getCurrentUser();
		IGroup org = ContextUtil.getCurrentGroup();
		Form newForm = this.get(form.getId());
		String printFormKey = newForm.getFormKey();

		String formKey = form.getFormKey();
		if(BeanUtils.isNotEmpty(this.getByFormKey(formKey))) {
			throw new RuntimeException("表单已经存在，key:" + formKey);
		}

		//添加表单元数据
		FormMeta bpmFormDef=formMetaManager.get(newForm.getDefId());
		String formNewDefId=UniqueIdUtil.getSuid();
		bpmFormDef.setId(formNewDefId);
		bpmFormDef.setKey(formKey);
		bpmFormDef.setName(form.getName());
		bpmFormDef.setTypeId(form.getTypeId());
		bpmFormDef.setType(form.getTypeName());
		bpmFormDef.setCreateBy(user.getUserId());
		bpmFormDef.setCreateOrgId(org.getGroupId());
		bpmFormDef.setRev(1);
		//清空表单定义更新状态
		bpmFormDef.setUpdateBy(null);
		bpmFormDef.setUpdateTime(LocalDateTime.now());
		formMetaManager.create(bpmFormDef);

		//添加表单信息
		String newFormId = UniqueIdUtil.getSuid();
		newForm.setId(newFormId);
		newForm.setDefId(formNewDefId);
		newForm.setFormKey(formKey);
		newForm.setName(form.getName());
		newForm.setTypeId(form.getTypeId());
		newForm.setTypeName(form.getTypeName());
		newForm.setStatus(Form.STATUS_DRAFT);
		newForm.setVersion(1);
		newForm.setCreateBy(user.getUserId());
		newForm.setCreateOrgId(org.getGroupId());
		newForm.setIsPrint("Y");
		//清空表单更新状态
		newForm.setUpdateTime(LocalDateTime.now());
		newForm.setUpdateBy(null);
		this.create(newForm);

		//添加表单打印模板
		FormPrintTemplate formPrintTemplate = new FormPrintTemplate();
		String printType = "form";
		formPrintTemplate.setDefId(formNewDefId);
		formPrintTemplate.setFormId(newFormId);
		formPrintTemplate.setFormKey(printFormKey);
		formPrintTemplate.setFileName(form.getName());
		formPrintTemplate.setPrintType(printType);
		List<FormPrintTemplate> formPrintTemplates = formPrintTemplateManager.getPrintTemplates(printFormKey, printType);
 		if(BeanUtils.isEmpty(formPrintTemplates)) {
 			formPrintTemplate.setIsMain("Y");
 		}else {
 			formPrintTemplate.setIsMain("N");
 		}
 		formPrintTemplateManager.create(formPrintTemplate);

		return new CommonResult<String>(true, "保存成功");
	}

	@Override
	public void updatePermissionByKey(String formKey) throws Exception {
		updatePermissionm(formKey);
	}
	
	@Override
	@CachePut(value = "form:importFile", key="#fileId",
			  firstCache = @FirstCache(expireTime = 1, timeUnit = TimeUnit.HOURS))
	public String putImportFileInCache(String fileId, String fileJson) {
		return fileJson;
	}

	@Override
	@Cacheable(value = "form:importFile", key="#fileId",
			   firstCache = @FirstCache(expireTime = 1, timeUnit = TimeUnit.HOURS))
	public String getImportFileFromCache(String fileId) {
		return null;
	}

	@Override
	@CacheEvict(value = "form:importFile", key="#fileId")
	public void delImportFileFromCache(String fileId) {}
}
