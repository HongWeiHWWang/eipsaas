package com.hotent.base.manager;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.base.exception.BaseException;
import com.hotent.base.query.Direction;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.FieldSort;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FieldConvertUtil;

/**
 * QueryFilter转为Wrapper的帮助接口
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月9日
 */
public interface QueryFilterHelper<T> {
	
	public static final String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";
	
	/**
	 * 条件SQL的KEY
	 */
	public static final String WHERE_SQL_TAG = "whereSql";
	/**
	 * 排序SQL的KEY
	 */
	public static final String ORDER_SQL_TAG = "orderBySql";

	// PageBean转换为IPage
	default IPage<T> convert2IPage(PageBean pageBean) {
		return pageBean == null ? new Page<>()
				: new Page<T>(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
	}

	default Wrapper<T> convert2Wrapper(QueryFilter<?> queryFilter, Class<T> currentModelClass) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		// 1.处理QueryField（querys）列表
		Map<String, List<QueryField>> groupQueryField = queryFilter.groupQueryField();
		FieldRelation groupRelation = queryFilter.getGroupRelation();
		groupQueryField.forEach((group, list) -> {
			if (FieldRelation.AND.equals(groupRelation)) {
				queryWrapper.and(x -> {
					list.forEach(l -> convertQueryField(x, l, currentModelClass));
				});
			} else if (FieldRelation.OR.equals(groupRelation)) {
				queryWrapper.or(x -> {
					list.forEach(l -> convertQueryField(x, l, currentModelClass));
				});
			} else {
				throw new BaseException("");
			}
		});

		// 2.处理排序字段
		Map<Direction, List<FieldSort>> groupFieldSort = queryFilter.groupFieldSort();
		groupFieldSort.forEach((d, l) -> {
			if (Direction.DESC.equals(d)) {
				queryWrapper.orderByDesc(convertSortFieldList(l, currentModelClass));
			} else {
				queryWrapper.orderByAsc(convertSortFieldList(l, currentModelClass));
			}
		});

		// 3.处理自定义参数（使用时在sql中通过ew.paramNameValuePairs.{mapKey}的方式使用）
		Map<String, Object> params = queryFilter.getParams();
		if(params!=null && params.size() > 0) {
			Map<String, Object> paramNameValuePairs = queryWrapper.getParamNameValuePairs();
			paramNameValuePairs.putAll(params);
		}
		return queryWrapper;
	}

	// 将SortField列表转换为数据库字段数组
	default String[] convertSortFieldList(List<FieldSort> list, Class<T> currentModelClass) {
		if (list == null) {
			return null;
		}
		String[] ary = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (currentModelClass != null) {
				ary[i] = FieldConvertUtil.property2Field(list.get(i).getProperty(), currentModelClass);
			} else {
				ary[i] = list.get(i).getProperty();
			}
		}
		return ary;
	}

	// 解析通用查询条件，通过QueryWrapper构建对应的查询命令
	default void convertQueryField(QueryWrapper<T> queryWrapper, QueryField queryField, Class<T> currentModelClass) {
		FieldRelation r = queryField.getRelation();
		if (FieldRelation.OR.equals(r)) {
			queryWrapper.or();
		}
		String field = queryField.getProperty();
		if (currentModelClass != null) {
			field = FieldConvertUtil.property2Field(field, currentModelClass);
		}
		QueryOP operation = queryField.getOperation();
		switch (operation) {
		case EQUAL:
		case EQUAL_IGNORE_CASE:
			queryWrapper.eq(field, queryField.getValue());
			break;
		case NOT_EQUAL:
			queryWrapper.ne(field, queryField.getValue());
			break;
		case LESS:
			queryWrapper.lt(field, queryField.getValue());
			break;
		case GREAT:
			queryWrapper.gt(field, queryField.getValue());
			break;
		case LESS_EQUAL:
			queryWrapper.le(field, queryField.getValue());
			break;
		case GREAT_EQUAL:
			queryWrapper.ge(field, queryField.getValue());
			break;
		case LIKE:
			queryWrapper.like(field, queryField.getValue());
			break;
		case LEFT_LIKE:
			queryWrapper.likeLeft(field, queryField.getValue());
			break;
		case RIGHT_LIKE:
			queryWrapper.likeRight(field, queryField.getValue());
			break;
		case IS_NULL:
			queryWrapper.isNull(field);
			break;
		case NOTNULL:
			queryWrapper.isNotNull(field);
			break;
		case IN:
			queryWrapper.in(field, convert2ObjectArray(queryField.getValue()));
			break;
		case BETWEEN:
			Object[] objs = convert2ObjectArray(queryField.getValue());
			Assert.notNull(objs, String.format("查询条件：%s的查询值为空", field));
			Assert.isTrue(objs.length == 2, String.format("查询条件为between时，查询值必须为两个，但是传入的查询值为：%s", objs));
			queryWrapper.between(field, objs[0], objs[1]);
			break;
		}
	}

	/**
	 * 将传入的Object类型解析为对象数组型
	 * 
	 * <pre>
	 * 1.以List格式提供的数据；
	 * 2.以逗号分割的字符串提供的数据；
	 * 3.以Object数组提供的数据。
	 * </pre>
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default Object[] convert2ObjectArray(Object obj) {
		if (BeanUtils.isEmpty(obj)) {
			return new Object[] {"''"};
		}
		if (obj instanceof String) {
			// 字符串形式，通过逗号分隔
			String str = obj.toString();
			String[] split = str.split(",");
			for(int i=0;i<split.length;i++) {
				split[i] = handleQuotation(split[i]);
			}
			return split;

		} else if (obj instanceof List) {
			// 列表形式
			List<Object> objList = (List<Object>) obj;
			return objList.toArray();
		} else if (obj instanceof Object[]) {
			// 数组形式
			return (Object[]) obj;
		} else if (obj instanceof Object) {
			// 单个对象
			return new Object[] { obj };
		}
		return null;
	}
	
	/**
	 * 处理用单引号包起来的字符串
	 * @param str
	 * @return
	 */
	default String handleQuotation(String str) {
		String ResultString = str;
		try {
			Pattern regex = Pattern.compile("^'(.*)'$");
			Matcher regexMatcher = regex.matcher(str);
			if (regexMatcher.find()) {
				ResultString = regexMatcher.group(1);
			} 
		} catch (PatternSyntaxException ex) {}
		return ResultString;
	}

	/**
	 * 將QueryFilter中的querys放入params
	 * <pre>
	 * 1.只把QueryField的property為key，value為value放入params；
	 * 2.在params中已經有該key存在時，不會使用QueryField來覆蓋。
	 * </pre>
	 * @param queryFilter
	 */
	default void copyQuerysInParams(QueryFilter queryFilter){
		List<QueryField> querys = queryFilter.getQuerys();
		Map<String, Object> params = queryFilter.getParams();
		if(BeanUtils.isNotEmpty(querys)){
			querys.forEach(i -> {
				if(!params.containsKey(i.getProperty())){
					params.put(i.getProperty(), i.getValue());
				}
			});
		}
	}

}
