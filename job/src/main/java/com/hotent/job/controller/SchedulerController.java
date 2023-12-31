package com.hotent.job.controller;


import com.hotent.base.context.BaseContext;
import com.hotent.job.model.JobDetails;
import com.hotent.job.persistence.manager.JobDetailsManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.job.model.SchedulerVo;
import com.hotent.job.model.SysJobLog;
import com.hotent.job.model.TriggerModel;
import com.hotent.job.persistence.manager.SchedulerService;
import com.hotent.job.persistence.manager.SysJobLogManager;

/**
 * 定时计划控制类
 *
 * @author maoww
 * @company 广州宏天软件股份有限公司
 * @email maoww@jee-soft.cn
 * @date 2018年6月7日
 */
@RestController
@RequestMapping("/job/scheduler/v1/")
@Api(tags = "定时计划")
@ApiGroup(group = {ApiGroupConsts.GROUP_PORTAL})
@SuppressWarnings({"rawtypes", "unchecked"})
public class SchedulerController extends BaseController<SysJobLogManager, SysJobLog> {

    @Resource
    SchedulerService schedulerService;
    @Resource
    SysJobLogManager jobLogManager;
    @Resource
    JobDetailsManager jobDetailsManager;
    @Resource
    BaseContext baseContext;

    @RequestMapping(value = "addJob", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "添加任务", httpMethod = "POST", notes = "添加任务")
    public CommonResult addJob(@ApiParam(name = "scheduler", value = "实体对象") @RequestBody SchedulerVo scheduler) throws Exception {
        boolean isExist = schedulerService.isJobExists(scheduler.getJobName());
        if (isExist) {
            return new CommonResult(false, "任务名称已经存在，添加失败", null);
        } else {
            if (isExist("className", scheduler.getClassName())) {
                return new CommonResult(false, "任务列表中已添加该任务类记录，不能多次添加同一任务类！", null);
            } else{
                boolean isSuccess = schedulerService.addJob(scheduler.getJobName(), scheduler.getClassName(), scheduler.getParameterJson(), scheduler.getDescription());
                if(!isSuccess){
                    return new CommonResult(false, "任务添加失败(可能是任务类不存在或属性类型、值不一致!", null);
                }
            }
        }
        return new CommonResult(true, "添加任务成功", null);
    }

    @RequestMapping(value = "saveJob", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "保存任务", httpMethod = "POST", notes = "添加任务")
    public CommonResult saveJob(@ApiParam(name = "scheduler", value = "实体对象") @RequestBody SchedulerVo scheduler, @ApiParam(name = "scheduler", value = "实体对象") @RequestParam String jobName) throws Exception {
        if (!jobName.equals(scheduler.getJobName())) {
            boolean isExist = schedulerService.isJobExists(scheduler.getJobName());
            if (isExist) {
                return new CommonResult(false, "任务名称【" + scheduler.getJobName() + "】已经存在，更新失败", null);
            }
        }
        boolean isSuccess = schedulerService.addJob(scheduler.getJobName(), scheduler.getClassName(), scheduler.getParameterJson(), scheduler.getDescription());
        if (!isSuccess) {
            return new CommonResult(false, "任务保存失败(可能是任务类不存在或属性类型、值不一致!", null);
        }
        return new CommonResult(true, "保存成功", null);

    }

    @RequestMapping(value = "jobList", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取任务列表", httpMethod = "POST", notes = "获取任务列表")
    public PageList<JobDetails> jobList(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<JobDetails> queryFilter) throws Exception {
        queryFilter.addFilter("jobGroup", baseContext.getCurrentTenantId(), QueryOP.EQUAL);
        return jobDetailsManager.query(queryFilter);
    }

    @RequestMapping(value = "getStand", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取定时器状态", httpMethod = "GET", notes = "获取定时器状态")
    public boolean getStand() throws Exception {
        boolean inStandbyMode = schedulerService.isInStandbyMode();
        return !inStandbyMode;
    }

    @RequestMapping(value = "schedulerJobJson", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取定时器任务列表", httpMethod = "GET", notes = "获取定时器任务列表")
    public List<SchedulerVo> schedulerJobJson() throws Exception {
        List<SchedulerVo> list = schedulerService.getJobList();
        return list;
    }

    @RequestMapping(value = "delJob", method = RequestMethod.DELETE, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "删除任务", httpMethod = "DELETE", notes = "删除任务")
    public CommonResult delJob(@ApiParam(name = "jobName", value = "任务名称", required = true) @RequestParam String jobName) throws Exception {

        try {
            schedulerService.delJob(jobName);
            return new CommonResult(true, "删除成功", null);
        } catch (Exception e) {
            return new CommonResult(false, "删除失败", null);
        }

    }

    @RequestMapping(value = "addTrigger", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "添加计划", httpMethod = "POST", notes = "添加计划")
    public CommonResult addTrigger(@ApiParam(name = "triggerModel", value = "定时计划") @RequestBody TriggerModel triggerModel) throws Exception {
        // 判断触发器是否存在
        boolean rtn = schedulerService.isTriggerExists(triggerModel.getTriggerName());
        if (rtn) {
            return new CommonResult(false, "指定的计划名称已经存在!", null);
        }
        try {
            schedulerService.addTrigger(triggerModel.getJobName(), triggerModel.getTriggerName(), triggerModel.getDescription());
            return new CommonResult(true, "添加计划成功", null);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new CommonResult(false, "添加计划失败", null);
        }
    }

    @RequestMapping(value = "getTriggersJsonByJob", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获得触发器实体", httpMethod = "POST", notes = "获得触发器实体")
    public List<TriggerModel> getTriggersJsonByJob(@ApiParam(name = "jobName", value = "计划名称", required = true) @RequestParam String jobName) throws SchedulerException {
        List<Trigger> list = schedulerService.getTriggersByJob(jobName);
        HashMap<String, Trigger.TriggerState> mapState = schedulerService.getTriggerStatus(list);
        List<TriggerModel> modelList = new ArrayList<TriggerModel>();
        for (Trigger trigger : list) {
            String trigName = trigger.getKey().getName();
            TriggerModel model = new TriggerModel();
            model.setJobName(trigger.getJobKey().getName());
            model.setTriggerName(trigName);
            model.setDescription(trigger.getDescription());
            Trigger.TriggerState state = (Trigger.TriggerState) mapState.get(trigName);
            model.setState(state.name());

            modelList.add(model);
        }
        return modelList;
    }


    @RequestMapping(value = "executeJob", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "执行任务", httpMethod = "POST", notes = "执行任务")
    public CommonResult executeJob(@ApiParam(name = "jobName", value = "计划名称", required = true) @RequestParam String jobName) throws Exception {
        try {
            schedulerService.executeJob(jobName);
            return new CommonResult(true, "执行任务成功!", null);
        } catch (Exception ex) {
            return new CommonResult(false, "执行任务失败!", null);
        }
    }

    @RequestMapping(value = "validClass", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "验证类", httpMethod = "GET", notes = "验证类")
    public CommonResult validClass(@ApiParam(name = "className", value = "类名", required = true) @RequestParam String className) throws Exception {
        boolean rtn = BeanUtils.validClass(className);
        if (rtn) {
            return new CommonResult(true, "验证类成功!", null);
        } else {
            return new CommonResult(false, "验证类失败!", null);
        }
    }

    @RequestMapping(value = "delTrigger", method = RequestMethod.DELETE, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "删除触发器", httpMethod = "DELETE", notes = "删除触发器")
    public CommonResult delTrigger(@ApiParam(name = "name", value = "触发器名称", required = true) @RequestParam String name) throws Exception {
        try {
            schedulerService.delTrigger(name);
            return new CommonResult(true, "删除计划成功!", null);
        } catch (Exception e) {
            return new CommonResult(false, "删除计划失败!", null);
        }
    }

    @RequestMapping(value = "toggleTriggerRun", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "启用或禁用", httpMethod = "POST", notes = "启用或禁用")
    public CommonResult toggleTriggerRun(@ApiParam(name = "name", value = "触发器名称", required = true) @RequestParam String name) throws Exception {
        try {
            schedulerService.toggleTriggerRun(name);
            return new CommonResult(true, "成功!", null);
        } catch (Exception e) {
            return new CommonResult(false, "失败!", null);
        }
    }

    @RequestMapping(value = "listJson", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取任务执行日志列表", httpMethod = "POST", notes = "获取任务执行日志列表")
    public PageList<SysJobLog> listJson(@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter,
                                        @ApiParam(name = "jobName", value = "任务名称") @RequestParam String jobName,
                                        @ApiParam(name = "trigName", value = "触发器名") @RequestParam String trigName) throws Exception {
        if (!"".equals(jobName) && jobName != null) {
            queryFilter.addFilter("job_Name_", jobName, QueryOP.EQUAL);
        }
        if (!"".equals(trigName) && trigName != null) {
            queryFilter.addFilter("trig_Name_", trigName, QueryOP.EQUAL);
        }
        return jobLogManager.query(queryFilter);
    }

    @RequestMapping(value = "delJobLog", method = RequestMethod.DELETE, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "删除任务日志", httpMethod = "DELETE", notes = "删除任务日志")
    public CommonResult del(@ApiParam(name = "id", value = "日志id", required = true) @RequestParam String ids) throws Exception {
        try {
            String[] aryIds = StringUtil.getStringAryByStr(ids);
            List<String> idList = Arrays.asList(aryIds);
            jobLogManager.removeByIds(idList);
            return new CommonResult(true, "删除任务日志成功", null);
        } catch (Exception e) {
            return new CommonResult(false, "删除任务日志失败", null);
        }
    }

    @RequestMapping(value = "changeStart", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "修改定时器的状态", httpMethod = "POST", notes = "修改定时器的状态")
    public CommonResult changeStart(@ApiParam(name = "isStandby", value = "挂起状态", required = true) @RequestParam Boolean isStandby) throws Exception {
        String message = null;
        try {
            // 如果是挂起状态就启动，否则就挂起
            if (isStandby) {
                schedulerService.start();
                message = "启动定时器成功！";
                boolean inStandbyMode = schedulerService.isInStandbyMode();
                System.out.println(inStandbyMode);
            } else {
                schedulerService.shutdown();
                boolean f = schedulerService.isInStandbyMode();
                message = "停止定时器成功!";
            }
            return new CommonResult(true, message, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (isStandby) {
                message = "启动定时器失败:";
            } else {
                message = "停止定时器失败:";

            }
            return new CommonResult(false, message, null);
        }
    }

    @RequestMapping(value = "isExist", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "是否存在", httpMethod = "GET", notes = "是否存在")
    public boolean isExist(@ApiParam(name = "name", value = "名称", required = true) @RequestParam String name,
                           @ApiParam(name = "type", value = "类型", required = true) @RequestParam String type
    ) throws Exception {
        boolean isExist = false;
        try {
            if ("className".equals(type)) {
                List<SchedulerVo> list = schedulerService.getJobList();
                if (BeanUtils.isNotEmpty(list)) {
                    for (SchedulerVo job : list) {
                        String className = job.getClassName();
                        if (name.equals(className)) {
                            isExist = true;
                            break;
                        }
                    }
                }
            } else if ("jobName".equals(type)) {
                isExist = schedulerService.isJobExists(name);
            }
        } catch (Exception e) {
        }
        return isExist;
    }


    @RequestMapping(value = "getJobDetail", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取定时计划明细", httpMethod = "GET", notes = "获取定时计划明细")
    public SchedulerVo getJobDetail(@ApiParam(name = "name", value = "计划名称", required = true) @RequestParam String name) throws Exception {
        return schedulerService.getJobDetail(name);
    }
}
