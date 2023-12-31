package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.nodedef.JumpRule;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.jamesmurty.utils.XMLBuilder;

/**
 * 节点跳转规则处理器。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-16-上午9:07:42
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class TransRulesBpmDefXmlHandler extends AbstractBpmDefXmlHandler<List<JumpRule>> {

	
	
	private String getXml(List<JumpRule> jumpRuleList){
		String xml = "";
		if(BeanUtils.isEmpty(jumpRuleList)) return xml;

		try {
			xml +="<ext:transformRules xmlns:ext=\""+BpmConstants.BPM_XMLNS+"\">";
			for(JumpRule jumpRule : jumpRuleList){
				XMLBuilder ruleBuilder = XMLBuilder.create("ext:rule")
						.a("name", jumpRule.getRuleName())
						.a("targetnode", jumpRule.getTargetNode())
						.e("ext:condition").d(jumpRule.getCondition()).up();
				xml += ruleBuilder.asString();
			}
			xml +="</ext:transformRules>";
		} catch (Exception e) {
				e.printStackTrace();
		} 
		return xml;
	}

	@Override
	protected String getXml(String defId, String nodeId, List<JumpRule> jumpRuleList) {
		String xml = getXml(jumpRuleList);
		
		BpmDefinition bpmDef= bpmDefinitionManager.getById(defId);
		String defXml=bpmDef.getBpmnXml();
		
		
		String xPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:transformRules";
		String xParentPath="//ext:*[@bpmnElement='"+nodeId+"']";
		
		String rtnXml=DefXmlHandlerUtil.getXml(defXml, xml, xParentPath, xPath);
		
		
		return rtnXml;
	}

	

}
