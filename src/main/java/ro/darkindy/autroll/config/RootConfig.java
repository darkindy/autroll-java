package ro.darkindy.autroll.config;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.UnexpectedAlertBehaviour;
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

    @Value("${autroll.selenium.chromedriver.exe:#{null}}")
    private String chromeDriverExecutable;

    @Value("${autroll.selenium.chromedriver.args:#{null}}")
    private String chromeDriverArguments;

    @Value("${autroll.chrome.bin:#{null}}")
    private String chromeBinaryLocation;

    @Bean
    public WebDriver webDriver() {
        if (System.getProperty("webdriver.chrome.driver") == null
                && StringUtils.isNotEmpty(chromeDriverExecutable)) {
            System.setProperty("webdriver.chrome.driver", chromeDriverExecutable);
        }
        ChromeOptions options = new ChromeOptions();
        if (StringUtils.isNotEmpty(chromeDriverArguments)) {
            options.addArguments(chromeDriverArguments);
        }
        if (StringUtils.isNotEmpty(chromeBinaryLocation)) {
            options.setBinary(chromeBinaryLocation);
        }
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        return new ChromeDriver(options);
    }

}
