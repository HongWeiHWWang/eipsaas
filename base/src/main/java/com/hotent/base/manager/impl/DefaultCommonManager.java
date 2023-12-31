package com.hotent.base.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.dao.CommonDao;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.SystemException;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.manager.QueryFilterHelper;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;

/**
 * 通用管理器的默认实现
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
@Service
public class DefaultCommonManager implements CommonManager, QueryFilterHelper<Map<String, Object>>{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	CommonDao commonDao;
	
	@Override
	public int execute(String sql) {
		Assert.notNull(sql, "sql can not be empty.");
		return this.execute(sql, new HashMap<>());
	}
	
	/**
	 * 将参数化sql和值转换为map型
	 * <p>
	 * 该方法返回修改后的sql，传入的map会被清空后将objs按顺序放入map，sql转化后示例：
	 * select * from ... where a=? and b=? 转换为 select * from ... where a=#{key0} and b=#{key1}
	 * </p>
	 * @param map
	 * @param sql
	 * @param objs
	 */
	private String convertToMap(Map<String,Object> map, String sql, Object...objs) {
		Assert.notNull(map, "map不能为空");
		Assert.isTrue(StringUtil.isNotEmpty(sql), "sql不能为空");
		Assert.isTrue(BeanUtils.isNotEmpty(objs), "objs不能为空");
		Pattern pattern = Pattern.compile("(\\?)");
        Matcher matcher = pattern.matcher(sql);
        StringBuffer sb = new StringBuffer();
        int count = 0;
        while (matcher.find()) {
            matcher.appendReplacement(sb, String.format("#{key%s}", count));
            count++;
        }
        matcher.appendTail(sb);
        
        if(count!=objs.length) {
        	throw new BaseException(String.format("sql：%s中参数化的数量和传递进来的参数：%s数量不匹配。", sql, objs));
        }
        map.clear();
        for (int i = 0; i < objs.length; i++) {
        	map.put("key" + i, objs[i]);
		}
        return sb.toString();
	}

	@Override
	public int execute(String sql, Object... objs) {
		Map<String,Object> map = new HashMap<>();
		String newSql = convertToMap(map, sql, objs);
		return this.execute(newSql, map);
	}

	@Override
	public int execute(String sql, Map<String, Object> map) {
		map.put("sql", sql);
		return commonDao.update(map);
	}
	
	@Override
	public List<Map<String,Object>> query(String sql) {
		return this.query(sql, new HashMap<>());
	}

	@Override
	public List<Map<String, Object>> query(String sql, Object... objs) {
		Map<String,Object> map = new HashMap<>();
		String newSql = convertToMap(map, sql, objs);
		return this.query(newSql, map);
	}
	
	@Override
	public List<Map<String, Object>> query(String sql, Map<String, Object> map) {
		Assert.notNull(sql, "sql can not be empty.");
		Assert.notNull(map, "map can not be empty.");
		if(map.containsKey("sql")) {
			logger.warn("can not define 'sql' in map when invoke CommonManager.query(sql, map), it will be override by sql string.");
		}
		map.put("sql", sql);
		return commonDao.query(map);
	}

	@Override
	public PageList<Map<String,Object>> query(String sql, PageBean pageBean) {
		Assert.notNull(sql, "sql can not be empty.");
		IPage<Map<String,Object>> result = commonDao.queryByPage(convert2IPage(pageBean),sql);
		return new PageList<Map<String,Object>>(result);
	}

	@Override
	public PageList<Map<String,Object>> query(String sql, QueryFilter<?> queryFilter) throws SystemException {
		Assert.notNull(sql, "sql can not be empty.");
		Assert.notNull(queryFilter, "queryFilter can not be empty.");
		PageBean pageBean = queryFilter.getPageBean();
		IPage<Map<String,Object>> result = commonDao.queryByQueryFilter(convert2IPage(pageBean), sql, convert2Wrapper(queryFilter, null));
		return new PageList<Map<String,Object>>(result);
	}

	@Override
	public PageList<Map<String, Object>> queryByCustomSql(String sql, QueryFilter<?> queryFilter) throws SystemException {
		Assert.notNull(sql, "sql can not be empty.");
		Assert.notNull(queryFilter, "queryFilter can not be empty.");
		PageBean pageBean = queryFilter.getPageBean();
		IPage<Map<String,Object>> result = commonDao.queryByCustomSql(convert2IPage(pageBean), sql, convert2Wrapper(queryFilter, null));
		return new PageList<Map<String,Object>>(result);
	}

}
