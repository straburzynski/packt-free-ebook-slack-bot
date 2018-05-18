package pl.straburzynski.packt.ebook.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class Ebook {

    private String title;
    private String description;
    private String imageUrl;
    private String bookUrl;

}
