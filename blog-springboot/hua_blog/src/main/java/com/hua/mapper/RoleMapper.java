package com.hua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hua.pojo.dto.ResourceRoleDTO;
import com.hua.pojo.entity.Role;
import com.hua.pojo.vo.RoleVO;
import com.hua.pojo.vo.param.QueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 20:08
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询路由角色列表
     * @return 角色标签
     */
    List<ResourceRoleDTO> listResourceRole();

    /**
     * 根据用户id获取角色列表
     * @param userId 用户id
     * @return 角色标签
     */
    List<String> listRolesByUserId(@Param("userId") Integer userId);

    /**
     * 查询角色列表
     * @param current 页码
     * @param size 页大小
     * @param queryParam 条件
     * @return 角色列表
     */
    List<RoleVO> listRole(@Param("current") Long current, @Param("size") Long size, @Param("queryParam") QueryParam queryParam);
}
