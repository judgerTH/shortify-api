package jade.product.shortifyapi.domain.article.repository;

import jade.product.shortifyapi.domain.article.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

    List<ArticleComment> findByArticleIdOrderByCreatedAtAsc(Long articleId);

    @Query("""
        select ac.articleId, count(ac)
        from ArticleComment ac
        where ac.articleId in :ids
          and ac.deleted = false
        group by ac.articleId
    """)
    List<Object[]> countGroupedByArticleIds(@Param("ids") List<Long> ids);
}