package com.hua.common.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hua.common.strategy.SearchStrategy;
import com.hua.mapper.ArticleMapper;
import com.hua.pojo.entity.Article;
import com.hua.pojo.vo.ArticleSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.*;
import static com.hua.common.enums.ArticleStatusEnum.PUBLIC;

/**
 * mysql 搜索策略
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/21 17:30
 */
@Service
public class MySqlSearchStrategyImpl implements SearchStrategy {

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public List<ArticleSearchVO> searchArticle(String keywords) {
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
}
