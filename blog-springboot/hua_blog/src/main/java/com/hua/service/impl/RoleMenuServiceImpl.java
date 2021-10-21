package com.hua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.mapper.RoleMenuMapper;
import com.hua.pojo.entity.RoleMenu;
import com.hua.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/16 16:50
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
