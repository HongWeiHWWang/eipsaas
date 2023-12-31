package com.hotent.assembly.feign.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import com.hotent.form.controller.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoResult;
import com.hotent.form.model.FieldAuth;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormRight;
import com.hotent.form.vo.FormRestfulModel;

@Service
@Primary
public class FormFeignServiceAssemblyImpl implements FormFeignService{
	@Resource
	FormServiceController formServiceController;
	@Resource
	CustomQueryController customQueryController;
	@Resource
	CustomDialogController customDialogController;
	@Resource
	FieldAuthController fieldAuthController;
	@Resource
	FormController formController;
	@Resource
	BoDefController boDefController;

	@Override
	public ObjectNode getMainBOEntByDefAliasOrId(String alias, String defId) {
		try {
			return formServiceController.getMainBOEntByDefAliasOrId(alias, defId);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public boolean getSupportDb(String alias) {
		try {
			return boDefController.getSupportDb(alias);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> handlerBoData(ObjectNode param) {
		try {
			FormRestfulModel frm = JsonUtil.toBean(param, FormRestfulModel.class);
			
			List<BoResult> handlerBoData = formServiceController.handlerBoData(frm);
			return JsonUtil.listToListNode(handlerBoData);
		} catch (BaseException e){
			throw new BaseException(e.getMessage());
		} catch(NumberFormatException e){
			throw new NumberFormatException(e.getMessage());
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getBodataByDefCode(String saveMode, String code) {
		try {
			BoData bodataByDefCode = formServiceController.getBodataByDefCode(saveMode, code);
			return (ObjectNode)JsonUtil.toJsonNode(bodataByDefCode);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getBodataById(ObjectNode param) {
		try {
			BoData bodataByDefCode = formServiceController.getBodataById(JsonUtil.toBean(param, FormRestfulModel.class));
			return (ObjectNode)JsonUtil.toJsonNode(bodataByDefCode);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getByFormKey(String formKey) {
		try {
			Form byFormKey = formServiceController.getByFormKey(formKey);
			return (ObjectNode)JsonUtil.toJsonNode(byFormKey);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getFormExportXml(String formKeys) throws IOException {
		try {
			return formServiceController.getFormExportXml(formKeys);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getBoDefExportXml(ObjectNode bodef) {
		try {
			return formServiceController.getBoDefExportXml(bodef);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getFormRightExportXml(ObjectNode bodef) {
		try {
			return formServiceController.getFormRightExportXml(bodef);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode importBo(String bodefXml) {
		try {
			CommonResult<String> importBo = formServiceController.importBo(bodefXml);
			return (ObjectNode)JsonUtil.toJsonNode(importBo);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode importBoDef(List<ObjectNode> bos) {
		try {
			List<BoDef> importBoDef = formServiceController.importBoDef(bos);
			return (ObjectNode)JsonUtil.toJsonNode(importBoDef);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode importForm(String formfXml) {
		try {
			CommonResult<String> importForm = formServiceController.importForm(formfXml);
			return (ObjectNode)JsonUtil.toJsonNode(importForm);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode importFormRights(String formRightsXml) {
		try {
			CommonResult<String> importFormRights = formServiceController.importFormRights(formRightsXml);
			return (ObjectNode)JsonUtil.toJsonNode(importFormRights);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getByFormId(String formId) {
		try {
			Form byFormId = formServiceController.getByFormId(formId);
			return (ObjectNode)JsonUtil.toJsonNode(byFormId);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getInstPermission(ObjectNode param) {
		try {
			return formServiceController.getInstPermission(JsonUtil.toBean(param, FormRestfulModel.class));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getStartPermission(ObjectNode param) {
		try {
			return formServiceController.getStartPermission(JsonUtil.toBean(param, FormRestfulModel.class));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getPermission(ObjectNode param) {
		try {
			return formServiceController.getPermission(JsonUtil.toBean(param, FormRestfulModel.class));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getFormRigthListByFlowKey(String formId) {
		try {
			List<FormRight> formRigthListByFlowKey = formServiceController.getFormRigthListByFlowKey(formId);
			return JsonUtil.listToListNode(formRigthListByFlowKey);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public void removeFormRights(String flowKey, String parentFlowKey) {
		try {
			formServiceController.removeFormRights(flowKey, parentFlowKey);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getBodefByAlias(String alias) {
		try {
			BoDef bodefByAlias = formServiceController.getBodefByAlias(alias);
			return (ObjectNode)JsonUtil.toJsonNode(bodefByAlias);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getBoJosn(String id) {
		try {
			return formServiceController.getBoJosn(id);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getBoEntByName(String name) {
		try {
			BoEnt boEntByName = formServiceController.getBoEntByName(name);
			return (ObjectNode)JsonUtil.toJsonNode(boEntByName);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public void removeFormRightByFlowKey(ObjectNode param) {
		try {
			formServiceController.removeFormRightByFlowKey(JsonUtil.toBean(param, FormRestfulModel.class));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public void createFormRight(ObjectNode bpmFormRight) {
		try {
			formServiceController.createFormRight(bpmFormRight);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> queryFormRight(QueryFilter queryFilter) {
		try {
			List<FormRight> queryFormRight = formServiceController.queryFormRight(queryFilter);
			return JsonUtil.listToListNode(queryFormRight);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getFormBoLists(String formKey) {
		try {
			List<BoDef> formBoLists = formServiceController.getFormBoLists(formKey);
			return JsonUtil.listToListNode(formBoLists);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Map<String, String> getFormAndBoExportXml(ObjectNode obj) {
		try {
			return formServiceController.getFormAndBoExportXml(obj);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public void removeDataByBusLink(JsonNode links) {
		try {
			formServiceController.removeDataByBusLink(links);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<String> importFormAndBo(ObjectNode obj) {
		try {
			return formServiceController.importFormAndBo(obj);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public PageList getQueryPage(String alias) {
		try {
			return customQueryController.getQueryPage(alias);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<?> getCustomDialogs() {
		try {
			return customDialogController.getAll();
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getByClassName(String className) {
		try {
			FieldAuth byClassName = fieldAuthController.getByClassName(className);
			return (ObjectNode)JsonUtil.toJsonNode(byClassName);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public PageList doQuery(String alias,Integer  page, String queryData) {
		try {
			Optional<String> aliasO = Optional.ofNullable(alias);
			Optional<Integer> pageO = Optional.ofNullable(page);
			Optional<String> queryDataO = Optional.ofNullable(queryData);
			return customQueryController.doQuery(aliasO, queryDataO, pageO);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Map<String, Object> getFormData(String pcAlias, String mobileAlias) {
		try {
			return formController.getFormData(pcAlias, mobileAlias);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
}
