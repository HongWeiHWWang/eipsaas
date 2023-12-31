package com.hotent.form.persistence.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.form.def.BpmBoDef;
import com.hotent.form.extmodel.ProcBoDef;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormField;
import com.hotent.form.model.FormMeta;
import com.hotent.form.persistence.dao.FormMetaDao;
import com.hotent.form.persistence.manager.FormFieldManager;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.service.FormService;
import com.hotent.form.util.FormUtil;

/**
 * 表单元数据 处理实现类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("bpmFormDefManager")
public class FormMetaManagerImpl extends BaseManagerImpl<FormMetaDao, FormMeta> implements FormMetaManager {
	@Resource
	FormFieldManager formFieldManager;
	@Resource
	FormManager bpmFormManager;
	@Resource
	FormService formService;
	@Resource
	BoDefManager boDefManager;

	/**
	 * 创建实体包含子表实体
	 */
	@Transactional
	public void create(FormMeta bpmFormDef) {
		super.create(bpmFormDef);
		try {
			this.createFields(bpmFormDef,null);
			updateBpmFormBo(bpmFormDef);
		} catch (IOException e) {
			throw new BaseException(e.getMessage());
		}
	}

	@Override
	public void createFields(FormMeta bpmFormDef,Map<String,String> entIdMap) throws IOException {
		createFormField(bpmFormDef.getExpand(),bpmFormDef.getId());
	}

	private void createFields(JsonNode field, String bpmFormDefId, int sn)
			throws JsonParseException, JsonMappingException, IOException {
		String boDefId = "";
		ObjectNode fieldObj = (ObjectNode) field;
		String type = JsonUtil.getString(fieldObj, "type");
		boolean isMain = "main".equals(type);
		if (isMain) {
			boDefId = JsonUtil.getString(fieldObj, "boDefId");
		}
		String boAttrId = JsonUtil.getString(fieldObj, "boAttrId");
		String ctrlType = JsonUtil.getString(fieldObj, "ctrlType");
		String columnType = JsonUtil.getString(fieldObj, "columnType");
		FormField formField = JsonUtil.toBean(fieldObj, FormField.class);
		formField.setBoDefId(boDefId);
		formField.setBoAttrId(boAttrId);
		formField.setCtrlType(ctrlType);
		formField.setType(columnType);
		formField.setId(UniqueIdUtil.getSuid());
		formField.setFormId(bpmFormDefId);
		formField.setSn(sn);
		formFieldManager.create(formField);
	}
	/**
	 * 更新表单和BO的关联记录
	 *
	 * @param formDef
	 * @throws IOException
	 */
	private void updateBpmFormBo(FormMeta formDef) throws IOException {
		String formId = formDef.getId();
		if (!StringUtil.isEmpty(formDef.getExpand())) {
			JsonNode expand = JsonUtil.toJsonNode(formDef.getExpand());
			JsonNode boDefs = expand.findValue("boDefList");
			for (int i = 0; i < boDefs.size(); i++) {
				String boDefId = boDefs.get(i).path("id").textValue();
				baseMapper.createBpmFormBo(UniqueIdUtil.getSuid(), boDefId, formId);
			}
		}
	}

	@Override
	public List<FormMeta> getByBODefId(String BODefId) {
		return baseMapper.getByBODefId(BODefId);
	}

	@Override
	public FormMeta getByKey(String formKey) {
		return baseMapper.getByKey(formKey);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public void updateOpinionConf(String id, String opinionJson) {
		Map map = new HashMap();
		map.put("id", id);
		map.put("opinionJson", opinionJson);
		baseMapper.updateOpinionConf(map);
	}

	@Override
	public String getMetaKeyByFormKey(String formKey) {
		return baseMapper.getMetaKeyByFormKey(formKey);
	}

	@Override
	public List<String> getBOCodeByFormId(String formDefId) {
		return baseMapper.getBOCodeByFormId(formDefId);
	}

	/**
	 * 删除记录包含子表记录
	 */
	@Transactional
	public void remove(String entityId) {
		List<Form> forms = bpmFormManager.getByDefId(entityId);
		for (Form form : forms) {
			bpmFormManager.remove(form.getId());
		}

		baseMapper.deleteBpmFormBo(entityId);
		formFieldManager.delByMainId(entityId);
		super.remove(entityId);
	}

	/**
	 * 更新实体同时更新子表记录
	 */
	@Transactional
	public void update(FormMeta bpmFormDef) {
		super.update(bpmFormDef);
		String formDefId = bpmFormDef.getId();

		try {
			formFieldManager.delByMainId(formDefId);
			createFields(bpmFormDef,null);
			baseMapper.deleteBpmFormBo(formDefId);
			updateBpmFormBo(bpmFormDef);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<BoEnt> getChildrenByFormKey(String formKey) throws IOException {
		List<BoEnt> ents = new ArrayList<BoEnt>();
		FormMeta def = baseMapper.getByKey(formKey);
		if (BeanUtils.isNotEmpty(def)) {
			String expandStr = def.getExpand();
			if (StringUtil.isNotEmpty(expandStr)) {
				ObjectNode expandJson = (ObjectNode) JsonUtil.toJsonNode(expandStr);
				ArrayNode boDefList = (ArrayNode) JsonUtil.toJsonNode(expandJson.get("boDefList"));
				if (BeanUtils.isNotEmpty(boDefList)) {
					for (JsonNode jsonNode : boDefList) {
						BoDef byAlias = null;
						if(jsonNode.has("alias")) {
							byAlias = boDefManager.getByAlias(jsonNode.get("alias").asText());
						}
						if(BeanUtils.isEmpty(byAlias) &&  jsonNode.has("id")) {
							byAlias = boDefManager.getByDefId(jsonNode.get("id").asText());
						}
						BoEnt boEnt = byAlias.getBoEnt();
						if(BeanUtils.isNotEmpty(boEnt)) {
							return boEnt.getChildEntList();
						}

					}
				}
			}
		}
		return ents;
	}

	@Override
	public List<BoData> getBoDataByFormDefId(String formDefId) {
		List<String> boCodes = baseMapper.getBOCodeByFormId(formDefId);
		List<BoData> boDatas = new ArrayList<BoData>();
		for (String code : boCodes) {
			BoData boData = formService.getBodataByDefCode("database", code);
			if (BeanUtils.isNotEmpty(boData)) {
				boDatas.add(boData);
			}
		}
		return boDatas;
	}

	@Override
	@Transactional
	public void deleteBpmFormBo(String formId) {
		baseMapper.deleteBpmFormBo(formId);
	}

	@Override
	@Transactional
	public void createBpmFormBo(String id, String boDefId, String formId) {
		baseMapper.createBpmFormBo(id, boDefId, formId);
	}

	@Override
	public List<String> getBODefIdByFormId(String formId) {
		return baseMapper.getBODefIdByFormId(formId);
	}

	@Override
	public List<FormMeta> getBODefByFormId(String formId){
		return baseMapper.getBODefByFormId(formId);
	}

	@Override
	public FormMeta getFormDefByRev(Map<String, Object> map) {
		return baseMapper.getFormDefByRev(map);
	}

	@Override
	public PageList listJson(QueryFilter queryFilter) throws Exception {
		PageList<FormMeta> list = (PageList<FormMeta>) query(queryFilter);
		List<JsonNode> bpmFormDefList = new ArrayList<JsonNode>();
		for (FormMeta bpmFormdef : list.getRows()) {
			Map formJson = new HashMap();// JSONObject.fromObject(bpmForm);
			formJson.put("desc", bpmFormdef.getName());
			formJson.put("key", bpmFormdef.getKey());
			formJson.put("id", bpmFormdef.getId());
			formJson.put("name", bpmFormdef.getName());
			formJson.put("type", bpmFormdef.getType());
			// 计算bo对象
			String expand = bpmFormdef.getExpand();
			if (StringUtil.isNotEmpty(expand)) {
				JsonNode expandJson = JsonUtil.toJsonNode(expand);
				if (expandJson.get("boDefList") != null) {
					formJson.put("boDefList", expandJson.get("boDefList").toString());
				}
			}
			bpmFormDefList.add(JsonUtil.toJsonNode(formJson));
		}
		PageList pageJson = new PageList(bpmFormDefList);
		pageJson.setPage(list.getPage());
		pageJson.setPageSize(list.getPageSize());
		pageJson.setRows(list.getRows());
		pageJson.setTotal(list.getTotal());
		return pageJson;
	}

	@Override
	public PageList listJsonByBODef(QueryFilter queryFilter, String defId, String formType, String topDefKey) throws Exception {
		List<ProcBoDef> boList = new ArrayList();
		// 如果是子流程，继承父类的bo数据。
		if (StringUtil.isNotEmpty(topDefKey)) {
			BpmBoDef bpmBodef = new BpmBoDef();
			/* BpmBoDef bpmBodef= BoDefUtil.getBpmBoDef(topDefKey); */
			boList = bpmBodef.getBoDefs();
		} else {
			// TODO
			/*
			 * BpmProcessDef<BpmProcessDefExt> bpmProcessDef=
			 * bpmDefinitionAccessor.getBpmProcessDef(defId); DefaultBpmProcessDefExt
			 * defExt= (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt(); boList=
			 * defExt.getBoDefList();
			 */
		}
		if (boList.size() == 0) {
			return new PageList();
		}
		List<String> codes = new ArrayList<String>();
		for (ProcBoDef procBoDef : boList) {
			codes.add(procBoDef.getKey());
		}
		List<Form> list = bpmFormManager.getByBoCodes(codes, formType, queryFilter);

		return new PageList(list);
	}

	@Override
	public Map getChooseDesignTemplate(String subject, String categoryId, String formDesc, Boolean isSimple) throws Exception {
		Map mv = new HashMap();

		String templatePath = FormUtil.getDesignTemplatePath();
		String xml = FileUtil.readByClassPath(templatePath + "designtemps.xml");
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
			reStr += "{name:'" + name + "',alias:'" + alias + "',templateDesc:'" + templateDesc + "'}";
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
	public void createByImport(FormMeta formDef, Map<String, String> entIdMap) {
		super.create(formDef);
		try {
			this.createFields(formDef,entIdMap);
			updateBpmFormBo(formDef);
		} catch (IOException e) {
			throw new BaseException(e.getMessage());
		}
	}

	@Override
	public void updateByImport(FormMeta formDef, Map<String, String> entIdMap) {
		super.update(formDef);
		String formDefId = formDef.getId();

		try {
			formFieldManager.delByMainId(formDefId);
			createFields(formDef,entIdMap);
			baseMapper.deleteBpmFormBo(formDefId);
			updateBpmFormBo(formDef);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void createFormField(String expandStr,String defId) throws IOException {

		
		JsonNode expand = JsonUtil.toJsonNode(expandStr);
		ArrayNode table = (ArrayNode) expand.findValue("list");
		int sn = 0;
		for (JsonNode tableNode : table) {
			String ctrlType = "";
			if (tableNode.has("ctrlType")) {
				ctrlType = tableNode.get("ctrlType").asText();
			}
			if (tableNode.has("columns")) {
				// 栅格布局的字段保存  tab 折叠 
				ArrayNode columns = (ArrayNode) tableNode.findValue("columns");
				for (JsonNode column : columns) {
					if (column.has("list")) {
						ArrayNode fields = (ArrayNode) column.findValue("list");
						for (JsonNode field : fields) {

							if(Form.GRID_LAYOUT.equals(field.get("ctrlType").asText())){//tab 折叠 嵌套栅格布局
								ArrayNode gridColumns = (ArrayNode) field.findValue("columns");
								for (JsonNode gridColumn: gridColumns) {
									if(gridColumn.has("list")){
										ArrayNode gridFields = (ArrayNode) gridColumn.findValue("list");
										for (JsonNode gridField : gridFields) {
											ObjectNode fieldObj =(ObjectNode) gridField;
											fieldObj.put("type", "main");
											createFields(fieldObj, defId, sn);
											sn++;
										}
									}
								}
							}else if(Form.SUBTABLE_LAYOUT.equals(field.get("ctrlType").asText())){//tab 折叠 嵌套子表布局
								// 子表布局
								ArrayNode subtableLists = (ArrayNode) field.findValue("list");
								sn = toCreateFields(subtableLists, defId, sn);
								/*for (JsonNode subtableField : subtableLists) {
									createFields(subtableField, defId, sn);
									sn++;
								}*/
							}else if(Form.SUBDIV_LAYOUT.equals(field.get("ctrlType").asText())){//tab 折叠 嵌套子表布局
								// div子表布局
								ArrayNode subtableLists = (ArrayNode) field.findValue("list");
								sn = toCreateFields(subtableLists, defId, sn);
								/*for (JsonNode subtableField : subtableLists) {
									createFields(subtableField, defId, sn);
									sn++;
								}*/
							}else if(Form.HOT_TABLE.equals(field.get("ctrlType").asText())){
								// tab handsonTable子表布局
								ArrayNode subtableLists = (ArrayNode) field.findValue("list");
								for (JsonNode subtableField : subtableLists) {
									createFields(subtableField, defId, sn);
									sn++;
								}
							}else if(Form.TAB_LAYOUT.equals(field.get("ctrlType").asText())){
								// tab 
								String tctrlType = "";
								if (tableNode.has("ctrlType")) {
									tctrlType = field.get("ctrlType").asText();
								}
								ArrayNode tabColumns = (ArrayNode) field.findValue("columns");
								for (JsonNode tabColumn : tabColumns) {
									if (tabColumn.has("list")) {
										ArrayNode tabFields = (ArrayNode) tabColumn.findValue("list");
										for (JsonNode tabField : tabFields) {

											if(Form.GRID_LAYOUT.equals(tabField.get("ctrlType").asText())){//tab 折叠 嵌套栅格布局
												ArrayNode gridColumns = (ArrayNode) tabField.findValue("columns");
												for (JsonNode gridColumn: gridColumns) {
													if(gridColumn.has("list")){
														ArrayNode gridFields = (ArrayNode) gridColumn.findValue("list");
														for (JsonNode gridField : gridFields) {
															ObjectNode fieldObj =(ObjectNode) gridField;
															fieldObj.put("type", "main");
															createFields(fieldObj, defId, sn);
															sn++;
														}
													}
												}
											}else if(Form.SUBTABLE_LAYOUT.equals(tabField.get("ctrlType").asText())){//tab 折叠 嵌套子表布局
												// 子表布局
												ArrayNode subtableLists = (ArrayNode) tabField.findValue("list");
												sn = toCreateFields(subtableLists, defId, sn);
											}else if(Form.SUBDIV_LAYOUT.equals(tabField.get("ctrlType").asText())){//tab 折叠 嵌套子表布局
												// div子表布局
												ArrayNode subtableLists = (ArrayNode) tabField.findValue("list");
												sn = toCreateFields(subtableLists, defId, sn);
											}else if(Form.HOT_TABLE.equals(tabField.get("ctrlType").asText())){
												// tab handsonTable子表布局
												ArrayNode subtableLists = (ArrayNode) tabField.findValue("list");
												for (JsonNode subtableField : subtableLists) {
													createFields(subtableField, defId, sn);
													sn++;
												}
											}else{
												ObjectNode fieldObj =(ObjectNode) tabField;
												fieldObj.put("type", "main");
												createFields(fieldObj, defId, sn);
												sn++;
											}
										}
									}else if(Form.SUBTABLE_LAYOUT.equals(tctrlType) || Form.SUBDIV_LAYOUT.equals(tctrlType) || Form.HOT_TABLE.equals(tctrlType)) {
										// 子表布局
										ArrayNode Lists = (ArrayNode) field.findValue("list");
										sn = toCreateFields(Lists, defId, sn);
									} else {
										// 不使用布局
										createFields(field, defId, sn);
										sn++;
									}
								}
							}else{
								ObjectNode fieldObj =(ObjectNode) field;
								fieldObj.put("type", "main");
								createFields(fieldObj, defId, sn);
								sn++;
							}

						}
					}
				}
			}else if(Form.SUBTABLE_LAYOUT.equals(ctrlType) || Form.HOT_TABLE.equals(ctrlType)) {
				// 子表布局
				ArrayNode Lists = (ArrayNode) tableNode.findValue("list");
				sn = toCreateFields(Lists, defId, sn);
				/*for (JsonNode field : Lists) {
					createFields(field, defId, sn);
					sn++;
				}*/
			}else if(Form.SUBDIV_LAYOUT.equals(ctrlType)){
				// div子表布局
				ArrayNode subtableLists = (ArrayNode) tableNode.findValue("list");
				sn = toCreateFields(subtableLists, defId, sn);
			} else {
				// 不使用布局
				createFields(tableNode, defId, sn);
				sn++;
			}
		}
	
		
	}
	private int toCreateFields(ArrayNode Lists,String defId,int sn) throws IOException{
		if(BeanUtils.isNotEmpty(Lists)){
			for (JsonNode field : Lists) {
				if(field.has("ctrlType") && field.has("list") && (Form.SUNTABLE_LAYOUT.equals(field.get("ctrlType").asText())
						|| Form.SUNDIV_LAYOUT.equals(field.get("ctrlType").asText()))){
					ArrayNode sunLists = (ArrayNode) field.findValue("list");
					for (JsonNode sunField : sunLists) {
						if(Form.GRID_LAYOUT.equals(sunField.get("ctrlType").asText())){//div子表布局  嵌套栅格布局
							ArrayNode gridColumns = (ArrayNode) sunField.findValue("columns");
							for (JsonNode gridColumn: gridColumns) {
								if(gridColumn.has("list")){
									ArrayNode gridFields = (ArrayNode) gridColumn.findValue("list");
									for (JsonNode gridField : gridFields) {
										ObjectNode fieldObj =(ObjectNode) gridField;
										createFields(fieldObj, defId, sn);
										sn++;
									}
								}
							}
						}else{
							createFields(sunField, defId, sn);
							sn++;
						}
					}
				}else if(field.has("ctrlType") && Form.GRID_LAYOUT.equals(field.get("ctrlType").asText())){
					ArrayNode gridColumns = (ArrayNode) field.findValue("columns");
					for (JsonNode gridColumn: gridColumns) {
						if(gridColumn.has("list")){
							ArrayNode gridFields = (ArrayNode) gridColumn.findValue("list");
							for (JsonNode gridField : gridFields) {
								ObjectNode fieldObj =(ObjectNode) gridField;
								createFields(fieldObj, defId, sn);
								sn++;
							}
						}
					}
				}else{
					createFields(field, defId, sn);
					sn++;
				}
			}
		}
		return sn;
	}
}
