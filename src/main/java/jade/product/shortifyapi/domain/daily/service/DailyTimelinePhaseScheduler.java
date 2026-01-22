package jade.product.shortifyapi.domain.daily.service;

import jade.product.shortifyapi.domain.daily.sse.TimelinePhase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DailyTimelinePhaseScheduler {

    private final DailyTimelinePhaseService phaseService;
    private final DailyTimelineSseService sseService;

    /**
     * 타임라인 상태 감시 스케줄러
     *
     * - 30초 주기로 현재 시각 기준 Phase 판단
     * - Phase 변경이 있을 때만 SSE 전송
     */
    @Scheduled(fixedDelay = 30_000)
    public void checkTimelinePhase() {

        LocalDateTime now = LocalDateTime.now();

        TimelinePhase phase = phaseService.resolvePhase(now);

        sseService.publishPhase(phase);
    }
}
