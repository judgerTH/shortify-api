package jade.product.shortifyapi.domain.daily.controller;

import jade.product.shortifyapi.domain.daily.service.DailyTimelineSseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 타임라인 상태 SSE 연결 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class DailyTimelineSseController {

    private final DailyTimelineSseService sseService;

    /**
     * 타임라인 상태 구독
     */
    @GetMapping(
            value = "/v1/daily/timeline/sse",
            produces = "text/event-stream"
    )
    public SseEmitter connect() {
        return sseService.connect();
    }
}
