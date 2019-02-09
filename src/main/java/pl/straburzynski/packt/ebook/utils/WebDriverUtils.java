package pl.straburzynski.packt.ebook.utils;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.machinepublishers.jbrowserdriver.UserAgent;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.util.logging.Level;

@Log
public class WebDriverUtils {

    public static Settings settings = Settings.builder()
            .headless(true)
            .timezone(Timezone.AMERICA_NEWYORK)
            .logTrace(false)
            .cache(true)
            .userAgent(UserAgent.CHROME)
            .ignoreDialogs(true)
            .blockAds(true)
            .loggerLevel(Level.OFF)
            .javascript(true)
            .connectTimeout(30000)
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
