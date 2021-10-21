package com.hua.controller;

import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.OperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 日志模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 22:22
 */
@Api(tags = "日志模块")
@RestController
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查看操作日志
     * @param queryParam 条件
     * @return 日志列表
     */
    @ApiOperation(value = "查看操作日志")
    @GetMapping("/admin/log")
    public Result listLog(QueryParam queryParam) {
        return Result.success(operationLogService.listOperationLog(queryParam));
    }

    /**
     * 删除操作日志
     * @param logIdList 日志id列表
     * @return {@link Result}
     */
    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/admin/log")
    public Result deleteLog(@RequestBody List<Integer> logIdList) {
        operationLogService.deleteLog(logIdList);
        return Result.success();
    }

}
