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
        // 查询文章数量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));
        // 查询分类数量
        Integer categoryCount = categoryMapper.selectCount(null);
        // 查询标签数量
        Integer tagCount = tagMapper.selectCount(null);
        // 查询访问量
        Object count = redisUtils.get(BLOG_VIEW_COUNT);
        String viewCount = String.valueOf(Optional.ofNullable(count).orElse(0));
        // 查询网站配置
        WebsiteConfigVO websiteConfigVO = this.getWebsiteConfig();
        // 查询页面图片
        List<PageVO> pageVOList = pageService.listPage();
        // 封装数据
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
        // 查询访问量
        Object count = redisUtils.get(BLOG_VIEW_COUNT);
        Integer viewCount = Integer.parseInt(String.valueOf(Optional.ofNullable(count).orElse(0)));
        // 查询留言量
        Integer messageCount = messageMapper.selectCount(null);
        // 查询用户量
        Integer userCount = userMapper.selectCount(null);
        // 查询文章量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE));
        // 查询一周访问量
        List<UniqueViewVO> uniqueViewVOList = uniqueViewService.listUniqueView();
        // 查询文章统计
        List<ArticleStatisticsVO> articleStatisticsVOList = articleMapper.listArticleStatistics();
        // 查询分类数据
        List<CategoryVO> categoryVOList = categoryMapper.listCategory();
        // 查询标签数据
        List<TagVO> tagVOList = BeanCopyUtils.copyList(tagMapper.selectList(null), TagVO.class);
        // 查询redis访问量前5的文章
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
            // 查询文章排行
            List<ArticleRankVO> articleRankVOList = listArticleRank(articleMap);
            blogBackInfoVO.setArticleRankVOList(articleRankVOList);
        }
        return blogBackInfoVO;
    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        WebsiteConfigVO websiteConfigVO;
        // 获取缓存数据
        Object websiteConfig = redisUtils.get(WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        } else {
            // 从数据库中读取
            String config = websiteConfigMapper.selectById(1).getConfig();
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO.class);
            redisUtils.set(WEBSITE_CONFIG, config);
        }
        return websiteConfigVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        // 修改网站配置
        WebsiteConfig websiteConfig = WebsiteConfig.builder()
                .id(1)
                .config(JSON.toJSONString(websiteConfigVO))
                .build();
        websiteConfigMapper.updateById(websiteConfig);
        // 删除缓存
        redisUtils.del(WEBSITE_CONFIG);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAboutMe(BlogInfoParam blogInfoParam) {
        redisUtils.set(ABOUT, blogInfoParam.getAboutContent());
    }

    @Override
    public void report() {
        // 获取ip
        String ipAddr = IpUtils.getIpAddr(request);
        // 获取访问设备
        UserAgent userAgent = IpUtils.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 生成唯一用户标识
        String uuid = ipAddr + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisUtils.sHasKey(UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtils.getIpSource(ipAddr);
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(PROVINCE, "")
                        .replaceAll(CITY, "");
                redisUtils.hIncr(VISITOR_AREA, ipSource, 1L);
            } else {
                redisUtils.hIncr(VISITOR_AREA, UNKNOWN, 1L);
            }
            // 访问量+1
            redisUtils.incr(BLOG_VIEW_COUNT, 1);
            // 保存唯一标识
            redisUtils.sSet(UNIQUE_VISITOR, md5);
        }
    }

    /**
     * 查询文章排行
     * @param articleMap 文章信息
     * @return 文章排行
     */
    private List<ArticleRankVO> listArticleRank(Map<Object, Double> articleMap) {
        // 提取文章id
        List<Integer> articleIdList = new ArrayList<>();
        articleMap.forEach((key, value) -> articleIdList.add((Integer) key));
        // 查询文章详情
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
