package com.hua.common.handler;

import com.hua.common.exception.ServiceException;
import com.hua.pojo.vo.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.hua.common.enums.ErrorCodeEnum.SYSTEM_ERROR;
import static com.hua.common.enums.ErrorCodeEnum.VALID_ERROR;

/**
 * 全局异常处理
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 21:56
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常服务
     * @param e 自定义异常
     * @return 返回异常消息
     */
    @ExceptionHandler(value = ServiceException.class)
    public Result serviceExceptionHandler(ServiceException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result errorHandler(MethodArgumentNotValidException e) {
        return Result.fail(VALID_ERROR.getCode(),
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 处理系统异常
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception e) {
        e.printStackTrace();
        return Result.fail(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMessage());
    }

}
