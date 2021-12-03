package com.ppb.cpp.slack;

import com.slack.api.bolt.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackApp {
    @Autowired
    private CPPBotCommand cppCommand;

    @Bean
    public App initSlackApp() {
        final var app = new App();
        app.command("/cpp", (req, ctx) -> cppCommand.handle(req));
        app.command("/cpp-bot", (req, ctx) -> cppCommand.handle(req));
        app.command("/cpp-bot-rares", (req, ctx) -> cppCommand.handle(req));
        return app;
    }
}
