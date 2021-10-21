package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 18:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleVO {

    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名
     */
    private String roleName;

}
