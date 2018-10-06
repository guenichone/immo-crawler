package org.barrak.immocrawler.batch.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserUtilsTest {

    @Test
    public void inlineText() {
        String in = "AUDUN L\'E ROMAN A 10 mn\r\n";
        String expected = "AUDUN L'E ROMAN A 10 mn";
        String out = ParserUtils.inlineText(in);

        assertThat(out).isEqualTo(expected);
    }
}