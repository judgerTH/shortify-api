package jade.product.shortifyapi.domain.daily.service;

import jade.product.shortifyapi.domain.daily.sse.TimelinePhase;
import jade.product.shortifyapi.domain.daily.sse.TimelinePhaseState;
import jade.product.shortifyapi.domain.daily.sse.TimelineSseEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DailyTimelineSseService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final TimelinePhaseState phaseState = new TimelinePhaseState();

    public SseEmitter connect() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        // 연결 확인용 코드
        try {
            emitter.send(
                    SseEmitter.event()
                            .name("connected")
                            .data("ok")
            );
        } catch (Exception ignored) {}

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    public void publishPhase(TimelinePhase phase) {
        if (!phaseState.isChanged(phase)) return;

        phaseState.update(phase);

        TimelineSseEvent event = TimelineSseEvent.of(phase);

        emitters.forEach(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("phase")
                                .data(event)
                );
            } catch (IOException e) {
                emitters.remove(emitter);
                emitter.complete(); // 클라이언트 끊김
            }
            catch (Exception e) {
                emitters.remove(emitter);
                emitter.completeWithError(e); // 서버 로직 에러
            }

        });
    }

    public void heartbeat() {
        emitters.forEach(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("heartbeat")
                                .data("")
                );
            } catch (Exception ex) {
                emitters.remove(emitter);
                emitter.complete();
            }
        });
    }

}
