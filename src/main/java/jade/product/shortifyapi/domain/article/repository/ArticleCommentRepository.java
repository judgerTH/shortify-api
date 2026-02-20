package jade.product.shortifyapi.domain.article.repository;

import jade.product.shortifyapi.domain.article.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

    List<ArticleComment> findByArticleIdOrderByCreatedAtAsc(Long articleId);

}