package com.hotent.form.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.form.model.FormRight;
import com.hotent.form.model.FormRightXml;
import com.hotent.form.persistence.manager.FormRightManager;


@Service
public class DefaultBpmFormRightsService implements BpmFormRightsService{
	@Resource
	FormRightManager bpmFormRightManager;
	
	public String getPermission(String formKey, String userId, String flowKey,String parentFlowKey, String nodeId, boolean isGlobalPermission) {
		try {
			JsonNode permission = bpmFormRightManager.getPermission(formKey, flowKey, parentFlowKey, nodeId, 1,isGlobalPermission);
			return permission.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public String getInstPermission(String formKey, String userId,String flowKey) {
		try {
			JsonNode permission  = bpmFormRightManager.getPermission(formKey, flowKey, "", "", 2,true);
			return permission.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public String getStartPermission(String formKey, String flowKey, String nodeId, String nextNodeId) {
		try {
			JsonNode permission  = bpmFormRightManager.getStartPermission(formKey,flowKey,nodeId,nextNodeId);
			return BeanUtils.isEmpty(permission)?"":permission.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getFormRightExportXml(ObjectNode formRight) throws JAXBException, JsonParseException, JsonMappingException, IOException {
		FormRightXml formRightList = new FormRightXml();
		ArrayNode arr= (ArrayNode) formRight.get("bpmFormRight");
		List<FormRight> rightList=JsonUtil.toBean(JsonUtil.toJson(arr),new TypeReference<List<FormRight>>(){});
		formRightList.setRightList(rightList);
		String xml=JAXBUtil.marshall(formRightList, FormRightXml.class);
		return xml;
	}
	@Override
	public void importFormRights(String formRightsXml) {
		bpmFormRightManager.importFormRights(formRightsXml);
	}


	@Override
	public void removeFormRights(String flowKey, String parentFlowKey) {
		bpmFormRightManager.removeInst(flowKey);
		bpmFormRightManager.remove(flowKey, parentFlowKey);
	}


	@Override
	public List<FormRight> getFormRigthListByFlowKey(String flowKey) {
		return bpmFormRightManager.getByFlowKey(flowKey);
	}


	@Override
	public void removeFormRightByFlowKey(String flowKey,
			String parentFlowKey, int permissionType) {
		bpmFormRightManager.removeByFlowKey(flowKey, parentFlowKey, permissionType);
	}


	@Override
	public void createFormRight(FormRight bpmFormRight) {
		bpmFormRightManager.create(bpmFormRight);
	}


	@Override
	public List<FormRight> queryFormRight(QueryFilter queryFilter) {
		return bpmFormRightManager.query(queryFilter).getRows();
	}
	
	
}
