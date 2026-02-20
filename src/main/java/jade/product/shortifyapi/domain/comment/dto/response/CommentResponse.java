package jade.product.shortifyapi.domain.comment.dto.response;

import jade.product.shortifyapi.domain.article.entity.ArticleComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;
    private Long parentId;
    private String content;
    private boolean deleted;
    private LocalDateTime createdAt;

    public static CommentResponse from(ArticleComment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .parentId(comment.getParentId())
                .content(comment.getContent())
                .deleted(comment.isDeleted())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}