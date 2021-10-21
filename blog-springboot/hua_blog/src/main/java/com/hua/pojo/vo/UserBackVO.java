package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 后台用户 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 18:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBackVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户角色
     */
    private List<UserRoleVO> roleList;

    /**
     * 用户登录ip
     */
    private String ipAddr;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 最近登录时间
     */
    private Date lastLoginTime;

    /**
     * 用户评论状态
     */
    private Integer isDisable;

    /**
     * 状态
     */
    private Integer status;

}
