package com.nhnacademy.heukbaekfrontend.common.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekfrontend.common.config.keymanager.SecureKeyManagerConfig;
import com.nhnacademy.heukbaekfrontend.common.rabbitmq.dto.RabbitMqConfigResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.nhnacademy.heukbaekfrontend.common.rabbitmq.util.CouponRabbitMqConstants.*;


@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final SecureKeyManagerConfig secureKeyManagerConfig;

    @Value("${rabbitmq.key}")
    private String rabbitMqKey;

    @Bean
    public ConnectionFactory connectionFactory() {
        String rabbitMqConfig = secureKeyManagerConfig.getSecret(rabbitMqKey);

        RabbitMqConfigResponse rabbitMqConfigResponse = getRabbitMqConfig(rabbitMqConfig);

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqConfigResponse.host());
        connectionFactory.setPort(rabbitMqConfigResponse.port());
        connectionFactory.setUsername(rabbitMqConfigResponse.userName());
        connectionFactory.setPassword(rabbitMqConfigResponse.password());
        connectionFactory.setVirtualHost(rabbitMqConfigResponse.virtualHost());

        return connectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setReplyTimeout(15000L);
        return rabbitTemplate;
    }

    // CouponIssue

    @Bean
    public DirectExchange couponIssueExchange() {
        return new DirectExchange(COUPON_ISSUE_EXCHANGE);
    }


    private RabbitMqConfigResponse getRabbitMqConfig(String rabbitMqConfig){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(rabbitMqConfig, RabbitMqConfigResponse.class);
        }catch (Exception e){
            throw new RuntimeException("SecureKeyManager parsing Error, RabbitMQ", e);
        }

    }
}
