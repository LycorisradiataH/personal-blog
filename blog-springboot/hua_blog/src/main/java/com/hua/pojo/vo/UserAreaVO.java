package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户地区 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 19:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAreaVO {

    /**
     * 地区名
     */
    private String name;

    /**
     * 数量
     */
    private Long value;

}
