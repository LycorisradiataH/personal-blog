package com.hua.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.mapper.UniqueViewMapper;
import com.hua.pojo.entity.UniqueView;
import com.hua.pojo.vo.UniqueViewVO;
import com.hua.service.UniqueViewService;
import com.hua.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.hua.common.constant.RedisPrefixConst.UNIQUE_VISITOR;
import static com.hua.common.constant.RedisPrefixConst.VISITOR_AREA;
import static com.hua.common.enums.ZoneEnum.SHANGHAI;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/13 13:21
 */
@Service
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {

    @Autowired
    private UniqueViewMapper uniqueViewMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<UniqueViewVO> listUniqueView() {
        DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endTime = DateUtil.endOfDay(new Date());
        return uniqueViewMapper.listUniqueView(startTime, endTime);
    }

    @Scheduled(cron = " 0 0 0 * * ?", zone = "Asia/Shanghai")
    public void saveUniqueView() {
        // 获取每天用户量
        Long count = redisUtils.sSize(UNIQUE_VISITOR);
        // 获取昨天日期插入数据
        UniqueView uniqueView = UniqueView.builder()
                .gmtCreate(LocalDateTimeUtil.offset(LocalDateTime
                                .now(ZoneId.of(SHANGHAI.getZone())), -1, ChronoUnit.DAYS))
                .viewCount(Optional.of(count.intValue()).orElse(0))
                .build();
        uniqueViewMapper.insert(uniqueView);
    }

    @Scheduled(cron = " 0 1 0 * * ?", zone = "Asia/Shanghai")
    public void clear() {
        // 清空redis访客记录
        redisUtils.del(UNIQUE_VISITOR);
        // 清空redis游客区域统计
        redisUtils.del(VISITOR_AREA);
    }

}
