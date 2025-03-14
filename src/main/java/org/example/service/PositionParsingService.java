package org.example.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.ParsingPricesException;
import org.example.model.CarPosition;
import org.example.model.PositionPrice;
import org.example.repository.PositionPriceRepository;
import org.example.util.RandomTimeSleep;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionParsingService {

    private final PositionPriceRepository positionPriceRepository;

    private final String getUrlAndCountUrl = "https://www.avito.ru/web/1/js/items?" +
            "_=&" +
            "categoryId=9&" +
            "locationId=621540&" +
            "cd=1&" +
            "p=1&" +
            "verticalCategoryId=0&" +
            "rootCategoryId=1&" +
            "disabledFilters%5Bids%5D%5B0%5D=byTitle&" +
            "disabledFilters%5Bslugs%5D%5B0%5D=bt&" +
            "countOnly=1&";

    public void parseCarPosition(CarPosition carPosition) {
        String apiUrl = getUrlByParams(carPosition);

        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--remote-allow-origins=*");

        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

        List<Integer> prices = new ArrayList<>();
        try {
            driver.get(apiUrl);
            RandomTimeSleep.randomSleep();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String jsonResponse = (String) js.executeScript("return window.document.body.innerText");

            JSONObject jsonObjectResult = new JSONObject(jsonResponse);

            String getProductsUrl = "https://www.avito.ru" + jsonObjectResult.getString("url");
            System.out.println("url: " + getProductsUrl);
            int productsCount = jsonObjectResult.getInt("count");
            System.out.println("найдено объявлений: " + productsCount);

            int pageCount = productsCount / 50 + 1;
            for (int i = 1; i <= pageCount; i++) {
                driver.get(getProductsUrl + "&p=" + i);
                RandomTimeSleep.randomSleep();
                List<WebElement> ads = driver.findElements(By.xpath("//div[@data-marker='item']"));
                for (int j = 0; j < ads.size(); j++) {
                    try {
                        WebElement titleElement = ads.get(j).findElement(By.xpath(".//h3"));
                        String title = titleElement.getText();

                        WebElement priceElement = ads.get(j).findElement(By.xpath(".//meta[@itemprop='price']"));
                        String price = priceElement.getAttribute("content");
                        prices.add(Integer.parseInt(price));
                        System.out.println(i + " " + j + title + " - " + price);
                    } catch (Exception e) {
                        log.error("error with parsing ad carPosition: {}", carPosition.getId(), e);
                    }
                }
            }
        } catch (Exception e) {
            log.info("error with parsing ads carPosition: {}", carPosition.getId(), e);
            throw new ParsingPricesException("ошибка при обработке объявлений");
        } finally {
            driver.quit();
        }

        int medianPrice = findMedian(prices);
        PositionPrice positionPrice = PositionPrice.builder()
                .date(LocalDate.now())
                .price(medianPrice)
                .position(carPosition)
                .build();
        positionPriceRepository.save(positionPrice);
    }

    public static int findMedian(List<Integer> prices) {
        Collections.sort(prices);
        int size = prices.size();

        if (size == 0) {
            return 0;
        }

        if (size % 2 == 1) {
            return prices.get(size / 2);
        } else {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2;
        }
    }

    private String getUrlByParams(CarPosition carPosition) {
        String apiUrl = getUrlAndCountUrl +
                "params%5B110000%5D=" + carPosition.getBrand().getValue() + "&" +
                "params%5B110001%5D=" + carPosition.getModel().getValue() + "&";

        if (carPosition.getYearFrom() != null) {
            apiUrl = apiUrl + "params%5B164669%5D%5Bfrom%5D=" + carPosition.getYearFrom() + "&";
        }
        if (carPosition.getYearBefore() != null) {
            apiUrl = apiUrl + "params%5B164669%5D%5Bto%5D=" + carPosition.getYearBefore() + "&";
        }
        if (carPosition.getMileageFrom() != null) {
            apiUrl = apiUrl + "params%5B2687%5D%5Bfrom%5D=" + carPosition.getMileageFrom() + "&";
        }
        if (carPosition.getMileageBefore() != null) {
            apiUrl = apiUrl + "params%5B2687%5D%5Bto%5D=" + carPosition.getMileageBefore() + "&";
        }
        return apiUrl;
    }
}
