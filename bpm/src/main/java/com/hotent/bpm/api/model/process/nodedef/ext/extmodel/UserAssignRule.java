package com.hotent.bpm.api.model.process.nodedef.ext.extmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.plugin.core.context.UserCalcPluginContext;

/**
 * 用户分配规则定义。
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-11-下午5:41:01
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class UserAssignRule implements Comparable<UserAssignRule>,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//规则名称
	private String name="";
	//规则描述
	private String description="";
	//规则条件
	private String condition="";
	//条件模式(设计器，直接填写脚本)
	private String conditionMode="";
	//父流程定义Key
	private String parentFlowKey="";
	//分组编号。
	private int groupNo=1;
    //类型 user：节点审批人员 copyTo：节点传阅人员
    private String type="user";
	//人员计算规则
	@JsonProperty("calcs")
	private List<UserCalcPluginContext> calcPluginContextList=new ArrayList<UserCalcPluginContext>();
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		if(StringUtil.isEmpty(description)){
			String desc ="";
			for (UserCalcPluginContext ctx : calcPluginContextList) 
				desc =desc + "　　　【"+ctx.getTitle()+"】"+ctx.getDescription()+";";
			 
			return desc;
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	
	
	public String getConditionMode() {
		return conditionMode;
	}

	public void setConditionMode(String conditionMode) {
		this.conditionMode = conditionMode;
	}

	public int getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}

	public List<UserCalcPluginContext> getCalcPluginContextList() {
		return calcPluginContextList;
	}

	public void setCalcPluginContextList(
			List<UserCalcPluginContext> calcPluginContextList) {
		this.calcPluginContextList = calcPluginContextList;
	}

	@Override
	public int compareTo(UserAssignRule userRule) {
		if(this.groupNo>userRule.groupNo) return 1;
		if(this.groupNo<userRule.groupNo) return -1;
		return 0;
	}

	public String getParentFlowKey() {
		return parentFlowKey;
	}

	public void setParentFlowKey(String parentFlowKey) {
		this.parentFlowKey = parentFlowKey;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
