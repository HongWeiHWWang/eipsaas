package com.hotent.form.persistence.manager.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.form.enums.FormType;
import com.hotent.form.model.FormMeta;
import com.hotent.form.persistence.manager.FormMetaManager;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.FormTemplate;
import com.hotent.form.persistence.dao.FormTemplateDao;
import com.hotent.form.persistence.manager.FormTemplateManager;

import javax.annotation.Resource;


@Service("bpmFormTemplateManager")
public class FormTemplateManagerImpl extends BaseManagerImpl<FormTemplateDao, FormTemplate> implements FormTemplateManager{

	@Resource
	FormMetaManager formMetaManager;
	/**
	 * 返回模版物理的路径。
	 * @return
	 * @throws Exception 
	 */
	public static  String getFormTemplatePath() throws Exception{
		return FileUtil.getClassesPath() + File.separator + "template" + File.separator +"form" + File.separator;
	}
	
	/**
	 * 根据模版别名取得模版。
	 * @param alias
	 * @return
	 */
	@Override
	public FormTemplate getByTemplateAlias(String alias){
		return baseMapper.getByTemplateAlias(alias);
	}
	
	/**
	 * 获取所有的系统原始模板
	 * @return
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public void initAllTemplate() throws Exception{
		baseMapper.delSystem();
		addTemplate();
	}
	
	/**
	 * 当模版数据为空时，将form目录下的模版添加到数据库中。
	 */
	@Override
	@Transactional
	public void init() throws Exception{
		Integer amount=baseMapper.getHasData();
		if(amount==0){
			addTemplate();
		}
	}
	
	/**
	 * 初始化模版，在系统启用的时候进行调用。
	 */
	@Transactional
	public static void initTemplate(){
		FormTemplateManager service= (FormTemplateManager) AppUtil.getBean(FormTemplateManager.class);
		try {
			service.init();
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
	}
	/**
	 * 初始化添加form下的模版数据到数据库。
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	private void addTemplate() throws Exception{
		// 因为没有使用File的相关类来读取jar包中的文件，所以无需处理不同操作系统的 斜杠
		String templateFilePath = "template/form/templates.xml";
		String xml= getTemplateFile(templateFilePath);
		Document document=Dom4jUtil.loadXml(xml);
		Element root=document.getRootElement();
		List<Element> list=root.elements();
		for(Element element:list){
			String alias=element.attributeValue("alias");
			String name=element.attributeValue("name");
			String type=element.attributeValue("type");
			String templateDesc=element.attributeValue("templateDesc");
			String macroAlias=element.attributeValue("macroAlias");
			String dir = element.attributeValue("dir");
			// 因为没有使用File的相关类来读取jar包中的文件，所以无需处理不同操作系统的 斜杠
			String filePath = String.format("template/form/%s/%s.ftl", dir, alias);
			String html = getTemplateFile(filePath);
			FormTemplate bpmFormTemplate=new FormTemplate();
			bpmFormTemplate.setTemplateId(UniqueIdUtil.getSuid());
			bpmFormTemplate.setMacrotemplateAlias(macroAlias);
			bpmFormTemplate.setHtml(html);
			bpmFormTemplate.setTemplateName(name);
			bpmFormTemplate.setAlias(alias);
			bpmFormTemplate.setCanedit(0);
			bpmFormTemplate.setTemplateType(type);
			bpmFormTemplate.setTemplateDesc(templateDesc);
			bpmFormTemplate.setSource("system");
			this.create(bpmFormTemplate);
		}
	}
	
	private String getTemplateFile(String path) throws IOException {
		InputStream stream = new ClassPathResource(path).getInputStream();
		byte[] readByte = FileUtil.readByte(stream);
		String word = new String(readByte, "UTF-8");
		Assert.isTrue(StringUtil.isNotEmpty(word), String.format("读取路径为：%s 的文件时，获取到的内容为空.", path));
		return word;
	}
	
	/**
	 * 检查模板别名是否唯一
	 * @param alias
	 * @return
	 */
	@Override
	public boolean isExistAlias(String alias){
		FormTemplate b = baseMapper.getByTemplateAlias(alias);
		if(b!=null){
			return true;
		}
		return false;
	}
	
	/**
	 * 将用户自定义模板备份
	 * @param id
	 */
	@Override
	@Transactional
	public void backUpTemplate(String id){
		FormTemplate bpmFormTemplate=this.get(id);
		String alias=bpmFormTemplate.getAlias();
		String name=bpmFormTemplate.getTemplateName();
		String desc=bpmFormTemplate.getTemplateDesc();
		String html=bpmFormTemplate.getHtml();
		String type=bpmFormTemplate.getTemplateType();
		String macroAlias=bpmFormTemplate.getMacrotemplateAlias();
		
		String templatePath = null;
		try {
			templatePath = getFormTemplatePath();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String xmlPath=templatePath +"templates.xml";
		String xml= FileUtil.readFile(xmlPath);
		
		Document document=Dom4jUtil.loadXml(xml);
		Element root=document.getRootElement();
		
		Element e=root.addElement("template");
		e.addAttribute("alias", alias);
		e.addAttribute("name", name);
		e.addAttribute("type", type);
		e.addAttribute("templateDesc", desc);
		e.addAttribute("macroAlias",macroAlias);
		String content=document.asXML();
		
		FileUtil.writeFile(xmlPath, content);
		FileUtil.writeFile(templatePath +alias+".ftl", html);
		
		bpmFormTemplate.setCanedit(0);
		this.update(bpmFormTemplate);
	}
	
	/**
	 * 根据模版类型取得模版列表。
	 * @param type
	 * @return
	 */
	@Override
	public List<FormTemplate> getTemplateType(String type){
		if("macro".equals(type)){
			List<FormTemplate> templates = new ArrayList<FormTemplate>();
			templates.addAll(baseMapper.getTemplateType(type));
			templates.addAll(baseMapper.getTemplateType("mobileMacro"));
			return templates;
		}else{
			return baseMapper.getTemplateType(type);
		}
	}

	@Override
	public Map<String, Object> getTemplateTypeMap(String... types) {
		Map<String,Object> map = new HashMap<>();
		for (String type:types){
			map.put(type,getTemplateType(type));
		}
		return map;
	}

	/**
	 * 获取主表模版
	 * @return
	 */
	@Override
	public List<FormTemplate> getAllMainTableTemplate(boolean isPC) {
		return getTemplateType(isPC? FormTemplate.MAIN_TABLE : FormTemplate.MOBILE_MAIN);
	}
	
	/**
	 * 获取子表模版。
	 * @return
	 */
	@Override
	public List<FormTemplate> getAllSubTableTemplate(boolean isPC) {
		return getTemplateType(isPC? FormTemplate.SUB_TABLE : FormTemplate.Mobile_SUB);
	}

	@Override
	public Map<String, Object> selectTemplate(String defId, int isSimple, String templatesId, String formType) throws Exception {
		FormMeta bpmFormDef = formMetaManager.get(defId);
		if(StringUtil.isEmpty(formType)){
			formType= FormType.PC.value();
		}else{
			formType=formType.replace("'", "\"").trim();
		}

		JsonNode fieldList =bpmFormDef.getFieldList();

		List<FormTemplate> mainTableTemplates = getAllMainTableTemplate(formType.equals(FormType.PC.value()));
		List<FormTemplate> subTableTemplates = getAllSubTableTemplate(formType.equals(FormType.PC.value()));

		Map map=new HashMap();
		map.put("mainTableTemplates", mainTableTemplates);
		map.put("subTableTemplates", subTableTemplates);
		map.put("bpmForm", bpmFormDef);
		map.put("isSimple", isSimple);
		map.put("formType", formType);
		map.put("tableList", fieldList);
		return map;
	}

	/**
	 * 获取宏模版。
	 * @return
	 */
	@Override
	public List<FormTemplate> getAllMacroTemplate() {
		return getTemplateType(FormTemplate.MACRO);
	}
	
	/**
	 * 获取表管理模版。
	 * @return
	 */
	@Override
	public List<FormTemplate> getAllTableManageTemplate() {
		return getTemplateType(FormTemplate.TABLE_MANAGE);
	}
	
	/**
	 * 获取列表模版。
	 * @return
	 */
	@Override
	public List< FormTemplate> getListTemplate() {
		return getTemplateType(FormTemplate.LIST);
	}
	
	/**
	 * 获取明细模版。
	 * @return
	 */
	public List< FormTemplate> getDetailTemplate() {
		return getTemplateType(FormTemplate.DETAIL);
	}
	
	/**
	 * 获取数据模版。
	 * @return
	 */
	@Override
	public List< FormTemplate> getDataTemplate() {
		return getTemplateType(FormTemplate.DATA_TEMPLATE);
	}
	/**
	 * 获取查询数据模版。
	 * @return
	 */
	@Override
	public List< FormTemplate> getQueryDataTemplate() {
		return getTemplateType(FormTemplate.QUERY_DATA_TEMPLATE);
	}

	@Override
	public FormTemplate getTemplateByRev(Map<String, Object> map) {
		return baseMapper.getTemplateByRev(map);
	}

	@Override
	@Transactional
	public void setDefault(String templateId, String templateType) {
		baseMapper.setDefault(templateId);
	}
}
