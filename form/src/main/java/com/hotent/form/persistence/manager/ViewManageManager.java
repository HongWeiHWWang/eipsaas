package com.hotent.form.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.form.model.ViewManage;

/**
 * 
 * <pre> 
 * 描述：视图管理 处理接口
 * 构建组：x7
 * 作者:pangq
 * 邮箱:pangq@jee-soft.cn
 * 日期:2020-04-30 17:01:50
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface ViewManageManager extends BaseManager<ViewManage>{

	void savePub(ViewManage viewManage, Integer saveType);

	void createPhysicalView(String id);

}
