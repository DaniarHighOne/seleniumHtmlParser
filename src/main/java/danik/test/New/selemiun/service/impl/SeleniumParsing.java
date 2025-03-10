package danik.test.New.selemiun.service.impl;


import danik.test.New.selemiun.service.ParsingUnit;
import danik.test.New.selemiun.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class SeleniumParsing implements ParsingUnit {


    @Override
    public String parseHtml(String url, int statusCode) {

        List<String> userAgentsList = Arrays.asList(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:124.0) Gecko/20100101 Firefox/124.0",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 Edg/123.0.2420.81",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 OPR/109.0.0.0",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 14.4; rv:124.0) Gecko/20100101 Firefox/124.0",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 14_4_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4.1 Safari/605.1.15",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 14_4_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 OPR/109.0.0.0",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
                "Mozilla/5.0 (X11; Linux i686; rv:124.0) Gecko/20100101 Firefox/124.0"
        );

        Random randomChoose = new Random();
        String selectedUserAgent = userAgentsList.get(randomChoose.nextInt(userAgentsList.size()));
        log.info("Try to parse the page using Selenium driver!!");
        log.info("Select this user agent: {}", selectedUserAgent);

        FirefoxProfile profile = new FirefoxProfile();
        FirefoxOptions options = new FirefoxOptions();
        profile.setPreference("javascriptEnabled", true);
        profile.setPreference("general.useragent.override", selectedUserAgent);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("-headless");
        options.setProfile(profile);
        log.info("Firefox profile: {}, start to activate the driver", profile);

        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));

        String htmlContent = "";
        Path outputPath = Paths.get("htmlPageWithSelenium.html");


        try {
            driver.get(url);
            log.info("Successfully accessed url: {}", url);

            //getting the page source
            htmlContent = driver.getPageSource();
            log.info("Fetched html content: {}", htmlContent);

            //html to file for testing
            Files.writeString(outputPath, htmlContent, StandardCharsets.UTF_8);
            log.info("Html content saved to path: {}", outputPath.toAbsolutePath());

        } catch (IOException ex) {
            log.error("Failed to save html content: {}", outputPath.toAbsolutePath(), ex);
        } catch (Exception ignored) {
            log.error("Failed to parse html content:", ignored);
        }

        finally {
            driver.quit();
            log.info("Driver quit");
        }

        return htmlContent;
    }

    @Override
    public String parseHtmlWithProxy(String url, int statusCode, ProxyService proxyservice) {
        return "";
    }

}
