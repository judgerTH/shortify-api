package jade.product.shortifyapi.domain.article.repository;

import jade.product.shortifyapi.domain.article.entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    boolean existsByArticleIdAndClientId(Long articleId, String clientId);

    long countByArticleId(Long articleId);

    void deleteByArticleIdAndClientId(Long articleId, String clientId);

    @Query("""
    select al.articleId, count(al)
    from ArticleLike al
    where al.articleId in :ids
        group by al.articleId
    """)
    List<Object[]> countGroupedByArticleIds(@Param("ids") List<Long> ids);
}