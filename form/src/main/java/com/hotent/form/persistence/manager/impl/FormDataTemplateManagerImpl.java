package com.hotent.form.persistence.manager.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.CachePut;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.cache.annotation.FirstCache;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.*;
import com.hotent.form.model.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.constants.DataSourceConsts;
import com.hotent.base.constants.SQLConst;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.SystemException;
import com.hotent.base.feign.BpmRuntimeFeignService;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.Direction;
import com.hotent.base.query.FieldSort;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.template.impl.FreeMarkerEngine;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bo.bodef.BoDefService;
import com.hotent.bo.constant.BoConstants;
import com.hotent.bo.instance.BoDataHandler;
import com.hotent.bo.instance.BoDataImportHandler;
import com.hotent.bo.instance.BoInstanceFactory;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoResult;
import com.hotent.bo.model.ValidateResult;
import com.hotent.bo.persistence.manager.BoAttributeManager;
import com.hotent.bo.persistence.manager.BoEntManager;
import com.hotent.bo.util.BoUtil;
import com.hotent.form.param.DataTemplateQueryVo;
import com.hotent.form.persistence.dao.FormDataTemplateDao;
import com.hotent.form.persistence.manager.FormDataImportLogManager;
import com.hotent.form.persistence.manager.FormDataTemplateDraftManager;
import com.hotent.form.persistence.manager.FormDataTemplateManager;
import com.hotent.form.persistence.manager.FormFieldManager;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.persistence.manager.FormRightManager;
import com.hotent.form.persistence.manager.FormTemplateManager;
import com.hotent.form.service.FormService;
import com.hotent.form.util.FreeMakerUtil;
import com.hotent.form.vo.ExportSubVo;
import com.hotent.table.datasource.DataSourceUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.impl.util.PermissionCalc;
import com.hotent.uc.api.impl.util.PermissionUtil;
import com.hotent.uc.api.impl.var.IContextVar;
import com.hotent.uc.api.model.IUser;

import poi.util.ExcelUtil;


/**
 * 业务数据模板
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("bpmDataTemplateManager")
public class FormDataTemplateManagerImpl extends BaseManagerImpl<FormDataTemplateDao, FormDataTemplate> implements FormDataTemplateManager{
	private static final String LOGIN_USER = "loginUser";
	private static final String LOGIN_USER_ORGS = "loginUserOrgs";
	private static final String LOGIN_USER_SUB_ORGS = "loginUserSubOrgs";
	private static final String CUSTOM_ORGS = "customOrgs";
	private static final String[] flowField= {"F_bpm_proc_inst_id_","F_bpm_subject_","F_bpm_proc_def_name_","F_bpm_status_","F_bpm_create_time_","F_bpm_end_time_","F_bpm_is_forbidden_","F_bpm_creator_","F_bpm_is_dele_"};
	@Resource
	FormTemplateManager formTemplateManager;
	@Resource
	FormManager formManager;
	@Resource
	FormFieldManager formFieldManager;
	@Resource
	BoDataHandler boDataHandler;
	@Resource
	BoInstanceFactory boInstanceFactory;
	@Resource
	BoDefService boDefService;
	@Resource(name="formPermissionCalc")
	PermissionCalc permssionCalc;
	@Resource
	FreeMarkerEngine freemarkEngine;
	@Resource
	DatabaseContext databaseContext;
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	FormMetaManager formMetaManager;
	@Autowired
	UserDetailsService userDetailsService;
	@Resource
	BoEntManager boEntManager;
	@Resource
	BoAttributeManager boAttributeManager;
	@Resource
	FormRightManager bpmFormRightManager;
	@Resource
	FormService formService;
	@Resource
	BpmRuntimeFeignService bpmRuntimeFeignService;
	@Resource
	CommonManager commonManager;
	@Resource
	FormDataTemplateDraftManager dataTemplateDraftManager;
	@Resource
	FormDataImportLogManager formDataImportLogManager;
	
	@Override
	public ObjectNode getByFormKey(String formKey, String boId) throws IOException {
		ObjectNode jsonObject = JsonUtil.getMapper().createObjectNode();
		jsonObject.put("status", 1);
		jsonObject.put("msg", "支持生成业务数据模板");
		List<FormTemplate> templates = new ArrayList<FormTemplate>();
		FormDataTemplate bpmDataTemplate = new FormDataTemplate();
		List<FormField> fields = new ArrayList<FormField>();
		List<FormField> formField = new ArrayList<FormField>();
		BoDef baseBoDef = null;
		String boDefId = "";
		String colPrefix = "";
		String displaySettingFields ="";
		Boolean flag = true;

		// formKey ==>获取表单元数据定义id  def_id 根据def_id 获取bo对象的个数， 只有一个才支持业务对象模板的绑定
		Form bpmForm = formManager.getMainByFormKey(formKey);
		String formId = bpmForm.getDefId();
		List<String> boDefIds = formMetaManager.getBODefIdByFormId(formId);
//		if(boDefIds.isEmpty() || boDefIds.size()!=1 ){
//			ThreadMsgUtil.addMsg("该业务表单不支持生成业务数据模板(只支持一个BO生成的表单)");
//			jsonObject.put("status", 0);
//			jsonObject.put("msg", ThreadMsgUtil.getMessage().trim());
//			fag = false;
//		}
		if (BeanUtils.isEmpty(boDefIds)){
			ThreadMsgUtil.addMsg("该业务表单BO为空");
			jsonObject.put("status", 0);
			jsonObject.put("msg", ThreadMsgUtil.getMessage().trim());
			flag = false;
		}

		if(flag){
			boDefId = boDefIds.get(0);
			if (StringUtil.isNotEmpty(boId)){
				for (int i=0;i<boDefIds.size();i++){
					if (boDefIds.get(i).equals(boId)){
						boDefId = boDefIds.get(i);
					}
				}
			}
//			boDefId = boDefIds.get(0);
			baseBoDef =  boDefService.getByDefId(boDefId);
			colPrefix = baseBoDef.getBoEnt().isExternal()?"":SQLConst.CUSTOMER_COLUMN_PREFIX;
			if(!baseBoDef.isSupportDb()){
				jsonObject.put("status", 0);
				jsonObject.put("msg", "该表单不支持数据库，不能生成业务数据模板");
				flag = false;
			}
		}
		if(flag){

			BoDef boDef = boDefService.getByAlias(baseBoDef.getAlias());
			BoEnt boEnt = (BoEnt) boDef.getBoEnt();
			fields = getFormFields(boEnt.getId());
			for(FormField field:fields) {
				formField.add(field);
			}

			/*// 获取字段信息  根据form_id_(表单元数据定义id) , 业务对象定义id 获取主表字段
			List<FormField> initFields  = formFieldManager.getByFormIdAndBoDefId(formId, boDefId);
			//只获取主表字段
			for (FormField bpmFormField : initFields) {
				if(bpmFormField.getEntId().equals(boEnt.getId()) && !"sub".equals(bpmFormField.getCtrlType()) && !"tabs".equals(bpmFormField.getCtrlType())){
					fields.add(bpmFormField);
				}
			}*/

			displaySettingFields = this.getDisplayField(fields, "");
			bpmDataTemplate = new FormDataTemplate();
			bpmDataTemplate.setFormKey(formKey);
			bpmDataTemplate.setDisplayField(displaySettingFields);
			bpmDataTemplate.setBoDefId(boDefId); // 设置业务对象定义id
			bpmDataTemplate.setBoDefAlias(baseBoDef.getAlias());
			bpmDataTemplate.setName(bpmForm.getName());
			bpmDataTemplate.setTypeId(bpmForm.getTypeId());
			bpmDataTemplate.setTypeName(bpmForm.getTypeName());
			//添加流程字段
			if(StringUtil.isNotEmpty(bpmDataTemplate.getDefId())){
				addFieldList(fields);
			}
		}

		// 获取表单模板
		templates = formTemplateManager.getTemplateType(FormTemplate.DATA_TEMPLATE);

		jsonObject.set("templates", JsonUtil.toJsonNode(templates));
		jsonObject.set("bpmDataTemplate", JsonUtil.toJsonNode(bpmDataTemplate));
		jsonObject.set("fields", JsonUtil.toJsonNode(fields));
		jsonObject.put("displaySettingFields", displaySettingFields);
		jsonObject.set("data",tidyJson(bpmDataTemplate));
		jsonObject.put("colPrefix", colPrefix);
		jsonObject.put("formId", formId);
		jsonObject.set("formField", JsonUtil.toJsonNode(formField));

		jsonObject.set("permissionList", PermissionUtil.getPermissionList("formPermissionCalcList"));
		return jsonObject;
	}

	@Override
	public ObjectNode getByTemplateId(String id,String boId) throws IOException {
		ObjectNode jsonObject = JsonUtil.getMapper().createObjectNode();
		jsonObject.put("status", 1);
		jsonObject.put("msg", "支持生成业务数据模板");
		List<FormTemplate> templates = new ArrayList<FormTemplate>();
		FormDataTemplate bpmDataTemplate = this.get(id);
		List<FormField> fields = new ArrayList<FormField>();
		List<FormField> formField = new ArrayList<FormField>();
		BoDef baseBoDef = null;
		String boDefId = "";
		String colPrefix = "";
		String displaySettingFields ="";
		Boolean flag = true;
		String formKey = bpmDataTemplate.getFormKey();
		// formKey ==>获取表单元数据定义id  def_id 根据def_id 获取bo对象的个数， 只有一个才支持业务对象模板的绑定
		Form bpmForm = formManager.getMainByFormKey(formKey);
		String formId = bpmForm.getDefId();
		List<String> boDefIds = formMetaManager.getBODefIdByFormId(formId);

		if (BeanUtils.isEmpty(boDefIds)){
			ThreadMsgUtil.addMsg("该业务表单BO为空");
			jsonObject.put("status", 0);
			jsonObject.put("msg", ThreadMsgUtil.getMessage().trim());
			flag = false;
		}

		if(flag){
			boDefId = boDefIds.get(0);
			if (StringUtil.isNotEmpty(boId)){
				for (int i=0;i<boDefIds.size();i++){
					if (boDefIds.get(i).equals(boId)){
						boDefId = boDefIds.get(i);
					}
				}
			}
			baseBoDef =  boDefService.getByDefId(boDefId);
			colPrefix = baseBoDef.getBoEnt().isExternal()?"":SQLConst.CUSTOMER_COLUMN_PREFIX;
			if(!baseBoDef.isSupportDb()){
				jsonObject.put("status", 0);
				jsonObject.put("msg", "该表单不支持数据库，不能生成业务数据模板");
				flag = false;
			}
		}
		if(flag){

			BoDef boDef = boDefService.getByAlias(baseBoDef.getAlias());
			BoEnt boEnt = (BoEnt) boDef.getBoEnt();
			fields = getFormFields(boEnt.getId());
			for(FormField field:fields) {
				formField.add(field);
			}

			// 获取字段信息  根据form_id_(表单元数据定义id) , 业务对象定义id 获取主表字段
			List<FormField> initFields  = formFieldManager.getByFormIdAndBoDefId(formId, boDefId);
			//只获取主表字段
			for (FormField bpmFormField : initFields) {
				if(bpmFormField.getEntId().equals(boEnt.getId()) && !"sub".equals(bpmFormField.getCtrlType()) && !"tabs".equals(bpmFormField.getCtrlType())){
//					fields.add(bpmFormField);
					formField.add(bpmFormField);
				}
			}

			//添加流程字段
			if(StringUtil.isNotEmpty(bpmDataTemplate.getDefId())){
				addFieldList(fields);
			}
			displaySettingFields = this.getDisplayField(fields, "");
			bpmDataTemplate.setDisplayField(bpmDataTemplate.getDisplayField());
		}
		// 获取表单模板
		templates = formTemplateManager.getTemplateType(FormTemplate.DATA_TEMPLATE);
		jsonObject.set("templates", JsonUtil.toJsonNode(templates));
		jsonObject.set("bpmDataTemplate", JsonUtil.toJsonNode(bpmDataTemplate));
		jsonObject.set("fields", JsonUtil.toJsonNode(fields));
		jsonObject.put("displaySettingFields", displaySettingFields);
		jsonObject.set("data",tidyJson(bpmDataTemplate));
		jsonObject.put("colPrefix", colPrefix);
		jsonObject.put("formId", formId);
		jsonObject.set("formField", JsonUtil.toJsonNode(formField));

		jsonObject.set("permissionList", PermissionUtil.getPermissionList("formPermissionCalcList"));
		return jsonObject;
	}

	private List<FormField> getFormFields(String boEntId){
		List<FormField> fields = new ArrayList<>();
		List<BoAttribute> attributes = boAttributeManager.getByEntId(boEntId);
		fields.addAll(convertAttr2Field(attributes));
		return fields;
	}

	private List<FormField> convertAttr2Field(List<BoAttribute> attributes){
		List<FormField> fields = new ArrayList<>();
		for (BoAttribute attribute:attributes){
			FormField field=new FormField();
			field.setName(attribute.getName());
			field.setDesc(attribute.getDesc());
			field.setType(attribute.getDataType());
			field.setShowFlowField(true);
			field.setStatus(attribute.getStatus());
			fields.add(field);
		}
		return fields;
	}

	private ObjectNode tidyJson(FormDataTemplate template) throws IOException{
		template.setTemplateHtml("");
		ObjectNode jsonObject= (ObjectNode) JsonUtil.toJsonNode(template);
		jsonObject.remove("pageBean");
		jsonObject.remove("createBy");
		jsonObject.remove("createtime");
		jsonObject.remove("updateBy");
		jsonObject.remove("updatetime");
		return jsonObject;
	}

	private String getDisplayField(List<FormField> fields, String displayField) throws IOException {
		Map<String, String> map = getDisplayFieldRight(displayField);
		Map<String, String> descMap = getDisplayFieldDesc(displayField);
		if (BeanUtils.isNotEmpty(fields)) {
			ArrayNode jsonAry = JsonUtil.getMapper().createArrayNode();
			for (FormField bpmFormField : fields) {
				ObjectNode json = JsonUtil.getMapper().createObjectNode();
				json.put("name", bpmFormField.getName());
				String desc = bpmFormField.getDesc();
				if (BeanUtils.isNotEmpty(map) && map.containsKey(bpmFormField.getName())) {
					desc = descMap.get(bpmFormField.getName());
				}
				json.put("desc", desc);
				json.put("type", bpmFormField.getType());
				json.put("status",bpmFormField.getStatus());
				String right = "";
				if (BeanUtils.isNotEmpty(map))
					right = map.get(bpmFormField.getName());
				if (StringUtil.isEmpty(right))
					right = getDefaultDisplayFieldRight();

				json.put("right", right);
				if(bpmFormField.isShowFlowField()) {
					json.put("showFlowField", true);
				}
				jsonAry.add(json);
			}
			displayField = jsonAry.toString();
		}
		return displayField;

	}

	private Map<String, String> getDisplayFieldDesc(String displayField) throws IOException {
		if (StringUtil.isEmpty(displayField))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		ArrayNode jsonAry = (ArrayNode) JsonUtil.toJsonNode(displayField);
		for (Object obj : jsonAry) {
			ObjectNode json = (ObjectNode)obj;
			String name = json.get("name").asText();
			String desc = json.get("desc").asText();
			map.put(name, desc);
		}
		return map;
	}

	private Map<String, String> getDisplayFieldRight(String displayField) throws IOException {
		if (StringUtil.isEmpty(displayField))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		ArrayNode jsonAry = (ArrayNode) JsonUtil.toJsonNode(displayField);

		for (Object obj : jsonAry) {
			ObjectNode json = (ObjectNode)obj;
			String name = json.get("name").asText();
			ArrayNode right = (ArrayNode) json.get("right");
			map.put(name, right.toString());
		}
		return map;
	}

	private String getDefaultDisplayFieldRight() {
		ArrayNode array = JsonUtil.getMapper().createArrayNode();
		ObjectNode json = JsonUtil.getMapper().createObjectNode();
		json.put("type", "everyone");
		json.put("id", "");
		json.put("name", "");
		json.put("script", "");
		array.add(json);
		return array.toString();
	}

	@Override
	@Transactional
	public void save(FormDataTemplate bpmDataTemplate, boolean resetTemp) throws Exception {
		Integer bpmDataTemplateNum = getCountByAlias(bpmDataTemplate.getAlias());
		boolean flag1 = bpmDataTemplateNum > 0;//判断是否已存在数据
		boolean flag2 = StringUtil.isEmpty(bpmDataTemplate.getId());

		String templateHtml = generateTemplate(bpmDataTemplate);
		if (!flag1 && flag2) {//新
			bpmDataTemplate.setId(UniqueIdUtil.getSuid());
			bpmDataTemplate.setTemplateHtml(templateHtml);//每次保存都需要重新生成模板
			bpmDataTemplate.setCreateTime(LocalDateTime.now());
			this.create(bpmDataTemplate);
		} else {
			FormDataTemplate temp = getByAlias(bpmDataTemplate.getAlias());
			if(temp != null && !bpmDataTemplate.getId() .equals(temp.getId()) ){
				throw new BaseException("报表别名："+bpmDataTemplate.getAlias()+"已存在，请输入其他别名！");
			}
			if(resetTemp){//需要更新模板
				bpmDataTemplate.setTemplateHtml(templateHtml);
			}else{
				FormDataTemplate old = baseMapper.getByAlias(bpmDataTemplate.getAlias());
				bpmDataTemplate.setTemplateHtml(old.getTemplateHtml());
			}
			bpmDataTemplate.setUpdateTime(LocalDateTime.now());
			this.update(bpmDataTemplate);
		}
	}

	/**
	 * 解析生成第一次的模板
	 *
	 * @param bpmDataTemplate
	 * @return
	 * @throws Exception
	 */
	private String generateTemplate(FormDataTemplate bpmDataTemplate) throws Exception {

		FormTemplate bpmFormTemplate = formTemplateManager.getByTemplateAlias(bpmDataTemplate.getTemplateAlias());//获取需要第一次解释的模板
		List<FormField> fileds = formFieldManager.getByboDefId(bpmDataTemplate.getBoDefId()); // 获取主表字段
		// 是否有条件查询
		String conditionField = bpmDataTemplate.getConditionField();
		boolean hasCondition = hasCondition(conditionField);
		// 是否有功能按钮
		boolean hasManage = hasManage(bpmDataTemplate.getManageField());
		//合并查询处理
		String isIndistinct=bpmDataTemplate.getIsIndistinct();
		String conditionAllName="";
		String conditionAllDesc="请输入关键字  "+bpmDataTemplate.getConditionAllDesc();

		// 获取主键
		BoDef boDef = boDefService.getByAlias(bpmDataTemplate.getBoDefAlias());
		BoEnt boEnt = boDef.getBoEnt();
		if (hasCondition) {
			ArrayNode conditions=(ArrayNode) JsonUtil.toJsonNode(conditionField);
			for (JsonNode jsonNode : conditions) {
				ObjectNode condition = (ObjectNode) jsonNode;
				String qt=condition.get("qt").asText();
				String type="";
				if (condition.hasNonNull("ty") && "date".equals(condition.get("ty").asText()) ) {
					String field =(BoEnt.FIELD_PREFIX+ condition.get("name").asText()).toLowerCase();
					BoAttribute attribute = boEnt.getAttrByField(field);
					condition.put("format", attribute.getFormat());
					if ("between".equals(qt)) {
						type = "yyyy-MM-dd HH:mm:ss".equals(attribute.getFormat())?"datetimerange":"daterange";
					}else {
						type = "yyyy-MM-dd HH:mm:ss".equals(attribute.getFormat())?"datetime":"date";
					}
				}else if (condition.hasNonNull("ct") && "date".equals(condition.get("ct").asText())) {//此处不好获取表单里面日期字符串的格式化配置，暂时使用年月日时分秒
					condition.put("format", "yyyy-MM-dd HH:mm:ss");
					type = "between".equals(qt)?"datetimerange":"datetime";
				}
				condition.put("ctrlType", type);
			}
			bpmDataTemplate.setConditionField(JsonUtil.toJson(conditions));
		}
		String[] conditionArr=bpmDataTemplate.getConditionAllName().split(",");
		for(int i=0;i<conditionArr.length;i++) {
			if(boEnt.isExternal()) {
				conditionAllName+=conditionArr[i]+",";
			}else {
				conditionAllName+=SQLConst.CUSTOMER_COLUMN_PREFIX+conditionArr[i]+",";
			}
		}
		if(!"".equals(conditionAllName)) {
			conditionAllName=conditionAllName.substring(0,conditionAllName.length()-1);
		}

		// 第一次解析模板
		FreeMakerUtil freeMakerUtil = new FreeMakerUtil();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bpmDataTemplate", bpmDataTemplate);
		map.put("hasCondition", hasCondition);
		map.put("hasManage", hasManage);
		map.put("pkField", boEnt.getPkKey().toLowerCase());
		map.put("colPrefix", boEnt.isExternal()?"":SQLConst.CUSTOMER_COLUMN_PREFIX);
		List<FormField> list = new ArrayList<>();
		for (int i=0;i<fileds.size();i++) {
			if("textFixed".equals(fileds.get(i).getCtrlType())){
				fileds.get(i).setDesc(fileds.get(i).getDesc().replaceAll("\"","\'" ));
			}
			list.add(fileds.get(i));
		}
		map.put("formatData", list);
		map.put("util", freeMakerUtil);
		map.put("isIndistinct", isIndistinct);
		map.put("conditionAllName", conditionAllName);
		map.put("conditionAllDesc", conditionAllDesc);

		String templateHtml = freemarkEngine.parseByStringTemplate(bpmFormTemplate.getHtml(),map);
		return templateHtml;

	}

	/**
	 * 是否有管理
	 *
	 * @param manageField
	 * @return
	 * @throws IOException
	 */
	private boolean hasManage(String manageField) throws IOException {
		if (StringUtil.isEmpty(manageField))
			return false;
		ArrayNode jsonAry = (ArrayNode) JsonUtil.toJsonNode(manageField);
		return jsonAry.size() > 0 ? true : false;
	}

	/**
	 * 是否有条件
	 *
	 * @param conditionField
	 * @return
	 * @throws IOException
	 */
	private boolean hasCondition(String conditionField) throws IOException {
		if (StringUtil.isEmpty(conditionField))
			return false;
		ArrayNode jsonAry = (ArrayNode) JsonUtil.toJsonNode(conditionField);
		return jsonAry.size() > 0 ? true : false;
	}

	/**
	 * 根据formKey获取业务表单数量。
	 *
	 * @param formKey
	 * @return
	 */
	public Integer getCountByAlias(FormDataTemplate bpmDataTemplate) {
		return baseMapper.getCountByAlias(bpmDataTemplate.getAlias());
	}

	/**
	 * 根据表单key获取是否定义了数据模版。
	 *
	 * @param formKey
	 * @return
	 */
	public Integer getCountByAlias(String alias) {
		return baseMapper.getCountByAlias(alias);
	}

	@Override
	public String getDisplay(String alias, Map<String, Object> params,
			Map<String, Object> queryParams) throws Exception {
		Map<String, Set<String>> curProfiles =  permssionCalc.getCurrentProfiles();
		Map<String, Object> model = new HashMap<String, Object>();

		//获取业务模板数据
		FormDataTemplate bpmDataTemplate = baseMapper.getByAlias(alias);

		params.put(FormDataTemplate.PARAMS_KEY_BOALIAS, bpmDataTemplate.getBoDefAlias());
		params.put(FormDataTemplate.PARAMS_KEY_FORM_KEY, bpmDataTemplate.getFormKey());
		params.put(FormDataTemplate.PARAMS_KEY_DEF_ID, BeanUtils.isEmpty(bpmDataTemplate.getDefId())?"":bpmDataTemplate.getDefId());

		Map<String,Boolean> managePermission = getManagePermission(bpmDataTemplate.getManageField(), curProfiles);

		if(StringUtil.isEmpty(bpmDataTemplate.getDefId())){
			managePermission.put(FormDataTemplate.MANAGE_TYPE_START_FLOW, false);
		}

		List<Map<String,String>> filters = getFilterPermission(bpmDataTemplate.getFilterField(),curProfiles);
		// actionUrl
		//model.put("actionUrl", getActionUrl(params));
		model.put("ctx",params.get(FormDataTemplate.PARAMS_KEY_CTX));
		model.put("bpmDataTemplate", bpmDataTemplate);
		// 当前字段的权限
		model.put("permission", getPermission( bpmDataTemplate.getDisplayField(), curProfiles));
		// 功能按钮的权限
		model.put("managePermission", managePermission);
		model.put("hasSub", StringUtil.isNotEmpty(getSubEntIds(bpmDataTemplate.getFormKey()))?true:false);//当前是否有子表

		// 获取主键
		BoDef boDef = boDefService.getByAlias(bpmDataTemplate.getBoDefAlias());
		BoEnt boEnt = (BoEnt) boDef.getBoEnt();
		model.put("pkField", boEnt.getPkKey().toLowerCase());
		model.put("colPrefix", boEnt.isExternal()? "":SQLConst.CUSTOMER_COLUMN_PREFIX);
		model.put("filters", filters);
		JsonUtil jsonUtil = new JsonUtil();
		model.put("JsonUtil", jsonUtil);
		String templateHtml = bpmDataTemplate.getTemplateHtml();

		String html  = freemarkEngine.parseByStringTemplate(templateHtml, model);

		return html;
	}

	private String getSubEntIds(String formKey){
		try {
			List<BoEnt> list = formMetaManager.getChildrenByFormKey(formKey);
			if(BeanUtils.isNotEmpty(list)){
				StringBuilder sql = new StringBuilder();
				boolean isInit = false;
				for (BoEnt boEnt : list) {
					if(!isInit){
						isInit = true;
					}else{
						sql.append(",");
					}
					sql.append(boEnt.getId());
				}
				return sql.toString();
			}
		} catch (IOException e) {}
		return "";
	}

	/**
	 * 获取当前用户有权限的过滤条件
	 * @param filterField
	 * @param curProfiles
	 * @return
	 * @throws IOException
	 */
	private List<Map<String, String>> getFilterPermission(String filterField,
			Map<String, Set<String>> curProfiles) throws IOException {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		if(StringUtil.isEmpty(filterField))
			return list;
		ArrayNode jsonAry = (ArrayNode) JsonUtil.toJsonNode(filterField);

		for (Object obj : jsonAry) {
			ObjectNode json = (ObjectNode)obj;
			ArrayNode rights = json.get("right").isArray()?(ArrayNode)json.get("right"):(ArrayNode)JsonUtil.toJsonNode(json.get("right").asText());
			boolean hasRight = false;
			for (JsonNode permission : rights) {
				hasRight = permssionCalc.hasRight(permission.toString(), curProfiles);
				if(hasRight){
					break;
				}
			}
			if(hasRight){
				Map<String,String> map = new HashMap<String, String>();
				map.put("name", JsonUtil.getString(json, "name", ""));
				map.put("filterKey", JsonUtil.getString(json, "key", ""));
				list.add(map);
			}
		}

		return list;
	}

	/**
	 * 获取管理的权限
	 *
	 * @param manageField
	 * @param rightMap
	 * @return
	 * @throws IOException
	 */
	private Map<String, Boolean> getManagePermission(String manageField, Map<String, Set<String>> currentMap) throws IOException {
		if (StringUtil.isEmpty(manageField))
			return null;
		ArrayNode jsonAry = (ArrayNode) JsonUtil.toJsonNode(manageField);
		return getPermissionMap(jsonAry, currentMap);
	}

	/**
	 * [{"desc":"新增","name":"add","right":[{"type":"everyone"}]},
	 * {"desc":"编辑","name":"edit","right":[{"type":"everyone"}]},
	 * {"desc":"删除","name":"del","right":[{"type":"everyone"}]},
	 * {"desc":"明细","name":"detail","right":[{"type":"everyone"}]}]
	 * @param jsonAry
	 * @param currentMap
	 * @return
	 * @throws IOException
	 */
	private Map<String, Boolean> getPermissionMap(ArrayNode jsonAry,
			Map<String, Set<String>> currentMap) throws IOException {
		Map<String,Boolean> map = new HashMap<String, Boolean>();
		if(BeanUtils.isEmpty(jsonAry) || jsonAry.size()<1){
			return map;
		}
		for (Object obj : jsonAry) {
			ObjectNode json = (ObjectNode) JsonUtil.toJsonNode(obj);
			String name = json.get("name").asText();
			String rightStr = json.get("right").textValue();
			ArrayNode rights = StringUtil.isEmpty(rightStr)?(ArrayNode)json.get("right"):(ArrayNode) JsonUtil.toJsonNode(rightStr);
			boolean hasRight = false;
			for (JsonNode permission : rights) {
				hasRight = permssionCalc.hasRight(permission.toString(), currentMap);
				if(hasRight){
					map.put(name, hasRight);
					break;
				}
			}
			map.put(name, hasRight);
		}
		return map;
	}


	/**
	 * 获取字段的权限
	 *
	 * @param userId
	 * @param type
	 * @param bpmDataTemplate
	 * @return
	 * @throws IOException
	 */
	private Map<String, Boolean> getPermission( String displayField, Map<String, Set<String>> rightMap) throws IOException {
		ArrayNode jsonAry = (ArrayNode) JsonUtil.toJsonNode(displayField);
		return getPermissionMap(jsonAry, rightMap);
	}

	@Override
	public Map<String,Object> getFormByFormKey(String formKey) {
		Map<String,Object> map = new HashMap<String, Object>();
		Form formModel = formManager.getMainByFormKey(formKey);
		if(formModel==null || StringUtil.isEmpty(formModel.getFormHtml())){
			map.put("result", "formEmpty");
			return map;
		}
		map.put("form", formModel);
		map.put("result", true);
		return map;
	}

	@Override
	@Transactional
	public void boSave(ObjectNode jsonObject,String boAlias, String delDraftId) throws Exception {
		JsonNode jsonData = JsonUtil.toJsonNode(jsonObject.get(boAlias).toString());
		BoData boData = BoUtil.transJSON(jsonData);
		BoDataHandler handler= boInstanceFactory.getBySaveType(BoConstants.SAVE_MODE_DB);
		BoDef boDef = boDefService.getByAlias(boAlias);
		BoEnt boEnt = boDef.getBoEnt();
		boData.setBoEnt(boEnt);
		boData.setBoDef(boDef);
		List<BoResult> resultList = handler.save("", "", boData);
		if(BeanUtils.isNotEmpty(resultList)) {
			//添加修改记录
			handleBoResult(resultList, jsonObject);
		}
		//删除草稿
		if(BeanUtils.isNotEmpty(delDraftId)){
			dataTemplateDraftManager.remove(delDraftId);
		}
	}
	@Override
	@Transactional
	public void boSaveDraft(FormDataTemplateDraft dataTemplateDraft) {
		IUser currentUser = ContextUtil.getCurrentUser();
		if(BeanUtils.isEmpty(dataTemplateDraft.getId())){
			FormDataTemplate dataTemplate = this.getByAlias(dataTemplateDraft.getTempAlias());
			String currentTime = DateUtil.getCurrentTime();
			dataTemplateDraft.setTitle(dataTemplate.getName()+"_"+currentTime);
			dataTemplateDraft.setId(UniqueIdUtil.getSuid());
			dataTemplateDraft.setCreateBy(currentUser.getUserId());
			dataTemplateDraft.setCreateTime(LocalDateTime.now());
			dataTemplateDraftManager.create(dataTemplateDraft);
		}else{
			dataTemplateDraftManager.update(dataTemplateDraft);
		}
	}
	
	private void handleBoResult(List<BoResult> resultList, ObjectNode data) throws Exception {
		List<ObjectNode> list = new ArrayList<ObjectNode>();
		for(BoResult result:resultList) {
			list.add((ObjectNode)JsonUtil.toJsonNode(result));
		}
		Map<String, Object> params = new HashMap<String, Object>();
		ArrayNode boResult = JsonUtil.listToArrayNode(list);
		params.put("boResult", boResult);
		params.put("data", data);
		bpmRuntimeFeignService.handleBoDateModify(params);
	}

	@Override
	@Transactional
	public void boDel(String[] ids, String boAlias) {
		BoDataHandler handler= boInstanceFactory.getBySaveType(BoConstants.SAVE_MODE_DB);
		handler.removeBoData(boAlias, ids);
	}

	@Override
	public List<FormDataTemplate> getTemplateByFormKey(String formKey) {
		return baseMapper.getByFormKey(formKey);
	}

	@Override
	public FormDataTemplate getExportDisplay(String alias) throws IOException {
		FormDataTemplate bpmDataTemplate = baseMapper.getByAlias(alias);
		if(BeanUtils.isNotEmpty(bpmDataTemplate)){
			ArrayNode jsonAry = (ArrayNode) JsonUtil.toJsonNode(bpmDataTemplate.getDisplayField());
			Map<String, Set<String>> curProfiles =  permssionCalc.getCurrentProfiles();
			if(BeanUtils.isNotEmpty(jsonAry) && jsonAry.size()>0){
				ArrayNode newjsonAry = JsonUtil.getMapper().createArrayNode();
				for (Object obj : jsonAry) {
					ObjectNode json = (ObjectNode)obj;
					ArrayNode rights = (ArrayNode)JsonUtil.toJsonNode(json.get("right").asText());
					boolean hasRight = false;
					for (JsonNode permission : rights) {
						hasRight = permssionCalc.hasRight(permission.toString(), curProfiles);
						break;
					}
					json.put("permission", hasRight);
					newjsonAry.add(json);
				}
				bpmDataTemplate.setDisplayField(newjsonAry.toString());
			}
		}
		return bpmDataTemplate;
	}

	@Override
	public String getFilterSql(String filterField,String dsName,Map<String, Object> param) throws IOException {
		StringBuffer sb = new StringBuffer();
		String sql = "";
		Map<String, Set<String>> curProfiles =  permssionCalc.getCurrentProfiles();
		List<Map<String,String>> filters = getFilterPermission(filterField,curProfiles);
		ArrayNode jsonArray = (ArrayNode) JsonUtil.toJsonNode(filterField);
		ObjectNode json = JsonUtil.arrayToObject(jsonArray, "key");
		if(BeanUtils.isEmpty(filters)) return sb.toString();
		for (Map<String,String> map : filters) {
			ObjectNode jsonObject = (ObjectNode) json.get(map.get("filterKey"));
			int type = JsonUtil.getInt(jsonObject, "type", 0);
			if (2==type) {// 过滤条件是SQL替代，直接返回
				return executeScript(jsonObject.get("condition").asText(), param);
			}else if(1==type){// 条件脚本
				String dbType = databaseContext.getDbTypeByAlias(dsName);
				sql = FilterJsonStructUtil.getSql(JsonUtil.getString(jsonObject, "condition"), dbType);
			}else if(3==type){//追加SQL
				sql = executeScript(jsonObject.get("condition").asText(), param);
			}else if(4==type){//数据权限
				sql = getDataPermissionSql(jsonObject.get("condition").asText(), "");
			}
			if(StringUtil.isNotEmpty(sql)){
				if(4!=type){
					sb.append(" AND ");
				}
				sb.append(sql);
			}
		}
		return sb.toString();
	}

	@Override
	public String getDataPermissionSql(String dataPermission,String fieldPre) throws IOException {
		StringBuffer sb = new StringBuffer();
		if(StringUtil.isNotEmpty(dataPermission)){
			ArrayNode permissionArrayJson = (ArrayNode) JsonUtil.toJsonNode(dataPermission);
			Set<String> orgIds = new HashSet<String>();
			IUser currentUser = ContextUtil.getCurrentUser();
			//  获取数据权限配置 从缓存中获取
			for (JsonNode node : permissionArrayJson) {
				if(BeanUtils.isNotEmpty(node.get("field"))){
					if(LOGIN_USER.equals(node.get("type").asText())){
						sb.append(" AND "+fieldPre+node.get("field").asText()+"='"+currentUser.getUserId()+"'");
					}else if(LOGIN_USER_ORGS.equals(node.get("type").asText())){
						String currentUserOrgIds = currentUser.getAttrbuite("CURRENT_USER_ORGIDS");
						if(StringUtil.isNotEmpty(currentUserOrgIds)){
							String[] oids = currentUserOrgIds.split(",");
							Set<String> oidSet = new HashSet<String>(Arrays.asList(oids));
							String inSql = StringUtil.convertListToSingleQuotesString(oidSet);
							sb.append(" AND "+fieldPre+node.get("field").asText()+" in ("+inSql+")");
							orgIds.addAll(oidSet);
						}
					}else if(LOGIN_USER_SUB_ORGS.equals(node.get("type").asText())){
						String currentUserSubOrgIds = StringUtil.isNotEmpty(AuthenticationUtil.getCurrentUserSubOrgIds())?AuthenticationUtil.getCurrentUserSubOrgIds():"";
						String currentUserOrgIds = StringUtil.isNotEmpty(AuthenticationUtil.getCurrentUserOrgIds())?AuthenticationUtil.getCurrentUserOrgIds():"";
						currentUserSubOrgIds += "," + currentUserOrgIds;
						String[] oids = new String[]{};
						if(StringUtil.isNotEmpty(currentUserSubOrgIds)){
							oids = currentUserSubOrgIds.split(",");
						}
						if (oids.length==0){
							oids = new String[]{"-1"};
						}
						Set<String> oidSet = new HashSet<String>(Arrays.asList(oids));
						String inSql = StringUtil.convertListToSingleQuotesString(oidSet);
						sb.append(" AND "+fieldPre+node.get("field").asText()+" in ("+inSql+")");
						orgIds.addAll(oidSet);
					}else if(CUSTOM_ORGS.equals(node.get("type").asText())){
						ArrayNode tmpArray = (ArrayNode)node.get("orgs");
						for (JsonNode tmpJsonNode : tmpArray) {
							orgIds.add(tmpJsonNode.get("id").asText());
						}
						String inSql = StringUtil.convertListToSingleQuotesString(orgIds);
						sb.append(" AND "+fieldPre+node.get("field").asText()+" in ("+inSql+")");
					}
				}
			}
		}
		return sb.toString();
	}


	/**
	 * 字符串的常量
	 *
	 * @param script
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	private String executeScript(String script, Map<String, Object> param) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("param", param);
		vars.putAll(param);
		String str = groovyScriptEngine.executeString(replaceVar(script), vars);
		return str;
	}

	private String replaceVar(String str) {
		//当前用户id
		@SuppressWarnings("unchecked")
		List<IContextVar> comVarList = (List<IContextVar>) AppUtil.getBean("queryViewComVarList");
		for (IContextVar c : comVarList) {
			str = str.replace("[" + c.getAlias() + "]", c.getValue());
		}
		return "return \"" + str + "\" ;";
	}

	@Override
	public Set<String> getAllFormKeys() {
		Set<String> formSets = new HashSet<String>();
		List<String> formKeys = baseMapper.getAllFormKeys();
		if(BeanUtils.isNotEmpty(formKeys)){
			formSets = new HashSet<String>(formKeys);
		}
		return formSets;
	}

	@Override
	@Transactional
	public void importData(List<MultipartFile> files, String refId, String alias) throws Exception{
		Iterator<MultipartFile> it = files.iterator();
		while (it.hasNext()) {
			MultipartFile file = it.next();
			List<Map<String, String>> rows = ExcelUtil.ImportDate(file);
			BoEnt boEnt = boEntManager.getByName(alias);
			JdbcTemplate template = DataSourceUtil.getJdbcTempByDsAlias(boEnt.getDsName()==null?DataSourceConsts.LOCAL_DATASOURCE:boEnt.getDsName());
			for (Map<String,String> row:rows){
				int i=0;
				StringBuilder excuteSql = preSql(boEnt);
				excuteSql.append(UniqueIdUtil.getSuid()+",");
				excuteSql.append(refId);
				for (Map.Entry<String,String> map:row.entrySet()){
					if ("varchar".equals(((BoAttribute)boEnt.getColumnList().get(i++)).getDataType())){
						excuteSql.append(",'"+map.getValue()+"'");
					}else{
						excuteSql.append(","+map.getValue());
					}
				}
				excuteSql.append(",0)");
				template.execute(excuteSql.toString());
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void importMain(List<MultipartFile> files, String alias) throws Exception {
		FormDataTemplate template = this.getByAlias(alias);
		BoDef boDef = this.boDefService.getByDefId(template.getBoDefId());
		BoEnt boEnt = boDef.getBoEnt();
		Iterator<MultipartFile> it = files.iterator();
		while (it.hasNext()) {
			MultipartFile file = it.next();
			List<Map<String, String>> rows = ExcelUtil.ImportDate(file);
			JdbcTemplate jdbcTemplate = DataSourceUtil.getJdbcTempByDsAlias(boEnt.getDsName()==null?DataSourceConsts.LOCAL_DATASOURCE:boEnt.getDsName());
			for (Map<String,String> row:rows){
				StringBuffer sql = new StringBuffer("insert into ");
				
				List<String> fields = new ArrayList<String>();
				List<String> values = new ArrayList<String>();
				for(Entry<String, String> map : row.entrySet()){
					if("主键".equals(map.getKey())){
						fields.add(boEnt.getPkKey());
						if(BeanUtils.isNotEmpty(row.get("主键"))){
							values.add("'"+row.get("主键")+"'");
						}else{
							values.add(UniqueIdUtil.getSuid());
						}
					}else{
						//excel单元格有值才去拼接sql
						if(BeanUtils.isNotEmpty(map.getValue())){
							String field = this.getFieldName(map.getKey(),boEnt.getColumnList());
							fields.add(field);
							String dataType = this.getDataType(map.getKey(),boEnt.getColumnList());
							if("number".equals(dataType)){
								values.add(map.getValue());
							}else{
								values.add("'"+map.getValue()+"'");
							}
						}
					}
				}
				//判断如果excel中无[主键]列，则补充完整
				if(!fields.contains(boEnt.getPkKey())){
					fields.add(boEnt.getPkKey());
					values.add(UniqueIdUtil.getSuid());
				}
				//加入额外的两个字段
				fields.add(BoEnt.FK_NAME);
				values.add("0");
				fields.add("F_form_data_rev_");
				values.add("0");
				
				sql.append(boEnt.getTableName());
				sql.append("("+ String.join(",", fields) +")");
				sql.append(" values ");
				sql.append("("+ String.join(",", values) +")");
				
				jdbcTemplate.execute(sql.toString());
			}
		}
	}

	private String getFieldName(String key, List<BoAttribute> columnList) {
		for(BoAttribute boAtt: columnList){
			if(boAtt.getDesc().equals(key)){
				return boAtt.getFieldName();
			}
		}
		return "";
	}

	private String getDataType(String key, List<BoAttribute> columnList) {
		for(BoAttribute boAtt: columnList){
			if(boAtt.getDesc().equals(key)){
				return boAtt.getDataType();
			}
		}
		return "";
	}

	@Override
	public Map<String, Object> getFormData(String formKey, String boAlias, String id, String action, String recordId) throws Exception {
		int type = "get".equals(action)?2:1;
		Map<String,Object> map = getFormByFormKey(formKey);
		if("formEmpty".equals(map.get("result"))){
			return map;
		}
		// 表单权限
		map.put("permission", bpmFormRightManager.getPermission(formKey, "", "", "", type,true));
		// 表单数据
		List<BoData> boDatas =  new ArrayList<BoData>();
		if(StringUtil.isNotEmpty(id)){
			//表单修改记录数据
			if(StringUtil.isNotEmpty(recordId)) {
				ObjectNode record = bpmRuntimeFeignService.getModifyById(recordId);
				if(BeanUtils.isNotEmpty(record)) {
					if(BeanUtils.isNotEmpty(record.get("data"))) {
						String data = record.get("data").asText();
						if(StringUtil.isNotEmpty(data)) {
							JsonNode formData = JsonUtil.toJsonNode(data);
							map.put("data", formData);
							return map;
						}
					}
				}
			}
			// 获取编辑数据
			BoData boData = boDataHandler.getById(id, boAlias);
			if(StringUtil.isEmpty(boData.getBoDefAlias())){
				boData.setBoDefAlias(boData.getBoDef().getAlias());
			}
			boDatas = Arrays.asList(boData);
		}else{
			for (String code : Arrays.asList(boAlias)){
				BoData boData = formService.getBodataByDefCode("database", code);
				if(StringUtil.isEmpty(boData.getBoDefAlias())){
					boData.setBoDefAlias(boData.getBoDef().getAlias());
				}
				if(BeanUtils.isNotEmpty(boData)){
					boDatas.add(boData);
				}
			}
		}
		JsonNode object = BoUtil.hanlerData(boDatas);
		// 表单数据
		map.put("data", object);

		return map;
	}
	@Override
	public Map<String, Object> getTempDraftData(String draftId) throws IOException {
		FormDataTemplateDraft formDataTemplateDraft = dataTemplateDraftManager.get(draftId);
		FormDataTemplate formDataTemplate = this.getByAlias(formDataTemplateDraft.getTempAlias());
		
		Map<String,Object> map = getFormByFormKey(formDataTemplate.getFormKey());
		if("formEmpty".equals(map.get("result"))){
			return map;
		}
		// 表单权限
		map.put("permission", bpmFormRightManager.getPermission(formDataTemplate.getFormKey(), "", "", "", 1,true));
		// 表单数据
		JsonNode object =  JsonUtil.toJsonNode(formDataTemplateDraft.getDataJson());
		// 表单数据
		map.put("data", object);
		map.put("draft",formDataTemplateDraft);
		return map;
	}

	@Override
	public void exportData(HttpServletResponse response, String formKey, String getType, String filterKey, String expField, QueryFilter queryFilter) throws Exception {
		FormDataTemplate template = getByAlias(formKey);
		getType = StringUtil.isEmpty(getType)?"getType":getType;
		PageBean page = new PageBean(1, Integer.MAX_VALUE-1);
		if("page".equals(getType)){
			if(template.getNeedPage().toString().equals("1")){
				page.setPageSize(Integer.valueOf(template.getPageSize().toString()));
			}
		}
		expField = Base64.getFromBase64(expField);
		queryFilter.setPageBean(page);
		filterKey = StringUtil.isNotEmpty(filterKey)?filterKey:"";
		PageList<Map<String, Object>> pageList = getList(template,queryFilter,filterKey);
		// 拼装exprotMaps
		Map<String, String> exportMaps = new LinkedHashMap<String, String>();
		ArrayNode showJA = (ArrayNode) JsonUtil.toJsonNode(template.getDisplayField());
		ObjectNode showJO = JsonUtil.arrayToObject(showJA, "name");
		for (String str : expField.split(",")) {
			exportMaps.put(str, showJO.get(str).get("desc").asText());
		}
		HSSFWorkbook book = ExcelUtil.exportExcel(template.getName(), 24, exportMaps, pageList.getRows());
		ExcelUtil.downloadExcel(book, template.getName(), response);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void downloadMainTempFile(HttpServletResponse response, String alias) throws Exception {
		FormDataTemplate template = this.getByAlias(alias);
		
		BoDef boDef = this.boDefService.getByDefId(template.getBoDefId());
		BoEnt boEnt = boDef.getBoEnt();
		Map<String, String> exportMaps = new LinkedHashMap<String, String>();
		//加入主键列
		exportMaps.put(boEnt.getPkKey(), "主键");
		
		List<BoAttribute> boAttrList = boEnt.getColumnList();
		for(BoAttribute boAttr: boAttrList){
			exportMaps.put(boAttr.getName(), boAttr.getDesc());
		}
		// 拼装exprotMaps
		HSSFWorkbook book = ExcelUtil.exportExcel(template.getName(), 24, exportMaps, new ArrayList<>());
		
		ExcelUtil.downloadExcel(book, template.getName()+"_导入模板", response);
	}

	@Override
	public PageList getList(FormDataTemplate template, DataTemplateQueryVo dataTemplateQueryVo) throws Exception {
		PageList<Map<String, Object>> returnData = new PageList<Map<String,Object>>();
		QueryFilter queryFilter=dataTemplateQueryVo.getQueryFilter();
		BpmRuntimeFeignService service = AppUtil.getBean(BpmRuntimeFeignService.class);
		List<Map<String,Object>> flowBpmList=null;
		Map<String, Object> flowFields=new LinkedHashMap<String, Object>();
		String flowQuery="";
		if(template.getNeedPage()==1 && BeanUtils.isEmpty(queryFilter.getPageBean())){
			queryFilter.setPageBean(new PageBean(1, 30, true));
		}
		returnData.setRows(new ArrayList<Map<String, Object>>());

		//数据视图控件查询
		if(StringUtil.isNotEmpty(dataTemplateQueryVo.getSelectField())) {
			String fieldName = "F_" + dataTemplateQueryVo.getSelectField();
			if(BeanUtils.isEmpty(dataTemplateQueryVo.getSelectValue())) {
				returnData.setPage(1);
				if(template.getNeedPage()==1){
					returnData.setPageSize(template.getPageSize());
				}else {
					returnData.setPageSize(Integer.MAX_VALUE-1);
				}
				return returnData;
			}
			queryFilter.addFilter(fieldName, dataTemplateQueryVo.getSelectValue(), QueryOP.EQUAL);
		}

		//过滤流程字段查询
		QueryFilter flowQueryFilter=QueryFilter.build();
		boolean isFlowBpmField=false;

		Iterator<QueryField> it = queryFilter.getQuerys().iterator();
		while (it.hasNext())
		{
			QueryField flowObj = it.next();
			if(ArrayUtils.contains(flowField,flowObj.getProperty())) {
				if(flowObj.getValue()!=null) {
					isFlowBpmField=true;

					String flowProperty=flowObj.getProperty().substring(6);
					flowQueryFilter.addFilter(flowProperty, flowObj.getValue(), flowObj.getOperation());
					it.remove();
				}else {
					it.remove();
				}

			}
		}
		//添加流程定义查询
		if(StringUtil.isNotEmpty(template.getDefId())){
			flowQueryFilter.addFilter("proc_def_key_", template.getDefId(), QueryOP.EQUAL);
		}
		//
		if(isFlowBpmField) {//流程字段查询
			flowBpmList=service.getFlowFieldList(flowQueryFilter);
			if(BeanUtils.isNotEmpty(flowBpmList)) {
				for(Map<String,Object> oNode:flowBpmList) {
					//添加查询字段信息
					String id=oNode.get("id_").toString();
					flowQuery=flowQuery+id+",";

					flowFields.put(id, oNode);
				}
				queryFilter.addFilter("ID_", flowQuery, QueryOP.IN);
			}else {
				returnData.setPage(1);
				if(template.getNeedPage()==1){
					returnData.setPageSize(template.getPageSize());
				}else {
					returnData.setPageSize(Integer.MAX_VALUE-1);
				}

				return returnData;
			}
		}else {//表单字段查询
			flowBpmList=service.getFlowFieldList(flowQueryFilter);
			if(BeanUtils.isNotEmpty(flowBpmList)) {
				for(Map<String,Object> oNode:flowBpmList) {
					//添加查询字段信息
					String id=oNode.get("id_").toString();

					flowFields.put(id, oNode);
				}
			}
		}

		Map<String, Object> params = queryFilter.getParams();
		BoDef boDef = boDefService.getByAlias(template.getBoDefAlias());
		BoEnt boEnt = (BoEnt)boDef.getBoEnt();
		String dsName = boEnt.getDsName();
		if(StringUtil.isEmpty(dsName)){
			dsName = DataSourceConsts.LOCAL_DATASOURCE;
		}

		String showSql = " select *  from " + boEnt.getTableName();
		if(dataTemplateQueryVo.isJoinFlow()){
			String pk="id_";
			if(StringUtil.isNotEmpty(boEnt.getPk())){
				pk=boEnt.getPk();
			}
			ObjectNode nodes=JsonUtil.getMapper().createObjectNode();
			nodes.put("defKey", dataTemplateQueryVo.getDefKey());
			nodes.put("taskType", dataTemplateQueryVo.getTaskType());
			List<String> listIds=bpmRuntimeFeignService.getBusLink(nodes);
			if(listIds.size()>0){
				queryFilter.addFilter(pk, listIds, QueryOP.IN);
			}else{
				return returnData;
			}
		}

		//排序处理（字段前缀）
		if(BeanUtils.isNotEmpty(queryFilter.getSorter())){
			String colPrefix = boEnt.isExternal()? "":SQLConst.CUSTOMER_COLUMN_PREFIX;
			List<FieldSort> sorter = queryFilter.getSorter();
			if(sorter!=null) {
				sorter.forEach(i -> {
					i.setProperty(colPrefix+i.getProperty());
				});
			}
		}
		//处理排序等其他条件
		queryFilter = getTemplateQueryFilter(queryFilter,template,boEnt);
		String filterSql = getFilterSql(template.getFilterField(),dsName,params);
		// 处理分页
		PageList<Map<String, Object>> list = null;
		try (DatabaseSwitchResult result = databaseContext.setDataSource(dsName)){
			list = (PageList<Map<String, Object>>) query(showSql, queryFilter, filterSql);
		} catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
		if(BeanUtils.isNotEmpty(list)){
			returnData.setPage(list.getPage());
			returnData.setPageSize(list.getPageSize());
			for (Map<String, Object> rowMap : list.getRows()) {
				Map<String, Object> rtnMap = convertDbToData(boEnt, rowMap);
				rtnMap.put("isStartFlow", true);
				String pkKey = boEnt.getPkKey();
				if("0".equals(boEnt.getIsExternal().toString())){
					if(StringUtil.isNotEmpty(pkKey)){
						pkKey = pkKey.toUpperCase();
					}else{
						pkKey = "ID_";
					}
				}
				String businesKey = String.valueOf(rowMap.get(pkKey));
				if(StringUtil.isNotEmpty(businesKey)){
					ObjectNode bblm = service.getByBusinesKey(String.valueOf(rowMap.get(pkKey)),"", "number".equals(boEnt.getPkType()));
					if(BeanUtils.isEmpty(bblm)){
						rtnMap.put("isStartFlow", false);
					}
				}
				//处理流程字段
				Map<String,Object> flowFieldTest=(Map<String, Object>) flowFields.get(businesKey);
				if(BeanUtils.isNotEmpty(flowFieldTest)) {
					rtnMap.put("bpm_proc_inst_id_", flowFieldTest.get("proc_inst_id_").toString());
					rtnMap.put("bpm_subject_", flowFieldTest.get("subject_").toString());
					rtnMap.put("bpm_proc_def_name_", flowFieldTest.get("proc_def_name_").toString());
					rtnMap.put("bpm_status_", flowFieldTest.get("status_").toString());

					//转换时间格式
					if(flowFieldTest.get("create_time_")!=null) {
						rtnMap.put("bpm_create_time_", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flowFieldTest.get("create_time_")));
					}
					if(flowFieldTest.get("end_time_")!=null) {
						rtnMap.put("bpm_end_time_", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flowFieldTest.get("end_time_")));
					}

					rtnMap.put("bpm_is_forbidden_", flowFieldTest.get("is_forbidden_").toString());
					rtnMap.put("bpm_creator_", flowFieldTest.get("creator_").toString());
					rtnMap.put("bpm_is_dele_", flowFieldTest.get("is_dele_").toString());
				}


				returnData.getRows().add(rtnMap);
				returnData.setTotal(list.getTotal());
			}
		}
		return returnData;
	}

	@Override
	public PageList<Map<String, Object>> getSubDataPagination(QueryFilter queryFilter, String alias, String refId) throws Exception {
		if (StringUtil.isNotEmpty(refId) && StringUtil.isNotEmpty(alias)){
			return getSubList(queryFilter,alias, refId);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getSubData(String alias, String refId) throws Exception {
		if(StringUtil.isNotEmpty(refId) && StringUtil.isNotEmpty(alias)){
			return getSubList(alias, refId);
		}
		return null;
	}

	@Override
	public void exportSub(HttpServletResponse response, ExportSubVo exportVo) throws Exception {
		String getType = StringUtil.isEmpty(exportVo.getType())?"getType":exportVo.getType();
		PageBean page = new PageBean(1, Integer.MAX_VALUE-1);
		if("page".equals(getType)){
			page.setPageSize(10);
		}
		QueryFilter queryFilter = exportVo.getQueryFilter();
		queryFilter.setPageBean(page);
		PageList<Map<String, Object>> pageList = getSubList(queryFilter,exportVo.getAlias(),exportVo.getRefId());
		// 拼装exprotMaps
		Map<String, String> exportMaps = new LinkedHashMap<String, String>();
		for (JsonNode str : (ArrayNode) JsonUtil.toJsonNode(exportVo.getExpField())) {
			exportMaps.put(str.get("key").asText(), str.get("value").asText());
		}
		HSSFWorkbook book = ExcelUtil.exportExcel(exportVo.getAlias(), 24, exportMaps, pageList.getRows());
		ExcelUtil.downloadExcel(book, exportVo.getAlias(), response);
	}

	@SuppressWarnings("unchecked")
	public StringBuilder preSql(BoEnt boEnt){
		StringBuilder sql = new StringBuilder("insert into ");
		sql.append(boEnt.getTableName());
		sql.append(" (id_,ref_id_");
		List<?> columnList = boEnt.getColumnList();
		if(columnList!=null) {
			((List<BoAttribute>)columnList).forEach(i -> {
				sql.append(","+i.getFieldName());
			});
		}
		sql.append(",F_form_data_rev_");
		sql.append(") values (");
		return sql;
	}

	//生成流程字段
	private void addFieldList(List<FormField> fields) {
		String[] fileArr=new String[] {"流程编号","标题","流程名称","实例状态","创建时间","结束时间","是否挂起","发起人","是否删除"};
		String[] nameArr=new String[] {"bpm_proc_inst_id_","bpm_subject_","bpm_proc_def_name_","bpm_status_","bpm_create_time_","bpm_end_time_","bpm_is_forbidden_","bpm_creator_","bpm_is_dele_"};
		String[] typeArr=new String[] {"varchar","varchar","varchar","varchar","data","data","varchar","varchar","varchar"};

		for(int i=0;i<fileArr.length;i++) {
			FormField field=new FormField();
			field.setName(nameArr[i]);
			field.setDesc(fileArr[i]);
			field.setType(typeArr[i]);
			field.setShowFlowField(true);
			fields.add(field);
		}
	}

	@Override
	public FormDataTemplate getByAlias(String alias) {
		return baseMapper.getByAlias(alias);
	}

	@Override
	public void removeByFormKey(String formKey) {
		baseMapper.removeByFormKey(formKey);
	}

	/**
	 * 将从数据库读取的数据到实例数据。
	 *
	 * @param boEnt
	 * @param map
	 * @return
	 */
	private Map<String, Object> convertDbToData(BoEnt boEnt, Map<String, Object> map) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		for (Map.Entry<String, Object> ent : map.entrySet()) {
			String field = ent.getKey().toLowerCase();
			BoAttribute attribute = boEnt.getAttrByField(field);
			if(BeanUtils.isNotEmpty(attribute)){
				// 处理日期。
				Object val = handValue(attribute, ent.getValue());
				rtnMap.put(attribute.getName(), val);
			}
		}
		return rtnMap;
	}

	/**
	 * 数据根据bo属性处理。
	 *
	 * @param attr
	 * @param val
	 * @return
	 */
	private Object handValue(BoAttribute attr, Object val) {
		if (BeanUtils.isEmpty(val))
			return val;
		String format = attr.getFormat();
		if(val instanceof Timestamp){
			Timestamp times = (Timestamp) val;
			return TimeUtil.getDateTimeString(times.toLocalDateTime(), format);
		}else if(val instanceof java.util.Date){
			return TimeUtil.getDateTimeString(DateFormatUtil.parse((Date)val), format);
		}else{
			return val;
		}
	}

	private QueryFilter getTemplateQueryFilter(QueryFilter queryFilter,FormDataTemplate bpmDataTemplate,BoEnt boEnt) throws Exception{
		if(BeanUtils.isNotEmpty(bpmDataTemplate)){
			// 是否分页
			PageBean page = queryFilter.getPageBean();
			if(2 == bpmDataTemplate.getNeedPage()){
				page.setPageSize(Integer.MAX_VALUE-1);
			} else {
				page.setPageSize(queryFilter.getPageBean().getPageSize());
			}
			queryFilter.setPageBean(page);
			// 排序
			String sortField = bpmDataTemplate.getSortField();
			if(StringUtil.isNotZeroEmpty(sortField)){
				//获取字段前缀
				String colPrefix = boEnt.isExternal()?"":SQLConst.CUSTOMER_COLUMN_PREFIX;
				ArrayNode array = (ArrayNode) JsonUtil.toJsonNode(sortField);
				for(int i=0;i<array.size();i++){
					ObjectNode obj = (ObjectNode) array.get(i);
					queryFilter.getSorter().add(new FieldSort(colPrefix+obj.get("name").asText(), Direction.fromString(obj.get("sort").asText())));
				}
			}
		}
		return queryFilter;
	}

	private PageList<?> query(String sql, QueryFilter queryFilter,String filterSql) throws SystemException {
		Assert.notNull(sql, "sql can not be empty.");
		Assert.notNull(queryFilter, "queryFilter can not be empty.");
		if(StringUtil.isNotEmpty(filterSql)){
			queryFilter.addParams(filterSql, null);
		}
		queryFilter.withParam("filterSql",filterSql);
		return commonManager.queryByCustomSql(sql, queryFilter);
	}

	private PageList getList(FormDataTemplate template,QueryFilter queryFilter,String filterKey) throws Exception{
		PageList<Map<String, Object>> returnData = new PageList<Map<String,Object>>();
		BpmRuntimeFeignService service = AppUtil.getBean(BpmRuntimeFeignService.class);
		List<Map<String,Object>> flowBpmList=null;
		Map<String, Object> flowFields=new LinkedHashMap<String, Object>();
		String flowQuery="";
		if(template.getNeedPage()==1 && BeanUtils.isEmpty(queryFilter.getPageBean())){
			queryFilter.setPageBean(new PageBean(1, 30, true));
		}
		returnData.setRows(new ArrayList<Map<String, Object>>());
		//过滤流程字段查询
		QueryFilter flowQueryFilter=QueryFilter.build();
		boolean isFlowBpmField=false;
		Iterator<QueryField> it = queryFilter.getQuerys().iterator();
		while (it.hasNext())
		{
			QueryField flowObj = it.next();
			if(ArrayUtils.contains(flowField,flowObj.getProperty())) {
				if(flowObj.getValue()!=null) {
					isFlowBpmField=true;
					String flowProperty=flowObj.getProperty().substring(6);
					flowQueryFilter.addFilter(flowProperty, flowObj.getValue(), flowObj.getOperation());
					it.remove();
				}else {
					it.remove();
				}
			}
		}
		//添加流程定义查询
		flowQueryFilter.addFilter("proc_def_key_", template.getDefId(), QueryOP.EQUAL);
		//
		if(isFlowBpmField) {//流程字段查询
			flowBpmList=service.getFlowFieldList(flowQueryFilter);
			if(BeanUtils.isNotEmpty(flowBpmList)) {
				for(Map<String,Object> oNode:flowBpmList) {
					//添加查询字段信息
					String id=oNode.get("id_").toString();
					flowQuery=flowQuery+id+",";
					flowFields.put(id, oNode);
				}
				queryFilter.addFilter("ID_", flowQuery, QueryOP.IN);
			}else {
				returnData.setPage(1);
				if(template.getNeedPage()==1){
					returnData.setPageSize(template.getPageSize());
				}else {
					returnData.setPageSize(Integer.MAX_VALUE-1);
				}
				return returnData;
			}
		}else {//表单字段查询
			flowBpmList=service.getFlowFieldList(flowQueryFilter);
			if(BeanUtils.isNotEmpty(flowBpmList)) {
				for(Map<String,Object> oNode:flowBpmList) {
					//添加查询字段信息
					String id=oNode.get("id_").toString();
					flowFields.put(id, oNode);
				}
			}
		}
		Map<String, Object> params = queryFilter.getParams();
		BoDef boDef = boDefService.getByAlias(template.getBoDefAlias());
		BoEnt boEnt = (BoEnt)boDef.getBoEnt();
		String dsName = boEnt.getDsName();
		if(StringUtil.isEmpty(dsName)){
			dsName = DataSourceConsts.LOCAL_DATASOURCE;
		}
		String showSql = " select *  from " + boEnt.getTableName();
		//五合一版本sql
		//String showSql = " select t1.*,t2.proc_inst_id_,t3.subject_,t3.proc_def_name_,t3.status_,t3.create_time_,t3.end_time_,t3.is_forbidden_,t3.creator_,t3.is_dele_ from " + boEnt.getTableName() + " t1 inner join bpm_bus_link t2 on t1.ID_=t2.BUSINESSKEY_STR_ inner join bpm_pro_inst t3 on t2.PROC_INST_ID_=t3.ID_";
		//排序处理（字段前缀）
		if(BeanUtils.isNotEmpty(queryFilter.getSorter())){
			String colPrefix = boEnt.isExternal()? "":SQLConst.CUSTOMER_COLUMN_PREFIX;
			List<FieldSort> sorter = queryFilter.getSorter();
			if(sorter!=null) {
				sorter.forEach(i -> {
					i.setProperty(colPrefix+i.getProperty());
				});
			}
		}
		//处理排序等其他条件
		queryFilter = getTemplateQueryFilter(queryFilter,template,boEnt);
		String filterSql = getFilterSql(template.getFilterField(),dsName,params);
		// 处理分页
		PageList<Map<String, Object>> list = null;
		try (DatabaseSwitchResult result = databaseContext.setDataSource(dsName)){
			list = (PageList<Map<String, Object>>) query(showSql, queryFilter, filterSql);
		} catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
		if(BeanUtils.isNotEmpty(list)){
			returnData.setPage(list.getPage());
			returnData.setPageSize(list.getPageSize());
			for (Map<String, Object> rowMap : list.getRows()) {
				Map<String, Object> rtnMap = convertDbToData(boEnt, rowMap);
				rtnMap.put("isStartFlow", true);
				String pkKey = boEnt.getPkKey();
				if("0".equals(boEnt.getIsExternal().toString())){
					if(StringUtil.isNotEmpty(pkKey)){
						pkKey = pkKey.toUpperCase();
					}else{
						pkKey = "ID_";
					}
				}
				String businesKey = String.valueOf(rowMap.get(pkKey));
				if(StringUtil.isNotEmpty(businesKey)){
					ObjectNode bblm = service.getByBusinesKey(String.valueOf(rowMap.get(pkKey)),"", "number".equals(boEnt.getPkType()));
					if(BeanUtils.isEmpty(bblm)){
						rtnMap.put("isStartFlow", false);
					}
				}
				//处理流程字段
				Map<String,Object> flowFieldTest=(Map<String, Object>) flowFields.get(businesKey);
				if(BeanUtils.isNotEmpty(flowFieldTest)) {
					rtnMap.put("bpm_proc_inst_id_", flowFieldTest.get("proc_inst_id_").toString());
					rtnMap.put("bpm_subject_", flowFieldTest.get("subject_").toString());
					rtnMap.put("bpm_proc_def_name_", flowFieldTest.get("proc_def_name_").toString());
					rtnMap.put("bpm_status_", flowFieldTest.get("status_").toString());
					//转换时间格式
					if(flowFieldTest.get("create_time_")!=null) {
						rtnMap.put("bpm_create_time_", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flowFieldTest.get("create_time_")));
					}
					if(flowFieldTest.get("end_time_")!=null) {
						rtnMap.put("bpm_end_time_", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flowFieldTest.get("end_time_")));
					}
					rtnMap.put("bpm_is_forbidden_", flowFieldTest.get("is_forbidden_").toString());
					rtnMap.put("bpm_creator_", flowFieldTest.get("creator_").toString());
					rtnMap.put("bpm_is_dele_", flowFieldTest.get("is_dele_").toString());
				}
				returnData.getRows().add(rtnMap);
				returnData.setTotal(list.getTotal());
			}
		}
		return returnData;
	}



	private PageList<Map<String,Object>> getSubList(QueryFilter queryFilter,String alias,String refId) throws Exception{
		// 处理分页
		PageList<Map<String, Object>> pageList = new PageList<>();
		BoEnt boEnt = boEntManager.getByName(alias);
		String dsName = boEnt.getDsName();
		if (StringUtil.isEmpty(dsName)){
			dsName = DataSourceConsts.LOCAL_DATASOURCE;
		}
		String showSql = "select * from " + boEnt.getTableName();
		if (StringUtil.isNotEmpty(refId)){
			queryFilter.addFilter(boEnt.getFk(),refId,QueryOP.EQUAL);
		}
		try (DatabaseSwitchResult result = databaseContext.setDataSource(dsName)){
			pageList = (PageList<Map<String, Object>>) query(showSql, queryFilter, "");
		} catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
		List<Map<String,Object>> list = pageList.getRows();
		convertToDateTime(list,boEnt.getColumnList());
		pageList.setRows(list);
		if(BeanUtils.isNotEmpty(pageList)){
			return pageList;
		}
		return pageList;
	}

	private void convertToDateTime(List<Map<String,Object>> list, List<BoAttribute> columnList){
		for(Map<String,Object> map : list){
			for (BoAttribute column: columnList){
				if ("date".equals(column.getDataType())){
					if (BeanUtils.isNotEmpty(map.get(column.getFieldName()))){
						map.put(column.getFieldName(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get(column.getFieldName())));
					}
				}
			}
		}
	}

	private List<Map<String, Object>> getSubList(String alias,String refId) throws Exception{
		QueryFilter queryFilter = QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
		PageList pageList = getSubList(queryFilter,alias,refId);
		return pageList.getRows();
	}

    @Override
    public Map<String, String> exportDef(List<String> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        FormDataTemplateXmlList formDataTemplateXmlList = new FormDataTemplateXmlList();
        for (String id : list) {
            FormDataTemplateXml formDataTemplateXml = getByTemplateId(id);
            formDataTemplateXmlList.addFormDataTemplateXml(formDataTemplateXml);
        }
        try {
            String xml = JAXBUtil.marshall(formDataTemplateXmlList, FormDataTemplateXmlList.class);
            map.put("formDataTemplates.form.xml", xml);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败！" + e.getMessage(), e);
        }
        return map;
    }

    @Override
    @CachePut(value = "formDataTemplate:importFile", key="#fileId",
            firstCache = @FirstCache(expireTime = 1, timeUnit = TimeUnit.HOURS))
    public String putImportFileInCache(String fileId, String fileJson) {
        return fileJson;
    }

    @Override
    @Cacheable(value = "formDataTemplate:importFile", key="#fileId",
            firstCache = @FirstCache(expireTime = 1, timeUnit = TimeUnit.HOURS))
    public String getImportFileFromCache(String fileId) {
        return null;
    }

    @Override
    @CacheEvict(value = "formDataTemplate:importFile", key="#fileId")
    public void delImportFileFromCache(String fileId) {}

    @Override
    public CommonResult<String> importDef(ObjectNode objectNode, String typeId) {
        try {
            String formDataTemplatesXml = objectNode.get("formDataTemplatesXml").asText();

            // 数据报表xml 导入处理
            FormDataTemplateXmlList formDataTemplateXmlList = (FormDataTemplateXmlList) JAXBUtil.unmarshall(formDataTemplatesXml, FormDataTemplateXmlList.class);
            List<FormDataTemplateXml> list = formDataTemplateXmlList.getFormDataTemplateXmlList();
            for (FormDataTemplateXml formDataTemplateXml : list) {
                importDef(formDataTemplateXml, typeId);
            }
            return new CommonResult<String>("导入成功");

        } catch (Exception e) {
            throw new RuntimeException("XML转换为POJO类型错误" + e.getMessage(), e);
        }
    }


    /**
     * 导入某个数据报表
     *
     * @param formDataTemplateXml
     *            void
     */
    private void importDef(FormDataTemplateXml formDataTemplateXml, String typeId) {
        importDefinition(formDataTemplateXml, typeId);
    }


    /**
     * 导入数据报表
     *
     * @param formDataTemplateXml
     * @return FormDataTemplate
     */
    private FormDataTemplate importDefinition(FormDataTemplateXml formDataTemplateXml, String typeId) {
        FormDataTemplate formDataTemplate = formDataTemplateXml.getFormDataTemplate();
        PortalFeignService portalFeignService = AppUtil.getBean(PortalFeignService.class);
        ObjectNode sysType = portalFeignService.getSysTypeById(typeId);
        String typeName = "";
        if (BeanUtils.isNotEmpty(sysType)) {
            typeName = sysType.get("name").asText();
        }
        // 数据报表分类
        if (StringUtil.isNotEmpty(typeId)) {
            formDataTemplate.setTypeId(typeId);
            formDataTemplate.setTypeName(typeName);
        }
        IUser user = ContextUtil.getCurrentUser();
        if (BeanUtils.isNotEmpty(user)) {
            formDataTemplate.setCreateBy(user.getUserId());
        }
        formDataTemplate.setCreateTime(LocalDateTime.now());
        FormDataTemplate entity = baseMapper.getByAlias(formDataTemplate.getAlias());
        //判断导入的数据报表数据库是否存在
        if(BeanUtils.isNotEmpty(entity)){
            if (BeanUtils.isNotEmpty(user)) {
                formDataTemplate.setUpdateBy(user.getUserId());
                formDataTemplate.setUpdateTime(LocalDateTime.now());
            }
            baseMapper.updateById(formDataTemplate);
        }else{
            baseMapper.insert(formDataTemplate);
        }
        return formDataTemplate;
    }
    /**
     * 根据流程定义ID获取FormDataTemplateXml
     *
     * @param id
     * @return FormDataTemplateXml
     */
    private FormDataTemplateXml getByTemplateId(String id) {
        FormDataTemplate formDataTemplate = baseMapper.selectById(id);

        FormDataTemplateXml formDataTemplateXml = new FormDataTemplateXml();
        // 数据报表
        formDataTemplateXml.setFormDataTemplate(formDataTemplate);

        return formDataTemplateXml;
    }

	@Override

	@Transactional
	public void checkAndImportData(List<Map<String, Object>> rows,BoEnt boEnt,Map<String, BoAttribute> columnMap,String bindFilld ,String fillValue,FormDataTemplate template) throws Exception {
		
		FormMeta formDef = formMetaManager.getByKey(template.getFormKey());
		JsonNode expandJsonNode = JsonUtil.toJsonNode(formDef.getExpand());
		Map<String, Map<String, JsonNode>> filedValidateMap = new HashMap<>();
		ArrayNode filedsArray = JsonUtil.getMapper().createArrayNode();
		if (BeanUtils.isNotEmpty(expandJsonNode.get("list"))) {
			//解析表单配置的校验规则，后台支持的校验规则，在后台校验一遍
			for (JsonNode obj : expandJsonNode.get("list")) {
				if ("grid".equals(obj.get("ctrlType").asText()) && obj.hasNonNull("columns")) {
					ArrayNode columns = (ArrayNode) obj.get("columns");
					for (JsonNode col : columns) {
						if (!col.hasNonNull("list") || !col.get("list").isArray()) {
							continue;
						}
						filedsArray.addAll((ArrayNode) col.get("list"));
					}
				}else if (BeanUtils.isNotEmpty(obj.get("options")) &&  BeanUtils.isNotEmpty(obj.get("options").get("validateList"))) {
					filedsArray.add(obj);
				}
				
			}
			for (JsonNode filed : filedsArray) {
				if (BeanUtils.isEmpty(filed.get("options")) || BeanUtils.isEmpty(filed.get("options").get("validateList"))) {
					continue;
				}
				Map<String, JsonNode> validateMap = new HashMap<>();
				for (JsonNode validat : filed.get("options").get("validateList")) {
					validateMap.put(validat.get("key").asText(), validat);
				}
				filedValidateMap.put(filed.get("name").asText(), validateMap );
			}
		}
		
		JdbcTemplate jdbcTemplate = DataSourceUtil.getJdbcTempByDsAlias(boEnt.getDsName()==null?DataSourceConsts.LOCAL_DATASOURCE:boEnt.getDsName());
		List<FormDataImportLog> validateList = new ArrayList<>();
		BoDataImportHandler boDataImportHandler = AppUtil.getBean(BoDataImportHandler.class);
		Map<String, Object> threadVarMap = new HashMap<>();
		//临时存放唯一校验的map
		Map<String, Set<Object>> uniqueValeMap =new HashMap<>();
		for (int i = 0; i < rows.size(); i++){
			StringBuffer sql = new StringBuffer("insert into ");
			Map<String, Object> row = rows.get(i);
			if (boDataImportHandler != null) {
				List<ValidateResult> validateData = validateData(row,filedValidateMap,columnMap,uniqueValeMap);
				if (BeanUtils.isNotEmpty(validateData)) {
					for (ValidateResult validateResult : validateData) {
						FormDataImportLog log =new FormDataImportLog(validateResult);
						log.setId(UniqueIdUtil.getSuid());
						log.setRowNumber(i+2);
						log.setPId(fillValue);
						validateList.add(log);
					}
					continue;
				}
				//校验数据
				List<ValidateResult> validateRes= boDataImportHandler.validateData(rows.get(i), boEnt,threadVarMap);
				if (BeanUtils.isNotEmpty(validateRes)) {
					for (ValidateResult validateResult : validateRes) {
						FormDataImportLog log =new FormDataImportLog(validateResult);
						log.setId(UniqueIdUtil.getSuid());
						log.setRowNumber(i+2);
						log.setPId(fillValue);
						validateList.add(log);
					}
					continue;
				}
				//调用接口转换数据
			    row = boDataImportHandler.transData(rows.get(i), boEnt,threadVarMap);
			}
			
			List<String> fields = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
			StringBuffer params = new StringBuffer();
			
			for(Entry<String, Object> map : row.entrySet()){
				if("主键".equals(map.getKey())){
					fields.add(boEnt.getPkKey());
					if(BeanUtils.isNotEmpty(row.get("主键"))){
						values.add("'"+row.get("主键")+"'");
					}else{
						values.add(UniqueIdUtil.getSuid());
					}
					params.append("?,");
				}else{
					//excel单元格有值才去拼接sql
					if(BeanUtils.isNotEmpty(map.getValue())){
						BoAttribute field = columnMap.get(map.getKey());
						if (BeanUtils.isEmpty(field) || field.getName().equals(bindFilld)) {
							continue;
						}
						fields.add(field.getFieldName());
						values.add(map.getValue());
						params.append("?,");
					}
				}
			}
			//判断如果excel中无[主键]列，则补充完整
			if(!fields.contains(boEnt.getPkKey())){
				fields.add(boEnt.getPkKey());
				values.add(UniqueIdUtil.getSuid());
				params.append("?,");
			}
			
			if (StringUtil.isNotEmpty(bindFilld) && StringUtil.isNotEmpty(fillValue)) {
				fields.add(BoEnt.FIELD_PREFIX+bindFilld);
				values.add(fillValue);
				params.append("?,");
			}
			
			//加入额外的两个字段
			fields.add(BoEnt.FK_NAME);
			values.add("0");
			params.append("?,");
			fields.add("F_form_data_rev_");
			values.add("0");
			params.append("?");
			
			sql.append(boEnt.getTableName());
			sql.append("("+ String.join(",", fields) +")");
			sql.append(" values ");
			sql.append("("+ params +")");
			
			try {
				jdbcTemplate.update(sql.toString(),values.toArray());
			} catch (Exception e) {
				FormDataImportLog log =new FormDataImportLog();
				log.setId(UniqueIdUtil.getSuid());
				log.setRowNumber(i+2);
				log.setPId(fillValue);
				log.setErrorMsg(e.getMessage());
				validateList.add(log);
			}
		}
		if (validateList.size()>0) {
			ExecutorService executorService = Executors.newCachedThreadPool();
			executorService.execute(() -> {
				try {
					formDataImportLogManager.deleteByPid(fillValue);
					formDataImportLogManager.saveBatch(validateList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			throw new RuntimeException("导入失败，详情请查看导入日志");
		}else{
			formDataImportLogManager.deleteByPid(fillValue);
		}
	}
	
	
	private List<ValidateResult> validateData(Map<String, Object> data,Map<String, Map<String, JsonNode>> filedValidateMap,Map<String, BoAttribute> columnMap,Map<String, Set<Object>> uniqueValeMap){
		List<ValidateResult> list = new ArrayList<>();
		Map<String, BoAttribute> filedNameMap =new HashMap<>();
		for (BoAttribute attr : columnMap.values()) {
			filedNameMap.put(attr.getName(), attr);
		}
		for (Iterator<Entry<String, Map<String, JsonNode>>> iterator = filedValidateMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Map<String, JsonNode>> next =  iterator.next();
			if (!filedNameMap.containsKey(next.getKey())) {
				continue;
			}
			BoAttribute attribute = filedNameMap.get(next.getKey());
			Object value = data.get(attribute.getDesc());
			for (Iterator<Entry<String, JsonNode>> iterator2 = next.getValue().entrySet().iterator(); iterator2.hasNext();) {
				Entry<String, JsonNode> entity = iterator2.next();
				ValidateResult validateRes = validate(attribute,value,entity.getKey(),entity.getValue(),filedNameMap,data,uniqueValeMap);
				if (BeanUtils.isNotEmpty(validateRes)) {
					list.add(validateRes);
				}
			}
		}
		return list;
	}
	

	private ValidateResult validate(BoAttribute attribute,Object value,String validateName,JsonNode validate,Map<String, BoAttribute> filedNameMap,
			Map<String, Object> data,Map<String, Set<Object>> uniqueValeMap){
		String errorMsg = "";
		switch (validateName) {
		case "required":
			if (BeanUtils.isEmpty(value)) {
				errorMsg = "不能为空";
			}
			break;
		case "max":
			int maxLen = JsonUtil.getInt((ObjectNode)validate, "value");
			if (BeanUtils.ObjectToString(value).length()> maxLen) {
				errorMsg ="长度超出"+maxLen;
			}
			break;
		case "min":
			int minLen = JsonUtil.getInt((ObjectNode)validate, "value");
			if (BeanUtils.ObjectToString(value).length()<minLen) {
				errorMsg = "长度不够"+minLen;
			}
			break;
		case "is":
			String valueScope = JsonUtil.getString((ObjectNode)validate, "value");
			if (StringUtil.isNotEmpty(valueScope)) {
				Set<String> values = new HashSet<>(Arrays.asList(valueScope.split(",")));
				if (!values.contains(value)) {
					errorMsg = "输入值不在【"+StringUtil.join(values)+"】范围之内";
				}
			}
			break;
		case "regex":
			String regex = JsonUtil.getString((ObjectNode)validate, "value");
			if (StringUtil.isNotEmpty(regex) && !Pattern.matches(regex, BeanUtils.ObjectToString(value))) {
				errorMsg = "正则不匹配";
			}
			break;
		case "row_unique":
			List<String> uniqueKeyList = new ArrayList<>(Arrays.asList(JsonUtil.getString((ObjectNode)validate, "value").split(",")));
			if (!uniqueKeyList.contains(attribute.getName())) {
				uniqueKeyList.add(attribute.getName());
			}
			List<String> uniqueValList = new ArrayList<>();
			List<String> uniqueDescList = new ArrayList<>();
			for (String key : uniqueKeyList) {
				if (key.split("\\.").length ==3) {
					key = key.split("\\.")[2];
				}
				if (filedNameMap.containsKey(key)) {
					uniqueValList.add(BeanUtils.ObjectToString(data.get(filedNameMap.get(key).getDesc())));
					uniqueDescList.add(filedNameMap.get(key).getDesc());
				}
			}
			String uniqueKey = StringUtil.join(uniqueKeyList,"");
			Set<Object> valueSet = BeanUtils.isNotEmpty(uniqueValeMap.get(uniqueKey))?uniqueValeMap.get(uniqueKey):new HashSet<>();
			if ( valueSet.contains(StringUtil.join(uniqueValList,""))) {
				errorMsg = "违反【"+StringUtil.join(uniqueDescList,"+")+"】唯一约束";
			}else{
				valueSet.add(StringUtil.join(uniqueValList,""));
			}
			uniqueValeMap.put(uniqueKey, valueSet);
			break;
		default:
			break;
		}
		if (StringUtil.isEmpty(errorMsg)) {
			return null;
		}
		return new ValidateResult(attribute.getDesc(),errorMsg);
	}

	@Override

	public List<Map<String, Object>> resolutionExcel(MultipartFile firstFile, Map<String, BoAttribute> columnMap ,String bindFilld) {
		//用来存放表中数据
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Workbook wb = ExcelUtil.readExcel(firstFile);
		if(wb == null){
			return list;
		}
		//获取第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		//获取最大行数
		int rownum = sheet.getPhysicalNumberOfRows();
		//获取第一行
		Row row = sheet.getRow(0);
		//获取最大列数
		int colnum = row.getPhysicalNumberOfCells();
		List<String> columns=new ArrayList<String>();
		for (int i = 0; i<rownum; i++) {
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			row = sheet.getRow(i);
			if(i==0){
				for (int j=0;j<colnum;j++){
					columns.add((String) ExcelUtil.getCellFormatValue(row.getCell(j)));
				}
			}else{
				//中间有空行的，也放一个空map吧。以便后面确认错误数据的行数
				if(row ==null){
					list.add(map);
					continue;
				}
				for (int j=0;j<colnum;j++){
					BoAttribute boAttribute = columnMap.get(columns.get(j));
					//关联主表的字段值，不取excel的，直接根据传入的参数填充
					if (BeanUtils.isEmpty(boAttribute) || bindFilld.equals(boAttribute.getName())) {
						continue;
					}
					map.put(boAttribute.getDesc(), getCellFormatValue(boAttribute,row.getCell(j)));
				}
				
				list.add(map);
			}
		}
		return list;

	}
	

	@SuppressWarnings("deprecation")
	public  Object getCellFormatValue(BoAttribute boAttribute ,Cell cell){
		
		if(cell==null){
			return "";
		}
		Object cellValue = null;
		//判断cell类型
		switch(boAttribute.getDataType()){
		case "number":{
			cellValue = cell.getNumericCellValue();
			break;
		}
		case "date":{
			if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
				cellValue = cell.getRichStringCellValue().getString();
			}else{
				cellValue = cell.getDateCellValue();
			}
			
			break;
		}
		case "varchar":{
			if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
				cellValue = BeanUtils.ObjectToString(cell.getNumericCellValue());
			}else{
				cellValue = cell.getRichStringCellValue().getString();
			}
			break;
		}
		default:
			cellValue = "";
		}
		return cellValue;
	}
}
