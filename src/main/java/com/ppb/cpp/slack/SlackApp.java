package com.ppb.cpp.slack;

import com.slack.api.bolt.App;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackApp {

    //integrate with PPH
    //public bot reply
    //use case: promo summary (dedicated command)
    //locally use own command
    //integrate firestore (define pph, endpoint)
    //how to secure slack bot & pph communication. (oath)
    //use case: stats
    //use case: for promos about to expire
    //use case: for promos reaching optin limit threshold
    //

    @Bean
    public App initSlackApp() {
        final var app = new App();
        app.command("/hello", (req, ctx) -> {
            System.out.println("command: hello");
            return ctx.ack("Hi there!");
        });
        app.command("/bot", (req, ctx) -> {
            System.out.println("command: bot");
            return ctx.ack("Hi there bot!");
        });
        return app;
    }
}
