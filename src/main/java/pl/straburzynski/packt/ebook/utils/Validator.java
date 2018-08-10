package pl.straburzynski.packt.ebook.utils;

import org.apache.commons.validator.routines.UrlValidator;
import pl.straburzynski.packt.ebook.model.Ebook;
import pl.straburzynski.packt.ebook.model.ValidationMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Validator {

    public static ValidationMessage validateEbook(Ebook ebook) {
        ValidationMessage validationMessage = new ValidationMessage();
        List<String> messages = new ArrayList<>();
        if (ebook == null) {
            validationMessage.setMessages(Collections.singletonList("No ebook found"));
            return validationMessage;
        }
        if (ebook.getTitle().isEmpty()) {
            messages.add("Title is empty");
        }
        if (ebook.getBookUrl().isEmpty() || !isUrlValid(ebook.getBookUrl())) {
            messages.add("Book URL is empty or invalid");
        }
        if (ebook.getImageUrl().isEmpty() || !isUrlValid(ebook.getImageUrl())) {
            messages.add("Image URL is empty or invalid");
        }
        if (ebook.getDescription().isEmpty()) {
            messages.add("Description is empty");
        }
        if (messages.isEmpty()) {
            validationMessage.setValid(true);
        }
        validationMessage.setMessages(messages);
        return validationMessage;
    }

    private static boolean isUrlValid(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
