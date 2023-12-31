package com.hotent.base.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hotent.base.feign.UCFeignService;

/**
 * feign调用服务相关工具类
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxw
 * @email zhangxw@jee-soft.cn
 * @date 2018年4月11日
 */
public class FeignServiceUtil {

	/**
	 * 根据用户id获取组织路径
	 * @param auditors
	 * @return
	 */
	public static List<Map<String,String>> getPathNames(List<String> auditors){
		if(BeanUtils.isNotEmpty(auditors)){
			UCFeignService ucFeignService = AppUtil.getBean(UCFeignService.class);
			int times = 3;
			try {
				return getPathNamesByTimes(auditors, times, ucFeignService);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}else{
			return new ArrayList<Map<String,String>>();
		}
	}
	
	private static List<Map<String,String>> getPathNamesByTimes(List<String> auditors,int times,UCFeignService ucFeignService) throws Exception{
		try {
			return ucFeignService.getPathNames(auditors);
		} catch (Exception e) {
			if(times>0){
				--times;
				System.out.println("-------------第"+(3-times)+"次调用getPathNames："+JsonUtil.toJson(auditors)+"--------------------");
				return getPathNamesByTimes(auditors, times, ucFeignService);
			}else{
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	
}
