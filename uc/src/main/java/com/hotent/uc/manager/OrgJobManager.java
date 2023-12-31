package com.hotent.uc.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.params.job.JobVo;
import com.hotent.uc.params.user.UserVo;


/**
 * 
 * <pre> 
 * 描述：组织关系定义 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-29 18:00:43
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgJobManager extends   BaseManager<OrgJob>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	/**
	 * 根据职务编码获取职务定义
	 * @param code
	 * @return
	 */
	public OrgJob getByCode(String code);
	
	/**
	 * 根据职务名称获取职务
	 * @param name
	 * @return
	 */
	List<OrgJob> getByName(String name);
	
	/**
	 * 通过用户ID获取其拥有的职务
	 * @param userId
	 * @return
	 */
	List<OrgJob> getListByUserId(String userId);
	
	/**
	 * 通过用户账号获取其拥有的职务
	 * @param account
	 * @return
	 */
	List<OrgJob>  getListByAccount(String account);
	
	/**
	 * 新增职务
	 * @param job
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addJob(JobVo job) throws Exception;
	
	/**
	 * 通过编码删除职务
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> deleteJob(String codes) throws Exception;
	
	/**
	 * 更新职务
	 * @param job
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateJob(JobVo job) throws Exception;
	
	/**
	 * 获取职务下的用户列表
	 * @param code
	 * @return
	 * @throws Exception
	 */
	List<UserVo> getUsersByJob(String code) throws Exception;
	
	/**
	 *  根据时间获取职务数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<OrgJob> getJobByTime(String btime,String etime) throws Exception ;
	
	/**
	 * 查询职务编码是否已存在
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> isCodeExist(String code) throws Exception ;

	/**
	 * 根据职务id删除
	 * @param ids
	 * @return
	 */
	CommonResult<String> deleteJobByIds(String ids);
	
}
