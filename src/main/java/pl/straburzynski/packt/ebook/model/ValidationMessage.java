package pl.straburzynski.packt.ebook.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class ValidationMessage {

    private boolean isValid;
    private List<String> messages;

}
