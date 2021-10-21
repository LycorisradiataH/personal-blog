package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.common.exception.ServiceException;
import com.hua.mapper.ArticleMapper;
import com.hua.mapper.CategoryMapper;
import com.hua.pojo.entity.Article;
import com.hua.pojo.entity.Category;
import com.hua.pojo.vo.CategoryBackVO;
import com.hua.pojo.vo.CategoryOptionVO;
import com.hua.pojo.vo.CategoryVO;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.param.CategoryParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.CategoryService;
import com.hua.util.BeanCopyUtils;
import com.hua.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 18:17
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageResult<CategoryVO> listCategory() {
        List<CategoryVO> categoryVOList = categoryMapper.listCategory();
        Integer count = categoryMapper.selectCount(null);
        return new PageResult<>(categoryVOList, count);
    }

    @Override
    public PageResult<CategoryBackVO> listBackCategory(QueryParam queryParam) {
        // 查询分类数量
        Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(queryParam.getKeywords()), Category::getCategoryName,
                        queryParam.getKeywords()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询分类列表
        List<CategoryBackVO> categoryBackVOList = categoryMapper
                .listBackCategory(PageUtils.getLimitCurrent(), PageUtils.getSize(), queryParam);
        return new PageResult<>(categoryBackVOList, count);
    }

    @Override
    public List<CategoryOptionVO> listCategoryBySearch(QueryParam queryParam) {
        // 搜索分类
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(queryParam.getKeywords()), Category::getCategoryName, queryParam.getKeywords())
                .orderByDesc(Category::getId));
        return BeanCopyUtils.copyList(categoryList, CategoryOptionVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrUpdateCategory(CategoryParam categoryParam) {
        // 判断分类名是否重复
        Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, categoryParam.getCategoryName()));
        if (count > 0) {
            throw new ServiceException("分类名已存在");
        }
        Category category = Category.builder()
                .id(categoryParam.getId())
                .categoryName(categoryParam.getCategoryName())
                .build();
        this.saveOrUpdate(category);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        // 查询分类id下是否有文章
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList));
        if (count > 0) {
            throw new ServiceException("删除失败，该分类下存在文章");
        }
        categoryMapper.deleteBatchIds(categoryIdList);
    }

}
