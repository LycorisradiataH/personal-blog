package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 访问量 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 20:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueViewVO {

    /**
     * 日期
     */
    private String day;

    /**
     * 访问量
     */
    private Integer viewCount;

}
