package pl.straburzynski.packt.ebook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ClaimingEbookException extends RuntimeException {
    public ClaimingEbookException(String message) {
        super(message);
    }
}
