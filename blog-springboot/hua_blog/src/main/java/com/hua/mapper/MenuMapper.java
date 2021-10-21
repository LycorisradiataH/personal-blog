package com.hua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hua.pojo.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/16 16:32
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id查询菜单
     * @param userId 用户id
     * @return 菜单列表
     */
    List<Menu> listMenusByUserId(@Param("userId") Integer userId);

}
