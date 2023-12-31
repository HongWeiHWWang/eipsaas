package com.hotent.bpm.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.util.AppUtil;

public class PortalDataUtil {
	/**
	 * 根据别名获取系统属性。
	 * @param bpmProcessDefExt
	 * @param jsonObj 
	 * void
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String  getPropertyByAlias(String alias){

		try {
			PortalFeignService service = AppUtil.getBean(PortalFeignService.class);
			return service.getPropertyByAlias(alias);
		}  catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
