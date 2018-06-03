package pl.straburzynski.packt.ebook.utils;

import com.machinepublishers.jbrowserdriver.*;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.util.logging.Level;

@Log
public class WebDriverUtils {

    public static Settings settings = Settings.builder()
            .timezone(Timezone.EUROPE_WARSAW)
            .userAgent(UserAgent.CHROME)
            .requestHeaders(RequestHeaders.CHROME)
            .loggerLevel(Level.OFF)
            .cache(true)
            .blockAds(true)
            .javascript(true)
            .logTrace(false)
            .logWarnings(false)
            .logWire(false)
            .ignoreDialogs(false)
            .build();

    public static boolean isElementPresent(By elementBy, JBrowserDriver driver) {
        try {
            return driver.findElement(elementBy).isDisplayed();
        } catch (NoSuchElementException ex) {
            log.warning("Element not found: " + elementBy.toString());
            return false;
        }
    }

}
