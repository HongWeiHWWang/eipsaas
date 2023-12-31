package com.hotent.activiti.cache;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.CachePut;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BpmDefCache;
import com.hotent.base.util.FileUtil;
import com.hotent.bpm.api.constant.BpmConstants;

/**
 * 流程引擎的自定义缓存接口，可以配置到activiti的配置文件中
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月17日
 */
public class ActivitiDefCache implements DeploymentCache<ProcessDefinitionEntity>, BpmDefCache {
	@Override
	public ProcessDefinitionEntity get(String id) {
		ActivitiDefCache bean = AppUtil.getBean(getClass());
		// 获取缓存数据时，必须通过Spring容器中获取到的bean来调用（否则缓存方法上的注解不会生效），而且缓存方法只能是protected后public方法
		ProcessDefinitionEntity ent = bean.getFromCache(id);
		if(ent==null) return null;
		ProcessDefinitionEntity cloneEnt=null;
		try {
			//克隆流程定义。
			cloneEnt=(ProcessDefinitionEntity)FileUtil.cloneObject(ent);
		} catch (Exception e) {
			throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
		}
		// 从线程变量获取
		ProcessDefinitionEntity p=getThreadLocalDef(id);
		if(p==null){
			setThreadLocalDef(cloneEnt);
		}
		p=getThreadLocalDef(id);
		return p;
	}

	@Override
	public void add(String id, ProcessDefinitionEntity object) {
		ActivitiDefCache bean = AppUtil.getBean(getClass());
		// 获取缓存数据时，必须通过Spring容器中获取到的bean来调用（否则缓存方法上的注解不会生效），而且缓存方法只能是protected后public方法
		bean.putInCache(id, object);
	}

	@Cacheable(value = BpmConstants.PROCESSDEFINITIONENTITY_CACHENAME, key = "#id", ignoreException = false)
	public ProcessDefinitionEntity getFromCache(String id) {
		return null;
	}

	@CachePut(value = BpmConstants.PROCESSDEFINITIONENTITY_CACHENAME, key = "#id", ignoreException = false)
	public ProcessDefinitionEntity putInCache(String id, ProcessDefinitionEntity object) {
		return object;
	}

	@Override
	@CacheEvict(value=BpmConstants.PROCESSDEFINITIONENTITY_CACHENAME, key = "#id", ignoreException = false)
	public void remove(String id) {
		clearProcessDefinitionEntity(id);
	}

	@Override
	@CacheEvict(value=BpmConstants.PROCESSDEFINITIONENTITY_CACHENAME, allEntries = true, ignoreException = false)
	public void clear() {
		clearProcessCache();
	}

	@CacheEvict(value=BpmConstants.PROCESSDEFINITIONENTITY_CACHENAME, allEntries = true, ignoreException = false)
	public void cleanAll() {
		clearProcessCache();
	}
	
	private ThreadLocal<Map<String,ProcessDefinitionEntity>> processDefinitionCacheLocal = new ThreadLocal<Map<String,ProcessDefinitionEntity>>();

	private void clearProcessDefinitionEntity(String defId){
		processDefinitionCacheLocal.remove();
	}

	private void clearProcessCache(){
		processDefinitionCacheLocal.remove();
	}

	private void setThreadLocalDef(ProcessDefinitionEntity processEnt){
		if(processDefinitionCacheLocal.get()==null){
			Map<String, ProcessDefinitionEntity> map=new HashMap<String, ProcessDefinitionEntity>();
			map.put(processEnt.getId(),processEnt);
			processDefinitionCacheLocal.set(map);
		}
		else{
			Map<String, ProcessDefinitionEntity> map=processDefinitionCacheLocal.get();
			map.put(processEnt.getId(),processEnt);
		}
	}

	private ProcessDefinitionEntity getThreadLocalDef(String processDefinitionId){
		if(processDefinitionCacheLocal.get()==null){
			return null;
		}
		Map<String, ProcessDefinitionEntity> map=processDefinitionCacheLocal.get();
		if(!map.containsKey(processDefinitionId)){
			return null;
		}
		return map.get(processDefinitionId);
	}
}
