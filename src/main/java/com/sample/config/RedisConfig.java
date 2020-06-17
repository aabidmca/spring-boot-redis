package com.sample.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by OmiD.HaghighatgoO on 8/19/2019.
 */

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
    	System.out.println("Creating Redis Template Bean");
    	RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setEnableDefaultSerializer(false);
        
        RedisSerializer<?> genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        redisTemplate.setConnectionFactory(connectionFactory());
      //  redisTemplate.setEnableTransactionSupport(true);
        System.out.println("Redis Template Bean Created");
        return redisTemplate;
    }
    
    @Bean
    public LettuceConnectionFactory connectionFactory() {
    	RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("localhost");
        configuration.setPort(6379);
    	return new LettuceConnectionFactory(configuration);
    }
     
}

