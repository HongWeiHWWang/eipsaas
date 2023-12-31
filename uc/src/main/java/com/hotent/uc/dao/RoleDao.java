package com.hotent.uc.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.Role;
import org.springframework.security.core.parameters.P;

/**
 * 
 * <pre> 
 * 描述：角色管理 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:28:04
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface RoleDao extends BaseMapper<Role>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 通过编码删除角色
	 * @param code
	 * @return
	 */
    Role getByCode(@Param("code") String code);
    
    /**
     * 根据用户ID获取角色列表
     * @param userId
     * @return
     */
    List<Role> getListByUserId(@Param("userId") String userId);
   
    /**
     * 根据用户账号获取角色列表
     * @param account
     * @return
     */
    List<Role> getListByAccount(@Param("account") String account);
    
    /**
     * 获取组织角色
     * @param map
     * @return
     */
    List<Role> getOrgRoleList(Map<String,Object> map);

    /**
     * 根据角色别名获取除这个角色之外的所有角色
     * @param code
     * @return
     */
    List<Role> getOrgRoleListNotCode(@Param("code") String code);

    /**
     * 根据code查询记录数量
     * @param code
     * @return
     */
    Integer getCountByCode(@Param("code") String code);
}
