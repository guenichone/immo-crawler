package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.ICrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Component
public class SeLogerCrawler implements ICrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeLogerCrawler.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${provider.seloger.api}")
    private String apiUrl;
    @Value("${provider.seloger.cityApi}")
    private String cityApiUrl;

    @Override
    public List<SearchResultDocument> search(SearchCriteria criteria) {

        Map<String, String> paramMap =  new HashMap<>();
        paramMap.put("enterprise", "0");
        // paramMap.put("places", getCityParam(criteria.getCity());
        paramMap.put("price", criteria.getMinPrice() + "/" + criteria.getMaxPrice());
        paramMap.put("proximity", "0/" + criteria.getAround());

        paramMap.put("natures", "1,2,4"); // TODO
        paramMap.put("projects", "2"); // TODO
        paramMap.put("types", "2"); // TODO

        paramMap.put("qsversion", "1.0");

        // https://www.seloger.com/list.htm?
        // enterprise=0
        // &natures=1,2,4
        // &places=%5b%7bci%3a570041%7d%5d
        // &price=150000%2f600000
        // &projects=2
        // &proximity=0,10
        // &qsversion=1.0
        // &types=2

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class, paramMap);

        LOGGER.info("Response {}", response.getBody());

        return null;
    }

    private String getCityParam(List<String> cities) {
        List<String> ciList = new ArrayList<>();
        for (String city : cities) {
            String cityUrl = cityApiUrl + "text=" + city.toLowerCase();

            LOGGER.info("Calling cityApi : {}", cityUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.add("origin","https://www.seloger.com");
            // headers.add("Referer", "https://www.seloger.com/list.htm?types=2%2C4&projects=2&enterprise=0&natures=1%2C2%2C4&proximity=0%2C10&places=%5B%7Bci%3A570041%7D%5D&qsVersion=1.0");
            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<String> response = restTemplate.exchange(cityUrl, HttpMethod.GET, entity, String.class);

            JsonParser jsonParser = new BasicJsonParser();
            List<Object> jsonObjects = jsonParser.parseList(response.getBody());

            LOGGER.info("jsonobjects {}", jsonObjects);
        }

        // [{ci:570041}<,...>]
        String cityParam = "[" + ciList.stream().collect(Collectors.joining(",")) + "]";

        LOGGER.info("CityParam", cityParam);

        return cityParam;
    }
}
