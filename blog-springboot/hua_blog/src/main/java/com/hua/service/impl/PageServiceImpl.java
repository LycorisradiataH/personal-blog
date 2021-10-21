package com.hua.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.mapper.PageMapper;
import com.hua.pojo.entity.Page;
import com.hua.pojo.vo.PageVO;
import com.hua.pojo.vo.param.PageParam;
import com.hua.service.PageService;
import com.hua.util.BeanCopyUtils;
import com.hua.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.hua.common.constant.RedisPrefixConst.PAGE_COVER;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 21:29
 */
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<PageVO> listPage() {
        List<PageVO> pageVOList;
        // 查找缓存信息，不存在则从数据库中读取，更新缓存
        Object pageList = redisUtils.get(PAGE_COVER);
        if (Objects.nonNull(pageList)) {
            pageVOList = JSON.parseObject(String.valueOf(pageList), List.class);
        } else {
            pageVOList = BeanCopyUtils.copyList(pageMapper.selectList(null), PageVO.class);
            redisUtils.set(PAGE_COVER, JSON.toJSONString(pageVOList));
        }
        return pageVOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrUpdatePage(PageParam pageParam) {
        Page page = BeanCopyUtils.copyObject(pageParam, Page.class);
        this.saveOrUpdate(page);
        // 删除缓存
        redisUtils.del(PAGE_COVER);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePage(Integer pageId) {
        pageMapper.deleteById(pageId);
        // 删除缓存
        redisUtils.del(PAGE_COVER);
    }
}
