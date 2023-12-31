package com.hotent.uc.manager.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.UserRelDao;
import com.hotent.uc.exception.HotentHttpStatus;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.OrgPostManager;
import com.hotent.uc.manager.RelAuthManager;
import com.hotent.uc.manager.RoleManager;
import com.hotent.uc.manager.UserGroupManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserRelManager;
import com.hotent.uc.manager.UserRoleManager;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.RelAuth;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserGroup;
import com.hotent.uc.model.UserRel;
import com.hotent.uc.model.UserRole;
import com.hotent.uc.params.user.UserRelFilterObject;
import com.hotent.uc.params.user.UserRelVo;
import com.hotent.uc.util.OrgUtil;

/**
 * 
 * <pre> 
 * 描述：用户关系 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2017-06-12 09:21:48
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class UserRelManagerImpl extends BaseManagerImpl <UserRelDao, UserRel> implements UserRelManager{
	
	@Autowired
	UserRoleManager userRoleService;
	@Autowired
	UserManager userService;
	@Autowired
	OrgManager orgService;
	@Autowired
	RoleManager roleService;
	@Autowired
	OrgPostManager orgPostService;
	@Autowired
	UserGroupManager userGroupService;
	@Autowired
	RelAuthManager relAuthService;
	@Autowired
	PortalFeignService portalFeignService;

	@Override
	public List<UserRel> getByTypeId(String typeId) throws Exception {
		String authSql = getAuthSql(typeId);
		return baseMapper.getByTypeId(typeId,authSql);
	}
	@Override
	public UserRel getByUserIdAndParentId(String typeId, String value,
			String parentId) {
		return baseMapper.getByUserIdAndParentId(typeId,parentId,value);
	}
	
	@Override
	public UserRel getByAlias(String alias) {
		return baseMapper.getByAlias(alias);
	}
	
	@Override
	public List<User> getSuperUser(String account, String typeId) throws Exception {
		List<UserRel> rels = getSuperUserRel(account, typeId);
		List<User> users = new ArrayList<User>();
		if(BeanUtils.isNotEmpty(rels)){
			users = convertRelToUser(rels);
			OrgUtil.removeDuplicate(users);
		}
		return users;
	}
	
	/**
	 * 获取汇报线分类id
	 * @param userRelFilterObject
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String getRelTypeId(UserRelFilterObject userRelFilterObject) throws Exception{
		if(StringUtil.isEmpty(userRelFilterObject.getAccount()) && StringUtil.isEmpty(userRelFilterObject.getUserId())){
			throw new RequiredException(HotentHttpStatus.REUIRED.description()+"：account、userId用户账号或id任填一个！");
		}
		User user = userService.getByAccount(userRelFilterObject.getAccount());
		if(BeanUtils.isEmpty(user)){
			user = userService.get(userRelFilterObject.getUserId());
			if(BeanUtils.isEmpty(user)){
				throw new RequiredException("未获取到用户！");
			}else{
				userRelFilterObject.setAccount(user.getAccount());
			}
		}
		if(StringUtil.isEmpty(userRelFilterObject.getTypeCode())){
			throw new RequiredException(HotentHttpStatus.REUIRED.description()+"：typeCode汇报线分类编码！");
		}
		ObjectNode proType = portalFeignService.getSysTypeById(userRelFilterObject.getTypeCode());
		if(BeanUtils.isEmpty(proType)){
			throw new RequiredException("汇报线分类编码【"+userRelFilterObject.getTypeCode()+"】不存在！");
		}
		return proType.get("id").asText();
	}
	
	
	@Override
	public List<User> getAllSuperUser(String account, String typeId) throws Exception{
		List<UserRel> rels = getSuperUserRel(account,  typeId);
		List<User> users = new ArrayList<User>();
		if(BeanUtils.isNotEmpty(rels)){
			List<UserRel> list = new ArrayList<UserRel>();
			list.addAll(rels);
			for (UserRel rel : rels) {
				getParentsToTop(list, rel);
			}
			users = convertRelToUser(list);
			OrgUtil.removeDuplicate(users);
		}
		return users;
	}
	
	@Override
	public List<User> getLowerUser(String account,String typeId) throws Exception{
		List<UserRel> rels = getUserRels(account, typeId);
		List<User> users = new ArrayList<User>();
		if(BeanUtils.isNotEmpty(rels)){
			List<UserRel> sysUserRels = new ArrayList<UserRel>();
			for (UserRel rel : rels) {
				QueryFilter queryFilter = QueryFilter.build();
				queryFilter.addFilter("STATUS_", 1, QueryOP.EQUAL,FieldRelation.AND);
				if(StringUtil.isNotEmpty(typeId)){
					queryFilter.addFilter("TYPE_ID_", typeId, QueryOP.EQUAL,FieldRelation.AND);
				}
				queryFilter.addFilter("PARENT_ID_",rel.getId(), QueryOP.EQUAL,FieldRelation.AND);
				List<UserRel> childs =  this.queryNoPage(queryFilter);
				if(BeanUtils.isNotEmpty(childs)){
					sysUserRels.addAll(childs);
				}
			}
			BeanUtils.removeDuplicate(sysUserRels);
			users = convertRelToUser(sysUserRels);
			OrgUtil.removeDuplicate(users);
		}
		return users;
	}
	
	@Override
	public List<User> getAllLowerUser(String account, String typeId) throws Exception{
		List<UserRel> rels = getUserRels(account, typeId);
		List<User> users = new ArrayList<User>();
		List<UserRel> allRel = new ArrayList<UserRel>();
		if(BeanUtils.isNotEmpty(rels)){
			for (UserRel rel : rels) {
				QueryFilter queryFilter = QueryFilter.build();
				queryFilter.addFilter("STATUS_", 1, QueryOP.EQUAL,FieldRelation.AND);
		
				if(StringUtil.isNotEmpty(typeId)){
					queryFilter.addFilter("TYPE_ID_", typeId, QueryOP.EQUAL,FieldRelation.AND);
				}
				queryFilter.addFilter("ID_", rel.getId(), QueryOP.NOT_EQUAL,FieldRelation.AND);
				queryFilter.addFilter("PATH_",rel.getPath(), QueryOP.RIGHT_LIKE,FieldRelation.AND);
				List<UserRel> childs =  this.queryNoPage(queryFilter);
				if(BeanUtils.isNotEmpty(childs)){
					allRel.addAll(childs);
				}
			}
			BeanUtils.removeDuplicate(allRel);
			users.addAll(convertRelToUser(allRel));
		}
		if(BeanUtils.isNotEmpty(users)){
			OrgUtil.removeDuplicate(users);
		}
		return users;
	}
	
	/**
	 * 根据用户账号获取用户汇报线的上级
	 * @param account
	 * @param level
	 * @param typeId
	 * @return
	 * @throws Exception 
	 */
	private List<UserRel> getSuperUserRel(String account, String typeId) throws Exception{
		ArrayNode array = getUserGroupInfo(account);
		StringBuilder initSql = new StringBuilder();
		String sql = getUserRelCommonSql(array, typeId,initSql.toString());
		List<UserRel> list = baseMapper.getSuperUserRelBySql(sql);
		return list;
	}
	
	/**
	 * 根据用户账号获取用户所在汇报线
	 * @param account
	 * @param level
	 * @param typeId
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<UserRel> getUserRels(String account, String typeId) throws Exception{
		ArrayNode array = getUserGroupInfo(account);
		StringBuilder initSql = new StringBuilder();
		String sql = getUserRelCommonSql(array, typeId,initSql.toString());
		List<UserRel> list = baseMapper.getByWhereSql(sql);
		return list;
	}
	
	/**
	 * 获取用户关系通用sql
	 * @param array
	 * @param level
	 * @param typeId
	 * @param initSql
	 * @return
	 * @throws IOException 
	 */
	private String getUserRelCommonSql(ArrayNode array,String typeId,String initSql) throws IOException{
		StringBuilder sql = new StringBuilder();
		sql.append(initSql);
		if(StringUtil.isNotEmpty(typeId)){
			sql.append(" and rela.type_id_='"+typeId+"'");
		}

		String userGroupWhereSql = getUserGroupWhereSql(array);
//		String groupWhereSql = getLikeSqlByType(array);
		if(StringUtil.isNotEmpty(userGroupWhereSql)){
			sql.append(" and ( ");
			if(StringUtil.isNotEmpty(userGroupWhereSql)){
				sql.append(userGroupWhereSql);
			}
//			if(StringUtil.isNotEmpty(groupWhereSql)){
//				if(StringUtil.isNotEmpty(userGroupWhereSql)){
//					sql.append(" or ");
//				}
//				sql.append(" (");
//				sql.append(" rela.group_type_='group' and rela.value_ in ( ");
//				sql.append(" select id_ from uc_user_group where 1=1 and ");
//				sql.append(" (");
//				sql.append(groupWhereSql);
//				sql.append(" ) ");
//				sql.append(" ) ");
//				sql.append(" ) ");
//			}
			sql.append(" ) ");
		}
		return sql.toString();
	}
	
	/**
	 * 获取用户组条件sql
	 * @param array
	 * @return
	 * @throws IOException 
	 */
	private String getUserGroupWhereSql(ArrayNode array) throws IOException{
		StringBuffer sql = new StringBuffer();
		boolean isFirst = true;
		for (Object object : array) {
			ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(object);
			String type = obj.get("type").asText();
			if(StringUtil.isNotEmpty(type)){
				String typeSql =  getInSqlByType(array, type);
				if(StringUtil.isNotEmpty(typeSql)){
					if(isFirst){
						isFirst = false;
					}else{
						sql.append(" or ");
					}
					sql.append(" (rela.value_ in ("+typeSql+") and rela.group_type_='"+type+"' )");
				}
			}
		}
		return sql.toString();
	}
	
	/**
	 * 根据用户组类型获取in的条件sql
	 * @param array
	 * @param type
	 * @return
	 * @throws IOException 
	 */
	private String getInSqlByType(ArrayNode array,String type) throws IOException{
		StringBuffer sql = new StringBuffer();
		boolean isEmpty = true;
		for (Object object : array) {
			ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(object);
			if(type.equals(obj.get("type").asText())){
				if(isEmpty){
					isEmpty = false;
				}else{
					sql.append(",");
				}
				sql.append("'"+obj.get("id").asText()+"'");
			}
		}
		return isEmpty?"":sql.toString();
	}
	
	/**
	 *  获取群组的like条件sql
	 * @param array
	 * @param type
	 * @return
	 * @throws IOException 
	 */
	private String getLikeSqlByType(ArrayNode array) throws IOException{
		StringBuffer sql = new StringBuffer();
		boolean isEmpty = true;
		for (Object object : array) {
			ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(object);
			String type = obj.get("type").asText();
			if(isEmpty){
				isEmpty = false;
			}else{
				sql.append(" or ");
			}
			sql.append(" "+type.replace("\"", "")+"_id_ like '%"
					+obj.get("id").asText().replace("\"", "")+"%' ");
		}
		return isEmpty?"":sql.toString();
	}
	
	/**
	 * 根据用户账号获取用户相关的用户组信息
	 * @param account
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private ArrayNode getUserGroupInfo(String account) throws Exception{
		ArrayNode array = JsonUtil.getMapper().createArrayNode();
		if(StringUtil.isNotEmpty(account)){
			User user = userService.getByAccount(account);
			if(BeanUtils.isNotEmpty(user)){
				convertObj(array, UserRel.GROUP_USER, user.getId());
				//处理组织岗位
				QueryFilter queryFilter = QueryFilter.build();
				queryFilter.addFilter("orguser.user_id_", user.getId(), QueryOP.EQUAL,FieldRelation.AND);
				List orgUserList = userService.queryOrgUserRel(queryFilter);
				if(BeanUtils.isNotEmpty(orgUserList)){
					for (Object object : orgUserList) {
						ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(object);
						convertObj(array, UserRel.GROUP_ORG, obj.get("orgId").asText());
						if(obj.findValue("relId") !=null &&StringUtil.isNotEmpty(obj.get("relId").asText())){
							convertObj(array, UserRel.GROUP_POS, obj.get("relId").asText());
						}
					}
				}
				
				//处理角色
				List<UserRole> roles = userRoleService.getListByUserId(user.getId());
				if(BeanUtils.isNotEmpty(roles)){
					for (UserRole userRole : roles) {
						convertObj(array, UserRel.GROUP_ROLE, userRole.getRoleId());
					}
				}
			}
		}
		return array;
	}
	
	/**
	 * 封装用户组
	 * @param array
	 * @param type
	 * @param id
	 */
	private void convertObj(ArrayNode array,String type,String id){
		ObjectNode userMap = JsonUtil.getMapper().createObjectNode();
		userMap.put("type", type);
		userMap.put("id", id);
		array.add(userMap);
	}
	
	/**
	 * 从用户关系定义中抽取出用户
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	private List<User> convertRelToUser(List<UserRel> list) throws Exception{
		List<User> userList = new ArrayList<User>() ;
		if(BeanUtils.isNotEmpty(list)){
			for (UserRel rel : list) {
				String groupType = rel.getGroupType();
				String value = rel.getValue();
				if(UserRel.GROUP_GROUP.equals(groupType)){
					UserGroup sysUserGroup = userGroupService.get(value);
					if(BeanUtils.isNotEmpty(sysUserGroup)){
						//用户
						List<User> listUser = OrgUtil.getUserListByTypeId(UserRel.GROUP_USER, sysUserGroup.getUserId());
						if(BeanUtils.isNotEmpty(listUser)){
							userList.addAll(listUser);
						}
						//组织
						List<User> listOrg = OrgUtil.getUserListByTypeId(UserRel.GROUP_ORG, sysUserGroup.getOrgId());
						if(BeanUtils.isNotEmpty(listOrg)){
							userList.addAll(listOrg);
						}
						//角色
						List<User> listRole = OrgUtil.getUserListByTypeId(UserRel.GROUP_ROLE, sysUserGroup.getRoleId());
						if(BeanUtils.isNotEmpty(listRole)){
							userList.addAll(listRole);
						}
						
						//岗位
						List<User> listPos = OrgUtil.getUserListByTypeId(UserRel.GROUP_POS, sysUserGroup.getPosId());
						if(BeanUtils.isNotEmpty(listPos)){
							userList.addAll(listPos);
						}
					}
				}else{
					List<User> lists = OrgUtil.getUserListByTypeId(groupType, value);
					if(BeanUtils.isNotEmpty(lists)){
						userList.addAll(lists);
					}
				}
			}
		}
		return userList;
	}
	
	
	
	
	/**
	 * 递归获取所有父关系定义
	 * @param list
	 * @param UserRel
	 */
	private void getParentsToTop(List<UserRel> list,UserRel sysUserRel){
		if(!sysUserRel.getParentId().equals(sysUserRel.getTypeId())){
			UserRel pSysUserRel = this.get(sysUserRel.getParentId());
			if(BeanUtils.isNotEmpty(pSysUserRel)){
				list.add(pSysUserRel);
				getParentsToTop(list, pSysUserRel);
			}
		}
	}
	
	@Override
    @Transactional
	public CommonResult<String> addUserRel(List<UserRelVo> list)
			throws Exception {
		List<UserRel> relList=new ArrayList<UserRel>();
		//先判断所有的是否可以添加
		for (UserRelVo userRelVo : list) {
			UserRel userRel=  buildUserRel(userRelVo);
			if(BeanUtils.isNotEmpty(userRel))relList.add(userRel);
		 }
		for (UserRel userRel : relList) {
			this.create(userRel);
		 }
		return new CommonResult<String>(true, "添加用户关系定义成功！", "");
	}


	/**
    * 判断当用户组和父节点是否相同。以及当前父节点下是否已包含该用户组
    */
	@SuppressWarnings("unchecked")
	private String isExitInparentAndBrother(UserRel  parentUserRel,UserRel  userRel,Boolean isAdd){
		//如果子节点的用户组值，和父节点的相同
		String errMsg="";
		if (parentUserRel.getValue().equals(userRel.getValue())) {
			errMsg="所选用户组含有和父用户关系定义相同的用户组【"+userRel.getName()+"】，请重新选择！";
		}
		//根据父用户关系定义和当前用户关系定义的类型和值，来判断当前用户定义在该父用户关系定义下是否已存在
		QueryFilter queryFilter =QueryFilter.build();
		//queryFilter.setClazz(UserRel.class);
		queryFilter.addFilter("PARENT_ID_", parentUserRel.getId(), QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("VALUE_", userRel.getValue(), QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("GROUP_TYPE_", userRel.getGroupType(), QueryOP.EQUAL, FieldRelation.AND);
		PageList<UserRel> pageList= this.query(queryFilter);
		if (pageList.getTotal()>0) {
			if (isAdd) {
				errMsg="所选用户组【"+userRel.getName()+"】在当前父用户关系定义下已存在，请重新选择！";
			} else {
               if(! pageList.getRows().get(0).getId().equals(userRel.getId())) errMsg="所选用户组【"+userRel.getName()+"】在当前用户关系定义下已存在，请重新选择！";
			}	
		}
		//如果是编辑，需新增判断，当前点不能改为和子节点一样的用户
		if (!isAdd) {
			QueryFilter queryFilter1 =QueryFilter.build();
			//queryFilter1.setClazz(UserRel.class);
			queryFilter1.addFilter("PARENT_ID_", userRel.getId(), QueryOP.EQUAL, FieldRelation.AND);
			queryFilter1.addFilter("VALUE_", userRel.getValue(), QueryOP.EQUAL, FieldRelation.AND);
			queryFilter1.addFilter("GROUP_TYPE_", userRel.getGroupType(), QueryOP.EQUAL, FieldRelation.AND);
			PageList<UserRel> query = this.query(queryFilter1);
			if (query.getTotal()>0) {
				errMsg="所选用户组【"+userRel.getName()+"】在当前用户关系定义下已存在，请重新选择！";
			}
		}
		return errMsg;
	};

	/**
	 * 判断传入的UserRelVo是否可以添加。可以则返回userRel对象。不可以抛出异常
	 * @param userRelVo
	 * @return
	 * @throws Exception
	 */
		
	private UserRel buildUserRel(UserRelVo userRelVo)
			throws Exception {
		if(StringUtil.isEmpty(userRelVo.getValue())){
			throw new RequiredException(HotentHttpStatus.REUIRED.description()+"：value用户关系定义值必填！");
		}
		if(StringUtil.isEmpty(userRelVo.getType())){
			throw new RequiredException(HotentHttpStatus.REUIRED.description()+"：type用户关系定义类型必填！");
		}
		if(StringUtil.isEmpty(userRelVo.getParentAlias())){
			throw new RequiredException(HotentHttpStatus.REUIRED.description()+"：parentAlias父用户关系定义别名必填！");
		}
		
	    //根据父节点别名去找父节点
	    UserRel parentUserRel=this.getByAlias(userRelVo.getParentAlias());
	    ObjectNode proType = null;
	    String serchKey =BeanUtils.isEmpty(parentUserRel)?userRelVo.getParentAlias():parentUserRel.getTypeId();
		QueryFilter queryFilter = QueryFilter.build();
		queryFilter.addFilter("typeGroupKey", "REPORT_LINE", QueryOP.EQUAL,FieldRelation.AND,"typeGroup");
		queryFilter.addFilter("typeKey", serchKey, QueryOP.EQUAL,FieldRelation.OR,"keyGroup");
		queryFilter.addFilter("id", serchKey, QueryOP.EQUAL,FieldRelation.OR,"keyGroup");
	    ObjectNode allSysType = portalFeignService.getAllSysType(queryFilter);
	    if (BeanUtils.isNotEmpty(allSysType) && allSysType.hasNonNull("rows") && (allSysType.get("rows") instanceof ArrayNode) ) {
	    	proType = (ObjectNode) allSysType.get("rows").get(0);
		}
	    if(BeanUtils.isEmpty(parentUserRel)){//根据别名未找到汇报线。则尝试去找汇报线分类		
			if (BeanUtils.isEmpty(proType)) {
				throw new RequiredException("父用户关系定义别名【"+userRelVo.getParentAlias()+"】不存在！");
			} else {//如果找到则用分类构建一个根汇报线节点
				parentUserRel=new UserRel();
				String typeId = proType.get("id").asText();
				parentUserRel.setTypeId(typeId);
				parentUserRel.setValue(typeId);
				parentUserRel.setId(typeId);
				parentUserRel.setPath(typeId+".");
		   }
		}
		UserRel userRel = UserRelVo.parse(userRelVo);
		userRel.setId(UniqueIdUtil.getSuid());
		userRel.setAlias(proType.get("typeKey").asText()+userRel.getGroupType()+userRel.getValue());
		userRel.setTypeId(parentUserRel.getTypeId());
		userRel.setPath(parentUserRel.getPath()+userRel.getId()+".");
		userRel.setParentId(parentUserRel.getId());	
		setUserRelNameAndValue(userRel);
		userRelVo.setValue(userRel.getValue());
		
		if (BeanUtils.isNotEmpty(isExitInparentAndBrother(parentUserRel,userRel,true))) {
			return null;
		}	
		UserRel uRel=this.getByAlias(userRel.getAlias());
		if(BeanUtils.isNotEmpty(uRel)){
			userRel.setAlias(userRel.getAlias()+userRel.getId().substring(userRel.getId().length()-3, userRel.getId().length()));
		}		
		return userRel;
	}
	

	
	@Override
    @Transactional
	public CommonResult<String> deleteUserRel(String codes) throws Exception {
		String[] codeArray = codes.split(",");
		StringBuilder str = new StringBuilder();
		boolean isTrue = false;
		for (String code : codeArray) {
			UserRel userRel = getByAlias(code);
			if(BeanUtils.isNotEmpty(userRel)){
				removeByPath(userRel.getPath()+"%");
				isTrue = true;
			}else{
				str.append(code);
				str.append("，");
			}
		}
		String msg = StringUtil.isEmpty(str.toString())?"删除户关系定义成功！":"部分删除失败，户关系定义别名："+str.toString()+"不存在！";
		return new CommonResult<String>(isTrue, msg, str.toString());
	}
	
	@Override
    @Transactional
	public CommonResult<String> updateUserRel(UserRelVo userRelVo)
			throws Exception {
		//根据唯一别名。获取当前节点。取出汇报线分类，
//		UserRel userRel = userRelDao.getByAlias(userRelVo.getAlias());
		UserRel userRel = this.get(userRelVo.getId());
		if(StringUtil.isEmpty(userRelVo.getAlias())){
			throw new RequiredException("更新用户关系定义失败，用户关系定义别名【alias】必填！");
		}
		if(StringUtil.isEmpty(userRelVo.getValue())){
			throw new RequiredException("更新用户关系定义失败，用户关系定义值【value】必填！");
		}
		if(StringUtil.isEmpty(userRelVo.getType())){
			throw new RequiredException("更新用户关系定义失败，用户关系定义类型【type】必填！");
		}
		userRel.setValue(userRelVo.getValue());
		userRel.setGroupType(userRelVo.getType());
		setUserRelNameAndValue(userRel);
		if(BeanUtils.isNotEmpty(userRelVo.getStatus())){
			userRel.setStatus(userRelVo.getStatus());
		}
	
		UserRel parentUserRel=this.get(userRel.getParentId());
		if(BeanUtils.isEmpty(parentUserRel)){
			//根据别名未找到汇报线。则尝试去找汇报线分类
			ObjectNode proType = portalFeignService.getSysTypeById(userRelVo.getParentAlias());
			if (BeanUtils.isEmpty(proType)) {
				throw new RequiredException("更新用户关系定义失败，父用户关系定义别名【"+parentUserRel.getAlias()+"】不存在！");
			} else {//如果找到则用分类构建一个根汇报线节点
				parentUserRel=new UserRel();
				String typeId = proType.get("id").asText();
				parentUserRel.setTypeId(typeId);
				parentUserRel.setValue(typeId);
				parentUserRel.setId(typeId);
				parentUserRel.setPath(typeId+".");
		   }
		}
        String errMsg=isExitInparentAndBrother(parentUserRel,userRel,false);	
  	    if(BeanUtils.isNotEmpty(errMsg)){
  	    	throw new Exception(errMsg);
  	    }
		this.update(userRel);
		return new CommonResult<String>(true, "更新用户关系定义成功！", "");
	}

	/**
	 * 更新用户组信息，根据用户组别名和类型。获取其id和name，将其设置为节点的value和name
	 * @param code
	 * @param type
	 * @return
	 * @throws Exception 
	 */
    @Transactional
	private UserRel setUserRelNameAndValue(UserRel userRel ) throws Exception{
		String type=userRel.getGroupType();
		String id=userRel.getValue();
		if(UserRel.GROUP_USER.equals(type)){
			User user = userService.get(id);
			if(BeanUtils.isNotEmpty(user)){
				userRel.setValue(user.getId());
				userRel.setName(user.getFullname());
			}
		}else if(UserRel.GROUP_ORG.equals(type)){
			Org org = orgService.get(id);
			if(BeanUtils.isNotEmpty(org)){
				userRel.setValue(org.getId());
				userRel.setName(org.getName());
			}
		}else if(UserRel.GROUP_POS.equals(type)){
			OrgPost post = orgPostService.get(id);
			if(BeanUtils.isNotEmpty(post)){
				userRel.setValue(post.getId());
				userRel.setName(post.getName());
			}
		}else if(UserRel.GROUP_ROLE.equals(type)){
			Role role = roleService.get(id);
			if(BeanUtils.isNotEmpty(role)){
				userRel.setValue(role.getId());
				userRel.setName(role.getName());
			}
		}else if(UserRel.GROUP_GROUP.equals(type)){
			UserGroup group = userGroupService.get(id);
			if(BeanUtils.isNotEmpty(group)){
				userRel.setValue(group.getId());
				userRel.setName(group.getName());
			}
		}
		return userRel;
		//throw new RequiredException("根据用户组别名【"+code+"】未找到对应用户组！");
	}

	@Override
	public List<UserRel> getUserRelByTypeId(String typeId) throws Exception {
		ObjectNode proType = portalFeignService.getSysTypeById(typeId);
		List<UserRel>  list = new ArrayList<UserRel>();
		if(BeanUtils.isNotEmpty(proType)){
			UserRel userRel=new UserRel();
			userRel.setId(typeId);
			userRel.setValue(typeId);
			userRel.setAlias(proType.get("typeKey").asText());
			userRel.setParentId("-1");
			userRel.setName(proType.get("name").asText());
			userRel.setTypeId(typeId);
			String authSql = getAuthSql(typeId);
			list = baseMapper.getByTypeId(typeId,authSql);
			if(StringUtil.isEmpty(authSql)){
				list.add(userRel);
			}
		}else{
			throw new RequiredException("根据汇报线分类ID【"+typeId+"】未找到对应汇报线！");
		}
		return list;
	}
	@Override
	public PageList<UserRel> getChildRelByAilas(String code) {
		String parentId="";
		UserRel userRel = this.getByAlias(code);
		if (BeanUtils.isEmpty(userRel)) {
			ObjectNode proType = portalFeignService.getSysTypeById(code);
			if (BeanUtils.isEmpty(proType)) {
				throw new RequiredException("根据汇报线别名【"+code+"】未找到对应汇报线！");
			}else{
				parentId= proType.get("id").asText();
			}	
		}else{
			parentId=userRel.getId();
		}
		QueryFilter queryFilter =QueryFilter.build();
		//queryFilter.setClazz(UserRel.class);
		PageBean page=new PageBean(1, 1000);
		queryFilter.setPageBean(page);
		queryFilter.addFilter("PARENT_ID_",parentId , QueryOP.EQUAL, FieldRelation.AND);
		PageList<UserRel>  list=   this.query(queryFilter);
		return list;
	}
	@Override
	public List<UserRel> getUserRelByTime(String btime, String etime)
			throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(btime, etime);
		return this.queryNoPage(queryFilter);
	}
	
	@Override
	public List<User> getUsersByRel(String alias) throws Exception {
		UserRel rel = this.getByAlias(alias);
		if(BeanUtils.isEmpty(rel)){
			throw new RequiredException("汇报线别名【"+alias+"】不存在！");
		}
		List<UserRel> rels = new ArrayList<UserRel>();
		rels.add(rel);
		return this.convertRelToUser(rels);
	}
	@Override
    @Transactional
	public void removeByPath(String path) {
		baseMapper.removeByPath(path,LocalDateTime.now());
	}
	
	private String getAuthSql(String typeId) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("");	
		User user = OrgUtil.getCurrentUser();
		if(BeanUtils.isNotEmpty(user)&&!user.isAdmin()){
			List<RelAuth> auths = relAuthService.getRelAuthsByTypeAndUser(typeId, user.getId());
			sql.append(" AND ");
			if(BeanUtils.isNotEmpty(auths)){
				sql.append(" ( ");
				boolean isFirst = true;
				for (RelAuth relAuth : auths) {
					sql.append(isFirst?"":" OR ");
					sql.append(" PATH_ LIKE '"+relAuth.getRelPath()+"%' ");
					isFirst = false;
				}
				sql.append(" ) ");
			}else{
				sql.append(" ID_='0' ");
			}
		}
		return sql.toString();
	}
	@Override
	public List<UserRel> getByParentId(String parentId) throws Exception {
		if(StringUtil.isEmpty(parentId)){
			throw new RequiredException("父节点id“parentId”不能为空！");
		}
		return baseMapper.getByParentId(parentId);
	}
	@Override
    @Transactional
	public CommonResult<String> updateRelPos(String relId, String parentId)
			throws Exception {
		if(StringUtil.isEmpty(relId)||StringUtil.isEmpty(parentId)){
			throw new RequiredException("移动节点或目标节点id不能为空！");
		}
		UserRel rel = this.get(relId);
		if(BeanUtils.isEmpty(rel)){
			throw new RequiredException("根据移动节点id【"+relId+"】未找到对应节点！");
		}
		if(parentId.equals(rel.getParentId())){
			return new CommonResult<String>(true, "节点【"+rel.getName()+"】已经是其子节点，不需要更新！", "");
		}
		if(parentId.equals(rel.getTypeId())){
			throw new RequiredException("不能将节点【"+rel.getName()+"】移至分类下，一条汇报线只能有一个根节点！");
		}
		UserRel parentRel = this.get(parentId);
		List<UserRel> childrens = this.getByParentId(parentRel.getId());
		for (UserRel userRel : childrens) {
			if(!userRel.getId().equals(rel.getId())&&userRel.getValue().equals(rel.getValue())&&
					userRel.getGroupType().equals(rel.getGroupType())){
				throw new RequiredException("汇报线节点【"+parentRel.getName()+"】下已存在节点【"+rel.getName()+"】，不能添加多个!");
			}
		}
		if(BeanUtils.isEmpty(parentRel)){
			throw new RequiredException("根据目标节点id【"+parentId+"】未找到对应节点！");
		}else{
			rel.setPath(parentRel.getPath()+relId+".");
		}
		rel.setParentId(parentId);
		this.update(rel);
		updateChildrenRel(rel);
		return new CommonResult<String>(true, "更新汇报线成功！", "");
	}
	
	/**
	 * 递归更新子组织的path
	 * @param parentOrg
	 * @throws Exception 
	 */
    @Transactional
	private void updateChildrenRel(UserRel parentRel) throws Exception{
		List<UserRel> childrens = this.getByParentId(parentRel.getId());
		for (UserRel rel : childrens) {
			rel.setPath(parentRel.getPath()+rel.getId()+".");
			this.update(rel);
			updateChildrenRel(rel);
		}
	}
	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
	
	@Override
	@Transactional(readOnly=true)
	public PageList<UserRel> query(QueryFilter<UserRel> queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
		Map<String, Object> params = queryFilter.getParams();
		if(BeanUtils.isNotEmpty(params.get("typeId"))){
			queryFilter.addFilter("TYPE_ID_", params.get("typeId"), QueryOP.EQUAL);
			
		}
		if(BeanUtils.isNotEmpty(params.get("parentId"))){
			queryFilter.addFilter("PARENT_ID_", params.get("parentId"), QueryOP.EQUAL);
		}
		
		IPage<UserRel> result=baseMapper.query(convert2IPage(pageBean), convert2Wrapper(queryFilter, currentModelClass()));
		return new PageList<UserRel>(result);
	}
	
}
