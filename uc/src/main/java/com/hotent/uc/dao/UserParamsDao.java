package com.hotent.uc.dao;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.UserParams;

/**
 * 
 * <pre> 
 * 描述：用户参数 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2016-11-01 17:11:33
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserParamsDao extends BaseMapper<UserParams>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	List<UserParams> getByUserId(@Param("id") String id);

	UserParams getByUserIdAndCode(@Param("userId") String userId,@Param("code") String code);

	void removeByUserId(@Param("userId") String userId,@Param("updateTime")LocalDateTime updateTime);

	void removeByAlias(@Param("alias") String alias,@Param("updateTime")LocalDateTime updateTime);
}
