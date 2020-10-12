package com.frizo.ucc.server.websocket;

import com.frizo.ucc.server.security.CustomUserDetailsService;
import com.frizo.ucc.server.security.TokenProvider;
import com.frizo.ucc.server.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            System.out.println("bearerToken: " + bearerToken);
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                Map sessionAttributes = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
                sessionAttributes.put(CsrfToken.class.getName(), new DefaultCsrfToken("Authorization", "Authorization", bearerToken));
                String jwt = bearerToken.substring(7, bearerToken.length());
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
                    Long userId = tokenProvider.getUserIdFromToken(jwt);
                    String securityCode = tokenProvider.getUserSecurityCodeFromToken(jwt);
                    UserPrincipal userPrincipal = customUserDetailsService.loadUserById(userId);
                    if (userPrincipal.getSecurityCode().equals(securityCode)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        return message;
    }
}
