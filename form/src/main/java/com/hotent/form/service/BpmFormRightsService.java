package com.hotent.form.service;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.query.QueryFilter;
import com.hotent.form.model.FormRight;



/**
 * 表单权限接口
 * @author heyifan
 * @version 创建时间: 2014-11-27
 */
public interface BpmFormRightsService {
	/**
	 * 获取表单权限
	 * <pre>
	 * {
	 * 	field：{"NAME": "w", "SEX": "r"}
	 * 	table：{"TABLE1": "r", "TABLE2": "w"}
	 * 	opinion：{"领导意见": "w", "部门意见": "r"}
	 * }
	 * </pre>
	 * @param formKey 表单KEY 对应BPM_FROM key字段。
	 * @param userId 用户ID
	 * @param flowKey 流程KEY
	 * @param nodeId 节点ID
	 * @param isGlobalPermission 
	 * @return
	 */
	String getPermission(String formKey, String userId, String flowKey,String parentFlowKey, String nodeId, boolean isGlobalPermission);

	/**
	 * 获取流程实例表单的权限。
	 * <pre>
	 * {
	 * 	field：{"NAME": "w", "SEX": "r"}
	 * 	table：{"TABLE1": "r", "TABLE2": "w"}
	 * 	opinion：{"领导意见": "w", "部门意见": "r"}
	 * }
	 * </pre>
	 * @param formKey	表单KEY 对应BPM_FROM key字段。
	 * @param userId
	 * @param flowKey
	 * @return
	 */
	String getInstPermission(String formKey, String userId, String flowKey);

	String getStartPermission(String formKey,String flowKey, String nodeId, String nextNodeId);


	/**
	 *   导入formRigths
	 * @param formfXml
	 * @return
	 */
	void importFormRights(String formRightsXml);


	/**
	 *   根据FormRight获得导出用的xml文件
	 * @param formKeys
	 * @return
	 * @throws JAXBException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	String getFormRightExportXml(ObjectNode formRight) throws JAXBException, JsonParseException, JsonMappingException, IOException;

	/**
	 * 根据表单key获得权限列表。
	 * @param formId
	 * @return ObjectNode FormService.getByFormId
	 */
	List<FormRight> getFormRigthListByFlowKey(String formId);

	/**
	 * 删除表单权限
	 * 包括bpmFormRightManager.removeInst(flowKey);bpmFormRightManager.remove(flowKey, parentFlowKey);2个方法
	 * @param flowKey
	 * @param parentFlowKey
	 * @return
	 */
	void removeFormRights(String flowKey,String  parentFlowKey);

	/**
	 *删除表单权限
	 * @param flowKey  流程定义key
	 * @param parentFlowKey 父流程定义key 
	 * @param permissionType 权限类型
	 * @return
	 */
	void removeFormRightByFlowKey (String flowKey, String parentFlowKey,int permissionType);
	
	
	
	/**
	 * 新增表单权限
	 * @param bpmFormRight
	 * @return
	 */
	void createFormRight (FormRight bpmFormRight);
	
	/**
	 * 查询表单权限
	 * @param queryFilter
	 * @return
	 */
	List<FormRight> queryFormRight (QueryFilter queryFilter);
}
