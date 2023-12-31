package com.hotent.redis.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.hotent.redis.service.RedisCacheManager;
import com.hotent.redis.util.CustomRedisSerialize;

@Configuration
@ConditionalOnProperty(value="redis.enable", matchIfMissing = true)
public class RedisConfig {
 
   /**
    * springboot2.x 使用LettuceConnectionFactory 代替 RedisConnectionFactory
    * application.yml配置基本信息后,springboot2.x  RedisAutoConfiguration能够自动装配
    * LettuceConnectionFactory 和 RedisConnectionFactory 及其 RedisTemplate
    * @param redisConnectionFactory
    * @return
    */
   @Bean
   public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory){
       RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
       redisTemplate.setKeySerializer(new StringRedisSerializer());
       redisTemplate.setValueSerializer(new CustomRedisSerialize());
       redisTemplate.setHashKeySerializer(new StringRedisSerializer());
       redisTemplate.setHashValueSerializer(new CustomRedisSerialize());
       redisTemplate.setConnectionFactory(redisConnectionFactory);
       return redisTemplate;
   }
   
   @Bean(name = "redisCacheManager")
   @Primary
   public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate) {
	   RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
	   return redisCacheManager;
   }
}