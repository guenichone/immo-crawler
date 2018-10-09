package org.barrak.immocrawler.batch.crawler.impl.optimhome;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.RealEstateType;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.crawler.impl.JsoupPagedCrawler;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class OptimHomeCrawler extends JsoupPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptimHomeCrawler.class);

    @Value("${provider.website.optimhome}")
    private String optimhomeUrl;

    @Value("${webdriver.gecko.driver}")
    private String geckodriverPath;

    @Autowired
    private Map<String, SearchResultDocument> cache;

    @Autowired
    public OptimHomeCrawler(@Value("${webdriver.gecko.driver}") String geckodriverPath) {
        System.setProperty("webdriver.gecko.driver", geckodriverPath);
    }

    @Override
    protected int getTotal(Document document) {
        return (int) ParserUtils.getNumericOnly(document.getElementById("se-results-count").text());
    }

    @Override
    protected Elements getArticles(Document document) {
        return document.getElementsByClass("grid-view").first().getElementsByTag("article");
    }

    @Override
    protected Document getDocumentPage(SearchCriteria criteria, int pageNumber) {
        String url = buildSearchUrl(criteria, pageNumber);

        WebDriver driver = new FirefoxDriver();
        driver.get(url);

        try {
            driver.findElement(By.linkText(String.valueOf(pageNumber))).click();
        } catch (Exception ex) {
            WebElement page;
            while ((page = driver.findElement(By.linkText(String.valueOf(pageNumber)))) == null) {
                driver.findElement(By.linkText(">>")).click();
            }
            page.click();
        }

        Document document = Jsoup.parse(driver.getPageSource());

        driver.close();

        return document;
    }

    @Override
    protected SearchResultDocument parseArticle(SearchCriteria criteria, Element article) {
        if (!article.classNames().contains("alerte_mail")) { // One article is a publicity
            String href = article.getElementsByTag("a").attr("href");
            String city = article.getElementsByClass("property_city").text();
            city = city.substring(0, city.length() - 5); // 'City (54)'
            int price = (int) ParserUtils.getNumericOnly(article.getElementsByClass("property_price").text()) * 1000;

            if (cache.containsKey(href)) {
                SearchResultDocument oldSearchResult = cache.get(href);
                if (oldSearchResult.getPrice() != price) {
                    LOGGER.info("New price for {}, previous {}, new {}", href, oldSearchResult.getPrice(), price);
                } else {
                    return null;
                }
            } else {
                LOGGER.info("Add new result {}", href);
            }

            SearchResultDocument result = new SearchResultDocument(href, ProviderEnum.OPTIMHOME, RealEstateType.HOUSE, city, price);

            String img = article.getElementsByTag("img").get(1).attr("src");
            result.setImageUrl(img);
            result.setNbRooms((int) ParserUtils.getNumericOnly(article.getElementsByClass("nb_piece").text()));
            result.setHomeSurface((int) ParserUtils.getNumericOnly(article.getElementsByClass("nb_surface").text()));

            return result;
        } else {
            return null;
        }
    }

    @Override
    protected String buildSearchUrl(SearchCriteria criteria, int page) {
        // https://www.optimhome.com/annonces/achat?affichage=grid-view
        // &adresse=Crusnes%2C%20France
        // &rayon=20000
        // &geo=49.434418%2C5.921767000000045
        // &type=HOUSE
        // &budget=%3A500000

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(optimhomeUrl + "/annonces/achat?affichage=grid-view")
                .queryParam("adresse", "Crusnes, France") // TODO Use geo service
                .queryParam("budget",  getPriceOrEmpty(criteria.getMinPrice()) + ":" + getPriceOrEmpty(criteria.getMaxPrice()))
                .queryParam("rayon", criteria.getAround() * 1000)
                .queryParam("geo", "49.434418%2C5.921767000000045") // TODO Use geo service
                .queryParam("type", "HOUSE");
        //        .queryParam("page", page); Direct page parameter is not used, JS triggered by link at the bottom do the change

        return builder.toUriString();
    }

    private String getPriceOrEmpty(int price) {
        return price > 0 ? String.valueOf(price) : "";
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.OPTIMHOME;
    }
}
