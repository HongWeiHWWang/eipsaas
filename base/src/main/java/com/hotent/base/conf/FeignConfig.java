package com.hotent.base.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotent.base.annotation.IgnoreOnAssembly;
import com.hotent.base.constants.SystemConstants;
import com.hotent.base.util.Base64;
import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.StringUtil;

import feign.Contract;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 
 * @author liyg
 * @Date 2018-08-14
 */
@Configuration
@IgnoreOnAssembly	
public class FeignConfig{

	@Value("${feign.encry.key:feignCallEncry}")
	private String encryKey;

	@Bean
	public Contract feignContract(){
		return new SpringMvcContract();
	}

	/**
	 * 从请求中获取 Authorization设置到feign请求中
	 * @return
	 */
	@Bean
	public RequestInterceptor requestTokenBearerInterceptor() {
		return new RequestInterceptor() {
			@Override	
			public void apply(RequestTemplate requestTemplate) {
				String token ="";
				requestTemplate.header("Referer", SystemConstants.REFERER_FEIGN);
				try {
					token = HttpUtil.getRequest().getHeader("Authorization");
				} catch (Exception e) {}
				if(StringUtil.isNotEmpty(token)){
					requestTemplate.header("Authorization", token);
				}else {
					try {
						requestTemplate.header("Authorization","Basic "+ Base64.getBase64("admin:"+EncryptUtil.encrypt(encryKey)));
					} catch (Exception e) {
					}
				}
			}
		};
	}

}
