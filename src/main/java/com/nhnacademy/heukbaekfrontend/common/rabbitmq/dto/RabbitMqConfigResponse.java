package com.nhnacademy.heukbaekfrontend.common.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RabbitMqConfigResponse (
        String host,
        int port,
        @JsonProperty("username")
        String userName,
        String password,
        String virtualHost
){
}
