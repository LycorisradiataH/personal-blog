package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章排行 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 20:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRankVO {

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 浏览量
     */
    private Integer viewCount;

}
