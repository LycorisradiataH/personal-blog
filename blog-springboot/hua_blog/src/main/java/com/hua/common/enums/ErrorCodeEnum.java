package com.hua.common.enums;

/**
 * 错误码信息枚举
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 19:19
 */
public enum ErrorCodeEnum {

    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000, "系统异常"),
    /**
     * 参数校验失败
     */
    VALID_ERROR(52000, "参数格式不正确"),
    /**
     * 用户名已存在
     */
    USERNAME_EXIST(70001, "用户名已存在"),
    /**
     * 用户名不存在
     */
    USERNAME_NOT_EXIST(70002, "用户名不存在"),
    /**
     * 没有操作权限
     */
    AUTHORIZED_ERROR(80001, "没有操作权限"),
    /**
     * 未登录
     */
    NO_LOGIN(90001, "未登录"),
    ;

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 消息
     */
    private final String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
