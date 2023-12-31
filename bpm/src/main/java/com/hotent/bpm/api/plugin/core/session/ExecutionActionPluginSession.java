/**
 * 描述：TODO
 * 包名：com.hotent.bpm.plugin.core.session
 * 文件名：TaskActionExecutionPluginSession.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-4-8-下午3:19:51
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.api.plugin.core.session;

import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

/**
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-8-下午3:19:51
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface ExecutionActionPluginSession extends BpmPluginSession{
	/**
	 * 取得任务完成的命令参数实体
	 * @return 
	 * TaskFinishCmd
	 * @exception 
	 * @since  1.0.0
	 */
	public TaskFinishCmd getTaskFinishCmd();
	
	/**
	 * 取得执行数据
	 * @return 
	 * BpmDelegateExecution
	 * @exception 
	 * @since  1.0.0
	 */
	public BpmDelegateExecution getBpmDelegateExecution();
}
