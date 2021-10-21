package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资源 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 17:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceVO {

    /**
     * 资源id
     */
    private Integer id;

    /**
     * 资源名
     */
    private String resourceName;

    /**
     * 资源路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 是否禁用
     */
    private Integer isDisable;

    /**
     * 是否匿名访问
     */
    private Integer isAnonymous;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 子资源列表
     */
    private List<ResourceVO> children;

}
