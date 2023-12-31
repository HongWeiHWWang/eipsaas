package com.hotent.form.persistence.manager.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.CommonManager;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.form.model.CustomChart;
import com.hotent.form.persistence.dao.CustomChartDao;
import com.hotent.form.persistence.manager.CustomChartManager;

/**
 * 自定义对图表
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("customChartManager")
public class CustomChartManagerImpl extends BaseManagerImpl<CustomChartDao, CustomChart> implements CustomChartManager{
	@Resource
	CommonManager commonManager;

	@Override
	public CustomChart get(Serializable id) {
		CustomChart customChart = super.get(id);
		if (BeanUtils.isEmpty(customChart)) {
			QueryFilter<CustomChart> queryFilter = QueryFilter.<CustomChart>build();
			queryFilter.addFilter("ALIAS_", id, QueryOP.EQUAL);
			PageList<CustomChart> query = this.query(queryFilter);
			if (query.getRows().size()>0) {
				customChart = query.getRows().get(0);
			}
		}
		return customChart;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getListData(CustomChart customDialog, QueryFilter<?> filter, String dbType) throws IOException {
		String sql ="";
		if(customDialog.getIsTable()==2){
			sql = customDialog.getDiySql();
		}else{
			sql = "select * from "+customDialog.getObjName();
		}
		PageList query = commonManager.query(sql, filter);
		//大小写兼容处理
		for(int i=0;i<query.getRows().size();i++){
			Map<String, Object> m =(Map<String, Object>) query.getRows().get(i);
			Map<String, Object> tm = new HashMap<String, Object>();
			for(String k : m.keySet()){
				tm.put(k.toUpperCase(), m.get(k));
			}
			query.getRows().set(i, tm);
		}
		return query;
	}

	@Override
	public CustomChart getChartByAlias(String alias) {
		return baseMapper.getChartByAlias(alias);
	}

	@Override
	public boolean listChartByAlias(String alias) {
		List<CustomChart> customCharts = baseMapper.listChartByAlias(alias);
		if (customCharts.size()>1) {
			return false;
		}
		return true;
	}
}
