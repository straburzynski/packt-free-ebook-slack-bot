package pl.straburzynski.packt.ebook.model.slack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Message {

    private String username;
    private String icon_emoji;
    private List<Attachment> attachments;

}
