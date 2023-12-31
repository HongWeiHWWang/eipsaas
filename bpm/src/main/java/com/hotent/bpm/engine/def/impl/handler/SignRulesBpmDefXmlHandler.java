package com.hotent.bpm.engine.def.impl.handler;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;


import com.hotent.base.util.Dom4jUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.SignRule;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;

/**
 * 会签节点规则保存。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-15-下午11:12:08
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class SignRulesBpmDefXmlHandler extends AbstractBpmDefXmlHandler<SignRule>{

	

	@Override
	protected String getXml(String defId, String nodeId, SignRule rule) {
		String xml=getXml( rule);
		
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String defXml=def.getBpmnXml();
		
		Document doc=Dom4jUtil.loadXml(defXml);
		Element root=doc.getRootElement();
		
		String xPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:signSetting/ext:signRule" ;
		String xParentPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:signSetting" ;
		
		
		
		String rtnXml=DefXmlHandlerUtil.getXml(root, xml, xParentPath, xPath);
		return rtnXml;
	}
	
	
	private String getXml(SignRule rule){
		String xml="<ext:signRule xmlns:ext=\""+BpmConstants.BPM_XMLNS+"\" decideType=\""+ rule.getDecideType().getKey()
				+"\" voteType=\""+ rule.getVoteType().getKey()
				+"\" voteAmount=\""+rule.getVoteAmount() +"\" followMode=\""+rule.getFollowMode().getKey()+"\"/>";
		
		return xml;
	}

}
