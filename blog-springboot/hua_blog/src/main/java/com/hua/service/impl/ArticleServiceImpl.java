package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.common.exception.ServiceException;
import com.hua.mapper.ArticleMapper;
import com.hua.mapper.ArticleTagMapper;
import com.hua.mapper.CategoryMapper;
import com.hua.mapper.TagMapper;
import com.hua.pojo.dto.ArticleBackDTO;
import com.hua.pojo.entity.Article;
import com.hua.pojo.entity.ArticleTag;
import com.hua.pojo.entity.Category;
import com.hua.pojo.entity.Tag;
import com.hua.pojo.vo.*;
import com.hua.pojo.vo.param.ArticleTopParam;
import com.hua.pojo.vo.param.DeleteParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.ArticleService;
import com.hua.service.ArticleTagService;
import com.hua.service.TagService;
import com.hua.util.BeanCopyUtils;
import com.hua.util.PageUtils;
import com.hua.util.RedisUtils;
import com.hua.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.*;
import static com.hua.common.constant.RedisPrefixConst.*;
import static com.hua.common.enums.ArticleStatusEnum.DRAFT;
import static com.hua.common.enums.ArticleStatusEnum.PUBLIC;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 16:09
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private TagService tagService;

    @Autowired
    private HttpSession session;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<ArticleHomeVO> listArticle() {
        return articleMapper.listArticle(PageUtils.getLimitCurrent(), PageUtils.getSize());
    }

    @Override
    public PageResult<ArchiveVO> listArchive() {
        Page<Article> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        // 获取分页数据
        Page<Article> articlePage = articleMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getGmtCreate)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .orderByDesc(Article::getGmtCreate));
        List<ArchiveVO> articleVOList =
                BeanCopyUtils.copyList(articlePage.getRecords(), ArchiveVO.class);
        return new PageResult<>(articleVOList, (int) articlePage.getTotal());
    }

    @Override
    public ArticleVO getArticleById(Integer articleId) {
        // 根据id查询文章
        ArticleVO articleVO = articleMapper.getArticleById(articleId);
        if (Objects.isNull(articleVO)) {
            throw new ServiceException("文章不存在");
        }
        // 更新文章阅读量
        updateArticleViewCount(articleId);
        // 查询上一篇文章和下一篇文章
        Article lastArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last("limit 1"));
        Article nextArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .gt(Article::getId, articleId)
                .orderByAsc(Article::getId)
                .last("limit 1"));
        articleVO.setLastArticle(BeanCopyUtils.copyObject(lastArticle, ArticlePaginationVO.class));
        articleVO.setNextArticle(BeanCopyUtils.copyObject(nextArticle, ArticlePaginationVO.class));
        // 查询推荐文章
        CompletableFuture<List<ArticleRecommendVO>> recommendArticleList =
                CompletableFuture.supplyAsync(() -> articleMapper.listRecommendArticle(articleId));
        // 查询最新文章
        CompletableFuture<List<ArticleRecommendVO>> newestArticleList =
                CompletableFuture.supplyAsync(() -> {
                    List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                            .select(Article::getId, Article::getArticleTitle,
                                    Article::getArticleCover, Article::getGmtCreate)
                            .eq(Article::getIsDelete, FALSE)
                            .eq(Article::getStatus, PUBLIC.getStatus())
                            .orderByDesc(Article::getId)
                            .last("limit 5"));
                    return BeanCopyUtils.copyList(articleList, ArticleRecommendVO.class);
                });
        // 封装点赞量和浏览量
        Double score = redisUtils.zScore(ARTICLE_VIEW_COUNT, articleId);
        if (Objects.nonNull(score)) {
            articleVO.setViewCount(score.intValue());
        }
        articleVO.setLikeCount((Integer) redisUtils.hGet(ARTICLE_LIKE_COUNT, String.valueOf(articleId)));
        // 封装文章信息
        try {
            articleVO.setRecommendArticleList(recommendArticleList.get());
            articleVO.setNewestArticleList(newestArticleList.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articleVO;
    }

    @Override
    public ArticlePreviewListVO listArticleByQueryParam(QueryParam queryParam) {
        // 搜索条件对应数据
        List<ArticlePreviewVO> articlePreviewVOList =
                articleMapper.listArticleByQueryParam(PageUtils.getLimitCurrent(),
                        PageUtils.getSize(), queryParam);
        // 搜索条件对应名(标签或分类名)
        String name;
        if (Objects.nonNull(queryParam.getCategoryId())) {
            name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                    .select(Category::getCategoryName)
                    .eq(Category::getId, queryParam.getCategoryId()))
                    .getCategoryName();
        } else {
            name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getTagName)
                    .eq(Tag::getId, queryParam.getTagId()))
                    .getTagName();
        }
        return ArticlePreviewListVO.builder()
                .articlePreviewVOList(articlePreviewVOList)
                .name(name)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likeArticle(Integer articleId) {
        // 判断是否点过赞
        String articleLikeKey = ARTICLE_USER_LIKE + UserUtils.getLoginUser().getId();
        if (redisUtils.sHasKey(articleLikeKey, articleId)) {
            // 点过赞则删除文章id
            redisUtils.sRem(articleLikeKey, articleId);
            // 文章点赞量 - 1
            redisUtils.hDecr(ARTICLE_LIKE_COUNT, String.valueOf(articleId), 1);
        } else {
            // 未点赞则增加文章id
            redisUtils.sSet(articleLikeKey, String.valueOf(articleId));
            // 文章点赞量 + 1
            redisUtils.hIncr(ARTICLE_LIKE_COUNT, String.valueOf(articleId), 1);
        }
    }

    @Override
    public List<ArticleSearchVO> searchArticle(QueryParam queryParam) {
        String keywords = queryParam.getKeywords();
        // 判空
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        // 搜索文章
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .and(i -> i.like(Article::getArticleTitle, keywords)
                        .or()
                        .like(Article::getArticleContent, keywords)));
        // 高亮处理
        return articleList.stream().map(item -> {
            // 获取关键词第一次出现的位置
            String articleContent;
            int index = item.getArticleContent().indexOf(keywords);
            if (index != -1) {
                // 获取关键词前面的文字
                int preIndex = index > 25 ? index - 25 : 0;
                String preText = item.getArticleContent().substring(preIndex, index);
                // 获取关键词到后面的文字
                int last = index + keywords.length();
                int postLength = item.getArticleContent().length() - last;
                int postIndex = postLength > 175 ? last + 175 : last + postLength;
                String postText = item.getArticleContent().substring(index, postIndex);
                // 文章内容高亮
                articleContent = (preText + postText)
                        .replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
            } else {
                articleContent = item.getArticleContent();
            }
            // 文章标题高亮
            String articleTitle = item.getArticleTitle()
                    .replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
            return ArticleSearchVO.builder()
                    .id(item.getId())
                    .articleTitle(articleTitle)
                    .articleContent(articleContent)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public PageResult<ArticleBackVO> listBackArticle(QueryParam queryParam) {
        // 查询文章总数
        Integer count = articleMapper.queryCount(queryParam);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台文章
        List<ArticleBackVO> articleBackVOList =
                articleMapper.listBackArticle(PageUtils.getLimitCurrent(), PageUtils.getSize(),
                        queryParam);
        // 查询文章浏览量
        Map<Object, Double> viewCountMap = redisUtils.zAllScore(ARTICLE_VIEW_COUNT);
        // 查询文章点赞量
        Map<Object, Object> likeCountMap = redisUtils.hmGet(ARTICLE_LIKE_COUNT);
        // 封装文章浏览量和点赞量
        articleBackVOList.forEach(articleBackVO -> {
            Double viewCount = viewCountMap.get(articleBackVO.getId());
            if (Objects.nonNull(viewCount)) {
                articleBackVO.setViewCount(viewCount.intValue());
            }
            articleBackVO.setLikeCount((Integer) likeCountMap.get(String.valueOf(articleBackVO.getId())));
        });
        return new PageResult<>(articleBackVOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrUpdateArticle(ArticleBackDTO articleBackDTO) {
        // 保存文章分类
        Category category = saveArticleCategory(articleBackDTO);
        // 保存或修改文章
        Article article = BeanCopyUtils.copyObject(articleBackDTO, Article.class);
        if (Objects.nonNull(category)) {
            article.setCategoryId(category.getId());
        }
        article.setAuthorId(UserUtils.getLoginUser().getId());
        this.saveOrUpdate(article);
        // 保存文章标签
        saveArticleTag(articleBackDTO, article.getId());
    }

    /**
     * 保存文章分类
     * @param articleBackDTO 文章参数
     * @return 文章分类
     */
    private Category saveArticleCategory(ArticleBackDTO articleBackDTO) {
        // 判断分类是否存在
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, articleBackDTO.getCategoryName()));
        if (Objects.isNull(category) && !articleBackDTO.getStatus().equals(DRAFT.getStatus())) {
            category = Category.builder()
                    .categoryName(articleBackDTO.getCategoryName())
                    .build();
            categoryMapper.insert(category);
        }
        return category;
    }

    /**
     * 保存文章标签
     * @param articleBackDTO 文章参数
     * @param articleId 文章id
     */
    private void saveArticleTag(ArticleBackDTO articleBackDTO, Integer articleId) {
        // 编辑文章则删除文章所有标签
        if (Objects.nonNull(articleBackDTO.getId())) {
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, articleBackDTO.getId()));
        }
        // 添加文章标签
        List<String> tagNameList = articleBackDTO.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已存在的标签
            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>()
                    .in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            // 对比新增不存在的标签
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder()
                            .tagName(item)
                            .build())
                        .collect(Collectors.toList());
                tagService.saveBatch(tagList);
                List<Integer> tagIdList = tagList.stream()
                        .map(Tag::getId)
                        .collect(Collectors.toList());
                existTagIdList.addAll(tagIdList);
            }
            // 提取标签id绑定文章
            List<ArticleTag> articleTagList = existTagIdList.stream().map(item -> ArticleTag.builder()
                        .articleId(articleId)
                        .tagId(item)
                        .build())
                    .collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticleTop(ArticleTopParam articleTopParam) {
        // 修改置顶状态值
        Article article = Article.builder()
                .id(articleTopParam.getId())
                .isTop(articleTopParam.getIsTop())
                .build();
        articleMapper.updateById(article);
    }

    @Override
    public ArticleBackDTO getBackArticleById(Integer articleId) {
        // 查询文章信息
        Article article = articleMapper.selectById(articleId);
        // 查询文章分类
        Category category = categoryMapper.selectById(article.getCategoryId());
        String categoryName = null;
        if (Objects.nonNull(category)) {
            categoryName = category.getCategoryName();
        }
        // 查询文章标签
        List<String> tagNameList = tagMapper.listTagNameByArticleId(articleId);
        // 封装数据
        ArticleBackDTO articleBackDTO = BeanCopyUtils.copyObject(article, ArticleBackDTO.class);
        articleBackDTO.setCategoryName(categoryName);
        articleBackDTO.setTagNameList(tagNameList);
        return articleBackDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticleDelete(DeleteParam deleteParam) {
        // 修改文章逻辑删除的状态
        List<Article> articleList = deleteParam.getIdList().stream()
                .map(id -> Article.builder()
                        .id(id)
                        .isTop(FALSE)
                        .isDelete(deleteParam.getIsDelete())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteArticle(List<Integer> articleIdList) {
        // 删除文章和标签的关联
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        // 删除文章
        articleMapper.deleteBatchIds(articleIdList);
    }

    /**
     * 更新文章阅读量
     * @param articleId 文章id
     */
    @Async
    public void updateArticleViewCount(Integer articleId) {
        // 判断是否第一次访问，增加阅读量
        Set<Integer> articleSet =
                (Set<Integer>) Optional.ofNullable(session.getAttribute(ARTICLE_SET))
                        .orElse(new HashSet<>());
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET, articleSet);
            // 浏览量 + 1
            redisUtils.zIncr(ARTICLE_VIEW_COUNT, articleId, 1D);
        }
    }

}
