//package com.nhnacademy.heukbaekfrontend.common.sse;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.data.redis.connection.Message;
//
//import java.nio.charset.StandardCharsets;
//
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class RedisEventHandler {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final SseEmitterService sseEmitterService;
//
//    public void onMessage(Message message, byte[] pattern) {
//        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
//        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
//        log.info("received Message from Shop-server, {}: {}", channel, msg);
//
//        try {
//            SseMessage sseMessage = objectMapper.readValue(msg, SseMessage.class);
//            Long customerId = getCustomerIdFromChannel(channel);
//
//            sseEmitterService.sendMessageToCustomer(customerId, sseMessage);
//        } catch (Exception e) {
//            log.error("Sending Message Failed", e);
//        }
//    }
//
//    private Long getCustomerIdFromChannel(String channel) {
//        String[] parts = channel.split(":");
//        if (parts.length < 4) {
//            log.error("Invalid channel format: {}", channel);
//            throw new IllegalArgumentException("Invalid channel format");
//        }
//        return Long.valueOf(parts[3]);
//    }
//}
