package com.hotent.form.persistence.dao;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.QueryMetafield;

/**
 * 
 * <pre> 
 * 描述：sys_query_metafield DAO接口
 * 日期:2016-06-13 16:42:01
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface QueryMetafieldDao extends BaseMapper<QueryMetafield> {

	List<QueryMetafield> getBySqlId(String sqlId);

	void removeBySqlId(String sqlId);
}
