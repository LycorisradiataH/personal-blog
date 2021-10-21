package com.hua.common.handler;

import com.alibaba.fastjson.JSON;
import com.hua.mapper.UserMapper;
import com.hua.pojo.entity.User;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.UserInfoVO;
import com.hua.util.BeanCopyUtils;
import com.hua.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 18:08
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        // 返回登录信息
        UserInfoVO userInfoVO =
                BeanCopyUtils.copyObject(UserUtils.getLoginUser(), UserInfoVO.class);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.success(userInfoVO)));
        // 更新用户ip，最近登录时间
        updateUserInfo();
    }

    /**
     * 更新用户信息
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo() {
        User user = User.builder()
                .id(UserUtils.getLoginUser().getId())
                .ipAddr(UserUtils.getLoginUser().getIpAddr())
                .ipSource(UserUtils.getLoginUser().getIpSource())
                .lastLoginTime(UserUtils.getLoginUser().getLastLoginTime())
                .build();
        userMapper.updateById(user);
    }

}
