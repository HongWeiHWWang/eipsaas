package com.hotent.bpm.api.helper.identity;

import java.util.List;
import java.util.Map;

import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.uc.api.model.IUser;

/**
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-3-上午10:07:17
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface UserQueryPluginHelper {

    public static final String TYPE_USER="user"; //审批人员插件
    public static final String TYPE_COPYTO="copyto";//传阅人员插件
	/**
	 * 
	 * 根据插件定义和流程变量查询bpmIdentity集合
	 * @param bpmPluginContexts
	 * @param variables
	 * @return 
	 * List&lt;BpmIdentity>
	 * @throws Exception 
	 */
	public List<BpmIdentity> query(List<BpmPluginContext> bpmPluginContexts,Map<String, Object> variables,String type) throws Exception;
	
	/**
	 * 
	 * 根据插件定义和流程变量查询用户集合
	 * @param bpmPluginContexts
	 * @param variables
	 * @return 
	 * List&lt;User>
	 * @throws Exception 
	 */
	public List<IUser> queryUsers(List<BpmPluginContext> bpmPluginContexts,Map<String, Object> variables) throws Exception;
	
	/**
	 * 通过条件json 预览用户
	 * @param conditionJson
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public List<IUser> queryUsersByConditions(String conditionJson,Map map) throws Exception;
	
	
}
