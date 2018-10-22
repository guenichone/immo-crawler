package org.barrak.immocrawler.geoloc.model.opendatasoft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpendataSoftResult {
    private int nhits;
    private Record[] records;

    public int getNhits() {
        return nhits;
    }

    public Record[] getRecords() {
        return records;
    }
}
