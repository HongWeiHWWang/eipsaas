package com.hotent.bpm.api.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.base.groovy.IScript;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.model.process.hi.BpmProcessInstanceHistory;


/**
 * 描述：流程实例历史服务接口
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-8-下午3:39:10
 * 版权：广州宏天软件有限公司版权所有
 */
public interface BpmHistoryService  extends IScript  {
	/**
	 * 通过ID查找程历史实例
	 * @param processInstId
	 * @return
	 */
	public BpmProcessInstanceHistory getProcessInstHistory(String processInstId);
	
	/**
	 * 删除流程历史实例
	 * @param processInstId
	 * @return
	 */
	public boolean removeProcessInstHistory(String processInstId);
	/**
	 * 查找所有的流程历史
	 * @param queryFilter
	 * @return
	 */
	public List<BpmProcessInstanceHistory> getAll(QueryFilter queryFilter);
	
	/**
	 * 取得某个用户发起的归档的流程实例
	 * @param userId
	 * @return
	 */
	public List<BpmProcessInstanceHistory> getProcessInstancesByUserId(String userId);
	/**
	 * 取得某个用户发起的归档的流程实例
	 * @param userId
	 * @param page
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<BpmProcessInstanceHistory> getProcessInstancesByUserId(String userId,Page page);
	/**
	 * 取得某个用户发起的归档的流程实例
	 * @param userId
	 * @param queryFilter
	 * @return 
	 * @return List<BpmProcessInstanceHistory>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<BpmProcessInstanceHistory> getProcessInstancesByUserId(String userId,QueryFilter queryFilter);
	/**
	 * 取得某用户参与的归档流程实例列表
	 * @param userId
	 * @return
	 */
	public List<BpmProcessInstanceHistory> getAttendProcessInstancesByUserId(String userId);
	/**
	 * 取得某用户参与的归档流程实例列表
	 * @param userId
	 * @param page
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<BpmProcessInstanceHistory> getAttendProcessInstancesByUserId(String userId,Page page);
	/**
	 *取得某用户参与的归档流程实例列表
	 * @param userId
	 * @param queryFilter
	 * @return
	 */
	public List<BpmProcessInstanceHistory> getAttendProcessInstancesByUserId(String userId,QueryFilter queryFilter);

}
