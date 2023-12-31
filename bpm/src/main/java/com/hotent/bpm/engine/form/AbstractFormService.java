package com.hotent.bpm.engine.form;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeForm;
import com.hotent.bpm.api.model.process.nodedef.ext.CallActivityNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.api.service.BpmFormService;
import com.hotent.bpm.model.form.BpmForm;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.form.FormCategory;
import com.hotent.bpm.model.form.FormModel;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.persistence.manager.BpmBusLinkManager;
import com.hotent.bpm.persistence.manager.BpmInstFormManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.BpmBusLink;
import com.hotent.bpm.persistence.model.BpmInstForm;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.util.BpmUtil;
import com.hotent.bpm.util.PortalDataUtil;
import com.hotent.i18n.util.I18nUtil;



/**
 * 表单获取抽象类，他有两种情况。
 * <pre>
 * 1.PC表单。
 * 2.mobile表单。
 * </pre>
 * @author ray
 *
 */
public abstract class AbstractFormService implements BpmFormService {
	/**
	 * 匹配 "{pk},{用户}这种模式的字符串。
	 */
	private static Pattern regex = Pattern.compile("\\{(\\w+)\\}", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	@Resource
	protected BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	protected BpmDefinitionService bpmDefinitionService;
	@Resource
	protected BpmBusLinkManager bpmBusLinkManager;
	@Resource
	protected NatProInstanceService natProcessInstanceService;
	@Resource
	protected FormFeignService formService;
	@Resource
	protected BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	protected BpmInstFormManager bpmInstFormManager;
	/**
	 * 获取节点表单
	 * @param bpmNodeDef
	 * @return
	 */
	protected abstract Form getFormByNodeDef(BpmNodeDef bpmNodeDef);
	
	/**
	 * 获取全局表单
	 * @param defExt
	 * @return
	 */
	protected abstract Form getGlobalFormByDefExt(DefaultBpmProcessDefExt defExt);
	
	/**
	 * 获取实例表单。
	 * @param defExt
	 * @return
	 */
	protected abstract Form getInstFormByDefExt(DefaultBpmProcessDefExt defExt);
	
	/**
	 * 获取子流程表单。
	 * @param bpmNodeDef
	 * @param parentDefKey
	 * @return
	 */
	protected abstract Form  getSubForm(BpmNodeDef bpmNodeDef,String parentDefKey);
	
	
	@Override
	public BpmNodeForm getByDefId(String defId) throws Exception{
		BpmNodeForm nodeForm = getFormDefByDefId(defId);
		if (nodeForm == null) 	return null;

		FormModel formModel = getByForm(nodeForm.getForm(), null);
		String formHtml = I18nUtil.replaceTemp(formModel.getFormHtml(), StringPool.FORM_REG,LocaleContextHolder.getLocale());
		formModel.setFormHtml(formHtml);
		nodeForm.setForm(formModel);
		return nodeForm;
	}

	
	/**
	 * 表单是否为空。
	 * @param form
	 * @return
	 */
	protected boolean isNotEmptyForm(Form form){
		if(form==null) return false;
		return !form.isFormEmpty();
	}
	
	
	
	/**
	 * 在发起流程时获取流程表单。
	 * 
	 * <pre>
	 * 获取逻辑顺序：
	 * 1.取得开启节点的表单。
	 * 2.获取第一个任务节点的表单。
	 * 3.取得全局表单。
	 * </pre>
	 * 
	 * @param defId
	 * @return Form
	 * @throws Exception 
	 */
	protected BpmNodeForm getFormDefByDefId(String defId) throws Exception{
		BpmNodeForm nodeForm=new BpmNodeForm();
	
		BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
		// 开始节点
		BpmNodeDef bpmNodeDef = bpmProcessDef.getStartEvent();
		//1.获取开始节点的表单
		Form form =getFormByNodeDef(bpmNodeDef);
		String helpFile = "";
		if (BeanUtils.isNotEmpty(form)){
			if (StringUtil.isNotEmpty(form.getHelpFile()) && StringUtil.isNotEmpty(form.getFormValue())) {
				nodeForm.setBpmNodeDef(bpmNodeDef);
				nodeForm.setForm(form);
				return nodeForm;
			}
			helpFile = form.getHelpFile();
		}
		//2.获取第一个节点的表单。
		List<BpmNodeDef> bpmNodeDefs = bpmProcessDef.getStartNodes();
		if (BeanUtils.isNotEmpty(bpmNodeDefs) && bpmNodeDefs.size() == 1){
			BpmNodeDef nodeDef = bpmNodeDefs.get(0);
			if (nodeDef instanceof UserTaskNodeDef){
				//form = nodeDef.getMobileForm();
				Form task1Form =getFormByNodeDef( nodeDef);
				if(BeanUtils.isNotEmpty(task1Form)){
					helpFile = StringUtil.isNotEmpty(helpFile) ? helpFile:task1Form.getHelpFile();
					if (!isNotEmptyForm(form) && StringUtil.isNotEmpty(task1Form.getFormValue())) {
						form = task1Form;
					}
				}
			}
		}
		//3.获取全局的表单。
		if (!isNotEmptyForm(form) || StringUtil.isEmpty(helpFile)){
			DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
			Form gloableForm = getGlobalFormByDefExt(defExt);
			if(BeanUtils.isNotEmpty(gloableForm)){
				helpFile = StringUtil.isNotEmpty(helpFile) ? helpFile:gloableForm.getHelpFile();
				if (!isNotEmptyForm(form) && StringUtil.isNotEmpty(gloableForm.getFormValue())) {
					form = gloableForm;
				}
			}
		}
		if (isNotEmptyForm(form)) {
			form.setHelpFile(helpFile);
			nodeForm.setBpmNodeDef(bpmNodeDef);
			nodeForm.setForm(form);
			return nodeForm;
		}
		return null;
	}
	
	@Override
	public BpmNodeForm getByDraft(BpmProcessInstance instance) throws Exception{
		String defId = instance.getProcDefId();
		BpmNodeForm nodeForm = getFormDefByDefId(defId);
		
		if (nodeForm == null) 	return null;

		FormModel formModel = getByForm(nodeForm.getForm(), instance);
		String formHtml = I18nUtil.replaceTemp(formModel.getFormHtml(), StringPool.FORM_REG,LocaleContextHolder.getLocale());
		formModel.setFormHtml(formHtml);	
		nodeForm.setForm(formModel);
		return nodeForm;
	}
	
	private BpmForm getFormByKey(String formKey) throws JsonParseException, JsonMappingException, IOException {
		AbstractFormService bean = AppUtil.getBean(getClass());
		return bean.getFormByKeyFromCache(formKey);
	}
	
	@Cacheable(value = "form:bpmForm", key = "#formKey")
	protected BpmForm getFormByKeyFromCache(String formKey) throws JsonParseException, JsonMappingException, IOException {
		ObjectNode byFormKey = formService.getByFormKey(formKey);
		BpmForm bpmFrom = JsonUtil.toBean(byFormKey, BpmForm.class);
		if(BeanUtils.isEmpty(bpmFrom)) throw new RuntimeException("根据本表单key:"+formKey+"未找到相应的表单");
		return bpmFrom;
	}
	
	/**
	 * 根据表单定义对象获取表单model。
	 * 
	 * @param frm
	 * @return FormModel
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	protected FormModel getByForm(Form frm, BpmProcessInstance instance) throws ClientProtocolException, IOException{
		FormModel formModel = new BpmForm(frm);
		FormCategory formType=formModel.getType();
		if (FormCategory.INNER.equals(formType)){
			String formKey = formModel.getFormValue();
			BpmForm bpmFrom = getFormByKey(formKey);
			FormModel formModelDB = (FormModel) bpmFrom;
			if(BeanUtils.isNotEmpty(formModelDB)){
				String html=formModelDB.getFormHtml();
				if(html.indexOf("</form>")==-1){
					//html="<form name='custForm' >" + html +"</form>";
				}
				
				formModel.setFormHtml(html);
				formModel.setFormKey(formModelDB.getFormKey());
				formModel.setFormId(formModelDB.getFormId());
			}
		}
		else if (FormCategory.FRAME.equals(formType)){
			if (instance != null){
				String url = frm.getFormValue();
				url = getUrl(instance, url);
				formModel.setFormValue(url);
			}
			else{
				String url = frm.getFormValue();
				url=replaceStr(url);
				formModel.setFormValue(url);
			}
		}
		String formHtml = I18nUtil.replaceTemp(formModel.getFormHtml(), StringPool.FORM_REG,LocaleContextHolder.getLocale());
		formModel.setFormHtml(formHtml);
		return formModel;
	}
	
	/**
	 * 根据表单定义对象获取表单model。
	 * 
	 * @param frm
	 * @return FormModel
	 */
	protected FormModel handForm(FormModel formModel, BpmProcessInstance instance){
		FormCategory formType=formModel.getType();
		if (FormCategory.INNER.equals(formType)){
			String html=formModel.getFormHtml();
			if(html.indexOf("</form>")==-1){
				//html="<form name='custForm' >" + html +"</form>";
			}
			formModel.setFormValue(formModel.getFormKey());
			formModel.setFormHtml(html);
		}
		else if (FormCategory.FRAME.equals(formType)){
			if (instance != null){
				String url = formModel.getFormValue();
				url = getUrl(instance, url);
				formModel.setFormValue(url);
			}
			else{
				String url = formModel.getFormValue();
				url=replaceStr(url);
				formModel.setFormValue(url);
			}
		}
		return formModel;
	}
	
	/**
	 * 获取流程节点的表单定义。
	 * 
	 * <pre>
	 * 只获取本流程定义下的流程节点表单。
	 * </pre>
	 * @throws Exception 
	 * 
	 */
	@Override
	public Form getFormDefByDefNode(String defId, String nodeId, BpmProcessInstance bpmProcessInstance) throws Exception{
		BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		Form form = null;
		//检查子流程配置
		String parentInstId = bpmProcessInstance.getParentInstId();
		//没有父流程实例，即不是外部子流程的情况。
		if(StringUtil.isZeroEmpty(parentInstId)&&bpmNodeDef!=null){
			form=getFormByNodeDef(bpmNodeDef);
		}
		return form;
	}

	
	
	@Override
	public FormModel getInstFormByDefId(BpmProcessInstance instance) throws Exception
	{
		BpmProcessInstance topInstance=bpmProcessInstanceManager.getTopBpmProcessInstance(instance);
		
		String defId=topInstance.getProcDefId();
		FormModel formModel = getInstanceNodeForm(instance, defId, instance.getId());
		//实例中的表单取实例创建时的表单版本
		if(BeanUtils.isNotEmpty(formModel)){
			return formModel;
		}
		// 如果实例表单为空，去拿全局表单（启动时的版本）。
		FormModel frmModel = getInstanceNodeForm(instance, defId, null);
		return frmModel;
	}
	
	
	@Override
    public FormModel getByDefId(String defId, String nodeId, BpmProcessInstance instance,boolean isTodoForm) throws Exception{

        BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
        String useMainForm = "";
        if(BeanUtils.isNotEmpty(bpmNodeDef)){
            useMainForm = BpmUtil.getUseMainForm(bpmNodeDef);
            if(StringUtil.isNotZeroEmpty(instance.getParentInstId())&&bpmNodeDef.getParentBpmNodeDef()==null){
                BpmProcessInstance parentInstance = bpmProcessInstanceManager.get(instance.getParentInstId());
                if(BeanUtils.isNotEmpty(parentInstance)){
                    BpmNodeDef parentBpmNodeDef = bpmDefinitionAccessor.getStartEvent(parentInstance.getProcDefId());
                    useMainForm = BpmUtil.getUseMainForm(parentBpmNodeDef);
                }
            }
        }
        if(StringUtil.isEmpty(useMainForm)){
            useMainForm = BpmConstants.MAIN_VERSION;
            String isUsedMainForm=PortalDataUtil.getPropertyByAlias("inst.useMainForm");
            if(BeanUtils.isEmpty(isUsedMainForm)) isUsedMainForm="true";
            if(!Boolean.parseBoolean(isUsedMainForm)){
                useMainForm = BpmConstants.START_VERSION;
            }
        }
        if(useMainForm.equals(BpmConstants.START_VERSION)){
            // 获取流程启动时节点使用的表单
            FormModel formModel = getInstanceNodeForm(instance, defId, nodeId);
            if(BeanUtils.isNotEmpty(formModel)){
                return formModel;
            }
        }
        Form frm = null;
        String formHelpFile = "";
        //属于主流程
        if(StringUtil.isZeroEmpty(instance.getParentInstId())){
            // 从节点配置获取。
            frm = getFormDefByDefNode(defId, nodeId, instance);
            if (frm != null){
            	formHelpFile = frm.getHelpFile();
            	//配置了节点表单和帮助文档，则直接返回
                if (StringUtil.isNotEmpty(frm.getFormValue()) && StringUtil.isNotEmpty(formHelpFile)) {
                	FormModel formModel = getByForm(frm, instance);
                    return formModel;
				}
            }

            // 节点表单为空或者没有节点帮助文档，则获取全局表单或者全局帮助文档
            if (frm == null || StringUtil.isEmpty(frm.getFormValue()) || StringUtil.isEmpty(formHelpFile)){
                BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
                DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
                // 待办的话，：节点表单>全局表单>实例表单--否则提示无表单
                if (isTodoForm) {
                	Form globalForm= getGlobalFormByDefExt(defExt);
                	//如果没有配置节点表单，则用全局表单
                	if (BeanUtils.isEmpty(frm) || BeanUtils.isEmpty(frm.getFormValue()) ) {
                		frm = globalForm;
					}
                	//如果没有配置节点帮助文档，则使用全局帮助文档
                	if (StringUtil.isEmpty(formHelpFile) && BeanUtils.isNotEmpty(globalForm) ) {
                		formHelpFile =globalForm.getHelpFile();
					}
                	
                    if(BeanUtils.isEmpty(frm) || BeanUtils.isEmpty(frm.getFormValue())){
                        frm = getInstFormByDefExt(defExt);
                    }
				}else if(BeanUtils.isEmpty(frm) || StringUtil.isEmpty(frm.getFormValue())){//已办的表单:节点表单>实例表单>全局表单
					frm=getInstFormByDefExt(defExt);
                    //没有全局表单，最后获取实例表单
                    if(BeanUtils.isEmpty(frm)){
                        frm = getGlobalFormByDefExt(defExt);
                    }
				}
            }
            if (frm != null){
            	if (StringUtil.isNotEmpty(formHelpFile)) {
            		frm.setHelpFile(formHelpFile);
				}
                FormModel formModel = getByForm(frm, instance);
                return formModel;
            }
        }
        //外部流程的情况。
        else{
			// 往上查询到最外层流程实例。
			BpmProcessInstance topInstance = bpmProcessInstanceManager.getTopBpmProcessInstance(instance);

			// 判断是否存在外部子流程的配置。
			String parentDefKey = topInstance.getProcDefKey();
			if (BeanUtils.isNotEmpty(bpmNodeDef)) {
				// 1,首先找外部子流程的节点表单
				frm = getSubForm(bpmNodeDef, parentDefKey);
				if (BeanUtils.isNotEmpty(frm) && StringUtil.isEmpty(formHelpFile)) {
					formHelpFile = frm.getHelpFile();
				}
			}
			// 2，找外部子流程的全局表单
			if (!isNotEmptyForm(frm) || StringUtil.isEmpty(formHelpFile)) {
				BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
				DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
				FormExt subGlobalForm = defExt.getGlobalFormByDefKey(parentDefKey, false);
				if (BeanUtils.isNotEmpty(subGlobalForm)) {
					if (!isNotEmptyForm(frm)) {
						frm = subGlobalForm;
					}
					if (StringUtil.isEmpty(formHelpFile)) {
						formHelpFile = frm.getHelpFile();
					}
				}
			}

			// 3,子流程全局表单为空，则找主流程的全局表单
			if (!isNotEmptyForm(frm) || StringUtil.isEmpty(formHelpFile)) {
				BpmNodeForm nodeFrm = getFormDefByParentFlowKey(topInstance.getProcDefId(), BpmConstants.LOCAL, false);
				if (BeanUtils.isNotEmpty(nodeFrm)) {
					if (!isNotEmptyForm(frm)) {
						frm = nodeFrm.getForm();
					}
					if (StringUtil.isEmpty(formHelpFile)) {
						formHelpFile = frm.getHelpFile();
					}
				}
			}

            FormModel formModel = getByForm(frm, topInstance);
            formModel.setHelpFile(formHelpFile);
            return formModel;
        }
        return null;
    }
//	public FormModel getByDefId(String defId, String nodeId, BpmProcessInstance instance) throws Exception{
//
//		BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
//		String useMainForm = "";
//		if(BeanUtils.isNotEmpty(bpmNodeDef)){
//			useMainForm = BpmUtil.getUseMainForm(bpmNodeDef);
//			if(StringUtil.isNotZeroEmpty(instance.getParentInstId())&&bpmNodeDef.getParentBpmNodeDef()==null){
//				BpmProcessInstance parentInstance = bpmProcessInstanceManager.get(instance.getParentInstId());
//				if(BeanUtils.isNotEmpty(parentInstance)){
//					BpmNodeDef parentBpmNodeDef = bpmDefinitionAccessor.getStartEvent(parentInstance.getProcDefId());
//					useMainForm = BpmUtil.getUseMainForm(parentBpmNodeDef);
//				}
//			}
//		}
//
//		if(StringUtil.isEmpty(useMainForm)){
//			useMainForm = BpmConstants.MAIN_VERSION;
//			String isUsedMainForm=PortalDataUtil.getPropertyByAlias("inst.useMainForm");
//			if(BeanUtils.isEmpty(isUsedMainForm)) isUsedMainForm="true";
//			if(!Boolean.parseBoolean(isUsedMainForm)){
//				useMainForm = BpmConstants.START_VERSION;
//			}
//		}
//		if(useMainForm.equals(BpmConstants.START_VERSION)){
//			// 获取流程启动时节点使用的表单
//			FormModel formModel = getInstanceNodeForm(instance, defId, nodeId);
//			if(BeanUtils.isNotEmpty(formModel)){
//				return formModel;
//			}
//		}
//
//
//
//		Form frm = null;
//		//属于主流程
//		if(StringUtil.isZeroEmpty(instance.getParentInstId())){
//			// 从节点配置获取。
//			frm = getFormDefByDefNode(defId, nodeId, instance);
//			if (frm != null){
//				FormModel formModel = getByForm(frm, instance);
//				return formModel;
//			}
//
//			// 获取全局表单。
////			if (frm == null){
////				BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
////				DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
////				frm=getGlobalFormByDefExt(defExt);
////			}
////			if (frm != null){
////				FormModel formModel = getByForm(frm, instance);
////				return formModel;
////			}
//
//			//获取流程启动时的表单版本
//			if(frm==null) {
//				FormModel formModel = getInstanceNodeForm(instance, defId, nodeId);
//				if(BeanUtils.isNotEmpty(formModel)){
//					return formModel;
//				}
//			}
//		}
//		//外部流程的情况。
//		else{
//			//往上查询到最外层流程实例。
//			//BpmProcessInstance topInstance=bpmProcessInstanceManager.getTopBpmProcessInstance(instance);
//
//			// 从节点配置获取。
//			frm = getFormDefByDefNode(defId, nodeId, instance);
//			if (frm != null){
//				FormModel formModel = getByForm(frm, instance);
//				return formModel;
//			}
//			//获取子流程表单，按优先级获取
//			if(frm==null) {
//				FormModel formModel = getSubForm(instance,defId);
//				if(BeanUtils.isNotEmpty(formModel)){
//					return formModel;
//				}
//			}
//
//
//
//			//判断是否存在外部子流程的配置。
//			/*String parentDefKey = topInstance.getProcDefKey();
//			frm =  getSubForm(bpmNodeDef,parentDefKey);
//
//			//通过父类key获取全局表单。
//			if(!isNotEmptyForm(frm)){
//				BpmNodeForm  nodeFrm = getFormDefByParentFlowKey(topInstance.getProcDefId(),BpmConstants.LOCAL,false);
//				if (nodeFrm != null){
//					frm = nodeFrm.getForm();
//				}
//			}
//
//			//存在则获取
//			if(isNotEmptyForm(frm))  {
//				FormModel formModel = getByForm(frm, topInstance);
//				return formModel;
//			}
//			//不存在则获取最外层表单的配置。
//			else{
//				BpmNodeForm  nodeForm = getFormDefByDefId(topInstance.getProcDefId());
//				if (nodeForm == null) return null;
//				FormModel formModel = getByForm(nodeForm.getForm(), topInstance);
//				return formModel;
//			}*/
//
//
//
//		}
//
//		return null;
//	}
	
	@Override
	public void handleInstForm(String instId, String defId,Boolean isSubFlow) throws Exception {
		List<BpmNodeDef> allBpmNodeDefs = bpmDefinitionAccessor.getAllNodeDef(defId);
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		if(!isSubFlow){
			BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
			DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
			Form frm=defExt.getGlobalForm();
			// 全局表单
			saveBpmInstForm(frm,instId, defId,null);
			
			// 全局手机表单
			frm=defExt.getGlobalMobileForm();
			saveBpmInstForm(frm,instId, defId,null);
			
			// 实例表单
			frm = defExt.getInstForm();
			saveBpmInstForm(frm,instId, defId,instId);
			
			// 实例手机表单
			frm = defExt.getInstMobileForm();
			saveBpmInstForm(frm,instId, defId,instId);
			
		}
	
		
		for (BpmNodeDef bpmNodeDef : allBpmNodeDefs) {
			if( bpmNodeDef instanceof CallActivityNodeDef ){
				BpmDefinition bpmDef = bpmDefinitionService.getBpmDefinitionByDefKey(((CallActivityNodeDef) bpmNodeDef).getFlowKey(), false);
				handleInstForm(instId,bpmDef.getDefId(),true);
				continue;
			}
			if(!isSubFlow){
				saveBpmInstForm(bpmNodeDef.getForm(),instId, defId,bpmNodeDef.getNodeId());
				saveBpmInstForm(bpmNodeDef.getMobileForm(),instId, defId,bpmNodeDef.getNodeId());
			}
			
			if(isSubFlow){
				saveBpmInstForm(bpmNodeDef.getSubForm(String.valueOf(cmd.getVariables().get(BpmConstants.BPM_FLOW_KEY)), FormType.MOBILE),instId, defId,bpmNodeDef.getNodeId());
				saveBpmInstForm(bpmNodeDef.getSubForm(String.valueOf(cmd.getVariables().get(BpmConstants.BPM_FLOW_KEY)), FormType.PC),instId, defId,bpmNodeDef.getNodeId());
			}
		}
	
	}
	
	
	private void saveBpmInstForm(Form form, String instId, String defId,
			String nodeId) throws ClientProtocolException, IOException {
		if(BeanUtils.isEmpty(form) || StringUtil.isEmpty(form.getFormValue()))return;
		BpmInstForm bpmInstForm = new BpmInstForm();
		bpmInstForm.setInstId(instId);
		bpmInstForm.setDefId(defId);
		bpmInstForm.setNodeId( nodeId);
		bpmInstForm.setFormCategory(form.getType().value());
		bpmInstForm.setFormType(form.getFormType());
		if(FormCategory.INNER.equals(form.getType())){
			String formKey = form.getFormValue();
			FormModel formModel = getFormByKey(formKey);
			if(BeanUtils.isNotEmpty(formModel)){
				bpmInstForm.setFormValue(formModel.getFormId());
			}
		}else{
			bpmInstForm.setFormValue(form.getFormValue());
		}
		if(StringUtil.isNotEmpty(bpmInstForm.getFormValue())){
			bpmInstFormManager.create(bpmInstForm);
		}
		
	}

	private String getUrl(BpmProcessInstance instance, String url){

		Map<String, String> map = new HashMap<String, String>();

		if (ActionCmd.DATA_MODE_PK.equals(instance.getDataMode())){
			map.put("pk", instance.getBizKey());
			map.put("instId", instance.getId());
		} 
		else{
			List<BpmBusLink> list = bpmBusLinkManager.getByInstId(instance.getId());
			for (BpmBusLink link : list){
				map.put(link.getFormIdentify(), link.getBusinesskeyStr());
			}
		}
		url = replaceStr(url, map);

		return url;
	}
	
	/**
	 * 替换字符串。
	 * 
	 * @param str
	 * @param map
	 * @return String
	 */
	private static String replaceStr(String str, Map<String, String> map){
		if (StringUtil.isEmpty(str)) 	return "";

		Matcher regexMatcher = regex.matcher(str);
		while (regexMatcher.find()){
			String key = regexMatcher.group(1);
			String toReplace = regexMatcher.group(0);
			String val = map.get(key);
			if (val == null) val="";
			str = str.replace(toReplace, val);
		}
		return str;

	}
	
	/**
	 * 替换地址。
	 * "{pk} ,{user}"等。
	 * @param str
	 * @return
	 */
	private static String replaceStr(String str){
		if (StringUtil.isEmpty(str)) 	return "";
		
		Matcher regexMatcher = regex.matcher(str);
		while (regexMatcher.find()){
			String toReplace = regexMatcher.group(0);
			str = str.replace(toReplace, "");
		}
		return str;

	}
	
	/**
     *  通过父类key获取全局表单。
     * @param defId
     * @param parentFlowKey
     * @param isMobile
     * @return
	 * @throws Exception 
     */
    private BpmNodeForm getFormDefByParentFlowKey(String defId,String parentFlowKey,boolean isMobile) throws Exception{
        BpmNodeForm nodeForm=new BpmNodeForm();
        BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
        // 开始节点
        BpmNodeDef bpmNodeDef = bpmProcessDef.getStartEvent();
        DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
        Form flowForm = null;
        if(BeanUtils.isNotEmpty(defExt) && BeanUtils.isNotEmpty(defExt.getAllGlobalForm())){
        	FormType formType=isMobile?FormType.MOBILE:FormType.PC;
        	for (FormExt form : defExt.getAllGlobalForm()) {
    			if(form.getParentFlowKey().equals(parentFlowKey)&&form.getFormType().equalsIgnoreCase(formType.toString())){
    				flowForm = form;
    				break;
    			}
    		}
        }
        if(isNotEmptyForm(flowForm)){
            nodeForm.setForm(flowForm);
            nodeForm.setBpmNodeDef(bpmNodeDef);
            return nodeForm;
        }
        
        return null;
    }
    
    /**
     * 获取子流程表单
     * 若子流程配置全局表单，则获取当前实例启动表单
     * 若没有配置全局表单，则获取父流程实例启动表单
     * @param instance
     * @param defId
     * @return
     */
    private FormModel getSubForm(BpmProcessInstance instance,String defId) {
    	
    	FormModel formModel = new BpmForm();
    	String instId=instance.getId();
    	BpmProcessInstance topInstance=bpmProcessInstanceManager.getTopBpmProcessInstance(instance);
    	String topInstId=topInstance.getId();
    	
		try {
			BpmInstForm bpmInstForm=bpmInstFormManager.getSubInstanFrom(instId, FormType.PC.value());
			//通过父类key获取全局表单
			BpmNodeForm nodeFrm = getFormDefByParentFlowKey(defId,topInstance.getProcDefKey(),false);//获取全局表单
			
			//当子流程没有配置全局表单时，获取父流程实例表单
	    	if(nodeFrm==null) {
	    		bpmInstForm=bpmInstFormManager.getSubInstanFrom(topInstId, FormType.PC.value());
	    	}
	    	
	    	//转换表单
	    	String formCategory = bpmInstForm.getFormCategory();
	    	if(FormCategory.INNER.equals(FormCategory.fromValue(formCategory))){
				FormFeignService formService=AppUtil.getBean(FormFeignService.class);
				ObjectNode objectNode = formService.getByFormId(bpmInstForm.getFormValue()); 
				formModel = JsonUtil.toBean(objectNode, BpmForm.class);
				formModel.setType(FormCategory.INNER);
			}else{
				formModel.setType(FormCategory.FRAME);
				// bpmInstForm.getFormValue();
				formModel.setFormValue(bpmInstForm.getFormValue());
			}
			formModel = handForm(formModel, instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return formModel;
    }
    
}
