package com.hua.util;

import com.hua.pojo.dto.UserDetailsDTO;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户工具类
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 18:11
 */
public class UserUtils {

    /**
     * 获取当前登录的用户
     * @return 用户登录信息类
     */
    public static UserDetailsDTO getLoginUser() {
        return (UserDetailsDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
