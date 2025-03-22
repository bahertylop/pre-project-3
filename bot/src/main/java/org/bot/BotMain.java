package org.bot;

import org.bot.api.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties(ApiProperties.class)
@EnableFeignClients
public class BotMain {
    public static void main(String[] args) {
        SpringApplication.run(BotMain.class);
    }
}