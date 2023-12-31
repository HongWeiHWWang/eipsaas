package com.hotent.bpm.listener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.hotent.base.feign.FormFeignService;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.StartFlowEvent;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeForm;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.engine.inst.DefaultProcessInstCmd;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.model.form.BpmForm;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.form.FormCategory;
import com.hotent.bpm.model.form.FormModel;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmInstFormManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.BpmInstForm;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 监听并处理流程发起事件。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-26-上午9:14:47
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service(value="startFlowEventListener")
public class StartFlowEventListener implements ApplicationListener<StartFlowEvent>,Ordered {

	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager  ;
	@Resource
	BpmDefinitionManager  bpmDefinitionManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmInstService bpmInstService;
	@Resource
	BpmInstFormManager bpmInstFormManager;
	@Resource
	protected FormFeignService formService;
	
	@Override
	public void onApplicationEvent(StartFlowEvent ev) {
		BpmDelegateExecution execution=(BpmDelegateExecution)ev.getSource();
		String currentProcInstId=(String)execution.getVariable(BpmConstants.PROCESS_INST_ID);
		//流程通讯变量，这个在外部子流程CallSubProcessStartListener中进行传入。
		Map<String, Object> commuVars=ContextThreadUtil.getCommuVars();
		ActionCmd cmd= ContextThreadUtil.getActionCmd();
		//一般的流程
		if(commuVars.isEmpty()){
			//创建发起流程的审批意见
			createOpinion(execution, currentProcInstId, null);
		}
		//子流程的情况。
		else{
			String parentProcInstId=(String)commuVars.get(BpmConstants.PROCESS_INST_ID);
			//获取父的流程实例ID
			BpmProcessInstance parentInstance=(BpmProcessInstance) cmd.getTransitVars(BpmConstants.PROCESS_INST);
			
			BpmProcessInstance topInstance=(BpmProcessInstance)bpmProcessInstanceManager.getTopBpmProcessInstance(parentInstance);
            String subject=(String) commuVars.get(BpmConstants.SUBJECT);
            if(StringUtil.isEmpty(subject)){
                subject = cmd.getVariables().get("subject_").toString();
            }
			//产生BPMPROCESSINSTANCE,
			DefaultBpmProcessInstance instance= createInstance(execution,commuVars,parentInstance,subject);
			
			//子流程启动添加实例表单
			String instId=instance.getId();
			String defId=instance.getProcDefId();
			Form frm = null;
			try {
				//通过父类key获取全局表单
				BpmNodeForm nodeFrm = getFormDefByParentFlowKey(defId,topInstance.getProcDefKey(),false);//获取全局表单
				if (nodeFrm != null){
					frm = nodeFrm.getForm();
					saveBpmInstForm(frm,instId,defId,null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//子流程转换cmd。
			converCmd(parentProcInstId,instance);
			
			commuVars.put(BpmConstants.PROCESS_PARENT_INST_ID, parentProcInstId);
			commuVars.put(BpmConstants.PROCESS_INST_ID, instance.getId());
			commuVars.put(BpmConstants.PROCESS_DEF_ID, instance.getProcDefId());
			//传递流程变量。
			commuVars.put(BpmConstants.BPM_FLOW_KEY, topInstance.getProcDefKey());

			//设置流程变量
			execution.setVariables(commuVars);
			//添加流程意见
			createOpinion(execution, instance.getId(), parentProcInstId);	
			
		}
		
	}
	
	
	
	private void converCmd(String parentProcInstId,BpmProcessInstance instance){
		
	
		BaseActionCmd baseCmd=(BaseActionCmd)ContextThreadUtil.getActionCmd();
		
		DefaultTaskFinishCmd cmd=new DefaultTaskFinishCmd();
		cmd.setInstId(instance.getId());
		
		cmd.setActionName(OpinionStatus.AGREE.getKey());
		
		cmd.setBpmIdentities(baseCmd.getBpmIdentities());
		cmd.putTransitVars(baseCmd.getTransitVars());
		cmd.addTransitVars(BpmConstants.PROCESS_INST, instance);
		
		ContextThreadUtil.setActionCmd(cmd);
	}
	
	
	
	private DefaultBpmProcessInstance createInstance(BpmDelegateExecution execution ,Map<String, Object> commuVars,BpmProcessInstance parentInstance,String subject){
		String businessKey=(String) commuVars.get(BpmConstants.BUSINESS_KEY);
		String parentProcInstId=(String)commuVars.get(BpmConstants.PROCESS_INST_ID);
		//String subject=(String) commuVars.get(BpmConstants.SUBJECT);

		String bpmnDefId=execution.getBpmnDefId();
		
		String  defId=  bpmDefinitionManager.getDefIdByBpmnDefId(bpmnDefId);
		DefaultBpmDefinition bpmDefinition =bpmDefinitionManager.getById(defId);
		
		DefaultBpmProcessInstance instance=new DefaultBpmProcessInstance();
		instance.setId(UniqueIdUtil.getSuid());
		instance.setParentInstId(parentProcInstId);
		instance.setProcDefId(bpmDefinition.getDefId());
		instance.setProcDefKey(bpmDefinition.getDefKey());
		instance.setBpmnDefId(bpmDefinition.getBpmnDefId());
		instance.setProcDefName(bpmDefinition.getName());
		//数据处理模式
		instance.setDataMode(parentInstance.getDataMode());
		
		if(execution.getSupperExecution()!=null){
		   String superNodeId= execution.getSupperExecution().getNodeId();
		   instance.setSuperNodeId(superNodeId);
		}
		instance.setBpmnInstId(execution.getBpmnInstId());
		instance.setBizKey(businessKey);
		if(BeanUtils.isNotEmpty(commuVars.get(BpmConstants.SYS_CODE))){
			instance.setSysCode((String)commuVars.get(BpmConstants.SYS_CODE));
		}
		if(BpmDefinition.TEST_STATUS.RUN.equals(bpmDefinition.getTestStatus())){
			instance.setIsFormmal(BpmProcessInstance.FORMAL_YES);
		}
		
		//按照外部子流程的标题规则，生成子流程的标题
		try {
			ActionCmd subCmd= ContextThreadUtil.getActionCmd();
			DefaultProcessInstCmd cInstCmd =new DefaultProcessInstCmd();
			cInstCmd.setFlowKey(instance.getProcDefKey());
			cInstCmd.setBusData(subCmd.getBusData());
			cInstCmd.setVariables(subCmd.getVariables());
			cInstCmd.putTransitVars(subCmd.getTransitVars());
			cInstCmd.setInstId(instance.getId());
			cInstCmd.setBusinessKey(instance.getBizKey());
			
			BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(instance.getProcDefId());
		    subject = bpmProcessInstanceManager.getSubject(bpmProcessDef,cInstCmd , instance);
			instance.setSubject(subject);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		int supportMobile = bpmDefinition.getSupportMobile();
		//是否支持移动端：如果父流程实例存在，则取父流程
		if(BeanUtils.isNotEmpty(parentInstance)){
			DefaultBpmProcessInstance pInstance = bpmProcessInstanceManager.get(parentInstance.getId());
			if(BeanUtils.isNotEmpty(pInstance)){
				supportMobile = pInstance.getSupportMobile();
			}
		}
		instance.setSupportMobile(supportMobile);
		IUser currentUser=ContextUtil.getCurrentUser();
		//设置创建用户ID
		instance.setCreateBy(currentUser.getUserId());
		instance.setCreator(currentUser.getFullname());
		instance.setCreateTime(LocalDateTime.now());
		
		instance.setStatus(ProcessInstanceStatus.STATUS_RUNNING.getKey());
		
		bpmProcessInstanceManager.create(instance);
		
		return instance;
		
	}
	
	/**
	 * 在流程发起时记录在流程意见表中记录提交人信息。
	 * @param execution				
	 * @param procInstId
	 * @param parentProcInstId 
	 * void
	 */
	private void createOpinion(BpmDelegateExecution execution,String procInstId,String parentProcInstId){
		DefaultBpmCheckOpinion bpmCheckOpinion = BpmCheckOpinionUtil.buildBpmCheckOpinion(execution, procInstId,false);
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		bpmCheckOpinion.setStatus(OpinionStatus.START.getKey());
		bpmCheckOpinion.setOpinion("发起流程");
		bpmCheckOpinion.setSupInstId(parentProcInstId);
		if (StringUtil.isNotEmpty(cmd.getBusData())) {
			try {
				bpmCheckOpinion.setFormData(Base64.getBase64(cmd.getBusData()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (StringUtil.isNotEmpty(cmd.getAgentLeaderId()) && !"0".equals(cmd.getAgentLeaderId())) {
			bpmCheckOpinion.setAgentLeaderId(cmd.getAgentLeaderId());
		}
		bpmCheckOpinionManager.create(bpmCheckOpinion);
	}
	
	
	

	@Override
	public int getOrder() {
		return 1;
	}
	
	private void saveBpmInstForm(Form form, String instId, String defId,
			String nodeId) throws ClientProtocolException, IOException {
		if(BeanUtils.isEmpty(form))return;
		BpmInstForm bpmInstForm = new BpmInstForm();
		bpmInstForm.setInstId(instId);
		bpmInstForm.setDefId(defId);
		bpmInstForm.setNodeId(nodeId);
		bpmInstForm.setFormCategory(form.getType().value());
		bpmInstForm.setFormType(form.getFormType());
		if(FormCategory.INNER.equals(form.getType())){
			FormModel formModel = JsonUtil.toBean( formService.getByFormKey(form.getFormValue()), BpmForm.class);
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
	 * 表单是否为空。
	 * @param form
	 * @return
	 */
	protected boolean isNotEmptyForm(Form form){
		if(form==null) return false;
		return !form.isFormEmpty();
	}
	
}
