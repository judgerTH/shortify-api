package jade.product.shortifyapi.domain.article.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_meta")
@Getter
public class ArticleMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "press")
    private String press;

    @Column(name = "url")
    private String url;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "collected_at")
    private LocalDateTime collectedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public enum Status {
        COLLECTED,
        SUMMARY_DONE,
        SUMMARY_FAILED
    }
}

