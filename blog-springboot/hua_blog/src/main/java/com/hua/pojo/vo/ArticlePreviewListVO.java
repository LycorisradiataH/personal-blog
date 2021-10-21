package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章预览列表 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 22:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePreviewListVO {

    /**
     * 文章列表
     */
    private List<ArticlePreviewVO> articlePreviewVOList;

    /**
     * 条件名
     */
    private String name;

}
