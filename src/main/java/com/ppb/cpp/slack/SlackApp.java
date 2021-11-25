package com.ppb.cpp.slack;

import com.slack.api.bolt.App;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class SlackApp {
    private static final Logger LOG = getLogger(SlackApp.class);
    @Autowired
    private CPPBotCommand cppBotCommand;

    @Bean
    public App initSlackApp() {
        final var app = new App();
        app.command("/cpp-bot", (req, ctx) -> cppBotCommand.handle(req));
        app.command("/cpp-bot-rares", (req, ctx) -> cppBotCommand.handle(req));
        return app;
    }
}
