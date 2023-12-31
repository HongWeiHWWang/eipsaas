package com.hotent.form.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.form.model.QueryView;

/**
 * 
 * <pre> 
 * 描述：sys_query_view DAO接口
 * 日期:2016-06-13 17:26:55
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface QueryViewDao extends BaseMapper<QueryView> {

	List<QueryView> getBySqlAlias(String sqlAlias);

	void removeBySqlAlias(String sqlAlias);

	QueryView getBySqlAliasAndAlias(@Param("sqlAlias")String sqlAlias,@Param("alias")String alias);

	QueryView getByAlias(String alias);

	List<QueryView> listByAlias(String alias);
}
