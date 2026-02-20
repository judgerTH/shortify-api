package jade.product.shortifyapi.domain.daily.controller;

import jade.product.shortifyapi.domain.article.entity.NewsInsight;
import jade.product.shortifyapi.domain.daily.dto.response.NewsInsightDto;
import jade.product.shortifyapi.domain.daily.service.DailyNewsInsightService;
import jade.product.shortifyapi.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/daily/newsInsght")
public class DailyNewsInsightController {
    private final DailyNewsInsightService dailyNewsInsightService;

    /*
        초기 접속 시, 어제의 하루 분석결과 반환
     */
    @GetMapping("/")
    public ApiResponse<NewsInsightDto> getYesterdayInsight() {
        return ApiResponse.ok(dailyNewsInsightService.getYesterdayInsight());
    }
}
