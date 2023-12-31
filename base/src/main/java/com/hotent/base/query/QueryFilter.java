package com.hotent.base.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.util.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 通用查询器
 * 
 * <pre>
 * 通用查询器是VO对象，转换Restful接口的通用请求为MyBatis-plus的查询命令。
 * </pre>
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月4日
 */
@ApiModel(description = "通用查询器")
public class QueryFilter<T extends Model<T>> implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name = "pageBean", notes = "分页信息")
	private PageBean pageBean;

	@ApiModelProperty(name = "sorter", notes = "排序字段")
	private List<FieldSort> sorter = new ArrayList<FieldSort>();

	@ApiModelProperty(name = "params", notes = "自定义查询")
	private Map<String, Object> params = new LinkedHashMap<String, Object>();

	@ApiModelProperty(name = "querys", notes = "查询条件组")
	private List<QueryField> querys = new ArrayList<QueryField>();

	@ApiModelProperty(name = "groupRelation", notes = "查询条件分组的关系", example = "AND")
	private FieldRelation groupRelation = FieldRelation.AND;

	private QueryFilter() {
	}

	public static <T extends Model<T>> QueryFilter<T> build() {
		return new QueryFilter<>();
	}

	public QueryFilter<T> withDefaultPage() {
		this.pageBean = new PageBean();
		return this;
	}

	public QueryFilter<T> withPage(PageBean pageBean) {
		this.pageBean = pageBean;
		return this;
	}

	public QueryFilter<T> withSorter(FieldSort fieldSort) {
		this.sorter.add(fieldSort);
		return this;
	}

	public QueryFilter<T> withQuery(QueryField queryField) {
		this.querys.add(queryField);
		return this;
	}

	public QueryFilter<T> withParam(String key, Object value) {
		this.params.put(key, value);
		return this;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * 获取Params并清空Params
	 * 
	 * @return
	 */
	@JsonIgnore
	public Map<String, Object> getParamsAndEmpty() {
		Map<String, Object> copyParams = this.params;
		this.params = new HashMap<>();
		return copyParams;

	}

	/**
	 * 从querys里面获取查询条件
	 * 
	 * @return
	 */
	@JsonIgnore
	public Map<String, Object> getInitParams() {

		Map<String, Object> initParams = new LinkedHashMap<String, Object>();
		if (BeanUtils.isEmpty(this.querys))
			return initParams;
		for (QueryField element : this.querys) {
			QueryField queryField = (QueryField) element;
			QueryOP operation = queryField.getOperation();
			if (QueryOP.IS_NULL.equals(operation) || QueryOP.NOTNULL.equals(operation)
					|| QueryOP.IN.equals(operation)) {
				continue;
			}
			String property = queryField.getProperty();
			if (property.indexOf(".") > -1) {
				property = property.substring(property.indexOf(".") + 1);
			}
			Object value = queryField.getValue();
			initParams.put(property, value);
		}
		initParams.putAll(params);
		return initParams;
	}

	public void addFilter(String property, Object value, QueryOP operation, FieldRelation relation) {
		QueryField queryField = new QueryField(property, value, operation, relation);
		querys.add(queryField);
	}

	public void addFilter(String property, Object value, QueryOP operation, FieldRelation relation, String group) {
		QueryField queryField = new QueryField(property, value, operation, relation, group);
		querys.add(queryField);
	}

	public void addFilter(String property, Object value, QueryOP operation) {
		QueryField queryField = new QueryField(property, value, operation, FieldRelation.AND);
		querys.add(queryField);
	}

	public void addParams(String property, Object value) {
		params.put(property, value);
	}

	public List<FieldSort> getSorter() {
		return sorter;
	}

	public void setSorter(List<FieldSort> sorter) {
		this.sorter = sorter;
	}

	/**
	 * 设置一个默认的Sort,如果已有sort则不设置
	 * 
	 * @param sorter
	 */
	public void setDefaultSort(String property, Direction direction) {
		if (BeanUtils.isEmpty(sorter)) {
			this.sorter.add(new FieldSort(property, direction));
		}
	}

	public List<QueryField> getQuerys() {
		return querys;
	}

	public void setQuerys(List<QueryField> querys) {
		this.querys = querys;
	}

	public FieldRelation getGroupRelation() {
		return groupRelation;
	}

	public void setGroupRelation(FieldRelation groupRelation) {
		this.groupRelation = groupRelation;
	}

	/**
	 * 对查询条件进行分组
	 * 
	 * @return
	 */
	public Map<String, List<QueryField>> groupQueryField() {
		Map<String, List<QueryField>> map = new HashMap<>();
		this.querys.forEach(q -> {
			String group = q.getGroup();
			List<QueryField> list = map.get(group);
			if (list == null) {
				list = new ArrayList<>();
				map.put(group, list);
			}
			list.add(q);
		});
		return map;
	}

	/**
	 * 按照排序顺序将排序字段分组
	 * 
	 * @return
	 */
	public Map<Direction, List<FieldSort>> groupFieldSort() {
		Map<Direction, List<FieldSort>> map = new HashMap<>();
		this.sorter.forEach(q -> {
			Direction direct = q.getDirection();
			List<FieldSort> list = map.get(direct);
			if (list == null) {
				list = new ArrayList<>();
				map.put(direct, list);
			}
			list.add(q);
		});
		return map;
	}

}
