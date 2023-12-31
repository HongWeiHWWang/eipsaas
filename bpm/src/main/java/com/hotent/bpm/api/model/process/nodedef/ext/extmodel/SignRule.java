package com.hotent.bpm.api.model.process.nodedef.ext.extmodel;

import java.io.Serializable;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.constant.DecideType;
import com.hotent.bpm.api.constant.FollowMode;
import com.hotent.bpm.api.constant.VoteType;

/**
 * 会签规则。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-14-上午9:12:36
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class SignRule implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 决策类型。
	 * 同意，拒绝。
	 */
	private DecideType decideType=DecideType.REFUSE;
	/**
	 * 投票类型
	 * 票数，百分比。
	 */
	private VoteType voteType=VoteType.AMOUNT;
	
	/**
	 * 后续处理模式。
	 */
	private FollowMode followMode=FollowMode.COMPLETE;
	
	/**
	 * 票数。
	 */
	private int voteAmount=1;
	
	public SignRule(){
		
	}
	
	public SignRule(DecideType decideType,VoteType voteType,FollowMode followMode,int voteAmount){
		this.decideType=decideType;
		this.voteType=voteType;
		this.followMode=followMode;
		this.voteAmount=voteAmount;
	}
	
	
	public DecideType getDecideType() {
		return decideType;
	}
	public void setDecideType(DecideType decideType) {
		this.decideType = decideType;
	}
	public VoteType getVoteType() {
		return voteType;
	}
	public void setVoteType(VoteType voteType) {
		this.voteType = voteType;
	}
	public FollowMode getFollowMode() {
		return followMode;
	}
	public void setFollowMode(FollowMode followMode) {
		this.followMode = followMode;
	}
	public int getVoteAmount() {
		return voteAmount;
	}
	public void setVoteAmount(int voteAmount) {
		this.voteAmount = voteAmount;
	}

	public static ObjectNode toJson(SignRule signRule) {
		ObjectNode json =  JsonUtil.getMapper().createObjectNode();
		if(signRule == null) return json;
		
		json.put("voteAmount", signRule.getVoteAmount());
		json.put("followMode", signRule.getFollowMode().getKey());
		json.put("voteType", signRule.getVoteType().getKey());
		json.put("decideType", signRule.getDecideType().getKey());
		return json;
	}
}
