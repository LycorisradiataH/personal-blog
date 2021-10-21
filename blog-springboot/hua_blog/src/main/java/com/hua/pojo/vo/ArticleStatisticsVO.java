package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章统计 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 20:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleStatisticsVO {

    /**
     * 日期
     */
    private String date;

    /**
     * 数量
     */
    private Integer count;

}
