package jade.product.shortifyapi.domain.daily.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleDto {

    private Long id;
    private String title;
    private String media;
    private LocalDateTime publishedAt;
    private LocalDateTime collectedAt;
    private String summary;
    private String originalUrl;
    private int likes;
    private int comments;
}
