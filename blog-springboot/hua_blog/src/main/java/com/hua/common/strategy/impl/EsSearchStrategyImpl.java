package com.hua.common.strategy.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hua.common.strategy.SearchStrategy;
import com.hua.pojo.vo.ArticleSearchVO;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.*;
import static com.hua.common.enums.ArticleStatusEnum.PUBLIC;

/**
 * es 搜索模式
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/21 18:14
 */
@Log4j2
@Service
public class EsSearchStrategyImpl implements SearchStrategy {

    @Autowired
    private ElasticsearchRestTemplate esRestTemplate;

    @Override
    public List<ArticleSearchVO> searchArticle(String keywords) {
        return search(buildQuery(keywords));
    }

    /**
     * 搜索文章条件构造
     * @param keywords 关键字
     * @return 条件构造器
     */
    private NativeSearchQueryBuilder buildQuery(String keywords) {
        // 条件构造器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 根据关键字搜索文章标题或内容
        boolQueryBuilder.must(QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery("articleTitle", keywords))
                    .should(QueryBuilders.matchQuery("articleContent", keywords)))
                .must(QueryBuilders.termQuery("isDelete", FALSE))
                .must(QueryBuilders.termQuery("status", PUBLIC.getStatus()));
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        return nativeSearchQueryBuilder;
    }

    /**
     * 文章搜索结果高亮
     * @param buildQuery es条件构造器
     * @return 搜索结果
     */
    private List<ArticleSearchVO> search(NativeSearchQueryBuilder buildQuery) {
        // 添加文章标题高亮
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("articleTitle");
        titleField.preTags(PRE_TAG);
        titleField.postTags(POST_TAG);
        // 添加文章内容高亮
        HighlightBuilder.Field contentField = new HighlightBuilder.Field("articleContent");
        contentField.preTags(PRE_TAG);
        contentField.postTags(POST_TAG);
        contentField.fragmentSize(200);
        buildQuery.withHighlightFields(titleField, contentField);
        // 搜索
        try {
            SearchHits<ArticleSearchVO> searchHits =
                    esRestTemplate.search(buildQuery.build(), ArticleSearchVO.class);
            return searchHits.getSearchHits().stream().map(hit -> {
                ArticleSearchVO articleSearchVO = hit.getContent();
                // 获取文章标题高亮数据
                List<String> titleHighlightList = hit.getHighlightFields().get("articleTitle");
                if (CollectionUtils.isNotEmpty(titleHighlightList)) {
                    // 替换文章标题数据
                    articleSearchVO.setArticleTitle(titleHighlightList.get(0));
                }
                // 获取文章内容高亮数据
                List<String> contentHighlightList = hit.getHighlightFields().get("articleContent");
                if (CollectionUtils.isNotEmpty(contentHighlightList)) {
                    // 替换文章内容数据
                    articleSearchVO.setArticleContent(
                            contentHighlightList.get(contentHighlightList.size() - 1));
                }
                return articleSearchVO;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

}
