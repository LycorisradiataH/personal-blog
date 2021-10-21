package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hua.common.exception.ServiceException;
import com.hua.mapper.RoleMapper;
import com.hua.mapper.UserMapper;
import com.hua.pojo.dto.UserDetailsDTO;
import com.hua.pojo.entity.User;
import com.hua.util.IpUtils;
import com.hua.util.RedisUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.hua.common.constant.CommonConst.TRUE;
import static com.hua.common.constant.RedisPrefixConst.ARTICLE_USER_LIKE;
import static com.hua.common.constant.RedisPrefixConst.COMMENT_USER_LIKE;
import static com.hua.common.enums.ZoneEnum.SHANGHAI;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 21:51
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new ServiceException("用户名不能为空");
        }
        // 查询账户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getId, User::getEmail, User::getPassword, User::getAvatar,
                User::getNickname, User::getIntro, User::getWebSite, User::getIsDisable)
                .eq(User::getEmail, username);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new ServiceException("用户不存在");
        }
        if (user.getIsDisable() == TRUE) {
            throw new ServiceException("用户已被禁用");
        }
        // 封装登录信息
        return convertLoginUser(user, request);
    }

    /**
     * 封装用户登录信息
     * @param user 用户账号
     * @param request 请求
     * @return 用户登录信息
     */
    public UserDetails convertLoginUser(User user, HttpServletRequest request) {
        // 查询账号角色
        List<String> roleList = roleMapper.listRolesByUserId(user.getId());
        // 查询账号点赞信息
        Set<Object> articleLikeSet =
                redisUtils.sGet(ARTICLE_USER_LIKE + user.getId());
        Set<Object> commentLikeSet =
                redisUtils.sGet(COMMENT_USER_LIKE + user.getId());
        // 获取设备信息
        String ipAddr = IpUtils.getIpAddr(request);
        String ipSource = IpUtils.getIpSource(ipAddr);
        UserAgent userAgent = IpUtils.getUserAgent(request);
        // 封装权限集合
        return UserDetailsDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .intro(user.getIntro())
                .webSite(user.getWebSite())
                .roleList(roleList)
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .ipAddr(ipAddr)
                .ipSource(ipSource)
                .isDisable(user.getIsDisable())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())))
                .build();
    }

}
