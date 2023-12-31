/*package com.hotent.runtime.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotent.base.core.engine.script.GroovyScriptEngine;
import com.hotent.base.core.util.AppUtil;
import com.hotent.base.core.util.string.StringUtil;
import com.hotent.base.core.util.time.TimeUtil;
import com.hotent.runtime.service.BpmTaskActionService;
import com.hotent.runtime.core.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.runtime.core.util.BpmUtil;
import com.hotent.runtime.core.persistence.constants.TaskReminderCons;
import com.hotent.runtime.core.persistence.manager.BpmTaskManager;
import com.hotent.runtime.core.persistence.manager.BpmTaskReminderManager;
import com.hotent.runtime.core.persistence.manager.BpmTaskReminderStateManager;
import com.hotent.runtime.core.persistence.model.BpmTaskReminder;
import com.hotent.runtime.core.persistence.model.DefaultBpmTask;
import com.hotent.org.api.model.IUser;
import com.hotent.sys.api.calendar.ICalendarService;
import com.hotent.sys.api.jms.JmsProducer;
import com.hotent.sys.api.job.BaseJob;
import com.hotent.sys.api.msg.model.DefaultMsgVo;

*//**
 * 定时任务操作。
 * 
 * <pre>
 * 构建组：x5-bpmx-core
 * 作者：ouxb
 * 邮箱:ouxb@jee-soft.cn
 * 日期:2014-09-26 14:06:53
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 *//*
public class ReminderJob extends BaseJob {
	private static final Short REMIND = 1; // 流程提醒

	private static final Short COMPLETE = 2; // 执行到期处理动作

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private GroovyScriptEngine groovyScriptEngine = AppUtil.getBean(GroovyScriptEngine.class);

	private BpmTaskReminderManager bpmTaskReminderManager = AppUtil.getBean(BpmTaskReminderManager.class);

	private BpmTaskReminderStateManager bpmTaskReminderStateManager = AppUtil.getBean(BpmTaskReminderStateManager.class);

	private BpmTaskManager bpmTaskManager = AppUtil.getBean(BpmTaskManager.class);

	private BpmTaskActionService bpmTaskActionService = AppUtil.getBean(BpmTaskActionService.class);

	private ICalendarService iCalendarService = AppUtil.getBean(ICalendarService.class); // iCalendarService.getEndTimeByUser
																							// TODO
																							// 调试出来的时间偏差过大，不知是否存在问题

	private JmsProducer jmsProducer = AppUtil.getBean(JmsProducer.class);

	@Override
	public void executeJob(JobExecutionContext context) {
		bpmTaskReminderStateManager.delExpiredTaskReminderState(); // 清除任务状态数据。

		List<DefaultBpmTask> bpmTasks = bpmTaskManager.getReminderTask(); // 获取未到期的任务
		if (bpmTasks == null || bpmTasks.size() == 0) {
			logger.debug("=======================没有获取到任务===============================");
			return;
		}

		for (DefaultBpmTask task : bpmTasks) {
			String procDefId = task.getProcDefId();// 取得流程定义ID
			String nodeId = task.getNodeId();// 取得节点ID
			List<BpmTaskReminder> taskReminders = bpmTaskReminderManager.getAllByDefIdAndNodeId(procDefId, nodeId); // 获取催办记录
			Map<String, Object> vars = this.getVariables(task.getProcInstId()); // 获取流程常量

			for (BpmTaskReminder taskReminder : taskReminders) {
				// 判断条件表达式
				boolean conExpResult = this.executeConExpression(taskReminder.getConExpression(), vars);
				if (!conExpResult) {
					logger.debug("=======================条件表达式审核不通过===============================");
					continue;
				}

				Date taskStartTime = bpmTaskReminderManager.getRelativeStartTime(task.getProcInstId(), taskReminder.getRelNodeId(), taskReminder.getRelNodeEvent());// 任务开始时间
				if (taskStartTime == null) {
					logger.debug("=======================未获取到相对节点的创建或者完成时间==================");
					continue;
				}

				Set<IUser> userSet = bpmTaskReminderManager.getUserByTask(task);// 获取用户数据
				if (taskReminder.getSendTimes() > 0) { // 提醒次数如果为0则表示不发送消息
					this.handReminder(task, taskReminder, userSet, taskStartTime, vars); // 处理提醒
				}

				this.handlerDueTask(task, taskReminder, userSet, taskStartTime);// 到期处理
			}
		}
	}

	*//**
	 * 判断条件表达式,false表示终止，true表示继续执行，空也表示继续执行
	 * 
	 * @param conExpression
	 * @param vars
	 * @return
	 *//*
	private boolean executeConExpression(String conExpression, Map<String, Object> vars) {
		if (StringUtil.isEmpty(conExpression))
			return true;
		try {
			return groovyScriptEngine.executeBoolean(conExpression, vars);
		} catch (Exception e) {
			logger.error("groovy 脚步编译错误,请检查脚本:" + conExpression);
			return false;
		}
	}

	*//**
	 * 获取流程常量
	 * 
	 * @param procInstId
	 * @return
	 *//*
	private Map<String, Object> getVariables(String procInstId) {
		// TODO 此方法还有问题
		// Map<String, Object> vars = runtimeService.getVariables(procInstId);
		Map<String, Object> vars = null;
		if (vars == null) {
			vars = new HashMap<String, Object>();
		}
		return vars;
	}

	*//**
	 * 处理消息报警
	 * 
	 * @param task
	 * @param taskReminder
	 * @param userSet
	 * @param vars
	 * @throws Exception
	 *//*
	private void handReminder(DefaultBpmTask task, BpmTaskReminder taskReminder, Set<IUser> userSet, Date taskStartTime, Map<String, Object> vars) {
		int needRemindTimes = taskReminder.getSendTimes();// 需要提醒的次数。
		int reminderStart = taskReminder.getStartTime(); // 离任务起始时间的分钟数
		int interval = taskReminder.getInterval();// 任务距离触发时间的分钟数
		String bpmTaskId = task.getId();
		String procDefId = task.getProcDefId();
		String procInstanceId = task.getProcInstId();

		for (IUser user : userSet) {
			String userId = user.getUserId();
			int reminderTimes = bpmTaskReminderStateManager.getAmountByUserTaskId(bpmTaskId, userId, REMIND);// 已催办的次数
			if (reminderTimes >= needRemindTimes) {
				logger.debug("=======================已达到指定的催办次数=======================");
				continue;
			}

			int relBeginTime = reminderStart + interval * reminderTimes; // 相对开始时间
			boolean isNotBeginTime = this.isNotBeginTime(relBeginTime, taskStartTime, taskReminder.getRelTimeType(), userId);
			if (isNotBeginTime) {
				logger.debug("=======================催办时间未到===============================");
				continue;
			}

			int relCompleteTime = taskReminder.getDueTime(); // 相对结束时间
			boolean isCompleteDate = this.isCompleteDate(relCompleteTime, taskStartTime, taskReminder.getRelTimeType(), userId);
			if (isCompleteDate) {
				logger.debug("=======================催办时间已过期=============================");
				continue;
			}

			this.sendMsg(task, taskReminder, user);// 发送催办消息

			bpmTaskReminderStateManager.save(procDefId, bpmTaskId, LocalDateTime.now(), userId, procInstanceId, REMIND); // 记录提醒次数
		}
	}

	*//**
	 * 判断是否已经到期的催办时间
	 * 
	 * @param relCompleteTime
	 *            相对结束时间
	 * @param taskStartTime
	 *            相对节点任务完成或开始的具体时间段
	 * @param relTimeType
	 *            相对时间类型
	 * @param userId
	 *            用户id
	 * @return
	 *//*
	private boolean isCompleteDate(int relCompleteTime, Date taskStartTime, String relTimeType, String userId) {
		Date completeDate = null;
		if (TaskReminderCons.TASK_TIME_TYPE_CALTIME.equals(relTimeType)) { // 日历日
			completeDate = new Date(TimeUtil.getNextTime(TimeUtil.MINUTE, relCompleteTime, taskStartTime.getTime()));
		} else { // 工作日
			completeDate = iCalendarService.getEndTimeByUser(userId, taskStartTime, relCompleteTime);
		}
		return completeDate.compareTo(LocalDateTime.now()) < 0;
	}

	*//**
	 * 判断是否已经到了催办开始的时间
	 * 
	 * @param relBeginTime
	 *            相对开始的时间 =【开始时间】 + 【时间间隔】*【次数】
	 * @param taskStartTime
	 *            相对节点任务完成或开始的具体时间段
	 * @param relTimeType
	 *            相对时间类型
	 * @param userId
	 *            用户id
	 * @return
	 *//*
	private boolean isNotBeginTime(int relBeginTime, Date taskStartTime, String relTimeType, String userId) {
		Date beginDate = null;
		// 日历日
		if (TaskReminderCons.TASK_TIME_TYPE_CALTIME.equals(relTimeType)) {
			beginDate = new Date(TimeUtil.getNextTime(TimeUtil.MINUTE, relBeginTime, taskStartTime.getTime()));
		} else {// 工作日
			beginDate = iCalendarService.getEndTimeByUser(userId, taskStartTime, relBeginTime);
		}
		return beginDate.compareTo(LocalDateTime.now()) > 0;
	}

	*//**
	 * 发送消息
	 * 
	 * @param task
	 * @param taskReminder
	 * @param userId
	 *//*
	private void sendMsg(DefaultBpmTask task, BpmTaskReminder taskReminder, IUser user) {
		String masTypeHtml = taskReminder.getMasTypeHtml(); // 富文本类型
		String masTypePt = taskReminder.getMasTypePt(); // 谱图文本类型
		List<IUser> receivers = new ArrayList<IUser>();
		receivers.add(user);
		if (StringUtil.isNotEmpty(masTypeHtml)) {
			this.sendHtml(task, taskReminder, receivers); // 发送富文本
		}
		if (StringUtil.isNotEmpty(masTypePt)) {
			this.sendPt(task, taskReminder, receivers);// 发送普通文本
		}

	}
	*//**
	 * 发送普通文本
	 * 
	 * @param task
	 * @param taskReminder
	 * @param userId
	 *//*
	private void sendPt(DefaultBpmTask task, BpmTaskReminder taskReminder, List<IUser> receivers) {
		String subject = "摧办信息";
		String content = replaceContent(task, taskReminder.getPlainText());
		String type = taskReminder.getMasTypePt();
		this.send(subject, content, BpmUtil.getUser("0", "系统通知"), receivers, type, task);
	}

	*//**
	 * 发送富文本
	 * 
	 * @param task
	 * @param taskReminder
	 * @param userId
	 *//*
	private void sendHtml(DefaultBpmTask task, BpmTaskReminder taskReminder, List<IUser> receivers) {
		String subject = "摧办信息";
		String content = replaceContent(task, taskReminder.getHtml());
		String type = taskReminder.getMasTypeHtml();
		this.send(subject, content, BpmUtil.getUser("0", "系统通知"), receivers, type, task);
	}

	*//**
	 * 替换文本中的特殊字符串
	 * 
	 * @param task
	 * @param content
	 *//*
	private String replaceContent(DefaultBpmTask task, String content) {
		if (StringUtil.isEmpty(content)) {
			return "";
		}

		if (task == null) {
			return content;
		}

		if (content.indexOf("{title}") > -1) {
			content = content.replaceAll("\\{title\\}", task.getSubject());
		}

		if (content.indexOf("{time}") > -1) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strTime = sdf.format(task.getCreateTime());
			content = content.replaceAll("\\{time\\}", strTime);
		}

		return content;

	}

	*//**
	 * 发送消息
	 * 
	 * @param subject
	 * @param content
	 * @param receivers
	 * @param notifyType
	 * @param task
	 *//*
	private void send(String subject, String content, IUser sender, List<IUser> receivers, String notifyType, DefaultBpmTask task) {
		if (jmsProducer == null)
			return;
		if (StringUtil.isEmpty(notifyType))
			return;
		String[] aryType = notifyType.split(",");
		for (String type : aryType) {
			DefaultMsgVo msgVo = new DefaultMsgVo(subject, content, sender, receivers, type);
			jmsProducer.sendToQueue(msgVo);
		}

	}

	*//**
	 * 处理任务到期。
	 * 
	 * @param task
	 * @param taskReminder
	 * @param userSet
	 * @throws Exception
	 *//*
	private void handlerDueTask(DefaultBpmTask task, BpmTaskReminder taskReminder, Set<IUser> userSet, Date taskStartTime) {
		String bpmTaskId = task.getId();
		String procDefId = task.getProcDefId();
		String procInstanceId = task.getProcInstId();
		int reminderTimes = bpmTaskReminderStateManager.getAmountByTaskId(bpmTaskId, COMPLETE); // 到期动作，只执行一次
		if (reminderTimes > 0) {
			logger.debug("=======================已经执行过了到期动作===============================");
			return;
		}
		if (TaskReminderCons.TASK_TIME_TYPE_CALTIME.equals(taskReminder.getRelTimeType())) { // 日历日
			boolean isDue = this.isDueForCalTime(taskReminder.getDueTime(), taskStartTime.getTime());
			if (isDue)
				return;
		} else { // 工作日
			if (userSet.size() > 0) { // 该任务有执行人或候选人(工作日需要考虑不同的用户工作日历、加班、请假不同的情况)，默认只拿第一个人的工作日历
				boolean isDue = this.isDueForWorkTimeUserId(taskReminder.getDueTime(), taskStartTime, userSet);
				if (isDue)
					return;
			} else {
				boolean isDue = this.isDueForWorkTimeDefault(taskReminder.getDueTime(), taskStartTime);
				if (isDue)
					return;
			}
		}

		this.handlerAction(taskReminder, task); // 动作

		bpmTaskReminderStateManager.save(procDefId, bpmTaskId, LocalDateTime.now(), null, procInstanceId, COMPLETE);// 记录提醒次数

	}

	*//**
	 * 处理任务到期动作
	 * 
	 * @param taskReminder
	 * @param task
	 *//*
	private void handlerAction(BpmTaskReminder taskReminder, DefaultBpmTask task) {
		String dueAction = taskReminder.getDueAction();
		if (TaskReminderCons.TASK_DUE_ACTION_NO_ACTION.equals(dueAction)) {
			logger.debug("无动作");
		} else if (TaskReminderCons.TASK_DUE_ACTION_AUTO_NEXT.equals(dueAction)) {
			DefaultTaskFinishCmd taskFinishCmd = new DefaultTaskFinishCmd();
			taskFinishCmd.setTaskId(task.getTaskId());
			bpmTaskActionService.finishTask(taskFinishCmd);
			logger.debug("自动下一个任务");
		} else if (TaskReminderCons.TASK_DUE_ACTION_END_PROCESS.equals(dueAction)) {
			// bpmTaskActionService.endProcessByTaskId(task.getTaskId()); //
			// TODO
			logger.debug("结束任务");
		} else if (TaskReminderCons.TASK_DUE_ACTION_CALL_METHOD.equals(dueAction)) {
			String script = taskReminder.getCallScript();
			if (StringUtil.isEmpty(script)) {
				Map<String, Object> vars = new HashMap<String, Object>();
				// TODO vars 常量参数，还未知
				groovyScriptEngine.execute(script, vars);
			}
			logger.debug("调用方法");
		}

	}

	*//**
	 * 该任务有执行人或候选人(工作日需要考虑不同的用户工作日历、加班、请假不同的情况)<br>
	 * 默认只拿第一个人的工作日历，因为到期动作只执行一次
	 * 
	 * @param dueTime
	 * @param taskStartTime
	 * @param userSet
	 * @return
	 *//*
	private boolean isDueForWorkTimeUserId(Integer dueTime, Date taskStartTime, Set<IUser> userSet) {
		IUser user = this.getSetFirst(userSet);
		return isDueForWorkTime(user.getUserId(), dueTime, taskStartTime);
	}

	*//**
	 * 获取set集合中的第一个值
	 * 
	 * @param userSet
	 * @return
	 *//*
	private IUser getSetFirst(Set<IUser> userSet) {
		IUser user = null;
		for (Iterator<IUser> it = userSet.iterator(); it.hasNext();) {
			user = it.next();
			break;
		}
		return user;
	}

	*//**
	 * 没有用户的情况下，获取默认的日历
	 * 
	 * @param dueTime
	 * @param taskStartTime
	 * @param userSet
	 * @return
	 *//*
	private boolean isDueForWorkTimeDefault(Integer dueTime, Date taskStartTime) {
		return isDueForWorkTime(null, dueTime, taskStartTime);
	}

	*//**
	 * 判断在工作日模式下，是否已经过期了
	 * 
	 * @param dueTime
	 * @param time
	 * @return
	 *//*
	private boolean isDueForWorkTime(String userId, Integer dueTime, Date taskStartTime) {
		Date dueDate = iCalendarService.getEndTimeByUser(userId, taskStartTime, dueTime);
		return dueDate.compareTo(LocalDateTime.now()) < 0;
	}

	*//**
	 * 判断在日历模式下，是否已经过期了
	 * 
	 * @param dueTime
	 * @param time
	 * @return
	 *//*
	private boolean isDueForCalTime(Integer dueTime, long time) {
		Date dueDate = new Date(TimeUtil.getNextTime(TimeUtil.MINUTE, dueTime, time));
		return dueDate.compareTo(LocalDateTime.now()) < 0;
	}

}
*/