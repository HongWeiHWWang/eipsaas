package com.hotent.bpm.persistence.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.api.model.IPermission;



/**
 * 授权服务类
 * 
 * @author Administrator
 *
 */
@Service(value="bpmCurrentUserService")
public class CurrentUserService
{
	public static String DEFAULT_OBJECT_RIGHTTYPE_BEAN = "defaultObjectRightType";

	/**
	 * 获取当前用户已授权类型的Map列表（即各种授权插件策略实现的授权） Map<String, String>
	 * key:rightType,value:以逗号隔开并且以单引号括起来的字符串
	 * 
	 * @param currentUser
	 * @return
	 */
	public Map<String, String> getUserRightMapString(){
		Map<String, Set<String>> listMap = getUserRightMap( DEFAULT_OBJECT_RIGHTTYPE_BEAN);
		return getMapStringByMayList(listMap);
	}

	/**
	 * 值为List转为以逗号隔开并且以单引号括起来的字符串
	 * 
	 * @param listMap
	 * @return
	 */
	public Map<String, String> getMapStringByMayList(Map<String, Set<String>> listMap){
		Map<String, String> map = new HashMap<String, String>();
		for (String key : listMap.keySet()){
			Set<String> list = listMap.get(key);
			if(list==null)continue;
			String valueString = convertListToSingleQuotesString(list);
			if(StringUtil.isNotEmpty(valueString)){
				map.put(key, valueString);
			}
		}
		return map;
	}

	/**
	 * 获取当前用户已授权类型的Map列表（即各种授权插件策略实现的授权）
	 * 
	 * @param currentUser
	 * @return
	 */
	public Map<String, Set<String>> getUserRightMap(){
		return getUserRightMap( DEFAULT_OBJECT_RIGHTTYPE_BEAN);
	}

	/**
	 * 获取当前用户已授权类型的Map列表（即各种授权插件策略实现的授权）
	 * 
	 * @param currentUser
	 * @return
	 */
	public Map<String, Set<String>> getUserRightMap(String beanId){
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		List<IPermission> objectList = getCurUserServiceList(beanId);
		for (IPermission curObj : objectList){
			try {
				Set<String> list = curObj.getCurrentProfile();
				map.put(curObj.getType(), list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return map;
	}

	/**
	 * 获取权限类型与标题的Map
	 * 
	 * @param beanId
	 * @return
	 */
	public Map<String, String> getUserTypeMap(String beanId){
		Map<String, String> map = new HashMap<String, String>();
		List<IPermission> objectList = getCurUserServiceList(beanId);
		for (IPermission curObj : objectList){
			map.put(curObj.getType(), curObj.getTitle());
		}
		return map;
	}

	
	
	/**
	 * 获取授权的实现方法，这里返回对应实现类列表。
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<IPermission> getCurUserServiceList(String beanId){
		if (StringUtil.isEmpty(beanId))
			beanId = DEFAULT_OBJECT_RIGHTTYPE_BEAN;
		return (List<IPermission>) AppUtil.getBean(beanId);
	}

	/**
	 * List转成以单引号括起来字符串
	 * 
	 * @param list
	 * @return
	 */
	private String convertListToSingleQuotesString(Set<String> set){
		if(set==null)return "";
		String ids = "";
		for (String value : set){
			ids = ids + "\'" + value + "\',";
		}
		ids = ids.equals("") ? "" : ids.substring(0, ids.length() - 1);
		return ids;
	}


}
