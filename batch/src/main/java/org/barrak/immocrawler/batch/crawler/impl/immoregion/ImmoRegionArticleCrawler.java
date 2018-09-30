package org.barrak.immocrawler.batch.crawler.impl.immoregion;

import org.apache.http.HttpStatus;
import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ImmoRegionArticleCrawler implements IDetailsCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoRegionArticleCrawler.class);

    @Override
    public SearchResultDetailsDocument getDetails(SearchResultDocument article) {
        return parsePage(article);
    }

    private SearchResultDetailsDocument parsePage(SearchResultDocument article) {
        LOGGER.info("Parsing details for : {}", article.getUrl());

        SearchResultDetailsDocument result = new SearchResultDetailsDocument();
        result.setUrl(article.getUrl());
        result.setCity(article.getCity());

        try {
            Connection.Response response = Jsoup.connect(article.getUrl()).followRedirects(false).execute();
            if (response.statusCode() == HttpStatus.SC_MOVED_PERMANENTLY) {
                throw new NoSuchElementException();
            }
            Document doc = response.parse();

            result.setDescription(getDescription(doc));
            result.setPrice(getPrice(doc));
            result.setImageUrls(getImages(doc));
            result.setLandSurface(ParserUtils.getNumericOnly(getGeneralInfo(doc, "Terrain")));
            if (result.getLandSurface() == -1) {
                result.setLandSurface(findLandSurfaceInDescription(result.getDescription()));
            }
            result.setHomeSurface((int) ParserUtils.getNumericOnly(getGeneralInfo(doc, "Surface")));
            result.setNbRooms((int) ParserUtils.getNumericOnly(getGeneralInfo(doc, "Nombre de pièces")));

            return result;
        } catch (IOException e) {
            return null;
        }
    }

    private String getDescription(Document doc) {
        Element description = doc.getElementsByClass("description")
                .first().getElementsByTag("p").first();
        return ParserUtils.inlineText(description.text());
    }

    private int findLandSurfaceInDescription(String description) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile("[0-9]+([,.][0-9]{1,2})? ares").matcher(description);
        while (m.find()) {
            allMatches.add(m.group());
        }
        if (allMatches.size() == 1) {
            return (int) ParserUtils.getNumericOnly(allMatches.get(0));
        } else if (allMatches.size() > 1) {
            return allMatches.stream().mapToInt(val -> (int) ParserUtils.getNumericOnly(val)).sum();
        } else {
            return -1;
        }
    }

    private int getPrice(Document doc) {
        Element price = doc.getElementsByTag("h2").first();
        return (int) ParserUtils.getNumericOnly(price.text());
    }

    private Set<String> getImages(Document doc) {
        return doc.getElementsByTag("source").stream()
                .map(element -> element.attr("srcset"))
                .collect(Collectors.toSet());
    }

    private String getGeneralInfo(Document doc, String label) {
        return doc.getElementsByClass("feature-bloc-content-specification-content").stream()
                .filter(elem -> {
                    Element div = elem.getElementsByTag("div").first();
                    return div != null && label.equals(div.text());
                })
                .map(elem -> elem.getElementsByTag("div").get(1).text())
                .findFirst().orElse(null);
    }


}
