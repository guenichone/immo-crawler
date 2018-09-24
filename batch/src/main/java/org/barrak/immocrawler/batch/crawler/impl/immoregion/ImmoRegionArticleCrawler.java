package org.barrak.immocrawler.batch.crawler.impl.immoregion;

import org.apache.http.HttpStatus;
import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;
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
            result.setLandSurface(getNumericOnly(getGeneralInfo(doc, "Terrain")));
            result.setHomeSurface((int) getNumericOnly(getGeneralInfo(doc, "Surface")));
            result.setNbRooms((int) getNumericOnly(getGeneralInfo(doc, "Nombre de pièces")));

            return result;
        } catch (IOException e) {
            return null;
        }
    }

    private String getDescription(Document doc) {
        Element description = doc.getElementsByClass("description")
                .first().getElementsByTag("p").first();
        return inlineText(description.text());
    }

    private int getPrice(Document doc) {
        Element price =doc.getElementsByTag("h2").first();
        return (int) getNumericOnly(price.text());
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

    private String inlineText(String text) {
        return text.replaceAll("\n", "").replaceAll("\t", "");
    }

    private double getNumericOnly(String text) {
        try {
            return Double.valueOf(text.replaceAll("[^\\d\\,]", "").replaceAll(",", "."));
        } catch (NullPointerException ex) {
            LOGGER.error("NullPointerException for '" + text + "'");
        } catch (NumberFormatException ex) {
            LOGGER.error("NumberFormatException for '" + text + "'", ex);
        }
        return -1;
    }
}
