package org.example.parsing;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.CarBrand;
import org.example.model.CarModel;
import org.example.repository.CarBrandRepository;
import org.example.repository.CarModelRepository;
import org.example.util.RandomTimeSleep;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class BrandModelParser {

    private static final String parseBrandsUrl = "https://www.avito.ru/web/2/suggest/desktop?" +
            "categoryId=9&" +
            "locationId=650400&" +
            "searchRadius=200&" +
            "correctorMode=0&" +
            "page=1&" +
            "verticalCategoryId=0&" +
            "rootCategoryId=1&" +
            "localPriority=0&" +
            "disabledFilters%5Bids%5D%5B0%5D=byTitle&" +
            "disabledFilters%5Bslugs%5D%5B0%5D=bt&" +
            "paramId=110000&" +
            "query=&" +
            "isRedesign=true&" +
            "limit=500&" +
            "offset=0";

    private static final String parseModelsUrl = "https://www.avito.ru/search/filters/list?_=&" +
            "categoryId=9&" +
            "locationId=650400&" +
            "searchRadius=200&" +
            "correctorMode=1&" +
            "page=1&" +
            "verticalCategoryId=0&" +
            "rootCategoryId=1&" +
            "localPriority=0&" +
            "disabledFilters%5Bids%5D%5B0%5D=byTitle&" +
            "disabledFilters%5Bslugs%5D%5B0%5D=bt&" +
            "currentPage=&" +
            "filtersGroup=desktop_catalog_filters&";

    private final CarBrandRepository carBrandRepository;

    private final CarModelRepository carModelRepository;

    @Transactional
    public void parseBrandsModels() {

        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled"); // Убираем флаг Automation
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        chromeOptions.addArguments("--disable-infobars"); // Убираем "Chrome is being controlled by automated software"
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--remote-allow-origins=*");

        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
//        DevTools devTools = driver.getDevTools();
//        devTools.createSession();
//
//        // Отключаем navigator.webdriver
//        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        List<CarBrand> brands = new ArrayList<>();
        try {
            driver.get(parseBrandsUrl);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            String jsonResponse = (String) js.executeScript("return window.document.body.innerText");

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray sections = jsonObject.getJSONObject("result").getJSONArray("sections");

            for (int i = 0; i < sections.length(); i++) {
                JSONObject section = sections.getJSONObject(i);
                if (section.getString("label").equals("Все")) {
                    JSONArray options = section.getJSONArray("options");
                    for (int j = 0; j < options.length(); j++) {
                        JSONObject brandJson = options.getJSONObject(j);
                        String title = brandJson.getString("title");
                        String id = String.valueOf(brandJson.getInt("id"));

                        CarBrand newCarBrand = CarBrand.builder()
                                .value(id)
                                .name(title)
                                .build();
                        brands.add(carBrandRepository.save(newCarBrand));
                    }
                }
            }
        } catch (Exception e) {
            log.error("error during parsing brands", e);
        } finally {
            driver.quit();
        }

        for (CarBrand brand : brands) {
            parseModels(brand);
        }
    }

    @Transactional
    public void parseModels(CarBrand carBrand) {
        RandomTimeSleep.randomSleep();
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled"); // Убираем флаг Automation
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        chromeOptions.addArguments("--disable-infobars"); // Убираем "Chrome is being controlled by automated software"
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--remote-allow-origins=*");

        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
//        DevTools devTools = driver.getDevTools();
//        devTools.createSession();
//
//        // Отключаем navigator.webdriver
//        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        List<String> models = new ArrayList<>();
        try {
            String modelsParseUrl = parseModelsUrl + "params%5B110000%5D=" + carBrand.getValue();
            driver.get(modelsParseUrl);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            String jsonResponse = (String) js.executeScript("return window.document.body.innerText");

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray values = jsonObject.getJSONObject("result").getJSONArray("main").getJSONObject(3).getJSONArray("values");

            for (int i = 0; i < values.length(); i++) {
                JSONObject section = values.getJSONObject(i);
                if (section.getString("title").equals("Все")) {
                    JSONArray options = section.getJSONArray("options");
                    for (int j = 0; j < options.length(); j++) {
                        JSONObject modelJson = options.getJSONObject(j);
                        String title = modelJson.getString("name");
                        String id = modelJson.getString("value");
                        models.add("name: " + title + " id: " + id);

                        CarModel newCarBrand = CarModel.builder()
                                .value(id)
                                .name(title)
                                .brand(carBrand)
                                .build();
                        carModelRepository.save(newCarBrand);
                    }
                }
            }
        } catch (Exception e) {
            log.error("error during parsing models for brand: {}", carBrand.getName(), e);
        } finally {
            driver.quit();
        }
    }
}
