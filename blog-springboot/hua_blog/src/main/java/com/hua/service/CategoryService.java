package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.Category;
import com.hua.pojo.vo.CategoryBackVO;
import com.hua.pojo.vo.CategoryOptionVO;
import com.hua.pojo.vo.CategoryVO;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.param.CategoryParam;
import com.hua.pojo.vo.param.QueryParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 18:17
 */
public interface CategoryService extends IService<Category> {

    /**
     * 查询分类列表
     * @return 分类列表
     */
    PageResult<CategoryVO> listCategory();

    /**
     * 查询后台分类列表
     * @param queryParam 条件
     * @return 后台分类列表
     */
    PageResult<CategoryBackVO> listBackCategory(QueryParam queryParam);

    /**
     * 搜索文章分类
     * @param queryParam 条件
     * @return 分类列表
     */
    List<CategoryOptionVO> listCategoryBySearch(QueryParam queryParam);

    /**
     * 添加或修改分类
     * @param categoryParam 分类
     */
    void insertOrUpdateCategory(CategoryParam categoryParam);

    /**
     * 根据id集合删除分类
     * @param categoryIdList id集合
     */
    void deleteCategory(List<Integer> categoryIdList);

}
