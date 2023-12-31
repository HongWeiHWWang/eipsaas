package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmDefData;

/**
 * 
 * <pre> 
 * 描述：流程自动发起配置表 处理接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-04-07 10:51:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BpmDefDataManager extends BaseManager<BpmDefData>{

	void delByDefKey(String defKey);

		
}
