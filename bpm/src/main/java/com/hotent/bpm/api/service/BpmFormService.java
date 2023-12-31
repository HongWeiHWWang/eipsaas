package com.hotent.bpm.api.service;



import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeForm;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.form.FormModel;

/**
 * 流程表单获取接口。
 * <pre>
 *  主要的功能是:
 *  1.启动流程的表单。
 *  2.实例的表单。
 *  3.审批任务的表单。
 * </pre>
 * @author csx
 *
 */
public interface BpmFormService   {
	
	/**
	 * 获取流程节点的表单定义。
	 * <pre>
	 * 1.判断流程实例是否有父实例。即判断当前流程是否子流程。
	 * 2.如果是则取流程定义父表单的设置。
	 * 3.否则取当前的配置。
	 * </pre>
	 * @param defId
	 * @param nodeId
	 * @return 
	 * Form
	 * @throws Exception 
	 */
	Form getFormDefByDefNode(String defId,String nodeId,BpmProcessInstance instance) throws Exception;
	
	
	/**
	 * 获取某个流程的发起表单。
	 * <pre>
	 * 	1.获取开始节点的表单。
	 *  2.获取第一个节点的表单。
	 *  3.获取全局表单。
	 * </pre>
	 * @param defId
	 * @return FormModel
	 * @throws Exception 
	 */
	BpmNodeForm getByDefId(String defId) throws Exception;
	
	
	/**
	 * 根据草稿获取表单。
	 * <pre>
	 * 	1.获取发起节点的表单。
	 * 	2.获取表单将pk进行替换。
	 * </pre>
	 * @param instance
	 * @return  BpmNodeForm
	 * @throws Exception 
	 */
	BpmNodeForm getByDraft(BpmProcessInstance instance) throws Exception;
	
	
	/**
	 * 审批流程获取审批表单。
	 * <pre>
	 * 	1.获取运行时表单。
	 * 	2.获取不到则获取当前节点配置的表单。
	 * 	3.获取不到则获取全局表单。
	 * </pre>
	 * @param defId
	 * @param nodeId
	 * @param instance
	 * @param isTodoForm 是否获取待办的表单
	 * @return FormModel
	 * @throws Exception 
	 */
	FormModel getByDefId(String defId,String nodeId,BpmProcessInstance instancem,boolean isTodoForm) throws Exception;
	
	/**
	 * 获取流程实例审批的表单
	 * @param instId
	 * @param defId
	 * @param nodeId
	 * @return
	 * @throws Exception 
	 */
	FormModel getInstanceNodeForm(BpmProcessInstance instance, String defId, String nodeId) throws Exception;
	
	
	/**
	 * 流程启动时， 记录流程实例节点表单
	 * @param instId
	 * @param defId
	 * @throws Exception 
	 */
	void handleInstForm(String instId,String defId,Boolean isSubFlow) throws Exception;
	
	
	/**
	 * 获取流程业务表单。 
	 * <pre>
	 * 1.在运行时获取业务表单。
	 * 2.获取流程定义的业务表单。
	 * </pre>
	 * @param	instance流程实例
	 * @return  FormModel
	 * @throws Exception 
	 */
	FormModel getInstFormByDefId(BpmProcessInstance instance) throws Exception;


}
