package jade.product.shortifyapi.domain.daily.sse;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * SSE로 전달되는 타임라인 상태 이벤트
 */
@Getter
public class TimelineSseEvent {

    private final String phase;
    private final LocalDateTime emittedAt;

    private TimelineSseEvent(String phase) {
        this.phase = phase;
        this.emittedAt = LocalDateTime.now();
    }

    public static TimelineSseEvent of(TimelinePhase phase) {
        return new TimelineSseEvent(phase.name());
    }

}
