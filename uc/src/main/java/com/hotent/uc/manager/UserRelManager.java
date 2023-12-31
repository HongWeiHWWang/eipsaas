package com.hotent.uc.manager;

import java.util.List;

import com.hotent.uc.model.User;
import com.hotent.uc.model.UserRel;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.user.UserRelFilterObject;
import com.hotent.uc.params.user.UserRelVo;
import com.hotent.base.query.PageList;

/**
 * 
 * <pre> 
 * 描述：用户关系 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2017-06-12 09:21:48
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserRelManager extends BaseManager<UserRel>{
	
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	
	/**
	 * 获取typeId下的用户 ， 某汇报线下的所有用户
	 * @param typeId
	 * @return
	 * @throws Exception 
	 */
	List<UserRel> getByTypeId(String typeId) throws Exception;
	
	/**
	 * 根据别名获取
	 * @param alias
	 * @return
	 */
	UserRel getByAlias(String alias);
	
	/**
	 * 判断在typeId 中是否存在 
	 * @param typeId
	 * @param value
	 * @param parentId
	 * @return
	 */
	UserRel getByUserIdAndParentId(String typeId, String value,
			String parentId);
	
	
	/**
	 * 获取直接上级
	 * @param account
	 * @param level
	 * @param typeId
	 * @return
	 * @throws Exception 
	 */
	List<User> getSuperUser(String account, String typeId) throws Exception;
	
	/**
	 * 获取所有上级
	 * @param account
	 * @param level
	 * @param typeId
	 * @return
	 * @throws Exception 
	 */
	List<User> getAllSuperUser(String account,String typeId) throws Exception;

	/**
	 * 获取用户汇报线的下级用户（直接下级）
	 * @param account
	 * @param level
	 * @param typeId
	 * @return
	 * @throws Exception 
	 */
	List<User> getLowerUser(String account,  String typeId) throws Exception;

	/**
	 * 获取用户汇报线的所有下级用户
	 * @param account
	 * @param level
	 * @param typeId
	 * @return
	 * @throws Exception 
	 */
	List<User> getAllLowerUser(String account, String typeId) throws Exception;
	
	/**
	 * 添加用户关系定义
	 * @param userRelVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addUserRel(List<UserRelVo> userRelVo) throws Exception;
	
	/**
	 * 删除用户关系定义
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> deleteUserRel(String codes) throws Exception;
	
	/**
	 * 更新用户关系定义
	 * @param userRelVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateUserRel(UserRelVo userRelVo) throws Exception;
	
    /**
     * 根据汇报线分类ID获取汇报线
     * @param typeId
     * @return
     * @throws Exception 
     */
	List<UserRel> getUserRelByTypeId(String typeId) throws Exception;
    /**
     * 根据汇报线分类code获取汇报线
     * @param code
     * @return
     */
	PageList<UserRel> getChildRelByAilas(String code);

	
	/**
	 *  根据时间获取汇报线节点数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<UserRel> getUserRelByTime(String btime,String etime) throws Exception ;
	
	/**
	 * 通过用户账号（或级别、汇报线类型）获取用户所有所在汇报线节点
	 * @param account
	 * @param level
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	List<UserRel> getUserRels(String account,String typeId) throws Exception ;
	
	/**
	 * 获取用户关系定义节点中的用户列表
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	List<User> getUsersByRel(String alias) throws Exception ;
	
	/**
	 * 根据路径删除数据
	 * @param path
	 */
	void removeByPath(String path);
	
	/**
	 * 根据父id获取子节点列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<UserRel> getByParentId(String parentId) throws Exception ;

	/**
	 * 更新汇报线节点所在树的位置
	 * @param relId
	 * @param parentId
	 * @return
	 */
	CommonResult<String> updateRelPos(String relId,String parentId) throws Exception ;

	String getRelTypeId(UserRelFilterObject userRelFilterObject) throws Exception;
}
