package com.frizo.ucc.server.websocket;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        HttpHeaders headers = serverHttpRequest.getHeaders();
        headers.forEach((k , v) -> {
            System.out.println("key: " + k + " val: " + v);
        });
        System.out.println("----------------------------------------------------");
        map.forEach((k, v) -> {
            System.out.println("key: " + k + " val: " + v);
        });

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("before handshake, username: " + name);
        if(StringUtils.isEmpty(name)){
            return false;
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
    }
}
