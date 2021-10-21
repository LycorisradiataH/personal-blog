package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 标签选项 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 15:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelOptionVO {

    /**
     * 选项id
     */
    private Integer id;

    /**
     * 选项名
     */
    private String label;

    /**
     * 子选项
     */
    private List<LabelOptionVO> children;

}
