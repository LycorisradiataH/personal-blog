package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 后台标签 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 17:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagBackVO {

    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 文章量
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

}
