package com.hua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hua.pojo.entity.Tag;
import com.hua.pojo.vo.TagBackVO;
import com.hua.pojo.vo.param.QueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 17:38
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章id查询标签名
     * @param articleId 文章id
     * @return 标签名列表
     */
    List<String> listTagNameByArticleId(@Param("articleId") Integer articleId);

    /**
     * 查询后台标签列表
     * @param current 页码
     * @param size 页大小
     * @param queryParam 条件
     * @return 后台标签列表
     */
    List<TagBackVO> listBackTag(@Param("current") Long current, @Param("size") Long size, @Param("queryParam") QueryParam queryParam);
}
