package com.hotent.uc.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.uc.manager.DemensionManager;
import com.hotent.uc.manager.OrgAuthManager;
import com.hotent.uc.manager.OrgManager;
import com.hotent.uc.manager.RelAuthManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.model.RelAuth;
import com.hotent.uc.model.User;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;



public class AuthFilterUtil {
	
	
	/**
	 * 添加维度数据权限过滤
	 * @param filter
	 */
	public static void setDemAuthFilter(QueryFilter filter){
		User user = OrgUtil.getCurrentUser();
		if(BeanUtils.isNotEmpty(user)&&!user.isAdmin()){
			OrgAuthManager orgAuthService = AppUtil.getBean(OrgAuthManager.class);
			List<OrgAuth> auths = orgAuthService.getByUserId(user.getId());
			if(BeanUtils.isNotEmpty(auths)){
				Map<String,String> typeIdMap = new HashMap<String,String>();
				for (OrgAuth orgAuth : auths) {
					typeIdMap.put(orgAuth.getDemId(), orgAuth.getDemId());
				}
				String[] typeIds = new String[typeIdMap.size()];
				int i = 0;
				for (Map.Entry<String, String> entry : typeIdMap.entrySet()) {
					typeIds[i] = entry.getKey();
					i++;
			    }
				filter.addFilter("id", typeIds, QueryOP.IN, FieldRelation.AND);
			}else{
				filter.addFilter("id", "", QueryOP.EQUAL, FieldRelation.AND);
			}
		}
	}
	
	
	
	public static String getOrgAuthSql(String demId) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		User user = OrgUtil.getCurrentUser();
		if(BeanUtils.isNotEmpty(user)&&!user.isAdmin()&&StringUtil.isNotEmpty(demId)){
			DemensionManager demensionService = AppUtil.getBean(DemensionManager.class);
			Demension dem = demensionService.get(demId);
			if(BeanUtils.isNotEmpty(dem)){
				OrgAuthManager orgAuthService = AppUtil.getBean(OrgAuthManager.class);
				List<OrgAuth> auths = orgAuthService.getOrgAuthListByDemAndUser(dem.getDemCode(), user.getAccount());
				sql.append(" AND ");
				if(BeanUtils.isNotEmpty(auths)){
					sql.append(" ( ");
					boolean isFirst = true;
					for (OrgAuth orgAuth : auths) {
						sql.append(isFirst?"":" OR ");
						sql.append(" PATH_ LIKE '"+orgAuth.getOrgPath()+"%' ");
						isFirst = false;
					}
					sql.append(" ) ");
				}else{
					sql.append(" ID_='0' ");
				}
			}else{
				sql.append(" AND ID_='0' ");
			}
		}
		return sql.toString();
	}
	
	public static String getOrgAuthParentId(String demId) throws Exception{
		User user = OrgUtil.getCurrentUser();
		StringBuilder ids = new StringBuilder();
		if(BeanUtils.isNotEmpty(user)&&!user.isAdmin()&&StringUtil.isNotEmpty(demId)){
			DemensionManager demensionService = AppUtil.getBean(DemensionManager.class);
			Demension dem = demensionService.get(demId);
			if(BeanUtils.isNotEmpty(dem)){
				OrgAuthManager orgAuthService = AppUtil.getBean(OrgAuthManager.class);
				OrgManager orgService = AppUtil.getBean(OrgManager.class);
				List<OrgAuth> auths = orgAuthService.getOrgAuthListByDemAndUser(dem.getDemCode(), user.getAccount());
				boolean isFirst = true;
				for (OrgAuth orgAuth : auths) {
					String orgId = orgAuth.getOrgId();
					Org org = orgService.get(orgId);
					if(BeanUtils.isNotEmpty(org)){
						ids.append(isFirst?"":",");
						ids.append("'"+org.getParentId()+"'");
						isFirst = false;
					}
				}
			}
		}
		String parentId = StringUtil.isNotEmpty(ids.toString())?ids.toString():"'0'";
		return " ( "+parentId+" ) ";
	}
	
	public static String getPostAuthSql() throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("");
		User user = OrgUtil.getCurrentUser();
		if(BeanUtils.isNotEmpty(user)&&!user.isAdmin()){
			OrgAuthManager orgAuthService = AppUtil.getBean(OrgAuthManager.class);
			List<OrgAuth> auths = orgAuthService.getByUserId(user.getUserId());
			if(BeanUtils.isNotEmpty(auths)){
				sql.append(" AND ORG_ID_ IN(select ORG_ID_ FROM UC_ORG_AUTH WHERE USER_ID_='"+user.getId()+"') ");
			}else{
				sql.append(" AND ID_='0' ");
			}
		}
		return sql.toString();
	}
	
	
	public static void setRelTypeAuthFilter(QueryFilter filter){
		User user = OrgUtil.getCurrentUser();
		if(BeanUtils.isNotEmpty(user)&&!user.isAdmin()){
			RelAuthManager relAuthService = AppUtil.getBean(RelAuthManager.class);
			List<RelAuth> auths = relAuthService.getByUserId(user.getId());
			if(BeanUtils.isNotEmpty(auths)){
				Map<String,String> typeIdMap = new HashMap<String,String>();
				for (RelAuth relAuth : auths) {
					typeIdMap.put(relAuth.getTypeId(), relAuth.getTypeId());
				}
				String[] typeIds = new String[typeIdMap.size()];
				int i = 0;
				for (Map.Entry<String, String> entry : typeIdMap.entrySet()) {
					typeIds[i] = entry.getKey();
					i++;
			    }
				filter.addFilter("id", typeIds, QueryOP.IN, FieldRelation.AND);
			}else{
				filter.addFilter("id", "", QueryOP.EQUAL, FieldRelation.AND);
			}
		}
	}
	
}

