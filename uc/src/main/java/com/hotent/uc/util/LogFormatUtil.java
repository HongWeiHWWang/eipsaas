package com.hotent.uc.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.model.User;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;

public class LogFormatUtil {
	public final static String[] orgFields = new String[]{"name","parentId","grade","demId","orderNo","path","pathName"};
	public final static String[] postFields = new String[]{"relName","isCharge"};
	public final static String[] userFields = new String[]{"fullname","password","email","mobile","address","photo","sex","from","status","hasSyncToWx","notifyType","userNumber","idCard","phone","birthday","entryDate","education"};
	private static final Map<String, String> orgLableMap;  
    static  
    {  
    	orgLableMap = new HashMap<String, String>();  
    	orgLableMap.put("id", "组织id");  
    	orgLableMap.put("code", "组织编码");  
    	orgLableMap.put("name", "组织名称");  
    	orgLableMap.put("parentId", "父组织id");  
    	orgLableMap.put("grade", "组织级别");  
    	orgLableMap.put("demId", "维度id");  
    	orgLableMap.put("orderNo", "序号");  
    	orgLableMap.put("path", "组织路径");  
    	orgLableMap.put("pathName", "路径名称");  
    }  
    private static final Map<String, String> postLableMap;  
    static  
    {  
    	postLableMap = new HashMap<String, String>();  
    	postLableMap.put("id", "岗位id");  
    	postLableMap.put("orgId", "组织id");  
    	postLableMap.put("relDefId", "职务id");  
    	postLableMap.put("relName", "岗位名称");  
    	postLableMap.put("relCode", "岗位编码");  
    	postLableMap.put("isCharge", "是否主岗位");  
    }  
    private static final Map<String, String> orgUserLableMap;  
    static  
    {  
    	orgUserLableMap = new HashMap<String, String>();  
    	orgUserLableMap.put("id", "用户组织关系id");  
    	orgUserLableMap.put("orgId", "组织id");  
    	orgUserLableMap.put("userId", "用户id");  
    	orgUserLableMap.put("isMaster", "是否主组织");  
    	orgUserLableMap.put("isCharge", "是否组织负责人");  
    	orgUserLableMap.put("relId", "岗位id");  
    	orgUserLableMap.put("isRelActive", "是否生效"); 
    }  
    
    private static final Map<String, String> userLableMap;  
    static  
    {  
    	userLableMap = new HashMap<String, String>();  
    	userLableMap.put("id", "用户id");  
    	userLableMap.put("fullname", "姓名");  
    	userLableMap.put("account", "账号");  
    	userLableMap.put("password", "密码");  
    	userLableMap.put("email", "邮箱");  
    	userLableMap.put("mobile", "手机号码");  
    	userLableMap.put("address", "地址"); 
    	userLableMap.put("photo", "头像");  
    	userLableMap.put("sex", "性别");  
    	userLableMap.put("from", "来源");  
    	userLableMap.put("status", "状态：0:禁用，1正常，-1未激活，-2离职"); 
    	userLableMap.put("hasSyncToWx", "微信同步关注状态");  
    	userLableMap.put("notifyType", "消息通知类型");  
    	userLableMap.put("userNumber", "工号");  
    	userLableMap.put("idCard", "身份证号"); 
    	userLableMap.put("phone", "办公电话");  
    	userLableMap.put("birthday", "生日"); 
    	userLableMap.put("entryDate", "入职日期");  
    	userLableMap.put("education", "学历"); 
    }  
    
	/**
	 * 获取组织新增或更新信息
	 * @param newOrg
	 * @param oldOrg
	 * @return
	 */
	public static String getOrgLog(Org newOrg,Org oldOrg){
		StringBuilder msg = new StringBuilder();
		if(BeanUtils.isEmpty(newOrg)) return "";
		if(BeanUtils.isEmpty(oldOrg)){
			msg.append("新增组织："+newOrg.getName()+"【"+newOrg.getCode()+"】；");
			//msg.append(getCommonLog(newOrg, null, orgFields, orgLableMap));
		}else{
			String updateMsg = getCommonLog(newOrg, oldOrg, orgFields, orgLableMap);
			if(StringUtil.isNotEmpty(updateMsg)){
				msg.append("更新组织："+oldOrg.getName()+"【"+newOrg.getCode()+"】：");
				msg.append(updateMsg);
			}
		}
		return msg.toString();
	}
	
	/**
	 * 获取用户新增或更新信息
	 * @param newOrg
	 * @param oldOrg
	 * @return
	 */
	public static String getUserLog(User newUser,User oldUser){
		StringBuilder msg = new StringBuilder();
		if(BeanUtils.isEmpty(newUser)) return "";
		if(BeanUtils.isEmpty(oldUser)){
			msg.append("新增用户："+newUser.getFullname()+"【"+newUser.getAccount()+"】；");
		}else{
			String updateMsg = getCommonLog(newUser, oldUser, userFields, userLableMap);
			if(StringUtil.isNotEmpty(updateMsg)){
				msg.append("更新用户："+oldUser.getFullname()+"【"+newUser.getAccount()+"】：");
				msg.append(updateMsg);
			}
		}
		return msg.toString();
	}
	
	/**
	 *  获取岗位新增或更新信息
	 * @param newPost
	 * @param oldPost
	 * @return
	 */
	public static String getPostLog(OrgPost newPost,OrgPost oldPost){
		StringBuilder msg = new StringBuilder();
		if(BeanUtils.isEmpty(newPost)) return "";
		if(BeanUtils.isEmpty(oldPost)){
			msg.append("新增岗位："+newPost.getName()+"【"+newPost.getCode()+"】；");
			//msg.append(getCommonLog(newPost, null, orgFields, orgLableMap));
		}else{
			String updateMsg = getCommonLog(newPost, oldPost, orgFields, orgLableMap);
			if(StringUtil.isNotEmpty(updateMsg)){
				msg.append("更新岗位："+newPost.getName()+"【"+newPost.getCode()+"】：");
				msg.append(updateMsg);
			}
		}
		return msg.toString();
	}
	
	/**
	 *  获取用户组织岗位关系新增信息
	 * @param newPost
	 * @param oldPost
	 * @return
	 */
	public static String getOrgUserLog(OrgUser orgUser, Map<String,String> userMap,
			Map<String,String> orgMap,Map<String,String> postMap){
		StringBuilder msg = new StringBuilder();
		if(BeanUtils.isEmpty(orgUser)) return "";
		msg.append("用户组织关系：id【"+orgUser.getId()+"】，");
		msg.append("组织【"+orgMap.get(orgUser.getOrgId())+"】，");
		msg.append("用户【"+userMap.get(orgUser.getUserId())+"】，");
		if(StringUtil.isNotEmpty(orgUser.getRelId())){
			msg.append("岗位【"+postMap.get(orgUser.getRelId())+"】");
		}
		msg.append("；");
		//msg.append(getCommonLog(orgUser, null, null, orgUserLableMap));
		return msg.toString();
	}
	
	public static String getCommonLog(Object newObject,Object oldObject,String[] objectFields,Map<String,String> lableMap){
		StringBuilder msg = new StringBuilder();
		try {
			ObjectNode newJson = (ObjectNode) JsonUtil.toJsonNode(newObject);
			if(BeanUtils.isNotEmpty(oldObject)){
				ObjectNode oldJson = (ObjectNode) JsonUtil.toJsonNode(oldObject);
				for (String field : objectFields) {
					Object newValue = newJson.get(field);
					Object oldValue = oldJson.get(field);
					if(BeanUtils.isEmpty(newValue)){
						newValue = new Object();
					}
					if(BeanUtils.isEmpty(oldValue)){
						oldValue = new Object();
					}
					if(!(BeanUtils.isEmpty(oldJson.get(field))&&BeanUtils.isEmpty(newJson.get(field)))&&(!newValue.equals(oldValue))){
						msg.append(lableMap.get(field)+"由【"+oldJson.get(field)+"】更新为【"+newJson.get(field)+"】");
					}
				}
			}else{
				boolean isFirst = true;
				for (String key : lableMap.keySet()) {
					if(!isFirst){
						msg.append("，");
					}else{
						isFirst = false;
					}
					msg.append(lableMap.get(key)+"："+newJson.get(key));
				}
				msg.append("；");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg.toString();
	}
}
