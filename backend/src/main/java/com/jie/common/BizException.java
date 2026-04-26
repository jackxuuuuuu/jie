package com.jie.common;

import lombok.Getter;

/**
 * Business exception with HTTP-friendly status code.
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message) {
        super(message);
        this.code = 400;
    }

    public static BizException notFound(String entity) {
        return new BizException(404, entity + " not found");
    }

    public static BizException forbidden(String message) {
        return new BizException(403, message);
    }
}
