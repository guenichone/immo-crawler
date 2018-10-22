package org.barrak.immocrawler.geoloc.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.barrak.immocrawler.geoloc.IInseeService;
import org.barrak.immocrawler.geoloc.model.opendatasoft.OpendataSoftResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;

@Service
public class OpendataSoftService implements IInseeService {

    private static final String CODE_INSEE_CODE_POSTAL = "correspondance-code-insee-code-postal";

    private String url;

    public OpendataSoftService(@Value("${opendatasoft.api.url}") String url) {
        this.url = url;
    }

    @Override
    public String getCommunalCodeFromPostalCode(String postalCode) {
        InputStream is;
        try {
            is = new URL(buildUrl(CODE_INSEE_CODE_POSTAL, postalCode)).openStream();
            ObjectMapper mapper = new ObjectMapper();
            OpendataSoftResult result = mapper.readValue(is, OpendataSoftResult.class);
            if (result.getRecords().length > 0) {
                return result.getRecords()[0].getFields().getInsee_com();
            }
            is.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        throw new NoSuchElementException("There is no matching insee code for the given postal code : " + postalCode);
    }

    private String buildUrl(String dataset, String query) {
        return url + "/?dataset=" + dataset + "&rows=1" + "&q=" +query;
    }
}
