package com.frizo.ucc.server.security.oauth2.user;

import com.frizo.ucc.server.exception.OAuth2AuthenticationProcessingException;
import com.frizo.ucc.server.model.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        switch (authProvider){
            case google:
                return new GoogleOAuth2UserInfo(attributes);
            case facebook:
                return new FacebookOAuth2UserInfo(attributes);
            default:
                throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + authProvider + " is not supported yet.");
        }

    }
}
