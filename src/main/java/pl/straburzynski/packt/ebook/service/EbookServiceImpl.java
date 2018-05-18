package pl.straburzynski.packt.ebook.service;

import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.straburzynski.packt.ebook.config.ApplicationConfig;
import pl.straburzynski.packt.ebook.exception.CouldNotConnectToPacktWebsiteException;
import pl.straburzynski.packt.ebook.model.Ebook;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;

@Log
@Service
public class EbookServiceImpl implements EbookService {

    private final String BOOK_TITLE_CLASS = "dotd-title";
    private final String BOOK_DESCRIPTION_1_XPATH = "//div[@id='deal-of-the-day']//div[contains(@class, 'dotd-main-book-summary')]/div[3]/text()";
    private final String BOOK_DESCRIPTION_2_XPATH = "//div[@id='deal-of-the-day']//div[contains(@class, 'dotd-main-book-summary')]/div[4]";
    private final String BOOK_URL_XPATH = "//div[@id='deal-of-the-day']//div[contains(@class, 'dotd-main-book-image')]/a/@href";
    private final String BOOK_IMAGE_URL_XPATH = "//div[@id='deal-of-the-day']//div[contains(@class, 'dotd-main-book-image')]//img/@src";
    private final String HTTP_PREFIX = "http:";

    private final ApplicationConfig applicationConfig;

    @Autowired
    public EbookServiceImpl(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Override
    public Ebook getTodayFreeEbookDataFromPackt() {
        try {
            Document doc = Jsoup.connect(applicationConfig.getPacktFreeEbookUrl()).get();
            return Ebook.builder()
                    .title(doc.getElementsByClass(BOOK_TITLE_CLASS).text())
                    .description(prepareDescription(doc))
                    .bookUrl(applicationConfig.getPacktBaseUrl() + Xsoup.compile(BOOK_URL_XPATH).evaluate(doc).get())
                    .imageUrl(HTTP_PREFIX + Xsoup.compile(BOOK_IMAGE_URL_XPATH).evaluate(doc).get())
                    .build();
        } catch (IOException e) {
            throw new CouldNotConnectToPacktWebsiteException("Could not connect to Packt website");
        }
    }

    private String prepareDescription(Document doc) {
        String desc_1 = Xsoup.compile(BOOK_DESCRIPTION_1_XPATH).evaluate(doc).get().trim();
        String desc_2 = Xsoup.compile(BOOK_DESCRIPTION_2_XPATH).evaluate(doc).getElements().text();
        return desc_1 + ((desc_2.isEmpty()) ? "" : "\n" + desc_2);
    }
}
