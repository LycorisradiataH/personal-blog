package com.hua.pojo.vo.param;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 用户禁用参数
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 17:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户禁用参数")
public class UserDisableParam {

    /**
     * id
     */
    @NotNull(message = "用户id不能为空")
    private Integer id;

    /**
     * 禁用状态
     */
    @NotNull(message = "禁用状态不能为空")
    private Integer isDisable;

}
