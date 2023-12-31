package com.hotent.bpm.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.event.BpmDefinitionDelEvent;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.natapi.def.NatProDefinitionService;

@Service
public class BpmDefinitionListener implements ApplicationListener<BpmDefinitionDelEvent>{

	@Resource
	NatProDefinitionService natProDefinitionService;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	
	@Override
	public void onApplicationEvent(BpmDefinitionDelEvent endEvent) {
		BpmDefinition bpmDef=(BpmDefinition) endEvent.getSource();
		delFromCache(bpmDef);
	}
	
	/**
	 * 清除缓存。
	 * @param defId 
	 * void
	 */
	private void delFromCache(BpmDefinition def){
		String bpmnDefId=def.getBpmnDefId();
		String defId=def.getDefId();
		
		BpmDefinitionListener bean = AppUtil.getBean(getClass());
		
		bean.delByDefId(defId);
		bean.delByBpmnDefId(bpmnDefId);
		bean.delByFlowKey(def.getDefKey());
		
		natProDefinitionService.clearCacheByBpmnDefId(bpmnDefId);
		bpmDefinitionAccessor.clean(defId);
	}
	
	@CacheEvict(value = BpmDefinition.BPM_DEF_CACHENAME, key = "#defId", ignoreException = false)
	protected void delByDefId(String defId) {}
	
	@CacheEvict(value = BpmDefinition.BPMN_ID_CACHENAME, key = "#bpmnDefId", ignoreException = false)
	protected void delByBpmnDefId(String bpmnDefId) {}
	
	@CacheEvict(value = BpmDefinition.FLOW_KEY_CACHENAME, key = "#flowKey", ignoreException = false)
	protected void delByFlowKey(String flowKey) {}
}
