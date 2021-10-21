package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.Page;
import com.hua.pojo.vo.PageVO;
import com.hua.pojo.vo.param.PageParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 21:29
 */
public interface PageService extends IService<Page> {

    /**
     * 获取页面列表
     * @return 页面列表
     */
    List<PageVO> listPage();

    /**
     * 添加或更新页面
     * @param pageParam 页面信息
     */
    void insertOrUpdatePage(PageParam pageParam);

    /**
     * 删除页面
     * @param pageId 页面id
     */
    void deletePage(Integer pageId);
}
