package ro.darkindy.autroll.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by andrei.pietrusel on 10/12/2018.
 */
@Configuration
@EnableScheduling
@PropertySource("configuration.properties")
public class RootConfig {

    @Value("${autroll.selenium.chromedriver.location}")
    private String chromeDriverLocation;

    @Value("${autroll.selenium.chromedriver.arguments}")
    private String chromeDriverArguments;

    @Bean
    public WebDriver webDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
        ChromeOptions options = new ChromeOptions();
        options.addArguments(chromeDriverArguments);
        return new ChromeDriver(options);
    }

}
