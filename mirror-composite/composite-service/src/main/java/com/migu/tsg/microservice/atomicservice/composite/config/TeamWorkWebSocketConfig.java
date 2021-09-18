package com.migu.tsg.microservice.atomicservice.composite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 说明: 协同作战室 WebSocket通信配置
 * 工程: Teamwork
 * 作者: zhujuwang
 * 时间: 2021/3/15 22:15
 */
@Configuration
public class TeamWorkWebSocketConfig {

    /**
     * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
