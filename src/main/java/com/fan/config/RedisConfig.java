package com.fan.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;

@Configuration
public class RedisConfig {
//    @Bean
//    @SuppressWarnings("all")
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate();
//        template.setConnectionFactory(redisConnectionFactory);
//        //json序列化
//        Jackson2JsonRedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        //转义
//        ObjectMapper om=new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        objectJackson2JsonRedisSerializer.setObjectMapper(om);
//        //String 序列化
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//
//        //key采用String的序列化方式
//        template.setKeySerializer(stringRedisSerializer);
//        //hash key采用String的序列化方式
//        template.setHashKeySerializer(stringRedisSerializer);
//        //value采用json的序列化方式
//        template.setValueSerializer(objectJackson2JsonRedisSerializer);
//        //hash value采用json的序列化方式
//        template.setHashValueSerializer(objectJackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//        return template;
//    }
//@Bean(name = "redisTemplate")
//public RedisTemplate<String, Object> functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//    redisTemplate.setConnectionFactory(redisConnectionFactory);
//    // 使用Jackson2JsonRedisSerialize 替换默认序列化
//    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//    //设置value的序列化规则和 key的序列化规则
//    RedisSerializer stringSerializer = new StringRedisSerializer(Charset.forName("UTF8"));
//    redisTemplate.setKeySerializer(stringSerializer );
//    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//    redisTemplate.setHashKeySerializer(stringSerializer );
//    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//    redisTemplate.afterPropertiesSet();
//    return redisTemplate;
//}

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate (RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
                Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }


}
