package jade.product.shortifyapi.domain.article.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "article_summary")
public class ArticleSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_meta_id")
    private ArticleMeta articleMeta;

    @Column(columnDefinition = "text")
    private String summaryContent;

    private String summaryTitle;
}
