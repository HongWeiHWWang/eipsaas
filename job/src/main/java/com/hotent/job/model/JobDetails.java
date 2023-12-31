package com.hotent.job.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hotent.base.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("qrtz_job_details")
@ApiModel(description="系统定时任务")
public class JobDetails extends Model<JobDetails> {

    @ApiModelProperty("调度名称")
    @TableField("sched_name")
    protected String schedName;

    @ApiModelProperty("任务名")
    @TableField("job_name")
    protected String jobName;

    @ApiModelProperty("集群中job的所属组的名字")
    @TableField("job_group")
    protected String jobGroup;


    @ApiModelProperty("集群中job的所属组的名字")
    @TableField("description")
    protected String description;

    @ApiModelProperty("集群中job的所属组的名字")
    @TableField("job_class_name")
    protected String jobClassNname;


    @ApiModelProperty("是否持久化,把该属性设置为1，quartz会把job持久化到数据库中")
    @TableField("is_durable")
    protected String durable="1";


    @ApiModelProperty("是否并发")
    @TableField("is_nonconcurrent")
    protected String nonconcurrent="1";


    @ApiModelProperty("是否更新数据")
    @TableField("is_update_data")
    protected String updateData="0";


    @ApiModelProperty("是否接受恢复执行")
    @TableField("requests_recovery")
    protected String requestsRecovery="0";


    @ApiModelProperty("参数对象")
    @TableField("job_data")
    protected byte[] jobData;


    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClassNname() {
        return jobClassNname;
    }

    public void setJobClassNname(String jobClassNname) {
        this.jobClassNname = jobClassNname;
    }

    public String getDurable() {
        return durable;
    }

    public void setDurable(String durable) {
        this.durable = durable;
    }

    public String getNonconcurrent() {
        return nonconcurrent;
    }

    public void setNonconcurrent(String nonconcurrent) {
        this.nonconcurrent = nonconcurrent;
    }

    public String getUpdateData() {
        return updateData;
    }

    public void setUpdateData(String updateData) {
        this.updateData = updateData;
    }

    public String getRequestsRecovery() {
        return requestsRecovery;
    }

    public void setRequestsRecovery(String requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }

    public byte[] getJobData() {
        return jobData;
    }

    public void setJobData(byte[] jobData) {
        this.jobData = jobData;
    }
}
