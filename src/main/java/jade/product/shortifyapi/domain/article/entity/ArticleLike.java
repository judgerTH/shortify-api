package jade.product.shortifyapi.domain.article.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "article_like",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"articleId", "clientId"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long articleId;

    @Column(nullable = false)
    private String clientId;

    private LocalDateTime createdAt;

    public static ArticleLike create(Long articleId, String clientId) {
        ArticleLike like = new ArticleLike();
        like.articleId = articleId;
        like.clientId = clientId;
        like.createdAt = LocalDateTime.now();
        return like;
    }
}