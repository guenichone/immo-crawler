package org.barrak.immocrawler.geoloc.model.opendatasoft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Record {
    private String datasetid;
    private Fields fields;

    public String getDatasetid() {
        return datasetid;
    }

    public Fields getFields() {
        return fields;
    }
}
