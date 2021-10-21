package com.hua.common.exception;

import com.hua.common.enums.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务异常
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 21:54
 */
@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = 80000;

    /**
     * 错误信息
     */
    private final String message;

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(ErrorCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getMessage();
    }

}
