package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.Tag;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.TagVO;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.pojo.vo.param.TagParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 18:33
 */
public interface TagService extends IService<Tag> {

    /**
     * 查询标签列表
     * @return 标签列表
     */
    PageResult<TagVO> listTag();

    /**
     * 查询后台标签列表
     * @param queryParam 条件
     * @return 标签列表
     */
    Result listBackTag(QueryParam queryParam);

    /**
     * 搜索标签
     * @param queryParam 条件
     * @return 标签列表
     */
    List<TagVO> listTagBySearch(QueryParam queryParam);

    /**
     * 添加或修改标签
     * @param tagParam 标签
     */
    void insertOrUpdateTag(TagParam tagParam);

    /**
     * 根据id集合删除标签
     * @param tagIdList id集合
     */
    void deleteTag(List<Integer> tagIdList);

}
