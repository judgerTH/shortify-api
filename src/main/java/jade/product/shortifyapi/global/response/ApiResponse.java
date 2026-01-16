package jade.product.shortifyapi.global.response;

import jade.product.shortifyapi.global.error.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private ErrorResponse error;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<?> error(ErrorResponse error) {
        return new ApiResponse<>(false, null, error);
    }
}
