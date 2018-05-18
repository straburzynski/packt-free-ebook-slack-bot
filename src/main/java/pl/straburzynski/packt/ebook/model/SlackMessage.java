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
public class SlackMessage {

    private String username;
    private String icon_emoji;
    private List<SlackAttachment> attachments;

}
