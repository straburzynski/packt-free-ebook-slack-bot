package pl.straburzynski.packt.ebook.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.straburzynski.packt.ebook.config.ApplicationConfig;
import pl.straburzynski.packt.ebook.exception.ClaimingEbookException;
import pl.straburzynski.packt.ebook.exception.CouldNotConnectToPacktWebsiteException;
import pl.straburzynski.packt.ebook.exception.InvalidEbookException;
import pl.straburzynski.packt.ebook.exception.LoginFailedException;
import pl.straburzynski.packt.ebook.model.Ebook;
import pl.straburzynski.packt.ebook.model.ValidationMessage;
import pl.straburzynski.packt.ebook.utils.Validator;
import pl.straburzynski.packt.ebook.utils.WebDriverUtils;

import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
@Service
public class EbookServiceImpl implements EbookService {

    private final String BOOK_TITLE_XPATH = "//*[@itemprop='name']";
    private final String BOOK_DESC_XPATH = "//*[@itemprop='description']";
    private final String BOOK_IMAGE_URL_XPATH = "//img[@class='product__img']";
    private final String BOOK_URL_XPATH = "//a[@class='product__img-wrapper']";

    private final String BOOK_CLAIM_BUTTON_ID = "free-learning-claim";
    private final String USER_LOGIN_PAGE_URL = "https://www.packtpub.com/login";
    private final String MY_BOOKS_PAGE_URL = "https://www.packtpub.com/account/my-ebooks";
    private final String USER_NAME_XPATH = "//div[@id='left-side-menu-inner']/h1";
    private final String USER_EMAIL_ID = "edit-name";
    private final String USER_PASSWORD_ID = "edit-pass";
    private final String USER_LOGIN_BUTTON_ID = "edit-post-form";

    private final ApplicationConfig applicationConfig;

    @Autowired
    public EbookServiceImpl(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    private static WebClient initializeClient() {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setDoNotTrackEnabled(true);
        webClient.getOptions().setPopupBlockerEnabled(true);
        webClient.getOptions().setTimeout(10000);
        return webClient;
    }

    @Override
    public Ebook getTodayFreeEbookDataFromPackt() {

        WebClient webClient = initializeClient();

        HtmlPage page = null;
        try {
            page = webClient.getPage(applicationConfig.getPacktFreeEbookUrl());
        } catch (IOException e) {
            String message = "Could not connect to main page";
            log.error(message);
            throw new CouldNotConnectToPacktWebsiteException(message);
        }

        waitForJS();

        HtmlImage image = page.getFirstByXPath(BOOK_IMAGE_URL_XPATH);
        String imageUrl = image.getSrcAttribute();

        HtmlAnchor detailsPage = page.getFirstByXPath(BOOK_URL_XPATH);
        String bookUrl;

        try {
            bookUrl = page.getFullyQualifiedUrl(detailsPage.getHrefAttribute()).toString();
        } catch (MalformedURLException e) {
            String message = "Could not format URL for book details page";
            log.error(message);
            throw new CouldNotConnectToPacktWebsiteException(message);
        }

        try {
            page = detailsPage.click();
        } catch (IOException e) {
            String message = "Could not open book details page";
            log.error(message);
            throw new CouldNotConnectToPacktWebsiteException(message);
        }

        HtmlElement titleElement = page.getFirstByXPath(BOOK_TITLE_XPATH);
        String title = titleElement.getTextContent();

        HtmlElement descriptionElement = page.getFirstByXPath(BOOK_DESC_XPATH);
        String description = descriptionElement.getTextContent();

        Ebook ebook = Ebook.builder()
                .title(title)
                .description(description)
                .bookUrl(bookUrl)
                .imageUrl(imageUrl)
                .build();

        ValidationMessage validationMessage = Validator.validateEbook(ebook);
        if (validationMessage.isValid()) {
            return ebook;
        } else {
            String messages = validationMessage.getMessages().toString();
            throw new InvalidEbookException(messages);
        }
    }

    private void waitForJS() {
        // wait for js workaround
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            String message = "Error waiting for JS";
            log.error(message);
            throw new CouldNotConnectToPacktWebsiteException(message);
        }
    }

    @Override
    public void claimFreeEbookFromPackt() {
        JBrowserDriver driver = new JBrowserDriver(WebDriverUtils.settings);
        loginToPacktPage(driver);
        log.info("Claiming ebook");
        driver.navigate().to(applicationConfig.getPacktFreeEbookUrl());
        if (WebDriverUtils.isElementPresent(By.id(BOOK_CLAIM_BUTTON_ID), driver)) {
            log.info("Ebook: " + driver.findElement(By.xpath(BOOK_TITLE_XPATH)).getText());
            driver.findElement(By.id(BOOK_CLAIM_BUTTON_ID)).click();
            if (!driver.getCurrentUrl().equals(MY_BOOKS_PAGE_URL)) {
                throw new ClaimingEbookException("Error claiming ebook, captcha not solved");
            }
            log.info(driver.getCurrentUrl());
        } else {
            throw new ClaimingEbookException("Error claiming ebook, button not found");
        }
        driver.quit();
    }

    @Override
    public String checkLogin() {
        JBrowserDriver driver = new JBrowserDriver(WebDriverUtils.settings);
        loginToPacktPage(driver);
        String name = driver.findElement(By.xpath(USER_NAME_XPATH)).getText();
        log.info("User name: " + name);
        driver.quit();
        return name;
    }

    private void loginToPacktPage(JBrowserDriver driver) {
        log.info("Trying to log in");
        driver.get(USER_LOGIN_PAGE_URL);
        driver.findElementById(USER_EMAIL_ID).sendKeys(applicationConfig.getUserEmail());
        driver.findElementById(USER_PASSWORD_ID).sendKeys(applicationConfig.getUserPassword());
        driver.findElementById(USER_LOGIN_BUTTON_ID).click();
        if (WebDriverUtils.isElementPresent(By.xpath(USER_NAME_XPATH), driver)) {
            log.info("Logged in to account successfully");
        } else {
            throw new LoginFailedException("Login failed");
        }
    }
}
