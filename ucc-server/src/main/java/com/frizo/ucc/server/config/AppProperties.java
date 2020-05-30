package com.frizo.ucc.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();
    private final FileDir fileDir = new FileDir();

    public static class FileDir {
        private String avatarDir;
        private String avatarBaseUrl;
        private String backgroundDir;
        private String backgroundBaseUrl;
        private String dmDir;
        private String dmBaseUrl;

        public String getAvatarDir() {
            return this.avatarDir;
        }

        public void setAvatarDir(String avatarDir) {
            this.avatarDir = avatarDir;
        }

        public String getBackgroundDir() {
            return this.backgroundDir;
        }

        public void setBackgroundDir(String backgroundDir) {
            this.backgroundDir = backgroundDir;
        }

        public String getAvatarBaseUrl() {
            return avatarBaseUrl;
        }

        public void setAvatarBaseUrl(String avatarBaseUrl) {
            this.avatarBaseUrl = avatarBaseUrl;
        }

        public String getBackgroundBaseUrl() {
            return backgroundBaseUrl;
        }

        public void setBackgroundBaseUrl(String backgroundBaseUrl) {
            this.backgroundBaseUrl = backgroundBaseUrl;
        }

        public String getDmDir() {
            return dmDir;
        }

        public void setDmDir(String dmDir) {
            this.dmDir = dmDir;
        }

        public String getDmBaseUrl() {
            return dmBaseUrl;
        }

        public void setDmBaseUrl(String dmBaseUrl) {
            this.dmBaseUrl = dmBaseUrl;
        }
    }

    public static class Auth {
        private String tokenSecret;

        private long tokenExpirationMsec;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }
    }

    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }

    public FileDir getFileDir() {
        return fileDir;
    }
}
