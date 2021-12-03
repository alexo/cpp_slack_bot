package com.ppb.cpp.slack;

import com.ppb.cpp.client.pph.PPHClient;
import com.ppb.cpp.slack.query.HelpQuery;
import com.ppb.cpp.slack.query.PromoCodeQuery;
import com.ppb.cpp.slack.query.Query;
import com.ppb.cpp.slack.query.StatsQuery;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Encapsulates instruction handling for cpp-bot command.
 */
@Component
public class CPPCommand {
    private static final Logger LOG = getLogger(CPPCommand.class);
    private PPHClient pphClient;
    private List<Query> cppQueries;

    public Response handle(final SlashCommandRequest req) {
        LOG.info("handling request={}", req.getPayload().getText());
        return cppQueries.stream()
                .filter(q -> q.accept(req.getPayload().getText()))
                .map(q -> q.handle(req))
                .findFirst()
                .orElseGet(() -> unknownQuery(req));
    }

    private Response unknownQuery(final SlashCommandRequest req) {
        return req.getContext()
                .ack(res -> res.responseType("in_channel").text(":wave: Hi, this is CPP bot. How can I help you? Start by typing: help"));
    }

    @PostConstruct
    public void afterPropertiesSet() {
        cppQueries = new ArrayList<>();
        cppQueries.add(new PromoCodeQuery(pphClient));
        cppQueries.add(new StatsQuery());
        final var help = new HelpQuery();
        cppQueries.forEach(help::register);
        cppQueries.add(help);
    }

    @Autowired
    public void setPphClient(final PPHClient pphClient) {
        this.pphClient = pphClient;
    }
}
