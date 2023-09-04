package uz.OLXCone.payload;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiResult<T> {
    private Boolean isSuccess;
    private String message;
    private T object;
    private List<String> errors;

    public static <T> ApiResult<T> noObject(String message, boolean isSuccess) {
        return new ApiResult<>(isSuccess,
                message,
                null);
    }
    public static <T> ApiResult<T> noMessage(boolean isSuccess, T object) {
        return new ApiResult<>(isSuccess, null, object);

    }
    public static <T> ApiResult<T> errorResponse(List<String> errors) {
        return new ApiResult<>(false,
                null,
                null,
                errors);
    }

    public static  <t> ApiResult<t> success(String txt, t obj) {
        return new ApiResult<>(true, txt, obj);
    }
    private ApiResult(Boolean isSuccess, String message, T object) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.object = object;
    }
}
