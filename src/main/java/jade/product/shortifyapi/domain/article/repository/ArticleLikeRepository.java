package jade.product.shortifyapi.domain.article.repository;

import jade.product.shortifyapi.domain.article.entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    boolean existsByArticleIdAndClientId(Long articleId, String clientId);

    long countByArticleId(Long articleId);

    void deleteByArticleIdAndClientId(Long articleId, String clientId);
}