package com.hotent.job.persistence.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hotent.base.context.BaseContext;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.job.model.JobDetails;
import com.hotent.job.model.ParameterObj;
import com.hotent.job.model.SchedulerVo;
import com.hotent.job.persistence.dao.JobDetailsDao;
import com.hotent.job.persistence.manager.JobDetailsManager;
import com.hotent.job.persistence.manager.SchedulerService;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobDetailsManagerImpl   extends BaseManagerImpl<JobDetailsDao, JobDetails> implements JobDetailsManager {

    @Resource
    BaseContext baseContext;
    @Override
    public void addJob(SchedulerVo schedulerVo) throws IOException, ClassNotFoundException {
        Class<Job> cls = (Class<Job>) Class.forName(schedulerVo.getClassName());
        JobDetails jobDetails=new JobDetails();
        jobDetails.setSchedName("quartzScheduler");
        jobDetails.setJobName(schedulerVo.getJobName());
        jobDetails.setJobGroup(baseContext.getCurrentTenantId());
        jobDetails.setDescription(schedulerVo.getDescription());
        jobDetails.setJobClassNname(schedulerVo.getClassName());
        Map<String,Object> map = new HashMap<>();
        List<ParameterObj> list = JsonUtil.toBean(schedulerVo.getParameterJson(), new TypeReference<List<ParameterObj>>(){});
        for(ParameterObj obj : list) {
            String type = obj.getType();
            String name = obj.getName();
            String value = obj.getValue();
            if (type.equals(ParameterObj.TYPE_INT)) {
                Integer val = StringUtils.isEmpty(value) ? 0 : Integer
                        .parseInt(value);
                map.put(name, val);
            } else if (type.equals(ParameterObj.TYPE_LONG)) {
                Long val = StringUtils.isEmpty(value) ? 0L : Long
                        .parseLong(value);
                map.put(name, val);
            } else if (type.equals(ParameterObj.TYPE_FLOAT)) {
                Float val = StringUtils.isEmpty(value) ? 0.0f : Float
                        .parseFloat(value);
                map.put(name, val);
            } else if (type.equals(ParameterObj.TYPE_BOOLEAN)) {
                Boolean val = StringUtils.isEmpty(value) ? false : Boolean
                        .parseBoolean(value);
                map.put(name, val);
            } else {
                map.put(name, value);
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(map);
        byte[] bt=os.toByteArray();
        oos.close();
        os.close();
        jobDetails.setJobData(bt);
        super.create(jobDetails);
    }

    @Override
    public boolean isJobExists(String jobName) {
        JobDetails jobDetails=baseMapper.selectOne(Wrappers.<JobDetails>lambdaQuery()
                .eq(JobDetails::getJobName, jobName));
        return BeanUtils.isNotEmpty(jobDetails);
    }
}
