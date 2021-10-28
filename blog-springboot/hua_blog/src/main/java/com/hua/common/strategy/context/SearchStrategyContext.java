package com.hua.common.strategy.context;

import com.hua.common.strategy.SearchStrategy;
import com.hua.pojo.vo.ArticleSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.hua.common.enums.SearchModeEnum.getStrategy;

/**
 * 搜索策略上下文
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/21 17:27
 */
@Service
public class SearchStrategyContext {

    /**
     * 搜索模式
     */
    @Value("${search.mode}")
    private String searchMode;

    @Autowired
    private Map<String, SearchStrategy> searchStrategyMap;

    public List<ArticleSearchVO> executeSearchStrategy(String keywords) {
        return searchStrategyMap.get(getStrategy(searchMode)).searchArticle(keywords);
    }

}
