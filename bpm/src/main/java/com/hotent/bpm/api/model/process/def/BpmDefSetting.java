package com.hotent.bpm.api.model.process.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.model.form.Form;

/**
 * 流程定义配置对象类。
 * 配置关于表单，节点表单，节点属性。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-8-19-上午11:48:35
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmDefSetting {
	/**
	 * 全局表单
	 */
	private FormExt globalForm;
	
	/**
	 * 全局手机表单
	 */
	private FormExt globalMobileForm;
	
	/**
	 * 实例表单
	 */
	private FormExt instForm;
	
	/**
	 * 手机实例表单
	 */
	private FormExt instMobileForm;
	
	/**
	 * 父流程key。
	 */
	private String parentDefKey="";
	
	/**
	 * 节点表单
	 */
	private List<Form> nodeForms = new ArrayList<Form>();
	
	/**
	 * 子表单权限
	 */
	private List<BpmSubTableRight> bpmSubTableRights=new ArrayList<BpmSubTableRight>();
	
	/**
	 * 节点属性。
	 */
	private List<NodeProperties> nodeProperties=new ArrayList<NodeProperties>();
	
	Map<String,NodeProperties> nodePropertieMap = new HashMap<String,NodeProperties>();
	/**
	 * 全局restful接口事件
	 */
	private List<Restful> globalRestfuls = new ArrayList<Restful>();
	
	public FormExt getGlobalForm() {
		if(BeanUtils.isEmpty(globalForm)) return globalForm;
		String parentKey=getParentDefKey();
		globalForm.setParentFlowKey(parentKey);
		return globalForm;
	}

	public void setGlobalForm(FormExt globalForm) {
		this.globalForm = globalForm;
	}
	public FormExt getGlobalMobileForm() {
		if(BeanUtils.isEmpty(globalMobileForm)) return globalMobileForm;
		String parentKey=getParentDefKey();
		globalMobileForm.setParentFlowKey(parentKey);
		return globalMobileForm;
	}

	public void setGlobalMobileForm(FormExt globalMobileForm) {
		this.globalMobileForm = globalMobileForm;
	}

	public FormExt getInstForm() {
		if(BeanUtils.isEmpty(instForm)) return instForm;
		String parentKey=getParentDefKey();
		instForm.setParentFlowKey(parentKey);
		return instForm;
	}

	public void setInstForm(FormExt instForm) {
		this.instForm = instForm;
	}

	public FormExt getInstMobileForm() {
		if(BeanUtils.isEmpty(instMobileForm)) return instMobileForm;
		String parentKey=getParentDefKey();
		instMobileForm.setParentFlowKey(parentKey);
		return instMobileForm;
	}
	
	public void setInstMobileForm(FormExt instMobileForm) {
		this.instMobileForm = instMobileForm;
	}

	public String getParentDefKey() {
		if(StringUtil.isEmpty(this.parentDefKey))
			return BpmConstants.LOCAL;
		return parentDefKey;
	}

	public void setParentDefKey(String parentDefKey) {
		this.parentDefKey = parentDefKey;
	}

	public List<Form> getNodeForms() {
		if(BeanUtils.isEmpty(this.nodeForms)) return this.nodeForms;
		String parentKey=getParentDefKey();
		for(Form frm:this.nodeForms){
			frm.setParentFlowKey(parentKey);
		}
		return nodeForms;
	}
	
	public Map<String,Form> getFormMap(boolean isPc) {
		List<Form> forms= getNodeForms();
		Map<String,Form> map=new HashMap<String, Form>();
		if(BeanUtils.isEmpty(forms))return map;
		
		String formType=isPc? FormType.PC.value() : FormType.MOBILE.value();
		
		for(Form item:forms){
			if(formType.equals(item.getFormType())) {
				map.put(item.getNodeId(), item);
			}
		}
		
		
		return map;
	}
	
	public Map<String,Form> getFormMap() {
		return getFormMap(true);
	}
	public Map<String,Form> getMobileFormMap() {
		return getFormMap(false);
	}
	
	
	public void setNodeForms(List<Form> nodeForms) {
		this.nodeForms = nodeForms;
	}

	public List<NodeProperties> getNodeProperties() {
		String parentKey=getParentDefKey();
		for(NodeProperties prop:this.nodeProperties){
			prop.setParentDefKey(parentKey);
		}
		return nodeProperties;
	}

	public void setNodeProperties(List<NodeProperties> nodeProperties) {
		 this.nodeProperties = nodeProperties;
		 for(String key:nodePropertieMap.keySet()){
				nodeProperties.add(nodePropertieMap.get(key));
		 }
	}
	
	public Map<String,NodeProperties> getNodePropertieMap() {
		List<NodeProperties> tmpList= getNodeProperties();
		Map<String,NodeProperties> map=new HashMap<String, NodeProperties>();
		for(NodeProperties item:tmpList){
			map.put(item.getNodeId(),item);
		}
		return map;
	}
	
	public void setNodePropertieMap(Map<String,NodeProperties> map){
		this.nodePropertieMap = map;
		 for(String key:nodePropertieMap.keySet()){
				nodeProperties.add(nodePropertieMap.get(key));
		 }
	}

	public List<BpmSubTableRight> getBpmSubTableRights() {
		return bpmSubTableRights;
	}

	public void setBpmSubTableRights(List<BpmSubTableRight> bpmSubTableRights) {
		
		this.bpmSubTableRights = bpmSubTableRights;
	}
	
	public Map<String,List<BpmSubTableRight>> getBpmSubTableRightMap() {
		Map<String,List<BpmSubTableRight>> map=new HashMap<String, List<BpmSubTableRight>>();
		for(BpmSubTableRight item:bpmSubTableRights){
			if(map.containsKey(item.getNodeId())){
				List<BpmSubTableRight> list=map.get(item.getNodeId());
				list.add(item);
			}
			else{
				List<BpmSubTableRight> list=new ArrayList<BpmSubTableRight>();
				list.add(item);
				map.put(item.getNodeId(),list);
			}
		}
		return map;
	}

	public void setMobileFormMap(Map<String,Form> formMap){
		for (String key : formMap.keySet()) {
			Form form = formMap.get(key);
			if(StringUtil.isNotEmpty(form.getFormValue()))
				nodeForms.add(form);
		}
	}
	
	public void setFormMap(Map<String,Form> formMap){
		for (String key : formMap.keySet()) {
			Form form = formMap.get(key);
			if(StringUtil.isNotEmpty(form.getFormValue()) || StringUtil.isNotEmpty(form.getHelpFile()))
				nodeForms.add(form);
		}
	}

	public List<Restful> getGlobalRestfuls() {
		String parentKey=getParentDefKey();
		for(Restful prop:this.globalRestfuls){
			prop.setParentDefKey(parentKey);
		}
		return globalRestfuls;
	}

	public void setGlobalRestfuls(List<Restful> globalRestfuls) {
		this.globalRestfuls = globalRestfuls;
	}

	
}
