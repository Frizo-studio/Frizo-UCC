package com.frizo.ucc.server.model;

public enum AuthProvider {
    local("local"),
    facebook("facebook"),
    google("google");

    private String name;

    AuthProvider(String authProvider) {
        this.name = authProvider;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
