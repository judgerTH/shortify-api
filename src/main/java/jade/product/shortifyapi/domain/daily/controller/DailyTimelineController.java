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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/daily")
public class DailyTimelineController {

    private final DailyTimelineService dailyTimelineService;

    /**
     * 초기 타임라인 조회
     * - 현재 시점 기준
     * - 최신 기사 100건
     */
    @GetMapping("/timeline/init")
    public ApiResponse<DailyTimelineResponse> initTimeline() {
        return ApiResponse.ok(
                dailyTimelineService.getInitialTimeline()
        );
    }
}
