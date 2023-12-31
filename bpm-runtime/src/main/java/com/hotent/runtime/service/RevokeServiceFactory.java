package com.hotent.runtime.service;

import java.util.HashMap;
import java.util.Map;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.model.process.nodedef.ext.CustomSignNodeDef;
import com.hotent.runtime.service.impl.BeforeSignRevokeService;
import com.hotent.runtime.service.impl.ParallelApproveRevokeService;
import com.hotent.runtime.service.impl.ParallelRevokeService;
import com.hotent.runtime.service.impl.SequentialRevokeService;

/**
 * 撤回处理器工厂类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年3月11日
 */
public class RevokeServiceFactory {
	private static final Map<String, RevokeService> map = new HashMap<>();
	
	/**
	 * 根据类型获取对应的撤回处理器
	 * @param signType
	 * @return
	 */
	public static RevokeService getRevokeService(String signType) {
		RevokeService revokeService = (RevokeService)map.get(signType);
		
		if(revokeService == null) {
			switch(signType) {
				// 并行签署
	            case CustomSignNodeDef.SIGNTYPE_PARALLEL:
	            	revokeService = AppUtil.getBean(ParallelRevokeService.class);
	                break;
	            // 并行审批
	            case CustomSignNodeDef.SIGNTYPE_PARALLELAPPROVE:
	            	revokeService = AppUtil.getBean(ParallelApproveRevokeService.class);
	                break;
	            // 串行签署
	            case CustomSignNodeDef.SIGNTYPE_SEQUENTIAL:
	            	revokeService = AppUtil.getBean(SequentialRevokeService.class);
	                break;
	            // 串并签前置任务
	            case CustomSignNodeDef.BEFORE_SIGN:
	            	revokeService = AppUtil.getBean(BeforeSignRevokeService.class);
	            	break;
			}
		}
		
		return revokeService;
	}
}
