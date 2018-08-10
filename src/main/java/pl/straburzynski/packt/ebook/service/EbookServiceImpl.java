package pl.straburzynski.packt.ebook.service;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;

@Log
@Service
public class EbookServiceImpl implements EbookService {

    private final String BOOK_TITLE_CLASS = "dotd-title";
    private final String BOOK_DESCRIPTION_1_XPATH = "//div[contains(@class, 'dotd-main-book-summary')]/div[3]/text()";
    private final String BOOK_DESCRIPTION_2_XPATH = "//div[contains(@class, 'dotd-main-book-summary')]/div[4]";
    private final String BOOK_URL_XPATH = "//div[contains(@class, 'dotd-main-book-image')]/a/@href";
    private final String BOOK_IMAGE_URL_XPATH = "//div[contains(@class, 'dotd-main-book-image')]//img/@src";
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

    @Override
    public Ebook getTodayFreeEbookDataFromPackt() {
        try {
            Document doc = Jsoup.connect(applicationConfig.getPacktFreeEbookUrl()).get();
            Ebook ebook = Ebook.builder()
                    .title(doc.getElementsByClass(BOOK_TITLE_CLASS).text())
                    .description(prepareDescription(doc))
                    .bookUrl(applicationConfig.getPacktBaseUrl() + Xsoup.compile(BOOK_URL_XPATH).evaluate(doc).get())
                    .imageUrl(Xsoup.compile(BOOK_IMAGE_URL_XPATH).evaluate(doc).get())
                    .build();
            ValidationMessage validationMessage = Validator.validateEbook(ebook);
            if (validationMessage.isValid()) {
                return ebook;
            } else {
                String messages = validationMessage.getMessages().toString();
                throw new InvalidEbookException(messages);
            }
        } catch (IOException e) {
            throw new CouldNotConnectToPacktWebsiteException("Could not connect to Packt website");
        }
    }

    @Override
    public void claimFreeEbookFromPackt() {
        JBrowserDriver driver = new JBrowserDriver(WebDriverUtils.settings);
        loginToPacktPage(driver);
        log.info("Claiming ebook");
        driver.navigate().to(applicationConfig.getPacktFreeEbookUrl());
        if (WebDriverUtils.isElementPresent(By.id(BOOK_CLAIM_BUTTON_ID), driver)) {
            log.info("Ebook: " + driver.findElement(By.className(BOOK_TITLE_CLASS)).getText());
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

    private String prepareDescription(Document doc) {
        String desc_1 = Xsoup.compile(BOOK_DESCRIPTION_1_XPATH).evaluate(doc).get().trim();
        String desc_2 = Xsoup.compile(BOOK_DESCRIPTION_2_XPATH).evaluate(doc).getElements().text();
        return desc_1 + ((desc_2.isEmpty()) ? "" : "\n" + desc_2);
    }
}
