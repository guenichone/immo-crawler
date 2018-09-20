package org.barrak.immocrawler.impl.seloger;

import org.barrak.immocrawler.crawler.impl.seloger.SeLogerCrawler;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@RestClientTest(SeLogerCrawler.class)
public class SeLogerCrawlerTest {

    @Autowired
    private SeLogerCrawler seLogerCrawler;

    @Autowired
    private MockRestServiceServer server;

    @Before
    public void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("searchResultPage1.html");
        File file = new File(resource.getPath());

//        try (BufferedReader r = Files.newBufferedReader(Path.(resource.getPath(), Charset.forName("UTF-8"))) {
//            r.lines().collect(Collectors.joining());
//        }
//        this.server.expect(requestTo("/john/details"))
//                .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
    }

}
