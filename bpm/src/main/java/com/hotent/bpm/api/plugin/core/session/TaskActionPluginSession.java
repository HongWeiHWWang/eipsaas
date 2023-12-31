package com.hotent.bpm.api.plugin.core.session;

import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
/**
 * 
 * <pre> 
 * 描述：任务动作参数会话
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-3-17-下午9:08:56
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface TaskActionPluginSession extends BpmPluginSession{
	/**
	 * 取得任务完成的命令参数实体
	 * @return 
	 * TaskFinishCmd
	 * @exception 
	 * @since  1.0.0
	 */
	public TaskFinishCmd getTaskFinishCmd();
	/**
	 * 获取任务的实体
	 * @return 
	 * BpmDelegateTask
	 * @exception 
	 * @since  1.0.0
	 */
	public BpmDelegateTask getBpmDelegateTask();
}
