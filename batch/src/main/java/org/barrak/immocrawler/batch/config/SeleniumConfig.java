package org.barrak.immocrawler.batch.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

    @Value("${webdriver.gecko.driver}")
    private String geckodriverPath;

    @Bean
    public WebDriver getDriver() {
        System.setProperty("webdriver.gecko.driver", geckodriverPath);
        return new FirefoxDriver();
    }
}
