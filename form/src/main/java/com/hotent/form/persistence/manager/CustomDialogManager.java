package com.hotent.form.persistence.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.form.model.CustomDialog;

/**
 * 自定义对话框管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
public interface CustomDialogManager extends BaseManager<CustomDialog> {
	/**
	 * 通过别名获取自定义对话框
	 * @param alias
	 * @return
	 */
	CustomDialog getByAlias(String alias);
	/**
	 * 通过查询参数对自定义对话框进行查询
	 * @param customDialog
	 * @param param
	 * @param dbType
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public List geTreetData(CustomDialog customDialog, Map<String, Object> param, String dbType) throws IOException;
	
	/**
	 * 通过查询参数对自定义对话框进行分页查询
	 * @param customDialog
	 * @param param
	 * @param dbType
	 * @param pageBean
	 * @return
	 */
    @SuppressWarnings("rawtypes")
    public PageList getListData(CustomDialog customDialog, Map<String, Object> param, PageBean pageBean) throws Exception;

	/**
	 * 自定义对话框查询
	 * @return
	 * @throws Exception
	 */
	PageList getCustomDialogData( String alias, QueryFilter filter, String mapParam) throws Exception;

	/**
	 * 手机端自定义对话框
	 * @param isCombine
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	Map getMobileCustomDialogData(Boolean isCombine, String alias) throws Exception;
}
