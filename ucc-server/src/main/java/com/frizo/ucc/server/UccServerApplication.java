package com.frizo.ucc.server;

import com.frizo.ucc.server.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class UccServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UccServerApplication.class, args);
    }
}
