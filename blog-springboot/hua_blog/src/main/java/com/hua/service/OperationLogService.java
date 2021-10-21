package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.OperationLog;
import com.hua.pojo.vo.OperationLogVO;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.param.QueryParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 22:30
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 查看操作日志列表
     * @param queryParam 条件
     * @return 日志列表
     */
    PageResult<OperationLogVO> listOperationLog(QueryParam queryParam);

    /**
     * 删除日志
     * @param logIdList 日志id列表
     */
    void deleteLog(List<Integer> logIdList);
}
