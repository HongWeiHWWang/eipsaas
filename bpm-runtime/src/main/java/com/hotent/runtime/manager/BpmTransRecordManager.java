package com.hotent.runtime.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.runtime.model.BpmTransRecord;

/**
 * 
 * <pre> 
 * 描述：移交记录 处理接口
 * 构建组：x7
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2019-02-18 09:46
 * </pre>
 */
public interface BpmTransRecordManager extends BaseManager<BpmTransRecord> {
	
	/**
	 * 批量移交流程
	 * @param bpmTransRecord
	 * @return
	 */
	CommonResult<String> turnOver(BpmTransRecord bpmTransRecord);
}
