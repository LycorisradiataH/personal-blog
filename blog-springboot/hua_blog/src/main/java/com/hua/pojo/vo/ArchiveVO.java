package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 归档文章 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 22:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 发表时间
     */
    private LocalDateTime gmtCreate;

}
