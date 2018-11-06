package org.barrak.immocrawler.batch.crawler.impl.optimhome;

import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class OptimHomeArticleCrawler implements IDetailsCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptimHomeArticleCrawler.class);

    @Override
    public void updateDetails(ArticleDocument article) {
        try {
            Document document = Jsoup.connect(article.getUrl()).get();

            if (article.getNbRooms() == -1) {
                article.setNbRooms((int) ParserUtils.getNumericOnly(document.getElementsByClass("nb_piece").first().text()));
            }
            if (article.getHomeSurface() == -1) {
                article.setHomeSurface((int) ParserUtils.getNumericOnly(document.getElementsByClass("p_surface").text()));
            }

            Map<String, String> generalInfo = getGeneralInfo(document);
            article.setLandSurface((int) ParserUtils.getNumericOnly(generalInfo.get("Superficie terrain")));

            String description = document.getElementById("p_descriptif").parent().getElementsByTag("p").text();
            article.setDescription(ParserUtils.inlineText(description));

            if (article.getLandSurface() == -1) {
                article.setLandSurface(ParserUtils.findLandSurfaceInDescription(article.getDescription()));
            }

            article.setImageUrls(getImageUrls(document));
            
            String[] references = getReference(description);
            article.setInternalReference(references[0]);
            article.setExternalReference(references[1]);

            article.setDetailsParsed(true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Set<String> getImageUrls(Document document) {
        return document.getElementById("carouselControls")
                .getElementsByTag("a")
                .stream()
                .map(a -> a.attr("href"))
                .filter(href -> !"#carouselControls".equals(href))
                .collect(Collectors.toSet());
    }

    private String[] getReference(String description) {
        Pattern pattern = Pattern.compile("\\(réf\\. (?<intern>[0-9]+) / (?<extern>[0-9]+) ?\\)");
        Matcher matcher = pattern.matcher(description);

        if (matcher.find()) {
            return new String[]{matcher.group("intern"), matcher.group("extern")};
        } else {
            return new String[2];
        }
    }

    private Map<String, String> getGeneralInfo(Document doc) {
        Elements liElements = doc.getElementsByClass("details_product").first()
                .getElementsByClass("row").first()
                .getElementsByTag("li");

        return liElements.stream()
                .map(li -> li.getElementsByTag("span"))
                .filter(spans -> spans.size() > 1)
                .collect(Collectors.toMap(spans -> spans.get(0).data(), spans -> spans.get(1).data(), (s1, s2) -> s1));
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.OPTIMHOME;
    }
}
