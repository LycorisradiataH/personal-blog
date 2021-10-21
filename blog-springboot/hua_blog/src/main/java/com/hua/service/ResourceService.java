package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.Resource;
import com.hua.pojo.vo.LabelOptionVO;
import com.hua.pojo.vo.ResourceVO;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.pojo.vo.param.ResourceParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 17:32
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 导入swagger接口
     */
    void importSwagger();

    /**
     * 查看资源列表
     * @param queryParam 条件
     * @return 资源列表
     */
    List<ResourceVO> listResource(QueryParam queryParam);

    /**
     * 添加或修改资源
     * @param resourceParam 资源信息
     */
    void insertOrUpdateResource(ResourceParam resourceParam);

    /**
     * 删除资源
     * @param resourceId 资源id
     */
    void deleteResource(Integer resourceId);

    /**
     * 查看资源选项
     * @return 资源选项
     */
    List<LabelOptionVO> listResourceOption();

}
