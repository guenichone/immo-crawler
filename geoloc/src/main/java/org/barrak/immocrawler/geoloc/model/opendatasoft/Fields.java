package org.barrak.immocrawler.geoloc.model.opendatasoft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {
    private String insee_com;
    private String nom_comm;

    public String getInsee_com() {
        return insee_com;
    }

    public String getNom_comm() {
        return nom_comm;
    }
}
