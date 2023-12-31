package com.hotent.bpm.api.model.process.nodedef.ext;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmSubTableRight;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.nodedef.MultiInstanceDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.DefaultJumpRule;


/**
 * 用户任务节扩展定义。
 * 
 * @author yongguo
 *
 */
public class UserTaskNodeDef extends BaseBpmNodeDef implements MultiInstanceDef {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3495303503741341818L;

	/**
	 * 跳转规则列表。
	 * 备注：如果在会签节点定义了这个规则，那么完成时可以跳转到某个节点。
	 */
	private List<DefaultJumpRule> jumpRuleList;
	
	private List<BpmVariableDef> variableList;
	
	
	
	
	/**
	 * 子表权限。
	 */
	private List<BpmSubTableRight> bpmSubTableRights=new ArrayList<BpmSubTableRight>();

	

	public List<DefaultJumpRule> getJumpRuleList() {
		return jumpRuleList;
	}

	public void setJumpRuleList(List<DefaultJumpRule> jumpRuleList) {
		this.jumpRuleList = jumpRuleList;
	}
	
	

	public List<BpmVariableDef> getVariableList() {
		return variableList;
	}

	public void setVariableList(List<BpmVariableDef> variableList) {
		this.variableList = variableList;
	}
	
	

	

	@Override
	public boolean supportMuliInstance() {
		return false;
	}

	@Override
	public boolean isParallel() {
		
		return false;
	}

	@Override
	public void setSupportMuliInstance(boolean support) {
		
	}

	@Override
	public void setParallel(boolean isParallel) {
		
		
	}

	

	public List<BpmSubTableRight> getBpmSubTableRights() {
		return bpmSubTableRights;
	}

	public void setBpmSubTableRights(List<BpmSubTableRight> bpmSubTableRights) {
		this.bpmSubTableRights = bpmSubTableRights;
	}

	
	public void addBpmSubTableRight(BpmSubTableRight tableRight) {
		bpmSubTableRights.add(tableRight);
	}
	
	/**
	 * 根据父key获取子表表单权限
	 * @param parentDefKey
	 * @return  BpmSubTableRight
	 */
	public List<BpmSubTableRight> getBpmSubTableRightByParentDefKey(String parentDefKey){
		if(StringUtil.isEmpty(parentDefKey)){
			parentDefKey=BpmConstants.LOCAL;
		}
		List<BpmSubTableRight> list = new ArrayList<BpmSubTableRight>();
		for(BpmSubTableRight right:this.bpmSubTableRights){
			if(StringUtil.isNotEmpty(right.getParentDefKey()) && right.getParentDefKey().equals(parentDefKey)){
				list.add(right);
			}
		}
		return list;
	}

}
