package com.targinou.taskmanagement.commom.view;



import com.targinou.taskmanagement.commom.enums.ResultType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericView<T> {

    private T result;
    private String resultMessage;
    private ResultType resultType;

    public static <T> GenericView<T> ok() {
        return ok(null);
    }
    public static <T> GenericView<T> ok(T result) {
        return GenericView.<T>builder()
                .resultType(ResultType.OK)
                .result(result)
                .build();
    }

    public static GenericView<Void> fail(ResultType type, String message) {
        return GenericView.<Void>builder()
                .resultType(type)
                .resultMessage(message)
                .build();
    }

}
