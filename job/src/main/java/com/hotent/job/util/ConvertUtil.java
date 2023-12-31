package com.hotent.job.util;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.job.model.SchedulerVo;

public class ConvertUtil {
	public static SchedulerVo toBean(JobDetail detail){
		SchedulerVo v=new SchedulerVo();
		v.setClassName(detail.getJobClass().getName());
		v.setDescription(detail.getDescription());
		v.setJobName(detail.getKey().getName());
		return v;
	}
	
	public static SchedulerVo toJobBean(JobDetail detail) throws IOException{
		SchedulerVo v=new SchedulerVo();
		v.setClassName(detail.getJobClass().getName());
		v.setDescription(detail.getDescription());
		v.setJobName(detail.getKey().getName());
		JobDataMap dataMap = detail.getJobDataMap();
		Set<Entry<String, Object>>  set = dataMap.entrySet();
		ArrayNode array = JsonUtil.getMapper().createArrayNode();
		for (Entry<String, Object> entry : set) {
			ObjectNode node = JsonUtil.getMapper().createObjectNode();
			node.put("name", entry.getKey());
			if(BeanUtils.isNotEmpty(entry.getValue())){
				if ("true".equals(entry.getValue()) || "false".equals(entry.getValue())) {
					node.put("type", "blooean");
					node.put("value", Boolean.valueOf(entry.getValue().toString()));
				}else if (entry.getValue() instanceof String) {
					node.put("type", "string");
					node.put("value", (String)entry.getValue());
				}else if(entry.getValue() instanceof Integer){
					node.put("type", "int");
					node.put("value", (Integer)entry.getValue());
				}else if(entry.getValue() instanceof Boolean){
					node.put("type", "blooean");
					node.put("value", (Boolean)entry.getValue());
				}else if(entry.getValue() instanceof Float){
					node.put("type", "float");
					node.put("value", (Float)entry.getValue());
				}else if(entry.getValue() instanceof Long){
					node.put("type", "long");
					node.put("value", (Long)entry.getValue());
				}
			}else{
				node.put("type", "string");
				node.put("value", "");
			}
			array.add(node);
		}
		if(BeanUtils.isNotEmpty(array)){
			v.setParameterJson(JsonUtil.toJson(array));
		}
		return v;
	}
}
