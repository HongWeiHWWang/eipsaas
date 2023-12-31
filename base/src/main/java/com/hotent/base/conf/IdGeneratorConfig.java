package com.hotent.base.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotent.base.id.IdGenerator;

/**
 * ID生成器配置
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月19日
 */
@Configuration
public class IdGeneratorConfig {
	@Value("${system.id.workerId:1}")
    private Long workerId;
	@Value("${system.id.datacenterId:1}")
    private Long datacenterId;
	
	@Bean
	public IdGenerator defaultIdGenerator(){
		IdGenerator idGenerator = new IdGenerator(workerId,datacenterId);
		return idGenerator;
	}
}
