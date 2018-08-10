package pl.straburzynski.packt.ebook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidEbookException extends RuntimeException {
    public InvalidEbookException(String message) {
        super(message);
    }
}
