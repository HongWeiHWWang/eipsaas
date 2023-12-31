package com.hotent.bpm.plugin.task.userassign;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.hotent.bpm.plugin.task.userassign.context.UserCopyToPluginContext;
import com.hotent.bpm.plugin.task.userassign.def.UserAssignPluginDef;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 传阅人员定义插件保存
 * <pre>
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-08-21 10:35:22
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class UserCopyToDefBpmDefXmlHandler extends AbstractBpmDefXmlHandler <String> {
	
	@Override
	protected String getXml(String defId, String nodeId, String json) {
		throw new RuntimeException("该方法已经过时！");
	}
	

	public void saveNodeXml(String defId, String nodeId, String nodeJson,String parentFlowKey) throws Exception {
		if(StringUtil.isEmpty(parentFlowKey)) parentFlowKey = BpmConstants.LOCAL;
		
		BpmDefinition def= bpmDefinitionManager.getById(defId);
		String xml=getXmlByJson(nodeJson,defId,nodeId,parentFlowKey); 
		
		String defxml=def.getBpmnXml();
		Document doc=Dom4jUtil.loadXml(defxml);
		Element root= doc.getRootElement();
		root.addNamespace("copyTo", "http://www.jee-soft.cn/bpm/plugins/task/userCopyTo");
		
		String xPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:extPlugins/copyTo:userCopyTo" ;
		String xParentPath="//ext:*[@bpmnElement='"+nodeId+"']/ext:extPlugins" ;
		
		
		String rtnXml=DefXmlHandlerUtil.getXml(root, xml, xParentPath, xPath);
		updateXml(defId,rtnXml);
	}

    /**
     * 根据json获取人员的XML数据。
     * @param json
     * @return String
     * @throws Exception
     */
    private String getXmlByJson(String json ,String defId,String nodeId,String parentFlowKey) throws Exception{
        UserCopyToPluginContext context=AppUtil.getBean(UserCopyToPluginContext.class);
        context.parse(json);
        handelContext(context,parentFlowKey,defId,nodeId);

        String xml=context.getPluginXml();
        return xml;
    }
    /**
     * 如果是子流程的情况，前端只维护了当前parentFlowKey的 规则列表。 所以把其他的都加进去
     * @throws Exception
     * **/
    private void handelContext(UserCopyToPluginContext context, String parentFlowKey, String defId, String nodeId) throws Exception{

        UserAssignPluginDef def = (UserAssignPluginDef) context.getBpmPluginDef();
        List<UserAssignRule> ruleList = def.getRuleList();
        for (UserAssignRule rule : ruleList) {
            rule.setParentFlowKey(parentFlowKey);
        }

        BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
        UserCopyToPluginContext oldContext = (UserCopyToPluginContext) nodeDef.getPluginContext(UserCopyToPluginContext.class);
        if(oldContext == null) return;

        UserAssignPluginDef oldDef = (UserAssignPluginDef) oldContext.getBpmPluginDef();
        List<UserAssignRule> oldRuleList = oldDef.getRuleList();
        if(BeanUtils.isEmpty(oldRuleList)) return ;

        for (UserAssignRule oldRule : oldRuleList) {
            if(StringUtil.isEmpty(oldRule.getParentFlowKey()))oldRule.setParentFlowKey(BpmConstants.LOCAL);
            if(!oldRule.getParentFlowKey().equals(parentFlowKey)){
                ruleList.add(oldRule);
            }
        }

    }
}
