package com.hotent.base.feign.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.EmptyFeignException;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;

@Component
public class FormFeignServiceImpl implements FormFeignService {
	@Override
	public ObjectNode getMainBOEntByDefAliasOrId(String alias, String defId) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> handlerBoData(ObjectNode param) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getBodataByDefCode(String saveMode, String code) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getBodataById(ObjectNode param) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getByFormKey(String formKey) {
		throw new EmptyFeignException();
	}

	@Override
	public String getFormExportXml(String formKeys) throws IOException {
		throw new EmptyFeignException();
	}

	@Override
	public String getBoDefExportXml(ObjectNode bodef) {
		throw new EmptyFeignException();
	}

	@Override
	public String getFormRightExportXml(ObjectNode bodef) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode importBo(String bodefXml) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode importBoDef(List<ObjectNode> bos) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode importForm(String formfXml) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode importFormRights(String formRightsXml) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getByFormId(String formId) {
		throw new EmptyFeignException();
	}

	@Override
	public String getInstPermission(ObjectNode param) {
		throw new EmptyFeignException();
	}

	@Override
	public String getStartPermission(ObjectNode param) {
		throw new EmptyFeignException();
	}

	@Override
	public String getPermission(ObjectNode param) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getFormRigthListByFlowKey(String formId) {
		throw new EmptyFeignException();
	}

	@Override
	public void removeFormRights(String flowKey, String parentFlowKey) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getBodefByAlias(String alias) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getBoJosn(String id) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getBoEntByName(String name) {
		throw new EmptyFeignException();
	}

	@Override
	public void removeFormRightByFlowKey(ObjectNode param) {
		throw new EmptyFeignException();
	}

	@Override
	public void createFormRight(ObjectNode bpmFormRight) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> queryFormRight(QueryFilter queryFilter) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getFormBoLists(String formKey) {
		
		throw new EmptyFeignException();
	}

	@Override
	public Map<String, String> getFormAndBoExportXml(ObjectNode obj) {
		throw new EmptyFeignException();
	}

	@Override
	public void removeDataByBusLink(JsonNode links) {

		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<String> importFormAndBo(ObjectNode obj) {
		throw new EmptyFeignException();
	}

	@Override
	public boolean getSupportDb(String alias) {
		throw new EmptyFeignException();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public PageList getQueryPage(String alias) {
		throw new EmptyFeignException();
	}

	@Override
	public List<?> getCustomDialogs() {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getByClassName(String className) {
		throw new EmptyFeignException();
	}

	@Override
	public PageList doQuery(String alias, Integer page,
			String queryData) {
		throw new EmptyFeignException();
	}

	@Override
	public Map<String, Object> getFormData(String pcAlias, String mobileAlias) {
		throw new EmptyFeignException();
	}
}
