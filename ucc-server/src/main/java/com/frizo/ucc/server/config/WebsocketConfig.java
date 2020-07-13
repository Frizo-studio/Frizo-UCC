package com.frizo.ucc.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 開啟 websocket 消息代理功能，包括配置 SimpleMessageingTemplate Bean。
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 消息代理終點。Client 可以訂閱這裡獲取訊息。
        registry.setApplicationDestinationPrefixes("/app"); // 消息處理器
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/endpont").withSockJS(); // Websocket 端點地址，支援 SockJS。
    }
}
