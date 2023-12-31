package com.hotent.bpmModel.manager;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpmModel.model.BpmApprovalItem;

/**
 * 常用语管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月15日
 */
public interface BpmApprovalItemManager extends BaseManager<BpmApprovalItem>{
	/**
	 * 添加常用语
	 * @param bpmApprovalItem
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	void addTaskApproval(BpmApprovalItem bpmApprovalItem) throws ClientProtocolException, IOException;
	/**
	 * 获取常用语
	 * @param defKey
	 * @param typeIdPath
	 * @param userId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<String> getApprovalByDefKeyAndTypeId(String defKey,String typeIdPath,String userId) throws ClientProtocolException, IOException;
}
