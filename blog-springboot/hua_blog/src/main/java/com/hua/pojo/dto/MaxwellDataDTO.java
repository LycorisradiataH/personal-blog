package com.hua.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * maxwell 监听数据
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/21 19:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaxwellDataDTO {

    /**
     * 数据库
     */
    private String database;

    /**
     * xid
     */
    private Integer xid;

    /**
     * 数据
     */
    private Map<String, Object> data;

    /**
     * 是否提交
     */
    private Boolean commit;

    /**
     * 类型
     */
    private String type;

    /**
     * 表
     */
    private String table;

    /**
     * ts
     */
    private Integer ts;

}
