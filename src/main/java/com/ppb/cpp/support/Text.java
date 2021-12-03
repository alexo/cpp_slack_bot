package com.ppb.cpp.support;

import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public class Text {
    public static final String REGEX_FIRST_WORD = "(?i)^%s\\b.*";
    private String value;
    public Text(final String value) {
        this.value = value;
    }

    /**
     * @return true if the argument matches the first word from the text. The match is case insensitive.
     */
    public boolean isFirstWord(final String word) {
        return value.matches(format(REGEX_FIRST_WORD, word));
    }

    public Optional<String> findLastWord() {
        return tokens().reduce((first, second) -> second);
    }

    private Stream<String> tokens() {
        return stream(value.split("\\s+"));
    }
}
