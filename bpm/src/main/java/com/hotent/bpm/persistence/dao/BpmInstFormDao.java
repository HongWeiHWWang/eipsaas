package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmInstForm;

/**
 * 
 * <pre> 
 * 描述：bpm_inst_form DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2017-07-04 15:19:05
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmInstFormDao extends BaseMapper<BpmInstForm> {

	List<BpmInstForm> getNodeForm(@Param("instId") String instId,@Param("defId") String defId,@Param("nodeId") String nodeId,@Param("formType") String type);

	List<BpmInstForm> getGlobalForm(@Param("instId") String instId,@Param("formType") String formType);

	List<BpmInstForm> getInstForm(@Param("instId") String instId,@Param("formType") String formType);
	
	BpmInstForm getSubInstanFrom(@Param("instId") String instId,@Param("formType") String formType);

	void removeDataByDefId(@Param("defId") String defId);
	
	void removeDataByInstId(@Param("instId") String instId);

}
