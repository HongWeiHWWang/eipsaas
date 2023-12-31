/**
 * 描述：TODO
 * 包名：com.hotent.bpm.plugin.core.session
 * 文件名：DefaultExecutionActionPluginSession.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-4-8-下午3:22:39
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.plugin.core.session;

import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.plugin.core.session.ExecutionActionPluginSession;

/**
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-plugin-core
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-8-下午3:22:39
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefaultExecutionActionPluginSession extends AbstractBpmPluginSession implements ExecutionActionPluginSession{
	private TaskFinishCmd taskFinishCmd;
	private BpmDelegateExecution bpmDelegateExecution;
	public TaskFinishCmd getTaskFinishCmd() {
		return taskFinishCmd;
	}
	public void setTaskFinishCmd(TaskFinishCmd taskFinishCmd) {
		this.taskFinishCmd = taskFinishCmd;
	}
	public BpmDelegateExecution getBpmDelegateExecution() {
		return bpmDelegateExecution;
	}
	public void setBpmDelegateExecution(BpmDelegateExecution bpmDelegateExecution) {
		this.bpmDelegateExecution = bpmDelegateExecution;
	}
	
}
