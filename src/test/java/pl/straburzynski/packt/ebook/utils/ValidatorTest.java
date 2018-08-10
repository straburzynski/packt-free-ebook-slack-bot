package pl.straburzynski.packt.ebook.utils;

import org.junit.Test;
import pl.straburzynski.packt.ebook.model.Ebook;
import pl.straburzynski.packt.ebook.model.ValidationMessage;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidatorTest {

    private Ebook validEbook = Ebook.builder()
            .title("Test title")
            .bookUrl("http://www.packtpub.com/application-development/test-ebook")
            .description("Test description")
            .imageUrl("https://test.com/123.png").build();

    private Ebook invalidEbook = Ebook.builder()
            .title("")
            .bookUrl("")
            .description("")
            .imageUrl("").build();

    private Ebook nullEbook = null;

    @Test
    public void validateEbook_validEbook() {
        ValidationMessage validationMessage = Validator.validateEbook(validEbook);
        assertThat(validationMessage.isValid()).isTrue();
        assertThat(validationMessage.getMessages()).isNullOrEmpty();
    }

    @Test
    public void validateEbook_invalidEbook() {
        ValidationMessage validationMessage = Validator.validateEbook(invalidEbook);
        assertThat(validationMessage.isValid()).isFalse();
        assertThat(validationMessage.getMessages()).hasSize(4);
        assertThat(validationMessage.getMessages()).contains("Title is empty");
        assertThat(validationMessage.getMessages()).contains("Book URL is empty or invalid");
        assertThat(validationMessage.getMessages()).contains("Image URL is empty or invalid");
        assertThat(validationMessage.getMessages()).contains("Description is empty");
    }

    @Test
    public void validateEbook_nullEbook() {
        ValidationMessage validationMessage = Validator.validateEbook(nullEbook);
        assertThat(validationMessage.isValid()).isFalse();
        assertThat(validationMessage.getMessages()).hasSize(1);
        assertThat(validationMessage.getMessages()).contains("No ebook found");
    }

}
