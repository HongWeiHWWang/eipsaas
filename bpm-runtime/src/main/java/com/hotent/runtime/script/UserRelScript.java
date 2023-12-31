package com.hotent.runtime.script;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.groovy.IUserScript;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;


/**
 * 根据用户关系或组织关系获取人员
 * @author zhangxw
 *
 */

@Component
public class UserRelScript implements IUserScript {
	
	@Resource
	IUserService userService;
	@Resource
	UCFeignService uCFeignService;
	
	/**
	 * 将用户列表转换成BpmIdentity列表
	 * @param list
	 * @return
	 */
	private Set<BpmIdentity> convertUserList(List<IUser> list){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		for (IUser iUser : list) {
			if(BeanUtils.isNotEmpty(iUser) && (BeanUtils.isEmpty(iUser.getStatus()) || iUser.getStatus()==1)){
				DefaultBpmIdentity bpmIdentity = new DefaultBpmIdentity();
				bpmIdentity.setId(iUser.getUserId());
				bpmIdentity.setName(iUser.getFullname());
				bpmIdentity.setType(BpmIdentity.TYPE_USER);
				identitys.add(bpmIdentity);
			}
		}
		return identitys;
	}
	
	/**
	 * 根据用户关系获取人员列表
	 * @param userId 用户ID
	 * @param typeCode 关系类型code
	 * @return
	 */
	private List<IUser> getSuperUserByRel(String userId, String typeCode){
		List<IUser> list = new ArrayList<IUser>();
		ObjectNode obj=JsonUtil.getMapper().createObjectNode();
		obj.put("userId", userId);
		obj.put("typeCode", typeCode);
		List<ObjectNode> sysUserRels = uCFeignService.getSuperUser(obj);
		if(BeanUtils.isNotEmpty(sysUserRels)){
			for (ObjectNode sysUserRel : sysUserRels) {
				IUser user = new UserFacade();
				user.setUserId(sysUserRel.get("id").asText());
				user.setFullname(sysUserRel.get("fullname").asText());
				list.add(user);
			}
		}
		return list;
	}
	
	/**
	 * json转iuser
	 * @param objs
	 * @return
	 */
	private List<IUser> usersObjConvertToIusers(List<ObjectNode> objs){
		List<IUser> list = new ArrayList<IUser>();
		if(BeanUtils.isNotEmpty(objs)){
			for (ObjectNode obj : objs) {
				UserFacade user = new UserFacade();
				user.setId(obj.get("id").asText());
				user.setAccount(obj.get("account").asText());
				user.setFullname(obj.get("fullname").asText());
				user.setUserId(obj.get("id").asText());
				user.setStatus(obj.get("status").asInt());
				list.add(user);
			}
		}
		return list;
	}
	
	/**
	 * 获取组织负责人(包含主负责人)或主负责人
	 * @param orgId  组织ID
	 * @param isMain 是否主负责人
	 * @return
	 */
	public Set<BpmIdentity> getChargesByOrgId(Object orgId,Boolean isMain){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		if(BeanUtils.isNotEmpty(orgId)){
			String obj=orgId+"";
			Object[] orgIds = obj.split(",");
			List<IUser> list = new ArrayList<IUser>();
			for (Object id : orgIds) {
				List<ObjectNode> users = uCFeignService.getChargesByOrgId(id.toString(),isMain);
				if(BeanUtils.isNotEmpty(users)) {
					list.addAll(usersObjConvertToIusers(users));
				}
			}
			if(BeanUtils.isNotEmpty(list)){
				identitys = convertUserList(list);
			}
		}
		return identitys;
	}
	
	/**
	 * 获取上级组织负责人(包含主负责人)或主负责人
	 * @param orgId  组织ID
	 * @param isMain 是否主负责人
	 * @return
	 */
	public Set<BpmIdentity> getUpChargesByOrgId(Object orgId,Boolean isMain){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		if(BeanUtils.isNotEmpty(orgId)){
			ObjectNode org =  uCFeignService.getOrgByIdOrCode(orgId.toString());
			if(BeanUtils.isNotEmpty(org)){
				identitys = getChargesByOrgId(org.get("parentId").asText(),isMain);
			}
		}
		return identitys;
	}
	
	
	/**
	 * 获取上一节点执行人的组织负责人
	 * @param isMain 是否主负责人
	 * @return
	 */
	public Set<BpmIdentity> getChargesByPreNode(Boolean isMain,String demCode){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ObjectNode orgUser = uCFeignService.getOrgUserMaster(ContextUtil.getCurrentUserId(), demCode);
		if(BeanUtils.isNotEmpty(orgUser)){
			identitys = getChargesByOrgId(orgUser.get("orgId").asText(),isMain);
		}
		return identitys;
	}
	
	/**
	 * 通过上一节点执行人获取汇报线上级 人员列表(参数选填)
	 * @param level 级别
	 * @param typeCode 类型code
	 * @return
	 */
	public Set<BpmIdentity> getByRelPreNode(String typeCode){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		List<IUser> list = getSuperUserByRel(ContextUtil.getCurrentUser().getUserId(), typeCode);
		identitys = convertUserList(list);
		return identitys;
	}
	
	/**
	 * 通过发起人获取汇报线上级 人员列表(参数选填)
	 * @param level 级别
	 * @param typeCode 类型ID
	 * @return
	 */
	public Set<BpmIdentity> getByRelStartUser(String typeCode){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		if(BeanUtils.isNotEmpty(taskCmd)){
			String userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
			List<IUser> list = getSuperUserByRel(userId, typeCode);
			identitys = convertUserList(list);
		}
		return identitys;
	}
	
	
	/**
	 * 通过上一节点执行人获取汇报线上级 人员列表
	 * @param level 级别
	 * @param typeId 类型ID
	 * @return
	 */
	public Set<BpmIdentity> getByRelPreNode(){
		return getByRelPreNode(null);
	}
	
	/**
	 * 通过发起人获取汇报线上级 人员列表
	 * @param level 级别
	 * @param typeId 类型ID
	 * @return
	 */
	public Set<BpmIdentity> getByRelStartUser(){
		return getByRelStartUser(null);
	}
	
	/**
	 * 获取子表字段(人员选择器)作为节点审批人员。
	 * @param tableName	表明
	 * @param field		字段名
	 * @return
	 * @throws IOException 
	 */
	public Set<BpmIdentity> getSubFieldUser(String tableName,String field) throws IOException{
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		String json= cmd.getBusData();
		ObjectNode jsonObj=(ObjectNode) JsonUtil.toJsonNode(json);
		Iterator<Entry<String, JsonNode>> it = jsonObj.fields();
		List<IUser> userList = new ArrayList<IUser>();
		while(it.hasNext()){
			ObjectNode mainTable = (ObjectNode) it.next();
			if(BeanUtils.isNotEmpty(mainTable)){
				ArrayNode subTable =  (ArrayNode) mainTable.get("sub_"+tableName);
				for (JsonNode object : subTable) {
					ObjectNode subData = (ObjectNode) object;
					String fieldValue = subData.get(field).asText();
					if(StringUtil.isNotEmpty(fieldValue)){
						String[] ids = fieldValue.split(",");
						for (String id : ids) {
							IUser iUser = userService.getUserById(id);
							if(BeanUtils.isNotEmpty(iUser)){
								userList.add(iUser);
							}
						}
					}
				}
			}
		}
		if(BeanUtils.isNotEmpty(userList)){
			identitys.addAll(convertUserList(userList));
		}
		return identitys;
	}
	/**
	 * 获取子表字段(部门选择器，组织负责人)作为节点审批人员。
	 * @param tableName	表明
	 * @param field		字段名
	 * @return
	 * @throws IOException 
	 */
	public Set<BpmIdentity> getSubFieldOrg(String tableName,String field,boolean isMain) throws IOException{
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		String json= cmd.getBusData();
		ObjectNode jsonObj=(ObjectNode) JsonUtil.toJsonNode(json);
		Iterator<Entry<String, JsonNode>> it = jsonObj.fields();
		while(it.hasNext()){
			ObjectNode mainTable = (ObjectNode) it.next();
			if(BeanUtils.isNotEmpty(mainTable)){
				ArrayNode subTable =  (ArrayNode) mainTable.get("sub_"+tableName);
				for (Object object : subTable) {
					ObjectNode subData = (ObjectNode) object;
					String fieldValue = subData.get(field).asText();
					if(StringUtil.isNotEmpty(fieldValue)){
						String[] orgIds = fieldValue.split(",");
						for (String orgId : orgIds) {
							Set<BpmIdentity> orgIdentitys = getChargesByOrgId(orgId, isMain);
							identitys.addAll(orgIdentitys);
						}
					}
				}
			}
		}
		return identitys;
	}
	
}
