package pl.straburzynski.packt.ebook.utils;

import org.junit.Test;
import pl.straburzynski.packt.ebook.model.Ebook;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectConverterTest {

    private Ebook ebook = Ebook.builder()
            .title("Title")
            .bookUrl("http://www.ebook.com")
            .description("Description")
            .imageUrl("https://test.com/123.png").build();

    @Test
    public void asJsonString_ebook() {
        String stringJson = ObjectConverter.asJsonString(ebook);
        assertThat(stringJson).isEqualTo("{\"title\":\"Title\",\"description\":\"Description\",\"imageUrl\":\"https://test.com/123.png\",\"bookUrl\":\"http://www.ebook.com\"}");
    }
}