package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.stereotype.Service;

import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmSubTableRight;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;

/**
 * 脚本节点插件。 参数保存脚本即可。
 * 
 * <pre>
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-26-上午9:49:19
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class SubRightBpmDefXmlHandler extends AbstractBpmDefXmlHandler<Map<String, Object>> {

	@SuppressWarnings("unchecked")
	@Override
	protected String getXml(String defId, String nodeId, Map<String, Object> param) {

		List<BpmSubTableRight> rights =  (List<BpmSubTableRight>) param.get("list");
		BpmDefinition def = bpmDefinitionManager.getById(defId);
		String defXml = def.getBpmnXml();

		String parentDefKey = (String) param.get("parentDefKey");
		if (StringUtil.isEmpty(parentDefKey)) {
			parentDefKey = BpmConstants.LOCAL;
		}
		Document doc = Dom4jUtil.loadXml(defXml);
		Element root = doc.getRootElement();

		String xPath = "//ext:*[@bpmnElement='" + nodeId + "']/ext:subTableRights";
		String xParentPath = "//ext:*[@bpmnElement='" + nodeId + "']";
		Node el = root.selectSingleNode(xPath);
		Element elParent = (Element) root.selectSingleNode(xParentPath);
		
		if (el == null) {
			String propxml = "<ext:subTableRights xmlns:ext=\"" + BpmConstants.BPM_XMLNS + "\"/>";
			elParent.add(Dom4jUtil.loadXml(propxml).getRootElement());
		}
		String xml = "";
		// 先清除再添加
		String xPathItem = "//ext:*[@bpmnElement='" + nodeId + "']/ext:subTableRights/ext:rightsItem[@parentDefKey='" + parentDefKey + "']";
		if (rights.isEmpty()) {
			elParent.remove(elParent.selectSingleNode("ext:subTableRights"));
		} else {
			DefXmlHandlerUtil.handXmlDom(root, xml, xPath, xPathItem);
			for (BpmSubTableRight tableRight : rights) {
				String tableName = tableRight.getTableName();
				xPathItem = "//ext:*[@bpmnElement='" + nodeId + "']/ext:subTableRights/ext:rightsItem[@parentDefKey='" + parentDefKey + "' and @tableName='" + tableName + "']";
				// 添加子表权限项。
				xml = BpmDefSettingBpmDefXmlHandler.getTableRight(tableRight);
				DefXmlHandlerUtil.handXmlDom(root, xml, xPath, xPathItem);
			}
		}
		return root.asXML();
	}

}
