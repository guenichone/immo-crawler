package org.barrak.immocrawler.batch.crawler.impl.immoregion;

import org.apache.http.HttpStatus;
import org.barrak.crawler.database.document.ProviderEnum;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ImmoRegionArticleCrawler implements IDetailsCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoRegionArticleCrawler.class);

    @Override
    public void updateDetails(SearchResultDocument article) {
        try {
            Connection.Response response = Jsoup.connect(article.getUrl()).followRedirects(false).execute();
            if (response.statusCode() == HttpStatus.SC_MOVED_PERMANENTLY) {
                throw new NoSuchElementException();
            }
            Document doc = response.parse();

            Element description = doc.getElementsByClass("description").first();
            Elements p = description.getElementsByTag("p");

            article.setDescription(ParserUtils.inlineText(p.get(0).text()));
            article.setInternalReference(ParserUtils.inlineText(p.get(1).textNodes().get(1).text()));
            try {
                article.setExternalReference(ParserUtils.inlineText(p.get(2).textNodes().get(1).text()));
            } catch (Exception ex) {
                // Nothing
            }
            article.setPrice(getPrice(doc));
            article.setImageUrls(getImages(doc));
            article.setLandSurface((int) ParserUtils.getNumericOnly(getGeneralInfo(doc, "Terrain")));
            if (article.getLandSurface() == -1) {
                article.setLandSurface(ParserUtils.findLandSurfaceInDescription(article.getDescription()));
            }
            article.setHomeSurface((int) ParserUtils.getNumericOnly(getGeneralInfo(doc, "Surface")));
            article.setNbRooms((int) ParserUtils.getNumericOnly(getGeneralInfo(doc, "Nombre de pièces")));

            article.setDetailsParsed(true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.IMMOREGION;
    }
}
