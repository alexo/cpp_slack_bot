package com.ppb.cpp.slack;

import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.vavr.API.*;
import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class SlackApp {
    private static final Logger LOG = getLogger(SlackApp.class);
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


    public static void main(String[] args) {
        System.out.println ("test2");
        System.out.println("help".matches("help"));
    }

    @Bean
    public App initSlackApp() {
        final var app = new App();
        app.command("/hello", (req, ctx) -> Match(new Router(req.getPayload())).of(
                Case($(r -> r.isInstruction("promocode")), promoCodeResponse(req)),
                Case($(), helpResponse(req))));
        app.command("/cpp-bot", (req, ctx) -> {
            LOG.info("command: cpp-bot");
            return ctx.ack(":wave: Hi there bot!");
        });

        return app;
    }

    private class Router {
        private SlashCommandPayload payload;
        public Router(final SlashCommandPayload payload) {
            this.payload = payload;
        }

        public boolean isInstruction(final String instruction) {
            return payload.getText().startsWith(instruction);
        }
    }

    private Response helpResponse(final SlashCommandRequest req) {
        return req.getContext().ack("Available instructions: [help|promoCode]");
    }


    private Response promoCodeResponse(final SlashCommandRequest req) {
        return req.getContext().ack("Available instructions: [help|promoCode]");
    }
}
