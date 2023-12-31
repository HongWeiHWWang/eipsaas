package com.hotent.base.util;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;

/**
 * 支持分页查询的jdbcTemplate
 * @author liyanggui
 * @date 2020-04-21
 */
public class JdbcTemplateUtil {
	/**
	 * @param originalSql
	 * @param page
	 * @param size
	 */
	public static <T> PageList<T> queryForListWithPage(String originalSql, PageBean pageBean, Class<T> elementType) {
		return queryForListWithPage(originalSql,null,pageBean,elementType);
	}
	
	/**
	 * <pre>
	 * 分页查询sql支持参数
	 * </pre>
	 * @param <T>
	 * @param originalSql
	 * @param args
	 * @param pageBean
	 * @param elementType
	 * @return
	 */
	public static <T> PageList<T> queryForListWithPage(String originalSql, Object[] args, PageBean pageBean, Class<T> elementType) {
		JdbcTemplate jdbcTemplate = AppUtil.getBean(JdbcTemplate.class);
		PageList<T> pageList = new PageList<T>();
		String dialectSql = initPageList(jdbcTemplate,originalSql,pageBean,pageList);
		Object[] pageArgs = getPageArgs(pageBean);
		Object[] newArgs =pageArgs;
		if(BeanUtils.isNotEmpty(args)) {
			newArgs = ArrayUtil.concat(args, pageArgs);
		}
		List<T> queryForList = jdbcTemplate.queryForList(dialectSql,newArgs,elementType);
		pageList.setRows(queryForList);
		return pageList;
	}
	
	/**
	 * @param originalSql
	 * @param pageBean
	 */
	public static PageList<Map<String,Object>> queryForListWithPage(String originalSql,PageBean pageBean) {
		return queryForListWithPage(originalSql,null,pageBean);
		
	}
	
	/**
	 * 分页查询
	 * @param originalSql
	 * @param args
	 * @param pageBean
	 * @return
	 */
	public static PageList<Map<String,Object>> queryForListWithPage(String originalSql, Object[] args, PageBean pageBean) {
		JdbcTemplate jdbcTemplate = AppUtil.getBean(JdbcTemplate.class);
		PageList<Map<String,Object>> pageList = new PageList<Map<String,Object>>();
		String dialectSql = initPageList(jdbcTemplate,originalSql,pageBean,pageList);
		Object[] pageArgs = getPageArgs(pageBean);
		Object[] newArgs =pageArgs;
		if(BeanUtils.isNotEmpty(args)) {
			newArgs = ArrayUtil.concat(args, pageArgs);
		}
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(dialectSql,newArgs);
		pageList.setRows(queryForList);
		return pageList;
		
	}
	
	/**
	 * @param originalSql
	 * @param pageBean
	 */
	public static <T> PageList<T> query(String originalSql,PageBean pageBean,RowMapper<T> rowMapper) {
		return query(originalSql,null,pageBean,rowMapper);
	}
	
	/**
	 * <pre>
	 * 分页查询
	 * </pre>
	 * @param <T>
	 * @param originalSql
	 * @param args
	 * @param pageBean
	 * @param rowMapper
	 * @return
	 */
	public static <T> PageList<T> query(String originalSql, Object[] args, PageBean pageBean,RowMapper<T> rowMapper) {
		JdbcTemplate jdbcTemplate = AppUtil.getBean(JdbcTemplate.class);
		PageList<T> pageList = new PageList<T>();
		Object[] pageArgs = getPageArgs(pageBean);
		Object[] newArgs =pageArgs;
		if(BeanUtils.isNotEmpty(args)) {
			newArgs = ArrayUtil.concat(args, pageArgs);
		}
		String dialectSql = initPageList(jdbcTemplate,originalSql,args,pageBean,pageList);
		List<T> query = jdbcTemplate.query(dialectSql, newArgs, rowMapper);
		pageList.setRows(query);
		return pageList;
		
	}

    

	/**
     * 获取总条数
     * @param <T>
     * @param jdbcTemplate
     * @param originalSql
     * @param pageBean
     * @return
     */
	private static <T> String initPageList(JdbcTemplate jdbcTemplate, String originalSql, PageBean pageBean,PageList<T> pageList ) {
		return initPageList(jdbcTemplate,originalSql,null,pageBean,pageList);
	}
	
	private static <T> String initPageList(JdbcTemplate jdbcTemplate, String originalSql, Object[] args, PageBean pageBean,
			PageList<T> pageList) {
		SqlInfo sqlInfo = SqlParserUtils.getOptimizeCountSql(true, null, originalSql);
		String countSql = sqlInfo.getSql();
		IDialect dialect = DialectFactory.getDialect(SQLUtil.getDbTypeObj());
		DialectModel model = dialect.buildPaginationSql(originalSql, pageBean.getPage(), pageBean.getPageSize());
		String dialectSql = model.getDialectSql();
		Long total = 0L;
		if(BeanUtils.isNotEmpty(args)) {
			total = jdbcTemplate.queryForObject(countSql,args, Long.class);
		}else {
			total = jdbcTemplate.queryForObject(countSql, Long.class);
		}
		
		pageList.setPage(pageBean.getPage());
		pageList.setPageSize(pageBean.getPageSize());
		pageList.setTotal(total);
		return dialectSql;
	}
	
	private static Object[] getPageArgs(PageBean pageBean) {
		Object[] args = new Object[2];
		args[0] = pageBean.getLimit();
		args[1] = pageBean.getOffset();
		return args;
	}
	
	
}
