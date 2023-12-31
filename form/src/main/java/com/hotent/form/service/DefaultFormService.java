package com.hotent.form.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bo.bodef.BoDefService;
import com.hotent.bo.context.FormContextThreadUtil;
import com.hotent.bo.instance.BoDataHandler;
import com.hotent.bo.instance.BoInstanceFactory;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoDefXml;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoEntRel;
import com.hotent.bo.model.BoResult;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.bo.persistence.manager.BoEntManager;
import com.hotent.bo.persistence.manager.BoEntRelManager;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormRight;
import com.hotent.form.model.FormRightXml;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormRightManager;
import com.hotent.form.vo.FormRestfulModel;
import com.hotent.table.datasource.DataSourceUtil;

@Service
public class DefaultFormService implements FormService {
	@Resource
	private FormManager bpmFormManager;
	@Resource
	BoDefManager boDefManager;
	@Resource
	BoEntManager boEntManager;
	@Resource
	BoInstanceFactory boInstanceFactory;
	@Resource
	FormRightManager bpmFormRightManager;
	@Resource
	BoDefService boDefService;
	@Resource
	BoEntRelManager boEntRelManager;
	@Resource
	BpmFormRightsService bpmFormRightsService;
	@Resource
	JdbcTemplate jdbcTemplate;
	
	public Form getByFormKey(String formKey) {
		Form form = bpmFormManager.getMainByFormKey(formKey);
		if(BeanUtils.isNotEmpty(form)) return form;
		
		return this.getByFormId(formKey);
	}

	
	@Override
	public Form getByFormId(String formId) {
		return bpmFormManager.get(formId);
	}


	@Override
	public String getFormExportXml(String formKeyStr) {
		List<String> id = new ArrayList<String>();
		String[] formKeys=formKeyStr.split(",");
		for (String formKey : formKeys) {
			Form form = bpmFormManager.getMainByFormKey(formKey);
			id.add(form.getId());
		}
		Map<String,String> map = bpmFormManager.exportForms(id, false); 
		
		return map.get("form.xml");
	}


	@Override
	public void importForm(String formXmlStr) {
		try {
			bpmFormManager.importByFormXml(formXmlStr,null,null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("导入表单失败"+e.getMessage(),e);
		}
	}
	

	@Override
	public BoDef getBoDefByAlias(String alias) throws IOException {
		if(StringUtil.isEmpty(alias)) throw new RuntimeException("Bo别名不能为空");
		return boDefManager.getByAlias(alias);
	}


	@Override
	public List<BoResult> handlerBoData(String id, String defId,
			ObjectNode boData, String saveType) throws JsonParseException, JsonMappingException, IOException {
		BoDataHandler handler= boInstanceFactory.getBySaveType(saveType);
		BoData curData=JsonUtil.toBean(boData, BoData.class);
		if(BeanUtils.isNotEmpty(boData.get("subMap"))){
			handlerSubMap(curData, boData);
		}
		curData.setBoDefAlias(boData.get("boDef").get("alias").asText());
		Map<String, Object> boMap = new HashMap<String, Object>();
		boMap.put(boData.get("boDef").get("alias").asText(), curData.getData());
		String data = JsonUtil.toJson(boMap);
		List<BoResult> list= handler.save(id, defId, curData);
		//添加表单
		for(BoResult result : list) {
			if(StringUtil.isNotEmpty(result.getModifyDetail())) {
				result.setData(data);
				break;
			}
		}
		return list;
	}
	
	/**
	 * 处理子表数据
	 * @param curData
	 * @param boData
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private void handlerSubMap(BoData curData,ObjectNode boData) throws JsonParseException, JsonMappingException, IOException{
		ObjectNode subMapNode = (ObjectNode) boData.get("subMap");
		Iterator<Entry<String, JsonNode>> fields = subMapNode.fields();
		while(fields.hasNext()){
			Entry<String, JsonNode> next = fields.next();
			String key = next.getKey();
			JsonNode jNode = next.getValue();
			if(jNode.isArray()){
				List<BoData> datas = new ArrayList<BoData>();
				for (JsonNode jsonNode : jNode) {
					Iterator<Entry<String, JsonNode>> subFields = jsonNode.fields();
					BoData data = new BoData();
					Map<String,List<BoData>> sunBoDataMap = new HashMap<String, List<BoData>>();
					while(subFields.hasNext()){
						Entry<String, JsonNode> subNext = subFields.next();
						String subkey = subNext.getKey();
						data.set(subkey, subNext.getValue());
						if(subkey.startsWith("sub_") && BeanUtils.isNotEmpty(subNext.getValue())){
							JsonNode sunNode = subNext.getValue();
							List<BoData> sunBoDatas = new ArrayList<BoData>();
							for (JsonNode itemNode : sunNode) {
								BoData sunData = new BoData();
								Iterator<Entry<String, JsonNode>> sunFields = itemNode.fields();
								while(sunFields.hasNext()){
									Entry<String, JsonNode> sunNext = sunFields.next();
									String sunkey = sunNext.getKey();
									sunData.set(sunkey, sunNext.getValue());
								}
								BoData bb = JsonUtil.toBean(JsonUtil.toJson(sunData), BoData.class);
								sunBoDatas.add(bb);
							}
							sunBoDataMap.put(subkey, sunBoDatas);
						}
					}
					//这里不通过json转bean的话不能取到正确属性类型的值，比如数字的取到的是转string后的值
					BoData aa = JsonUtil.toBean(JsonUtil.toJson(data), BoData.class);
					if(!sunBoDataMap.isEmpty()){
						for (String sunKey : sunBoDataMap.keySet()) {
							aa.setSubList(sunKey, sunBoDataMap.get(sunKey));
						}
					}
					datas.add(aa);
				}
				curData.setSubList(key, datas);
			}
		}
	}


	@Override
	public BoData getBodataByDefCode(String saveMode, String code) {
		if(!boDefManager.getByAlias(code).isSupportDb()){
			saveMode="boObject";
		}
		BoDataHandler handler= boInstanceFactory.getBySaveType(saveMode);
		return handler.getByBoDefCode(code);
	}


	@Override
	public BoData getBodataById(String saveMode, String id, String code) throws IOException {
		BoDataHandler handler= boInstanceFactory.getBySaveType(saveMode);
		return handler.getById(id, code);
	}


	@Override
	public String getBoDefExportXml(ObjectNode bodef) throws JAXBException, JsonParseException, JsonMappingException, IOException {
		ArrayNode arr= (ArrayNode) bodef.get("defList");
		BoDefXml bodefXml =  new BoDefXml();
		List<BoDef> boList=JsonUtil.toBean(JsonUtil.toJson(arr),new TypeReference<List<BoDef>>(){});
		bodefXml.setDefList(boList);
		String xml=JAXBUtil.marshall(bodefXml, BoDefXml.class);
		return xml;
	}


	


	@Override
	public List<BoDef> importBo(String bodefXml) {
		List<BoDef> boDefs= boDefManager.parseXml(bodefXml);
		
		return boDefManager.importBoDef(boDefs);
	}


	@Override
	public List<BoDef> importBoDef(List<BoDef> bos) {
		return boDefManager.importBoDef(bos);
	}



	@Override
	public ObjectNode getBoJosn(String id) throws IOException {
		return boDefManager.getBOJson(id);
	}


	@Override
	public BoEnt getBoEntByName(String name) {
		return boDefService.getEntByName(name);
	}


	@Override
	public ObjectNode getMainBOEntByDefAliasOrId(String alias, String defId)
			throws IOException {
		List<BoEnt> list = new ArrayList<BoEnt>();
		if(BeanUtils.isNotEmpty(defId)){
			list = boEntManager.getByDefId(defId);
		}else{
			BoDef def = getBoDefByAlias(alias);
			if(BeanUtils.isNotEmpty(def)){
				defId = def.getId();
				list = boEntManager.getByDefId(def.getId());
			}
		}
		ObjectNode mainBoent = JsonUtil.getMapper().createObjectNode();
		ArrayNode childEntList = JsonUtil.getMapper().createArrayNode();
		for (BoEnt boEnt : list) {
			BoEntRel rel = boEntRelManager.getByDefIdAndEntId(defId, boEnt.getId());
			if(BeanUtils.isNotEmpty(rel)){
				if("main".equals(rel.getType())){
					mainBoent = (ObjectNode) JsonUtil.toJsonNode(boEnt);
					continue;
				}
			}
			childEntList.add((ObjectNode) JsonUtil.toJsonNode(boEnt));
		}
		if(BeanUtils.isNotEmpty(mainBoent)&&BeanUtils.isNotEmpty(childEntList)){
			mainBoent.set("childEntList",childEntList);
		}
		return mainBoent;
	}

	
	/**
	 * database  需要从bo定义中获取 是否支持表
	 */
	@Override
	public List<BoData> getBoDataByBoKeys(List<String> boCode) {
		List<BoData> list = new ArrayList<BoData>();
		for (String code : boCode) {
			BoData bodataByDefCode = this.getBodataByDefCode("database", code);
			list.add(bodataByDefCode);
		}
		return list;
	}

	@Override
	public void removeDataByBusLink(JsonNode links) throws Exception {
		if(BeanUtils.isNotEmpty(links)){
			for (JsonNode jsonNode : links) {
				ObjectNode link = (ObjectNode) jsonNode;
				String saveModel = link.get("saveMode").asText();
				String idVal = BeanUtils.isEmpty(link.get("businesskeyStr")) ? link.get("businesskey").asText() : link.get("businesskeyStr").asText();
				if (link.get("saveMode").asText().equals("boObject")) {
					// 删除form_bo_int的数据
					String sql = "delete from form_bo_int where id_ = '" + idVal + "'";
					jdbcTemplate.execute(sql);
				} else if (saveModel.equals("database")) {
					BoEnt ent = boDefService.getEntByName(link.get("formIdentify").asText());
					if(BeanUtils.isNotEmpty(ent)){
						String sql = "delete from " + ent.getTableName() + " where " + ent.getPkKey() + " = '" + idVal + "'";
						if (ent.getIsExternal()==1) {// 外部表
							try {
								DataSourceUtil.getJdbcTempByDsAlias(ent.getDsName()).execute(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							jdbcTemplate.execute(sql);
						}
					}
				}
			}
		}
	}



	@Override
	public Map<String, String> getFormAndBoExportXml(ObjectNode obj) throws JAXBException {
		String formKeyStr=obj.get("formKeys").asText();
		String defKeys=obj.get("defKeys").asText();
		
		List<String> id = new ArrayList<String>();
		String[] formKeys=formKeyStr.split(",");
		for (String formKey : formKeys) {
			Form form = bpmFormManager.getMainByFormKey(formKey);
			if(BeanUtils.isNotEmpty(form))id.add(form.getId());
		}
		Map<String,String> map = bpmFormManager.exportForms(id, true); 
		String[]  defKeyArr=defKeys.split(",");
		FormRightXml formRightList = new FormRightXml();
		for (String defKey : defKeyArr) {
			List<FormRight> bpmFormRight=bpmFormRightsService.getFormRigthListByFlowKey(defKey);
			formRightList.addBpmFormRight(bpmFormRight);
		}
		String formRightXml = JAXBUtil.marshall(formRightList, FormRightXml.class);
		
		map.put("formrights.xml", formRightXml);
		
		return map;
	}


	@Override
	public CommonResult<String> importFormAndBo(ObjectNode obj) throws Exception {
		String formXmlStr = obj.get("formXmlStr").asText();
		String boXmlStr = obj.get("boXmlStr").asText();
		String formRightsXml = obj.get("formRightsXml").asText();
		List<BoDef> boDefs= boDefManager.parseXml(boXmlStr);
		Map<String,String> nameMap = this.getEntIdMap(boDefs, null);
		List<BoDef> importBoDef = boDefManager.importBoDef(boDefs);
		bpmFormManager.importByFormXml(formXmlStr,importBoDef,nameMap);
		bpmFormRightManager.importFormRights(formRightsXml);
		return new CommonResult<String>("导入成功");
	}


	@Override
	public BoData getByFormRestfulModel(FormRestfulModel model) throws IOException {
		BoDef boDef = boDefManager.getPureByAlias(model.getCode());
		String saveType=model.getSaveType();
		if(boDef!=null && !boDef.isSupportDb()){
			saveType="boObject";
		}
        FormContextThreadUtil.putCommonVars("defId", model.getFlowDefId());
        FormContextThreadUtil.putCommonVars("nodeId", model.getNodeId());
        FormContextThreadUtil.putCommonVars("parentDefKey", model.getParentFlowKey());
		BoData boData = this.getBodataById(saveType, model.getBoid(), model.getCode());
		boData.setBoDefAlias(boData.getBoDef().getAlias());
		return boData;
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
}
