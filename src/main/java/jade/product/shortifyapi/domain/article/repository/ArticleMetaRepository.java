package jade.product.shortifyapi.domain.article.repository;

import jade.product.shortifyapi.domain.article.entity.ArticleMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleMetaRepository extends JpaRepository<ArticleMeta, Long> {

}
