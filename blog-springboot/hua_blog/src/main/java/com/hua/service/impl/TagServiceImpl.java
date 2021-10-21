package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.common.exception.ServiceException;
import com.hua.mapper.ArticleTagMapper;
import com.hua.mapper.TagMapper;
import com.hua.pojo.entity.ArticleTag;
import com.hua.pojo.entity.Tag;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.TagBackVO;
import com.hua.pojo.vo.TagVO;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.pojo.vo.param.TagParam;
import com.hua.service.TagService;
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
 * @date 2021/10/11 18:34
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public PageResult<TagVO> listTag() {
        // 查询标签列表
        List<Tag> tagList = tagMapper.selectList(null);
        // 查询标签数量
        Integer count = tagMapper.selectCount(null);
        // 转换vo
        List<TagVO> tagVOList = BeanCopyUtils.copyList(tagList, TagVO.class);
        return new PageResult<>(tagVOList, count);
    }

    @Override
    public PageResult<TagBackVO> listBackTag(QueryParam queryParam) {
        // 查询标签数量
        Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(queryParam.getKeywords()), Tag::getTagName,
                        queryParam.getKeywords()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询标签列表
        List<TagBackVO> tagBackVOList =
                tagMapper.listBackTag(PageUtils.getLimitCurrent(), PageUtils.getSize(), queryParam);
        return new PageResult<>(tagBackVOList, count);
    }

    @Override
    public List<TagVO> listTagBySearch(QueryParam queryParam) {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(queryParam.getKeywords()), Tag::getTagName,
                        queryParam.getKeywords())
                .orderByDesc(Tag::getId));
        return BeanCopyUtils.copyList(tagList, TagVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrUpdateTag(TagParam tagParam) {
        // 查询标签是否已存在
        Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getTagName, tagParam.getTagName()));
        if (count > 0) {
            throw new ServiceException("标签已存在");
        }
        Tag tag = BeanCopyUtils.copyObject(tagParam, Tag.class);
        this.saveOrUpdate(tag);
    }

    @Override
    public void deleteTag(List<Integer> tagIdList) {
        // 查询标签下是否有文章
        Integer count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIdList));
        if (count > 0) {
            throw new ServiceException("删除失败，该标签下存在文章");
        }
        tagMapper.deleteBatchIds(tagIdList);
    }

}
