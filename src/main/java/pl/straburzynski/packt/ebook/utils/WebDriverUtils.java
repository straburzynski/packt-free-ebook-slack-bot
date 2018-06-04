package pl.straburzynski.packt.ebook.utils;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.machinepublishers.jbrowserdriver.UserAgent;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

@Log
public class WebDriverUtils {

    private static UserAgent userAgent = new UserAgent(UserAgent.Family.WEBKIT, "", "Android", "", "5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.75 Safari/537.36", "Mozilla/5.0 (Linux; U; Android 6.0.1; en-gb; SM-N920G Build/MMB29K) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");

    public static Settings settings = Settings.builder()
            .headless(true)
            .userAgent(userAgent)
            .timezone(Timezone.AMERICA_NEWYORK)
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
