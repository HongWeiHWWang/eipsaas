package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.bpm.persistence.model.CopyTo;


/**
 * 抄送的数据库访问。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-11-27-下午1:43:39
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface CopyToDao extends BaseMapper<CopyTo> {
	
	/**
	 * 根据流程实例删除抄送。
	 * @param instList 
	 * void
	 */
	void delByInstList(@Param("list") List<String> instList);
	
	
	/**
	 * 取得某人接收到的抄送转发。
	 * @param userId
	 * @param filter
	 * @return 
	 * List&lt;CopyTo>
	 */
	List<CopyTo> getReceiverCopyTo(IPage<CopyTo> page,@Param("map") Map<String, Object> params);
	
	/**
	 * 获取由我发起的操送。
	 * @param userId
	 * @param filter
	 * @return List&lt;CopyTo>
	 */
	List<CopyTo> getMyCopyTo(IPage<CopyTo> page,@Param("map") Map<String, Object> params);
	
}
