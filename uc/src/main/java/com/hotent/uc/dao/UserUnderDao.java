package com.hotent.uc.dao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.uc.model.UserUnder;
import com.hotent.base.constants.SQLConst;
import com.baomidou.mybatisplus.core.toolkit.Constants;
/**
 * 
 * <pre> 
 * 描述：下属管理 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-25 09:24:29
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserUnderDao extends BaseMapper<UserUnder>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 获取下级用户
	 * @param iPage 
	 * @param wrapper
	 * @return
	 */
	IPage<UserUnder> getUserUnder(IPage<UserUnder> iPage,@Param(Constants.WRAPPER) Wrapper<UserUnder> wrapper);
	
	List<UserUnder> getUserUnderNOPage(@Param(SQLConst.QUERY_FILTER) Map<String, Object> params);
	
	/**
	 * 根据ids删除下属
	 * @param ids
	 */
	void delByOrgId(@Param("orgId") String orgId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 根据上级id与下级id删除上下级关系
	 * @param map
	 */
	void delByUpIdAndUderId(@Param("orgId") String orgId,@Param("underUserId") String underUserId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 删除用户在某组织下的下属
	 * @param map
	 */
	void delByUserIdAndOrgId(Map<String,Object> map);
}
