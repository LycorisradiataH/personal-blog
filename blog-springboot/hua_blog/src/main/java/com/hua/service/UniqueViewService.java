package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.UniqueView;
import com.hua.pojo.vo.UniqueViewVO;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/13 13:20
 */
public interface UniqueViewService extends IService<UniqueView> {

    /**
     * 获取7天访问量统计
     * @return 访问量
     */
    List<UniqueViewVO> listUniqueView();

}
