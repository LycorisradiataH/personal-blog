package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.mapper.OperationLogMapper;
import com.hua.pojo.entity.OperationLog;
import com.hua.pojo.vo.OperationLogVO;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.OperationLogService;
import com.hua.util.BeanCopyUtils;
import com.hua.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 22:31
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public PageResult<OperationLogVO> listOperationLog(QueryParam queryParam) {
        Page<OperationLog> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        // 查询日志列表
        Page<OperationLog> operationLogPage =
                operationLogMapper.selectPage(page, new LambdaQueryWrapper<OperationLog>()
                        .like(StringUtils.isNotBlank(queryParam.getKeywords()),
                                OperationLog::getOptModule, queryParam.getKeywords())
                        .or()
                        .like(StringUtils.isNotBlank(queryParam.getKeywords()),
                                OperationLog::getOptDesc, queryParam.getKeywords())
                        .orderByDesc(OperationLog::getId));
        List<OperationLogVO> operationLogVOList =
                BeanCopyUtils.copyList(operationLogPage.getRecords(), OperationLogVO.class);
        return new PageResult<>(operationLogVOList, (int) operationLogPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteLog(List<Integer> logIdList) {
        operationLogMapper.deleteBatchIds(logIdList);
    }
}
