package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

//@Component
public class SelogerSeleniumCrawler implements IPagedCrawler {

    @Value("${provider.website.seloger}")
    private String selogerUrl;

    @Value("${webdriver.gecko.driver}")
    private String geckodriverPath;

    private WebDriver driver;

    @Autowired
    private Map<String, SearchResultDocument> cache;


    @Override
    public void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer) {
        open();
    }

    private void open() {
        System.setProperty("webdriver.gecko.driver", geckodriverPath);

        driver = new FirefoxDriver();
        driver.get(selogerUrl);
    }

    private void fillCriteria(SearchCriteria criteria) {
        WebElement cityInput = driver.findElement(By.xpath("//input[@placeholder='Un autre Lieu ?']"));
        cityInput.click();
        cityInput.clear();

        DriverUtils.sendHumanKeys(cityInput, criteria.getCity());

        DriverUtils.sleep(500);

        cityInput.sendKeys("Enter");

        // driver.findElement(By.xpath("//a[@title='Crusnes (FR)']")).click();

        DriverUtils.findByNormalizedText(driver, "15").click();
        DriverUtils.findByNormalizedText(driver, "OK").click();

        driver.findElement(By.linkText("Tout type de bien")).click();
        driver.findElement(By.linkText("Maison")).click();
        driver.findElement(By.linkText("Terrain")).click();

        driver.findElement(By.linkText("Budget")).click();

//        driver.findElement(By.linkText(amountToEuro(criteria.getMinPrice()))).click();
//        driver.findElement(By.linkText(amountToEuro(criteria.getMaxPrice()))).click();
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.SELOGER;
    }
}
