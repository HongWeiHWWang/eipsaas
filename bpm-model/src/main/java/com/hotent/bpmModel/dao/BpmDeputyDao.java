package com.hotent.bpmModel.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpmModel.model.BpmDeputy;

/**
 * 
 * <pre> 
 * 描述：bpm_deputy DAO接口
 * 构建组：x7
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2019-02-18 09:46
 * </pre>
 */
public interface BpmDeputyDao extends BaseMapper<BpmDeputy> {
	/**
	 * 根据代理人获取代理记录
	 * @param agentId
	 * @return
	 */
	List<BpmDeputy> getByAgentId(@Param("agentId") String agentId);
	/**
	 * 根据被代理人获取代理记录
	 * @param userId
	 * @return
	 */
	BpmDeputy getByUserId(@Param("userId") String userId);
}
