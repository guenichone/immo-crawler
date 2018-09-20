package org.barrak.immocrawler.utils;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverUtils.class);

    private static final int WAIT_TIME = 200;

    public static WebElement waitUntil(WebDriver driver, By by, int times) {
        int counter = times;
        WebElement webElement = null;
            do {
                try {
                    webElement = driver.findElement(by);
                } catch (WebDriverException ex) {
                    sleep(WAIT_TIME);
                    counter--;
                    if (counter < 0) {
                        LOGGER.info("Waited {} * {}ms", times, WAIT_TIME);
                        throw new RuntimeException("Element not found");
                    }
                }
            } while (webElement == null);
        return webElement;
    }

    public static void sendHumanKeys(WebDriver driver, By by, String text) {
        WebElement webElement = driver.findElement(by);
        sendHumanKeys(webElement, text);
    }

    public static void sendHumanKeys(WebElement webElement, String text) {
        for (char c : text.toCharArray()) {
            webElement.sendKeys("" + c);
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static WebElement findByNormalizedText(WebDriver driver, String text) {
        return driver.findElement(By.xpath(".//*[normalize-space(text()) and normalize-space(.)='" + text + "']"));
    }

    public static void scrollDown(WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,2500)", "");
    }

    public static void scrollUp(WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,-2500)", "");
    }

    public static void sleep(int wait) {
        try {
            Thread.sleep(wait);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
