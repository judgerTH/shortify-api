package jade.product.shortifyapi.domain.daily.sse;

import java.time.LocalDateTime;

/**
 * 마지막으로 전송된 타임라인 상태를 기억하기 위한 객체
 *
 * - 동일한 phase가 반복 전송되는 것을 방지
 * - 상태가 실제로 변경된 경우에만 SSE 전송
 */
public class TimelinePhaseState {

    private TimelinePhase lastPhase;
    private LocalDateTime lastUpdatedAt;

    /**
     * 이전 상태와 비교하여 변경 여부 판단
     */
    public boolean isChanged(TimelinePhase newPhase) {
        return lastPhase == null || lastPhase != newPhase;
    }

    /**
     * 상태 업데이트
     */
    public void update(TimelinePhase phase) {
        this.lastPhase = phase;
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
