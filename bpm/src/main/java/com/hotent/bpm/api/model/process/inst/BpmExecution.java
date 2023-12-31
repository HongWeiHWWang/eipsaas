package com.hotent.bpm.api.model.process.inst;

/**
 * Bpm节点执行实体接口
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-7-下午2:58:34
 * 版权：广州宏天软件有限公司版权所有
 */
public interface BpmExecution {
	/**
	 * 执行ID
	 * @return
	 */
	public String getExecId();	
	/**
	 * 获取节点编码
	 * @return
	 */
	public String getNodeKey();	
	/**
	 * 返回流程实例ID
	 * @return
	 */
	public String getProcInstId();
	/**
	 * 流程定义ID
	 * @return
	 */
	public String getProcDefId();
	/**
	 * 父执行ID
	 * @return
	 */
	public String getParentExecId();	
	/**
	 * 关联数据业务主键
	 * @return
	 */
	public String getBizKey();
	/**
	 * 是否同步
	 * @return
	 */
	public boolean isConcurrent();
}
