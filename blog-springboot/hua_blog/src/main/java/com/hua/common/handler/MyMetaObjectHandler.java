package com.hua.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.hua.common.enums.ZoneEnum.SHANGHAI;

/**
 * mybatis plus 自动填充字段
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/17 17:24
 */
@Log4j2
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill!!!");
        this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime.class,
                LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())));
        this.strictInsertFill(metaObject, "gmtModified", LocalDateTime.class,
                LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill!!!");
        this.strictUpdateFill(metaObject, "gmtModified", LocalDateTime.class,
                LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())));
    }

}
