package com.hotent.form.persistence.manager.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.SQLUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.form.persistence.dao.ViewManageDao;
import com.hotent.form.model.ViewManage;
import com.hotent.form.persistence.manager.ViewManageManager;
import com.hotent.table.operator.IViewOperator;
import com.hotent.table.util.MetaDataUtil;

/**
 * 
 * <pre>
 *  
 * 描述：视图管理 处理实现类
 * 构建组：x7
 * 作者:pangq
 * 邮箱:pangq@jee-soft.cn
 * 日期:2020-04-30 17:01:50
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("viewManageManager")
public class ViewManageManagerImpl extends BaseManagerImpl<ViewManageDao, ViewManage> implements ViewManageManager {
	@Resource
	DatabaseContext databaseContext;

	/**
	 * 保存视图
	 * @param saveType 保存类型：0仅保存，1保存并创建视图
	 */
	@Override
	@Transactional
	public void savePub(ViewManage viewManage, Integer saveType) {
		if (saveType == 1) {
			this.createPhysicalView(viewManage);
			viewManage.setStatus(ViewManage.Generated);
		}
		
		if (StringUtil.isEmpty(viewManage.getId())) {
			this.create(viewManage);
		} else {
			this.update(viewManage);
		}
	}

	@Override
	@Transactional
	public void createPhysicalView(String id) {
		ViewManage viewManage = this.get(id);
		this.createPhysicalView(viewManage);
		viewManage.setStatus(ViewManage.Generated);
		this.update(viewManage);
	}
	
	public void createPhysicalView(ViewManage viewManage){
		if(SQLUtil.containsSqlInjection(viewManage.getSql())){
			throw new BaseException("sql语句含有非法注入！");
		}
		try (DatabaseSwitchResult dResult = databaseContext.setDataSource(viewManage.getDsAlias())) {
			IViewOperator iViewOperator = MetaDataUtil.getIViewOperatorAfterSetDT(dResult.getDbType());
			iViewOperator.createOrRep(viewManage.getViewName(), viewManage.getSql());
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
	}

}
