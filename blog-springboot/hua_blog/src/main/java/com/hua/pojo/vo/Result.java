package com.hua.pojo.vo;

import com.hua.common.enums.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回类
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 19:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    /**
     * 状态
     */
    private Boolean status;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    public static Result success() {
        return success(null);
    }

    public static Result success(Object data) {
        return success("success", data);
    }

    public static Result success(String message, Object data) {
        return new Result(true, 200, message, data);
    }

    public static Result fail(Integer code, String message) {
        return new Result(false, code, message, null);
    }

    public static Result fail(ErrorCodeEnum errorCode) {
        return new Result(false, errorCode.getCode(), errorCode.getMessage(), null);
    }

}
