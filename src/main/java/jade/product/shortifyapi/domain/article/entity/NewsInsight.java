package jade.product.shortifyapi.domain.article.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "news_insight")
public class NewsInsight {
    @Id
    private Long id;

    private Integer positivity;
    private Integer stability;
    private Integer tension;
    private String summary;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
