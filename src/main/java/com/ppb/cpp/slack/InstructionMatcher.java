package com.ppb.cpp.slack;

import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;

import static java.lang.String.format;

class InstructionMatcher {
    private SlashCommandPayload payload;

    public InstructionMatcher(final SlashCommandPayload payload) {
        this.payload = payload;
    }

    public boolean is(final String instruction) {
        return payload.getText().matches(format("(?i)^%s\\b.*", instruction));
    }
}
