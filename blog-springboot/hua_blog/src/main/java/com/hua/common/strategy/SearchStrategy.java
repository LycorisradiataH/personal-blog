package com.hua.common.strategy;

import com.hua.pojo.vo.ArticleSearchVO;

import java.util.List;

/**
 * 搜索策略
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/21 17:26
 */
public interface SearchStrategy {

    /**
     * 搜索文章
     * @param keywords 关键字
     * @return 文章列表
     */
    List<ArticleSearchVO> searchArticle(String keywords);

}
