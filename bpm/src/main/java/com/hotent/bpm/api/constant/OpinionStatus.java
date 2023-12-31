package com.hotent.bpm.api.constant;
/**
 * 审批状态
 * <pre>
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-1-下午5:54:18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 *
 */
public enum OpinionStatus {
	/**
	 * 提交
	 */
	START("start","提交"),

	/**
	 * 结束
	 */
	END("end","结束"),

    /**
     * 加签
     */
    ADDSIGN("addsign","加签"),

	/**
	 * 待审批
	 */
	AWAITING_CHECK("awaiting_check","待审批"),
	/**
	 * 发起沟通
	 */
	START_COMMU("start_commu","发起沟通"),
	/**
	 * 待反馈
	 */
	AWAITING_FEEDBACK("awaiting_feedback","待反馈"),
	/**
	 * 待反馈
	 */
	FEEDBACK("feedback","已反馈"),
	/**
	 * 同意
	 */
	AGREE("agree","同意"),
	/**
	 * 反对
	 */
	OPPOSE("oppose","反对"),
	/**
	 * 弃权
	 */
	ABANDON("abandon","弃权"),
	/**
	 * 驳回
	 */
	REJECT("reject","驳回"),
	/**
	 * 驳回到发起人
	 */
	BACK_TO_START("backToStart","驳回到发起人"),
	/**
	 * 重新提交
	 */
	RE_SUBMIT("reSubmit","重新提交"),
	/**
	 * 撤回
	 */
	REVOKER("revoker","撤回"),
	/**
	 * 撤销流转
	 */
	TRANS_REVOKER("transRevoker","撤销流转"),
	/**
	 * 撤销到发起人
	 */
	REVOKER_TO_START("revoker_to_start","撤回到发起人"),
	/**
	 * 会签通过
	 */
	SIGN_PASSED("signPass","会签通过"),
	/**
	 * 会签不通过
	 */
	SIGN_NOT_PASSED("signNotPass","会签不通过"),
	/**
	 * 驳回取消
	 */
	SIGN_BACK_CANCEL("signBackCancel","驳回取消"),
	/**
	 * 被撤回
	 */
	SIGN_RECOVER_CANCEL("signRecoverCancel","被撤回"),
	/**
	 * 任务被撤销
	 */
	RETRACTED("retracted","被撤回"),
	/**
	 * 并行签署任务被撤销
	 */
	SIGN_LINE_RETRACTED("signLineRetracted","被撤回"),
	/**
	 * 通过取消
	 */
	SIGN_PASS_CANCEL("passCancel","通过取消"),
	/**
	 * 不通过取消
	 */
	SIGN_NOPASS_CANCEL("notPassCancel","不通过取消"),
	/**
	 * 流转
	 */
	TRANS_FORMING("transforming","流转"),
	/**
	 * 转办任务
	 */
	DELIVERTO("deliverto","转办任务"),
	/**
	 * 转办任务取消
	 */
	DELIVERTO_CANCEL("deliverto_cancel","转办任务取消"),
	/**
	 * 转办同意
	 */
	DELIVERTO_AGREE("delivertoAgree","转办同意"),
	/**
	 * 转办反对
	 */
	DELIVERTO_OPPOSE("delivertoOppose","转办反对"),
	/**
	 * 流转同意
	 */
	TRANS_AGREE("transAgree","流转同意"),
	/**
	 * 流转反对
	 */
	TRANS_OPPOSE("transOppose","流转反对"),

    /**
     * 加签同意
     */
    ADDSIGN_AGREE("addsignAgree","加签同意"),
    /**
     * 加签反对
     */
    ADDSIGN_OPPOSE("addsignOppose","加签反对"),
	/**
	 * 跳过执行
	 */
	SKIP("skip","跳过执行"),
	/**
	 * 人工终止
	 */
	MANUAL_END("manual_end","人工终止"),
	/**
	 * 传阅
	 */
	COPYTO("copyto","传阅"),
	/**
	 * 传阅回复
	 */
	COPYTO_REPLY("copyto_reply","传阅回复"),
	/**
	 * 征询
	 */
	INQU("inqu","征询"),
	/**
	 * 征询
	 */
	SHAER("SHAER","共享"),
	/**
	 * 征询回复
	 */
	INQU_REPLY("inqu_reply","征询回复"),
	/**
	 * 并行审批
	 */
	APPROVE_LINEING("approveLineing","并行审批"),
	/**
	 * 同意
	 */
	APPROVE_LINEING_AGREE("approveLineAgree","并行审批同意"),
	/**
	 * 反对
	 */
	APPROVE_LINEING_OPPOSE("approveLineOppose","并行审批反对"),
	/**
	 * 顺序签署中
	 */
	SIGNSEQUENCEING("signSequenceing","发起顺序签署"),
	/**
	 * 签署同意
	 */
	SIGNSEQUENCE_AGREE("signSequenceAgree","顺序签署同意"),
	/**
	 * 签署反对
	 */
	SIGNSEQUENCE_OPPOSE("signSequenceOppose","顺序签署反对"),
	/**
	 * 发起并行签署
	 */
	SIGNLINEING("signLineing","发起并行签署"),
	/**
	 * 签署同意
	 */
	SIGNLINE_AGREE("signLineAgree","并行签署同意"),
	/**
	 * 签署反对
	 */
	SIGNLINE_OPPOSE("signLineOppose","并行签署反对"),
	/**
	 * 签收任务
	 */
	LOCK_TASK("lockTask","签收");


	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private OpinionStatus(String key, String value) {
		this.key = key;
		this.value = value;
	}

	// =====getting and setting=====
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return key;
	}

	/**
	 * 通过key获取对象
	 * 
	 * @param key
	 * @return
	 */
	public static OpinionStatus fromKey(String key) {
		for (OpinionStatus c : OpinionStatus.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
}
