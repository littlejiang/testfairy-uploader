package com.testfairy.uploader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StringsTest {
    @Test
    public void isEmpty_returns_true_with_null() {
        assertEquals(true, Strings.isEmpty(null));
    }

    @Test
    public void isEmpty_returns_true_with_empty_string() {
        assertEquals(true, Strings.isEmpty(""));
    }

    @Test
    public void isEmpty_returns_true_with_whitespace() {
        assertEquals(true, Strings.isEmpty("  "));
    }

    @Test
    public void isEmpty_returns_false_with_character() {
        assertEquals(false, Strings.isEmpty("a"));
    }
}
