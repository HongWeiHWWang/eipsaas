package com.hotent.bpm.persistence.model.nodehandler;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.DecideType;
import com.hotent.bpm.api.constant.FollowMode;
import com.hotent.bpm.api.constant.PrivilegeMode;
import com.hotent.bpm.api.constant.VoteType;
import com.hotent.bpm.api.model.process.nodedef.ext.BaseBpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.PrivilegeItem;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.SignRule;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.defxml.entity.ext.SignNode;
import com.hotent.bpm.defxml.entity.ext.SignNode.SignSetting;
import com.hotent.bpm.defxml.entity.ext.SignNode.SignSetting.Privilege.Item;
import com.hotent.bpm.plugin.core.util.UserAssignRuleParser;

/**
 * 会签节点的解析。
 * <pre> 
 * 1.这个解析继承了用户任务的基础部分定义。
 * 2.解析了流程定义关于会签规则和特权部分。
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-30-下午2:43:56
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class SignTaskHandler extends UserTaskHandler {
	
	@Override
	public void handNode(BaseBpmNodeDef nodeDef,Object baseNode) throws Exception {
		super.handNode(nodeDef,baseNode);
		
		SignNodeDef signNodeDef=(SignNodeDef)nodeDef;
		SignNode signNode=(SignNode)baseNode;
		//处理会签
		handSignRule(signNodeDef,signNode);
	}
	
	private void handSignRule(SignNodeDef signNodeDef,SignNode signNode){
		SignSetting setting= signNode.getSignSetting();
		//处理会签规则
		handRule(signNodeDef,setting);
		//处理特权人
		handPrivilege(signNodeDef,setting);
	}
	
	/**
	 * 处理会签规则。
	 * @param signNodeDef
	 * @param setting 
	 * void
	 */
	private void handRule(SignNodeDef signNodeDef,SignSetting setting){
		com.hotent.bpm.defxml.entity.ext.SignNode.SignSetting.SignRule signRule= setting.getSignRule();
		
		DecideType decideType=DecideType.fromKey( signRule.getDecideType().value());
		VoteType voteType=VoteType.fromKey(signRule.getVoteType().value());
		FollowMode flowMode=FollowMode.fromKey(signRule.getFollowMode().value());
		
		SignRule sign=new SignRule(decideType,
						voteType, flowMode, signRule.getVoteAmount());
		
		signNodeDef.setSignRule(sign);
	}
	
	/**
	 * 处理会签特权。
	 * @param signNodeDef
	 * @param setting 
	 * void
	 */
	private void handPrivilege(SignNodeDef signNodeDef,SignSetting setting){
		if(setting.getPrivilege()==null) return;

		List<Item> items= setting.getPrivilege().getItem();
		
		if(BeanUtils.isEmpty(items)) return;
		//执行特权。
		List<PrivilegeItem> privilegeList=new ArrayList<PrivilegeItem>();
			
		for(Item item:items){
			PrivilegeItem priItem=new PrivilegeItem();
			priItem.setPrivilegeMode(PrivilegeMode.fromKey(item.getMode().value()));
			Object obj= item.getMembers();
			if(obj instanceof Element){
				handPrivilegeItem(priItem,(Element) obj);
			}
			privilegeList.add(priItem);
		}
		//设置权限人员。
		signNodeDef.setPrivilegeList(privilegeList);
	}
	
	/**
	 * 处理会签特权项。
	 * @param priItem
	 * @param elMembers 
	 * void
	 */
	private void handPrivilegeItem(PrivilegeItem priItem,Element elMembers){
		if(elMembers.getChildNodes()==null || elMembers.getChildNodes().getLength()==0) return;
		List<UserAssignRule> assignRules= UserAssignRuleParser.parse(elMembers);
		priItem.setUserRuleList(assignRules);
	}
	
	

}
