package com.hotent.Calendar.params;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 日历设置参数类
 * @company 广州宏天软件股份有限公司
 * @author zhangxw
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月5日
 */
@ApiModel(value="日历设置参数类")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarParam {

	@ApiModelProperty(name="id",notes="id")
	private String id;
	
	@ApiModelProperty(name="name",notes="名称")
	private String name;
	
	@ApiModelProperty(name="memo",notes="memo")
	private String memo;
	
	@ApiModelProperty(name="year",notes="年份")
	private Integer year;
	
	@ApiModelProperty(name="month",notes="月份")
	private Integer month;
	
	@ApiModelProperty(name="days",notes="日期列表")
	private List<DayParam> days;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public List<DayParam> getDays() {
		return days;
	}

	public void setDays(List<DayParam> days) {
		this.days = days;
	}
}
