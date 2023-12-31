package com.hotent.activiti.cmd;

import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * 设置用户任务。
 * <pre>
 * 如果任务执行人就是当前人，则直接返回,不做处理。
 * </pre>
 * <pre> 
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-9-19-下午12:05:43
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class SetAssigneeCmd extends NeedsActiveTaskCmd<Void> {

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 7601625963655262976L;

	protected String userId;

	public SetAssigneeCmd(String taskId, String userId) {
		super(taskId);
		this.userId = userId;
	}

	@Override
	protected Void execute(CommandContext commandContext, TaskEntity task) {
		if (userId != null) {
			if (task.getAssignee() != null && userId.equals(task.getAssignee())) {
				return null;
			} 
			task.setAssignee(userId, true, true);
		} else {
			task.setAssignee(null);
		}
		// Add claim time
		commandContext.getHistoryManager().recordTaskClaim(taskId);
		return null;
	}

}
