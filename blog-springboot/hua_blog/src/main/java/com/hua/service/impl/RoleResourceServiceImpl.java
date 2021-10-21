package com.hua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.mapper.RoleResourceMapper;
import com.hua.pojo.entity.RoleResource;
import com.hua.service.RoleResourceService;
import org.springframework.stereotype.Service;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 16:54
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {
}
