package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.common.exception.ServiceException;
import com.hua.common.strategy.context.SearchStrategyContext;
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

import static com.hua.common.constant.CommonConst.ARTICLE_SET;
import static com.hua.common.constant.CommonConst.FALSE;
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
    private SearchStrategyContext searchStrategyContext;

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
        // ??????????????????
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
        // ??????id????????????
        ArticleVO articleVO = articleMapper.getArticleById(articleId);
        if (Objects.isNull(articleVO)) {
            throw new ServiceException("???????????????");
        }
        // ?????????????????????
        updateArticleViewCount(articleId);
        // ???????????????????????????????????????
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
        // ??????????????????
        CompletableFuture<List<ArticleRecommendVO>> recommendArticleList =
                CompletableFuture.supplyAsync(() -> articleMapper.listRecommendArticle(articleId));
        // ??????????????????
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
        // ???????????????????????????
        Double score = redisUtils.zScore(ARTICLE_VIEW_COUNT, articleId);
        if (Objects.nonNull(score)) {
            articleVO.setViewCount(score.intValue());
        }
        articleVO.setLikeCount((Integer) redisUtils.hGet(ARTICLE_LIKE_COUNT, String.valueOf(articleId)));
        // ??????????????????
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
        // ????????????????????????
        List<ArticlePreviewVO> articlePreviewVOList =
                articleMapper.listArticleByQueryParam(PageUtils.getLimitCurrent(),
                        PageUtils.getSize(), queryParam);
        // ?????????????????????(??????????????????)
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
        // ?????????????????????
        String articleLikeKey = ARTICLE_USER_LIKE + UserUtils.getLoginUser().getId();
        if (redisUtils.sHasKey(articleLikeKey, articleId)) {
            // ????????????????????????id
            redisUtils.sRem(articleLikeKey, articleId);
            // ??????????????? - 1
            redisUtils.hDecr(ARTICLE_LIKE_COUNT, String.valueOf(articleId), 1L);
        } else {
            // ????????????????????????id
            redisUtils.sSet(articleLikeKey, articleId);
            // ??????????????? + 1
            redisUtils.hIncr(ARTICLE_LIKE_COUNT, String.valueOf(articleId), 1L);
        }
    }

    @Override
    public List<ArticleSearchVO> searchArticle(QueryParam queryParam) {
        String keywords = queryParam.getKeywords();
        // ??????
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        return searchStrategyContext.executeSearchStrategy(keywords);
    }

    @Override
    public PageResult<ArticleBackVO> listBackArticle(QueryParam queryParam) {
        // ??????????????????
        Integer count = articleMapper.queryCount(queryParam);
        if (count == 0) {
            return new PageResult<>();
        }
        // ??????????????????
        List<ArticleBackVO> articleBackVOList =
                articleMapper.listBackArticle(PageUtils.getLimitCurrent(), PageUtils.getSize(),
                        queryParam);
        // ?????????????????????
        Map<Object, Double> viewCountMap = redisUtils.zAllScore(ARTICLE_VIEW_COUNT);
        // ?????????????????????
        Map<Object, Object> likeCountMap = redisUtils.hmGet(ARTICLE_LIKE_COUNT);
        // ?????????????????????????????????
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
        // ??????????????????
        Category category = saveArticleCategory(articleBackDTO);
        // ?????????????????????
        Article article = BeanCopyUtils.copyObject(articleBackDTO, Article.class);
        if (Objects.nonNull(category)) {
            article.setCategoryId(category.getId());
        }
        article.setAuthorId(UserUtils.getLoginUser().getId());
        this.saveOrUpdate(article);
        // ??????????????????
        saveArticleTag(articleBackDTO, article.getId());
    }

    /**
     * ??????????????????
     * @param articleBackDTO ????????????
     * @return ????????????
     */
    private Category saveArticleCategory(ArticleBackDTO articleBackDTO) {
        // ????????????????????????
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
     * ??????????????????
     * @param articleBackDTO ????????????
     * @param articleId ??????id
     */
    private void saveArticleTag(ArticleBackDTO articleBackDTO, Integer articleId) {
        // ???????????????????????????????????????
        if (Objects.nonNull(articleBackDTO.getId())) {
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, articleBackDTO.getId()));
        }
        // ??????????????????
        List<String> tagNameList = articleBackDTO.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // ????????????????????????
            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>()
                    .in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            // ??????????????????????????????
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
            // ????????????id????????????
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
        // ?????????????????????
        Article article = Article.builder()
                .id(articleTopParam.getId())
                .isTop(articleTopParam.getIsTop())
                .build();
        articleMapper.updateById(article);
    }

    @Override
    public ArticleBackDTO getBackArticleById(Integer articleId) {
        // ??????????????????
        Article article = articleMapper.selectById(articleId);
        // ??????????????????
        Category category = categoryMapper.selectById(article.getCategoryId());
        String categoryName = null;
        if (Objects.nonNull(category)) {
            categoryName = category.getCategoryName();
        }
        // ??????????????????
        List<String> tagNameList = tagMapper.listTagNameByArticleId(articleId);
        // ????????????
        ArticleBackDTO articleBackDTO = BeanCopyUtils.copyObject(article, ArticleBackDTO.class);
        articleBackDTO.setCategoryName(categoryName);
        articleBackDTO.setTagNameList(tagNameList);
        return articleBackDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticleDelete(DeleteParam deleteParam) {
        // ?????????????????????????????????
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
        // ??????????????????????????????
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        // ????????????
        articleMapper.deleteBatchIds(articleIdList);
    }

    /**
     * ?????????????????????
     * @param articleId ??????id
     */
    @Async
    public void updateArticleViewCount(Integer articleId) {
        // ?????????????????????????????????????????????
        Set<Integer> articleSet =
                (Set<Integer>) Optional.ofNullable(session.getAttribute(ARTICLE_SET))
                        .orElse(new HashSet<>());
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET, articleSet);
            // ????????? + 1
            redisUtils.zIncr(ARTICLE_VIEW_COUNT, articleId, 1D);
        }
    }

}
