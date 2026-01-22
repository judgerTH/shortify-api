package jade.product.shortifyapi.domain.daily.controller;

import jade.product.shortifyapi.domain.daily.dto.response.DailyTimelineResponse;
import jade.product.shortifyapi.domain.daily.service.DailyTimelineService;
import jade.product.shortifyapi.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/daily/timeline")
public class DailyTimelineController {

    private final DailyTimelineService dailyTimelineService;

    /**
     * 초기 타임라인 조회
     * - 오늘 날짜 기준
     * - 최신 기사 최대 100건
     * - 2시간 단위 타임라인 그룹핑
     */
    @GetMapping("/init")
    public ApiResponse<DailyTimelineResponse> initTimeline() {
        return ApiResponse.ok(
                dailyTimelineService.getInitialTimeline()
        );
    }

    /**
     * 언론사 필터링 타임라인 조회
     * - 선택한 언론사(press) 목록 기준
     * - 오늘 날짜 기준
     * - 2시간 단위 타임라인 그룹핑
     *
     * 예) /v1/daily/timeline/press?press=동아일보&press=조선일보
     */
    @GetMapping("/press")
    public ApiResponse<DailyTimelineResponse> timelineByPress(
            @RequestParam List<String> press
    ) {
        return ApiResponse.ok(
                dailyTimelineService.getTimelineByPress(press)
        );
    }

    /**
     * 시계열(시간 범위) 타임라인 조회
     * - 날짜 + 시간 범위 기준
     * - published_at 기준 필터링
     * - 2시간 단위 타임라인 그룹핑
     *
     * 예)
     * /v1/daily/timeline/range?date=2026-01-22&from=10:00&to=14:00
     */
    @GetMapping("/range")
    public ApiResponse<DailyTimelineResponse> timelineByTimeRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime to
    ) {
        return ApiResponse.ok(
                dailyTimelineService.getTimelineByTimeRange(date, from, to)
        );
    }

    /**
     * 시계열 + 언론사 필터링 타임라인 조회
     *
     * - 특정 날짜(date)의 시간 구간(from ~ to)
     * - 선택한 언론사(press) 기준 필터링
     * - 기사 발행 시간(published_at) 기준
     * - 2시간 단위 고정 타임라인 그룹핑
     *
     * 예)
     * /v1/daily/timeline/range/press
     * ?date=2026-01-22
     * &from=10:00
     * &to=14:00
     * &press=동아일보
     * &press=조선일보
     */
    @GetMapping("/range/press")
    public ApiResponse<DailyTimelineResponse> timelineByTimeRangeAndPress(
            @RequestParam LocalDate date,
            @RequestParam LocalTime from,
            @RequestParam LocalTime to,
            @RequestParam List<String> press
    ) {
        return ApiResponse.ok(
                dailyTimelineService.getTimelineByTimeRangeAndPress(
                        date, from, to, press
                )
        );
    }

}
