package com.frizo.ucc.server.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class MyPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("detaultHandshakeHandler: " + name);
        if(StringUtils.isEmpty(name)){
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
