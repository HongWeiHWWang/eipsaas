package com.hotent.form.param;

import com.hotent.base.query.QueryFilter;
import com.hotent.form.model.QuerySqldef;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="自定义sql视图查询vo")
public class DataTemplateQueryVo {
    @ApiModelProperty(name="templateId", notes="业务对象主键")
    private String templateId;

    @ApiModelProperty(name="queryFilter", notes="通用查询对象")
    private QueryFilter queryFilter;

    @ApiModelProperty(name="isJoinFlow", notes="是否关联流程")
    private boolean isJoinFlow;


    @ApiModelProperty(name="taskType", notes="任务类型 todo代办 done已办 request我的请求 todoRead待阅 doneRead已阅 myRead我传阅的 myDelegate我转办的")
    private String taskType;

    @ApiModelProperty(name="defKey", notes="流程定义key")
    private String defKey;
    
    @ApiModelProperty(name="selectField", notes="关联查询字段名称")
    private String selectField;
    
    @ApiModelProperty(name="selectValue", notes="关联查询字段值")
    private String selectValue;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public QueryFilter getQueryFilter() {
        return queryFilter;
    }

    public void setQueryFilter(QueryFilter queryFilter) {
        this.queryFilter = queryFilter;
    }

    public boolean isJoinFlow() {
        return isJoinFlow;
    }

    public void setIsJoinFlow(boolean joinFlow) {
        isJoinFlow = joinFlow;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDefKey() {
        return defKey;
    }

    public void setDefKey(String defKey) {
        this.defKey = defKey;
    }

	public String getSelectField() {
		return selectField;
	}

	public void setSelectField(String selectField) {
		this.selectField = selectField;
	}

	public String getSelectValue() {
		return selectValue;
	}

	public void setSelectValue(String selectValue) {
		this.selectValue = selectValue;
	}
    
}
