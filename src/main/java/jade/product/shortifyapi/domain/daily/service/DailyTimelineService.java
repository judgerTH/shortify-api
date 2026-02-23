package jade.product.shortifyapi.domain.daily.service;

import jade.product.shortifyapi.domain.article.entity.ArticleMeta;
import jade.product.shortifyapi.domain.article.entity.ArticleSummary;
import jade.product.shortifyapi.domain.article.repository.ArticleCommentRepository;
import jade.product.shortifyapi.domain.article.repository.ArticleLikeRepository;
import jade.product.shortifyapi.domain.article.repository.ArticleSummaryRepository;
import jade.product.shortifyapi.domain.daily.dto.response.ArticleDto;
import jade.product.shortifyapi.domain.daily.dto.response.DailyTimelineResponse;
import jade.product.shortifyapi.domain.daily.dto.response.TimeGroupDto;
import jade.product.shortifyapi.global.error.ErrorCode;
import jade.product.shortifyapi.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyTimelineService {

    private final ArticleSummaryRepository articleSummaryRepository;
    private final ArticleLikeRepository  articleLikeRepository;
    private final ArticleCommentRepository articleCommentRepository;

    // 초기 조회 시 가져올 최대 기사 수
    private static final int INITIAL_SIZE = 100;

    // 타임라인 그룹 단위 (2시간)
    private static final int SLOT_HOURS = 2;

    /**
     * 초기 타임라인 조회
     * - 오늘 날짜 기준
     * - 최신 기사 최대 100건
     * - 발행 시간 기준 2시간 단위로 그룹핑
     */
    public DailyTimelineResponse getInitialTimeline() {

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<ArticleSummary> summaries =
                articleSummaryRepository.findTodaySummaries(
                        start,
                        end,
                        PageRequest.of(0, INITIAL_SIZE)
                );

        List<ArticleDto> articles = toArticleDtos(summaries);

        List<TimeGroupDto> groups = groupByFixedTimeline(articles);

        return new DailyTimelineResponse(
                today,
                articles.isEmpty()
                        ? LocalTime.now()
                        : articles.get(0).getPublishedAt().toLocalTime(),
                groups
        );
    }

    /**
     * ArticleSummary → ArticleDto 변환
     */
    private List<ArticleDto> toArticleDtos(List<ArticleSummary> summaries) {

        if (summaries == null || summaries.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> articleIds = summaries.stream()
                .map(s -> s.getArticleMeta().getId())
                .toList();

        Map<Long, Long> likeMap =
                articleLikeRepository.countGroupedByArticleIds(articleIds)
                        .stream()
                        .collect(Collectors.toMap(
                                r -> (Long) r[0],
                                r -> (Long) r[1]
                        ));

        Map<Long, Long> commentMap =
                articleCommentRepository.countGroupedByArticleIds(articleIds)
                        .stream()
                        .collect(Collectors.toMap(
                                r -> (Long) r[0],
                                r -> (Long) r[1]
                        ));

        return summaries.stream()
                .map(summary -> {

                    ArticleMeta meta = summary.getArticleMeta();
                    Long articleId = meta.getId();

                    long likeCount = likeMap.getOrDefault(articleId, 0L);
                    long commentCount = commentMap.getOrDefault(articleId, 0L);

                    return new ArticleDto(
                            articleId,
                            summary.getSummaryTitle(),
                            meta.getPress(),
                            meta.getPublishedAt(),
                            meta.getCollectedAt(),
                            summary.getSummaryContent(),
                            meta.getUrl(),
                            (int) likeCount,
                            (int) commentCount
                    );
                })
                .toList();
    }

    /**
     * 발행 시간 기준으로 2시간 고정 슬롯 타임라인 그룹핑
     * 예) 10:00~11:59, 12:00~13:59
     */
    private List<TimeGroupDto> groupByFixedTimeline(List<ArticleDto> articles) {

        Map<Integer, List<ArticleDto>> grouped =
                articles.stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getPublishedAt().getHour() / SLOT_HOURS
                        ));

        return grouped.entrySet().stream()
                // 최신 시간대 먼저
                .sorted(Map.Entry.<Integer, List<ArticleDto>>comparingByKey().reversed())
                .map(e -> toFixedTimeGroup(e.getKey(), e.getValue()))
                .toList();
    }

    /**
     * 슬롯 인덱스를 실제 시간 범위 문자열로 변환
     */
    private TimeGroupDto toFixedTimeGroup(int slot, List<ArticleDto> articles) {

        int startHour = slot * SLOT_HOURS;
        int endHour = startHour + SLOT_HOURS - 1;

        String range = String.format(
                "%02d:00 ~ %02d:59",
                startHour,
                endHour
        );

        return new TimeGroupDto(range, articles);
    }

    /**
     * 언론사(press) 기준 타임라인 조회
     * - 선택한 언론사 목록만 필터링
     * - 시간 그룹핑 규칙은 초기 타임라인과 동일
     */
    public DailyTimelineResponse getTimelineByPress(List<String> presses) {

        if (presses == null || presses.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_PRESS_FILTER);
        }

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<ArticleSummary> summaries =
                articleSummaryRepository.findByPressTimeline(
                        presses,
                        start,
                        end,
                        PageRequest.of(0, INITIAL_SIZE)
                );

        List<ArticleDto> articles = toArticleDtos(summaries);

        List<TimeGroupDto> groups = groupByFixedTimeline(articles);

        return new DailyTimelineResponse(
                today,
                articles.isEmpty()
                        ? LocalTime.now()
                        : articles.get(0).getPublishedAt().toLocalTime(),
                groups
        );
    }

    /**
     * 시계열(시간 범위) 기준 타임라인 조회
     * - 특정 날짜(date)의 시간 구간(from ~ to) 기준
     * - 기사 발행 시간(published_at)을 기준으로 필터링
     * - 최신 기사부터 최대 100건 조회
     * - 2시간 단위 고정 타임라인 그룹핑
     */
    public DailyTimelineResponse getTimelineByTimeRange(
            LocalDate date,
            LocalTime from,
            LocalTime to
    ) {

        // 파라미터 검증
        if (from.isAfter(to)) {
            throw new CustomException(ErrorCode.INVALID_TIME_RANGE);
        }

        // 날짜 + 시간 범위를 LocalDateTime 구간으로 변환
        LocalDateTime start = date.atTime(from);
        LocalDateTime end   = date.atTime(to);

        // 발행 시간 기준으로 기사 조회
        List<ArticleSummary> summaries =
                articleSummaryRepository.findByPublishedRange(
                        start,
                        end,
                        PageRequest.of(0, INITIAL_SIZE)
                );

        List<ArticleDto> articles = toArticleDtos(summaries);

        // 2시간 단위 고정 타임라인 그룹핑
        List<TimeGroupDto> groups = groupByFixedTimeline(articles);

        // 기준 날짜 + 가장 최신 기사 시간(baseTime) 포함하여 응답
        return new DailyTimelineResponse(
                date,
                articles.isEmpty()
                        ? LocalTime.now()
                        : articles.get(0).getPublishedAt().toLocalTime(),
                groups
        );
    }

    /**
     * 시계열 + 언론사 기준 타임라인 조회
     * - 특정 날짜(date)의 시간 구간(from ~ to)
     * - 선택한 언론사 목록 기준 필터링
     * - 기사 발행 시간(published_at) 기준 조회
     * - 최신 기사부터 최대 100건
     * - 2시간 단위 고정 타임라인 그룹핑
     */
    public DailyTimelineResponse getTimelineByTimeRangeAndPress(
            LocalDate date,
            LocalTime from,
            LocalTime to,
            List<String> presses
    ) {

        // 파라미터 검증
        if (from.isAfter(to)) {
            throw new CustomException(ErrorCode.INVALID_TIME_RANGE);
        }

        if (presses == null || presses.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_PRESS_FILTER);
        }

        LocalDateTime start = date.atTime(from);
        LocalDateTime end   = date.atTime(to);

        List<ArticleSummary> summaries =
                articleSummaryRepository.findByPublishedRangeAndPress(
                        presses,
                        start,
                        end,
                        PageRequest.of(0, INITIAL_SIZE)
                );

        List<ArticleDto> articles = toArticleDtos(summaries);

        List<TimeGroupDto> groups = groupByFixedTimeline(articles);

        return new DailyTimelineResponse(
                date,
                articles.isEmpty()
                        ? LocalTime.now()
                        : articles.get(0).getPublishedAt().toLocalTime(),
                groups
        );
    }

}
