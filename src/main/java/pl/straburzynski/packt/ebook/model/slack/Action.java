package pl.straburzynski.packt.ebook.model.slack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Action {

    private String type;
    private String text;
    private String url;
    private String style;

}
