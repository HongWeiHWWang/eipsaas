
package com.hotent.sys.persistence.manager.impl;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.constants.DataSourceConsts;
import com.hotent.base.datasource.DataSourceLoader;
import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.ExceptionUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.SQLUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.sys.persistence.dao.SysDataSourceDao;
import com.hotent.sys.persistence.manager.SysDataSourceManager;
import com.hotent.sys.persistence.model.SysDataSource;
import com.hotent.table.datasource.DataSourceUtil;

@Primary
@Service("sysDataSourceManager")
public class SysDataSourceManagerImpl extends BaseManagerImpl<SysDataSourceDao, SysDataSource> implements SysDataSourceManager, DataSourceLoader {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(SysDataSourceManagerImpl.class);
	
	@Override
	public void create(SysDataSource sysDataSource) {
		super.create(sysDataSource);
		updateDataSource(sysDataSource);
	}
	
	@Override
	public DataSource loadByAlias(String alias) {
		SysDataSource sysDataSource = this.getByAlias(alias);
		return this.getDsFromSysSource(sysDataSource);
	}

	@Override
	public void update(SysDataSource sysDataSource) {
		super.update(sysDataSource);
		updateDataSource(sysDataSource);
	}

	private void updateDataSource(SysDataSource sysDataSource){
		// 更新了数据同时也更新beans容器的数据源
		try {
			if (sysDataSource.getEnabled()) {
				DataSource dataSource=	getDsFromSysSource(sysDataSource);
				DataSourceUtil.addDataSource(sysDataSource.getAlias(), dataSource,true);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			LOGGER.error(ExceptionUtil.getExceptionMessage(e));
			throw new BaseException("操作数据源失败" + ExceptionUtil.getExceptionMessage(e));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			LOGGER.error(ExceptionUtil.getExceptionMessage(e));
			throw new BaseException("操作数据源失败" + ExceptionUtil.getExceptionMessage(e));
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOGGER.error(ExceptionUtil.getExceptionMessage(e));
			throw new BaseException("操作数据源失败" + ExceptionUtil.getExceptionMessage(e));
		}
	}

	/**
	 * 
	 * 利用Java反射机制把dataSource成javax.sql.DataSource对象
	 * 
	 * @param sysDataSource
	 * @param dataSourcePool
	 * @return javax.sql.DataSource
	 * @exception
	 * @since 1.0.0
	 */
	public DataSource getDsFromSysSource(SysDataSource sysDataSource) {

		try {
			// 获取对象
			Class<?> _class = null;
			_class = Class.forName(sysDataSource.getClassPath());
			javax.sql.DataSource sqldataSource = null;
			sqldataSource = (javax.sql.DataSource) _class.newInstance();// 初始化对象

			// 开始set它的属性
			String settingJson = sysDataSource.getSettingJson();
			
			ArrayNode arrayNode = (ArrayNode) JsonUtil.toJsonNode(settingJson);
			
			
			for (int i = 0; i < arrayNode.size(); i++) {
				ObjectNode jo = (ObjectNode) arrayNode.get(i);
				Object value = BeanUtils.convertByActType(JsonUtil.getString(jo, "type"),JsonUtil.getString(jo, "value"));
				BeanUtils.setProperty(sqldataSource, JsonUtil.getString(jo, "name"), value);
			}

			// 如果有初始化方法，需要调用，必须是没参数的
			String initMethodStr = sysDataSource.getInitMethod();
			if (!StringUtil.isEmpty(initMethodStr)) {
				Method method = _class.getMethod(initMethodStr);
				method.invoke(sqldataSource);
			}

			return sqldataSource;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug(e.getMessage());
		}

		return null;
	}



	@Override
	public boolean checkConnection(SysDataSource sysDataSource) {
		return checkConnection(getDsFromSysSource(sysDataSource), sysDataSource.getCloseMethod());
	}

	private boolean checkConnection(DataSource dataSource, String closeMethod) {
		boolean b = false;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			b = true;
		} catch(SQLException exc){
			closeInvoke(closeMethod);
			throw new BaseException("连接失败：" + exc.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			closeInvoke(closeMethod);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return b;
	}

	/**
	 * 调用关闭
	 * @param closeMethod
	 */
	private void closeInvoke(String closeMethod){
		if (!StringUtil.isEmpty(closeMethod) && closeMethod.split("\\|").length >= 2) {
			String cp = closeMethod.split("\\|")[0];
			String mn = closeMethod.split("\\|")[1];

			try {
				Class<?> _class = Class.forName(cp);

				Method method = _class.getMethod(mn, null);
				method.invoke(null, null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hotent.platform.system.manager.SysDataSourceManager#getSysDataSourcesInBean()
	 */
	@Override
	public List<SysDataSource> getSysDataSourcesInBean() {
		List<SysDataSource> result = new ArrayList<SysDataSource>();

		Map<String, DataSource> map;
		try {
			map = DataSourceUtil.getDataSources();// 在容器的数据源
		} catch (Exception e) {
			return result;
		}

		QueryFilter queryFilter = QueryFilter.build()
				 .withDefaultPage()
				 .withQuery(new QueryField("enabled_", 1));
		 PageList<SysDataSource> query = this.query(queryFilter);// 用户配置在数据库的数据源
		 List<SysDataSource> sysDataSources = query.getRows();
		// 容器的数据源 交集 数据库配置的数据源
		for (Object key : map.keySet()) {
			for (SysDataSource sysDataSource : sysDataSources) {
				if (sysDataSource.getAlias().equals(key.toString())) {
					result.add(sysDataSource);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hotent.platform.system.manager.SysDataSourceManager#getByAlias(java.lang.String)
	 */
	@Override
	public SysDataSource getByAlias(String alias) {
		QueryFilter queryFilter =  QueryFilter.build()
				 .withDefaultPage()
				 .withQuery(new QueryField("alias_", alias));
		PageList<SysDataSource> sysDataSources = this.query(queryFilter);
		if (sysDataSources != null && !sysDataSources.getRows().isEmpty()) {
			return sysDataSources.getRows().get(0);
		}
		return null;
	}
	
	@Override
	public Map<String, DataSource> getDataSource() {
		List<SysDataSource> list= baseMapper.getDataSource(true, true);
		Map<String, DataSource> maps=new HashMap<String, DataSource>();
		for(SysDataSource sysDataSource:list){
			DataSource ds=getDsFromSysSource(sysDataSource);
			if(ds==null) continue;
			maps.put(sysDataSource.getAlias(), ds);
			
		}
		return maps;
	}
	
	/**
	 * 获取默认数据源
	 * 
	 * @return SysDataSource
	 * @exception
	 * @since 1.0.0
	 */
	@Override
	public SysDataSource getDefaultDataSource() {
		SysDataSource defaultDataSource = new SysDataSource();
		defaultDataSource.setAlias(DataSourceConsts.LOCAL_DATASOURCE);
		defaultDataSource.setName("本地数据源");
		defaultDataSource.setDbType(SQLUtil.getDbType());
		return defaultDataSource;
	}

	@Override
	public boolean isAliasExist(String alias) {
		SysDataSource sysDataSource = getByAlias(alias);
		if (BeanUtils.isNotEmpty(sysDataSource)){
			return true;
		}
		return false;
	}
}
