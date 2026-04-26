package com.jie.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Unified API response wrapper.
 */
@Getter
@Schema(description = "统一响应体")
public class Result<T> {

    @Schema(description = "状态码: 0=成功, 非0=业务错误")
    private final int code;

    @Schema(description = "提示消息")
    private final String message;

    @Schema(description = "响应数据")
    private final T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(0, "success", data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(0, "success", null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }
}
