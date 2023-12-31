package com.hotent.uc.manager.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.constants.SystemConstants;
import com.hotent.base.exception.SystemException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.jwt.JwtTokenHandler;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.PinyinUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.ZipUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.model.Group;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.dao.PropertiesDao;
import com.hotent.uc.dao.UserDao;
import com.hotent.uc.dao.UserParamsDao;
import com.hotent.uc.exception.HotentHttpStatus;
import com.hotent.uc.exception.NotFoundException;
import com.hotent.uc.exception.RequiredException;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OperateLogManager;
import com.hotent.uc.manager.OrgAuthManager;
import com.hotent.uc.manager.OrgJobManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.OrgPostManager;
import com.hotent.uc.manager.OrgUserManager;
import com.hotent.uc.manager.ParamsManager;
import com.hotent.uc.manager.PropertiesService;
import com.hotent.uc.manager.PwdStrategyManager;
import com.hotent.uc.manager.RoleManager;
import com.hotent.uc.manager.TenantAuthManager;
import com.hotent.uc.manager.UserGroupManager;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.manager.UserParamsManager;
import com.hotent.uc.manager.UserRelManager;
import com.hotent.uc.manager.UserRoleManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.model.Params;
import com.hotent.uc.model.Properties;
import com.hotent.uc.model.PwdStrategy;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserGroup;
import com.hotent.uc.model.UserParams;
import com.hotent.uc.model.UserRel;
import com.hotent.uc.model.UserRole;
import com.hotent.uc.params.common.DataSyncObject;
import com.hotent.uc.params.common.DataSyncVo;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.common.UserExportObject;
import com.hotent.uc.params.echarts.ChartLink;
import com.hotent.uc.params.echarts.ChartNode;
import com.hotent.uc.params.echarts.ChartOption;
import com.hotent.uc.params.group.GroupIdentity;
import com.hotent.uc.params.params.ParamObject;
import com.hotent.uc.params.user.TriggerVo;
import com.hotent.uc.params.user.UserMarkObject;
import com.hotent.uc.params.user.UserPolymer;
import com.hotent.uc.params.user.UserPolymerOrgPos;
import com.hotent.uc.params.user.UserPolymerRole;
import com.hotent.uc.params.user.UserPwdObject;
import com.hotent.uc.params.user.UserRelObject;
import com.hotent.uc.params.user.UserStatusVo;
import com.hotent.uc.params.user.UserVo;
import com.hotent.uc.util.OrgUtil;
import com.hotent.uc.util.UserPhotoFileFindVisitor;

import poi.util.ExcelUtil;

/**
 * 
 * <pre> 
 * 描述：用户表 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:50
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class UserManagerImpl extends BaseManagerImpl <UserDao, User> implements UserManager{
	protected static Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);
	//导入数据code重复时给code加的后缀
	protected static String IMPORT_NEW_SUFFIX = "_imp";
	@Autowired  
    ResourceLoader resourceLoader;
    @Value("${system.attachment.path:''}")
    private String attachPath;
    
	@Autowired
	UserRoleManager userRoleManager;
	@Autowired
	OrgUserManager orgUserManager;
	@Autowired
	UserParamsDao userParamsDao;
	@Autowired
	OrgManager orgService;
	@Autowired
	OrgPostManager orgPostService;
	@Autowired
	OrgJobManager orgJobService;
	@Autowired
	OrgPostManager orgPostManager;
	@Autowired
	DemensionManager demensionService;
	@Autowired
	OrgUserManager orgUserService;
	@Autowired
	PropertiesService propertiesService;
	@Autowired
	UserRoleManager userRoleService;
	@Autowired
	UserParamsManager userParamsService;
	@Autowired
	UserGroupManager userGroupService;
	@Autowired
	RoleManager roleService;
	@Autowired
	OrgAuthManager orgAuthService;
	@Autowired
	ParamsManager paramsService;
	@Autowired
	UserRelManager userRelService;
	@Autowired
	OrgManager orgManager;
	@Autowired
	DemensionManager demensionManager;
	@Autowired
	OrgJobManager orgJobManager;
	@Autowired
	RoleManager roleManager;
	@Autowired
	OperateLogManager operateLogService;
	@Autowired
	PropertiesDao propertiesDao;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	PortalFeignService portalFeignService;
	@Autowired
	JwtTokenHandler jwtTokenHandler;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	PwdStrategyManager pwdStrategyManager;
	
	@Override
    @Transactional
	public void addUser(UserVo user)throws Exception {
		if(StringUtil.isEmpty(user.getAccount())){
			throw new RequiredException("添加用户失败，用户帐号【account】必填！");
		}
		if(StringUtil.isEmpty(user.getFullname())){
			throw new RequiredException("添加用户失败，用户名称【fullname】必填！");
		}
		if(StringUtil.isEmpty(user.getPassword())){
			throw new RequiredException("添加用户失败，登录密码【password】必填！");
		}
		if(baseMapper.getCountByAccount(user.getAccount())>0){
			throw new RuntimeException("添加用户失败，帐号【"+user.getAccount()+"】已存在，请重新输入！");
		}
		
		User u = this.getByNumber(user.getUserNumber());
		if(BeanUtils.isNotEmpty(u)){
			throw new RuntimeException("添加用户失败，工号【"+user.getUserNumber()+"】已存在，请重新输入！");
		}

		if(StringUtil.isNotEmpty(user.getMobile())){
            u = this.getByMobile(user.getMobile());
            if(BeanUtils.isNotEmpty(u)){
                throw new RuntimeException("添加用户失败，手机号【"+user.getMobile()+"】已存在，请重新输入！");
            }
        }
		if (!checkEmail(user.getEmail()) && !StringUtil.isEmpty(user.getEmail())){
			throw new RuntimeException("添加用户失败，邮箱格式不正确！");
		}


		User newUser = UserVo.parser(user);
		newUser.setStatus(User.STATUS_NORMAL);
		newUser.setId(UniqueIdUtil.getSuid());
		newUser.setCreateTime(LocalDateTime.now());
		newUser.setFrom(User.FROM_RESTFUL);
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setPwdCreateTime(LocalDateTime.now());
		Integer status = BeanUtils.isNotEmpty(user.getStatus())? user.getStatus():1;
		if(status!=1&&status!=-1&&status!=-2&&status!=0){
			status = 1;
		}
		newUser.setStatus(status);
		try {
			this.create(newUser);
		} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
		}
	 }
	
	@Override
    @Transactional
	public void updateUser(UserVo user)throws Exception {
//		if(demoMode) {
//    		throw new ServerRejectException("演示模式下无法执行该操作");
//    	}
		if(StringUtil.isEmpty(user.getAccount())){
			throw new RequiredException("更新用户失败，用户帐号【account】必填！");
		}
		User u = this.getByAccount(user.getAccount());
		if(BeanUtils.isEmpty(u)){
			throw new RuntimeException("更新用户失败，根据【"+user.getAccount()+"】没有找到对应的用户信息！");
		}
		
		if(StringUtil.isNotEmpty(user.getUserNumber())){
			if(!user.getUserNumber().equals(u.getUserNumber())
					&&BeanUtils.isEmpty(this.getByNumber(user.getUserNumber()))){
				u.setUserNumber(user.getUserNumber());
			}else if(!user.getUserNumber().equals(u.getUserNumber())
					&&BeanUtils.isNotEmpty(this.getByNumber(user.getUserNumber()))){
				throw new RuntimeException("更新用户失败，工号【"+user.getUserNumber()+"】在系统中已存在，不能重复！");
			}
		}
       /* if(StringUtil.isNotEmpty(user.getMobile())){
            u = this.getByMobile(user.getMobile());
            if(BeanUtils.isNotEmpty(u) && !u.getAccount().equals(user.getAccount())){
                throw new RuntimeException("更新用户失败，账号为【"+u.getAccount()+"】的手机号【"+user.getMobile()+"】已存在，请重新输入！");
            }else{
                u = this.getByAccount(user.getAccount());
            }
        }*/
		if(StringUtil.isNotEmpty(user.getBirthday())){
			u.setBirthday(DateFormatUtil.dateParse(user.getBirthday(), StringPool.DATE_FORMAT_DATE));
		}else{
			u.setBirthday(null);
		}
		if(StringUtil.isNotEmpty(user.getEntryDate())){
			u.setEntryDate(DateFormatUtil.dateParse(user.getEntryDate(), StringPool.DATE_FORMAT_DATE));
		}else{
			u.setEntryDate(null);
		}
		//下列字段信息，为空也要设置到最新数据中
		u.setAddress(user.getAddress());
		//邮箱转为小写
		if(StringUtil.isNotEmpty(user.getEmail())){
			u.setEmail(user.getEmail().toLowerCase());
		}else{
			u.setEmail(user.getEmail());
		}
		u.setPhoto(user.getPhoto());
		u.setFullname(user.getFullname());
		u.setSex(user.getSex());
		u.setMobile(user.getMobile());
		u.setStatus(user.getStatus());
		u.setIdCard(user.getIdCard());
		u.setPhone(user.getPhone());
		u.setPhoto(user.getPhoto());
		u.setEducation(user.getEducation());
		u.setLeaveDate(user.getLeaveDate());
		this.update(u);
//        if(User.HASSYNCTOWX_YEX == u.getHasSyncToWx() && (0 == u.getStatus() || -2 == u.getStatus())) {
//            wxUserService.upStatus(u,"0");
//        }else if(User.HASSYNCTOWX_YEX == u.getHasSyncToWx() && 1 == u.getStatus()) {
//            wxUserService.upStatus(u,"1");
//        }
	}
	
	@Override
    @Transactional
	public CommonResult<String> deleteUser(UserMarkObject userMark){
//		if(demoMode) {
//    		throw new ServerRejectException("演示模式下无法执行该操作");
//    	}
		StringBuilder msg = new StringBuilder();
		boolean isTrue = false;
		if(StringUtil.isNotEmpty(userMark.getAccount())){
			String[] accounts = userMark.getAccount().split(",");
			for (String account : accounts) {
				User user = baseMapper.getByAccount(account);
				if(BeanUtils.isNotEmpty(user)){
					if (OrgUtil.checkUserGruopIsUserRel("user", user.getId())) {
						msg.append("帐号【"+account+"】的用户为汇报节点不能删除，");
					}else{
						this.remove(user.getId());
					    isTrue = true;
					}
				
				}else{
					msg.append("帐号【"+account+"】不存在，");
				}
			}
		}else{
			if(StringUtil.isNotEmpty(userMark.getUserNumber())){
				String[] numbers = userMark.getUserNumber().split(",");
				for (String number : numbers) {
					User user = baseMapper.getByNumber(number);
					if(BeanUtils.isNotEmpty(user)){
						if (OrgUtil.checkUserGruopIsUserRel("user", user.getId())) {
							msg.append("工号【"+number+"】的用户为汇报节点不能删除，");
						}else{
							this.remove(user.getId());
						    isTrue = true;
						}
						
					}else{
						msg.append("工号【"+number+"】不存在，");
					}
				}
			}
		}
		if(StringUtil.isEmpty(userMark.getAccount())&&StringUtil.isEmpty(userMark.getUserNumber())){
			isTrue = false;
			msg.append("用户帐号和工号至少输入一个，不能同时为空！");
		}
		return new CommonResult<String>(isTrue,BeanUtils.isEmpty(msg)?"删除用户成功！":msg.toString(),"");
	}
	
	@Override
    @Transactional
	public CommonResult<String> deleteUserByIds(String ids) {
//		if(demoMode) {
//    		throw new ServerRejectException("演示模式下无法执行该操作");
//    	}
		String[] idArray = ids.split(",");
		StringBuilder msg = new StringBuilder();
		boolean isTrue = true;
		for (String id : idArray) {
			User user = this.get(id);
			if(BeanUtils.isNotEmpty(user)){
				if(user.isAdmin()) {
					throw new RuntimeException("管理员用户不能删除!");
				}
				this.remove(user.getId());
//				if(User.HASSYNCTOWX_YEX == user.getHasSyncToWx()) {
//					wxUserService.upStatus(user,"0");
//				}
                //删除租户管理员
                TenantAuthManager tenantAuthManager = AppUtil.getBean(TenantAuthManager.class);
                tenantAuthManager.delByUserId(user.getUserId());
			}else{
				isTrue = false;
				msg.append("帐号【"+id+"】不存在，");
			}
		}
		return new CommonResult<String>(isTrue,BeanUtils.isEmpty(msg)?"删除用户成功！":msg.toString(),"");
	}
	
	@Override
	public UserVo getUser(String json)throws Exception {
		User user = getUserByJson(json);
		if(BeanUtils.isEmpty(user)){
			return null;
		}
		UserVo userVo = OrgUtil.convertToUserVo(user);
		List<UserParams> params = this.getUserParams(user.getAccount());
		if(BeanUtils.isNotEmpty(params)){
			Map<String,Object> map = new HashMap<String,Object>();
			for (UserParams param : params) {
				map.put(param.getAlias(), param.getValue());
			}
			userVo.setParams(map);
		}
		return userVo;
	}
	
  
	public User getByAccount(String account) {
	    return baseMapper.getByAccount(account);
	}

    public User getByMobile(String mobile) {
        return baseMapper.getByMobile(mobile);
    }
	/**
	 * 不含组织用户关系数据
	 */
	public List<User> queryOrgUser(QueryFilter queryFilter) {
		queryFilter.addFilter("org.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		queryFilter.addFilter("u.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		Map params = queryFilter.getParams();
		if(BeanUtils.isNotEmpty(params.get("orgId"))){
			queryFilter.addFilter("org.ID_", params.get("orgId"), QueryOP.EQUAL);
		}
		return baseMapper.queryOrgUser(convert2Wrapper(queryFilter, currentModelClass()));
	}
	/**
	 * 不含组织用户关系数据
	 */
	public List<User> getUserListByOrgId(String orgId) {
		return baseMapper.getUserListByOrgId(orgId);
 
	}
	
	/**
	 * 含组织用户关系数据
	 */
	@SuppressWarnings("rawtypes")
	public List queryOrgUserRel(QueryFilter queryFilter) {
		queryFilter.addFilter("orguser.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		queryFilter.addFilter("org.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		
		return baseMapper.queryOrgUserRel(convert2Wrapper(queryFilter, currentModelClass()));
	}
	
	public List<User> getListByPostCode(String postCode) {
		return baseMapper.getListByPostCode(postCode);
	}
	
	public List<User> getListByPostId(String postId) {
		return baseMapper.getListByPostId(postId);
	}
	public List<User> getUserListByRoleId(String roleId) {
		return baseMapper.getUserListByRoleId(roleId);
	}
	public List<User> getUserListByRoleCode(String roleCode) {
		return baseMapper.getUserListByRoleCode(roleCode);
	}
	
	
	@Override
    @Transactional
	public void removeByIds(String... ids)  {
		User user = baseMapper.getByAccount(SystemConstants.SYSTEM_ACCOUNT);
		for (String id : ids) {
			if(id.equals(user.getUserId())){
				ThreadMsgUtil.addMsg(ids.length>=2?"admin帐号不能删除,其它用户删除成功。":"admin帐号不能删除");
				break;
			}
		}
		if(StringUtil.isEmpty(ThreadMsgUtil.getMessage(false))){
			ThreadMsgUtil.addMsg("用户删除成功。");
		}
		super.removeByIds(ids);
		UserManagerImpl bean = AppUtil.getBean(getClass());
		for (String id : ids) {
			orgUserManager.removeByUserId(id,LocalDateTime.now());
			userRoleManager.removeByUserId(id,LocalDateTime.now());
			userParamsDao.removeByUserId(id,LocalDateTime.now());
			bean.delUserMenuCache(id);
		}
	}
	
	@CacheEvict(value = CacheKeyConst.USER_MENU_CACHENAME, key = "#userId")
	protected void delUserMenuCache(String userId) {}
	
	@Override
	public List<User> getByUserEmail(String email) {
		return baseMapper.getByUserEmail(email);
	}

	/**
	 * 添加需要新增的组织
	 * @param org
	 * @param orgList
	 * @return
	 */
	public Org dealOrg(Org org,List<Org> orgList,String preCode,Map<String,String> orgMap){
		Org rtn = null;
		if(BeanUtils.isEmpty(orgList)){
			if(BeanUtils.isNotEmpty(orgMap.get(org.getCode()))){
				org.setCode(org.getCode()+"_"+org.getId().substring(org.getId().length()-3));
			}
			orgList.add(org);
			orgMap.put(org.getCode(), org.getId());
			rtn = org;
		}else{
			Boolean flag = true; 
			for(int i=0;i<orgList.size();i++){
				if(orgList.get(i).getPathName().equals(org.getPathName()) ){
					flag = false;
					rtn = orgList.get(i);
				}
				if(orgList.get(i).getName().equals(org.getName()) &&  orgList.get(i) != org){//新增数据中，组织同名的情况
					org.setCode(preCode+"_"+PinyinUtil.getPinYinHeadChar(org.getName())+(i+1));
				}
				Org systemOrg = orgService.getByCode(org.getCode());//判断是否与数据库中组织代码重复
				if(BeanUtils.isNotEmpty(systemOrg)){
					org.setCode(preCode+"_"+PinyinUtil.getPinYinHeadChar(org.getName())+(i+1)+(i+1));
				}
				if(i == (orgList.size()-1) && flag ){
					if(BeanUtils.isNotEmpty(orgMap.get(org.getCode()))){
						org.setCode(org.getCode()+"_"+org.getId().substring(org.getId().length()-3));
					}
					orgList.add(org);
					orgMap.put(org.getCode(), org.getId());
					rtn = org;
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 处理组织的上下级关系
	 * @param supperPathName  父级路径名
	 * @param underPathName  子级路径名
	 * @param demId 对应维度id
	 * @param orgList
	 * @return
	 */
	public Org dealOrgUnder(String supperPathName,String underPathName,List<Org> orgList,String demId,String preCode,Map<String,String> orgMap){
		String underName = underPathName.substring(underPathName.lastIndexOf("/")+1, underPathName.length());
		List<Org> chirList = orgService.getByPathName(underPathName);
		if(BeanUtils.isNotEmpty(chirList)){//子组织不为空
			for (Org org : chirList) {
				if(demId.equals(org.getDemId())){
					return org;
				}
			}
		}
		//子组织为空的情况
		List<Org> pList = orgService.getByPathName(supperPathName);
		List<Org> parentList = new ArrayList<Org>();
		for (Org org : pList) {
			if(demId.equals(org.getDemId())){
				parentList.add(org);
			}
		}
		Org chird = new Org();
		if(BeanUtils.isEmpty(parentList)){//父组织为空，直接从orgList中寻找父级组织
			for(int i=0;i<orgList.size();i++){
				if(orgList.get(i).getPathName().equals(supperPathName)){
					chird.setId(UniqueIdUtil.getSuid());
					chird.setParentId(orgList.get(i).getId());
					chird.setDemId(demId);
					chird.setName(underName);
					chird.setPathName(underPathName);
					chird.setPath(orgList.get(i).getPath()+chird.getId()+".");
					chird.setCode(preCode+"_"+PinyinUtil.getPinYinHeadChar(underName));
					//chird.setOrgType("实体");
				}
			}
		}else{//父组织不为空，从数据库中拿父组织
			chird.setId(UniqueIdUtil.getSuid());
			chird.setParentId(parentList.get(0).getId());
			chird.setDemId(demId);
			chird.setName(underName);
			chird.setCode(preCode+"_"+PinyinUtil.getPinYinHeadChar(underName));
			chird.setPathName(underPathName);
			chird.setPath(parentList.get(0).getPath()+chird.getId()+".");
			//chird.setOrgType("实体");
		}
		
		return dealOrg(chird,orgList,preCode,orgMap);
	}
	
	/**
	 * 处理excel中的职务
	 * @param reldefName 职务名称
	 * @param orgJobList 新增职务列表
	 * @return
	 */
	public List<OrgJob> dealOrgJob(String reldefNameStr,List<OrgJob> orgJobList,String preCode){
		if(StringUtil.isEmpty(reldefNameStr)) return null;
		String[] reldefNames = reldefNameStr.split(";");
		List<OrgJob> rtn = new ArrayList<OrgJob>();
		for (String reldefName : reldefNames) {
			OrgJob reldef = null;
			List<OrgJob> selectList = orgJobService.getByName(reldefName);
			if(BeanUtils.isNotEmpty(selectList)){
				reldef =  selectList.get(0);
			}else{
				Boolean flag = true;
				reldef = new OrgJob();
				reldef.setId(UniqueIdUtil.getSuid());
				reldef.setCode(PinyinUtil.getPinYinHeadChar(reldefName));
				reldef.setName(reldefName);
				OrgJob sysOrgJob = orgJobService.getByCode(reldef.getCode());
				if(BeanUtils.isNotEmpty(sysOrgJob)){
					reldef.setCode(reldef.getCode()+reldef.getId());
				}
				if(BeanUtils.isEmpty(orgJobList)){
					orgJobList.add(reldef);
				}else{
					for(int i=0;i<orgJobList.size();i++){
						if(orgJobList.get(i).getName().equals(reldefName)){
							flag = false;
							reldef = orgJobList.get(i);
						}
						if(i == (orgJobList.size()-1) && flag){
							//新增
							orgJobList.add(reldef);
						}
					}
				}
			}
			if(BeanUtils.isNotEmpty(reldef)){
				rtn.add(reldef);
			}
		}
		
		
		return rtn;
	}
	
	/**
	 * 处理新增用户列表
	 * @param user
	 * @param userList
	 * @return
	 */
	public User dealUser(User user,List<User> userList) throws Exception{
		User rtn = new User();
		if(StringUtil.isNotEmpty(user.getId())){
			rtn = user;
		}else{
			Boolean flag = true;
			if(BeanUtils.isEmpty(userList)){
				user.setId(UniqueIdUtil.getSuid());
				rtn = user;
				userList.add(user);
			}else{
				for(int i=0;i<userList.size();i++){
					if(userList.get(i).getAccount().equals(user.getAccount()) && userList.get(i) != user){
						logger.error("Excel表格中用户帐号重复："+userList.get(i).getAccount());
					}
					if(userList.get(i).getAccount().equals(user.getAccount()) && userList.get(i) == user){
						flag = false;
						rtn = userList.get(i);
					}
					if(i == (userList.size()-1) && flag){
						user.setId(UniqueIdUtil.getSuid());
						rtn = user;
						userList.add(user);
					}
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 处理用、组织、岗位关系
	 * @param user
	 * @param org
	 * @param orgPost
	 * @param orgUserList
	 * @param orgPostList
	 */
	public void dealUserOrgPost(User user,Org org,List<OrgPost> orgPosts,List<OrgUser> orgUserList,List<OrgPost> orgPostList,Map<String,String> orgPostAddMap,
			String isMaster,String isCharge,Map<String,String> isMasterMap,String demId){
		if(BeanUtils.isEmpty(org)) return;
		if(BeanUtils.isNotEmpty(orgPosts)){
			for (OrgPost orgPost : orgPosts) {
				OrgUser orgUser = new OrgUser();
				if(StringUtil.isNotEmpty(orgPostAddMap.get(orgPost.getId()))){//在数据库中没有存在，考虑是否加入新增岗位列表中
					Boolean flag = true;
					if(BeanUtils.isEmpty(orgPostList)){//新增岗位列表
						orgPost.setCode(org.getCode()+"_"+orgPost.getCode());
						orgPostList.add(orgPost);
					}else{
						for(int i=0;i<orgPostList.size();i++){
							if(orgPostList.get(i).getOrgId().equals(orgPost.getOrgId()) && orgPostList.get(i).getName().equals(orgPost.getName())){
								orgPost = orgPostList.get(i);
								flag = false;
							}
							if(flag && i == (orgPostList.size()-1)){
								orgPost.setCode(org.getCode()+"_"+orgPost.getCode());
								orgPostList.add(orgPost);
							}
						}
					}
				}
				QueryFilter filter = QueryFilter.build();
				filter.addFilter("orgId", orgPost.getOrgId(), QueryOP.EQUAL,FieldRelation.AND);
				filter.addFilter("userId", user.getId(), QueryOP.EQUAL,FieldRelation.AND);
				filter.addFilter("relId", orgPost.getId(), QueryOP.EQUAL,FieldRelation.AND);
				PageList<OrgUser> pageList = orgUserService.query(filter);
				if(BeanUtils.isEmpty(pageList.getRows())){//不存在则添加组织用户关系
					orgUser.setId(UniqueIdUtil.getSuid());
					orgUser.setOrgId(orgPost.getOrgId());//组织id
					orgUser.setRelId(orgPost.getId());//岗位编号
					orgUser.setUserId(user.getId());
					dealMasterAndCharge(isMaster, isCharge, orgUser,isMasterMap,demId);
					orgUserList.add(orgUser);
				}
			}
		}else{
			OrgUser orgUser = new OrgUser();
			QueryFilter filter = QueryFilter.build();
			filter.addFilter("userId", user.getId(), QueryOP.EQUAL,FieldRelation.AND);
			PageList<OrgUser> pageList = orgUserService.query(filter);
			if(BeanUtils.isEmpty(pageList.getRows())){//不存在则添加组织用户关系
				orgUser.setId(UniqueIdUtil.getSuid());
				orgUser.setOrgId(org.getId());//组织id
				orgUser.setUserId(user.getId());
				dealMasterAndCharge(isMaster, isCharge, orgUser,isMasterMap,demId);
				orgUserList.add(orgUser);
			}
		}
	}
	
	/**
	 * 处理负责人和主组织
	 */
	private void dealMasterAndCharge(String isMaster,String isCharge,OrgUser orgUser,Map<String,String> isMasterMap,String demId){
		if(StringUtil.isNotEmpty(isMaster)&&(isMaster.equals("1")||isMaster.equals("是"))
				&&hasMaster(orgUser.getUserId(), orgUser.getOrgId(),demId)&&StringUtil.isEmpty(isMasterMap.get(orgUser.getUserId()+"_"+demId))){
			orgUser.setIsMaster(1);
			isMasterMap.put(orgUser.getUserId()+"_"+demId, "1");
		}else{
			orgUser.setIsMaster(0);
		}
		if(StringUtil.isNotEmpty(isCharge)){
			//1：负责人；2：主负责
			if(isCharge.equals("1")){
				orgUser.setIsCharge(1);
			}else if(isCharge.equals("2")){
				if(BeanUtils.isEmpty(orgUserManager.getChargesByOrgId(orgUser.getOrgId(), 2))){
					orgUser.setIsCharge(2);
				}
			}
		}else{
			orgUser.setIsCharge(0);
		}
	}
	
	private boolean hasMaster(String userId,String orgId,String demId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<OrgUser> orgUsers = orgUserManager.getByParms(map);
		if(BeanUtils.isNotEmpty(orgUsers)){
			for (OrgUser orgUser : orgUsers) {
				if(!orgUser.getOrgId().equals(orgId)&&orgUser.getIsMaster() == 1){
					Org org = orgService.get(orgUser.getOrgId());
					if(BeanUtils.isNotEmpty(org)&&org.getDemId().equals(demId)){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public List<User> getUpUsersByUserId(String underUserId) {
		return baseMapper.getUpUsersByUserId(underUserId);
	}
	@Override
	public User getUpUserByUserIdAndOrgId(String account,String orgCode) {
		User u = this.getByAccount(account);
		if(BeanUtils.isEmpty(u)){
			throw new RuntimeException("帐号为【"+account+"】的用户不存在！");
		}
		Org o = orgService.getByCode(orgCode);
		if(BeanUtils.isEmpty(o)){
			throw new RuntimeException("编码为【"+orgCode+"】的组织不存在！");
		}
		return baseMapper.getUpUserByUserIdAndOrgId(u.getId(),o.getId());
	}
	@Override
	public List<User> getUnderUsersByUserId(String upUserId) {
		return baseMapper.getUnderUsersByUserId(upUserId);
	}
	@Override
	public List<User> getUnderUserByUserIdAndOrgId(String account,String orgCode) {
		User u = this.getByAccount(account);
		if(BeanUtils.isEmpty(u)){
			throw new RuntimeException("帐号为【"+account+"】的用户不存在！");
		}
		Org o = orgService.getByCode(orgCode);
		if(BeanUtils.isEmpty(o)){
			throw new RuntimeException("编码为【"+orgCode+"】的组织不存在！");
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("upUserId", u.getId());
		map.put("orgId", o.getId());
		return baseMapper.getUnderUserByUserIdAndOrgId(u.getId(),o.getId());
	}
	
	@Override
	public List<User> getListByJobId(String jobId) {
		List<User> list = new ArrayList<User>();
		List<OrgPost> orgPost = orgPostManager.getByReldefId(jobId);
		if(BeanUtils.isEmpty(orgPost)) return list;
		for (OrgPost rel : orgPost) {
			List<User> positionUser = getListByPostId(rel.getId());
			if(BeanUtils.isNotEmpty(positionUser)){
				list.addAll(positionUser);
			}
		}
		BeanUtils.removeDuplicate(list);
		return list;
	}
   
    @Override
    public User getByNumber(String userNumber) {
    	return baseMapper.getByNumber(userNumber);
    }

	@Override
    @Transactional
	public CommonResult<String> changUserPsd(UserPwdObject userPwdObject)
			throws Exception {
		if(StringUtil.isEmpty(userPwdObject.getAccount())&&StringUtil.isEmpty(userPwdObject.getUserNumber())){
			throw new RuntimeException("帐号和工号不能同时为空！");
		}
		User user = getUserByObject(userPwdObject);
		String oldPwd = userPwdObject.getOldPwd();
		String newPwd = userPwdObject.getNewPwd();
		if(user.getStatus()!=1){
			String stateStr = user.getStatus()==0||user.getStatus()==-1?user.getStatus()==0?"已被禁用":"未激活":"已被离职";
			return new CommonResult<String>(false, "用户"+stateStr+"，不能修改密码！", "");
		}
		if(StringUtil.isEmpty(oldPwd)){
			return new CommonResult<String>(false, "旧密码不能为空！", "");
		}
		if(StringUtil.isEmpty(newPwd)){
			return new CommonResult<String>(false, "新密码不能为空！", "");
		}
		if(newPwd.equals(oldPwd)){
			return new CommonResult<String>(false, "新密码不能和旧密码一样！", "");
		}
		PwdStrategy pwdStrategy = pwdStrategyManager.getDefault();
		if(BeanUtils.isNotEmpty(pwdStrategy)) {
        	// 密码策略
        	int pwdRule = pwdStrategy.getPwdRule();
        	// 密码长度
        	int pwdLength = pwdStrategy.getPwdLength();
        	// 启用策略	0:停用，1:启用
        	int enable = pwdStrategy.getEnable();
        	if(enable == 1) {
        		if(newPwd.length() < pwdLength) {
        			return new CommonResult<String>(false, "新密码长度不能小于" + pwdLength, "");
        		}
        		if(pwdRule == 2) {//必须包含数字、字母
    				String regex ="^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";
    				boolean result=newPwd.matches(regex);
    				if(!result) {
    					return new CommonResult<String>(false, "新密码必须包含数字、字母！", "");
    				}
    			}else if(pwdRule == 3) {//必须包含数字、字母、特殊字符
    				String regex = "^(?=.*?[A-Za-z])(?=.*?\\d)(?=.*?[~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/])[a-zA-Z\\d~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]*$";
    				boolean result=newPwd.matches(regex);
    				if(!result) {
    					return new CommonResult<String>(false, "新密码必须包含数字、字母、特殊字符！", "");
    				}
    			}else if(pwdRule == 4) {//必须包含数字、大小字母、特殊字符
    				String regex = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)(?=.*?[~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/])[a-zA-Z\\d~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]*$";
    				boolean result=newPwd.matches(regex);
    				if(!result) {
    					return new CommonResult<String>(false, "新密码必须包含数字、大小字母、特殊字符！", "");
    				}
    			}
        	}
        	
		}
		
		if(user.getPassword().equals(EncryptUtil.encryptSha256(oldPwd))){
			user.setPassword(EncryptUtil.encryptSha256(newPwd));
			user.setPwdCreateTime(LocalDateTime.now());
			this.update(user);
			return new CommonResult<String>(true, "更新密码成功！", "");
		}
		else{
			return new CommonResult<String>(false, "旧密码输入错误，更新密码失败！", "");
		}
	}
	
	@Override
    @Transactional
	public CommonResult<String> updateUserPsw(UserPwdObject userPwdObject)
			throws Exception {
		if(StringUtil.isEmpty(userPwdObject.getAccount())&&StringUtil.isEmpty(userPwdObject.getUserNumber())){
			return new CommonResult<String>(false, "帐号和工号不能同时为空！", "");
		}
		if(BeanUtils.isEmpty(userPwdObject)||StringUtil.isEmpty(userPwdObject.getNewPwd())){
			return new CommonResult<String>(false, "新密码不能为空！", "");
		}
		if(userPwdObject.getNewPwd().trim().length()<6){
			return new CommonResult<String>(false, "密码长度不能小于6位！", "");
		}
		String userMarks = userPwdObject.getAccount();
		int type = 1;//1： 帐号 2：工号 3：id
		if(StringUtil.isEmpty(userMarks)&&StringUtil.isNotEmpty(userPwdObject.getUserNumber())){
			userMarks = userPwdObject.getUserNumber();
			type = 2;
		}
		List<User> users = getUserByType(userMarks, type);
		if(BeanUtils.isNotEmpty(users)){
			for (User user : users) {
				user.setPassword(passwordEncoder.encode(userPwdObject.getNewPwd()));
				user.setPwdCreateTime(LocalDateTime.now());
				this.update(user);
			}
			return new CommonResult<String>(true, "更新密码成功！", "");
		}
	    return new CommonResult<String>(false, "未更新任何用户的密码（未找到对应用户）！", "");
	}
	
	@Override
    @Transactional
	public CommonResult<String> updateOneselfPsw(UserPwdObject userPwdObject)
			throws Exception {
		/*PasswordEncoder p = null;
		if(StringUtil.isEmpty(userPwdObject.getAccount()) && StringUtil.isEmpty(userPwdObject.getUserNumber())){
			throw new RuntimeException("用户帐号与工号不能同时为空！");
		}
		User u = null;
		if(StringUtil.isNotEmpty(userPwdObject.getAccount())){
			u = this.getByAccount(userPwdObject.getAccount());
		}
		if(StringUtil.isEmpty(userPwdObject.getAccount()) && StringUtil.isNotEmpty(userPwdObject.getUserNumber())){
			u = this.getByNumber(userPwdObject.getUserNumber());
		}
		if(BeanUtils.isEmpty(u)){
			throw new RuntimeException("没有找到符合添加的用户！");
		}
		if(BeanUtils.isEmpty(userPwdObject)||StringUtil.isEmpty(userPwdObject.getNewPwd())){
			throw new RuntimeException("新密码不能为空！");
		}
		if(userPwdObject.getNewPwd().trim().length()<6){
			return new CommonResult<String>(false, "密码长度不能小于6位！", "");
		}
		if(!p.matches(userPwdObject.getOldPwd(), u.getPassword())){
			return new CommonResult<String>(false, "旧密码输入错误", "");
		}
		
		u.setPassword(p.encode(userPwdObject.getNewPwd()));
		this.update(u);*/
		return new CommonResult<String>(true, "更新密码成功！", "");
	}
    
	/**
	 * 根据用户标识的json数据获取用户
	 * @param json
	 * @return
	 * @throws IOException 
	 */
	private User getUserByJson(String json) throws IOException{
		ObjectNode markJson = (ObjectNode) JsonUtil.toJsonNode(json);
		if(BeanUtils.isEmpty(markJson)){
			throw new RequiredException("用户ID、用户帐号、用户工号必填其中一个！");
		}
		UserMarkObject userMark = (UserMarkObject) JsonUtil.toBean(markJson, UserMarkObject.class);
		
		return getUserByObject(userMark);
	}
	
	 
	@SuppressWarnings("unused")
	private PageList<UserVo> convertRestPageList(PageList<User> pageList){
		 PageList<UserVo> voPageList = new PageList<UserVo>();
		 voPageList.setPage(pageList.getPage());
		 voPageList.setPageSize(pageList.getPageSize());
		 voPageList.setTotal(pageList.getTotal());
		 voPageList.setRows(OrgUtil.convertToUserVoList(pageList.getRows()));
		 return voPageList;
	 }
	
	private User getUserByObject(Object obj) throws IOException{
		User user = null;
		if(BeanUtils.isNotEmpty(obj)){
			String key = "";
			ObjectNode json = (ObjectNode) JsonUtil.toJsonNode(obj);
			if(json.findValue("account") !=null &&StringUtil.isNotEmpty(json.get("account").asText())){
				user = this.getByAccount(json.get("account").asText());
				key = json.get("account").asText();
			}else if(json.findValue("userNumber") !=null&&StringUtil.isNotEmpty(json.get("userNumber").asText())){
				user = this.getByNumber(json.get("userNumber").asText());
				key = json.get("userNumber").asText();
			}else if(json.findValue("userId") !=null &&StringUtil.isNotEmpty(json.get("userId").asText())){
				user = this.get(json.get("userId").asText());
				key = json.get("userId").asText();
			}
			if(BeanUtils.isEmpty(user)){
				return null;
			}
		}
		return user;
	}

	private List<User> getUserByType(String userMarks,int type){
		List<User> list = new ArrayList<User>();
		if(StringUtil.isNotEmpty(userMarks)){
			String[] strArray = userMarks.split(",");
			if(type==1){
				for (String str : strArray) {
					User user = this.getByAccount(str);
					if(BeanUtils.isNotEmpty(user)){
						list.add(user);
					}
				}
			}else if(type==2){
				for (String str : strArray) {
					User user = this.getByNumber(str);
					if(BeanUtils.isNotEmpty(user)){
						list.add(user);
					}
				}
			}else if(type==3){
				for (String str : strArray) {
					User user = this.get(str);
					if(BeanUtils.isNotEmpty(user)){
						list.add(user);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 通过帐号或工号获取用户id
	 * @param account
	 * @param type 1:帐号 2：工号
	 * @return
	 */
	private String getUserId(String value,int type){
		String userId = "";
		User user = null;
		if(type==1){
			user = this.getByAccount(value);
		}else if(type==2){
			user = this.getByNumber(value);
		}
		if(BeanUtils.isNotEmpty(user)){
			userId = user.getId();
		}
		return userId;
	}


	/**
	 * 获取维度id
	 * @param userRelObject
	 * @return
	 */
	private String getDemId(UserRelObject userRelObject){
		String demId = "";
		if(StringUtil.isNotEmpty(userRelObject.getDemCode())){
			Demension demension = demensionService.getByCode(userRelObject.getDemCode());
			if(BeanUtils.isNotEmpty(demension)){
				demId = demension.getId();
			}else{
				throw new RuntimeException("编码为【"+userRelObject.getDemCode()+"】的维度不存在！");
			}
		}
		return demId;
	}
	
	private List<User> getChargesByOrgId(String orgId,Boolean isMain){
		List<User> list = new ArrayList<User>();
		List<OrgPost> orgPost = orgPostService.getRelCharge(orgId, true);
		if(BeanUtils.isNotEmpty(orgPost)){
			List<User> users = baseMapper.getListByPostId(orgPost.get(0).getId());
			list.addAll(users);
		}else{
			List<OrgUser> orgUsers = orgUserService.getChargesByOrgId(orgId,isMain);
			if(BeanUtils.isNotEmpty(orgUsers)){
				for (OrgUser orgUser : orgUsers) {
					User user = this.get(orgUser.getUserId());
					list.add(user);
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取用户的组织信息
	 * @param userRelObject
	 * @return
	 * @throws IOException 
	 */
	private List<Org> getUserOrgs(UserRelObject userRelObject) throws IOException{
		if(StringUtil.isEmpty(userRelObject.getAccount())&&StringUtil.isEmpty(userRelObject.getUserNumber())){
			throw new RuntimeException("用户帐号和工号至少输入一个，不能同时为空！");
		}
		List<Org> orgs = new ArrayList<Org>();
		User user = getUserByObject(userRelObject);
		String demId = getDemId(userRelObject);
		if(StringUtil.isNotEmpty(demId)){
			Org org =  orgService.getMainGroup(user.getId(),demId); 
			if(BeanUtils.isNotEmpty(org)){
				orgs.add(org);
			}
		}else{
			orgs = orgService.getMainOrgListByUser(user.getId(), null);
		}
		return orgs;
	}

	@Override
    @Transactional
    public CommonResult<String> saveUserParams(String account,
			List<ParamObject> params) throws Exception {
		if(StringUtil.isEmpty(account)){
			throw new RuntimeException("用户帐号不能为空！");
		}
		try {
			StringBuilder pcodes = new StringBuilder();
			User user = this.getByAccount(account);
			boolean isTrue = false;
			if(BeanUtils.isEmpty(user)){
				throw new RuntimeException("保存失败，没找到帐号为【"+account+"】的用户！");
			}else{
				List<ObjectNode> list = new ArrayList<ObjectNode>();
				if(BeanUtils.isNotEmpty(params)){
					for (ParamObject paramObject : params) {
						Params param = paramsService.getByAlias(paramObject.getAlias());
						if(BeanUtils.isNotEmpty(param)&&"1".equals(param.getType())){
							list.add((ObjectNode) JsonUtil.toJsonNode(paramObject.toString()));
							isTrue = true;
						}else{
							pcodes.append(paramObject.getAlias()+"，");
						}
					}
				}
				if(BeanUtils.isNotEmpty(list)){
					userParamsService.saveParams(user.getId(), list);
					return new CommonResult<String>(true, isTrue&&StringUtil.isEmpty(pcodes.toString())?"保存成功":"部分保存成功，用户参数编码："+pcodes+"不存在！", "");
				}
				throw new RuntimeException ("未保存任何参数，用户参数编码："+pcodes+"不存在！");
			}
		} catch (Exception e) {
			String msg=e.getMessage();
			if(msg.indexOf("ORA-12899")>-1) msg="参数值过长";
			throw new RuntimeException("保存失败，"+msg);
		}
	}

	@Override
	public List<UserParams> getUserParams(String account) throws Exception {
		if(StringUtil.isEmpty(account)){
			throw new RuntimeException("帐号不能为空！");
		}
		String userId = getUserId(account, 1);
		if(StringUtil.isEmpty(userId)){
			throw new RuntimeException("没找到帐号为【"+account+"】的用户！");
		}else{
			return userParamsService.getByUserId(userId);
		}
	}

	@Override
	public UserParams getParamByCode(String account, String code)
			throws Exception {
		if(StringUtil.isEmpty(account)||StringUtil.isEmpty(code)){
			throw new RuntimeException("帐号和参数编码不能为空！");
		}
		String userId = getUserId(account, 1);
		if(StringUtil.isEmpty(userId)){
			throw new RuntimeException("没找到帐号为【"+account+"】的用户！");
		}else{
			return userParamsService.getByUserIdAndAlias(userId, code);
		}
	}
	
	@Override
	public CommonResult<Org> getMainOrgByDemCode(UserRelObject userRelObject)
			throws Exception {
		User user = getUserByObject(userRelObject);
		String demCode = userRelObject.getDemCode();
		if(StringUtil.isEmpty(demCode)){
			return new CommonResult<Org>(false, "请传入维度编码！",null);
		}
		Demension demension = demensionService.getByCode(demCode);
		if(BeanUtils.isEmpty(demension)){
			return new CommonResult<Org>(false, "根据维度编码【"+demCode+"】未找到对应维度！",null);
		}
		Org org = orgService.getMainGroup(user.getUserId(), demension.getId());
		boolean isTrue = BeanUtils.isNotEmpty(org)?true:false;
		String msg = isTrue?"成功获取用户主组织！":"用户在【"+demension.getDemName()+"】维度下未设置主组织！";
		return new CommonResult<Org>(isTrue, msg, org);
	}
	
	@Override
	public List<OrgPost> getUserPosts(UserRelObject userRelObject)
			throws Exception {
		User user = getUserByObject(userRelObject);
		String demId = "";
		if(StringUtil.isNotEmpty(userRelObject.getDemCode())){
			Demension demension = demensionService.getByCode(userRelObject.getDemCode());
			if(BeanUtils.isNotEmpty(demension)){
				demId = demension.getId();
			}else{
				throw new RuntimeException("根据输入的维度编码没有找到对应的维度信息！");
			}
		}
		List<OrgPost> list = orgPostService.getListByUserId(user.getId(),demId);
		if(BeanUtils.isNotEmpty(list)&&BeanUtils.isNotEmpty(userRelObject.getIsMain())&&userRelObject.getIsMain()){
			List<OrgPost> list2 = new ArrayList<OrgPost>();
			list2.addAll(list);
			for (OrgPost orgPost : list2) {
				if(BeanUtils.isNotEmpty(orgPost.getIsCharge())&& 1 != orgPost.getIsCharge()){
					list.remove(orgPost);
				}
			}
		}
		return list;
	}

	@Override
	public Set<GroupIdentity> getImmeSuperior(UserRelObject userRelObject)
			throws Exception {
		Set<GroupIdentity> identitys = new HashSet<GroupIdentity>();
		List<Org> orgs = getUserOrgs(userRelObject);
		for (Org org : orgs) {
			String orgId =  org.getParentId();
			if(StringUtil.isNotEmpty(orgId)){
				identitys.addAll(OrgUtil.convertToGroupIdentity(getChargesByOrgId(orgId,BeanUtils.isEmpty(userRelObject.getIsMain())?false:userRelObject.getIsMain())));
			}
		}
		return identitys;
	}

	@Override
	public Set<GroupIdentity> getImmeUnders(UserRelObject userRelObject)
			throws Exception {
		Set<GroupIdentity> identitys = new HashSet<GroupIdentity>();
		List<Org> orgs = getUserOrgs(userRelObject);
		QueryFilter queryFilter = QueryFilter.build();
		StringBuilder orgIds = new StringBuilder();
		boolean isFirst = true;
		for (Org org : orgs) {
			if(isFirst){
				isFirst = false;
			}else{
				orgIds.append(",");
			}
			orgIds.append(org.getId());
		}
		//queryFilter.setClazz(Org.class);
		if(StringUtil.isEmpty(orgIds.toString())){
			return identitys;
		}
		queryFilter.addFilter("parentId", orgIds.toString(), QueryOP.IN,FieldRelation.AND);
		List<Org> chirldOrgs =orgService.query(queryFilter).getRows();
		for (Org org : chirldOrgs) {
			identitys.addAll(OrgUtil.convertToGroupIdentity(getUserListByOrgId(org.getId())));
		}
		return identitys;
	}

	@Override
	public List<OrgJob> getUserJobs(String json) throws Exception {
		User user = getUserByJson(json);
		return orgJobService.getListByUserId(user.getId());
	}

	@Override
	public List<UserGroup> getUserGroups(String json) throws Exception {
		User user = getUserByJson(json);
		return userGroupService.getByUserId(user.getUserId());
	}

	@Override
	public List<UserVo> getNewUsersFromAD(String dateStr) throws Exception {
		if(StringUtil.isEmpty(dateStr)){
			throw new RequiredException(HotentHttpStatus.REUIRED.description()+"：date时间必填！");
		}
		QueryFilter queryFilter = QueryFilter.build();
		//queryFilter.setClazz(User.class);
		queryFilter.addFilter("from", User.FROM_AD, QueryOP.EQUAL,FieldRelation.AND);
		if(StringUtil.isNotEmpty(dateStr)){
			dateStr = dateStr.trim().length()==10?dateStr+" 00:00:00":dateStr;
			LocalDateTime date = DateFormatUtil.parse(dateStr, StringPool.DATE_FORMAT_DATETIME);
			queryFilter.addFilter("createTime", date, QueryOP.GREAT_EQUAL,FieldRelation.AND);
		}
		List<User> list =query(queryFilter).getRows();
		return OrgUtil.convertToUserVoList(list);
	}
	
	@Override
	public  Set<GroupIdentity> getByRoleCodeAndOrgCode(String roleCode, String orgCode)
			throws Exception {
		Role role = roleService.getByAlias(roleCode);
		if(BeanUtils.isEmpty(role)){
			throw new RequiredException("编码为【"+roleCode+"】的角色不存在！");
		}
		Org org = orgService.getByCode(orgCode);
		if(BeanUtils.isEmpty(org)){
			throw new RequiredException("编码为【"+orgCode+"】的组织不存在！");
		}
		Set<GroupIdentity> identitys = new HashSet<GroupIdentity>();
		List<OrgUser> orgUsers = orgUserManager.getByOrgCodeAndroleCode(orgCode, roleCode);
		if(BeanUtils.isNotEmpty(orgUsers)){
			List<User> list = new ArrayList<User>();
			for (OrgUser orgUser : orgUsers) {
				User user = get(orgUser.getUserId());
				if(BeanUtils.isNotEmpty(user)){
					list.add(user);
				}
			}
			for(int i=0;i<list.size();i++){
				for(int j=list.size()-1;j>i;j--){
					 if(list.get(i).getId().equals(list.get(j).getId())){
						 list.remove(j);
					 }
				}
			}
			identitys = OrgUtil.convertToGroupIdentity(list);
		}
		return identitys;
	}

	@Override
	public Set<GroupIdentity> getByJobCodeAndOrgCode(String jobCode, String orgCode)
			throws Exception {
		OrgJob job = orgJobService.getByCode(jobCode);
		if(BeanUtils.isEmpty(job)){
			throw new RequiredException("编码为【"+jobCode+"】的职务不存在！");
		}
		Org org = orgService.getByCode(orgCode);
		if(BeanUtils.isEmpty(org)){
			throw new RequiredException("编码为【"+orgCode+"】的组织不存在！");
		}
		Set<GroupIdentity> identitys = new HashSet<GroupIdentity>();
		List<User> users = baseMapper.getByJobCodeAndOrgCode(orgCode, jobCode);
		identitys = OrgUtil.convertToGroupIdentity(users);
		return identitys;
	}

	@Override
	public  Set<GroupIdentity> getByPostCodeAndOrgCode(String postCode, String orgCode)
			throws Exception {
		OrgPost post = orgPostService.getByCode(postCode);
		if(BeanUtils.isEmpty(post)){
			throw new RequiredException("编码为【"+postCode+"】的岗位不存在！");
		}
		Org org = orgService.getByCode(orgCode);
		if(BeanUtils.isEmpty(org)){
			throw new RequiredException("编码为【"+orgCode+"】的组织不存在！");
		}
		Set<GroupIdentity> identitys = new HashSet<GroupIdentity>();
		List<OrgUser> orgUsers = orgUserManager.getByPostCodeAndOrgCode(orgCode, postCode);
		if(BeanUtils.isNotEmpty(orgUsers)){
			List<User> list = new ArrayList<User>();
			for (OrgUser orgUser : orgUsers) {
				User user = get(orgUser.getUserId());
				if(BeanUtils.isNotEmpty(user)){
					list.add(user);
				}
			}
			identitys = OrgUtil.convertToGroupIdentity(list);
		}
		return identitys;
	}
	
	@Override
	public List<UserVo> getUpUsersByUser(String account) throws Exception {
		String userId = getUserId(account, 1);
		if(BeanUtils.isEmpty(userId)){
			throw new RuntimeException("没找到帐号为【"+account+"】的用户！");
		}
		return OrgUtil.convertToUserVoList(getUpUsersByUserId(userId));
	}

	@Override
	public UserVo getUpUserByUserAndOrg(String account, String orgCode)
			throws Exception {
		return OrgUtil.convertToUserVo(getUpUserByUserIdAndOrgId(account, orgCode));
	}

	@Override
	public List<UserVo> getUnderUsersByUser(String account) throws Exception {
		String userId = getUserId(account, 1);
		if(BeanUtils.isEmpty(userId)){
			throw new RuntimeException("没找到帐号为【"+account+"】的用户！");
		}
		return OrgUtil.convertToUserVoList(getUnderUsersByUserId(userId));
	}

	@Override
	public List<UserVo> getUnderUserByUserAndOrg(String account,
			String orgCode) throws Exception {
		return OrgUtil.convertToUserVoList(getUnderUserByUserIdAndOrgId(account, orgCode));
	}
	
	@Override
	public List<User> getOrgUsers(String orgId, Boolean isMain) {
		Map<String,String> params = new HashMap<String,String>();
		if(BeanUtils.isNotEmpty(isMain)){
			if(isMain){
				params.put("isMain", "1");
			}else{
				params.put("isMain", "0");
			}
		}
		params.put("orgId", orgId);
		return baseMapper.getOrgUsers(params);
	}

	@Override
    @Transactional
	public void updatePhoto(String account, String photo) throws Exception {
		User user = baseMapper.getByAccount(account);
		if(BeanUtils.isEmpty(user)){
			throw new RuntimeException("没找到帐号为【"+account+"】的用户！");
		}
		user.setPhoto(photo);
		this.update(user);
	}
	
	@Override
	public CommonResult<String> forbiddenUser(UserMarkObject userMark) throws Exception {
		return toDealUserState(userMark, 0);
	}

	@Override
	public CommonResult<String> activateUser(UserMarkObject userMark) throws Exception {
		return toDealUserState(userMark, 1);
	}
	
	@Override
	public CommonResult<String> leaveUser(UserMarkObject userMark) throws Exception {
		return toDealUserState(userMark, -2);
	}
	
	private CommonResult<String> toDealUserState(UserMarkObject userMark,int state) throws SQLException{
		StringBuilder msg = new StringBuilder();
		boolean isTrue = false;
		if(StringUtil.isNotEmpty(userMark.getAccount())){
			String[] accounts = userMark.getAccount().split(",");
			for (String account : accounts) {
				User user = baseMapper.getByAccount(account);
				if(BeanUtils.isNotEmpty(user)&&user.getStatus()!=state){
					dealUserState(user, state);
					isTrue = true;
				}else{
					String dmsg = BeanUtils.isEmpty(user)?"不存在":state==1||state==0?state==1?"已激活":"已被禁用":"已被离职";
					msg.append("用户帐号【"+account+"】"+dmsg+"，");
				}
			}
		}
		if(StringUtil.isNotEmpty(userMark.getUserNumber())){
			String[] numbers = userMark.getUserNumber().split(",");
			for (String number : numbers) {
				User user = baseMapper.getByNumber(number);
				if(BeanUtils.isNotEmpty(user)&&user.getStatus()!=state){
					dealUserState(user, state);
					isTrue = true;
				}else{
					String dmsg = BeanUtils.isEmpty(user)?"不存在":state==1||state==0?state==1?"已激活":"已被禁用":"已被离职";
					msg.append("用户工号【"+number+"】"+dmsg+"，");
				}
			}
		}
		String successMsg = state==1||state==0?state==1?"激活用户成功！":"用户禁用成功":"离职用户成功";
		if(StringUtil.isEmpty(userMark.getAccount())&&StringUtil.isEmpty(userMark.getUserNumber())){
			isTrue = false;
			msg.append("用户帐号和工号至少输入一个，不能同时为空！");
		}
		return new CommonResult<String>(isTrue, BeanUtils.isNotEmpty(msg)?msg.toString():successMsg, "");
	}
	
	@Override
	public List<UserVo> getUserByPost(String postCode) {
		if(StringUtil.isEmpty(postCode)){
			throw new RequiredException(HotentHttpStatus.REUIRED.description()+"：postCode岗位编码必填！");
		}
		OrgPost post = orgPostService.getByCode(postCode);
		if(BeanUtils.isEmpty(post)){
			post =orgPostService.get(postCode);
		}
		if(BeanUtils.isEmpty(post)){
			throw new RequiredException("岗位编码【"+postCode+"】不存在！");
		}
		return OrgUtil.convertToUserVoList(baseMapper.getUserByPost(post.getCode()));
	}
    
	
	@Override
    @Transactional
	public String exportUsers(boolean isOrg,boolean isRole,boolean isAll,QueryFilter queryFilter)
			throws Exception {
    	//queryFilter.setClazz(User.class);
    	boolean isOtherAll = isAll&&BeanUtils.isEmpty(queryFilter.getQuerys());
    	List<User> userList = new ArrayList<User>();
    	if(isAll){
    		queryFilter.addFilter("isDelete", "1", QueryOP.NOT_EQUAL, FieldRelation.AND, "group_1");
    		userList = this.queryNoPage(queryFilter);
    	}else{
    		PageList<User> query = this.query(queryFilter);
    		userList = query.getRows();
    	}
		String zipFilePath = "";
		if(BeanUtils.isNotEmpty(userList)){
			// 拼装exprotMaps
			Field[] baseFields = UserDao.class.getDeclaredFields();			
			String fileName = "htuc_user_"+DateFormatUtil.format(LocalDateTime.now(), "yyyy_MMdd_HHmm");
			String zipPath = (FileUtil.getIoTmpdir() +"/attachFiles/tempZip/" + fileName).replace("/", File.separator);
			String excelPath = zipPath + File.separator;
			
			//导出用户
			this.exportExcel(baseFields, userList, "用户列表", "user", excelPath);
			if(isOrg||isRole){
				List<String> userIds = new ArrayList<String>();
				if(!isOtherAll){
					for (User user : userList) {
						userIds.add(user.getId());
					}
				}
				if(isOrg){
					//导出用户组织关系（包含组织、用户组织关系、职务、岗位）
					this.exportOrg(userIds, baseFields, excelPath, fileName,isOtherAll);
				}
				if(isRole){
					//导出用户角色关系（包含角色、用户角色关系）
					this.exportRoles(userIds, baseFields, excelPath,isOtherAll);
				}
			}
			// 打包
			ZipUtil.zip(zipPath, true);
			zipFilePath = (FileUtil.getIoTmpdir() +"/attachFiles/tempZip/").replace("/", File.separator)+fileName;
		}else{
			throw new RuntimeException("没有需要导出的用户组织信息！");
		}
		return zipFilePath;
	}
	
    
    /**
     * 处理用户状态
     * @param user
     * @param status
     * @throws SQLException 
     */
    @Transactional
    private void dealUserState(User user,Integer status) throws SQLException{
    	if(BeanUtils.isNotEmpty(user)){
    		user.setStatus(status);
    		this.update(user);
    	}
    }
    
    /**
     * 导出组织
     * @param userIds
     * @param baseFields
     * @param excelPath
     * @param fileName
     * @throws Exception
     */
    @Transactional
    private void exportOrg(List<String> userIds,Field[] baseFields,String excelPath,String fileName,boolean isOtherAll) throws Exception{
    	QueryFilter orgUserQueryFilter = QueryFilter.build();
    	if(!isOtherAll){
    		orgUserQueryFilter.addFilter("userId", userIds, QueryOP.IN,FieldRelation.AND);
    	}
    	orgUserQueryFilter.addFilter("isDelete", "1", QueryOP.NOT_EQUAL, FieldRelation.AND, "group_1");
    	//orgUserQueryFilter.setClazz(OrgUser.class);
    	List<OrgUser> orgUsers = orgUserManager.queryNoPage(orgUserQueryFilter);
    	if(BeanUtils.isNotEmpty(orgUsers)){
    		//导出用户组织关系
    		this.exportExcel(baseFields, orgUsers, "用户组织关系列表", "orgUser", excelPath);
    		List<String> orgIds = new ArrayList<String>();
    		List<String> postIds = new ArrayList<String>();
    		for (int i=0;i<orgUsers.size();i++) {
    			orgIds.add(orgUsers.get(i).getOrgId());
    			if(StringUtil.isNotEmpty(orgUsers.get(i).getRelId())){
    				postIds.add(orgUsers.get(i).getRelId());
    			}
			}
    		//导出组织
    		QueryFilter orgQueryFilter = QueryFilter.build();
    		BeanUtils.removeDuplicate(orgIds);
    		orgQueryFilter.addFilter("id", orgIds, QueryOP.IN,FieldRelation.AND);
    		orgQueryFilter.addFilter("isDelete", "1", QueryOP.NOT_EQUAL, FieldRelation.AND, "group_1");
    		//orgQueryFilter.setClazz(Org.class);
        	List<Org> orgs = orgManager.queryNoPage(orgQueryFilter);
        	if(BeanUtils.isNotEmpty(orgs)){
        		this.exportExcel(baseFields, orgs, "组织列表", "org", excelPath);
        		//导出维度
        		List<String> demIds = new ArrayList<String>();
        		for (Org org : orgs) {
        			demIds.add(org.getDemId());
				}
        		exportDemension(baseFields, excelPath, demIds);
        	}
        	//导出岗位
        	if(BeanUtils.isNotEmpty(postIds)){
        		exportJobAndPost(baseFields, excelPath, postIds);
        	}
        	
    	}
    	
    }
    
    /**
     * 导出维度
     * @param userIds
     * @param baseFields
     * @param excelPath
     * @param postIds
     * @throws Exception
     */
    @Transactional
    private void exportDemension(Field[] baseFields,String excelPath,List<String> demIds) throws Exception{
    	BeanUtils.removeDuplicate(demIds);
		QueryFilter demQueryFilter = QueryFilter.build();
		demQueryFilter.addFilter("id", demIds, QueryOP.IN,FieldRelation.AND);
		demQueryFilter.addFilter("isDelete", "1", QueryOP.NOT_EQUAL, FieldRelation.AND, "group_1");
		//demQueryFilter.setClazz(OrgPost.class);
		List<Demension> dems = demensionManager.queryNoPage(demQueryFilter);
		if(BeanUtils.isNotEmpty(dems)){
    		this.exportExcel(baseFields, dems, "维度列表", "demension", excelPath);
    	}
    }
    
    
    /**
     * 导出职务岗位
     * @param userIds
     * @param baseFields
     * @param excelPath
     * @param postIds
     * @throws Exception
     */
    @Transactional
    private void exportJobAndPost(Field[] baseFields,String excelPath,List<String> postIds) throws Exception{
    	BeanUtils.removeDuplicate(postIds);
		QueryFilter postQueryFilter = QueryFilter.build();
		postQueryFilter.addFilter("id", postIds, QueryOP.IN,FieldRelation.AND);
		postQueryFilter.addFilter("isDelete", "1", QueryOP.NOT_EQUAL, FieldRelation.AND, "group_1");
		//postQueryFilter.setClazz(OrgPost.class);
		List<OrgPost> orgPosts = orgPostManager.queryNoPage(postQueryFilter);
		if(BeanUtils.isNotEmpty(orgPosts)){
    		this.exportExcel(baseFields, orgPosts, "岗位列表", "post", excelPath);
    		
    		List<String> jobIds = new ArrayList<String>();
    		//导出职务
    		for (OrgPost orgPost : orgPosts) {
    			if(StringUtil.isNotEmpty(orgPost.getRelDefId())){
    				jobIds.add(orgPost.getRelDefId());
    			}
			}
    		QueryFilter jobQueryFilter = QueryFilter.build();
    		jobQueryFilter.addFilter("id", jobIds, QueryOP.IN,FieldRelation.AND);
    		jobQueryFilter.addFilter("isDelete", "1", QueryOP.NOT_EQUAL, FieldRelation.AND, "group_1");
    		//jobQueryFilter.setClazz(OrgJob.class);
    		List<OrgJob> orgJobs = orgJobManager.queryNoPage(jobQueryFilter);
    		if(BeanUtils.isNotEmpty(orgJobs)){
    			this.exportExcel(baseFields, orgJobs, "职务列表", "job", excelPath);
    		}
    	}
    }
    
    /**
     * 导出角色
     * @param userIds
     * @param baseFields
     * @param excelPath
     * @param postIds
     * @throws Exception
     */
    @Transactional
    private void exportRoles(List<String> userIds,Field[] baseFields,String excelPath,boolean isOtherAll) throws Exception{
    	BeanUtils.removeDuplicate(userIds);
		QueryFilter userRoleQueryFilter = QueryFilter.build();
		if(!isOtherAll){
			userRoleQueryFilter.addFilter("userId", userIds, QueryOP.IN,FieldRelation.AND);
		}
		userRoleQueryFilter.addFilter("isDelete", "1", QueryOP.NOT_EQUAL, FieldRelation.AND, "group_1");
		//userRoleQueryFilter.setClazz(UserRole.class);
		List<UserRole> userRoles = userRoleManager.queryNoPage(userRoleQueryFilter);
		if(BeanUtils.isNotEmpty(userRoles)){
    		this.exportExcel(baseFields, userRoles, "用户角色关系列表", "userRole", excelPath);
    		
    		List<String> roleIds = new ArrayList<String>();
    		//导出职务
    		for (UserRole userRole : userRoles) {
    			roleIds.add(userRole.getRoleId());
			}
    		QueryFilter roleQueryFilter = QueryFilter.build();
    		roleQueryFilter.addFilter("id", roleIds, QueryOP.IN,FieldRelation.AND);
    		roleQueryFilter.addFilter("isDelete", "1", QueryOP.NOT_EQUAL, FieldRelation.AND, "group_1");
    		//roleQueryFilter.setClazz(Role.class);
    		List<Role> roles = roleManager.queryNoPage(roleQueryFilter);
    		if(BeanUtils.isNotEmpty(roles)){
    			this.exportExcel(baseFields, roles, "角色列表", "role", excelPath);
    		}
    	}
    }
   
    
    private <E> void exportExcel(Field[] baseFields,List<E> list,String sheetName,String excelName,String excelPath) throws Exception{
    	Map<String, String> exportMaps = new LinkedHashMap<String, String>();
		Field[] fields = list.get(0).getClass().getDeclaredFields();
		for (Field field : fields) {
			 if(field.getModifiers()==4){//修饰符类型为protected
				 exportMaps.put(field.getName(), field.getName());
			 }
		}
		for (Field field : baseFields) {
			if(field.getModifiers()==4){//修饰符类型为protected
				exportMaps.put(field.getName(), field.getName());
			}
		}
		HSSFWorkbook book = ExcelUtil.exportExcel(sheetName, 24, exportMaps, list);
		ExcelUtil.saveExcel(book, excelName, excelPath);
    }
    
	@Override
	public List<User> getUserByTime(UserExportObject userExport)
			throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(userExport.getBtime(), userExport.getEtime());
		StringBuilder sql = new StringBuilder();
		//职务
		if(StringUtil.isNotEmpty(userExport.getJobCodes())){
			sql.append(" and ID_ in( select aa.USER_ID_ from uc_org_user aa where aa.is_dele_ = '0' and aa.pos_id_ in (select bb.ID_ from uc_org_post bb where bb.is_dele_='0' and bb.job_id_ in (select cc.id_ from uc_org_job cc where cc.is_dele_='0'  and cc.CODE_ in ");
			sql.append(OrgUtil.getSubInSql(userExport.getJobCodes(), ","));
			sql.append(" ))) ");
		}
		
		//岗位
		if(StringUtil.isNotEmpty(userExport.getPostCodes())){
			sql.append(" and ID_ in( select dd.USER_ID_ FROM uc_org_user dd where dd.is_dele_='0' and dd.pos_id_ in( select ee.ID_ from uc_org_post ee where ee.is_dele_='0' and ee.CODE_ in ");
			sql.append(OrgUtil.getSubInSql(userExport.getPostCodes(), ","));
			sql.append(" )) ");	
		}
		//组织
		else if(StringUtil.isNotEmpty(userExport.getOrgCodes())){
			sql.append(" and ID_ in( select ff.USER_ID_ FROM uc_org_user ff where ff.is_dele_='0' and ff.org_id_ in( select gg.ID_ from uc_org gg where gg.is_dele_='0' and gg.CODE_ in ");
			sql.append(OrgUtil.getSubInSql(userExport.getOrgCodes(), ","));
			sql.append(" )) ");	
		}
		//维度
		else if(StringUtil.isNotEmpty(userExport.getDemCodes())){
			sql.append(" and ID_ in( select hh.user_id_ FROM uc_org_user hh where hh.is_dele_='0' and hh.org_id_ in( select ii.ID_ from uc_org ii where ii.is_dele_='0' and ii.dem_id_ in(select jj.ID_ from uc_demension jj where jj.is_dele_='0' and jj.code_ in ");
			sql.append(OrgUtil.getSubInSql(userExport.getDemCodes(), ","));
			sql.append(" ))) ");	
		}
		if(BeanUtils.isNotEmpty(sql)){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dimSql", sql.toString());
			queryFilter.setParams(params);
		}
		return baseMapper.queryByDim(convert2Wrapper(queryFilter, currentModelClass()));
	}
	
	@Override
	public List<UserParams> getUserParamByTime(String btime, String etime)
			throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(btime, etime);
		return userParamsService.queryNoPage(queryFilter);
	}
	
	@Override
	public List<UserRole> getUserRoleByTime(String btime, String etime)
			throws Exception {
		QueryFilter queryFilter = OrgUtil.getDataByTimeFilter(btime, etime);
		return userRoleManager.queryNoPage(queryFilter);
	}
	
	@Override
	public List<User> getChargesByOrg(String orgCode, Boolean isMain)
			throws Exception {
		Org org = orgService.getByCode(orgCode);
		if(BeanUtils.isEmpty(org)){
			org = orgService.get(orgCode);
			if(BeanUtils.isEmpty(org)){
				throw new RuntimeException("组织编码或id【"+orgCode+"不存在】！");
			}
		}
		List<OrgUser> orgUser = orgUserService.getChargesByOrgId(org.getId(), isMain);
		if(BeanUtils.isNotEmpty(orgUser)){
			List<String> userIds = new ArrayList<String>();
			for (OrgUser oUser : orgUser) {
				userIds.add(oUser.getUserId());
			}
			QueryFilter filter = QueryFilter.build();
			//filter.setClazz(User.class);
			filter.addFilter("id", userIds, QueryOP.IN, FieldRelation.AND);
			return this.query(filter).getRows();
		}
		return null;
	}

	@Override
	public DataSyncVo getSyncDataByTime(DataSyncObject dataSync)
			throws Exception {
		DataSyncVo dataSyncVo = new DataSyncVo();
		String btime = dataSync.getBtime();
		String etime = dataSync.getEtime();
		//用户
		if(dataSync.getIsUser()){
			UserExportObject userExport = new UserExportObject();
			userExport.setBtime(btime);
			userExport.setEtime(etime);
			userExport.setDemCodes(dataSync.getDemCodes());
			userExport.setOrgCodes(dataSync.getOrgCodes());
			userExport.setJobCodes(dataSync.getJobCodes());
			userExport.setPostCodes(dataSync.getPostCodes());
			dataSyncVo.setUserList(this.getUserByTime(userExport));
		}
		OrgExportObject exportObject = new OrgExportObject();
		exportObject.setBtime(btime);
		exportObject.setEtime(etime);
		exportObject.setDemCodes(dataSync.getDemCodes());
		exportObject.setOrgCodes(dataSync.getOrgCodes());
		//维度
		if(dataSync.getIsDem()){
			dataSyncVo.setDemList(demensionService.getDemByTime(exportObject));
		}
		//组织
		if(dataSync.getIsOrg()){
			dataSyncVo.setOrgList(orgService.getOrgByTime(exportObject));
		}
		//分级组织
		if(dataSync.getIsOrgAuth()){
			dataSyncVo.setOrgAuthList(orgAuthService.getOrgAuthByTime(exportObject));
		}
		//职务
		if(dataSync.getIsJob()){
			dataSyncVo.setJobList(orgJobService.getJobByTime(btime, etime));
		}
		//岗位
		if(dataSync.getIsPost()){
			dataSyncVo.setPostList(orgService.getOrgPostByTime(exportObject));
		}
		//用户组织关系
		if(dataSync.getIsOrgUser()){
			dataSyncVo.setOrgUserList(orgService.getOrgUserByTime(exportObject));
		}
		//组织中的用户下属
		if(dataSync.getIsUnder()){
			dataSyncVo.setUnderList(orgService.getUserUnderByTime(exportObject));
		}
		//组织角色
		if(dataSync.getIsOrgRole()){
			dataSyncVo.setOrgRoleList(orgService.getOrgRoleByTime(exportObject));
		}
		//用户组织参数
		if(dataSync.getIsParams()){
			dataSyncVo.setParamsList(paramsService.getParamsByTime(btime, etime));
		}
		//用户参数
		if(dataSync.getIsUserParams()){
			dataSyncVo.setUserParamList(this.getUserParamByTime(btime, etime));
		}
		//组织参数
		if(dataSync.getIsOrgParams()){
			dataSyncVo.setOrgParamList(orgService.getOrgParamByTime(exportObject));
		}
		//角色
		if(dataSync.getIsRole()){
			dataSyncVo.setRoleList(roleService.getRoleByTime(btime, etime));
		}
		//用户角色关系
		if(dataSync.getIsUserRole()){
			dataSyncVo.setUserRoleList(this.getUserRoleByTime(btime, etime));
		}
		//群组
		if(dataSync.getIsGroup()){
			dataSyncVo.setGroupList(userGroupService.getUserGroupByTime(btime, etime));
		}
		//汇报线节点
		if(dataSync.getIsUserRel()){
			dataSyncVo.setUserRelList(userRelService.getUserRelByTime(btime, etime));
		}
		return dataSyncVo;
	}
	
	// 清理头像目录
	private void cleanPhotoFolder(String account, String exceptFile) throws IOException{
		Path path = Paths.get(attachPath, account);
		if(Files.notExists(path) || StringUtil.isEmpty(exceptFile)) return;
		UserPhotoFileFindVisitor userPhotoFileFindVisitor = new UserPhotoFileFindVisitor(exceptFile);
		Files.walkFileTree(path, userPhotoFileFindVisitor);
		for (String fileName : userPhotoFileFindVisitor.getFilenameList()) {
			Files.delete(Paths.get(fileName));
		}
	}

	@Override
    @Transactional
	public void saveUser(UserPolymer userPolymer) throws Exception {
		UserVo user = userPolymer.getUser();
		String account = user.getAccount();
		// 处理用户数据
		if(userPolymer.getAdding()){
			this.addUser(user);
		}
		else{
			this.updateUser(user);
		}
		cleanPhotoFolder(account, user.getPhoto());
		// 处理角色数据
		List<UserPolymerRole> roles = userPolymer.getRoles();
		int roleSize = BeanUtils.isEmpty(roles)?0:roles.size();
		String[] roleCodes = new String[roleSize];
		for(int i = 0; i < roleSize; i++){
			UserPolymerRole userPolymerRole = roles.get(i);
			roleCodes[i] = userPolymerRole.getCode();
		}
		userRoleService.saveUserRole(account, roleCodes);
		
		// 处理组织岗位数据 
		List<UserPolymerOrgPos> orgsPoses = userPolymer.getOrgsPoses();
		orgUserService.saveOrgUser(account, orgsPoses);
	}
	@Override
    @Transactional
	public CommonResult<String>  saveUser(UserVo user)  {
		try {
		String account = user.getAccount();
		if(StringUtil.isEmpty(user.getAccount())){
			throw new RequiredException("更新用户失败，用户帐号【account】必填！");
		}
		User u = this.getByAccount(user.getAccount());
		if(BeanUtils.isEmpty(u)){
			throw new RuntimeException("更新用户失败，根据【"+user.getAccount()+"】没有找到对应的用户信息！");
		}
		if(StringUtil.isNotEmpty(user.getBirthday())){
			u.setBirthday(DateFormatUtil.parse(user.getBirthday(), StringPool.DATE_FORMAT_DATE).toLocalDate());
		}else{
			u.setBirthday(null);
		}
        u.setAddress(user.getAddress());
        u.setEducation(user.getEducation());
        u.setSex(user.getSex());
        u.setPhoto(user.getPhoto());
        u.setIdCard(user.getIdCard());
		this.update(u);
	    cleanPhotoFolder(account, user.getPhoto());
		} catch (Exception e) {
			return new CommonResult<String>(false, "修改用户信息失败", e.getMessage());
	    }
		return new CommonResult<String>(true, "修改用户信息成功", "");
	}
	
	@Override
	public ChartOption getUserRelCharts(String json) throws Exception {
		User user = getUserByJson(json);
		if(BeanUtils.isEmpty(user)){
			throw new RuntimeException("根据用户标识信息获取不到用户！");
		}
		String currentUserName = user.getFullname();
		String text = "【" + currentUserName+"】的汇报关系图";
		String subtext = "数据来自华润三九用户中心";
		List<String> legend = new ArrayList<String>();
		legend.add("组织领导");
		legend.add("汇报线");
		String seriesName = "汇报关系";
		ArrayNode categories = (ArrayNode) JsonUtil.toJsonNode("[{name: '当前用户'},{name: '组织领导'},{name:'汇报线'}]");
		List<ChartNode> nodes = new ArrayList<ChartNode>();
		List<ChartLink> links = new ArrayList<ChartLink>();
		ChartNode root = new ChartNode("root",0,currentUserName+"\n"+"（当前用户）","当前人",currentUserName+"\n"+"（当前用户）",35);
		nodes.add(root);
		Map<String,ChartNode> nodeIdMap = new HashMap<String, ChartNode>();
		//汇报线节点
		ChartOption option = new ChartOption(text, subtext, legend, seriesName, categories);
		//获取汇报线汇报关系
		getUserRelOptions(root,option, nodes, links, user.getAccount(),nodeIdMap);
		//获取组织汇报关系
		getOrgOptions(root,option, nodes, links, user.getId(),nodeIdMap);
		option.setNodes(nodes);
		option.setLinks(links);
		return option;
	}
	
	/**
	 * 获取汇报线汇报关系
	 * @param option
	 * @param nodes
	 * @param links
	 * @param account
	 * @return
	 * @throws Exception 
	 */
	private void getUserRelOptions(ChartNode root,ChartOption option,List<ChartNode> nodes,List<ChartLink> links,String account,
			Map<String,ChartNode> nodeIdMap) throws Exception{
		List<UserRel> rels = userRelService.getUserRels(account, null);
		for (UserRel userRel : rels) {
			//如果父节点id为分类id，则跳过
			if(userRel.getParentId().equals(userRel.getTypeId())){
				continue;
			}
			List<ChartNode> relNodes = new ArrayList<ChartNode>();
			List<ChartLink> relLinks = new ArrayList<ChartLink>();
			UserRel pRel = userRelService.get(userRel.getParentId());
			if(BeanUtils.isNotEmpty(pRel)&&pRel.getStatus()==1){
				ObjectNode proType = portalFeignService.getSysTypeById(pRel.getTypeId());
				String typeName = BeanUtils.isEmpty(proType)?"未找到！":proType.get("name").asText();
				getSupRel(root,userRel, pRel, relNodes, relLinks,true,nodeIdMap,typeName);
				if(BeanUtils.isNotEmpty(relNodes)){
					nodes.addAll(relNodes);
				}
				if(BeanUtils.isNotEmpty(relLinks)){
					links.addAll(relLinks);
				}
			}
		}
	}
	
	/**
	 * 获取上级汇报节点
	 * @throws Exception 
	 */
	private void getSupRel(ChartNode root,UserRel nowRel,UserRel pRel,List<ChartNode> relNodes,List<ChartLink> relLinks,boolean isFirst,
			Map<String,ChartNode> nodeIdMap,String typeName) throws Exception{
		if(BeanUtils.isNotEmpty(pRel)){
			ChartNode node = nodeIdMap.get(pRel.getId());
			if(BeanUtils.isEmpty(node)){
				String label = getRelLabel(pRel);
				node = new ChartNode(pRel.getId(),2, label, "所属汇报线【"+typeName+"】", label,30);
				nodeIdMap.put(pRel.getId(), node);
				relNodes.add(node);
			}
			ChartLink link = new ChartLink("汇报线", isFirst?root.getId():nowRel.getId(), pRel.getId(), isFirst?5:3);
			relLinks.add(link);
			UserRel ppRel = userRelService.get(pRel.getParentId());
			//如果不是顶层节点，则往上找上级节点
			if(BeanUtils.isNotEmpty(ppRel)&&ppRel.getStatus()==1){
				getSupRel(root,pRel,ppRel,relNodes,relLinks,false,nodeIdMap,typeName);
			}
		}
	}
	
	/**
	 * 获取汇报线节点标签
	 * @param rel
	 * @return
	 * @throws Exception 
	 */
	private String getRelLabel(UserRel rel) throws Exception{
		StringBuilder sbd = new StringBuilder();
		List<User> users = userRelService.getUsersByRel(rel.getAlias());
		sbd.append(rel.getName());
		sbd.append("\n");
		sbd.append("（");
		switch(rel.getGroupType()) {
        	case UserRel.GROUP_USER:sbd.append("用户");break;
        	case UserRel.GROUP_ORG:sbd.append("组织");break;
        	case UserRel.GROUP_POS:sbd.append("岗位");break;
        	case UserRel.GROUP_ROLE:sbd.append("角色");break;
        	case UserRel.GROUP_GROUP:sbd.append("群组");break;
        }
		if(!UserRel.GROUP_USER.equals(rel.getGroupType())){
			sbd.append("【");
			sbd.append(BeanUtils.isNotEmpty(users)?this.getUserNames(users):"未找到用户");
			sbd.append("】");
		}
		sbd.append("）");
		return sbd.toString();
	}
	
	/**
	 * 获取组织汇报关系
	 * @param option
	 * @param nodes
	 * @param links
	 * @param account
	 * @return
	 * @throws Exception 
	 */
	private void getOrgOptions(ChartNode root,ChartOption option,List<ChartNode> nodes,List<ChartLink> links,String userId,
			Map<String,ChartNode> nodeIdMap) throws Exception{
		List<Org> orgs = orgService.getMainOrgListByUser(userId, null);
		for (Org org : orgs) {
			List<ChartNode> orgNodes = new ArrayList<ChartNode>();
			List<ChartLink> orgLinks = new ArrayList<ChartLink>();
			Org pOrg = orgService.get(org.getParentId());
			if(BeanUtils.isNotEmpty(pOrg)){
				Demension dem = demensionService.get(pOrg.getDemId());
				String demName= BeanUtils.isEmpty(dem)?"未找到！":dem.getDemName();
				getSupOrg(root,org, pOrg, orgNodes, orgLinks,true,nodeIdMap,demName);
				if(BeanUtils.isNotEmpty(orgNodes)){
					nodes.addAll(orgNodes);
				}
				if(BeanUtils.isNotEmpty(orgLinks)){
					links.addAll(orgLinks);
				}
			}
		}
	}
	
	/**
	 * 获取上级组织
	 * @throws Exception 
	 */
	private void getSupOrg(ChartNode root,Org nowOrg,Org pOrg,List<ChartNode> orgNodes,List<ChartLink> orgLinks,boolean isFirst,
			Map<String,ChartNode> nodeIdMap,String demName) throws Exception{
		if(BeanUtils.isNotEmpty(pOrg)){
			List<OrgPost> posts = orgPostService.getRelCharge(pOrg.getId(),true);
			ChartNode node = nodeIdMap.get(pOrg.getId());
			if(BeanUtils.isEmpty(node)){
				String label = getOrgLabel(pOrg,posts);
				node = new ChartNode(pOrg.getId(),1, label, "所属维度【"+demName+"】", label,30);
				nodeIdMap.put(pOrg.getId(), node);
				orgNodes.add(node);
			}
			ChartLink link = new ChartLink("组织领导", isFirst?root.getId():nowOrg.getId(), pOrg.getId(), isFirst?5:3);
			orgLinks.add(link);
			Org ppOrg = orgService.get(pOrg.getParentId());
			//如果不是顶层节点，则往上找上级节点
			if(BeanUtils.isNotEmpty(ppOrg)){
				getSupOrg(root,pOrg,ppOrg,orgNodes,orgLinks,false,nodeIdMap,demName);
			}
		}
	}
	
	/**
	 * 获取组织节点标签
	 * @param rel
	 * @return
	 * @throws Exception 
	 */
	private String getOrgLabel(Org org,List<OrgPost> posts) throws Exception{
		StringBuilder sbd = new StringBuilder();
		sbd.append(org.getName());
		sbd.append("\n");
		sbd.append("（");
		List<User> users = null;
		if(BeanUtils.isNotEmpty(posts)){
			OrgPost post = posts.get(0);
			users = getListByPostId(post.getId());
//			如果没有设置主岗位，则找组织负责人（包含负责人和主负责人）
//			if(BeanUtils.isEmpty(users)){
//				sbd.append("负责人");
//				users = getChargesByOrg(org.getCode(), false);
//			}else{
//				sbd.append(post.getName());
//			}
			sbd.append(post.getName());
			String names = getUserNames(users);
			sbd.append("【");
			sbd.append(StringUtil.isNotEmpty(names)?names:"未设置人员");
			sbd.append("】");
		}else{
			users = getChargesByOrg(org.getCode(), false);
			if(BeanUtils.isEmpty(users)){
				sbd.append("未设置责任岗位、负责人");
			}else{
				sbd.append("负责人");
				sbd.append("【");
				sbd.append(getUserNames(users));
				sbd.append("】");
			}
			
		}
		sbd.append("）");
		return sbd.toString();
	}
	
	private String getUserNames(List<User> users){
		StringBuilder sbd = new StringBuilder();
		if(BeanUtils.isNotEmpty(users)){
			boolean isFirst = true;
			for (User user : users) {
				if(isFirst){
					isFirst = false;
				}else{
					sbd.append("，");
				}
				sbd.append(user.getFullname());
			}
		}
		return sbd.toString();
	}
	
	@Override
	public IPage<User> getRoleUserQuery(QueryFilter filter) {
		PageBean pageBean = filter.getPageBean();
		copyQuerysInParams(filter);
		return baseMapper.getUserListByRoleCodeMap(convert2IPage(pageBean),convert2Wrapper(filter, currentModelClass()));
	}
	
	@Override
	public IPage<User> getOrgUserQuery(QueryFilter<User> queryFilter) {
		return baseMapper.getUserListByOrgQuery(convert2IPage(queryFilter.getPageBean()), convert2Wrapper(queryFilter, currentModelClass()));
	}

	@Override
	public boolean showADButton() {
		 Properties sysProperties = propertiesService.getPropertiesByCode("ldaptime");
		 boolean flag = false;
		 if(BeanUtils.isNotEmpty(sysProperties)){
			 if(StringUtil.isNotEmpty(sysProperties.getValue())){
				 flag = true;
			 }
		 }
		 return flag;
	}
	
	/**
	 * 获取组织中用户为负责人的组织
	 * @param orgs
	 * @param userRelObject
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private List<Org> getUserManagerOrgs(List<Org> orgs,String userId) throws Exception{
		List<Org> list = new ArrayList<Org>();
		for (Org org : orgs) {
			if(isUserManagerInOrg(userId, org.getCode())){
				list.add(org);
			}
		}
		return list;
	}
	
	/**
	 * 判断用户在该组织中是否负责人（1、组织中有责任岗位，则判断该用户是否在责任岗位中；2、组织中没有责任岗位，则判断该用户是否为（主）负责人）
	 * @param userId
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private boolean isUserManagerInOrg(String userId,String orgCode) throws Exception{
		boolean isIn = false;
		List<OrgPost> orgPosts = orgService.getPostsByOrgCodes(orgCode, true);
		if(BeanUtils.isNotEmpty(orgPosts)){
			QueryFilter queryFilter = QueryFilter.build();
			queryFilter.addFilter("orguser.user_id_", userId, QueryOP.EQUAL,FieldRelation.AND);
			queryFilter.addFilter("orguser.pos_id_", orgPosts.get(0).getId(), QueryOP.EQUAL,FieldRelation.AND);
			List orgUserList = this.queryOrgUserRel(queryFilter);
			if(BeanUtils.isNotEmpty(orgUserList)){
				return true;
			}else{
				Org org = orgService.getByCode(orgCode);
				if(BeanUtils.isNotEmpty(org)){
					List<OrgUser> orgUsers = orgUserService.getListByOrgIdUserId(org.getId(),userId);
					for (OrgUser orgUser : orgUsers) {
						if(orgUser.getIsCharge()!=0){
							return true;
						}
					}
				}
			}
		}
		return isIn;
	}
	

	@Override
    @Transactional
	public void setStatus(UserStatusVo userStatusVo) throws Exception {
		baseMapper.updateStatusByAccounts(userStatusVo.getStatus(), userStatusVo.getAccounts(),LocalDateTime.now());
	}

	@Override
	public CommonResult<Boolean> isAccountExist(String account) throws Exception {
		Integer amount = baseMapper.queryByAccount(account);
		return new CommonResult<Boolean>(true, "", amount > 0);
	}

	@Override
	public CommonResult<Boolean> isUserNumberExist(String account, String userNumber) throws Exception {
		Integer amount = baseMapper.queryByUserNumber(account, userNumber);
		return new CommonResult<Boolean>(true, "", amount > 0);
	}

	@Override
	public CommonResult<String> uploadPortrait(String account, MultipartFile file) throws Exception {
		String finalName;
		if (!file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String extName = getExtName(originalFilename);
			finalName = UniqueIdUtil.getSuid() + extName;
			Path path = Paths.get(attachPath, account, finalName);
			Path parent = path.getParent();
			if(Files.notExists(parent)){
				File dir = new File(parent.toString());
				dir.mkdirs();
			}
            Files.copy(file.getInputStream(), path);
            return new CommonResult<String>(true, "上传成功", account + "/" + finalName);
	    } else {
	        throw new SystemException("Upload file is empty.");
	    }
	}
	
	private String getExtName(String fileName){
		Pattern regex = Pattern.compile("^.*(\\.\\w+)$");
		Matcher regexMatcher = regex.matcher(fileName);
		if (regexMatcher.matches()) {
			return regexMatcher.group(1);
		}
		return null;
	}

	@Override
	public Resource downloadPortrait(String account, String filename) throws Exception {
		return resourceLoader.getResource("file:" + Paths.get(attachPath, account, filename).toString());
	}

	@Override
	public IPage<User> getDemUserQuery(QueryFilter filter) throws Exception {
		PageBean pageBean = filter.getPageBean();
		copyQuerysInParams(filter);
		return baseMapper.getDemUserQuery(convert2IPage(pageBean),convert2Wrapper(filter, currentModelClass()));
	}

	@Override
	public CommonResult<String> setTrigger(TriggerVo triggerVo,String ip)
			throws Exception {
		/*String values = triggerVo.getValues();
		String cron = OrgUtil.getScheduledCron(values);
		String logMsg = OrgUtil.getScheduledCronLog(values);
		String type = triggerVo.getType();
		if(ScheduledOaTask.SCHEDULED_OA.equals(type)){
			scheduledOaTask.setCron(cron);
			logMsg = "将OA同步定时计划修改为："+logMsg;
		}else if(ScheduledAdTask.SCHEDULED_AD.equals(type)){
			scheduledAdTask.setCron(cron);
			logMsg = "将AD同步定时计划修改为："+logMsg;
		}
		Properties properties = propertiesDao.getByCode(type);
		properties.setValue(triggerVo.getValues());
		propertiesDao.update(properties, properties.getVersion());
		String reqUrl = "/api/user/v1/user/setTrigger";
		OperateLog log = new OperateLog(1, "POST", reqUrl, logMsg,values, "", "");
		log.setReqIp(ip);
		log.setEndTime(LocalDateTime.now());
		OperateLogUtil.doLogAsync(log);*/
		return new CommonResult<String>(true, "任务执行计划设置成功！", "");
	}

	@Override
	public CommonResult<UserVo> getUserById(String userId) throws Exception {
		if(StringUtil.isEmpty(userId)){
			return new CommonResult<UserVo>(false, "获取用户失败，用户ID：“userId”不能为空！", null);
		}
		User user = this.get(userId);
		if(BeanUtils.isEmpty(user)){
			return new CommonResult<UserVo>(false, "获取用户失败，用户ID为【"+userId+"】的用户不存在！", null);
		}
		UserVo userVo = new UserVo(user);
		return new CommonResult<UserVo>(true, "设置成功！", userVo);
	}

	@Override
	public PageList<User> queryByType(QueryFilter queryFilter)
			throws SystemException {
		this.copyQuerysInParams(queryFilter);
    	handleQueryFilter(queryFilter);
    	PageBean pageBean = queryFilter.getPageBean();
    	if(BeanUtils.isEmpty(pageBean)){
    		pageBean = new PageBean(1, Integer.MAX_VALUE, false);
    	}
    	IPage<User> query = baseMapper.queryByType(convert2IPage(pageBean),convert2Wrapper(queryFilter, currentModelClass()));
        return new PageList<User>(query);
    }
	
	
	 /**
     * 获取当前泛型的类型
     * @return 类型
     */
    @SuppressWarnings("unchecked")
	private Class<? super T> getTypeClass(){
    	// 获取第二个泛型(T)对应的class
    	Class<? super T> rawType = (Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    	return rawType;
    }
    
    /**
	 * 检查通用查询对象中的实体类型
	 * <pre>
	 * 1.若实体类型为空，使用当前的泛型补充；
	 * 2.若实体类不为空，使用当前泛型类检查，不一致则抛出异常。
	 * </pre>
	 * @param queryFilter
	 * @throws SystemException
	 */
    private void handleQueryFilter(QueryFilter queryFilter) throws SystemException{
		if(BeanUtils.isEmpty(queryFilter)){
    		throw new SystemException("QueryFilter通用查询对象不能为空.");
    	}
    	//Class<?> clazz = queryFilter.getClazz();
//    	Class<? super T> typeClass = getTypeClass();
//    	if(BeanUtils.isEmpty(clazz)){
//    		// 所传入的通用查询器未指定 对应实体类时，从当前泛型中获取
//    		queryFilter.setClazz(typeClass);
//    	}
//    	else{
//    		if(!clazz.equals(typeClass)){
//    			throw new SystemException(String.format("QueryFilter中的实体类:%s与Dao泛型中的实体类:%s不一致.", clazz, typeClass));
//    		}
//    	}
	}

	@Override
	public List<UserVo> queryUser(QueryFilter queryFilter) {
		List<User> users = this.queryNoPage(queryFilter);
		List<UserVo> userVos = new ArrayList<UserVo>();
		for(User user : users){
			UserVo userVo = OrgUtil.convertToUserVo(user);
			userVos.add(userVo);
		}
		return userVos;
	}

	@Override
	public List<User> getSuperFromUnder(String userId, String orgId,
			String demId) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		if(StringUtil.isNotEmpty(orgId)){
			params.put("orgId", orgId);
		}if(StringUtil.isNotEmpty(demId)){
			params.put("demId", demId);
		}
		return baseMapper.getSuperFromUnder(params);
	}

	@Override
	public Map<String, Object> getUserDetailed(String userId) {
		return baseMapper.getUserDetailed(userId);
	}

	@Override
	public List<UserVo>  getDepHeader(String userId, Boolean isMain) throws Exception {
		Org org = getOrgByUserId(userId);
		if (BeanUtils.isNotEmpty(org)) {
			return getDepHeaderByOrgId(org.getId(),isMain);
		}
		return new ArrayList<>();
		
	}
	
	private Org getOrgByUserId(String userId){
		User u = this.get(userId);
		if(BeanUtils.isNotEmpty(u)){
			return orgService.getMainGroup(userId, "");
		}
		return new Org();
	}
	
	private ObjectNode getUserInfoByUserId(String userId) throws Exception{
		ObjectNode objectNode = JsonUtil.getMapper().createObjectNode();
		User u = this.get(userId);
		if(BeanUtils.isNotEmpty(u)){
			objectNode.set("user", JsonUtil.toJsonNode(u));
			Org org = orgService.getMainGroup(userId, "");
			if(BeanUtils.isNotEmpty(org)) {
				objectNode.set("org", JsonUtil.toJsonNode(org));
			}
		}
		return objectNode;
	}
	
	@Override
	public List<UserVo>  getDepHeaderByOrg(String orgId, Boolean isMain) throws Exception {
		return getDepHeaderByOrgId(orgId,isMain);
	}
	
	/**
	 * 根据组织id获取组织负责人。并将其转化成用户对象
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	private List<UserVo> getDepHeaderByOrgId(String orgId, Boolean isMain) throws Exception {
		if(BeanUtils.isNotEmpty(orgId)){
			List<OrgUser> chargesByOrgId = orgUserService.getChargesByOrgId(orgId, isMain);
			List<String>  userIds = new ArrayList<>();
			for (OrgUser orgUser : chargesByOrgId) {
				userIds.add(orgUser.getUserId());
			}
			if (userIds.size() > 0) {
				QueryFilter queryFilter = QueryFilter.build();
				queryFilter.addFilter("ID_", userIds, QueryOP.IN);
				PageList<User> query = this.query(queryFilter);
				List<UserVo> voList = new ArrayList<>();
				for (User user : query.getRows()) {
					if (BeanUtils.isEmpty(user)) {
						continue;
					}
					voList.add(new UserVo(user));
				}
				return voList;
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Group> getGroupsByUserId(String userId,String type ) {
		List<IGroup> listMap = new ArrayList<IGroup>();
		User user = this.get(userId);
		if (BeanUtils.isEmpty(user)) {
			user = this.getByAccount(userId);
		}
		if (BeanUtils.isEmpty(user)) {
			throw new NotFoundException("根据所传用户id或者账号未找到用户");
		}
		userId = user.getId();
		if (GroupTypeConstant.All.key().equals(type) || GroupTypeConstant.ORG.key().equals(type)) {
			List<IGroup> listOrg = (List) orgService.getOrgListByUserId(userId);
			if (BeanUtils.isNotEmpty(listOrg)) {
				listMap.addAll(listOrg);
			}
		}
		if (GroupTypeConstant.All.key().equals(type) || GroupTypeConstant.ROLE.key().equals(type)) {
			List<IGroup> listRole = (List) roleService.getListByUserId(userId);
			if (BeanUtils.isNotEmpty(listRole)) {
				listMap.addAll(listRole);
			}
		}
		
		if (GroupTypeConstant.All.key().equals(type) || GroupTypeConstant.JOB.key().equals(type)) {
			List<IGroup> listOrgRel = (List) orgJobService.getListByUserId(userId);
			if (BeanUtils.isNotEmpty(listOrgRel)) {
				listMap.addAll(listOrgRel);
			}
		}
		
		if (GroupTypeConstant.All.key().equals(type) || GroupTypeConstant.POSITION.key().equals(type)) {
			List<IGroup> listOrgRelDef = (List)orgPostService.getListByUserId(userId,"");
			if (BeanUtils.isNotEmpty(listOrgRelDef)) {
				listMap.addAll(listOrgRelDef);
			}
		}
		
		List<Group> groupList = new ArrayList<Group>();
		for (IGroup igroup : listMap) {
			Group group = new Group();
			group.setGroupId(igroup.getGroupId());
			group.setGroupCode(igroup.getGroupCode());
			group.setName(igroup.getName());
			group.setGroupType(igroup.getGroupType());
			group.setIdentityType(igroup.getIdentityType());
			groupList.add(group);
		}
		return groupList;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getUserDetailByAccountOrId(String userId) {
		Map<String, Object> resultMap = new HashMap<>();
	
		User user = this.get(userId);
		if (BeanUtils.isEmpty(user)) {
			user =this.getByAccount(userId);
		}
		if (BeanUtils.isEmpty(user)) {
			throw new NotFoundException("根据所传账号或者ID未找到用户");
		}
		
		resultMap.put("user", user);
		List<Map<String, Object>> orgPostList = orgPostService.getUserByUserId(user.getId());
		if (BeanUtils.isEmpty(orgPostList)) {
			return resultMap;
		}
		Map<String, Object> postMap = new HashMap<>();
		Map<String, Object> orgMap = new HashMap<>();
		for (int i=0;i<orgPostList.size();i++) {
			if (i==orgPostList.size()-1) {
				resultMap.put("role", orgPostList.get(i).get("roleName"));
			}else{
				Map<String, Object> map = orgPostList.get(i);
				if (BeanUtils.isNotEmpty(map.get("pathName")) && BeanUtils.isNotEmpty(map.get("demName"))) {
					map.put("pathName", map.get("demName").toString()+map.get("pathName").toString());
				}
				//如果有岗位id，则将其放入岗位map中。
				if (BeanUtils.isNotEmpty(map.get("postId"))) {
					postMap.put(map.get("postId").toString(), map);
				}
				if (BeanUtils.isNotEmpty(map.get("orgId"))) {
				    if(BeanUtils.isNotEmpty(orgMap)){
                        for(Object org :orgMap.values()){
                            Map<String, Object> obj = (Map<String, Object>)org;
                            if(!"1".equals(obj.get("isMaster").toString()) && "1".equals(map.get("isMaster").toString())){
                                orgMap.clear();
                                orgMap.put(map.get("orgId").toString(), map);
                            }
                        }
                        Demension defaultDemension = demensionService.getDefaultDemension();
                        if(BeanUtils.isNotEmpty(defaultDemension)){
                            if(map.get("demName").toString().equals(defaultDemension.getDemName()) && "1".equals(map.get("isMaster").toString())){
                                orgMap.clear();
                                orgMap.put(map.get("orgId").toString(), map);
                            }
                        }
                    }else {
                        orgMap.put(map.get("orgId").toString(), map);
                    }
				}
			}
		}
		resultMap.put("post", postMap);
		resultMap.put("org", orgMap);
		return resultMap;
	}
	
	@Override
	public List<User> getUserByName(String query) {
		return baseMapper.getUserByName(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object>  calculateNodeUser( Map<String, Object> result) {
			
		for (Iterator<Entry<String, Object>> iterator = result.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> entry =  iterator.next();
			List<IUser> recievers = new  ArrayList<IUser>();
			List<ObjectNode> bpmIdentityList =new ArrayList<>();
			try {
				recievers = extractUser((List<Map<String, Object>>) entry.getValue());
			} catch (Exception e) {}

			for (IUser user : recievers) {
				ObjectNode bpmIdentity = JsonUtil.getMapper().createObjectNode();
				bpmIdentity.put("type", "user");
				bpmIdentity.put("id", user.getUserId());
				bpmIdentity.put("name", user.getFullname());
				bpmIdentityList.add(bpmIdentity);
			} 
			result.put(entry.getKey(), bpmIdentityList);
		}
		return result;
	}

	private List<IUser> extractUser(List<Map<String, Object>> bpmIdentities) throws Exception {
		List<IUser> results = new ArrayList<IUser>();
		String userIds ="";
		String accounts ="";
		String orbyaccounts="";
		if (BeanUtils.isEmpty(bpmIdentities)) {
			return results;
		}
		for(Map<String, Object> bpmIdentity:bpmIdentities){			
			if(bpmIdentity.get("type").toString().equals("group")){				
				List<User> users = getUserListByGroup(bpmIdentity);
				results.addAll(users);
			}else if("user".equals(bpmIdentity.get("type").toString()) || bpmIdentity.get("type").toString().equals("groupUser") ){
				if (BeanUtils.isNotEmpty(bpmIdentity.get("id"))) {
					userIds+=bpmIdentity.get("id").toString()+",";
				}else {
					accounts+=bpmIdentity.get("code").toString()+",";
					orbyaccounts+="'"+bpmIdentity.get("code").toString()+"'"+",";
				}
			}
		}
		if (StringUtil.isNotEmpty(userIds)) {
			QueryFilter queryFilter = QueryFilter.build();
			queryFilter.addFilter("id_", userIds, QueryOP.IN);
			PageList<User> users = this.query(queryFilter);
			results.addAll(users.getRows());
		}
		if (StringUtil.isNotEmpty(accounts)) {
			StringBuffer orbyaccount=new StringBuffer(orbyaccounts);
			orbyaccount.deleteCharAt(orbyaccount.length()-1);
			QueryFilter queryFilter = QueryFilter.build();
			queryFilter.addFilter("account_", accounts, QueryOP.IN);
			queryFilter.addParams("orderBySql", "FIELD(account_,"+orbyaccount.toString()+")");
			PageList<User> users = this.query(queryFilter);
			results.addAll(users.getRows());
		}
		return results;
	}
	
	private List<User> getUserListByGroup(Map<String, Object> bpmIdentityMap) throws Exception  {
		// 此处可以根据不同的groupType去调用真实的实现：如角色下的人，组织下的人
		List<User> result= new ArrayList<User>();
		if(BeanUtils.isEmpty(bpmIdentityMap)) return result;
		String groupType = MapUtil.getString(bpmIdentityMap, "groupType");
		if(StringUtil.isEmpty(groupType)) return result;
		String groupId = MapUtil.getString(bpmIdentityMap, "id");
		String groupCode = MapUtil.getString(bpmIdentityMap, "code");
		
		if (groupType.equals(GroupTypeConstant.ORG.key())) {
			QueryFilter queryFilter=QueryFilter.build();
			queryFilter.setPageBean(new PageBean(1, Integer.MAX_VALUE));
			if(StringUtil.isNotEmpty(groupId)) {
				queryFilter.addFilter("org.id_", groupId, QueryOP.EQUAL);
			}
			else if(StringUtil.isNotEmpty(groupCode)) {
				queryFilter.addFilter("org.code_", groupCode, QueryOP.EQUAL);
			}
			else {
				return result;
			}
			IPage<User> demUserQuery = this.getDemUserQuery(queryFilter);
			result = demUserQuery.getRecords();
		}
		if (groupType.equals(GroupTypeConstant.ROLE.key())) {
			if(StringUtil.isNotEmpty(groupId)) {
				result = getUsersByRoleId(groupId);
			}
			else if(StringUtil.isNotEmpty(groupCode)) {
				result = getUsersByRoleCode(groupCode);
			}
			else {
				return result;
			}
		}
		if (groupType.equals(GroupTypeConstant.POSITION.key())) {
			if(StringUtil.isNotEmpty(groupId)) {
				result = getUserByPostId(groupId);
			}
			else if(StringUtil.isNotEmpty(groupCode)) {
				result = getUserByPostCode(groupCode);
			}
			else {
				return result;
			}
		}
		if (groupType.equals(GroupTypeConstant.JOB.key())) {
			if(StringUtil.isNotEmpty(groupId)) {
				result = getUsersByJobId(groupId);
			}
			else if(StringUtil.isNotEmpty(groupCode)) {
				result = getUsersByJobCode(groupCode);
			}
			else {
				return result;
			}
		}
		return result;
	}
	
	private List<User> getUsersByJobId(String ids){
		List<User> list = new ArrayList<User>();
		String[] idArray = ids.split(",");
		for(String id : idArray){
			OrgJob job = orgJobManager.get(id);
			if(BeanUtils.isNotEmpty(job)){
				List<User> users = this.getListByJobId(job.getId());
				if(BeanUtils.isNotEmpty(users)){
					list.addAll(users);
				}
			}
		}
		OrgUtil.removeDuplicate(list);
		return list;
	}
	
	public List<User> getUsersByJobCode(String codes) throws Exception {
		List<User> list = new ArrayList<User>();
		String[] codeArray = codes.split(",");
		for(String code : codeArray){
			OrgJob job = orgJobManager.getByCode(code);
			if(BeanUtils.isNotEmpty(job)){
				List<User> users = this.getListByJobId(job.getId());
				if(BeanUtils.isNotEmpty(users)){
					list.addAll(users);
				}
			}
		}
		OrgUtil.removeDuplicate(list);
		return list;
	}
	
	
	private List<User> getUserByPostCode(String postCode) {
		if(StringUtil.isEmpty(postCode)){
			throw new RequiredException(HotentHttpStatus.REUIRED.description()+"：postCode岗位编码必填！");
		}
		OrgPost post = orgPostService.getByCode(postCode);
		if(BeanUtils.isEmpty(post)){
			throw new RequiredException("岗位编码【"+postCode+"】不存在！");
		}
		return baseMapper.getUserByPost(postCode);
	}
	
	private List<User> getUserByPostId(String postId) {
		Assert.notNull(postId, "岗位ID不能为空");
		OrgPost orgPost = orgPostService.get(postId);
		if(BeanUtils.isEmpty(orgPost)){
			throw new RequiredException("不存在岗位ID为【"+postId+"】的数据");
		}
		return baseMapper.getUserByPost(orgPost.getCode());
	}
	
	private List<User> getUsersByRoleId(String ids) throws Exception {
		if(StringUtil.isEmpty(ids)){
			throw new RequiredException("角色ID不能为空！");
		}
		List<User> list = new ArrayList<User>();
		String[] idArray = ids.split(",");
		StringBuilder msg = new StringBuilder();
		boolean isTrue = false;
		boolean isFirst = true;
		for (String id : idArray) {
			List<User> users = this.getUserListByRoleId(id);
			if(BeanUtils.isNotEmpty(users)){
				list.addAll(users);
				isTrue = true;
			}else{
				if(!isFirst){
					msg.append(",");
				}else{
					isFirst = false;
				}
				msg.append(id);
			}
		}
		if(!isTrue){
			throw new RequiredException("根据角色ID【"+msg+"】找不到对应的角色信息！");
		}
		OrgUtil.removeDuplicate(list);
		return list;
	}
	
	private List<User> getUsersByRoleCode(String codes) throws Exception {
		if(StringUtil.isEmpty(codes)){
			throw new RequiredException("角色编码不能为空！");
		}
		List<User> list = new ArrayList<User>();
		String[] codeArray = codes.split(",");
		StringBuilder msg = new StringBuilder();
		boolean isTrue = false;
		boolean isFirst = true;
		for (String code : codeArray) {
			List<User> users = this.getUserListByRoleCode(code);
			if(BeanUtils.isNotEmpty(users)){
				list.addAll(users);
				isTrue = true;
			}else{
				if(!isFirst){
					msg.append(",");
				}else{
					isFirst = false;
				}
				msg.append(code);
			}
		}
		if(!isTrue){
			throw new RequiredException("根据角色编码【"+msg+"】找不到对应的角色信息！");
		}
		OrgUtil.removeDuplicate(list);
		return list;
	}
	
	@Override
	public CommonResult<UserVo> getUserByOpenId(String openId) {
		try {
			if(StringUtil.isEmpty(openId)){
				return new CommonResult<UserVo>(false, "获取用户失败，用户：“openId”不能为空！", null);
			}
			
			User user = baseMapper.getUserByOpenId(openId);
			if(BeanUtils.isEmpty(user)){
				return new CommonResult<UserVo>(false, "获取用户失败，用户微信为【"+openId+"】的用户不存在！", null);
			}
			UserVo userVo = new UserVo(user);
			return new CommonResult<UserVo>(true, "设置成功！", userVo);
		} catch (Exception e) {
			return new CommonResult<UserVo>(true, "获取用户失败，"+e.getMessage()+"", null);
		}
	}

	@Override
	public Map<String, Map<String, String>> getUserRightMapByIds(Set<String> ids) {
		Map<String, Map<String, Set<String>>> resultMap =new HashMap<>();
		QueryFilter orgPostFiler = QueryFilter.build();
		orgPostFiler.addFilter("user_id_", new ArrayList<>(ids), QueryOP.IN);
		PageList<OrgUser> orgPostQuery = orgUserService.query(orgPostFiler);
		for (String userId : ids) {
			Map<String, Set<String>> userMap =new HashMap<>();
			userMap.put("user", new HashSet<>(Arrays.asList(new String[]{userId})));
			resultMap.put(userId, userMap);
		} 
		Set<String> allPostIds = new HashSet<>();
		if (BeanUtils.isNotEmpty(orgPostQuery)) {
			for (OrgUser orgUser : orgPostQuery.getRows()) {
				Map<String, Set<String>> userMap =resultMap.get(orgUser.getUserId());
			    Set<String> orgSet =userMap.containsKey(GroupTypeConstant.ORG.key())?userMap.get(GroupTypeConstant.ORG.key()):new HashSet<>();
			    orgSet.add(orgUser.getOrgId());
			    userMap.put(GroupTypeConstant.ORG.key(), orgSet);
			    if (StringUtil.isNotEmpty(orgUser.getRelId())) {
			    	Set<String> postSet =userMap.containsKey(GroupTypeConstant.POSITION.key())?userMap.get(GroupTypeConstant.POSITION.key()):new HashSet<>();
			    	postSet.add(orgUser.getRelId());
			    	userMap.put(GroupTypeConstant.POSITION.key(), postSet);
			    	allPostIds.add(orgUser.getRelId());
				}
			    resultMap.put(orgUser.getUserId(), userMap);
			}
		}
		Map<String, String> postJobMap =new HashMap<>();
		if (!allPostIds.isEmpty()) {
			QueryFilter postFilter = QueryFilter.build();
			postFilter.addFilter("ID_", new ArrayList<>(allPostIds), QueryOP.IN);
			PageList<OrgPost> postQuery = orgPostService.query(postFilter);
			if (BeanUtils.isNotEmpty(postQuery)) {
				for (OrgPost post : postQuery.getRows()) {
					postJobMap.put(post.getId(), post.getRelDefId());
				}
			}
		}
		QueryFilter roleFiler = QueryFilter.build();
		roleFiler.addFilter("user_id_", new ArrayList<>(ids), QueryOP.IN);
		PageList<UserRole> roleQuery = userRoleService.query(roleFiler);
		if (BeanUtils.isNotEmpty(roleQuery)) {
			for (UserRole userRole : roleQuery.getRows()) {
				Map<String, Set<String>> userMap =resultMap.get(userRole.getUserId());
			    Set<String> roleSet =userMap.containsKey(GroupTypeConstant.ROLE.key())?userMap.get(GroupTypeConstant.ROLE.key()):new HashSet<>();
			    roleSet.add(userRole.getRoleId());
			    userMap.put(GroupTypeConstant.ROLE.key(), roleSet);
			    resultMap.put(userRole.getUserId(), userMap);
			}
		}
		
		Map<String, Map<String, String>> userRightMap=new HashMap<>();
		for (Iterator<Entry<String, Map<String, Set<String>>>> iterator = resultMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Map<String, Set<String>>> next = iterator.next();
			Map<String, Set<String>> value = next.getValue();
			Map<String, String> rightMapStr =new HashMap<>();
			if (value.containsKey(GroupTypeConstant.POSITION.key())) {
				Set<String> jobSet =new HashSet<>();
				for (String postId : value.get(GroupTypeConstant.POSITION.key())) {
					jobSet.add(postJobMap.get(postId));
				}
				value.put(GroupTypeConstant.JOB.key(), jobSet);
			}
			for (Iterator<Entry<String, Set<String>>> iterator2 = value.entrySet().iterator(); iterator2.hasNext();) {
				Entry<String, Set<String>> item =  iterator2.next();
				rightMapStr.put(item.getKey(), StringUtil.convertListToSingleQuotesString(item.getValue()));
			}
			userRightMap.put(next.getKey(), rightMapStr);
		}
		
		return userRightMap;
	}

	// 检测邮箱格式是否正确
	private boolean checkEmail(String email){
		try {
			String check = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			return matcher.matches();
		} catch (Exception e) {}
		return false;
	}

	@Override
	public CommonResult<UserVo> getUserByMobile(String mobile) {
		User user = baseMapper.getByMobile(mobile);
		if(BeanUtils.isEmpty(user)){
			return new CommonResult<UserVo>(false, "获取用户失败，手机号为【"+mobile+"】的用户不存在！", null);
		}
		UserVo userVo = new UserVo(user);
		return new CommonResult<UserVo>(true, "获取成功！", userVo);
	}
	
	@Override
	public ArrayNode getUserInfoBySignData(ArrayNode customSignDatas) throws Exception {
		for (JsonNode jsonNode : customSignDatas) {
			ObjectNode objectNode = (ObjectNode) jsonNode;
			String executor = objectNode.get("executor").asText();
			JsonNode executorJson = JsonUtil.toJsonNode(executor);
			executorJson = executorJson.get(0);
			ObjectNode resultObj = this.getUserInfoByUserId(executorJson.get("id").asText());
			ObjectNode executorObject = (ObjectNode) executorJson;
			executorObject.put("orgName", resultObj.has("org")? resultObj.get("org").get("name").asText():"");
			executorObject.put("account", resultObj.has("user")? resultObj.get("user").get("account").asText():"");
			objectNode.set("executor", executorObject);
		}
		return customSignDatas;
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}

	@Override
	public IPage<User> getGroupUsersPage(UserGroup userGroup, QueryFilter queryFilter) {
		String[] userIds = {""};
		String[] orgIds = {""};
		String[] roleIds = {""};
		String[] posIds = {""};
		boolean isEmptyGroup=true;
		copyQuerysInParams(queryFilter);
		Map<String, Object> params = queryFilter.getParams();
		if(BeanUtils.isNotEmpty(userGroup)){
			if(StringUtil.isNotEmpty(userGroup.getUserId())){
				userIds = userGroup.getUserId().split(",");
				params.put("userIds", userIds);
				isEmptyGroup=false;
			}
			if(StringUtil.isNotEmpty(userGroup.getOrgId())){
				orgIds = userGroup.getOrgId().split(",");
				params.put("orgIds", orgIds);
				isEmptyGroup=false;
			}
			if(StringUtil.isNotEmpty(userGroup.getRoleId())){
				roleIds = userGroup.getRoleId().split(",");
				params.put("roleIds", roleIds);
				isEmptyGroup=false;
			}
			if(StringUtil.isNotEmpty(userGroup.getPosId())){
				posIds = userGroup.getPosId().split(",");
				params.put("posIds", posIds);
				isEmptyGroup=false;
			}
		}
		if(BeanUtils.isEmpty(params) || isEmptyGroup){
			return null;
		}
		PageBean pageBean = queryFilter.getPageBean();
		IPage<User> groupUsersPage = baseMapper.getGroupUsersPage(convert2IPage(pageBean), params);
		return groupUsersPage;
	}

	@Override
	public CommonResult<String> getTokenByUserName(String userName) {
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		final String token = jwtTokenHandler.generateToken(userDetails);
		return new CommonResult<>(true, "获取Token成功！", token);
	}
}
