package com.hotent.bpm.persistence.manager.impl;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.feign.UCFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.dao.BpmSecretaryManageDao;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmSecretaryManageManager;
import com.hotent.bpm.persistence.model.BpmSecretaryManage;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;

/**
 * 
 * <pre> 
 * 描述：秘书管理表 处理实现类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn	
 * 日期:2019-09-16 10:07:13
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("bpmSecretaryManageManager")
public class BpmSecretaryManageManagerImpl extends BaseManagerImpl<BpmSecretaryManageDao, BpmSecretaryManage> implements BpmSecretaryManageManager{

	@Resource
	BpmDefinitionManager bpmDefinitionManager;

	@Override
	public Map<String, Set<String>> getShareDefsBySecretaryId(String secretaryId,String  rightType) {
		Map<String, Set<String>> resultMap =new HashMap<>();
		QueryFilter<BpmSecretaryManage> queryFilter = QueryFilter.build();
		queryFilter.addFilter("SECRETARY_ID_", ","+secretaryId+",", QueryOP.LIKE);
		queryFilter.addFilter("ENABLED_", "1", QueryOP.EQUAL);
		if (StringUtil.isNotEmpty(rightType)) {
			queryFilter.addFilter("SHARE_RIGHT_", rightType, QueryOP.LIKE);
		}
		PageList<BpmSecretaryManage> query = super.query(queryFilter);
		if (BeanUtils.isEmpty(query) || query.getRows().size()==0) {
			return resultMap;
		}
		 Map<String, Set<String>> defKeyMap  =getDefTypeMap();
		for (BpmSecretaryManage secretary : query.getRows()) {
			Set<String> keySet = resultMap.containsKey(secretary.getLeaderId())?resultMap.get(secretary.getLeaderId()):new HashSet<>();
			String[] keyArray = secretary.getShareKey().split(",");
			if ("1".equals(secretary.getShareType())) {
				keySet.addAll(Arrays.asList(keyArray));
			}else{
				for (String typeId : keyArray) {
					if (defKeyMap.containsKey(typeId)) {
						keySet.addAll(defKeyMap.get(typeId));
					}
				}
			}
			resultMap.put(secretary.getLeaderId(), keySet);
		}
		return resultMap;
	}
	
	private Map<String, Set<String>> getDefTypeMap(){
		QueryFilter<DefaultBpmDefinition> deFilter = QueryFilter.build();
		deFilter.addParams("isAdmin", 1);
		deFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
		PageList<DefaultBpmDefinition> defQuery = bpmDefinitionManager.query(deFilter);
		Map<String, Set<String>> defKeyMap =new HashMap<>();
		if (BeanUtils.isNotEmpty(defQuery)) {
			for (DefaultBpmDefinition definition : defQuery.getRows()) {
				Set<String> keySet=new HashSet<>();
				if (defKeyMap.containsKey(definition.getTypeId())) {
					keySet = defKeyMap.get(definition.getTypeId());
				}
				keySet.add(definition.getDefKey());
				defKeyMap.put(definition.getTypeId(), keySet);
			}
		}
		return defKeyMap;
	}
	@Override
	public Map<String, Object> getLeadersRigthMapBySecretaryId(String secretaryId,String  rightType,Boolean isUser) {
		Map<String, Set<String>> shareDefsBySecretaryId = this.getShareDefsBySecretaryId(secretaryId, rightType);
		//把自己也加进去。避免查询待办的时候在查询一次用户组织
        if(isUser){//为true才把自己加进去
            shareDefsBySecretaryId.put(secretaryId, new HashSet<>());
        }
		UCFeignService  ucFeignService = AppUtil.getBean(UCFeignService.class);
		Map<String, Map<String, String>> userRightMapByIds = ucFeignService.getUserRightMapByIds(shareDefsBySecretaryId.keySet());
		
		Map<String, Object> resultMap =new HashMap<>();
		for (String userId : shareDefsBySecretaryId.keySet()) {
			Map<String, String> groupMap = new HashMap<String, String>();
			Map<String, Object> userRightMap =new HashMap<>();
			if (userId.equals(secretaryId)) {
				userRightMap.put("isSelf", true);
			}else{
				userRightMap.put("isSelf", false);
				userRightMap.put("defKeys", StringUtil.convertListToSingleQuotesString(shareDefsBySecretaryId.get(userId)));
			}
			if(userRightMapByIds.containsKey(userId)){
				groupMap = userRightMapByIds.get(userId);
			}
			userRightMap.put("groupMap", groupMap);
			resultMap.put(userId, userRightMap);
		}
		return resultMap;
	}
	@Override
	public Map<String, Set<String>> getSecretaryByleaderIds(Set<String> leaderIds, String defKey) {
		Map<String, Set<String>> resultMap =new HashMap<>();
		QueryFilter<BpmSecretaryManage> queryFilter = QueryFilter.build();
		queryFilter.addFilter("LEADER_ID_",new ArrayList<>(leaderIds), QueryOP.IN);
		queryFilter.addFilter("ENABLED_", "1", QueryOP.EQUAL);
		PageList<BpmSecretaryManage> query = super.query(queryFilter);
		if (BeanUtils.isEmpty(query) || query.getRows().size()==0) {
			return resultMap;
		}
		Map<String, Set<String>> defKeyMap  =getDefTypeMap();
		for (BpmSecretaryManage secretary : query.getRows()) {
			Set<String> secretaryIds = resultMap.containsKey(secretary.getLeaderId())?resultMap.get(secretary.getLeaderId()):new HashSet<>();
			Set<String> shareDefKeys = new HashSet<>();
			String[] keyArray = secretary.getShareKey().split(",");
			if ("1".equals(secretary.getShareType())) {
				shareDefKeys = new HashSet<>(Arrays.asList(keyArray));
			}else{
				for (String typeId : keyArray) {
					if (defKeyMap.containsKey(typeId)) {
						shareDefKeys.addAll(defKeyMap.get(typeId));
					}
				}
			}
			if (shareDefKeys.contains(defKey)) {
				secretaryIds.addAll(Arrays.asList(secretary.getSecretaryId().split(",")));
			}
			resultMap.put(secretary.getLeaderId(), secretaryIds);
		}
		return resultMap;
	}

	@Override
    public List<BpmSecretaryManage> getSecretaryByUserId(String userId){
	    return baseMapper.getSecretaryByUserId(userId);
    }
}
