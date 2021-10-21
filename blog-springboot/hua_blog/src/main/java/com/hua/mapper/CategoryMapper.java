package com.hua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hua.pojo.entity.Category;
import com.hua.pojo.vo.CategoryBackVO;
import com.hua.pojo.vo.CategoryVO;
import com.hua.pojo.vo.param.QueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 17:38
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询分类和对应文章数量
     * @return 分类集合
     */
    List<CategoryVO> listCategory();

    /**
     * 查询后台分类列表
     * @param current 页码
     * @param size 页大小
     * @param queryParam 条件
     * @return 后台分类列表
     */
    List<CategoryBackVO> listBackCategory(@Param("current") Long current, @Param("size") Long size, @Param("queryParam") QueryParam queryParam);
}
