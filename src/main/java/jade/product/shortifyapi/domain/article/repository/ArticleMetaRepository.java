package jade.product.shortifyapi.domain.article.repository;

import jade.product.shortifyapi.domain.article.entity.ArticleMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleMetaRepository extends JpaRepository<ArticleMeta, Long> {

    List<ArticleMeta> findTop100ByStatusInOrderByPublishedAtDesc(
            List<ArticleMeta.Status> statuses
    );

    List<ArticleMeta> findTop30ByStatusInOrderByPublishedAtDesc(List<ArticleMeta.Status> summaryDone);
}
