package jade.product.shortifyapi.domain.article.repository;

import jade.product.shortifyapi.domain.article.entity.ArticleSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleSummaryRepository
        extends JpaRepository<ArticleSummary, Long> {

    @Query("""
    select s
    from ArticleSummary s
    join fetch s.articleMeta m
    where m.publishedAt >= :start
      and m.publishedAt < :end
    order by m.publishedAt desc
""")
    List<ArticleSummary> findTodaySummaries(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );


}
