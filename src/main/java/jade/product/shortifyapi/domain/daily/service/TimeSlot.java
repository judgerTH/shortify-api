package jade.product.shortifyapi.domain.daily.service;

import java.time.LocalDateTime;

/**
 * 2시간 단위 기사 수집 슬롯
 *
 * 예)
 * 10:00 ~ 11:59 발행 기사 → 10시 슬롯
 * 수집 종료 시각 : 슬롯 시작 + 25분
 */
public class TimeSlot {

    private static final int SLOT_HOURS = 2;
    private static final int COLLECT_CLOSE_MINUTE = 25;

    private final LocalDateTime start;
    private final LocalDateTime end;

    private TimeSlot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 현재 시간 기준 슬롯 계산
     */
    public static TimeSlot from(LocalDateTime now) {
        int slotHour = (now.getHour() / SLOT_HOURS) * SLOT_HOURS;

        LocalDateTime start =
                now.toLocalDate()
                        .atTime(slotHour, 0);

        LocalDateTime end =
                start.plusHours(SLOT_HOURS).minusSeconds(1);

        return new TimeSlot(start, end);
    }

    /**
     * 수집 윈도우가 닫혔는지 판단
     *
     * 예) 10시 슬롯 → 10:25 이후 true
     */
    public boolean isCollectWindowClosed(LocalDateTime now) {
        LocalDateTime closeTime =
                start.plusMinutes(COLLECT_CLOSE_MINUTE);

        return now.isAfter(closeTime);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
