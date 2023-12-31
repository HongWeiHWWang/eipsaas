package com.hotent.bpm.api.model.identity;

import java.util.List;

/**
 * 流程节点条件配置对象和BpmIdentity为一对多的关系。
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2013-12-18-上午11:09:19
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmUserCondition {
	/**
	 * 获取流程定义ID
	 * @return  String
	 */
	public String getProcDefId();
	/**
	 * 流程节点ID
	 * @return String
	 */
	public String getNodeId();
	/**
	 * 显示条件名称
	 * @return String
	 */
	public String getConditionName();
	/**
	 * 配置的条件
	 * @return String
	 */
	public String getCondition();
	/**
	 * 排序号
	 * @return  Long
	 */
	public Long getSN();
	/**
	 * 获取条件的分组
	 * @return Integer
	 */
	public Integer getGroupNo();
	/**
	 * 条件类型
	 * 1，流程人员 2，操送人员, 3。。
	 * @return  Integer
	 */
	public Integer getType();
	/**
	 * 根据流程条件获取对应的配置人员。
	 * @return List<BpmIdentity>
	 */
	public List<BpmIdentity> getBpmIdentityList();
	
	

}
