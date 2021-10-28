package com.hua.mapper;

import com.hua.pojo.vo.ArticleSearchVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/21 19:52
 */
@Repository
public interface ElasticsearchMapper extends ElasticsearchRepository<ArticleSearchVO, Integer> {
}
