package org.barrak.immocrawler.geoloc.impl;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

public class OpendataSoftServiceTest {

    private OpendataSoftService opendataSoftService = new OpendataSoftService("https://public.opendatasoft.com/api/records/1.0/search");

    @Test
    public void getCommunalCodeFromPostalCode() throws IOException {
        String inseeCode = opendataSoftService.getCommunalCodeFromPostalCode("54680");

        Assertions.assertThat(inseeCode).isEqualTo("54149");
    }
}