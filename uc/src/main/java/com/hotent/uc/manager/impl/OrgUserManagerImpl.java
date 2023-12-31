package com.hotent.uc.manager.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.uc.dao.OrgUserDao;
import com.hotent.uc.exception.HotentHttpStatus;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.OrgUserManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.model.User;
import com.hotent.uc.params.user.UserPolymerOrgPos;


/**
 * 
 * <pre> 
 * 描述：用户组织关系 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:27:31
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class OrgUserManagerImpl extends BaseManagerImpl <OrgUserDao, OrgUser> implements OrgUserManager{
	protected Logger logger = LoggerFactory.getLogger(OrgUserManagerImpl.class);
	@Autowired
	DemensionManager demensionManager;
	@Autowired
	OrgManager orgService;

    @Transactional
	public int updateUserPost(String id, String relId) {
	  return baseMapper.updateUserPost(id, relId,LocalDateTime.now());
	}
	public OrgUser getOrgUser(String orgId, String userId, String relId) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("orgId", orgId);
	
		params.put("userId", userId);
		if(StringUtil.isEmpty(relId)){
		   params.put("relIdNull", "");
		}
		else {
		   params.put("relId", relId);
		}
		if(BeanUtils.isEmpty(baseMapper.getByParms(params))){
			return null;
		}
		return baseMapper.getByParms(params).get(0);
	}
	public List<OrgUser> getListByOrgIdUserId(String orgId, String userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
		map.put("userId", userId);
		return baseMapper.getByParms(map);
	}

    @Transactional
	public  int removeByOrgIdUserId(String orgId,String userId){
		return baseMapper.removeByOrgIdUserId(orgId, userId,LocalDateTime.now());
	}

    @Transactional
	public void setMaster(String... id) throws SQLException{
		for(String i:id) {
			OrgUser orgUser = this.get(i);
			if (orgUser.getIsMaster() == 0) {
				Org org = orgService.get(orgUser.getOrgId());
				List<OrgUser> orgUsers = baseMapper.getOrgUserMaster(orgUser.getUserId(), null);
				if (BeanUtils.isNotEmpty(orgUsers)) {
					//orgUserDao.cancelUserMasterOrgByUserId(orgUser.getUserId(),LocalDateTime.now());
					baseMapper.cancelUserMasterOrg(orgUser.getUserId(), org.getDemId(), LocalDateTime.now());
				}
				baseMapper.updateUserMasterOrg(i, LocalDateTime.now());
			} else {
				orgUser.setIsMaster(0);
				this.update(orgUser);
			}
		}
		
	}
	
	
	public List<OrgUser> getOrgUserMaster(String userId,String demId) {
		//demId传维度id或编码都可以
		if(StringUtil.isNotEmpty(demId)){
			Demension sysDemension = demensionManager.get(demId);
			if(BeanUtils.isEmpty(sysDemension)){
				sysDemension = demensionManager.getByCode(demId);
				if(BeanUtils.isNotEmpty(sysDemension)){
					demId = sysDemension.getId();
				}
			}
		}else{
			Demension sysDemension = demensionManager.getDefaultDemension();
			if(BeanUtils.isNotEmpty(sysDemension)){
				demId = sysDemension.getId();
			}
		}
		return baseMapper.getOrgUserMaster(userId,demId);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public IPage<HashMap<String,Object>> getUserByGroup(QueryFilter queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
		IPage<OrgUser> convert2iPage = convert2IPage(pageBean);
		// 设置分页
		//PageHelper.startPage(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
		if(queryFilter.getParams().containsKey("orgId")){
			queryFilter.addFilter("org.ID_", queryFilter.getParams().get("orgId"), QueryOP.EQUAL);
		}
		queryFilter.addFilter("u.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		queryFilter.addFilter("orguser.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		queryFilter.addFilter("org.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		return baseMapper.getUserByGroup(convert2iPage,convert2Wrapper(queryFilter, currentModelClass()));
	}
	
	/**
	 * 0  不是部门负责人  1 部门负责人  2 部门主负责人
	 * @throws SQLException 
	 */
	@Override
    @Transactional
	public void setCharge(String userId,Boolean isCharge, String orgId) throws SQLException {
		List<OrgUser> orgUserList=this.getListByOrgIdUserId(orgId,userId);
		//判断是否为设置为主负责人，若是则先取消之前的主负责人
		if(BeanUtils.isNotEmpty(orgUserList)){
			if(orgUserList.get(0).getIsCharge() !=2  && isCharge){
				baseMapper.cancleMainCharge(orgId,LocalDateTime.now());
			}
		}
		for(int i=0;i<orgUserList.size();i++){
			
			if(orgUserList.get(i).getIsCharge()==2){
				orgUserList.get(i).setIsCharge(1);
			}else if(orgUserList.get(i).getIsCharge()==1){
				orgUserList.get(i).setIsCharge(0);
			}else if(orgUserList.get(i).getIsCharge()==0){
				orgUserList.get(i).setIsCharge(1);
			}
			
			// 设置为主负责人
			if(orgUserList.get(i).getIsCharge() !=2  && isCharge){
				orgUserList.get(i).setIsCharge(2);
			}
			this.update(orgUserList.get(i));
		}
	}
	@Override
	public List<OrgUser> getChargesByOrgId(String orgId, Boolean isMain) {
		Integer isCharge = null;
		if(BeanUtils.isNotEmpty(isMain)&&isMain){
			isCharge = 2;
		}
		return baseMapper.getChargesByOrgId(orgId, isCharge);
	}
	
	@Override
	public void syncValidOrgUser() {
		List<OrgUser> orgUsers = this.getAll();
		for (OrgUser orgUser : orgUsers) {
			try {
				validRelIsActive(orgUser);
			} catch (Exception e) {
				String msg = "校验设置岗位有效性失败，用户ID:"+orgUser.getUserId()+"，岗位ID："+orgUser.getRelId()+"，错误信息："+e.getMessage();
				logger.error(msg);
			}
		}
	}
	
	/**
	 * 判断当前人员岗位是否有效
	 * @param orgUser
	 * @return
	 * @throws ParseException 
	 */
    @Transactional
	private void validRelIsActive(OrgUser orgUser) throws ParseException{
		int isRelActive = 1;
		LocalDateTime startDate = orgUser.getStartDate();
		LocalDateTime endDate = orgUser.getEndDate();
		String currentDateStr = DateUtil.getCurrentTime(StringPool.DATE_FORMAT_DATE);
		LocalDateTime currentDate = DateFormatUtil.parse(currentDateStr, StringPool.DATE_FORMAT_DATE);
		if(BeanUtils.isNotEmpty(startDate)&&BeanUtils.isNotEmpty(endDate)){
			if(!DateUtil.belongCalendar(currentDate, startDate, endDate)){
				isRelActive = 0;
			}
		}else if(BeanUtils.isNotEmpty(startDate)){
			long time = DateUtil.getTime(startDate,currentDate);
			if(time<0){
				isRelActive = 0;
			}
		}else if(BeanUtils.isNotEmpty(endDate)){
			long time = DateUtil.getTime(currentDate, endDate);
			if(time<0){
				isRelActive = 0;
			}
		}
		orgUser.setIsRelActive(isRelActive);
		this.update(orgUser);
	}


	@Override
    @Transactional
	public void delByOrgId(String orgId) {
		baseMapper.delByOrgId(orgId,LocalDateTime.now());
	}


	@Override
	public List<OrgUser> getByParms(Map<String, Object> params) {
		return baseMapper.getByParms(params);
	}


	@Override
    @Transactional
	public void saveOrgUser(String account, List<UserPolymerOrgPos> orgsPoses) throws Exception {
		List<OrgUser> orgUsers = baseMapper.getByAccount(account);
		
		Map<String, OrgUser> orgMap = new HashMap<String, OrgUser>();
		Map<String, OrgUser> posMap = new HashMap<String, OrgUser>();
		
		//需要被删除的记录
		List<String> orgUserIds = new ArrayList<String>();
		for (OrgUser orgUser : orgUsers) {
			orgUserIds.add(orgUser.getId());
		}
		//遍历用户所有的组织岗位信息
		for (OrgUser orgUser : orgUsers) {
			String orgCode = orgUser.getOrgCode();
			String posCode = orgUser.getPosCode();
			//遍历前端传入的用户组织岗位list
			for (UserPolymerOrgPos userPolymerOrgPos : orgsPoses) {
				//如果只是组织，没有岗位信息。组织号和前端传入的一致，
				if(UserPolymerOrgPos.TYPE_ORG.equals(userPolymerOrgPos.getType()) 
						&& StringUtil.isEmpty(posCode) 
						&& orgCode.equals(userPolymerOrgPos.getOrgCode())){
					//标注该用户该组织信息，数据库已有
					orgMap.put(orgCode, orgUser);
					//则从待删除数组中移除
					orgUserIds.remove(orgUser.getId());
				}
				//如果是岗位，岗位编码和前端传入的一致，
				if(UserPolymerOrgPos.TYPE_POS.equals(userPolymerOrgPos.getType()) 
						&& StringUtil.isNotEmpty(posCode)
						&& posCode.equals(userPolymerOrgPos.getPosCode())){
					//标注该用户该岗位信息，数据库已有.更新岗位信息
					posMap.put(posCode, orgUser);
					//则从待删除数组中移除
					orgUserIds.remove(orgUser.getId());
				}
			}
		}
		
		if(orgUserIds.size() > 0){
			String[] ids = new String[orgUserIds.size()];
			orgUserIds.toArray(ids);
			// 删除多余的记录
			this.removeByIds(ids);
		}
		
		List<String> orgCodeList = new ArrayList<String>();
		List<String> postCodeList = new ArrayList<String>();
		//遍历前端传入的组织岗位信息列表，将编放入list里，以便新增用
		if(BeanUtils.isNotEmpty(orgsPoses)) {
			for (UserPolymerOrgPos userPolymerOrgPos : orgsPoses) {
				if(UserPolymerOrgPos.TYPE_ORG.equals(userPolymerOrgPos.getType())){
					orgCodeList.add(userPolymerOrgPos.getOrgCode());
				}
				if(UserPolymerOrgPos.TYPE_POS.equals(userPolymerOrgPos.getType())){
					postCodeList.add(userPolymerOrgPos.getPosCode());
				}
			}
		}
		
		// 遍历用户已经保存在数据库的组织
		Iterator<String> keyIt = orgMap.keySet().iterator();
		while(keyIt.hasNext()){
			String code = keyIt.next();
			// 把据库中已存在的组织从需要新增的组织list里移除
			orgCodeList.remove(code);
			OrgUser orgUser = orgMap.get(code);
			orgUser.setIsDelete("0");
			// 更新仍然关联的组织
			this.update(orgUser);
		}
		// 补齐缺少的组织
		for (String orgCode : orgCodeList) {
			orgService.addUsersForOrg(orgCode, account);
		}
		
		// 处理所属岗位
		Iterator<String> posKeyIt = posMap.keySet().iterator();
		while(posKeyIt.hasNext()){
			String code = posKeyIt.next();
			// 把据库中已存在的岗位从需要新增的岗位list里移除
			postCodeList.remove(code);
			OrgUser orgUser = posMap.get(code);
			orgUser.setIsDelete("0");
			// 更新仍然关联的岗位
			this.update(orgUser);
		}
		// 补齐缺少的岗位
		for (String posCode : postCodeList) {
			orgService.saveUserPost(account, posCode);
		}
	}


	@Override
    @Transactional
	public void checkIsInActiveTime(LocalDateTime date) {
		baseMapper.checkIsInActiveTime(date,date);
	}


	@SuppressWarnings("rawtypes")
	@Override
	public IPage getUserOrgPage(QueryFilter queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
		IPage<OrgUser> convert2iPage = convert2IPage(pageBean);
		// 设置分页
		//PageHelper.startPage(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
		return baseMapper.getUserOrgPage(convert2iPage,convert2Wrapper(queryFilter, currentModelClass()));
	}

    @Override
    @Transactional
    public CommonResult<String> deleteOrgById(String orgId, String userId) throws Exception {
        if(StringUtil.isEmpty(orgId)){
            throw new RuntimeException(HotentHttpStatus.REUIRED.description()+":请传参数组织ID");
        }
        if(StringUtil.isEmpty(userId)){
            throw new RuntimeException(HotentHttpStatus.REUIRED.description()+":请传参数用户ID");
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId", userId);
        params.put("orgId", orgId);
        baseMapper.deleteOrgById(params);
        return new CommonResult<String>(true, "删除成功！", "");
    }

	@Override
    @Transactional
	public void setMasterByIds(String... ids) {
		for(String i:ids) {
			OrgUser orgUser = this.get(i);
			if (orgUser.getIsMaster() == 0) {
				Org org = orgService.get(orgUser.getOrgId());
				List<OrgUser> orgUsers = baseMapper.getOrgUserMaster(orgUser.getUserId(), null);
				if (BeanUtils.isNotEmpty(orgUsers)) {
					//orgUserDao.cancelUserMasterOrgByUserId(orgUser.getUserId(),LocalDateTime.now());
					baseMapper.cancelUserMasterOrg(orgUser.getUserId(), org.getDemId(), LocalDateTime.now());
				}
				baseMapper.updateUserMasterOrg(i, LocalDateTime.now());
			}
		}
	}

	@Override
	public List getUserByGroupList(QueryFilter queryFilter) {
		queryFilter.addFilter("u.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		queryFilter.addFilter("orguser.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		queryFilter.addFilter("org.IS_DELE_", User.DELETE_YES, QueryOP.NOT_EQUAL);
		queryFilter.addFilter("ud.IS_DELE_",User.DELETE_YES,QueryOP.NOT_EQUAL);
		QueryWrapper queryWrapper = (QueryWrapper) convert2Wrapper(queryFilter, currentModelClass());
		queryWrapper.groupBy("u.id_");
		return baseMapper.getUserByGroupList(queryWrapper);
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}

	@Override
	public List<Map<String, Object>> getUserNumByOrgCode(Map<String, Object> map) {
		return baseMapper.getUserNumByOrgCode(map);
	}

	@Override
    @Transactional
	public void updateUserOrgByPostId(String id, String orgId) {
		baseMapper.updateUserOrgByPostId(id,orgId);
	}

	@Override
	public List getOrgUserData(QueryFilter queryFilter) {
		return baseMapper.getOrgUserData(convert2Wrapper(queryFilter, currentModelClass()));
	}
	@Override
    @Transactional
	public void removeByUserId(String id, LocalDateTime now) {
		baseMapper.removeByUserId(id, now);
	}

	@Override
	public Object getChargesByOrgId(String orgId, int i) {
		return baseMapper.getChargesByOrgId(orgId,i);
	}

	@Override
	public List<OrgUser> getByOrgCodeAndroleCode(String orgCode, String roleCode) {
		return baseMapper.getByOrgCodeAndroleCode(orgCode,roleCode);
	}

	@Override
	public List<OrgUser> getByPostCodeAndOrgCode(String orgCode, String postCode) {
		return baseMapper.getByPostCodeAndOrgCode(orgCode,postCode);
	}
	@Override
	public OrgUser getMainPostOrOrgByUserId(String userId) {
		List<OrgUser> mainPostByUserId = baseMapper.getMainPostOrOrgByUserId(userId);
		if(BeanUtils.isNotEmpty(mainPostByUserId)) {
			// 优先获取默认维度的主岗位，没有时获取其他维度的主岗位
			return mainPostByUserId.get(0);
		}
		return null;
	}
}
