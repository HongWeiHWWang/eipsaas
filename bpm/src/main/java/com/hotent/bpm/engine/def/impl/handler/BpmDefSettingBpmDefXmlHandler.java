package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.def.BpmDefSetting;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmSubTableRight;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.engine.def.AbstractBpmDefXmlHandler;
import com.hotent.bpm.engine.def.BpmDefUtil;
import com.hotent.bpm.engine.def.DefXmlHandlerUtil;
import com.hotent.bpm.model.form.Form;

/**
 * 流程节点属性表单配置。
 * 
 * <pre>
 * 1.全局表单设置。
 * 2.实例表单设置。
 * 3.节点属性配置。
 * 4.节点表单设置。
 * 5.子表权限设置。
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-8-20-上午10:31:55
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class BpmDefSettingBpmDefXmlHandler extends AbstractBpmDefXmlHandler<BpmDefSetting> {
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;

	@Override
	protected String getXml(String defId, String nodeId, BpmDefSetting bpmDefSetting) throws Exception {
		BpmDefinition bpmDef = bpmDefinitionManager.getById(defId);
		List<BpmNodeDef> list = getNodeList(defId);
		String defXml = bpmDef.getBpmnXml();
		Document doc = Dom4jUtil.loadXml(defXml);
		Element root = doc.getRootElement();
		// 全局表单
		handGlobalForm(bpmDefSetting, root);
		// 实例表单
		handInstForm(bpmDefSetting, root);
		// 处理节点属性。
		handNodeProperties(bpmDefSetting, root, list);
		// 处理节点表单。
		handNodeForm(bpmDefSetting, root, list);
		// 子表单权限。
		handTableSubRights(bpmDefSetting, root, list);
		return root.asXML();
	}
	/**
	 * 处理流程节点表单包括普通节点和子流程节点的表单。
	 * @param bpmDefSetting
	 * @param root void
	 */
	void handNodeForm(BpmDefSetting bpmDefSetting, Element root, List<BpmNodeDef> list) {
		String parentDefKey = bpmDefSetting.getParentDefKey();
		// 获取PC表单map
		Map<String, Form> map = bpmDefSetting.getFormMap(true);
		// 获取手机表单。
		Map<String, Form> mobileMap = bpmDefSetting.getFormMap(false);
		for (BpmNodeDef nodeDef : list) {
			String nodeId = nodeDef.getNodeId();
			// pc表单
			handNodeForm(map, root, parentDefKey, nodeId, true);
			// 手机表单
			handNodeForm(mobileMap, root, parentDefKey, nodeId, false);
		}
	}
	void handNodeForm(Map<String, Form> map, Element root, String parentDefKey, String nodeId, boolean isPc) {
		String formPre = isPc ? "form" : "mobileForm";
		boolean isContain = map.containsKey(nodeId);
		String xml = "";
		if (isContain) {
			xml = BpmDefUtil.getFormXml(map.get(nodeId));
		}
		// 非外部流程
		if (BpmConstants.LOCAL.equals(parentDefKey)) {
			String xPath = "//ext:*[@bpmnElement='" + nodeId + "']/ext:" + formPre;
			String xParentPath = "//ext:*[@bpmnElement='" + nodeId + "']";
			DefXmlHandlerUtil.handXmlDom(root, xml, xParentPath, xPath);
		}
		// 外部流程。
		else {
			String xPath = "//ext:*[@bpmnElement='" + nodeId + "']/ext:subProcessForm";
			String xParentPath = "//ext:*[@bpmnElement='" + nodeId + "']";
			Element elSubForm = (Element) root.selectSingleNode(xPath);
			Element elParent = (Element) root.selectSingleNode(xParentPath);
			// 没有子流程节点定义。
			if (elSubForm == null) {
				String propxml = "<ext:subProcessForm xmlns:ext=\"" + BpmConstants.BPM_XMLNS + "\" />";
				Document doc = Dom4jUtil.loadXml(propxml);
				elParent.add(doc.getRootElement());
			}
			String xPathItem = "//ext:*[@bpmnElement='" + nodeId + "']/ext:subProcessForm/ext:" + formPre + "[@parentFlowKey='" + parentDefKey + "']";
			DefXmlHandlerUtil.handXmlDom(root, xml, xPath, xPathItem);
		}
	}
	/**
	 * 处理全局表单
	 * @param bpmDefSetting
	 * @param root void
	 */
	void handGlobalForm(BpmDefSetting bpmDefSetting, Element root) {
		String xParentPath = "//ext:extProcess";
		String xPath = "//ext:extProcess/ext:globalForm";
		Element el = (Element) root.selectSingleNode(xPath);
		Element elParent = (Element) root.selectSingleNode(xParentPath);
		if (el == null) {
			String propxml = "<ext:globalForm xmlns:ext=\"" + BpmConstants.BPM_XMLNS + "\" />";
			Document doc = Dom4jUtil.loadXml(propxml);
			elParent.add(doc.getRootElement());
		}
		String parentDefKey = bpmDefSetting.getParentDefKey();
		String xFormPath = "//ext:globalForm/ext:form[@parentFlowKey='" + parentDefKey + "']";
		FormExt frm = bpmDefSetting.getGlobalForm();
		
		
		String xml = BpmDefUtil.getFormXml(frm);
		DefXmlHandlerUtil.handXmlDom(root, xml, xPath, xFormPath);
		
		String xMobileFormPath = "//ext:globalForm/ext:mobileForm[@parentFlowKey='" + parentDefKey + "']";
		FormExt mobileFrm = bpmDefSetting.getGlobalMobileForm();
	
		String mobileXml = BpmDefUtil.getFormXml(mobileFrm);
		DefXmlHandlerUtil.handXmlDom(root, mobileXml, xPath, xMobileFormPath);
	}
	
	/**
	 * 处理实例表单。
	 * @param bpmDefSetting
	 * @param root void
	 */
	void handInstForm(BpmDefSetting bpmDefSetting, Element root) {
		String xParentPath = "//ext:extProcess";
		String xPath = "//ext:extProcess/ext:instForm";
		Element el = (Element) root.selectSingleNode(xPath);
		Element elParent = (Element) root.selectSingleNode(xParentPath);
		if (el == null) {
			String propxml = "<ext:instForm xmlns:ext=\"" + BpmConstants.BPM_XMLNS + "\" />";
			Document doc = Dom4jUtil.loadXml(propxml);
			elParent.add(doc.getRootElement());
		}
		String parentDefKey = bpmDefSetting.getParentDefKey();
		String xFormPath = "//ext:instForm/ext:form[@parentFlowKey='" + parentDefKey + "']";
		FormExt frm = bpmDefSetting.getInstForm();
		
		
		String xml = BpmDefUtil.getFormXml(frm);
		//清除以前旧版本遗留xml
		if(el!=null){
			String formPath = "//ext:instForm/ext:form";
			String mobileFormPath = "//ext:instForm/ext:mobileForm";
			Element formEl = (Element) el.selectSingleNode(formPath);
			Element mobileFormEl = (Element) el.selectSingleNode(mobileFormPath);
			if(StringUtil.isEmpty(xml) && formEl==null && BeanUtils.isEmpty(mobileFormEl)){
				elParent.remove(el);
			}
		}
		String instmobilePath = "//ext:extProcess/ext:instMobileForm";
		Element mobileel = (Element) root.selectSingleNode(instmobilePath);
		//如果存在旧的实例手机表单，则清空
		if(mobileel!=null){
			elParent.remove(mobileel);
		}
		DefXmlHandlerUtil.handXmlDom(root, xml, xPath, xFormPath);
		
		String xMobileFormPath = "//ext:instForm/ext:mobileForm[@parentFlowKey='" + parentDefKey + "']";
		FormExt mobileFrm = bpmDefSetting.getInstMobileForm();
	
		String mobileXml = BpmDefUtil.getFormXml(mobileFrm);
		DefXmlHandlerUtil.handXmlDom(root, mobileXml, xPath, xMobileFormPath);
		
		
	}
	/**
	 * 处理子表权限。
	 * @param bpmDefSetting
	 * @param root void
	 */
	void handTableSubRights(BpmDefSetting bpmDefSetting, Element root, List<BpmNodeDef> list) {
		Map<String, List<BpmSubTableRight>> map = bpmDefSetting.getBpmSubTableRightMap();
		String parentDefKey = bpmDefSetting.getParentDefKey();
		for (BpmNodeDef nodeDef : list) {
			// if(NodeType.Start.equals(nodeDef.getType())) continue;
			String nodeId = nodeDef.getNodeId();
			boolean isContain = map.containsKey(nodeId);
			if (!isContain) continue;
			List<BpmSubTableRight> subTableRights = map.get(nodeId);
			if (subTableRights == null) continue;
			String xPath = "//ext:*[@bpmnElement='" + nodeId + "']/ext:subTableRights";
			String xParentPath = "//ext:*[@bpmnElement='" + nodeId + "']";
			Node el = root.selectSingleNode(xPath);
			Element elParent = (Element) root.selectSingleNode(xParentPath);
			if (el == null) {
				String propxml = "<ext:subTableRights xmlns:ext=\"" + BpmConstants.BPM_XMLNS + "\" />";
				Document doc = Dom4jUtil.loadXml(propxml);
				elParent.add(doc.getRootElement());
			}
			String xml = "";
			// 先清除再添加
			String xPathItem = "//ext:*[@bpmnElement='" + nodeId + "']/ext:subTableRights/ext:rightsItem[@parentDefKey='" + parentDefKey + "']";
			DefXmlHandlerUtil.handXmlDom(root, xml, xPath, xPathItem);
			for (BpmSubTableRight tableRight : subTableRights) {
				String tableName = tableRight.getTableName();
				xPathItem = "//ext:*[@bpmnElement='" + nodeId + "']/ext:subTableRights/ext:rightsItem[@parentDefKey='" + parentDefKey + "' and @tableName='" + tableName + "']";
				// 添加子表权限项。
				xml = getTableRight(tableRight);
				DefXmlHandlerUtil.handXmlDom(root, xml, xPath, xPathItem);
			}
		}
	}
	/**
	 * 处理节点属性。
	 * @param bpmDefSetting
	 * @param root
	 * @param list
	 */
	void handNodeProperties(BpmDefSetting bpmDefSetting, Element root, List<BpmNodeDef> list) {
		Map<String, NodeProperties> map = bpmDefSetting.getNodePropertieMap();
		for (BpmNodeDef nodeDef : list) {
			// if(NodeType.Start.equals( nodeDef.getType())) continue;
			String nodeId = nodeDef.getNodeId();
			boolean isContain = map.containsKey(nodeId);
			if (!isContain)
				continue;
			String xml = getPropXml(map.get(nodeId));
			String xPath = "//ext:*[@bpmnElement='" + nodeId + "']/ext:propers";
			String xParentPath = "//ext:*[@bpmnElement='" + nodeId + "']";
			Node el = root.selectSingleNode(xPath);
			Element elParent = (Element) root.selectSingleNode(xParentPath);
			if (el == null) {
				String propxml = "<ext:propers xmlns:ext=\"" + BpmConstants.BPM_XMLNS + "\" />";
				Document doc = Dom4jUtil.loadXml(propxml);
				elParent.add(doc.getRootElement());
			}
			String xPathItem = "//ext:*[@bpmnElement='" + nodeId + "']/ext:propers/ext:item[@parentDefKey='" + bpmDefSetting.getParentDefKey() + "']";
			DefXmlHandlerUtil.handXmlDom(root, xml, xPath, xPathItem);
		}
	}
	@SuppressWarnings("static-access")
	private String getPropXml(NodeProperties properties) {
		String xml = "<ext:item xmlns:ext=\"%s\"  allowExecutorEmpty=\"%s\" backMode=\"%s\"" + "   backNode=\"%s\"  backUserMode=\"%s\"  jumpType=\"%s\" notifyType=\"%s\" parentDefKey=\"%s\"" + " postHandler=\"%s\" prevHandler=\"%s\" skipExecutorEmpty=\"%s\" dateType=\"%s\" dueTime=\"%s\" popWin=\"%s\" choiceExcutor=\"%s\" allowEditUrgentState=\"%s\" help=\"%s\" helpGlobal=\"%s\"  allowSmsApproval=\"%s\" userNodeType=\"%s\" approvalArea=\"%s\" referOpinion=\"%s\" template=\"%s\" phone=\"%s\" email=\"%s\" sendType=\"%s\" initFillData=\"%s\"/>";
		xml = xml.format(xml, BpmConstants.BPM_XMLNS, properties.isAllowExecutorEmpty(), properties.getBackMode(),
				properties.getBackNode(), properties.getBackUserMode(), properties.getJumpType(), properties.getNotifyType(),
				properties.getParentDefKey(), properties.getPostHandler(), properties.getPrevHandler(), properties.isSkipExecutorEmpty(),
				properties.getDateType(),properties.getDueTime(),properties.isPopWin(),properties.getChoiceExcutor(),properties.isAllowEditUrgentState(),
				properties.getHelp(),properties.getHelpGlobal(),properties.isAllowSmsApproval(),properties.getUserNodeType(),properties.getApprovalArea(),
				properties.isReferOpinion(),properties.getTemplate(),properties.getPhone(),properties.getEmail(),properties.getSendType(),properties.isInitFillData());
		return xml;
	}
	@SuppressWarnings("static-access")
	public static String getTableRight(BpmSubTableRight tableRight) {
		String xml = " <ext:rightsItem xmlns:ext=\"%s\"  tableName=\"%s\" rightType=\"%s\" parentDefKey=\"%s\">" + "<ext:script><![CDATA[%s]]></ext:script>" + "</ext:rightsItem>";
		xml = xml.format(xml, BpmConstants.BPM_XMLNS, tableRight.getTableName(), tableRight.getRightType(), tableRight.getParentDefKey(), tableRight.getScript());
		return xml;
	}
}
