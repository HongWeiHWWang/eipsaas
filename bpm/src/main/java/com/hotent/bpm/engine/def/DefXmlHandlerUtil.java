 package com.hotent.bpm.engine.def;

import org.dom4j.Document;
import org.dom4j.Element;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.StringUtil;

/**
 * 流程xml帮助类
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-8-20-上午10:31:55
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefXmlHandlerUtil {

	
	
	

	/**
	 * 在XML中添加一段xml。
	 * 
	 * <pre>
	 * 在XML中根据xParentPath找到父节点。
	 * 在XML中根据xPath找到节点。
	 * 如果xml为空，那么删除xml中节点。
	 * 否则添加在父节点删除该节点，在添加xml。
	 * </pre>
	 * 
	 * @param defXml
	 * @param xml
	 * @param xParentPath
	 * @param xPath
	 * @return String
	 */
	public static String getXml(String defXml, String xml, String xParentPath,
			String xPath) {
		Document doc = Dom4jUtil.loadXml(defXml);
		Element root = doc.getRootElement();
		return getXml(root, xml, xParentPath, xPath);
	}

	/**
	 * 处理xmldom操作。
	 * 
	 * <pre>
	 * 在XML中根据xParentPath找到父节点。
	 * 在XML中根据xPath找到节点。
	 * 如果xml为空，那么删除xml中节点。
	 * 否则添加在父节点删除该节点，在添加xml。
	 * </pre>
	 * 
	 * @param root
	 *            根节点元素。
	 * @param xml
	 *            xml片段数据
	 * @param xParentPath
	 *            父路径
	 * @param xPath
	 *            xpath void
	 */
	public static void handXmlDom(Element root, String xml, String xParentPath,
			String xPath) {
		Element parentEl = (Element) root.selectSingleNode(xParentPath);
		Element formEl = (Element) root.selectSingleNode(xPath);

		if (StringUtil.isEmpty(xml) && formEl != null) {
			parentEl.remove(formEl);
		} else if (StringUtil.isNotEmpty(xml)) {
			if(BeanUtils.isNotEmpty(parentEl)){
				Document formDoc = Dom4jUtil.loadXml(xml);
				Element frmNode = formDoc.getRootElement();
				if (formEl != null) {
					parentEl.remove(formEl);
				}
				parentEl.add(frmNode);
			}
		}
	}
	
	

	public static String getXml(Element root, String xml, String xParentPath,
			String xPath) {
		handXmlDom(root, xml, xParentPath, xPath);
		return root.asXML();
	}
}
