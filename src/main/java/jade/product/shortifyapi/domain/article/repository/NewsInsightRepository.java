package jade.product.shortifyapi.domain.article.repository;

import jade.product.shortifyapi.domain.article.entity.NewsInsight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsInsightRepository extends JpaRepository<NewsInsight, Long> {
    List<NewsInsight> findTop2ByOrderByCreatedAtDesc();
}
