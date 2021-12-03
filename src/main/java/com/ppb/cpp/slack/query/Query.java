package com.ppb.cpp.slack.query;

import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.block.LayoutBlock;

import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static java.lang.String.format;

public interface Query {
    /**
     * @return true if the payload text contains the instruction that can be handled.
     */
    default boolean accept(final String text) {
        return false;
    }

    /**
     * Performs actual handling when the instruction is accepted.
     */
    default Response handle(final SlashCommandRequest req) {
        return req.getContext().ack(":wave: Not implemented yet...");
    }

    /**
     * @return the description for the help section.
     */
    default LayoutBlock describe() {
        return section(section -> section.text(markdownText(format("*%s*\nNo documentation provided", getClass().getSimpleName()))));
    }
}
