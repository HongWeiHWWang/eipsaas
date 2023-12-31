package com.hotent.bpm.api.def;

/**
 * 流程节点xml定义处理器。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-16-上午9:10:52
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmDefXmlHandler<T> {
	
	/**
	 * 根据节点定义节点ID和需要更新的XML，更新流程节点定义。
	 * <pre>
	 *比如更新节点人员插件，更新方式如下：
	 *  1.找到节点的人员插件xml。
	 *  2.未找到则添加。
	 *  3.找到了则替换。
	 *  4.输入的XML为空则去掉这个人员配置插件。
	 * </pre>
	 * @param defId
	 * @param nodeId
	 * @param xml 
	 * void
	 * @throws Exception 
	 */
	void saveNodeXml(String defId, String nodeId,T xml) throws Exception;
}
