package com.hotent.job.persistence.manager;



import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.hotent.base.context.BaseContext;
import org.apache.commons.lang.StringUtils;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.job.model.ParameterObj;
import com.hotent.job.model.PlanObject;
import com.hotent.job.model.SchedulerVo;
import com.hotent.job.util.ConvertUtil;

/**
 * 定时框架。
 *
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月8日
 */
@Service
public class SchedulerService {
	@Resource
	Scheduler scheduler;

	private static HashMap<String, String> mapWeek;
	@Resource
	BaseContext baseContext;

	static {
		// "MON,TUE,WED,THU,FRI,SAT,SUN"
		mapWeek = new HashMap<String, String>();
		mapWeek.put("MON", "星期一");
		mapWeek.put("TUE", "星期二");
		mapWeek.put("WED", "星期三");
		mapWeek.put("THU", "星期四");
		mapWeek.put("FRI", "星期五");
		mapWeek.put("SAT", "星期六");
		mapWeek.put("SUN", "星期日");

	}


	/**
	 * 添加任务
	 *
	 * @param jobName 任务名称
	 * @param className 类名称
	 * @param parameterJson 参数
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean addJob(String jobName, String className,
			String parameterJson, String description)
			throws Exception {
		if (scheduler == null)
			return false;
		Class<Job> cls = (Class<Job>) Class.forName(className);
		JobBuilder jb = JobBuilder.newJob(cls);
		jb.withIdentity(jobName, baseContext.getCurrentTenantId());
		if (StringUtils.isNotEmpty(parameterJson))
			setJobMap(parameterJson, jb);
		jb.storeDurably();
		jb.withDescription(description);
		JobDetail jobDetail = jb.build();
		scheduler.addJob(jobDetail, true);
		return true;
	}

	/**
	 * 添加任务
	 *
	 * @param jobName 任务名称
	 * @param className 类的全路径
	 * @param parameterMap 参数
	 * @param description 任务描述
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public CommonResult<String> addJob(String jobName, String className,
			Map<String, Object> parameterMap, String description)
			throws SchedulerException {
		if (scheduler == null)
			return new CommonResult<String>(false, "scheduler 没有配置!", null);
		boolean isJobExist = this.isJobExists(jobName);
		if (isJobExist) {
			return new CommonResult<String>(false, "任务已存在", null);
		}
		Class<Job> cls;
		try {
			cls = (Class<Job>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			return new CommonResult<String>(false,  "指定的任务类不存在，或者没有实现JOB接口", null);
		}
		try {
			return addJob(jobName, cls, parameterMap, description);
		} catch (Exception e) {
			return new CommonResult<String>(false,  e.getMessage(), null);
		}
	}

	/**
	 * 添加任务
	 *
	 * @param jobName 任务名称
	 * @param cls	类
	 * @param parameterMap 参数map
	 * @param description 描述
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public CommonResult<String> addJob(String jobName, Class<?> cls,
			Map<String, Object> parameterMap, String description) {
		if (scheduler == null)
			return new CommonResult<>(false, "scheduler 没有配置!", null);
		try {
			JobBuilder jb = JobBuilder.newJob((Class<? extends Job>) cls);
			jb.withIdentity(jobName, baseContext.getCurrentTenantId());
			if (parameterMap != null) {
				JobDataMap map = new JobDataMap();
				map.putAll(parameterMap);
				jb.usingJobData(map);
			}
			jb.storeDurably();
			jb.withDescription(description);
			JobDetail jobDetail = jb.build();
			scheduler.addJob(jobDetail, true);
			return new CommonResult<String>(true, "添加任务成功!", null);
		} catch (Exception e) {
			return new CommonResult<String>(false, "添加任务失敗!", null);
		}
	}

	/**
	 * 判断任务是否存在
	 *
	 * @param jobName 任务名称
	 * @return		      返回判断结果
	 * @throws SchedulerException
	 */
	public boolean isJobExists(String jobName) throws SchedulerException {
		if (scheduler == null)
			return false;
		JobKey key = new JobKey(jobName, baseContext.getCurrentTenantId());
		return scheduler.checkExists(key);
	}

	/**
	 * 取得任务列表
	 *
	 * @return 返回任务列表
	 * @throws SchedulerException
	 */
	public List<SchedulerVo> getJobList() throws SchedulerException {
		if (scheduler == null)
			return new ArrayList<SchedulerVo>();
		List<SchedulerVo> list = new ArrayList<SchedulerVo>();
		GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(baseContext.getCurrentTenantId());
		Set<JobKey> set = scheduler.getJobKeys(matcher);
		for (JobKey jobKey : set) {
				JobDetail detail = scheduler.getJobDetail(jobKey);

				list.add(ConvertUtil.toBean(detail));
		}
		return list;
	}

	public SchedulerVo getJobDetail(String key) throws Exception{
		JobKey jobKey = new JobKey(key, baseContext.getCurrentTenantId());
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		return ConvertUtil.toJobBean(jobDetail);
	}

	/**
	 * 根据任务名称获取触发器
	 *
	 * @param jobName 任务名称
	 * @return  	      返回触发器列表
	 * @throws SchedulerException
	 */
	@SuppressWarnings("unchecked")
	public List<Trigger> getTriggersByJob(String jobName)
			throws SchedulerException {
		if (scheduler == null)
			return new ArrayList<Trigger>();
		JobKey key = new JobKey(jobName, baseContext.getCurrentTenantId());
		return (List<Trigger>) scheduler.getTriggersOfJob(key);
	}

	/**
	 * 取得触发器的状态
	 *
	 * @param list 触发器
	 * @return	         触发器状态
	 * @throws SchedulerException
	 */
	public HashMap<String, Trigger.TriggerState> getTriggerStatus(
			List<Trigger> list) throws SchedulerException {
		if (scheduler == null)
			return new HashMap<String, Trigger.TriggerState>();
		HashMap<String, Trigger.TriggerState> map = new HashMap<String, Trigger.TriggerState>();
		for (Iterator<Trigger> it = list.iterator(); it.hasNext();) {
			Trigger trigger = it.next();
			TriggerKey key = trigger.getKey();
			Trigger.TriggerState state = scheduler.getTriggerState(key);
			map.put(key.getName(), state);
		}
		return map;
	}

	/**
	 * 判断计划是否存在
	 *
	 * @param trigName 触发器名称
	 * @return		         返回结果
	 * @throws SchedulerException
	 */
	public boolean isTriggerExists(String trigName) throws SchedulerException {
		if (scheduler == null)
			return false;
		TriggerKey triggerKey = new TriggerKey(trigName, baseContext.getCurrentTenantId());
		return scheduler.checkExists(triggerKey);
	}

	/**
	 * 添加计划
	 *
	 * @param jobName 任务名
	 * @param trigName 计划名
	 * @param planJson 计划数据
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public void addTrigger(String jobName, String trigName, String planJson)
			throws Exception {
		if (scheduler == null)
			return;
 		JobKey jobKey = new JobKey(jobName, baseContext.getCurrentTenantId());
		TriggerBuilder<Trigger> tb = TriggerBuilder.newTrigger();
		tb.withIdentity(trigName, baseContext.getCurrentTenantId());
		// tb.withDescription(description);
		this.setTrigBuilder(planJson, tb);
		tb.forJob(jobKey);
		Trigger trig = tb.build();
		scheduler.scheduleJob(trig);
	}

	/**
	 * 添加触发器
	 *
	 * @param jobName 计划名称
	 * @param trigName 触发器名称
	 * @param minute   触发次数
	 * @throws SchedulerException
	 */
	public void addTrigger(String jobName, String trigName, int minute)
			throws SchedulerException {
		if (scheduler == null)
			return;
		JobKey jobKey = new JobKey(jobName, baseContext.getCurrentTenantId());
		TriggerBuilder<Trigger> tb = TriggerBuilder.newTrigger();
		tb.withIdentity(trigName, baseContext.getCurrentTenantId());
		ScheduleBuilder<?> sb = CalendarIntervalScheduleBuilder
				.calendarIntervalSchedule().withIntervalInMinutes(minute);
		tb.startNow().withSchedule(sb).withDescription("每:" + minute + "分钟执行!")
				.forJob(jobKey);
		Trigger trig = tb.build();
		scheduler.scheduleJob(trig);
	}

	private void setTrigBuilder(String planJson, TriggerBuilder<Trigger> tb)
			throws Exception {
		PlanObject planObject = JsonUtil.toBean(planJson, PlanObject.class);
		int type = planObject.getType();
		String value = planObject.getTimeInterval();
		ScheduleBuilder<?> sb = null;
		switch (type) {
		// 启动一次
		case PlanObject.TYPE_FIRST:
			LocalDateTime lt = DateFormatUtil.parseDateTime(value);
			ZoneId zoneId = ZoneId.systemDefault();
			ZonedDateTime zdt = lt.atZone(zoneId);
	        Date date = Date.from(zdt.toInstant());
			tb.startAt(date).withDescription("执行一次,执行时间:" + date.toLocaleString());
			break;
		// 每分钟执行
		case PlanObject.TYPE_PER_MINUTE:
			int minute = Integer.parseInt(value);
			sb = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
					.withIntervalInMinutes(minute);
			tb.startNow().withSchedule(sb)
					.withDescription("每:" + minute + "分钟执行!");
			break;
		// 每天时间点执行
		case PlanObject.TYPE_PER_DAY:
			String[] aryTime = value.split(":");
			int hour = Integer.parseInt(aryTime[0]);
			int m = Integer.parseInt(aryTime[1]);
			sb = CronScheduleBuilder.dailyAtHourAndMinute(hour, m);
			tb.startNow().withSchedule(sb)
					.withDescription("每天：" + hour + ":" + m + "执行!");
			break;
		// 每周时间点执行
		case PlanObject.TYPE_PER_WEEK:
			// 0 15 10 ? * MON-FRI Fire at 10:15am every Monday, Tuesday,
			// Wednesday, Thursday and Friday
			String[] aryExpression = value.split("[|]");
			String week = aryExpression[0];
			String[] aryTime1 = aryExpression[1].split(":");
			String h1 = aryTime1[0];
			String m1 = aryTime1[1];
			String cronExperssion = "0 " + m1 + " " + h1 + " ? * " + week;
			sb = CronScheduleBuilder.cronSchedule(cronExperssion);
			tb.startNow()
					.withSchedule(sb)
					.withDescription(
							"每周：" + getWeek(week) + "," + h1 + ":" + m1 + "执行!");
			break;
		// 每月执行
		case PlanObject.TYPE_PER_MONTH:
			// 0 15 10 15 * ?
			String[] aryExpression5 = value.split("[|]");
			String day = aryExpression5[0];
			String[] aryTime2 = aryExpression5[1].split(":");
			String h2 = aryTime2[0];
			String m2 = aryTime2[1];
			String cronExperssion1 = "0 " + m2 + " " + h2 + " " + day + " * ?";
			sb = CronScheduleBuilder.cronSchedule(cronExperssion1);
			tb.startNow()
					.withSchedule(sb)
					.withDescription(
							"每月:" + getDay(day) + "," + h2 + ":" + m2 + "执行!");
			break;
		// 表达式
		case PlanObject.TYPE_CRON:
			try {
				sb = CronScheduleBuilder.cronSchedule(value);
				tb.startNow().withSchedule(sb);
			} catch (RuntimeException e) {
				// MessageUtil.addMsg("CronTrigger 表达式异常:" + e.getMessage());
				throw e;
			}
			tb.withDescription("CronTrigger表达式:" + value);
			break;
		}
	}

	private String getDay(String day) {
		String[] aryDay = day.split(",");
		int len = aryDay.length;
		String str = "";
		for (int i = 0; i < len; i++) {
			String tmp = aryDay[i];
			tmp = tmp.equals("L") ? "最后一天" : tmp;
			if (i < len - 1) {
				str += tmp + ",";
			} else {
				str += tmp;
			}
		}
		return str;
	}

	/**
	 * 取得星期名称
	 *
	 * @param week 星期
	 * @return	         返回名称
	 */
	private String getWeek(String week) {
		String[] aryWeek = week.split(",");
		int len = aryWeek.length;
		String str = "";
		for (int i = 0; i < len; i++) {
			if (i < len - 1)
				str += mapWeek.get(aryWeek[i]) + ",";
			else
				str += mapWeek.get(aryWeek[i]);
		}
		return str;
	}

	/**
	 * 设置任务参数
	 *
	 * @param json ParameterObj的Json
	 * @param job  任务
	 * @throws Exception
	 */
	private void setJobMap(String json, JobBuilder job) throws Exception{
		List<ParameterObj> list = JsonUtil.toBean(json, new TypeReference<List<ParameterObj>>(){});
		for(ParameterObj obj : list) {
			String type = obj.getType();
			String name = obj.getName();
			String value = obj.getValue();
			if (type.equals(ParameterObj.TYPE_INT)) {
				Integer val = StringUtils.isEmpty(value) ? 0 : Integer
						.parseInt(value);
				job.usingJobData(name, val);
			} else if (type.equals(ParameterObj.TYPE_LONG)) {
				Long val = StringUtils.isEmpty(value) ? 0L : Long
						.parseLong(value);
				job.usingJobData(name, val);
			} else if (type.equals(ParameterObj.TYPE_FLOAT)) {
				Float val = StringUtils.isEmpty(value) ? 0.0f : Float
						.parseFloat(value);
				job.usingJobData(name, val);
			} else if (type.equals(ParameterObj.TYPE_BOOLEAN)) {
				Boolean val = StringUtils.isEmpty(value) ? false : Boolean
						.parseBoolean(value);
				job.usingJobData(name, val);
			} else {
				job.usingJobData(name, value);
			}
		}
	}

	/**
	 * 删除任务
	 *
	 * @param jobName 任务名称
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public void delJob(String jobName) throws SchedulerException {
		if (scheduler == null)
			return;
		JobKey key = new JobKey(jobName, baseContext.getCurrentTenantId());
		scheduler.deleteJob(key);
	}

	/**
	 * 通过触发名称获得触发器
	 *
	 * @param triggerName 触发名称
	 * @return			      返回触发器
	 * @throws SchedulerException
	 */
	public Trigger getTrigger(String triggerName) throws SchedulerException {
		if (scheduler == null)
			return null;
		TriggerKey key = new TriggerKey(triggerName, baseContext.getCurrentTenantId());
		return scheduler.getTrigger(key);
	}

	/**
	 * 删除计划
	 *
	 * @param triggerName 触发器名称
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public void delTrigger(String triggerName) throws SchedulerException {
		if (scheduler == null)
			return;
		TriggerKey key = new TriggerKey(triggerName, baseContext.getCurrentTenantId());
		scheduler.unscheduleJob(key);
	}

	/**
	 * 停止或暂停触发器
	 *
	 * @param triggerName 触发器名称
	 * @throws SchedulerException
	 */
	public void toggleTriggerRun(String triggerName) throws SchedulerException {
		if (scheduler == null)
			return;
		TriggerKey key = new TriggerKey(triggerName, baseContext.getCurrentTenantId());
		Trigger.TriggerState state = scheduler.getTriggerState(key);
		if (state == TriggerState.PAUSED) {
			scheduler.resumeTrigger(key);
		} else if (state == TriggerState.NORMAL) {
			scheduler.pauseTrigger(key);
		}
	}

	/**
	 * 直接执行任务
	 *
	 * @param jobName 任务名称
	 * @throws SchedulerException
	 */
	public void executeJob(String jobName) throws SchedulerException {
		if (scheduler == null)
			return;
		JobKey key = new JobKey(jobName, baseContext.getCurrentTenantId());
		scheduler.triggerJob(key);
	}

	/**
	 * 启动
	 *
	 * @throws SchedulerException
	 */
	public void start() throws SchedulerException{
		scheduler.start();
	}

	/**
	 * 关闭
	 *
	 * @throws SchedulerException
	 */
	public void shutdown() throws SchedulerException{
		scheduler.standby();
	}

	/**
	 * 是否启动
	 *
	 * @return 返回结果
	 * @throws SchedulerException
	 */
	public boolean isStarted() throws SchedulerException{
		return scheduler.isStarted();
	}
	/**
	 * 是否挂起
	 *
	 * @return 返回挂起结果
	 * @throws SchedulerException
	 */
	public boolean isInStandbyMode() throws SchedulerException{
		return scheduler.isInStandbyMode();
	}
}
