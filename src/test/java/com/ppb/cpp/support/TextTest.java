package com.ppb.cpp.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {
    @Test
    void firstWordMatcherShouldBeCaseInsensitive() {
        assertTrue(new Text("promoCode").isFirstWord("PROMOCODE"));
        assertTrue(new Text("promoCode").isFirstWord("promoCode"));
    }

    @Test
    void shouldNotMatchFirstWordWhenValueHasDifferentCharacters() {
        assertFalse(new Text("promoCode1").isFirstWord("promoCode"));
    }
}