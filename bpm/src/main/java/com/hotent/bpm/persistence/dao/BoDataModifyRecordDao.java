package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.model.BoDataModifyRecord;

/**
 * 
 * <pre> 
 * 描述：流程表单数据修改记录 DAO接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-23 11:45:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BoDataModifyRecordDao extends BaseMapper<BoDataModifyRecord> {
	
	List<BoDataModifyRecord> getListByRefId(@Param("refId")String refId);
}
