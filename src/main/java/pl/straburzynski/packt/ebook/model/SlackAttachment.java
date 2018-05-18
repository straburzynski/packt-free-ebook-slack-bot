package pl.straburzynski.packt.ebook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SlackAttachment {

    private String title;
    private String title_link;
    private String color;
    private String text;
    private String image_url;
    private String thumb_url;
    private List<SlackAction> actions;

}
