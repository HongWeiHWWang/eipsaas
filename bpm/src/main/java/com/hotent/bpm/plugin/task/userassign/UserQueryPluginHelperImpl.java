package com.hotent.bpm.plugin.task.userassign;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.helper.identity.UserQueryPluginHelper;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.UserQueryPluginContext;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.plugin.core.runtime.BpmUserCalcPlugin;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.core.util.UserAssignRuleQueryHelper;
import com.hotent.bpm.plugin.task.userassign.context.UserAssignPluginContext;
import com.hotent.bpm.plugin.task.userassign.def.UserAssignPluginDef;
import com.hotent.uc.api.model.IUser;

/**
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-core
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-3-上午10:06:39
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class UserQueryPluginHelperImpl implements UserQueryPluginHelper{
	
	public List<BpmIdentity> query(List<BpmPluginContext> bpmPluginContexts,Map<String, Object> variables,String type) throws Exception {
		List<BpmIdentity>allBpmIdentities = new ArrayList<BpmIdentity>();
		
		if(BeanUtils.isEmpty(bpmPluginContexts)) return allBpmIdentities;
		
		
		for(BpmPluginContext pluginContext:bpmPluginContexts){
		    if(type.equals("user")){
                if(pluginContext.getTitle().equals("用户分配插件")){
                    //如果是用户查询插件上下文
                    if(!(pluginContext instanceof UserQueryPluginContext)) continue;

                    //获得该运行时
                    BpmUserCalcPlugin userQueryPlugin = (BpmUserCalcPlugin)AppUtil.getBean(((UserQueryPluginContext)pluginContext).getUserQueryPluginClass());
                    if(userQueryPlugin==null) userQueryPlugin=(BpmUserCalcPlugin) AppUtil.getBean("userQueryPlugin");
                    //构造会话数据
                    BpmPluginSessionFactory bpmPluginSessionFactory = AppUtil.getBean(BpmPluginSessionFactory.class);
                    BpmUserCalcPluginSession bpmUserCalcPluginSession = bpmPluginSessionFactory.buildBpmUserCalcPluginSession(variables);

                    //计算用户
                    List<BpmIdentity> newBpmIdentities = userQueryPlugin.execute(bpmUserCalcPluginSession, pluginContext.getBpmPluginDef());

                    //加到返回集合中
                    allBpmIdentities.addAll(newBpmIdentities);
                }
            }else {
                if (type.equals("copyto")) {
                    if (pluginContext.getTitle().equals("传阅用户分配插件")) {
                        //如果是用户查询插件上下文
                        if (!(pluginContext instanceof UserQueryPluginContext)) continue;

                        //获得该运行时
                        BpmUserCalcPlugin userQueryPlugin = (BpmUserCalcPlugin) AppUtil.getBean(((UserQueryPluginContext) pluginContext).getUserQueryPluginClass());
                        if (userQueryPlugin == null)
                            userQueryPlugin = (BpmUserCalcPlugin) AppUtil.getBean("userQueryPlugin");
                        //构造会话数据
                        BpmPluginSessionFactory bpmPluginSessionFactory = AppUtil.getBean(BpmPluginSessionFactory.class);
                        BpmUserCalcPluginSession bpmUserCalcPluginSession = bpmPluginSessionFactory.buildBpmUserCalcPluginSession(variables);

                        //计算用户
                        List<BpmIdentity> newBpmIdentities = userQueryPlugin.execute(bpmUserCalcPluginSession, pluginContext.getBpmPluginDef());

                        //加到返回集合中
                        allBpmIdentities.addAll(newBpmIdentities);
                    }
                }
            }
		}
		
		return allBpmIdentities;
	}

	public List<IUser> queryUsers(List<BpmPluginContext> bpmPluginContexts,
			Map<String, Object> variables) throws Exception {
		//查询bi集合
		List<BpmIdentity> bpmIdentities = query(bpmPluginContexts, variables,UserQueryPluginHelper.TYPE_USER);
		//强制抽取
		BpmIdentityExtractService bpmIdentityExtractService = (BpmIdentityExtractService)AppUtil.getBean(BpmIdentityExtractService.class);
		
		List<IUser> users = bpmIdentityExtractService.extractUser(bpmIdentities);
		
		return users;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<IUser> queryUsersByConditions(String conditionJson,Map map) throws Exception {
		AbstractUserCalcPlugin.setPreviewMode(true); 
		UserAssignPluginContext context=AppUtil.getBean(UserAssignPluginContext.class);
		BpmIdentityExtractService bpmIdentityExtractService = (BpmIdentityExtractService)AppUtil.getBean(BpmIdentityExtractService.class);
		BpmPluginSessionFactory bpmPluginSessionFactory = AppUtil.getBean(BpmPluginSessionFactory.class);										
		BpmUserCalcPluginSession bpmUserCalcPluginSession = bpmPluginSessionFactory.buildBpmUserCalcPluginSession(map);//放入前天提供的数据
			
		try{
			context.parse(conditionJson);
			UserAssignPluginDef userAssignDef = (UserAssignPluginDef) context.getBpmPluginDef();
			
			List<UserAssignRule>  userAssignRuleList = userAssignDef.getRuleList(); 
			if(BeanUtils.isEmpty(userAssignRuleList)) return Collections.emptyList();
			
			List<BpmIdentity> identityList = UserAssignRuleQueryHelper.queryExtract(userAssignRuleList, bpmUserCalcPluginSession);
			if(BeanUtils.isEmpty(identityList)) return Collections.emptyList();
			
			List<IUser> users = bpmIdentityExtractService.extractUser(identityList);
			return users ;
		}finally{
			AbstractUserCalcPlugin.cleanPreviewMode();
		}
	}

}
