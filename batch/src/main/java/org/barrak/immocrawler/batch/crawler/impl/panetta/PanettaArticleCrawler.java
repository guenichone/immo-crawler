package org.barrak.immocrawler.batch.crawler.impl.panetta;

import org.barrak.crawler.database.document.ProviderEnum;
import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PanettaArticleCrawler implements IDetailsCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanettaArticleCrawler.class);

    @Override
    public SearchResultDetailsDocument getDetails(SearchResultDocument article) {
        try {
            Document document = Jsoup.connect(article.getUrl()).followRedirects(false).get();

            SearchResultDetailsDocument result = new SearchResultDetailsDocument();
            result.setInternalReference(ProviderEnum.PANETTA_IMMO);
            result.setUrl(article.getUrl());
            result.setCity(article.getCity());
            result.setPrice(article.getPrice());

            String description = getDescription(document);
            result.setDescription(description);

            if (article.getLandSurface() >= 0) {
                result.setLandSurface(article.getLandSurface());
            } else {
                ParserUtils.findLandSurfaceInDescription(description);
            }
            result.setHomeSurface(article.getHomeSurface());
            result.setNbRooms(article.getNbRooms());

            Element gallery = document.getElementById("gallery");
            result.setImageUrls(gallery.getElementsByTag("img").stream()
                    .map(img -> img.attr("src")).collect(Collectors.toSet()));

            return result;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    private String getDescription(Document article) {
        return ParserUtils.inlineText(article.getElementsByClass("description").first().text())
            .replaceFirst("Description ", "");
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.PANETTA_IMMO;
    }
}
