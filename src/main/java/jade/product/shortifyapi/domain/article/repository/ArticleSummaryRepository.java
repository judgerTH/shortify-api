package jade.product.shortifyapi.domain.article.repository;

import jade.product.shortifyapi.domain.article.entity.ArticleSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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

    @Query("""
        SELECT s
        FROM ArticleSummary s
        JOIN s.articleMeta m
        WHERE m.press IN :presses
          AND m.publishedAt BETWEEN :start AND :end
        ORDER BY m.publishedAt DESC
    """)
    List<ArticleSummary> findByPressTimeline(
            @Param("presses") List<String> presses,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    @Query("""
        select s
        from ArticleSummary s
        join s.articleMeta m
        where m.publishedAt >= :start
          and m.publishedAt <  :end
        order by m.publishedAt desc
    """)
    List<ArticleSummary> findByPublishedRange(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    @Query("""
        select s
        from ArticleSummary s
        join s.articleMeta m
        where m.press in :presses
          and m.publishedAt between :start and :end
        order by m.publishedAt desc
    """)
    List<ArticleSummary> findByPublishedRangeAndPress(
            @Param("presses") List<String> presses,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    /**
     * 특정 시간 슬롯 내 기사들이 모두 SUMMARY_DONE 상태인지 확인
     *
     * @return true  -> 전부 요약 완료
     *         false -> 아직 요약 안 된 기사 존재
     */
    @Query("""
        select count(m) = 0
        from ArticleMeta m
        where m.publishedAt >= :start
          and m.publishedAt < :end
          and m.status <> jade.product.shortifyapi.domain.article.entity.ArticleMeta.Status.SUMMARY_DONE
    """)
    boolean isAllSummarized(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
