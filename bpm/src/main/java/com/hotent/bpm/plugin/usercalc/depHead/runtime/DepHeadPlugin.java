package com.hotent.bpm.plugin.usercalc.depHead.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.usercalc.depHead.def.DepHeadPluginDef;

public class DepHeadPlugin  extends AbstractUserCalcPlugin{

	@Resource
	UCFeignService ucFeignService;
	
	@Override
	public List<BpmIdentity> queryByPluginDef(
			BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) {
		List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();
		Map<String,Object> map = pluginSession.getVariables();
		DepHeadPluginDef def = (DepHeadPluginDef)pluginDef;
		boolean isMain = def.isMainLeader();
		ArrayNode arrayNode = null;
		boolean isByOrg = false;
		if(map.containsKey(BpmConstants.PROCESS_INST) && BeanUtils.isNotEmpty(map.get(BpmConstants.PROCESS_INST))){
			DefaultBpmProcessInstance processInstance = (DefaultBpmProcessInstance) map.get(BpmConstants.PROCESS_INST);
			if(StringUtil.isNotEmpty(processInstance.getCreateOrgId())){
				isByOrg = true;
				arrayNode = ucFeignService.getDepHeaderByOrg(processInstance.getCreateOrgId(),isMain);
			}
		}
		if(!isByOrg && BeanUtils.isEmpty(arrayNode) && map.containsKey(BpmConstants.START_USER) && BeanUtils.isNotEmpty(map.get(BpmConstants.START_USER))){
			String userId = map.get(BpmConstants.START_USER).toString();
			arrayNode = ucFeignService.getDepHeader(userId,isMain);
		}
		if(BeanUtils.isNotEmpty(arrayNode)){
			for (JsonNode jsonNode : arrayNode) {
				DefaultBpmIdentity bpmIdentity = new DefaultBpmIdentity();
				bpmIdentity.setId(jsonNode.get("id").asText());
				bpmIdentity.setCode(jsonNode.get("account").asText());
				bpmIdentity.setName(jsonNode.get("fullname").asText());
				bpmIdentity.setType(BpmIdentity.TYPE_USER);
				bpmIdentities.add(bpmIdentity);	
			}
		}
		return bpmIdentities;
	}

	@Override
	public boolean supportPreView() {
		return false;
	}

}
