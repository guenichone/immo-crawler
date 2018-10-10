package org.barrak.immocrawler.batch.crawler.impl.leboncoin;

import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LeboncoinArticleCrawler implements IDetailsCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeboncoinArticleCrawler.class);

    @Override
    public void updateDetails(SearchResultDocument article) {
        try {
            Document document = LeboncoinJsoupConnectionUpdater.addConnectionParams(Jsoup.connect(article.getUrl())).get();

            String description = document.getElementsByAttributeValue("data-qa-id", "adview_description_container").text();
            article.setDescription(ParserUtils.inlineText(description.substring(0, description.length() - 16)));

            String nbRooms = document.getElementsByAttributeValue("data-qa-id", "criteria_item_rooms").text();
            article.setNbRooms((int) ParserUtils.getNumericOnly(nbRooms));

            article.setHomeSurface((int) ParserUtils.getNumericOnly(
                    document.getElementsByAttributeValue("data-qa-id", "criteria_item_square").text()));

            article.setLandSurface(ParserUtils.findLandSurfaceInDescription(article.getDescription()));

            String city = document.getElementsByAttributeValue("data-qa-id", "adview_location_informations").first()
                    .getElementsByTag("span").text();
            article.setCity(city.substring(0, city.length() - 6).toLowerCase());

            article.setImageUrls(getImages(document));

            article.setDetailsParsed(true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Set<String> getImages(Document doc) {
        return doc.getElementsByAttributeValue("data-qa-id", "slideshow_container").first()
                .getElementsByAttributeValueContaining("style", "background-image: ").stream()
                .map(element -> {
                    String style = element.attr("style");
                    style = style.replaceAll("background-image:url\\(", "");
                    return style.substring(0, style.length() - 2);
                })
                .collect(Collectors.toSet());
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.LEBONCOIN_PARTICULIER;
    }
}
