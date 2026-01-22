package jade.product.shortifyapi.domain.daily.service;

import jade.product.shortifyapi.domain.article.entity.ArticleMeta;
import jade.product.shortifyapi.domain.article.entity.ArticleSummary;
import jade.product.shortifyapi.domain.article.repository.ArticleSummaryRepository;
import jade.product.shortifyapi.domain.daily.dto.response.ArticleDto;
import jade.product.shortifyapi.domain.daily.dto.response.DailyTimelineResponse;
import jade.product.shortifyapi.domain.daily.dto.response.TimeGroupDto;
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

    private static final int INITIAL_SIZE = 100;
    private static final Duration GROUP_GAP = Duration.ofHours(2);
    private static final int SLOT_HOURS = 2;

    public DailyTimelineResponse getInitialTimeline() {

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<ArticleDto> articles =
                articleSummaryRepository.findTodaySummaries(
                                start,
                                end,
                                PageRequest.of(0, INITIAL_SIZE)
                        ).stream()
                        .map(this::toArticleDto)
                        .toList();

        List<TimeGroupDto> groups = groupByFixedTimeline(articles);

        return new DailyTimelineResponse(
                today,
                articles.isEmpty()
                        ? LocalTime.now()
                        : articles.get(0).getPublishedAt().toLocalTime(),
                groups
        );
    }

    private ArticleDto toArticleDto(ArticleSummary summary) {
        ArticleMeta meta = summary.getArticleMeta();

        return new ArticleDto(
                meta.getId(),
                summary.getSummaryTitle(),
                meta.getPress(),
                meta.getPublishedAt(),
                meta.getCollectedAt(),
                summary.getSummaryContent(),
                meta.getUrl(),
                0,
                0
        );
    }

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
}
