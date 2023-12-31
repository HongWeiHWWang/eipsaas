package com.hotent.uc.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.OrgDao;
import com.hotent.uc.dao.OrgPostDao;
import com.hotent.uc.dao.RoleDao;
import com.hotent.uc.dao.UserDao;
import com.hotent.uc.dao.UserGroupDao;
import com.hotent.uc.exception.HotentHttpStatus;
import com.hotent.uc.manager.UserGroupManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserGroup;
import com.hotent.uc.model.UserRel;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.userGroup.UserGroupVo;
import com.hotent.uc.util.OrgUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：群组管理  处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-11-27 17:55:17
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class UserGroupManagerImpl extends BaseManagerImpl <UserGroupDao, UserGroup> implements UserGroupManager{
	@Resource
	OrgDao orgDao;
	@Resource
	RoleDao roleDao;
	@Resource
	OrgPostDao orgPostDao;
	@Resource
	UserManager userManager;
	
	@Override
	public UserGroup getByCode(String code) {
		if(StringUtil.isEmpty(code)){
			throw new RuntimeException(HotentHttpStatus.REUIRED.description()+"：群组编码必填！");
		}
		UserGroup userGroup = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(userGroup)){
			throw new RuntimeException("群组编码：【"+code+"】不存在！");
		}
		return baseMapper.getByCode(code);
	}
	@Override
	public List<User> getUserListByGroupId(String id) throws Exception {
		List<User> list = new ArrayList<User>();
		UserGroup userGroup= this.get(id);
		if(BeanUtils.isNotEmpty(userGroup)){
			//用户
			list.addAll(OrgUtil.getUserListByTypeId(UserRel.GROUP_USER, userGroup.getUserId()));
			//组织
			list.addAll(OrgUtil.getUserListByTypeId(UserRel.GROUP_ORG, userGroup.getOrgId()));
			//角色
			list.addAll(OrgUtil.getUserListByTypeId(UserRel.GROUP_ROLE, userGroup.getRoleId()));
			//岗位
			list.addAll(OrgUtil.getUserListByTypeId(UserRel.GROUP_POS, userGroup.getPosId()));
			OrgUtil.removeDuplicate(list);
		}
		return list;
	}
	@Override
	public List<UserGroup> getByUserId(String userId) {
		return baseMapper.getByWhereSql(getGroupSql(userId));
	}
	
	
	private String getGroupSql(String userId){
		StringBuilder sql = new StringBuilder();
		//用户
		sql.append(" and user_id_ like '%,"+userId+",%' ");
		//组织
		List<Org> orgList = orgDao.getOrgListByUserId(userId);
		if(BeanUtils.isNotEmpty(orgList)){
			sql.append(" or ( ");
			boolean orgFirst = true;
			for (Org org : orgList) {
				if(orgFirst){
					orgFirst = false;
				}else{
					sql.append(" or ");
				}
				sql.append(" org_id_ like '%,"+org.getId()+",%' ");
			}
			sql.append(" ) ");
		}
		List<Role> roleList = roleDao.getListByUserId(userId);
		if(BeanUtils.isNotEmpty(roleList)){
			sql.append(" or ( ");
			boolean orgFirst = true;
			for (Role role : roleList) {
				if(orgFirst){
					orgFirst = false;
				}else{
					sql.append(" or ");
				}
				sql.append(" role_id_ like '%,"+role.getId()+",%' ");
			}
			sql.append(" ) ");
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<OrgPost> posList = orgPostDao.getRelListByParam(map);
		if(BeanUtils.isNotEmpty(posList)){
			sql.append(" or ( ");
			boolean orgFirst = true;
			for (OrgPost orgRel : posList) {
				if(orgFirst){
					orgFirst = false;
				}else{
					sql.append(" or ");
				}
				sql.append(" pos_id_ like '%,"+orgRel.getId()+",%' ");
			}
			sql.append(" ) ");
		}
		
		return sql.toString();
	}
	@Override
    @Transactional
	public CommonResult<String> addUserGroup(UserGroupVo userGroupVo) throws Exception {
		if(StringUtil.isEmpty(userGroupVo.getName())){
			throw new RuntimeException("群组添加失败，群组名称【name】必填！");
		}
		if(StringUtil.isEmpty(userGroupVo.getCode())){
			throw new RuntimeException("添加群组失败，群组代码【code】必填！");
		}
		UserGroup group = baseMapper.getByCode(userGroupVo.getCode());
		if(BeanUtils.isNotEmpty(group)){
			throw new RuntimeException("添加群组失败，群组编码【"+userGroupVo.getCode()+"】已经存在！");
		}
		User user = OrgUtil.getCurrentUser();
		group = new UserGroup();
		group.setId(UniqueIdUtil.getSuid());
		group.setCode(userGroupVo.getCode());
		group.setDescription(userGroupVo.getDescription());
		group.setName(userGroupVo.getName());
		if(BeanUtils.isNotEmpty(user)){
			group.setCreator(user.getId());
		}
		this.create(group);
		return new CommonResult<String>(true, "添加成功", "");
	}
	@Override
    @Transactional
	public CommonResult<String> updateUserGroup(UserGroupVo userGroupVo) throws Exception {
		if(StringUtil.isEmpty(userGroupVo.getCode())){
			throw new RuntimeException("更新群组失败，群组代码【code】必填！");
		}
		UserGroup group = this.getByCode(userGroupVo.getCode());
		if(BeanUtils.isEmpty(group)){
			throw new RuntimeException("更新群组失败，根据群组编码【"+userGroupVo.getCode()+"】没有找到对应的数据！");
		}
		if(StringUtil.isNotEmpty(userGroupVo.getName())){
			group.setName(userGroupVo.getName());
		}

		group.setDescription(userGroupVo.getDescription());

		group.setUpdateTime(LocalDateTime.now());
		this.update(group);
		return new CommonResult<String>(true, "更新成功", "");
	}
	@Override
    @Transactional
	public CommonResult<String> delUserGroup(String codes) throws Exception {
		String[] codeArray = codes.split(",");
		StringBuilder str = new StringBuilder();
		StringBuilder canotDEl = new StringBuilder();
		boolean isTrue = false;
		for (String code : codeArray) {
			UserGroup group = this.getByCode(code);
			if(BeanUtils.isNotEmpty(group)){
				if (OrgUtil.checkUserGruopIsUserRel("group", group.getId())) {
					canotDEl.append(code);
					canotDEl.append("，");
				}else{
					remove(group.getId());
					isTrue = true;
				}
			}else{
				str.append(code);
				str.append("，");
			}
		}
		String msg ="";
		if ( StringUtil.isEmpty(str.toString())&& StringUtil.isEmpty(canotDEl.toString())) {
			msg="删除群组成功！";
		}else {
			msg="部分删除失败";
		}
		if (StringUtil.isNotEmpty(str.toString())) {
			msg+="，群组编码："+str.toString()+"不存在！";
		}
		if (StringUtil.isNotEmpty(canotDEl.toString())) {
			msg+="，群组编码为："+canotDEl.toString()+"的群组为汇报节点不能删除！";
		}
		return new CommonResult<String>(isTrue, msg, str.toString());
	}
	@Override
    @Transactional
	public CommonResult<String> addGroupUsers(String code, List<ObjectNode> json) throws Exception {
		if(StringUtil.isEmpty(code)){
			throw new RuntimeException(HotentHttpStatus.REUIRED.description()+"：群组代码code必填！");
		}
		UserGroup group = this.getByCode(code);
		if(BeanUtils.isEmpty(group)){
			throw new RuntimeException("根据群组编码【"+code+"】没有找到对应的数据！");
		}
		if(BeanUtils.isEmpty(json)){
			throw new RuntimeException("用户组数据json为空！");
		}
		
		ArrayNode jsonArr = JsonUtil.getMapper().createArrayNode();
		for(ObjectNode o : json){
			if(BeanUtils.isEmpty(o.get("type"))){
				throw new RuntimeException("用户组数据json中type必填！");
			}
			String type = o.get("type").toString();
			if(!("user".equals(type) || "org".equals(type) || "pos".equals(type) || "role".equals(type))){
				throw new RuntimeException("用户组数据json中type只能填写user、pos、role、org四种类型！");
			}
		
			//分别为组织、用户、角色、岗位或用户代码，用户的为account
			String[] codeArr = o.get("codes").toString().split(",");
			StringBuilder ids = new StringBuilder();
			StringBuilder names = new StringBuilder();
			StringBuilder codes = new StringBuilder();
			for(String c : codeArr){
				if(StringUtil.isEmpty(c)){
					continue;
				}
				if(StringUtil.isNotEmpty(ids.toString())){
					ids.append(",");
					names.append(",");
					codes.append(",");
				}
				if("user".equals(type)){//用户类型
					User u = userManager.getByAccount(c);
					if(BeanUtils.isNotEmpty(u)){
						ids.append(u.getId());
						names.append(u.getFullname());
						codes.append(u.getAccount());
					}else{
						throw new RuntimeException("用户编码【"+c+"】不存在！");
					}
				}
				if ("org".equals(type)) {// 组织类型
					Org org = orgDao.getByCode(c);
					if(BeanUtils.isNotEmpty(org)){
						ids.append(org.getId());
						names.append(org.getName());
						codes.append(org.getCode());
					}else{
						throw new RuntimeException("组织编码【"+c+"】不存在！");
					}
				}
				if ("pos".equals(type)) {// 岗位类型
					OrgPost post = orgPostDao.getByCode(c);
					if(BeanUtils.isNotEmpty(post)){
						ids.append(post.getId());
						names.append(post.getName());
						codes.append(post.getCode());
					}else{
						throw new RuntimeException("岗位编码【"+c+"】不存在！");
					}
				}
				if ("role".equals(type)) {// 角色类型
					Role r = roleDao.getByCode(c);
					if(BeanUtils.isNotEmpty(r)){
						ids.append(r.getId());
						names.append(r.getName());
						codes.append(r.getCode());
					}else{
						throw new RuntimeException("角色编码【"+c+"】不存在！");
					}
				}
			}
			//判断加入什么组
			ObjectNode object = JsonUtil.getMapper().createObjectNode();
			object.put("type", type);
			object.put("id", ids.toString());
			object.put("name", names.toString());
			object.put("code", codes.toString());
			if("user".equals(type)){
				object.put("title", "用户");
			}
			if ("org".equals(type)) {
				object.put("title", "组织");
			}
			if ("pos".equals(type)) {
				object.put("title", "岗位");
			}
			if ("role".equals(type)) {
				object.put("title", "角色");
			}
			addObject(jsonArr,object);
		}
		
		/*		ArrayNode groupArr = JsonUtil.getMapper().createArrayNode();
		if(StringUtil.isNotEmpty(groupJson)){
			groupArr = (ArrayNode) JsonUtil.toJsonNode(groupJson);
		}
		dealNewAndOldData(jsonArr,groupArr);*/
		for(int i=0;i<jsonArr.size();i++){
			ObjectNode o = (ObjectNode) jsonArr.get(i);
			if("user".equals(o.get("type"))){
				if(StringUtil.isNotEmpty(o.get("id").toString())){
					group.setUserId(","+o.get("id")+",");
				}else{
					group.setUserId(null);
				}
			}
			if ("org".equals(o.get("type"))) {
				if(StringUtil.isNotEmpty(o.get("id").toString())){
					group.setOrgId(","+o.get("id")+",");
				}else{
					group.setOrgId(null);
				}
			}
			if ("pos".equals(o.get("type"))) {
				if(StringUtil.isNotEmpty(o.get("id").toString())){
					group.setPosId(","+o.get("id")+",");
				}else{
					group.setPosId(null);
				}
			}
			if ("role".equals(o.get("type"))) {
				if(StringUtil.isNotEmpty(o.get("id").toString())){
					group.setRoleId(","+o.get("id")+",");
				}else{
					group.setRoleId(null);
				}
			}
		}
		group.setUpdateTime(LocalDateTime.now());
		group.setJson(JsonUtil.toJson(jsonArr));
		this.update(group);
		String msg = "群组加入用户组成功";
		if(StringUtil.isEmpty(group.getUserId())&&StringUtil.isEmpty(group.getOrgId())&&StringUtil.isEmpty(group.getRoleId())&&StringUtil.isEmpty(group.getPosId())){
			msg = "群组用户组置空";
		}
		return new CommonResult<String>(true, msg, "");
	}
	
	/**
	 * 前台数据处理
	 * @param arr
	 * @param object
	 * @return
	 */
	private static ArrayNode addObject(ArrayNode arr,ObjectNode object){
		if(BeanUtils.isEmpty(arr)){
			arr.add(object);
			return arr;
		}
		for(int i=0;i<arr.size();i++){
			ObjectNode o = (ObjectNode) arr.get(i);
			if(o.get("type").equals(object.get("type"))){
				if(StringUtil.isEmpty(o.get("id").toString())){
					o.put("id", object.get("id").asText());
					o.put("name",object.get("name").asText());
					o.put("code",object.get("code").asText());
				}else{
					o.put("id", o.get("id")+","+object.get("id"));
					o.put("name", o.get("name")+","+object.get("name"));
					o.put("code", o.get("code")+","+object.get("code"));
				}
				arr.remove(i);
				arr.add(o);
				break;
			}
			if(i== arr.size()-1){
				arr.add(object);
				break;
			}
			
		}
		return arr;
	}
	
	@SuppressWarnings("unused")
	private static void dealNewAndOldData(ArrayNode newData,ArrayNode oldData){
		if(BeanUtils.isEmpty(oldData)){
			for(int i=0;i<newData.size();i++){
				ObjectNode o = (ObjectNode) newData.get(i);
				oldData.add(o);
			}
		}else{
			for(int j =0;j<newData.size();j++){
				 ObjectNode n = (ObjectNode) newData.get(j);
				for(int i=0;i<oldData.size();i++){
					ObjectNode old = (ObjectNode) oldData.get(i);
					if(n.get("type").equals(old.get("type"))){
						if(StringUtils.isNotEmpty(n.get("id").toString())){
							old.put("id", n.get("id").toString());
							old.put("name",n.get("name").toString());
							old.put("code",n.get("code").toString());
						}else{
							old.put("id", "");
							old.put("name","");
							old.put("code","");
						}
						break;
					}
				}
			}
		}
	}
	
	@Override
	public List<User> getGroupUsers(String code) throws Exception {
		if(StringUtil.isEmpty(code)){
			throw new RuntimeException(HotentHttpStatus.REUIRED.description()+"：群组代码code必填！");
		}
		UserGroup group = this.getByCode(code);
		if(BeanUtils.isEmpty(group)){
			throw new RuntimeException("根据群组编码【"+code+"】没有找到对应的数据！");
		}
		return getUserListByGroupId(group.getId());
	}
	@Override
	public List<UserGroup> getUserGroupByTime(String btime, String etime)
			throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(btime, etime);
		return this.queryNoPage(queryFilter);
	}
	@Override
	public IPage<User> getGroupUsersPage(String code,QueryFilter queryFilter) {
		if(StringUtil.isEmpty(code)){
			throw new RuntimeException(HotentHttpStatus.REUIRED.description()+"：群组代码code必填！");
		}
		UserGroup group = this.getByCode(code);
		if(BeanUtils.isEmpty(group)){
			throw new RuntimeException("根据群组编码【"+code+"】没有找到对应的数据！");
		}
		UserGroup userGroup = baseMapper.getByCode(code);
		return userManager.getGroupUsersPage(userGroup,queryFilter);
	}
	
	@Override
	public CommonResult<Boolean> isCodeExist(String code) throws Exception {
		UserGroup userGroup = baseMapper.getByCode(code);
		boolean isExist = BeanUtils.isNotEmpty(userGroup);
		return new CommonResult<Boolean>(isExist, isExist?"该群组编码已存在！":"", isExist);
	}
	
	@Override
    @Transactional
	public CommonResult<String> updateGroupAuth(String code,String account) throws Exception {
		if(StringUtil.isEmpty(code)||StringUtil.isEmpty(account)){
			throw new RuntimeException("群组编码“code”和用户账号“account”不能为空！");
		}
		UserGroup userGroup = baseMapper.getByCode(code);
		if(BeanUtils.isEmpty(userGroup)){
			throw new RuntimeException("根据群组编码【"+code+"】没有找到对应的数据！");
		}
		User user = userManager.getByAccount(account);
		if(BeanUtils.isEmpty(user)){
			throw new RuntimeException("根据用户账号【"+account+"】没有找到对应用户！");
		}
		userGroup.setCreator(user.getId());
		this.update(userGroup);
		return new CommonResult<String>(true, "修改群组管理员成功！", "");
	}
	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
}
