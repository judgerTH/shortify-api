package jade.product.shortifyapi.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== Common =====
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "E400", "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E404", "리소스를 찾을 수 없습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500", "서버 내부 오류가 발생했습니다."),

    // ===== Timeline =====
    INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "T400_1", "시간 범위가 올바르지 않습니다."),
    EMPTY_PRESS_FILTER(HttpStatus.BAD_REQUEST, "T400_2", "언론사 목록은 비어 있을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
