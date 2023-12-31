package com.hotent.bpmModel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.bpmModel.model.BpmOftenFlow;

/**
 * 
 * <pre> 
 * 描述：通用流程 DAO接口
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-03-04 15:23:03
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmOftenFlowDao extends BaseMapper<BpmOftenFlow> {
	/**
	 * 通过用户ID和流程key删除常用流程
	 * <pre>
	 * 不传入defkeys时，会删除该用户下的所有常用流程
	 * </pre>
	 * @param userId
	 * @param defkeys
	 */
	void removeByUserIdAndDefKeys(@Param("userId")String userId, @Param("defkeys")List<String> defkeys);
	
	IPage<BpmOftenFlow> customQuery(IPage<BpmOftenFlow> page, @Param(Constants.WRAPPER) Wrapper<BpmOftenFlow> wrapper);
	
	List<BpmOftenFlow> getBpmOftenFlows(@Param("userId")String userId, @Param("defkey")String defkey);
}
