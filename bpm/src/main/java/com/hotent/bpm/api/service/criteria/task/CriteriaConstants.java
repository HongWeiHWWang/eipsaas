/**
 * 描述：TODO
 * 包名：com.hotent.runtime.core.engine.task.service.criteria
 * 文件名：CriteriaConstants.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-4-4-下午6:12:04
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.api.service.criteria.task;

/**
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-core
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-4-下午6:12:04
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class CriteriaConstants {
	public final static String MINE = "self";										//查询指定用户的代办任务
	public final static String AGENT = "agent";								//查询指定用户代理的任务
	public final static String AGENTED_TO = "agented_to";		//查询指定用户代理给别人的任务
	public final static String COPY_TO = "copy_to";						//查询抄送给指定用户的任务
}
