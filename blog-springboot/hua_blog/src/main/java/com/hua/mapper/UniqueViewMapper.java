package com.hua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hua.pojo.entity.UniqueView;
import com.hua.pojo.vo.UniqueViewVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/13 13:20
 */
@Repository
public interface UniqueViewMapper extends BaseMapper<UniqueView> {

    /**
     * 查询7天的访问量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 访问量
     */
    List<UniqueViewVO> listUniqueView(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
