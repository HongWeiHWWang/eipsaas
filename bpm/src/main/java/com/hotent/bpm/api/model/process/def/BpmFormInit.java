package com.hotent.bpm.api.model.process.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;

public class BpmFormInit {
	
	private String parentDefKey="";
	
	
	/**
	 * 表单初始化项。
	 */
	private List<FormInitItem> formInitItems=new ArrayList<FormInitItem>();
	
	
	public String getParentDefKey() {
		if(StringUtil.isEmpty(parentDefKey)){
			return BpmConstants.LOCAL;
		}
		return parentDefKey;
	}

	public void setParentDefKey(String parentDefKey) {
		this.parentDefKey = parentDefKey;
	}

	public List<FormInitItem> getFormInitItems() {
		return formInitItems;
	}
	
	/**
	 * 表单数据初始化Map。
	 * @return 
	 * Map<String,FormInitItem>
	 */
	public Map<String,FormInitItem> getFormInitItemMap(){
		Map<String,FormInitItem> map=new HashMap<String, FormInitItem>();
		for(FormInitItem item:formInitItems){
			map.put(item.getNodeId(),item);
		}
		return map;
	}
	
	public void setFormInitItems(List<FormInitItem> formInitItems) {
		if(StringUtil.isNotEmpty(this.parentDefKey)){
			for(FormInitItem prop:this.formInitItems){
				prop.setParentDefKey(parentDefKey);
			}
		}
		this.formInitItems = formInitItems;
	}
	
	public BpmFormInit addFormInitItem(FormInitItem item) {
		formInitItems.add(item);
		return this;
	}
	
	public BpmFormInit addFormInitItems(List<FormInitItem> items) {
		formInitItems.addAll(items);
		return this;
	}

}
