package jade.product.shortifyapi.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentCreateRequest {

    private String content;
    private Long parentId; // null이면 일반 댓글

}