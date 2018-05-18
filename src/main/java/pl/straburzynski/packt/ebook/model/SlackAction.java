package pl.straburzynski.packt.ebook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SlackAction {

    private String type;
    private String text;
    private String url;
    private String style;

}
