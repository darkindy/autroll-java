package ro.darkindy.autroll.task;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.darkindy.autroll.bot.MessageProvider;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by andrei.pietrusel on 10/11/2018.
 */
@Component
public class TrollingTask {

    private static final Logger logger = LoggerFactory.getLogger(TrollingTask.class);

    @Value("${autroll.facebook.target.url}")
    private String fbTargetUrl;

    @Value("${autroll.facebook.source.email}")
    private String fbSourceEmail;

    @Value("${autroll.facebook.source.pass}")
    private String fbSourcePass;

    @Autowired
    private WebDriver webDriver;
    private JavascriptExecutor javascriptExecutor;

    @Autowired
    private MessageProvider messageProvider;

    @PostConstruct
    public void init() {
        javascriptExecutor = (JavascriptExecutor) webDriver;
        logInToFacebook();
        logger.info("Trolling initiated.");
    }

    @Scheduled(fixedRateString = "${autroll.task.recurrence}")
    public void performTrolling() throws InterruptedException {
        logger.info("Trolling iteration.");
        List<WebElement> postContainers = obtainPostContainers();
        if (postContainers.size() == 0) {
            logger.error("user content container not found");
            return;
        }

        WebElement postContainer = postContainers.get(0);
        if (checkIfAlreadyCommented(postContainer)) {
            logger.info("Already commented. Exiting.");
            return;
        }
        logger.info("Did not comment before. Continuing.");

        String postText = extractPostText(postContainer);
        if(postText == null) {
            logger.info("Post has no text. Exiting.");
            return;
        }
        String commentText = messageProvider.computeMessage(postText);
        postComment(postContainer, commentText);
        logger.info("Ended trolling iteration.");
    }

    private void postComment(WebElement postContainer, String commentText) throws InterruptedException {
        WebElement commentBox = postContainer.findElement(By.className("UFIAddCommentInput"));
        javascriptExecutor.executeScript("arguments[0].click();", commentBox);
        Thread.sleep(1500);
        WebElement currentElement = webDriver.switchTo().activeElement();
        currentElement.sendKeys(commentText);
        currentElement.sendKeys(Keys.RETURN);
    }

    private String extractPostText(WebElement postContainer) {
        List<WebElement> postParagraphs = postContainer.findElements(By.cssSelector("p"));
        return postParagraphs.stream().findFirst()
                            .map(WebElement::getText)
                            .orElse(null);
    }

    private List<WebElement> obtainPostContainers() throws InterruptedException {
        webDriver.get(fbTargetUrl);
        Thread.sleep(1500);
        return webDriver.findElements(By.className("userContentWrapper"));
    }

    private boolean checkIfAlreadyCommented(WebElement postContainer) {
        List<WebElement> ufiCommentActorAndBody = postContainer.findElements(By.className("UFICommentActorAndBody"));
        if (ufiCommentActorAndBody.size() > 0) {
            return true;
        }
        return false;
    }

    private void logInToFacebook() {
        webDriver.get("https://www.facebook.com");
        WebElement element = webDriver.findElement(By.xpath("//*[@id=\"email\"]"));
        element.sendKeys(fbSourceEmail);
        element = webDriver.findElement(By.xpath("//*[@id=\"pass\"]"));
        element.sendKeys(fbSourcePass);
        element = webDriver.findElement(By.xpath("//input[@value=\"Log In\"]"));
        javascriptExecutor.executeScript("arguments[0].click();", element);
    }

}
