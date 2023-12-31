package com.hotent.bpmModel.manager.impl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.model.def.BpmDefXml;
import com.hotent.bpm.model.def.BpmDefXmlList;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.form.FormCategory;
import com.hotent.bpm.natapi.def.NatProDefinitionService;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProBoManager;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpmModel.manager.BpmDefTransform;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 流程定义导入导出的实现。
 * 
 * <pre>
 *  
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-20-上午11:55:32
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class DefaultBpmDefTransform implements BpmDefTransform {
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	NatProDefinitionService natProDefinitionService;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmProBoManager bpmProBoManager;
	@Resource
	FormFeignService formRestfulService;

	@Override
	public Map<String, String> exportDef(List<String> defList) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		BpmDefXmlList list = new BpmDefXmlList();
		Set<String> formKeys = new HashSet<String>();
		Set<String> bocodes = new HashSet<String>();
		Set<String> defKeys = new HashSet<String>();
		for (String defId : defList) {
			// 处理表单
			handelFormBo(defId, formKeys, bocodes, defKeys);

			BpmDefXml defXml = getByDefId(defId);
			list.addBpmDefXml(defXml);
		}
		try {
			String xml = JAXBUtil.marshall(list, BpmDefXmlList.class);
			ObjectNode obj = JsonUtil.getMapper().createObjectNode();
			obj.put("formKeys", StringUtil.join(new ArrayList<>(formKeys), ","));
			obj.put("boCodes", StringUtil.join(new ArrayList<>(bocodes), ","));
			obj.put("defKeys", StringUtil.join(new ArrayList<>(defKeys), ","));
			Map<String, String> formRightXml = formRestfulService.getFormAndBoExportXml(obj);

			map.put("bpmdefs.flow.xml", xml);
			if (BeanUtils.isNotEmpty(formRightXml)) {
				map.putAll(formRightXml);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("导出失败！" + e.getMessage(), e);
		}
		return map;
	}

	// 处理表单和BO
	private void handelFormBo(String defId, Set<String> form, Set<String> bocode, Set<String> defKeys)
			throws Exception {
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		defKeys.add(bpmProcessDefExt.getDefKey());
		// 取出formKey 为导出做准备
		Form globalForm = defExt.getGlobalForm();
		if (globalForm != null && FormCategory.INNER == globalForm.getType()
				&& StringUtil.isNotEmpty(globalForm.getFormValue()))
			form.add(globalForm.getFormValue());

		FormExt globalMobileForm = defExt.getGlobalMobileForm();
		if (globalMobileForm != null && FormCategory.INNER == globalMobileForm.getType()
				&& StringUtil.isNotEmpty(globalMobileForm.getFormValue()))
			form.add(globalMobileForm.getFormValue());

		List<BpmNodeDef> nodeList = bpmDefinitionAccessor.getSignUserNode(defId);
		for (BpmNodeDef bpmNodeDef : nodeList) {
			Form nodeForm = bpmNodeDef.getForm();
			if (nodeForm != null && FormCategory.INNER == nodeForm.getType()
					&& StringUtil.isNotEmpty(nodeForm.getFormValue()))
				form.add(nodeForm.getFormValue());

			Form mobileNodeForm = bpmNodeDef.getMobileForm();
			if (mobileNodeForm != null && FormCategory.INNER == mobileNodeForm.getType()
					&& StringUtil.isNotEmpty(mobileNodeForm.getFormValue()))
				form.add(mobileNodeForm.getFormValue());

		}
		// 取出所有Bo
		List<ProcBoDef> boDefList = defExt.getBoDefList();
		for (ProcBoDef procBoDef : boDefList) {
			bocode.add(procBoDef.getKey());
		}
	}

	@Override
	public CommonResult<String> importDef(ObjectNode objectNode, String typeId) {
		try {

			String flowXml = objectNode.get("bpmdefsXml").asText();
			String formXmlStr = objectNode.get("formXmlStr").asText();
			String boXmlStr = objectNode.get("boXmlStr").asText();
			String formRightsXml = objectNode.get("formRightsXml").asText();

			// 流程xml 导入处理
			BpmDefXmlList defList = (BpmDefXmlList) JAXBUtil.unmarshall(flowXml, BpmDefXmlList.class);
			List<BpmDefXml> list = defList.getBpmList();
			for (BpmDefXml defXml : list) {
				importDef(defXml, typeId);
			}

			ObjectNode obj = JsonUtil.getMapper().createObjectNode();
			obj.put("formXmlStr", formXmlStr);
			obj.put("boXmlStr", boXmlStr);
			obj.put("formRightsXml", formRightsXml);
			return formRestfulService.importFormAndBo(obj);

		} catch (Exception e) {
			throw new RuntimeException("XML转换为POJO类型错误" + e.getMessage(), e);
		}
	}

	/**
	 * 根据流程定义ID获取BpmDefXml。
	 * 
	 * @param defId
	 * @return BpmDefXml
	 */
	private BpmDefXml getByDefId(String defId) {
		DefaultBpmDefinition definition = bpmDefinitionManager.getById(defId);

		BpmDefXml defXml = new BpmDefXml();
		// 流程定义
		defXml.setBpmDefinition(definition);

		return defXml;
	}

	/**
	 * 导入某个流程。
	 * 
	 * @param defXml
	 *            void
	 */
	private void importDef(BpmDefXml defXml, String typeId) {
		importDefinition(defXml, typeId);
	}

	/**
	 * 导入流程定义。
	 * 
	 * @param defXml
	 * @return DefaultBpmDefinition
	 */
	private DefaultBpmDefinition importDefinition(BpmDefXml defXml, String typeId) {
		String defId = UniqueIdUtil.getSuid();
		DefaultBpmDefinition def = defXml.getBpmDefinition();
		def.setDefId(defId);
		PortalFeignService portalFeignService = AppUtil.getBean(PortalFeignService.class);
		ObjectNode sysType = portalFeignService.getSysTypeById(typeId);
		String typeName = "";
		if (BeanUtils.isNotEmpty(sysType)) {
			typeName = sysType.get("name").asText();
		}
		// 流程定义分类
		if (StringUtil.isNotEmpty(typeId)) {
			def.setTypeId(typeId);
			def.setTypeName(typeName);
		}

		// 发布流程
		String bpmnXml = def.getBpmnXml();

		String deployId = "";
		try {
			deployId = natProDefinitionService.deploy("", def.getName(), bpmnXml);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String bpmnDefId = natProDefinitionService.getProcessDefinitionIdByDeployId(deployId);

		def.setIsMain("Y");
		def.setBpmnDefId(bpmnDefId);
		def.setBpmnDeployId(deployId);
		Integer version = bpmDefinitionManager.getMaxVersion(def.getDefKey());
		def.setVersion(version);

		IUser user = ContextUtil.getCurrentUser();
		if (user != null) {
			def.setCreateBy(user.getUserId());
		}

		def.setCreateTime(LocalDateTime.now());

		bpmDefinitionManager.create(def);

		bpmDefinitionManager.updMainVersion(defId);

		return def;
	}

}
