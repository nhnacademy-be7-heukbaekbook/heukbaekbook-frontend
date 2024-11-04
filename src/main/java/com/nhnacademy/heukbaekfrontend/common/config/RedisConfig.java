package com.nhnacademy.heukbaekfrontend.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.StringTokenizer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final SecureKeyManagerConfig secureKeyManagerConfig;

    @Value("${redis.key}")
    private String redisKey;

    @Value("${redis.database}")
    private int database;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String secret = secureKeyManagerConfig.getSecret(redisKey);
        StringTokenizer st = new StringTokenizer(secret, ",");

        String host = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        String password = st.nextToken();

        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(host, port);
        connectionFactory.setPassword(password);
        connectionFactory.setDatabase(database);

        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> sessionRedisTemplate = new RedisTemplate<>();
        sessionRedisTemplate.setConnectionFactory(redisConnectionFactory);
        sessionRedisTemplate.setKeySerializer(new StringRedisSerializer());
        sessionRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        sessionRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        sessionRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return sessionRedisTemplate;
    }
}
