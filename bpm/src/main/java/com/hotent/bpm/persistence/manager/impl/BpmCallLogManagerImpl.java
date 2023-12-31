package com.hotent.bpm.persistence.manager.impl;

import java.util.Iterator;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.dao.BpmCallLogDao;
import com.hotent.bpm.persistence.manager.BpmCallLogManager;
import com.hotent.bpm.persistence.model.BpmCallLog;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * <pre> 
 * 描述：sys_call_log 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2017-10-26 11:40:50
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmCallLogManager")
public class BpmCallLogManagerImpl extends BaseManagerImpl<BpmCallLogDao, BpmCallLog> implements BpmCallLogManager{
	@Resource
	BpmCallLogDao bpmCallLogDao;

	
	@Override
    @Transactional
	public void reinvoke(String id) throws Exception {
		BpmCallLog bpmCallLog = this.get(id);
		try {
			String response = reinvokeByLog(bpmCallLog);
			bpmCallLog.setResponse(response);
			int time = BeanUtils.isEmpty(bpmCallLog.getRetryCount())?0:bpmCallLog.getRetryCount().intValue();
			bpmCallLog.setRetryCount(time+1);
			bpmCallLog.setIsSuccess(true);
			this.update(bpmCallLog);
		} catch (Exception e) {
			bpmCallLog.setIsSuccess(false);
			this.update(bpmCallLog);
			throw new WorkFlowException("重调失败：" + ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
    @Transactional
	public void signSuccess(String id) {
		BpmCallLog bpmCallLog = this.get(id);
		if(BeanUtils.isNotEmpty(bpmCallLog)){
			bpmCallLog.setIsSuccess(true);
			this.update(bpmCallLog);
		}
	}
	
	private String reinvokeByLog(BpmCallLog bpmCallLog) throws Exception{
		Request request = Request.Post(bpmCallLog.getUrl());
		request = setHeaders(request,bpmCallLog.getHeader());
		String response = request.bodyString(bpmCallLog.getParams(), ContentType.APPLICATION_JSON)
			   .execute().returnContent().toString();
		return response;
	}
	
	@SuppressWarnings("rawtypes")
	private Request setHeaders(Request request,String headerStr){
		if(StringUtil.isNotEmpty(headerStr)){
			try {
				headerStr = Base64.getFromBase64(headerStr);
				if(StringUtil.isEmpty(headerStr) || "\"\"".equals(headerStr)) return request;
				ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(headerStr);
				 Iterator it = obj.fieldNames();
				 while(it.hasNext()){
					 String key = (String)it.next();
					 request.setHeader(key, obj.get(key).asText());
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return request;
	}
}
