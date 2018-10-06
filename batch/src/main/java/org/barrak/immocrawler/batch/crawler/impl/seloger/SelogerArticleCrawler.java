package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.crawler.database.document.ProviderEnum;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SelogerArticleCrawler implements IDetailsCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelogerArticleCrawler.class);

    @Override
    public void updateDetails(SearchResultDocument article) {
        try {
            Document document = Jsoup.connect(article.getUrl()).get();

            // TODO handle incorrect links

            String id = ParserUtils.matchesByRegexGroup(article.getUrl(), "(?<id>[0-9]+).htm", "id").get(0).get("id");
            Map<String, Object> details = getDetails(id);

            String description = (String) details.get("descriptif");
            article.setDescription(ParserUtils.inlineText(description)
                    .replaceAll("\\\\r",  " ")
                    .replaceAll("\\\\n",  "")
                    .replaceAll("\\\\t",  " "));

            if (article.getLandSurface() == -1) {
                article.setLandSurface(ParserUtils.findLandSurfaceInDescription(description));
            }
            article.setImageUrls(getImageUrls(document));
            article.setExternalReference(document.getElementsByClass("ref").text().replaceAll("Réf: ", ""));

            article.setDetailsParsed(true);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Set<String> getImageUrls(Document article) {
        return article.getElementsByClass("carrousel_image_visu").stream()
                .map(img -> img.attr("src"))
                .collect(Collectors.toSet());
    }

//    private Map<String, String> parseDataModel(Document article) {
//        String scriptText = article.getElementsByTag("script").stream()
//                .filter(script -> script.data().contains("Module contenant les infos de l'annonce"))
//                .findFirst().get()
//                .data();
//        scriptText = ParserUtils.inlineText(scriptText);
//
//        List<Map<String, String>> regexResultList = ParserUtils.matchesByRegexGroup(
//                scriptText, "Object\\.defineProperty\\( ConfigDetail, '(?<key>.*?)', \\{ +value: \"(?<value>.*?)\"", "key", "value");
//
//        return regexResultList.stream()
//                .map(map -> new String[]{
//                        map.get("key"),
//                        map.get("value")
//                                .replaceAll("\\\\r", "")
//                                .replaceAll("\\\\n", " ")
//                                .replaceAll("\\\\", "")
//                })
//                .collect(Collectors.toMap(o -> o[0], o -> o[1], (o1, o2) -> o1));
//    }

    private Map<String, Object> getDetails(String id) throws IOException {
        String json = Jsoup.connect("https://www.seloger.com/detail,json,caracteristique_bien.json?idannonce=" + id).get().text();
        JsonParser parser = new BasicJsonParser();
        return parser.parseMap(json);
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.SELOGER;
    }
}
