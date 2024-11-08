package com.shopping.orderservice.common.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    // RedisConnectionFactory: Redis 서버 연결 설정
    // redis는 16개의 db를 제공 -> 각 db마다 서로 다른 데이터 저장
    // 구분하기 위해서 @Qualifier를 사용해서 구분을 지어줘야 함.
    @Bean
    @Qualifier("refresh-token-db")
    public RedisConnectionFactory refreshTokenFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        configuration.setDatabase(0); // default: 0
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("chat-db")
    public RedisConnectionFactory chatFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        configuration.setDatabase(1);
        return new LettuceConnectionFactory(configuration);
    }


    @Bean
    @Qualifier("refresh-template")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("refresh-token-db") RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // redis의 key를 문자열로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        // redis의 value를 JSON으로 직렬화
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(factory);

        return template;
    }
}
