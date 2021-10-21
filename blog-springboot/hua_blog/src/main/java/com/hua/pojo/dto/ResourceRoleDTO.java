package com.hua.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 资源角色 dto
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 19:57
 */
@Data
public class ResourceRoleDTO {

    /**
     * 资源id
     */
    private Integer id;

    /**
     * 路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 角色名
     */
    private List<String> roleList;

}
