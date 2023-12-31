package com.hotent.bpm.api.model.process.def;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;

/**
 * 表单初始话设定项。
 * 一般一个节点只会有一项。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014年8月22日-上午10:09:48
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class FormInitItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2463498740809410997L;

	/**
	 * 节点ID。
	 */
	private String nodeId="";

	/**
	 * 父流程定义KEY
	 */
	private String parentDefKey="";
	
	/**
	 * 显示字段配置。
	 */
	private List<FieldInitSetting> showFieldsSetting=new ArrayList<FieldInitSetting>();
	
	/**
	 * 保存字段配置。
	 */
	private List<FieldInitSetting> saveFieldsSetting=new ArrayList<FieldInitSetting>();

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParentDefKey() {
		if(StringUtil.isEmpty(this.parentDefKey)){
			return BpmConstants.LOCAL;
		}
		return parentDefKey;
	}

	public void setParentDefKey(String parentDefKey) {
		this.parentDefKey = parentDefKey;
	}

	public List<FieldInitSetting> getShowFieldsSetting() {
		return showFieldsSetting;
	}

	public void setShowFieldsSetting(List<FieldInitSetting> showFieldsSetting) {
		this.showFieldsSetting = showFieldsSetting;
	}

	public List<FieldInitSetting> getSaveFieldsSetting() {
		return saveFieldsSetting;
	}

	public void setSaveFieldsSetting(List<FieldInitSetting> saveFieldsSetting) {
		this.saveFieldsSetting = saveFieldsSetting;
	}
	
	public void addSaveSetting(FieldInitSetting setting){
		this.saveFieldsSetting.add(setting);
	}
	
	public void addShowFieldsSetting(FieldInitSetting setting){
		this.showFieldsSetting.add(setting);
	}
	
}
