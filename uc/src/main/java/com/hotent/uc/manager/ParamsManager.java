package com.hotent.uc.manager;

import java.util.List;

import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import org.apache.ibatis.annotations.Param;

import com.hotent.uc.model.OrgParams;
import com.hotent.uc.model.Params;
import com.hotent.uc.model.UserParams;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.params.ParamVo;

/**
 * 组织参数 处理接口
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2019年1月4日
 */
public interface ParamsManager extends   BaseManager<Params>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	/**
	 * 通过参数别名获取参数
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	Params getByAlias(String alias) throws Exception;
	/**
	 * 通过参数类型查询参数列表
	 * @param type
	 * @return
	 * @throws Exception
	 */
	List<Params> getByType(String type) throws Exception;
	/**
	 * 添加参数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addParams(ParamVo param) throws Exception;
	/**
	 * 删除参数
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> deleteParams(String codes) throws Exception;
	/**
	 * 更新参数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateParams(ParamVo param) throws Exception;
	/**
	 * 通过用户账号获取指定参数的值
	 * @param account
	 * @param code
	 * @return
	 * @throws Exception
	 */
	UserParams getUserParamsByCode(String account,String code) throws Exception;
	/**
	 * 通过用户ID获取指定参数的值
	 * @param userId
	 * @param code
	 * @return
	 * @throws Exception
	 */
	UserParams getUserParamsById(String userId, String code) throws Exception;
	/**
	 * 通过组织代码获取指定参数的值
	 * @param orgCode
	 * @param code
	 * @return
	 * @throws Exception
	 */
	OrgParams getOrgParamsByCode(String orgCode,String code) throws Exception;
	/**
	 * 通过组织ID获取指定参数的值
	 * @param orgId
	 * @param code
	 * @return
	 * @throws Exception
	 */
	OrgParams getOrgParamsById(String orgId, String code) throws Exception;
	/**
	 *  根据时间获取用户组织参数数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<Params> getParamsByTime(String btime,String etime) throws Exception ;
	/**
	 * 查询用户组织参数编码是否已存在
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> isCodeExist(String code) throws Exception ;
	/**
	 * 根据ids删除参数
	 * @param ids
	 * @return
	 */
	CommonResult<String> deleteParamsByIds(String ids);
	
	/**
	 * 根据类型获取参数
	 * @param type
	 * @return
	 */
	List<Params> getByTenantTypeId(String tenantTypeId);

	/**
	 * 查询参数列表（包含租户类型名称）
	 * @param queryFilter
	 * @return
	 */
	PageList queryWithType(QueryFilter queryFilter);
}
