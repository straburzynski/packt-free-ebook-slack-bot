package pl.straburzynski.packt.ebook.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.straburzynski.packt.ebook.model.Ebook;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EbookServiceImplTest {

    @Autowired
    private EbookService ebookService;

    @Test
    public void getTodayFreeEbookDataFromPackt() {
        Ebook ebook = ebookService.getTodayFreeEbookDataFromPackt();
        assertThat(ebook).isNotNull();
    }

    @Test
    public void claimFreeEbookFromPackt() {
//        can't test because of captcha on website
    }

    @Test
    public void checkLogin_validCredentials() {
//        String name = ebookService.checkLogin();
//        assertThat(name).isNotEmpty();
    }
}