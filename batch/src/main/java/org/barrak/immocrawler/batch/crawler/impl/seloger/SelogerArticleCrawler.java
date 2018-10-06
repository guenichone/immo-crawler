package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.crawler.database.document.ProviderEnum;
import org.barrak.crawler.database.document.SearchResultDetailsDocument;
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
    public SearchResultDetailsDocument getDetails(SearchResultDocument article) {
        try {
            Document document = Jsoup.connect(article.getUrl())
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                    .get();

            // TODO handle incorrect links

            SearchResultDetailsDocument result = new SearchResultDetailsDocument(article);

            String id = ParserUtils.matchesByRegexGroup(article.getUrl(), "(?<id>[0-9]+).htm", "id").get(0).get("id");
            Map<String, Object> details = getDetails(id);

            String description = (String) details.get("descriptif");
            result.setDescription(ParserUtils.inlineText(description)
                    .replaceAll("\\\\r",  " ")
                    .replaceAll("\\\\n",  "")
                    .replaceAll("\\\\t",  " "));

            if (result.getLandSurface() == -1) {
                result.setLandSurface(ParserUtils.findLandSurfaceInDescription(description));
            }
            result.setImageUrls(getImageUrls(document));

            return result;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
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

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.SELOGER;
    }

    private Map<String, Object> getDetails(String id) throws IOException {
        String json = Jsoup.connect("https://www.seloger.com/detail,json,caracteristique_bien.json?idannonce=" + id).get().text();
        JsonParser parser = new BasicJsonParser();
        return parser.parseMap(json);
    }
}
