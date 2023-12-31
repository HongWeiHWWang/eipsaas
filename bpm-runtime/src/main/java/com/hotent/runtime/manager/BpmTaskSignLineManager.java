package com.hotent.runtime.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.runtime.model.BpmTaskSignLine;

/**
 * 
 * <pre> 
 * 描述：并行签署 处理接口
 * 构建组：x7
 * 作者:jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-10-14 10:34:11
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BpmTaskSignLineManager extends BaseManager<BpmTaskSignLine>{

	BpmTaskSignLine getByTaskId(String taskId);

	List<BpmTaskSignLine> getByInstNodeIdAndStatus(String instanceId, String rootTaskId, String nodeId, String status);
	
	/**
	 * 获取path 下的所有数据
	 * status null 或者 SignLineStatus中对应的key 
	 * @param path
	 * @param status
	 * @return
	 */
	List<BpmTaskSignLine> getByPathChildAndStatus(String path, String status);

	void removeByTaskIds(String[] taskIds);
	
	void removeByTaskId(String taskId);

	void removeByInstIdNodeId(String instanceId, String rootTaskId, String nodeId);

	void updateStatusByTaskIds(String status, String[] taskIds);
	
}
