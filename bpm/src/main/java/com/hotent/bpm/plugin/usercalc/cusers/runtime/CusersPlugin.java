package com.hotent.bpm.plugin.usercalc.cusers.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.exception.UserCalcException;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.usercalc.UserCalcHelper;
import com.hotent.bpm.plugin.usercalc.cuserrel.def.ExecutorVar;
import com.hotent.bpm.plugin.usercalc.cusers.def.CusersPluginDef;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IOrgService;

public class CusersPlugin extends AbstractUserCalcPlugin{

	@SuppressWarnings("unused")
	@Override
	public List<BpmIdentity> queryByPluginDef(
			BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) {
		
		List<BpmIdentity> list=new ArrayList<BpmIdentity>();
		ActionCmd action= ContextThreadUtil.getActionCmd();
		CusersPluginDef def=( CusersPluginDef)pluginDef;
		IOrgService orgEngine= pluginSession.getOrgEngine();
		Map<String, Object> vars= pluginSession.getVariables();
		String source=def.getSource();
		String curUserId="";
		boolean isReqUc = StringUtil.isEmpty((String)vars.get(BpmConstants.NOT_REQUEST_UC));
		if (isReqUc) {
			curUserId = ContextUtil.getCurrentUser().getUserId();
		}else {
			curUserId = (String)vars.get(BpmConstants.START_USER);
		}
		if("start".equals(source)){
			String startId=(String)vars.get(BpmConstants.START_USER);
			if ( StringUtil.isNotEmpty(startId)) {
				IUser user =new UserFacade();
				user.setUserId(startId);
				if (isReqUc) {
					 user= orgEngine.getUserService().getUserById(startId);
				}
				BpmIdentity bpmIdentity= getBpmIdentityConverter().convertUser(user);
				list.add(bpmIdentity);
			}
		}
		if("currentUser".equals(source) || "prev".equals(source)){
			IUser user =new UserFacade();
			user.setUserId(curUserId);
			if (isReqUc) {
				 user= orgEngine.getUserService().getUserById(curUserId);
			}
			BpmIdentity bpmIdentity= getBpmIdentityConverter().convertUser(user);
			list.add(bpmIdentity);
		}else if("spec".equals(source)){
			String userKeys=def.getAccount();
			String[] aryAccount=userKeys.split(",");
			for(String account:aryAccount){
				IUser user =new UserFacade();
				user.setAccount(account);
				if (isReqUc) {
					user= orgEngine.getUserService().getUserByAccount(account);
				}
				BpmIdentity bpmIdentity= getBpmIdentityConverter().convertUser(user);
				list.add(bpmIdentity);
			}
		}else if("var".equals(source)){
			ExecutorVar executorVar = def.getVar();
			
			//预览模式       （所有参数都是ID）
			if(isPreviewMode()){
				if(ExecutorVar.EXECUTOR_TYPE_USER.equals(executorVar.getExecutorType())){
					String userId = (String) vars.get(executorVar.getName());
					IUser user =new UserFacade();
					user.setUserId(userId);
					if (isReqUc) {
						user= orgEngine.getUserService().getUserByAccount(userId);
					}
					BpmIdentity bpmIdentity= getBpmIdentityConverter().convertUser(user);
					list.add(bpmIdentity);
				}
				return list;
			}
			
			//user变量，如果bo类型    取bo的值，如果是流程变量取流程变量的值
			if(ExecutorVar.EXECUTOR_TYPE_USER.equals(executorVar.getExecutorType())){
				if(ExecutorVar.SOURCE_BO.equals(executorVar.getSource())){
					String [] BOData =  executorVar.getName().split("\\.");
					if(BOData.length != 2 && BOData.length != 3) throw new UserCalcException("BO["+executorVar.getName()+"]数据 格式不合法");
				
//					String pk = (String)databoject.get(BOData[1]);
			 
					List<String> listName= UserCalcHelper.calcVarValue(executorVar, pluginSession, false);
					for (String keyName : listName) {
						IUser user = new  UserFacade();
						String pk =keyName;
						
						if (isReqUc) {
							if("account".equals(executorVar.getUserValType())){
								  user = orgEngine.getUserService().getUserByAccount(pk);
							}
							else{
								user= orgEngine.getUserService().getUserById(pk);
							}	
						}else{
							if("account".equals(executorVar.getUserValType())){
								user.setAccount(pk);
							}
							else{
								user.setUserId(pk);
							}
						}
						
						
						BpmIdentity bpmIdentity= getBpmIdentityConverter().convertUser(user);
						list.add(bpmIdentity);
					} 
					
				}else if(ExecutorVar.SOURCE_FLOW_VAR.equals(executorVar.getSource())){
					String PK=(String)vars.get(executorVar.getName());
					//如果流程变量为发起人，则默认按ID查找
					if(executorVar.getName().equals(BpmConstants.START_USER)){
						executorVar.setUserValType("id");
					}
					String[] PKs=PK.split(",");
					for(String pk:PKs){
						IUser user = new  UserFacade();
						if (isReqUc) {
							if("account".equals(executorVar.getUserValType())){
								  user = orgEngine.getUserService().getUserByAccount(pk);
							}
							else{
								user= orgEngine.getUserService().getUserById(pk);
							}	
						}else{
							if("account".equals(executorVar.getUserValType())){
								user.setAccount(pk);
							}
							else{
								user.setUserId(pk);
							}
						}
						
						BpmIdentity bpmIdentity= getBpmIdentityConverter().convertUser(user);
						list.add(bpmIdentity);
					}
				} 
			}
				
			
		}
		return list;
	}
	

}
