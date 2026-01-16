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

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyTimelineService {

    private final ArticleSummaryRepository articleSummaryRepository;

    private static final int INITIAL_SIZE = 100;

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

        Map<Integer, List<ArticleDto>> grouped =
                summaries.stream()
                        .map(this::toArticleDto)
                        .collect(Collectors.groupingBy(
                                dto -> dto.getPublishedAt().getHour() / 2
                        ));

        List<TimeGroupDto> groups =
                grouped.entrySet().stream()
                        // 최신 시간대 먼저
                        .sorted(Map.Entry.<Integer, List<ArticleDto>>comparingByKey().reversed())
                        .map(e -> toTimeGroup(e.getKey(), e.getValue()))
                        .toList();

        LocalTime baseTime = summaries.isEmpty()
                ? LocalTime.now()
                : summaries.get(0).getArticleMeta().getPublishedAt().toLocalTime();

        return new DailyTimelineResponse(
                today,
                baseTime,
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

    private TimeGroupDto toTimeGroup(int slot, List<ArticleDto> articles) {
        int startHour = slot * 2;
        int endHour = startHour + 1;

        String range = String.format(
                "%02d:00-%02d:59",
                startHour,
                endHour
        );

        return new TimeGroupDto(range, articles);
    }
}
