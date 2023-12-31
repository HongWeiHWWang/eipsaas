package com.hotent.bpm.persistence.model.nodehandler;

import java.util.ArrayList;
import java.util.List;

import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.plugin.task.userassign.def.UserAssignPluginDef;
import org.w3c.dom.Element;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.def.FieldInitSetting;
import com.hotent.bpm.api.model.process.def.FormInitItem;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.nodedef.ext.BaseBpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.PluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.defxml.entity.ext.ButtonDef;
import com.hotent.bpm.defxml.entity.ext.Buttons;
import com.hotent.bpm.defxml.entity.ext.ExtPlugins;
import com.hotent.bpm.defxml.entity.ext.FieldSetting;
import com.hotent.bpm.defxml.entity.ext.Form;
import com.hotent.bpm.defxml.entity.ext.FormInitSetting;
import com.hotent.bpm.defxml.entity.ext.InitItem;
import com.hotent.bpm.defxml.entity.ext.InitItem.PrevSetting;
import com.hotent.bpm.defxml.entity.ext.InitItem.SaveSetting;
import com.hotent.bpm.defxml.entity.ext.MobileForm;
import com.hotent.bpm.defxml.entity.ext.PropItem;
import com.hotent.bpm.defxml.entity.ext.Propers;
import com.hotent.bpm.defxml.entity.ext.Script;
import com.hotent.bpm.defxml.entity.ext.Scripts;
import com.hotent.bpm.defxml.entity.ext.SubProcessForm;
import com.hotent.bpm.model.form.DefaultForm;
import com.hotent.bpm.model.form.FormCategory;
import com.hotent.bpm.persistence.util.BpmDefAccessorUtil;


public class PluginContextUtil {
	
	

	/**
	 * 处理节点插件。
	 * @param baseNodeDef
	 * @param baseNode 
	 * void
	 * @throws Exception 
	 */
	public static void handBaseNode(BaseBpmNodeDef baseNodeDef,Object baseNode) throws Exception{

		//处理插件
		handPlugin( baseNodeDef, baseNode);
		//处理表单
		handForm(baseNodeDef, baseNode);
		//处理脚本
		handScript(baseNodeDef, baseNode);
		//表单初始化功能。
		handFormInit(baseNodeDef, baseNode);
		//节点属性。
		handNodeProperties(baseNodeDef, baseNode);
		//处理节点按钮
		handButton(baseNodeDef, baseNode);
		
	}
	
	
	/**
	 * 处理按钮。
	 * @param userNodeDef
	 * @param userTask 
	 * void
	 */
	private static void handButton(BaseBpmNodeDef userNodeDef,Object baseNode){
		List<Button> btnList=new ArrayList<Button>();
		
		Buttons buttons=  BpmDefAccessorUtil.getButtons(baseNode);
		
		if(buttons==null) return;
		
		List<ButtonDef> list= buttons.getButton();
		
		if(BeanUtils.isEmpty(list)) return;
		
		for(ButtonDef button:list){
			Button btn=new Button(button.getName(),button.getAlias(),button.getBeforeScript(),button.getAfterScript(),button.getLock());
			btn.setGroovyScript(button.getGroovyScript());
			btn.setRejectMode(button.getRejectMode());
			//btn.setUrlForm(button.getUrlForm());
			btnList.add(btn);
		}
		
		userNodeDef.setButtons(btnList);
		
	}
	
	/**
	 * 处理节点属性。  
	 * @param userNodeDef
	 * @param baseNode 
	 * void
	 */
	private static void handNodeProperties(BaseBpmNodeDef userNodeDef,Object baseNode){
		Propers nodeProperties=BpmDefAccessorUtil.getNodeProperties(baseNode);
		
		if(nodeProperties==null) return ;
		
		List<PropItem> items= nodeProperties.getItem();
		
		if(BeanUtils.isEmpty(items)) return;
		
		for(PropItem item :items){
			NodeProperties properties=new NodeProperties();
            properties.setReferOpinion(item.getReferOpinion());
            properties.setApprovalArea(item.getApprovalArea());
			properties.setHelp(item.getHelp());
			properties.setHelpGlobal(item.getHelpGlobal());
			properties.setJumpType(item.getJumpType());
			properties.setBackMode(item.getBackMode());
			properties.setBackNode(item.getBackNode());
			properties.setBackUserMode(item.getBackUserMode());
			properties.setPostHandler(item.getPostHandler());
			properties.setPrevHandler(item.getPrevHandler());
			properties.setNodeId(userNodeDef.getNodeId());
			
			properties.setAllowExecutorEmpty(item.isAllowExecutorEmpty());
			properties.setSkipExecutorEmpty(item.isSkipExecutorEmpty());
			properties.setNotifyType(item.getNotifyType());
			properties.setDateType(item.getDateType());
			properties.setDueTime(item.getDueTime());
			properties.setPopWin(item.isPopWin());

			properties.setTemplate(item.getTemplate());
			properties.setPhone(item.getPhone());
			properties.setEmail(item.getEmail());
			properties.setSendType(item.getSendType());
			properties.setInitFillData(item.isInitFillData());
			
			properties.setChoiceExcutor(item.getChoiceExcutor());
			properties.setParentDefKey(item.getParentDefKey());
			properties.setAllowEditUrgentState(item.getAllowEditUrgentState());
			properties.setAllowSmsApproval(item.getAllowSmsApproval());
			properties.setUserNodeType(item.getUserNodeType());
			userNodeDef.addNodeProperties(properties);
			
		}
	}
	
	private static void handFormInit(BaseBpmNodeDef baseNodeDef,Object baseNode){
		NodeType type=baseNodeDef.getType();
		if(!type.equals(NodeType.START) &&
				!type.equals(NodeType.USERTASK) &&
				!type.equals(NodeType.SIGNTASK)) return ;
		FormInitSetting formInitSetting=BpmDefAccessorUtil.getFormInitSetting(baseNode);
		if(formInitSetting==null) return ;
		List<InitItem> initItems= formInitSetting.getInitItem();
		if(BeanUtils.isEmpty(initItems)) return;
		
		//baseNodeDef.addFormInitItem(initItem);
		for(InitItem item:initItems){
			FormInitItem initItem=new FormInitItem();
			initItem.setNodeId(baseNodeDef.getNodeId());
			initItem.setParentDefKey(item.getParentDefKey());
			
			PrevSetting prev= item.getPrevSetting();
			if(prev!=null){
				List<FieldSetting> fieldInitSettings= prev.getFieldSetting();
				List<FieldInitSetting> showSettings=convertFieldSetting(fieldInitSettings);
				initItem.setShowFieldsSetting(showSettings);
			}
			SaveSetting save=item.getSaveSetting();
			if(save!=null){
				List<FieldSetting> saveInitSettings= save.getFieldSetting();
				List<FieldInitSetting> saveSettings=convertFieldSetting(saveInitSettings);
				initItem.setSaveFieldsSetting(saveSettings);
			}
			
			baseNodeDef.addFormInitItem(initItem);
		}
		
	}
	
	/**
	 * 转换表单字段初始化列表。 
	 * @param list
	 * @return 
	 * List&lt;FieldInitSetting>
	 */
	private static List<FieldInitSetting> convertFieldSetting(List<FieldSetting> list){
		List<FieldInitSetting> rtnlist=new ArrayList<FieldInitSetting>();
		
		for(FieldSetting fieldSetting:list){
			FieldInitSetting initSetting=new FieldInitSetting();
			initSetting.setDescription(fieldSetting.getDescription());
			initSetting.setSetting(fieldSetting.getSetting());
			
			rtnlist.add(initSetting);
		}
		
		return rtnlist;
	}
	
	/**
	 * 处理节点脚本。
	 * @param baseNodeDef
	 * @param baseNode 
	 * void
	 */
	private static void handScript(BaseBpmNodeDef baseNodeDef,Object baseNode){
		Scripts scripts=BpmDefAccessorUtil.getScripts(baseNode);
		if(scripts==null ) return;
		
		List<Script> list= scripts.getScript();
		for(Script script:list){
			ScriptType scriptType=ScriptType.fromKey(script.getScriptType().value());
			baseNodeDef.addScript(scriptType  , script.getContent());
		}
	}
	
	/**
	 * 处理子流程表单。
	 * @param baseNodeDef
	 * @param baseNode 
	 * void
	 */
	public static void handSubForm(BaseBpmNodeDef baseNodeDef,Object baseNode){
		SubProcessForm subForm=BpmDefAccessorUtil.getSubForm(baseNode);
		if(subForm==null) return;
		
		List<Form> list= subForm.getFormOrMobileForm();
		if(BeanUtils.isEmpty(list)) return ;
		
		List<com.hotent.bpm.model.form.Form> formList =new ArrayList<com.hotent.bpm.model.form.Form>();
		
		for(Form form:list){
			com.hotent.bpm.model.form.Form frm = new DefaultForm();
			frm.setName(form.getName());
			frm.setFormValue(form.getFormValue());
			frm.setFormExtraConf(form.getFormExtraConf());
			frm.setHelpFile(form.getHelpFile());
			com.hotent.bpm.defxml.entity.ext.FormCategory formCategory = form.getType();
			if(formCategory != null){
				frm.setType(FormCategory.fromValue( form.getType().value() ));
			}
			frm.setParentFlowKey(form.getParentFlowKey());
			
			if(form instanceof MobileForm){
				frm.setFormType(FormType.MOBILE.value());
			}
			
			formList.add(frm);
		}
		
		baseNodeDef.setSubFormList(formList);
	}
	
	
	/**
	 * 处理表单定义。
	 * @param baseNodeDef
	 * @param baseNode 
	 * void
	 */
	private static void handForm(BaseBpmNodeDef baseNodeDef,Object baseNode){
		List<Form> formList=BpmDefAccessorUtil.getForm(baseNode);
		
		if(BeanUtils.isEmpty(formList)) return;
		
		List<com.hotent.bpm.model.form.Form> list=new ArrayList<com.hotent.bpm.model.form.Form>();
		
		for(Form form:formList){
			com.hotent.bpm.model.form.Form frm = new DefaultForm(); 
			frm.setName(form.getName());
			frm.setFormValue(form.getFormValue());
			frm.setFormExtraConf(form.getFormExtraConf());
			frm.setHelpFile(form.getHelpFile());
			FormCategory formCategory = null;
			if(form.getType() != null){
				formCategory = FormCategory.fromValue( form.getType().value() );
				frm.setType(formCategory);
			}
			if(form instanceof MobileForm) {
				frm.setFormType(FormType.MOBILE.value());
			}
			list.add(frm);
		}
		baseNodeDef.setForm(list); 
	}
	
	

	/**
	 * 处理节点插件。
	 * @param baseNodeDef
	 * @param baseNode 
	 * void
	 * @throws Exception 
	 */
	private static void handPlugin(BaseBpmNodeDef baseNodeDef,Object baseNode) throws Exception{
		List<BpmPluginContext> list=new ArrayList<BpmPluginContext>();
		
		ExtPlugins extPlugins=BpmDefAccessorUtil.getNodeExtPlugins(baseNode);
		
		if(extPlugins==null) return ;
		
		List<Object> pluginList= extPlugins.getAny();
		for(Object obj:pluginList){
			if(!(obj instanceof Element)) continue;
				
			Element el=(Element)obj;
			//获取插件上下文定义。
			String pluginContextBeanId = el.getLocalName() + PluginContext.PLUGINCONTEXT; 
			PluginContext pluginContext=(PluginContext)AppUtil.getBean(pluginContextBeanId);
			if(pluginContext==null) continue;

			//解析插件的XML数据。
			BpmPluginDef bpmPluginDef = pluginContext.parse(el);
            if("userCopyToPluginContext".equals(pluginContextBeanId)){
                List<UserAssignRule> list1 = new ArrayList<>();
                for(UserAssignRule userAssignRule : ((UserAssignPluginDef) bpmPluginDef).getRuleList()){
                    userAssignRule.setType("copyTo");
                    list1.add(userAssignRule);
                }
                ((UserAssignPluginDef) bpmPluginDef).setRuleList(list1);
            }
			((AbstractBpmPluginContext)pluginContext).setBpmPluginDef(bpmPluginDef);
			
			// 防止从内存中获取到相同的对象
			BpmPluginContext cloneBean = (BpmPluginContext) BeanUtils.cloneBean(pluginContext);
			
			list.add(cloneBean);
			
			
		}
		baseNodeDef.setBpmPluginContexts(list);
		
	}
	
	
	
}
