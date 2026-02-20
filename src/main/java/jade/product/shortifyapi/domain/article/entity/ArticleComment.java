package jade.product.shortifyapi.domain.article.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long articleId;

    @Column
    private Long parentId;  // 대댓글용

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private boolean deleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ArticleComment create(
            Long articleId,
            String content,
            Long parentId
    ) {
        ArticleComment comment = new ArticleComment();
        comment.articleId = articleId;
        comment.content = content;
        comment.parentId = parentId;
        comment.deleted = false;
        comment.createdAt = LocalDateTime.now();
        return comment;
    }

    public void softDelete() {
        this.deleted = true;
        this.content = "삭제된 댓글입니다.";
    }
}