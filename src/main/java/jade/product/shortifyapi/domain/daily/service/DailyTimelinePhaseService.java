package jade.product.shortifyapi.domain.daily.service;

import jade.product.shortifyapi.domain.article.repository.ArticleSummaryRepository;
import jade.product.shortifyapi.domain.daily.sse.TimelinePhase;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DailyTimelinePhaseService {

    private final ArticleSummaryRepository articleSummaryRepository;

    public DailyTimelinePhaseService(ArticleSummaryRepository articleSummaryRepository) {
        this.articleSummaryRepository = articleSummaryRepository;
    }

    public TimelinePhase resolvePhase(LocalDateTime now) {

        // 현재 슬롯 계산 (10~11, 12~13 ...)
        TimeSlot slot = TimeSlot.from(now);

        // 아직 수집 대상 시간이면 → COLLECTING
        if (!slot.isCollectWindowClosed(now)) {
            return TimelinePhase.COLLECTING;
        }

        // 해당 슬롯 기사 중 SUMMARY_DONE 아닌 게 있으면 → COLLECTING
        boolean allSummarized =
                articleSummaryRepository.isAllSummarized(
                        slot.getStart(), slot.getEnd()
                );

        return allSummarized
                ? TimelinePhase.DEPLOYED
                : TimelinePhase.COLLECTING;
    }
}
