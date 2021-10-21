package com.hua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hua.pojo.entity.User;
import com.hua.pojo.vo.UserBackVO;
import com.hua.pojo.vo.param.QueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 19:21
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询后台用户列表
     * @param current 当前页
     * @param size 页大小
     * @param queryParam 条件
     * @return 用户列表
     */
    List<UserBackVO> listUser(@Param("current") Long current, @Param("size") Long size, @Param("queryParam") QueryParam queryParam);

}
