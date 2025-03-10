package danik.test.New.selemiun;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class NewSelemiunApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewSelemiunApplication.class, args);

		//System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");
		System.setProperty(FirefoxDriverLogLevel.INFO.toString(), FirefoxDriverLogLevel.INFO.toString());
	}
}
