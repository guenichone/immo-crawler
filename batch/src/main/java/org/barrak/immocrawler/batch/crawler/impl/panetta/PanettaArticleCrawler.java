package org.barrak.immocrawler.batch.crawler.impl.panetta;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;
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
    public void updateDetails(ArticleDocument article) {
        try {
            Document document = Jsoup.connect(article.getUrl()).followRedirects(false).get();

            // TODO handle invalid link

            String description = getDescription(document);
            article.setDescription(description);

            if (article.getLandSurface() >= 0) {
                article.setLandSurface(article.getLandSurface());
            } else {
                ParserUtils.findLandSurfaceInDescription(description);
            }
            article.setHomeSurface(article.getHomeSurface());
            article.setNbRooms(article.getNbRooms());

            Element gallery = document.getElementById("gallery");
            article.setImageUrls(gallery.getElementsByTag("img").stream()
                    .map(img -> img.attr("src")).collect(Collectors.toSet()));

            article.setDetailsParsed(true);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
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
