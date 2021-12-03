package com.ppb.cpp.slack.query;

import com.ppb.cpp.support.Text;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.block.LayoutBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class HelpQuery implements Query {
    private static final String QUERY_COMMAND = "help";
    private List<Query> knownQueries = new ArrayList<>();

    @Override
    public boolean accept(final String text) {
        return new Text(text).isFirstWord(QUERY_COMMAND);
    }

    @Override
    public Response handle(final SlashCommandRequest req) {
        return req.getContext()
                .ack(res -> res.blocks(computeHelpResponse(req)));
    }

    private List<LayoutBlock> computeHelpResponse(final SlashCommandRequest req) {
        final var specificQuery = new Text(req.getPayload().getText())
                .findLastWord()
                .orElse(null);
        return knownQueries.stream()
                .filter(q -> q.accept(specificQuery))
                .map(Query::describe)
                .findFirst()
                .map(Arrays::asList)
                .orElseGet(this::allKnownDescriptions);
    }

    private List<LayoutBlock> allKnownDescriptions() {
        return concat(Stream.of(describe()), knownQueries.stream().map(Query::describe))
                .flatMap(b -> Stream.of(b, divider()))
                .collect(toList());
    }

    @Override
    public LayoutBlock describe() {
        return section(section -> section.text(markdownText("*help*\n Displays all available instructions that the bot can handle")));
    }

    public void register(final Query instruction) {
        knownQueries.add(instruction);
    }

}
