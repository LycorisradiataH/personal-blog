package com.hua.pojo.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 页面参数
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 17:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "页面参数")
public class PageParam {

    /**
     * 页面id
     */
    @NotNull(message = "页面id不能为空")
    @ApiModelProperty(name = "id", value = "页面id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 页面名
     */
    @NotBlank(message = "页面名称不能为空")
    @ApiModelProperty(name = "pageName", value = "页面名称", required = true, dataType = "String")
    private String pageName;

    /**
     * 页面标签
     */
    @NotBlank(message = "页面标签不能为空")
    @ApiModelProperty(name = "pageLabel", value = "页面标签", required = true, dataType = "String")
    private String pageLabel;

    /**
     * 页面封面
     */
    @NotBlank(message = "页面封面不能为空")
    @ApiModelProperty(name = "pageCover", value = "页面封面", required = true, dataType = "String")
    private String pageCover;

}
