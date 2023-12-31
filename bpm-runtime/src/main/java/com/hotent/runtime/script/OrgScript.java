package com.hotent.runtime.script;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.groovy.IUserScript;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.uc.api.impl.model.Org;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;
import com.hotent.uc.api.service.IUserService;



@Component
public class OrgScript implements IUserScript  {
	
	@Resource 
	UCFeignService ucFeignService;
	@Resource
	IUserService userService;

	
	
	
	/**
	 * 将用户列表转换成BpmIdentity列表
	 * @param list
	 * @return
	 */
	private Set<BpmIdentity> convertUserList(List<IUser> list){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		for (IUser iUser : list) {
			if(BeanUtils.isNotEmpty(iUser) && iUser.getStatus()==1){
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
	
	private IGroup getUserMainGroup(String userId,String demCode){
		UCFeignService service = AppUtil.getBean(UCFeignService.class);
		ObjectNode orgObj = service.getMainGroup(userId,demCode);
		try {
			if(BeanUtils.isNotEmpty(orgObj)){
				boolean isParent = orgObj.get("isParent").asBoolean();
				orgObj.put("isIsParent", isParent?1:0);
				orgObj.remove("isParent");
				IGroup org = JsonUtil.toBean(orgObj, Org.class);
				return org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取当前用户的上级部门的负责人
	 * @param isMain 是否主负责人
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Set<BpmIdentity> getChargesByPOrg(Boolean isMain,String demCode) throws JsonParseException, JsonMappingException, IOException{
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		IGroup group = getUserMainGroup(ContextUtil.getCurrentUserId(),demCode);
		if(BeanUtils.isNotEmpty(group)){
			List<ObjectNode> users = ucFeignService.getChargesByOrgId(group.getParentId(),isMain);
			if(BeanUtils.isNotEmpty(users)){
				List<IUser> list = usersObjConvertToIusers(users);
				identitys = convertUserList(list);
			}
		}
		return identitys;
	}
	
	/**
	 * 获取发起人的上级部门的（主）负责人
	 * @param isMain 是否主负责人
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Set<BpmIdentity> getChargesByStartPOrg(Boolean isMain,String demCode) throws JsonParseException, JsonMappingException, IOException{
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		String userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
		IGroup group = getUserMainGroup(userId,demCode);
		if(BeanUtils.isNotEmpty(group)){
			List<ObjectNode> users = ucFeignService.getChargesByOrgId(group.getParentId(),isMain);
			if(BeanUtils.isNotEmpty(users)){//
				List<IUser> list = usersObjConvertToIusers(users);
				identitys = convertUserList(list);
			}
		}
		return identitys;
	}
	
	/**
	 * 判断发起人是否包含某职务
	 * @param orgReldefCode
	 * @return
	 */
	public boolean isContainsJob(String orgReldefCode){
		if(StringUtil.isEmpty(orgReldefCode)){
			return false;
		}
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		String userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
		ArrayNode reldefs = (ArrayNode) ucFeignService.getJobsByUserId(userId);
		for (JsonNode jsonNode : reldefs) {
			ObjectNode job = (ObjectNode) jsonNode;
			if(orgReldefCode.equals(job.get("code").asText())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断当前用户主部门是否有上级 
	 * @param orgReldefCode
	 * @return
	 */
	public boolean isSupOrgByCurrMain(int level){
        ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
        String userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
        return ucFeignService.isSupOrgByCurrMain(userId,null,level);
    }
	
	public boolean isSupOrgByCurrMain(String demCode) throws IOException{
       IGroup group = getUserMainGroup(ContextUtil.getCurrentUserId(),demCode);
       if(StringUtil.isNotZeroEmpty(group.getParentId())){
           return true;
       }
       return false;
    }
	
	
	/**
	 * 获取当前用户的部门的直接（主）负责人（负责人中可能包含自己）
	 * @param isMain 是否主负责人
	 * @return
	 * @throws IOException 
	 */
	public Set<BpmIdentity> getChargesByOrg(boolean isMain,String demCode) throws IOException{
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		IGroup group = getUserMainGroup(ContextUtil.getCurrentUserId(),demCode);
		if(BeanUtils.isNotEmpty(group)){
			List<ObjectNode> users = ucFeignService.getChargesByOrgId(group.getGroupId(),isMain);
			if(BeanUtils.isNotEmpty(users)){
				List<IUser> list = usersObjConvertToIusers(users);
				identitys = convertUserList(list);
			}
		}
		return identitys;
	}
	
	/**
	 * 获取发起人的部门的直接（主）负责人（负责人中可能包含自己）
	 * @param isMain 是否主负责人
	 * @return
	 */
	public Set<BpmIdentity> getChargesByStartOrg(Boolean isMain,String demCode){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		String userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
		ObjectNode org =  ucFeignService.getMainGroup(userId,demCode); 
		if(BeanUtils.isNotEmpty(org)){
			List<ObjectNode> users = ucFeignService.getChargesByOrgId(org.get("groupId").asText(),isMain);
			if(BeanUtils.isNotEmpty(users)){
				List<IUser> list = usersObjConvertToIusers(users);
				identitys = convertUserList(list);
			}
		}
		return identitys;
	}
	
	
	
	/**
	 * 根据组织别名获取组织负责人
	 * @param isMain 是否主负责人
	 * @return
	 */
	public Set<BpmIdentity> getChargesByOrgCode(String code,Boolean isMain){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ObjectNode org =  ucFeignService.getOrgByIdOrCode(code);
		if(BeanUtils.isNotEmpty(org)){
			List<ObjectNode> users = ucFeignService.getChargesByOrgId(org.get("groupId").asText(),isMain);
			if(BeanUtils.isNotEmpty(users)){
				List<IUser> list = usersObjConvertToIusers(users);
				identitys = convertUserList(list);
			}
		}
		return identitys;
	}
	
	/**
	 * 根据组织编码和职务编码获取人员
	 * @param orgCode 组织编码
	 * @param redDefCode 职务编码
	 * @return
	 */
	public Set<BpmIdentity> getByOrgRelDefCode(String orgCode,String redDefCode){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		if(StringUtil.isNotEmpty(orgCode)&&StringUtil.isNotEmpty(redDefCode)){
			String[] orgCodeArray = orgCode.split(",");
			List<ObjectNode> users = new ArrayList<ObjectNode>();
			for (String ocode : orgCodeArray) {
				try {
					List<ObjectNode> userList = ucFeignService.getByOrgRelDefCode(redDefCode, ocode);
					if(BeanUtils.isNotEmpty(userList)) {
						users.addAll(userList);
					}
				} catch (Exception e) {}
			}
			if(BeanUtils.isNotEmpty(users)){
				for (ObjectNode obj : users) {
					BpmIdentity bpmIdentity = new DefaultBpmIdentity();
					bpmIdentity.setType(IdentityType.USER);
					bpmIdentity.setId(obj.get("id").asText());
					bpmIdentity.setName(obj.get("name").asText());
					identitys.add(bpmIdentity);
				}
			}
		}
		return identitys;
	}
	
	/**
	 * 根据组织编码和岗位编码获取人员
	 * @param orgCode
	 * @param relCode
	 * @return
	 */
	public Set<BpmIdentity> getByOrgRelCode(String orgCode,String relCode){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		if(StringUtil.isNotEmpty(orgCode)&&StringUtil.isNotEmpty(orgCode)){
			List<ObjectNode> orgUsers = new ArrayList<ObjectNode>();
			// TODO
			/*StringBuilder sql = new StringBuilder();
			sql.append(" select * from sys_org_user a LEFT JOIN sys_org b on a.org_id_= b.id_ LEFT JOIN sys_org_rel c on a.rel_id_=c.id_ ");
			sql.append(" where b.code_='"+orgCode+"' and c.rel_code_='"+relCode+"' ");*/
			try {
				orgUsers = ucFeignService.getByOrgRelCode( orgCode, relCode);
			} catch (Exception e) {}
			if(BeanUtils.isNotEmpty(orgUsers)){
				List<IUser> list = new ArrayList<IUser>();
				for (ObjectNode obj : orgUsers) {
					IUser iUser = userService.getUserById(obj.get("USER_ID_").asText());
					list.add(iUser);
				}
				identitys = convertUserList(list);
			}
		}
		return identitys;
	}
	
	/**
	 * 获取当前用户的上级部门的负责人
	 * @param isMain 是否主负责人
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Set<BpmIdentity> getMasterFromUnder(String orgId,String demId) throws JsonParseException, JsonMappingException, IOException{
		return getMasterFromUnderByUser(ContextUtil.getCurrentUserId(), orgId, demId);
	}
	
	/**
	 * 通过组织下属管理获取发起人在某个组织下的上级
	 * @param orgId 
	 * @param demId
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Set<BpmIdentity> getMasterFromUnderByStart(String orgId,String demId) throws JsonParseException, JsonMappingException, IOException{
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		if(BeanUtils.isNotEmpty(taskCmd)){
			String userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
			identitys =  getMasterFromUnderByUser(userId, orgId, demId);
		}
		return identitys;
	}
	
	/**
	 * 通过用户id从组织下属管理中获取上级
	 * @param userId
	 * @param orgId
	 * @param demId
	 * @return
	 */
	public Set<BpmIdentity> getMasterFromUnderByUser(String userId,String orgId,String demId){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ArrayNode arry = ucFeignService.getSuperFromUnder(userId, orgId, demId);
		if(BeanUtils.isNotEmpty(arry)){
			List<ObjectNode> list = new ArrayList<ObjectNode>();
			for (JsonNode jsonNode : arry) {
				list.add((ObjectNode)jsonNode);
			}
			List<IUser> users = usersObjConvertToIusers(list);
			identitys = convertUserList(users);
		}
		return identitys;
	}
	
	/**
	   * 获取申请人指定级别组织的负责人 
	 * @return
	 */
	public Set<BpmIdentity> getCustomLevelCharge(String level, boolean isMainCharge){
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		String userId = ContextUtil.getCurrentUserId();
		if(BeanUtils.isNotEmpty(taskCmd)){
			userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
		}
		
		ArrayNode arry = ucFeignService.getCustomLevelCharge(userId, level, isMainCharge);
		if(BeanUtils.isNotEmpty(arry)){
			List<ObjectNode> list = new ArrayList<ObjectNode>();
			for (JsonNode jsonNode : arry) {
				list.add((ObjectNode)jsonNode);
			}
			List<IUser> users = usersObjConvertToIusers(list);
			identitys = convertUserList(users);
		}
		
		return identitys;
	}
	
	/**
	   * 获取申请人指定级别的组织的指定岗位人员
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Set<BpmIdentity> getCustomLevelPost(String level, String postCode) throws JsonParseException, JsonMappingException, IOException{
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		String userId = ContextUtil.getCurrentUserId();
		if(BeanUtils.isNotEmpty(taskCmd)){
			userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
		}
		
		Set<ObjectNode> arry = ucFeignService.getCustomLevelPost(userId, level, postCode);
		if(BeanUtils.isNotEmpty(arry)){
			List<IUser> users = new ArrayList<IUser>();
			for (JsonNode jsonNode : arry) {
				UserFacade user = new UserFacade();
				user.setId(jsonNode.get("id").asText());
				user.setFullname(jsonNode.get("name").asText());
				user.setStatus(1);
				users.add(user);
			}
			identitys = convertUserList(users);
		}
		
		return identitys;
	}
	
	/**
	   * 获取申请人指定级别的组织的指定职务人员
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Set<BpmIdentity> getCustomLevelJob(String level, String jobCode) throws JsonParseException, JsonMappingException, IOException{
		Set<BpmIdentity> identitys = new LinkedHashSet<BpmIdentity>();
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		String userId = ContextUtil.getCurrentUserId();
		if(BeanUtils.isNotEmpty(taskCmd)){
			userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
		}
		
		Set<ObjectNode> arry = ucFeignService.getCustomLevelJob(userId, level, jobCode);
		if(BeanUtils.isNotEmpty(arry)){
			List<IUser> users = new ArrayList<IUser>();
			for (JsonNode jsonNode : arry) {
				UserFacade user = new UserFacade();
				user.setId(jsonNode.get("id").asText());
				user.setFullname(jsonNode.get("name").asText());
				user.setStatus(1);
				users.add(user);
			}
			identitys = convertUserList(users);
		}
		
		return identitys;
	}
	
	/**
	   * 获取申请人组织的指定扩展参数值
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public String getStartOrgParam(String param) throws JsonParseException, JsonMappingException, IOException{
		ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
		String userId = ContextUtil.getCurrentUserId();
		if(BeanUtils.isNotEmpty(taskCmd)){
			userId =  (String) taskCmd.getVariables().get(BpmConstants.START_USER);
		}
		
		String value  = ucFeignService.getStartOrgParam(userId,param);
		return value;
	}
}
