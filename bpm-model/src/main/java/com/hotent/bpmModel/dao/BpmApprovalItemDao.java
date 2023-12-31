package com.hotent.bpmModel.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpmModel.model.BpmApprovalItem;

/**
 * 
 * <pre> 
 * 描述：常用语管理 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:hugh
 * 邮箱:zxh@jee-soft.cn
 * 日期:2014-11-03 10:56:20
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmApprovalItemDao extends BaseMapper<BpmApprovalItem> {
	/**
	 * 通过流程key和用户id获取审批常用语
	 * @param defKey
	 * @param curUserId
	 * @return
	 */
	List<BpmApprovalItem> getByDefKeyAndUserAndSys(@Param("defKey") String defKey,@Param("curUserId") String curUserId);
	/**
	 * 通过类型获取审批常用语
	 * @param type
	 * @return
	 */
	List<BpmApprovalItem> getItemByType(@Param("type") Short type);
}
