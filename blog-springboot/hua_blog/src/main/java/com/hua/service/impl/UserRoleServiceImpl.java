package com.hua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.mapper.UserRoleMapper;
import com.hua.pojo.entity.UserRole;
import com.hua.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 17:47
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
