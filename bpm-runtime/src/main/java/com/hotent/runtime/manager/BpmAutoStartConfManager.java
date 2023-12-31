package com.hotent.runtime.manager;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.runtime.model.BpmAutoStartConf;

/**
 * 
 * <pre> 
 * 描述：流程表单数据修改记录 处理接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-23 11:45:27	
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BpmAutoStartConfManager extends BaseManager<BpmAutoStartConf>{

	BpmAutoStartConf getByDefKey(String defKey);

	ObjectNode defAutoStart() throws Exception;
	
}
