package com.hotent.uc.manager;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.demension.DemensionVo;
import com.hotent.uc.params.user.UserVo;

/**
 * 
 * <pre> 
 * 描述：维度管理 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-19 15:30:09
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface DemensionManager extends   BaseManager<Demension>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	/**
	 * 根据Code取定义对象。
	 * @param code
	 * @return
	 */
	Demension getByCode(String code);
	
	
	
	/**
	 * 获取默认维度
	 * @return
	 */
	Demension getDefaultDemension();
	
	/**
	 * 设置默认维度
	 * @param id
	 * @throws SQLException 
	 */
	void setDefaultDemension(String id) throws SQLException;
	
	
	/**
	 * 添加维度
	 * @param demVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addDem(DemensionVo demVo) throws Exception ;
	
	/**
	 * 根据编码删除维度
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> deleteDem(String code) throws Exception ;
	
	/**
	 * 更新维度
	 * @param demVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateDem(DemensionVo demVo) throws Exception ;
	
	/**
	 * 获取维度下的用户
	 * @param code
	 * @return
	 * @throws Exception
	 */
	List<UserVo> getUsersByDem(String code) throws Exception ;
	
	/**
	 * 获取维度下的组织
	 * @param code
	 * @return
	 * @throws Exception
	 */
	List<Org> getOrgsByDem(String code) throws Exception ;
	
	/**
	 * 设置默认维度
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> setDefaultDem(String code) throws Exception ;
	
	/**
	 * 取消默认维度
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> cancelDefaultDem(String code) throws Exception ;
	
	/**
	 *  根据时间获取维度数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<Demension> getDemByTime(OrgExportObject exportObject) throws Exception ;
	
	/**
	 * 查询维度编码是否已存在
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> isCodeExist(String code) throws Exception ;
	
	/**
	 * 查询用于选择器的列表
	 * @param code
	 * @return
	 * @throws Exception
	 */
	ObjectNode getOrgSelectListInit(String code) throws Exception ;

	CommonResult<String> deleteDemByIds(String ids) throws Exception;
}
