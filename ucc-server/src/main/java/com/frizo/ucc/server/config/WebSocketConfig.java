package com.frizo.ucc.server.config;

import com.frizo.ucc.server.security.CustomUserDetailsService;
import com.frizo.ucc.server.security.TokenProvider;
import com.frizo.ucc.server.security.UserPrincipal;
import com.frizo.ucc.server.websocket.AuthHandshakeInterceptor;
import com.frizo.ucc.server.websocket.MyPrincipalHandshakeHandler;
import com.frizo.ucc.server.websocket.WebSocketAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.DefaultCsrfToken;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Map;


@Configuration
@EnableWebSocketMessageBroker // 開啟 websocket 消息代理功能，包括配置 SimpleMessageingTemplate Bean。
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private MyPrincipalHandshakeHandler myDefaultHandshakeHandler;
    @Autowired
    private AuthHandshakeInterceptor authHandshakeInterceptor;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 消息代理終點。Client 可以訂閱這裡獲取訊息。
        registry.setApplicationDestinationPrefixes("/app"); // 消息處理器 prefix
        registry.setUserDestinationPrefix("/user"); // 推送 user prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/endpont")
                .setAllowedOrigins("http://localhost:3000")
                .addInterceptors(authHandshakeInterceptor)
                .setHandshakeHandler(myDefaultHandshakeHandler)
                .withSockJS(); // Websocket 端點地址，支援 SockJS。
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
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
                                accessor.setUser(authentication);
                                System.out.println("在Interceptor中的 Name : " + authentication.getName());
                            }
                        }
                    }
                }
                return message;
            }
        });
    }

}
