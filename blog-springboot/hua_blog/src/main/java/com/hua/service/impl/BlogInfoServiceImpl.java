package com.hua.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hua.mapper.*;
import com.hua.pojo.entity.Article;
import com.hua.pojo.entity.WebsiteConfig;
import com.hua.pojo.vo.*;
import com.hua.pojo.vo.param.BlogInfoParam;
import com.hua.service.BlogInfoService;
import com.hua.service.PageService;
import com.hua.service.UniqueViewService;
import com.hua.util.BeanCopyUtils;
import com.hua.util.IpUtils;
import com.hua.util.RedisUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.*;
import static com.hua.common.constant.RedisPrefixConst.*;
import static com.hua.common.enums.ArticleStatusEnum.PUBLIC;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 17:27
 */
@Service
public class BlogInfoServiceImpl implements BlogInfoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private WebsiteConfigMapper websiteConfigMapper;

    @Autowired
    private UniqueViewService uniqueViewService;

    @Autowired
    private PageService pageService;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;

    @Override
    public BlogHomeInfoVO getBlogInfo() {
        // ??????????????????
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));
        // ??????????????????
        Integer categoryCount = categoryMapper.selectCount(null);
        // ??????????????????
        Integer tagCount = tagMapper.selectCount(null);
        // ???????????????
        Object count = redisUtils.get(BLOG_VIEW_COUNT);
        String viewCount = String.valueOf(Optional.ofNullable(count).orElse(0));
        // ??????????????????
        WebsiteConfigVO websiteConfigVO = this.getWebsiteConfig();
        // ??????????????????
        List<PageVO> pageVOList = pageService.listPage();
        // ????????????
        return BlogHomeInfoVO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewCount(viewCount)
                .websiteConfig(websiteConfigVO)
                .pageList(pageVOList)
                .build();
    }

    @Override
    public String getAboutMe() {
        Object value = redisUtils.get(ABOUT);
        return Objects.nonNull(value) ? String.valueOf(value) : "";
    }

    @Override
    public BlogBackInfoVO getBlogBackInfo() {
        // ???????????????
        Object count = redisUtils.get(BLOG_VIEW_COUNT);
        Integer viewCount = Integer.parseInt(String.valueOf(Optional.ofNullable(count).orElse(0)));
        // ???????????????
        Integer messageCount = messageMapper.selectCount(null);
        // ???????????????
        Integer userCount = userMapper.selectCount(null);
        // ???????????????
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE));
        // ?????????????????????
        List<UniqueViewVO> uniqueViewVOList = uniqueViewService.listUniqueView();
        // ??????????????????
        List<ArticleStatisticsVO> articleStatisticsVOList = articleMapper.listArticleStatistics();
        // ??????????????????
        List<CategoryVO> categoryVOList = categoryMapper.listCategory();
        // ??????????????????
        List<TagVO> tagVOList = BeanCopyUtils.copyList(tagMapper.selectList(null), TagVO.class);
        // ??????redis????????????5?????????
        Map<Object, Double> articleMap = redisUtils.zReverseRangeWithScore(ARTICLE_VIEW_COUNT, 0, 4);
        BlogBackInfoVO blogBackInfoVO = BlogBackInfoVO.builder()
                .viewCount(viewCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .articleStatisticsList(articleStatisticsVOList)
                .categoryVOList(categoryVOList)
                .uniqueViewVOList(uniqueViewVOList)
                .tagVOList(tagVOList)
                .build();
        if (CollectionUtils.isNotEmpty(articleMap)) {
            // ??????????????????
            List<ArticleRankVO> articleRankVOList = listArticleRank(articleMap);
            blogBackInfoVO.setArticleRankVOList(articleRankVOList);
        }
        return blogBackInfoVO;
    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        WebsiteConfigVO websiteConfigVO;
        // ??????????????????
        Object websiteConfig = redisUtils.get(WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        } else {
            // ?????????????????????
            String config = websiteConfigMapper.selectById(1).getConfig();
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO.class);
            redisUtils.set(WEBSITE_CONFIG, config);
        }
        return websiteConfigVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        // ??????????????????
        WebsiteConfig websiteConfig = WebsiteConfig.builder()
                .id(1)
                .config(JSON.toJSONString(websiteConfigVO))
                .build();
        websiteConfigMapper.updateById(websiteConfig);
        // ????????????
        redisUtils.del(WEBSITE_CONFIG);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAboutMe(BlogInfoParam blogInfoParam) {
        redisUtils.set(ABOUT, blogInfoParam.getAboutContent());
    }

    @Override
    public void report() {
        // ??????ip
        String ipAddr = IpUtils.getIpAddr(request);
        // ??????????????????
        UserAgent userAgent = IpUtils.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // ????????????????????????
        String uuid = ipAddr + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // ??????????????????
        if (!redisUtils.sHasKey(UNIQUE_VISITOR, md5)) {
            // ????????????????????????
            String ipSource = IpUtils.getIpSource(ipAddr);
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(PROVINCE, "")
                        .replaceAll(CITY, "");
                redisUtils.hIncr(VISITOR_AREA, ipSource, 1L);
            } else {
                redisUtils.hIncr(VISITOR_AREA, UNKNOWN, 1L);
            }
            // ?????????+1
            redisUtils.incr(BLOG_VIEW_COUNT, 1);
            // ??????????????????
            redisUtils.sSet(UNIQUE_VISITOR, md5);
        }
    }

    /**
     * ??????????????????
     * @param articleMap ????????????
     * @return ????????????
     */
    private List<ArticleRankVO> listArticleRank(Map<Object, Double> articleMap) {
        // ????????????id
        List<Integer> articleIdList = new ArrayList<>();
        articleMap.forEach((key, value) -> articleIdList.add((Integer) key));
        // ??????????????????
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle)
                .in(Article::getId, articleIdList));
        return articleList.stream().map(article -> ArticleRankVO.builder()
                .articleTitle(article.getArticleTitle())
                .viewCount(articleMap.get(article.getId()).intValue())
                .build())
                .sorted(Comparator.comparingInt(ArticleRankVO::getViewCount).reversed())
                .collect(Collectors.toList());
    }

}
