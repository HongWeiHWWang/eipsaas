package com.hotent.bpmModel.manager;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.hotent.base.manager.BaseManager;
import com.hotent.bpmModel.model.BpmDeputy;

/**
 * 流程代理管理
 * <pre> 
 * 描述：bpm_deputy 处理接口
 * 构建组：x7
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2019-02-18 09:46
 * </pre>
 */
public interface BpmDeputyManager extends BaseManager<BpmDeputy> {
	/**
	 * 根据代理人获取代理记录
	 * @param agentId
	 * @return
	 */
	List<BpmDeputy> getByAgentId(@Param("agentId") String agentId);
	/**
	 * 根据被代理人获取代理记录
	 * @param agentId
	 * @return
	 */
	BpmDeputy getByUserId(@Param("userId") String userId);
}
