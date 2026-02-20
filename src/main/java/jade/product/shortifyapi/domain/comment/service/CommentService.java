package jade.product.shortifyapi.domain.comment.service;

import jade.product.shortifyapi.domain.comment.dto.request.CommentCreateRequest;
import jade.product.shortifyapi.domain.comment.dto.response.CommentResponse;
import jade.product.shortifyapi.domain.article.entity.ArticleComment;
import jade.product.shortifyapi.domain.article.repository.ArticleCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleCommentRepository repository;

    @Transactional
    public Long create(Long articleId, CommentCreateRequest request) {

        // depth 1 제한
        if (request.getParentId() != null) {
            ArticleComment parent = repository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));

            if (parent.getParentId() != null) {
                throw new IllegalStateException("대댓글은 1단계까지만 허용됩니다.");
            }
        }

        ArticleComment comment = ArticleComment.create(
                articleId,
                request.getContent(),
                request.getParentId()
        );

        repository.save(comment);
        return comment.getId();
    }

    @Transactional
    public void delete(Long commentId) {

        ArticleComment comment = repository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        comment.softDelete();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long articleId) {

        return repository.findByArticleIdOrderByCreatedAtAsc(articleId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }
}