//package com.nhnacademy.heukbaekfrontend.common.sse;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//
//@Configuration
//@RequiredArgsConstructor
//public class RedisSubscriberConfig {
//    private static final String CHANNEL_PATTERN = "sse:publish:customerId:*";
//    private final RedisConnectionFactory redisConnectionFactory;
//    private final RedisEventHandler redisEventHandler;
//
//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer() {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(redisConnectionFactory);
//
//        // 패턴 구독
//        container.addMessageListener(messageListenerAdapter(), new PatternTopic(CHANNEL_PATTERN));
//
//        return container;
//    }
//
//    @Bean
//    public MessageListenerAdapter messageListenerAdapter() {
//        return new MessageListenerAdapter(redisEventHandler, "onMessage");
//    }
//}
